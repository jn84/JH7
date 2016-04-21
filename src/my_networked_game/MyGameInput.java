package my_networked_game;

import java.io.Serializable;

import my_networked_game.Enums.MyGameInputType;
import my_networked_game.HelperClasses.Player;

public class MyGameInput implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private MyGameInputType inputType = null;
	// populate with data from the user interface
	private DiceSet diceSet = null;
	
	private String originatingName = "",
				   originatingID = "";
	
	private Player originatingPlayer = null;
	
	private String playerMessage = null;


	//	GAME_BEGIN
	//		NOTHING

	public MyGameInput()
	{
		inputType = MyGameInputType.GAME_BEGIN;
	}
	
	
	
	//		REGISTER_PLAYER
	//		UNREGISTER_PLAYER,
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
		// Doesn't need originatingPlayer since it creates the object for the first time.
		case REGISTER_PLAYER:
			originatingName = p.getName();
			originatingID = p.getID();
			inputType = type;
			break;
		case UNREGISTER_PLAYER:
			originatingName = p.getName();
			originatingID = p.getID();
			originatingPlayer = p;
			inputType = type;
		default:
			break;
		}
	}


	//	PLAYER_SKIP
	//		Dice
	//		Player
	//	PLAYER_SUBMIT
	//		Dice
	//		Player
	//	PLAYER_ROLL
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

	//		GENERATE_NEW_TURN
	/**
	 * Use when player left the game,
	 * @param type
	 */
	public MyGameInput(MyGameInputType type)
	{
		inputType = type;
	}
	
	// 	MESSAGE
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
	}
	
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
	
	public DiceSet getCurrentDiceSet()
	{
		return diceSet;
	}
}
