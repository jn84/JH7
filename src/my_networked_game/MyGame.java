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
import my_networked_game.HelperClasses.Player;
import my_networked_game.HelperClasses.ScoreSheetBuilder;

public class MyGame extends GameNet_CoreGame implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final int NUMBER_OF_ROUNDS = 12;

	private ArrayList<Player> playerList = new ArrayList<Player>();
	
	private Player currentPlayer = null;
	
	private int roundCounter = 0;
	
	private boolean isGameEnded = false;
	
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
        
        Player player = null;
        
        DiceSet diceSet = null;
        
        int rollCounter = 0;
        
        
        
        switch (myGameInput.getInputType())
        {
        case REGISTER_PLAYER:
        	player = myGameInput.getOriginatingPlayer(); 
        	
        	System.out.println("Player [" + myGameInput.getUsername() + "] registered with ID [" + myGameInput.getPlayerID() + "]");
        	playerList.add(player);
        	
        	// Notify all players
        	myGameOutput = new MyGameOutput(player, MyGameOutputType.PLAYER_REGISTERED);
        	process(new MyGameOutput(playerList, MyGameOutputType.UPDATE_PLAYERS));
        	myGameOutput.setMessage(player.getName() + " joined the game.");
        	break;
        	
        // A player left the game. Remove them from the player list and move to the next player.
        case UNREGISTER_PLAYER:
        	player = myGameInput.getOriginatingPlayer();
        	
        	if (currentPlayer.equals(player))
        	{
        		currentPlayer = nextPlayer();
        		process(new MyGameInput(MyGameInputType.GENERATE_NEW_TURN));
        	}
        	
        	System.out.println("Player [" + myGameInput.getUsername() + "] left the game");
        	playerList.remove(player);
        	
        	// update the other players that this player left
        	myGameOutput = new MyGameOutput(player, MyGameOutputType.PLAYER_UNREGISTERED);
        	process(new MyGameOutput(playerList, MyGameOutputType.UPDATE_PLAYERS));
        	myGameOutput.setMessage(player.getName() + " left the game.");
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
        	break;
        	
        case GAME_BEGIN:
        	currentPlayer = playerList.get(0);
        	player = new Player(currentPlayer);
        	
        	System.out.println(player.getScoreData().isEmpty() ? "GAME_BEGIN: score data is empty" : "GAME_BEGIN: score data is NOT empty");
        	
        	diceSet = new DiceSet();
        	
        	rollCounter = 0;
        	
        	ScoreSheetBuilder.UpdatePlayerScoreSheet(diceSet, player, false);
        	
        	myGameOutput = new MyGameOutput(player, playerList, diceSet);
        	break;
        	
        case PLAYER_ROLL:
        	player = myGameInput.getOriginatingPlayer();
        	diceSet = myGameInput.getCurrentDiceSet();
        	
        	// roll the unheld dice
        	diceSet.roll();
        	
        	// process valid scoring plays
        	// apply the valid scoring plays to the player's score sheet
        	
        	ScoreSheetBuilder.UpdatePlayerScoreSheet(diceSet, player, false);

        	// increment the roll counter
        	rollCounter++;
        	
        	// build the output object
        	myGameOutput = new MyGameOutput(player, playerList, diceSet, rollCounter != 2);
        	break;
        	
        case PLAYER_SUBMIT:
        	player = myGameInput.getOriginatingPlayer();
        	diceSet = myGameInput.getCurrentDiceSet();
        	// TODO If the player scores a bonus jahtzee, they need another turn to cross out a box.
        	// commit the selected score to the player's scoresheet
        	// set the SELECTED VALUE isUsed = true

        	// In the case where a player uses a bonus jahtzee, they need to cross out another box afterwards
        	if (player.getScoreData().get(ScoreTypes.JAHTZEE_BONUS_1.ordinal()).isSelected ||
        		player.getScoreData().get(ScoreTypes.JAHTZEE_BONUS_2.ordinal()).isSelected ||
        		player.getScoreData().get(ScoreTypes.JAHTZEE_BONUS_3.ordinal()).isSelected)
        	{
        		ScoreSheetBuilder.FinalizeScore(player);
        		ScoreSheetBuilder.UpdatePlayerScoreSheet(diceSet, player, true);
        		myGameOutput = new MyGameOutput(player, playerList, diceSet, false);
        		myGameOutput.setMessage(player.getName() + " has scored a bonus Jahtzee!");
        		break;
        	}
        	
        	ScoreSheetBuilder.FinalizeScore(player);
        	
        	playerList.set(playerList.indexOf(player), player);
        	
        	// go to the next player: currentPlayer = nextPlayer()
        	// generate a new set of dice
        	// reset the roll counter
        	currentPlayer = nextPlayer();
        	
        	if (isGameEnded)
        	{
        		myGameOutput = new MyGameOutput(playerList, MyGameOutputType.GAME_OVER);
        		myGameOutput.setMessage("The game has ended.");
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
        		myGameOutput.setMessage(player.getName() + " scored thier play!\n" +
        							    "It's now " + currentPlayer.getName() + "'s turn!");
        	}
        	break;
        	
        case PLAYER_SKIP:
        	player = myGameInput.getOriginatingPlayer();
        	diceSet = myGameInput.getCurrentDiceSet();
        	
        	// player skipped
        	// Need to generate output to so that the player can select a field to skip
        	ScoreSheetBuilder.UpdatePlayerScoreSheet(diceSet, player, true);

        	myGameOutput = new MyGameOutput(player, playerList, diceSet, false);
        	myGameOutput.setMessage(player.getName() + " skipped their turn! Please wait while they choose which box to cross out.");
        	break;
        	
        case MESSAGE:
        	myGameOutput = new MyGameOutput(myGameInput.getOriginatingPlayerName(), myGameInput.getMessage());
        	break;
        	
        case UPDATE_PLAYERS:
        	myGameOutput = new MyGameOutput(playerList, MyGameOutputType.UPDATE_PLAYERS);

		default:
			break; 	
        }
        return myGameOutput;
	}
	
	private Player nextPlayer()
	{
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
			return playerList.get(0);
		}
		// Ran into a logic error bug, so check if list size is 1
		if (playerList.size() == 1)
			return playerList.get(0);
		else
			return playerList.get(playerList.lastIndexOf(currentPlayer) + 1);
	}
	
	@Override
	public void startGame (GameControl gameControl)
	{
		
	}
}
