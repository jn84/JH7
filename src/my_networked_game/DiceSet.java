package my_networked_game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import my_networked_game.HelperClasses.DiceObj;

public class DiceSet implements Serializable
{
	private static final long serialVersionUID = -5247243649185425468L;

	private ArrayList<DiceObj> diceList = new ArrayList<DiceObj>(5);
	
	public DiceSet()
	{
		for (int i = 0; i < 5; i++)
		{
			diceList.add(new DiceObj());
		}
			
		
		Collections.sort(diceList);
	}
	
	public void roll()
	{
		for (DiceObj dice : diceList)
			dice.generateNewValue();

		// Keep the set sorted
		Collections.sort(diceList);
	}
	
	// Read from list of checkboxes in user interface, then pass to this method
	// We can then send the diceset over myGameOutput
	// Server will perform the roll,
	public void setHeld(int index, boolean value)
	{
		diceList.get(index).setIsHeld(value);
	}
	
	public ArrayList<Integer> getDiceValues()
	{
		ArrayList<Integer> diceValues = new ArrayList<Integer>();
		
		for (DiceObj dice : diceList)
			diceValues.add(new Integer(dice.getValue()));
		
		return diceValues;
	}
	
	// Get Dice objects
	public ArrayList<DiceObj> getDice()
	{
		return diceList;
	}
}
