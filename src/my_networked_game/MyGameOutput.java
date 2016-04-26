package my_networked_game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import my_networked_game.Enums.MyGameOutputType;
import my_networked_game.HelperClasses.Player;

public class MyGameOutput implements Serializable
{
	
	private static final long serialVersionUID = 5334344851444190266L;

	private MyGameOutputType outputType = null;
	
	private String outputMessage = "",
				   messageSendingPlayer = null;
	
	private Player currentActivePlayer = null;


	private HashMap<String, Player> playerMap = new HashMap<String, Player>();
	
	private boolean canRoll = true,
					isDiceObjHeld = false;
	
	private int diceHoldChangeIndex = -1;
			
	// Everyone can see the rolls, but only the player identified by currentPlayerID can interact
	// Each player's user interface will determine if they are the active player 
	private DiceSet diceSet = null;
	
	// Player names/scores for score list
	
	//
	// Need some sort of game state object
	// Why? Should say why.
	//
	
	// Scoring data
	// Should be an array of score information per player
	// Players will pull their own scoresheet data since  they will know their PlayerID
	
	/**
	 * Constructor to register/unregister a player or spectator
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
	 *	Constructor for the game begin object which well tell the client windows to switch from
	 *	the lobby view to the main game view.
	 * @param currentPlayer
	 * @param playerList
	 * @param diceSet
	 */
	public MyGameOutput(Player currentPlayer, ArrayList<Player> playerList, DiceSet diceSet)
	{
		for (Player elem : playerList)
			playerMap.put(elem.getID(), elem);
		
		this.currentActivePlayer = currentPlayer;
		this.diceSet = diceSet;
		this.canRoll = true;
		outputType = MyGameOutputType.GAME_BEGIN; 
	}
	
	
	
	/**
	 * 	Main game constructor
	 */
	public MyGameOutput(Player currentPlayer, ArrayList<Player> playerList, DiceSet diceSet, boolean canRoll)
	{
		for (Player elem : playerList)
			playerMap.put(elem.getID(), elem);
		
		this.currentActivePlayer = currentPlayer;
		this.diceSet = diceSet;
		this.canRoll = canRoll;
		outputType = MyGameOutputType.MAIN_GAME; 
	}
	
	/**
	 * Game over constructor
	 */
	public MyGameOutput(ArrayList<Player> playerList, MyGameOutputType type)
	{
		for (Player elem : playerList)
			playerMap.put(elem.getID(), elem);
		
		this.outputType = type;
	}

	/**
	 * Constructor to send a message
	 * @param message
	 * Message to send
	 */
	public MyGameOutput(String playerName, String message)
	{
		this.messageSendingPlayer = playerName;
		this.outputMessage = message;
		this.outputType = MyGameOutputType.MESSAGE;
	}
	
	public MyGameOutput(int index, boolean value)
	{
		diceHoldChangeIndex = index;
		isDiceObjHeld = value;
		this.outputType = MyGameOutputType.DICE_HOLD_CHANGED;
	}
	
//	/**
//	 * Get from server: update the Dice Set for each player (including which are held)
//	 * @param diceSet
//	 * The set of dice for players to update to
//	 */
//	public MyGameOutput(DiceSet diceSet)
//	{
//		this.diceSet = diceSet;
//		this.outputType = MyGameOutputType.DICE_HOLD_CHANGED;
//	}
	
	
	
	// added as an afterthought
	public void setMessage(String message)
	{
		outputMessage = message;
	}
	
	public String getMessage()
	{
		return outputMessage;
	}
	
	public MyGameOutputType getOutputType()
	{
		return outputType;
	}
	
	public DiceSet getDice()
	{
		return diceSet;
	}
	
	public Player getActivePlayer()
	{
		return currentActivePlayer;
	}
	
	public Player getMyPlayer(String playerID)
	{
		return playerMap.get(playerID);
	}
	
	public boolean canPlayerRollDice()
	{
		return canRoll;
	}
	
	public String getMessageSendingPlayer()
	{
		return messageSendingPlayer;
	}
	
	public int getDieHoldIndex()
	{
		return diceHoldChangeIndex;
	}
	
	public boolean getIsDieHeld()
	{
		return isDiceObjHeld;
	}
	
	
	
	// Turns out we do need this.
	public HashMap<String, Player> getPlayersMap()
	{
		return playerMap;
	}
}
