package my_networked_game;

public enum ScoreTypes
{
	Aces(0),              
	Twos(1),              
	Threes(2),            
	Fours(3),             
	Fives(4),             
	Sixes(5),             
	UpperSubTotal(6),     
	UpperBonus(7),        
	UpperGrandTotal(8),   
	Kind3(9),             
	Kind4(10),             
	FullHouse(11),         
	SmStriaght(12),        
	LgStriaght(13),        
	Jahtzee(14),           
	Chance(15),            
	JahtzeeBonus_1(16),    
	JahtzeeBonus_2(17),    
	JahtzeeBonus_3(18),    
	JahtzeeBonusTotal(19), 
	re_UpperGrandTotal(20),
	LowerGrandTotal(21),   
	GrandTotal(22);
	
	int scoreID;
	
	private ScoreTypes(int id)
	{
		scoreID = id;
	}
	
}
