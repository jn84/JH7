package my_networked_game;

import java.io.Serializable;

import my_networked_game.Enums.MyGameInputType;

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
	 * @param username
	 * The sending player's username
	 * 
	 * @param playerID
	 * The sending player's playerID
	 */
	public MyGameInput(String username, String playerID)
	{
		originatingName = username;
		originatingID = playerID;
		inputType = MyGameInputType.REGISTER_PLAYER;
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
