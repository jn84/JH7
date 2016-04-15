package my_networked_game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import my_networked_game.Enums.MyGameOutputType;
import my_networked_game.Enums.ScoreTypes;
import my_networked_game.HelperClasses.Player;
import my_networked_game.HelperClasses.SelectableTextFieldGroup;
import my_networked_game.HelperClasses.SelectableTextFieldState;

public class MyGameOutput implements Serializable
{
	
	private static final long serialVersionUID = 5334344851444190266L;

	private MyGameOutputType outputType = null;
	
	private Player currentActivePlayer = null;

	private ArrayList<Player> playerList = null;
	
	private ArrayList<String> messageList = new ArrayList<String>();
	
	private SelectableTextFieldState[] textFieldStates = null;
	
	// Everyone can see the rolls, but only the player identified by currentPlayerID can interact
	// Each player's user interface will determine if they are the active player 
	DiceSet diceSet = null;
	
	// Player names/scores for score list
	
	//
	// Need some sort of game state object
	// Why? Should say why.
	//
	
	// Scoring data
	// Should be an array of score information per player
	// Players will pull their own scoresheet data since  they will know their PlayerID
	//HashMap<long, PlayerScoreData> scoresMap = new HashMap();
	
	/**
	 * Constructor to register a new player or specatator
	 * 
	 * @param name
	 * Name of the player registering
	 * @param ID
	 * ID of the player registering
	 */
	public MyGameOutput(Player p, MyGameOutputType type)
	{
		currentActivePlayer = p;
		outputType = type;
	}
	
	/**
	 * 	Main game constructor
	 */
	public MyGameOutput(Player currentPlayer, ArrayList<Player> playerList, DiceSet diceSet)
	{
		this.currentActivePlayer = currentPlayer;
		this.playerList = playerList;
		this.diceSet = diceSet;
		
		diceSet.roll();
		
		// TODO
		// This should now check what moves are valid for the current player, and get/build array of SelectableTextFieldState[]
		// This information should be pulled (or pushed) from MyGame
		/// ....
		// Maybe not. each playerList object contains this array.. so this array should be prepared before calling this method
		
		
		outputType = MyGameOutputType.MAIN_GAME; 
	}
	
	public MyGameOutputType getOutputType()
	{
		return outputType;
	}
	
	public Player getActivePlayer()
	{
		return currentActivePlayer;
	}
}
