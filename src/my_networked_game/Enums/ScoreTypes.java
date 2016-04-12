package my_networked_game.Enums;

public enum ScoreTypes
{
	// Indexes (might not need since we can use ordinal method)
	ACES(0),              
	TWOS(1),              
	THREES(2),            
	FOURS(3),             
	FIVES(4),             
	SIXES(5),             
	UPPER_SUB_TOTAL(6),     
	UPPER_BONUS(7),        
	UPPER_GRAND_TOTAL(8),   
	KIND_3(9),             
	KIND_4(10),             
	FULL_HOUSE(11),         
	SMALL_STRAIGHT(12),        
	LARGE_STRAIGHT(13),        
	JAHTZEE(14),           
	CHANCE(15),            
	JAHTZEE_BONUS_1(16),    
	JAHTZEE_BONUS_2(17),    
	JAHTZEE_BONUS_3(18),    
	JAHTZEE_BONUS_TOTAL(19), 
	FINAL_UPPER_GRAND_TOTAL(20),
	FINAL_LOWER_GRAND_TOTAL(21),   
	FINAL_GRAND_TOTAL(22);
	
	int scoreID;
	
	private ScoreTypes(int id)
	{
		scoreID = id;
	}
	
}
