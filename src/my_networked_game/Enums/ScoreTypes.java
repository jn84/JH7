package my_networked_game.Enums;

public enum ScoreTypes
{
	// Indexes (might not need since we can use ordinal method)
	ACES("aces"),              
	TWOS("twos"),              
	THREES("threes"),            
	FOURS("fours"),             
	FIVES("fives"),             
	SIXES("sixes"),             
	UPPER_SUB_TOTAL(""),     
	UPPER_BONUS(""),        
	UPPER_GRAND_TOTAL(""),   
	KIND_3("3 of a kind"),             
	KIND_4("4 of a kind"),             
	FULL_HOUSE("full house"),         
	SMALL_STRAIGHT("small straight"),        
	LARGE_STRAIGHT("large straight"),        
	JAHTZEE("JAHTZEE"),           
	CHANCE("chance"),            
	JAHTZEE_BONUS_1(""),    
	JAHTZEE_BONUS_2(""),    
	JAHTZEE_BONUS_3(""),    
	JAHTZEE_BONUS_TOTAL(""), 
	FINAL_UPPER_GRAND_TOTAL(""),
	FINAL_LOWER_GRAND_TOTAL(""),   
	FINAL_GRAND_TOTAL("");
	
	public String friendlyName;
	
	private ScoreTypes(String name)
	{
		friendlyName = name;
	}
}
