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
		for (DiceObj dice : diceList)
			dice = new DiceObj();
		
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
	public void setHeld(ArrayList<Boolean> heldList)
	{
		// heldList should be size 5
		if (heldList.size() != 5)
		{
			// Debug code
			System.out.println("DiceSet: setHeld: heldList incorrect size: " + heldList.size());
			System.out.println("DiceSet: setHeld: should be 5");
			return;
		}
		
		for (int  i = 0; i < heldList.size(); i++)
			diceList.get(i).setIsHeld(heldList.get(i));
	}
	
	// Pass this return value to the score verifier/parser
	public ArrayList<Integer> getDiceValues()
	{
		ArrayList<Integer> diceValues = new ArrayList<Integer>();
		
		for (DiceObj dice : diceList)
			diceValues.add(new Integer(dice.getValue()));
		
		return diceValues;
		
	}
}
