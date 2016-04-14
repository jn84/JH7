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

	/**
	 * Use this constructor ONLY to register the player
	 * 
	 * @param p
	 * The sending player's ID object
	 * 
	 */
	public MyGameInput(Player p)
	{
		originatingName = p.getName();
		originatingID = p.getID();
		inputType = MyGameInputType.REGISTER_PLAYER;
	}
	
	/**
	 * 
	 * Use this constructor in cases where a player left the game 
	 * 
	 * @param type 
	 * The type of action to force
	 * 
	 */
	public MyGameInput(MyGameInputType type)
	{
		inputType = type;
	}
	
	public MyGameInput()
	{
		
	}
	
	public MyGameInputType getInputType()
	{
		return inputType;
	}
	
	public String getUsername()
	{
		return originatingName;
	}
	
	public String getPlayerID()
	{
		return originatingID;
	}
	
	// gather all the data from the client user interface and send it off to the server
	
	// which move the player chose to score
	
	// the state of the dice
	
	// ??? probably more
	
	
	// server will determine, based on what was done, where to take the state of the game
}
