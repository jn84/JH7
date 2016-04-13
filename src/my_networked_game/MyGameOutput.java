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
	// MAYBE:::::: PLAYER ID SHOULD BE DETERMINED BY CLIENT AND SERVER SEPARATELY 
	// 			   SOME ALGORITHM MIGHT PRODUCE THE SAME ID ON DIFFERENT SYSTEMS
	//			   THIS WAY WE DON'T HAVE TO PASS THE ID OVER THE NETWORK
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
	//HashMap<long, PlayerScoreData> scoresMap = new HashMap();
	
	public MyGameOutput()
	{
		//scoresMap.put(999L, new PlayerScoreData());
	}
	
	public class PlayerScoreData
	{
		SelectableTextFieldState[] scoreData = new SelectableTextFieldState[ScoreTypes.values().length];
		
		
	}
}
