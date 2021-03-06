package my_networked_game;

import java.io.Serializable;

import my_networked_game.Enums.MyGameInputType;
import my_networked_game.Enums.MyGameOutputType;
import my_networked_game.HelperClasses.Player;

public class MyGameInput implements Serializable
{
	private static final long serialVersionUID = -6571379737191829342L;
	private MyGameInputType inputType = null;
	// populate with data from the user interface
	private DiceSet diceSet = null;
	
	private String originatingName = "",
				   originatingID = "";
	
	private Player originatingPlayer = null;
	
	private String playerMessage = null;
	
	private int diceObjIndex = -1;
	
	private boolean diceObjSelected = false;


	//	GAME_BEGIN
	//		NOTHING
	/**
	 * Use to trigger the game to begin (move on from lobby)
	 */
	public MyGameInput()
	{
		inputType = MyGameInputType.GAME_BEGIN;
	}
	
	
	
	//		REGISTER_PLAYER needs
	//			Player
	//		UNREGISTER_PLAYER needs
	//			Player
	/**
	 * Use this constructor only to register/unregister the player
	 * 
	 * @param p
	 * The sending player's ID object
	 * 
	 */
	public MyGameInput(Player p, MyGameInputType type)
	{
		switch (type)
		{
		case REGISTER_PLAYER:
		case UNREGISTER_PLAYER:
			originatingName = p.getName();
			originatingID = p.getID();
			originatingPlayer = p;
			inputType = type;
		default:
			break;
		}
	}


	//	PLAYER_SKIP needs
	//		Dice
	//		Player
	//	PLAYER_SUBMIT needs
	//		Dice
	//		Player
	//	PLAYER_ROLL needs
	//		Dice
	//		Player
	/**
	 * Use when the player rolls, submits, or skips
	 * @param p
	 * The player that created this object 
	 * The player object should have its scoresheet member variable 
	 * updated to reflect the current state of the scoresheet
	 * @param
	 * The state of the dice for the player that created this object
	 * @param type 
	 * The type of input to communicate to the server (which button was pressed)
	 * 
	 */
	public MyGameInput(Player p, DiceSet ds, MyGameInputType type)
	{
		originatingPlayer = p;
		diceSet = ds;
		inputType = type;
	}

	//		GENERATE_NEW_TURN needs
	//			inputType (GENERATE_NEW_TURN)
	//	    
	/**
	 * Use to generate new turn (only used internally by MyGame)
	 * @param type 
	 * GENERATE_NEW_TURN or UPDATE_PLAYERS
	 * 
	 */
	public MyGameInput(MyGameInputType type)
	{
		inputType = type;
	}
	
	// 	MESSAGE needs
	//		originatingName
	//		Message
	/**
	 * Constructor to send a message to other players
	 * @param playerName
	 * The name of the sending player (player.getName())
	 * @param message
	 * The message to send to other players
	 */
	public MyGameInput(String playerName, String message)
	{
		originatingName = playerName;
		playerMessage = message;
		inputType = MyGameInputType.MESSAGE;
	}
	
	public MyGameInput(int index, boolean value)
	{
		diceObjIndex = index;
		diceObjSelected = value;
		this.inputType = MyGameInputType.DICE_HOLD_CHANGED;
	}
	
	
//	/**
//	 * Send to server: update the Dice Set for each player (including which are held)
//	 * @param diceSet
//	 * The set of dice for players to update to
//	 */
//	public MyGameInput(DiceSet diceSet)
//	{
//		this.diceSet = diceSet;
//		this.inputType = MyGameInputType.DICE_HOLD_CHANGED;
//	}
	
	public MyGameInputType getInputType()
	{
		return inputType;
	}
	
	public String getMessage()
	{
		return playerMessage;
	}
	
	public String getUsername()
	{
		return originatingName;
	}
	
	public String getPlayerID()
	{
		return originatingID;
	}
	
	public Player getOriginatingPlayer()
	{
		return originatingPlayer;
	}
	
	public String getOriginatingPlayerName()
	{
		return originatingName;
	}
	
	public DiceSet getCurrentDiceSet()
	{
		return diceSet;
	}
	
	public int getDiceHeldChangedIndex()
	{
		return diceObjIndex;
	}
	
	public boolean isDiceObjHeld()
	{
		return diceObjSelected;
	}
}
