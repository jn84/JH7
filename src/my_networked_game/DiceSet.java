package my_networked_game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DiceSet
{
	private ArrayList<DiceObj> diceList = new ArrayList<DiceObj>(5);
	
	public void DiceSet()
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
			diceValues.add(new Integer(dice.value));
		
		return diceValues;
		
	}

	
	private class DiceObj implements Comparable<DiceObj>
	{
		private boolean isHeld = false;
		private int value;
		
		Random r = new Random();
		
		public DiceObj()
		{
			this.reset();
		}
		
		public void generateNewValue()
		{
			if (isHeld)
				return;
			value = r.nextInt(6) + 1;
		}
		
		public void setIsHeld(boolean value)
		{
			isHeld = value;
		}
		
		public void reset()
		{
			isHeld = false;
			value = r.nextInt(6) +  1;
		}
		
		@Override
		public int compareTo(DiceObj o)
		{
			// switch values if sort order is wrong
			return this.value - o.value;
		}
	}
}
