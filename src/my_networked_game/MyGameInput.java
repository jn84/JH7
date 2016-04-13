package my_networked_game;

import java.io.Serializable;

public class MyGameInput implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private long originatingPlayerID = 0L;
	
	// populate with data from the user interface
	private DiceSet diceSet = new DiceSet();

	// gather all the data from the client user interface and send it off to the server
	
	// which move the player chose to score
	
	// the state of the dice
	
	// ??? probably more
	
	
	// server will determine, based on what was done, where to take the state of the game
}
