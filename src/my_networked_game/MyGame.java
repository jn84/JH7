package my_networked_game;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.transform.Templates;

import gameNet.GameControl;
import gameNet.GameNet_CoreGame;
import my_networked_game.Enums.MyGameInputType;
import my_networked_game.Enums.MyGameOutputType;
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
        	
        	break;
        	
        // A player left the game. Remove them from the player list and move to the next player.
        case UNREGISTER_PLAYER:
        	player = myGameInput.getOriginatingPlayer();
        	
        	if (currentPlayer.equals(player))
        		currentPlayer = nextPlayer();
        	
        	System.out.println("Player [" + myGameInput.getUsername() + "] left the game");
        	playerList.remove(player);
        	
        	// recursively call this method t
        	process(new MyGameInput(MyGameInputType.GENERATE_NEW_TURN));
        	
        	// update the other players that this player left
        	myGameOutput = new MyGameOutput(player, MyGameOutputType.PLAYER_UNREGISTERED);

        case GENERATE_NEW_TURN:
        	//	Create a copy so we don't modify playerList until the player submits score
        	player = new Player(currentPlayer);
        	
        	diceSet = new DiceSet();
        	
        	rollCounter = 0;
        	// We use currentPlayer because it's a new turn
        	ScoreSheetBuilder.UpdatePlayerScoreSheet(diceSet, player, false);
        	
        	myGameOutput = new MyGameOutput(player, playerList, diceSet, true);
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
        	ScoreSheetBuilder.FinalizeScore(player);
        	
        	playerList.set(playerList.indexOf(player), player);
        	
        	// go to the next player: currentPlayer = nextPlayer()
        	// generate a new set of dice
        	// reset the roll counter
        	currentPlayer = nextPlayer();
        	
        	if (isGameEnded)
        	{
        		myGameOutput = new MyGameOutput(playerList);
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
        	}
        	break;
        	
        case PLAYER_SKIP:
        	player = myGameInput.getOriginatingPlayer();
        	diceSet = myGameInput.getCurrentDiceSet();
        	
        	// player skipped
        	// Need to generate output to so that the player can select a field to skip
        	ScoreSheetBuilder.UpdatePlayerScoreSheet(diceSet, player, true);

        	myGameOutput = new MyGameOutput(player, playerList, diceSet, false);
        	break;

		default:
			break; 	
        }
        return myGameOutput;
	}
	
	private Player nextPlayer()
	{
		if (isGameEnded)
			return null;
		
		if (currentPlayer == playerList.get(playerList.size() - 1))
		{
			if (++roundCounter > NUMBER_OF_ROUNDS)
			{
				isGameEnded = true;
				return null;
			}
			
			return playerList.get(0);
		}
		return playerList.get(playerList.lastIndexOf(currentPlayer) + 1);
	}
	
	@Override
	public void startGame (GameControl gameControl)
	{
		
	}
}
