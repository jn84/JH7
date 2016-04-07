package my_networked_game;

import java.io.Serializable;
import java.util.HashMap;

public class MyGameOutput implements Serializable
{
	
	private static final long serialVersionUID = 5334344851444190266L;

	// who generates player id? How do we ensure unique?
	// Server should assign the ID on connection
	long currentPlayerID = 0L;
	
	// Everyone can see the rolls, but only the player identified by currentPlayerID can interact
	// Each player's user interface will determine if they are the active player 
	DiceSet diceSet = new DiceSet();
	
	//
	//
	// Need some sort of game state object
	//
	//
	
	// Scoring data
	// Should be an array of score information per player
	// Players will pull their own scoresheet data since  they will know their PlayerID
	HashMap scoresMap = new HashMap();
	
	public class PlayerScoreData
	{
		// might be better made as an enum with arrays
		
		// if a move is valid or not
		// used with custom textfield object
		boolean isValid_Aces,              
				isValid_Twos,              
				isValid_Threes,            
				isValid_Fours,             
				isValid_Fives,             
				isValid_Sixes,             
				// isValid_UpperSubTotal,     
				// isValid_UpperBonus,        
				// isValid_UpperGrandTotal,   
				isValid_3Kind,             
				isValid_4Kind,             
				isValid_FullHouse,         
				isValid_SmStriaght,        
				isValid_LgStriaght,        
				isValid_Jahtzee,           
				isValid_Chance,            
				isValid_JahtzeeBonus_1,    
				isValid_JahtzeeBonus_2,    
				isValid_JahtzeeBonus_3,    
				// isValid_JahtzeeBonusTotal, 
				// isValid_UpperGrandTotal,
				isValid_LowerGrandTotal,   
				isValid_GrandTotal;        
		
		
		// -1 means crossed out value
		// 0 means unassigned
		//
		int score_Aces,
			score_Twos,
			score_Threes,
			score_Fours,
			score_Fives,
			score_Sixes,
			score_UpperSubTotal,
			score_UpperBonus,
			score_UpperGrandTotal,
			score_3Kind,
			score_4Kind,
			score_FullHouse,
			score_SmStriaght,
			score_LgStriaght,
			score_Jahtzee,
			score_Chance,
			score_JahtzeeBonus_1,
			score_JahtzeeBonus_2,
			score_JahtzeeBonus_3,
			score_JahtzeeBonusTotal,
			// score_UpperGrandTotal,
			score_LowerGrandTotal,
			score_GrandTotal;
			
	}
}
