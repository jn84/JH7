package my_networked_game;

import java.io.Serializable;
import java.time.Year;
import java.util.ArrayList;

import javax.xml.transform.Templates;

import gameNet.GameControl;
import gameNet.GameNet_CoreGame;
import my_networked_game.Enums.MyGameInputType;
import my_networked_game.Enums.MyGameOutputType;
import my_networked_game.Enums.ScoreTypes;
import my_networked_game.HelperClasses.DiceObj;
import my_networked_game.HelperClasses.Player;
import my_networked_game.HelperClasses.ScoreSheetBuilder;
import my_networked_game.HelperClasses.SelectableTextFieldState;

public class MyGame extends GameNet_CoreGame implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final int NUMBER_OF_ROUNDS = 12;

	private ArrayList<Player> playerList = new ArrayList<Player>();
	
	private Player currentPlayer = null;
	
	private GameControl gameControl = null;
	
	private MyGameOutput lastOutput = null;
	
	private int roundCounter = 0,
				rollCounter = 0;
	
	private boolean isGameEnded = false,
					isGameInProgress = false;
	
	public MyGame()
	{
		
	}
	
	/*
	 * Process takes one input of type Object. 
	 * Note that this input will actually be your game's MyGameInput class. 
	 * The method process returns a class of type Object which will actually be your MyGameOutput class. 
	 * Note that the GameNet_CoreGame abstract class contains your Server Code.
	 */
	@Override 
	public Object process(Object inputObj)
	{
        MyGameInput myGameInput = (MyGameInput)inputObj;
        
        MyGameOutput myGameOutput = null;
        
        Player player = null,
        	   quittingPlayer = null;
        
        DiceSet diceSet = null;
        
        //
        //
        //
        //
        //
        //
        //
        // nextPlayer has an issue when the active player quits
        // nextPlayer might not always work.. nextPlayer might sometimes skip valid players
        //		hard to reproduce!!!!!!!!!!!!!!!!!
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        
        System.out.println("Doing: " + myGameInput.getInputType().toString());
        switch (myGameInput.getInputType())
        {
        case REGISTER_PLAYER:
        	player = myGameInput.getOriginatingPlayer(); 
        	player.setSpectator(isGameInProgress);
        	
        	System.out.println("Player [" + myGameInput.getUsername() + "] registered with ID [" + myGameInput.getPlayerID() + "]");
        	
        	// Set the first player added as the host
        	// Only the host can start the game
        	if (playerList.isEmpty())
        		player.setIsHost(true);
       		playerList.add(player);
       		
       		
        	if (isGameInProgress)
        		gameControl.putMsgs(new MyGameOutput(lastOutput.getActivePlayer(), playerList, lastOutput.getDice()));
        	
        	// Notify all players
        	myGameOutput = new MyGameOutput(player, MyGameOutputType.PLAYER_REGISTERED);
        	gameControl.putMsgs(new MyGameOutput(playerList, MyGameOutputType.UPDATE_PLAYERS));
        	if (!isGameInProgress)
        		myGameOutput.setMessage(player.getName() + " joined the game.");
        	else
        		myGameOutput.setMessage(player.getName() + " joined the game as a spectator.");
        	break;
        	
        // A player left the game. Remove them from the player list and move to the next player.
        case UNREGISTER_PLAYER:
        	

        	
        	player = myGameInput.getOriginatingPlayer();
        	quittingPlayer = new Player(player);
        	
        	boolean didActivePlayerLeave = currentPlayer.equals(player) && isGameInProgress;
        	
        	if (didActivePlayerLeave)
        	{
        		if (currentPlayer != null)
        		{
        			// NPE when the active player leaves!
//        			if (currentPlayer == null)
//        				System.out.println("currentPlayer is NULL");
        			while (currentPlayer.isSpectator())
        				currentPlayer = nextPlayer();
        		}
        		else 
        			currentPlayer = nextPlayer();
        		gameControl.putMsgs(generateNewTurn(myGameInput));
        	}
        	
        	System.out.println("Player [" + myGameInput.getUsername() + "] left the game");
        	playerList.remove(player);
        	
        	// update the other players that this player left
        	myGameOutput = new MyGameOutput(quittingPlayer, MyGameOutputType.PLAYER_UNREGISTERED);
        	myGameOutput.setMessage(quittingPlayer.getName() + " left the game.");
        	break;

        case GENERATE_NEW_TURN:
        	//	Create a copy so we don't modify playerList until the player submits score
        	player = new Player(currentPlayer);
        	
        	diceSet = new DiceSet();
        	
        	rollCounter = 0;
        	// We use currentPlayer because it's a new turn
        	ScoreSheetBuilder.UpdatePlayerScoreSheet(diceSet, player, false);
        	
        	myGameOutput = new MyGameOutput(player, playerList, diceSet, true);
        	myGameOutput.setMessage("It's now " + player.getName() + "'s turn.");
        	lastOutput = myGameOutput;
        	break;
        	
        case GAME_BEGIN:
        	isGameInProgress = true;
        	currentPlayer = playerList.get(0);
        	player = new Player(currentPlayer);
        	
        	diceSet = new DiceSet();
        	
        	rollCounter = 0;
        	
        	ScoreSheetBuilder.UpdatePlayerScoreSheet(diceSet, player, false);
        	
        	myGameOutput = new MyGameOutput(player, playerList, diceSet);
        	myGameOutput.setMessage("The game has begun!\nIt's now " + player.getName() + "'s turn!");
        	lastOutput = myGameOutput;
        	break;
        	
        case PLAYER_ROLL:
        	int holdCount = 0;
        	
        	player = myGameInput.getOriginatingPlayer();
        	diceSet = myGameInput.getCurrentDiceSet();
        	
        	for (DiceObj elem : diceSet.getDice())
        		if (elem.isHeld())
        			holdCount++;
        	
        	// roll the unheld dice
        	diceSet.roll();
        	
        	// process valid scoring plays
        	// apply the valid scoring plays to the player's score sheet
        	
        	ScoreSheetBuilder.UpdatePlayerScoreSheet(diceSet, player, false);

        	// increment the roll counter
        	rollCounter++;
        	
        	// build the output object
        	myGameOutput = new MyGameOutput(player, playerList, diceSet, rollCounter != 2);
        	if (rollCounter != 2)
        		myGameOutput.setMessage(player.getName() + " held " + holdCount + " die and rolled.");
        	else 
        		myGameOutput.setMessage(player.getName() + " held " + holdCount + " die for their final roll.");
        	lastOutput = myGameOutput;
        	break;
        	
        case PLAYER_SUBMIT:
        	player = myGameInput.getOriginatingPlayer();
        	diceSet = myGameInput.getCurrentDiceSet();
        	
        	String scoreValue = "",
        		   scoreType = "", 
        		   msg = "";
        	
        	boolean wasCrossed = false;

        	// In the case where a player uses a bonus jahtzee, they need to cross out another box afterwards
        	if (player.getScoreData().get(ScoreTypes.JAHTZEE_BONUS_1.ordinal()).isSelected ||
        		player.getScoreData().get(ScoreTypes.JAHTZEE_BONUS_2.ordinal()).isSelected ||
        		player.getScoreData().get(ScoreTypes.JAHTZEE_BONUS_3.ordinal()).isSelected)
        	{
        		ScoreSheetBuilder.FinalizeScore(player);
        		ScoreSheetBuilder.UpdatePlayerScoreSheet(diceSet, player, true);
        		myGameOutput = new MyGameOutput(player, playerList, diceSet, false);
        		myGameOutput.setMessage(player.getName() + " has scored a bonus Jahtzee for 100 points!");
        		break;
        	}
        	
        	for (int i = 0; i < player.getScoreData().size(); i++)
        	{
        		if (player.getScoreData().get(i).isSelected)
        		{
        			scoreValue = player.getScoreData().get(i).fieldValue;
        			scoreType = ScoreTypes.values()[i].friendlyName;
        			
        			try
        			{
        				Integer.parseInt(scoreValue);        		
        			}
        			catch (NumberFormatException e)
        			{
        				wasCrossed = true;
        			}
        		}
        	}
        	
        	ScoreSheetBuilder.FinalizeScore(player);
        	
        	playerList.set(playerList.indexOf(player), player);

        	// go to the next player: currentPlayer = nextPlayer()
        	// generate a new set of dice
        	// reset the roll counter
        	currentPlayer = nextPlayer();
   			while (currentPlayer.isSpectator())
   				currentPlayer = nextPlayer();
        	
        	if (wasCrossed)
        		msg = player.getName() + " crossed out " + scoreType + "!\n";
        	else
        		msg = player.getName() + " scored " + scoreValue + " points in " + scoreType + "!\n";
				    
        	
        	if (isGameEnded)
        	{
        		myGameOutput = new MyGameOutput(playerList, MyGameOutputType.GAME_OVER);
        		myGameOutput.setMessage(msg + "The game has ended!");
        	}
        	else
        	{
        	
        		player = new Player(currentPlayer);
        		rollCounter = 0;
        		diceSet = new DiceSet();

        		// 	process the valid scoring plays against currentPlayer's score sheet
        		ScoreSheetBuilder.UpdatePlayerScoreSheet(diceSet, player, false);

        		// build the output object
        		myGameOutput = new MyGameOutput(player, playerList, diceSet, true);
        		myGameOutput.setMessage(msg + "It's now " + player.getName() + "'s turn!");
        	}
        	lastOutput = myGameOutput;
        	break;
        	
        case PLAYER_SKIP:
        	player = myGameInput.getOriginatingPlayer();
        	diceSet = myGameInput.getCurrentDiceSet();
        	
        	// player skipped
        	// Need to generate output to so that the player can select a field to skip
        	ScoreSheetBuilder.UpdatePlayerScoreSheet(diceSet, player, true);

        	myGameOutput = new MyGameOutput(player, playerList, diceSet, false);
        	myGameOutput.setMessage(player.getName() + " skipped their turn! Please wait while they choose which box to cross out.");
        	lastOutput = myGameOutput;
        	break;
        	
        case MESSAGE:
        	myGameOutput = new MyGameOutput(myGameInput.getOriginatingPlayerName(), myGameInput.getMessage());
        	break;
        	
        case UPDATE_PLAYERS:
        	myGameOutput = new MyGameOutput(playerList, MyGameOutputType.UPDATE_PLAYERS);
        	break;

        case DICE_HOLD_CHANGED:
        	myGameOutput = new MyGameOutput(myGameInput.getDiceHeldChangedIndex(), myGameInput.isDiceObjHeld());
        	break;
        	
		default:
			break; 	
        }
        return myGameOutput;
	}
	
	private MyGameOutput generateNewTurn(MyGameInput input)
	{
		MyGameOutput myGameOutput = null; 
		Player player = null;
		DiceSet diceSet = null;
		
    	player = new Player(currentPlayer);
    	
    	diceSet = new DiceSet();
    	
    	rollCounter = 0;
    	// We use currentPlayer because it's a new turn
    	ScoreSheetBuilder.UpdatePlayerScoreSheet(diceSet, player, false);
    	
    	myGameOutput = new MyGameOutput(player, playerList, diceSet, true);
    	myGameOutput.setMessage("It's now " + player.getName() + "'s turn.");
    	return myGameOutput;
	}
	
	private Player nextPlayer()
	{
		int i = 0;
		if (isGameEnded)
			return null;
		
		// If the current player is the last player in the list
		// We will increment the round number and check if we've reached the end of the game
		if (currentPlayer.equals(playerList.get(playerList.size() - 1)))
		{
			if (++roundCounter > NUMBER_OF_ROUNDS)
			{
				isGameEnded = true;
				return null;
			}
			// Return the first player
			for (i = 0; i < playerList.size(); i++)
				if (!playerList.get(i).isSpectator())
					break;
			// first non-spectator player
			return playerList.get(i);
		}
		// Ran into a logic error bug, so check if list size is 1
		if (playerList.size() == 1)
			{
			for (i = 0; i < playerList.size(); i++)
				if (!playerList.get(i).isSpectator())
					break;
			// first non-spectator player
			return playerList.get(i);
			}
		else
			return playerList.get(playerList.lastIndexOf(currentPlayer) + 1);
	}
	
	@Override
	public void startGame (GameControl gameControl)
	{
		this.gameControl = gameControl;
	}
}
