package my_networked_game;

import java.io.Serializable;
import java.util.ArrayList;

import gameNet.GameControl;
import gameNet.GameNet_CoreGame;
import my_networked_game.Enums.MyGameInputType;
import my_networked_game.Enums.MyGameOutputType;
import my_networked_game.HelperClasses.Player;

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
        
        Player p = null;
        
        switch (myGameInput.getInputType())
        {
        case REGISTER_PLAYER:
        	p = new Player(myGameInput.getUsername(), myGameInput.getPlayerID(), 0);        	
        	System.out.println("Player [" + myGameInput.getUsername() + "] registered with ID [" + myGameInput.getPlayerID() + "]");
        	playerList.add(p);
        	
        	// Notify all players
        	myGameOutput = new MyGameOutput(p, MyGameOutputType.PLAYER_REGISTERED);
        	
        	break;
        	
        // A player left the game. Remove them from the player list and move to the next player.
        case UNREGISTER_PLAYER:

        	if (currentPlayer != null)
        		currentPlayer = nextPlayer();

        	
        	System.out.println("Player [" + myGameInput.getUsername() + "] registered with ID [" + myGameInput.getPlayerID() + "]");
        	playerList.remove(p);
        	
        	// Notify all players
        	process(new MyGameInput(MyGameInputType.GENERATE_NEW_TURN));
        	myGameOutput = new MyGameOutput(p, MyGameOutputType.PLAYER_UNREGISTERED);

        case GENERATE_NEW_TURN:
        	myGameOutput = generateNewTurn();
        	break;
        	
        case GAME_BEGIN:
        	// TODO XXX klgdjlksdjgsdg
        	currentPlayer = playerList.get(0);
        	break;
        	
        case PLAYER_ROLL:
        	// roll the unheld dice
        	// process valid scoring plays
        	// apply the valid scoring plays to the player's score sheet
        	// build the output object
        	break;
        	
        case PLAYER_SUBMIT:
        	// commit the selected score to the player's scoresheet
        	// set the SELECTED VALUE isUsed = true
        	// go to the next player: currentPlayer = nextPlayer()
        	// generate a new set of dice
        	// process the valid scoring plays against currentPlayer's score sheet
        	// build the output object
        	// if (isGameEnded == true)
        	//		MyGameOutputType.GAME_OVER
        	// else
        	//		MyGameOutputType.MAIN_GAME
        	break;
        	
        case PLAYER_SKIP:
        	// player skipped
        	// Need to generate output to so that the player can select a field to skip
        	break;

		default:
			// TODO leave this for whatever is left over
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
	
	private MyGameOutput generateNewTurn()
	{
		// TODO Should populate the SelectableTextFieldState[] array in each Player object. MyGameOutput relies on this.
		
		return new MyGameOutput(currentPlayer, playerList, new DiceSet());
	}
	
	@Override
	public void startGame (GameControl gameControl)
	{
		
	}
}
