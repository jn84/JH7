package my_networked_game;

import java.io.Serializable;
import java.util.HashMap;

import my_networked_game.Enums.MyGameOutputType;
import my_networked_game.Enums.ScoreTypes;
import my_networked_game.HelperClasses.SelectableTextFieldGroup;
import my_networked_game.HelperClasses.SelectableTextFieldState;

public class MyGameOutput implements Serializable
{
	
	private static final long serialVersionUID = 5334344851444190266L;

	// who generates player id? How do we ensure unique?
	// Server should assign the ID on connection
	long currentPlayerID = 0L;
	
	// Everyone can see the rolls, but only the player identified by currentPlayerID can interact
	// Each player's user interface will determine if they are the active player 
	DiceSet diceSet = new DiceSet();
	
	// Player names/scores for score list
	
	//
	// Need some sort of game state object
	// Why? Should say why.
	//
	
	// Scoring data
	// Should be an array of score information per player
	// Players will pull their own scoresheet data since  they will know their PlayerID
	HashMap scoresMap = new HashMap();
	
	public class PlayerScoreData
	{
		SelectableTextFieldState[] scoreData = new SelectableTextFieldState[ScoreTypes.values().length];
		
		
	}
}
