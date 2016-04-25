package my_networked_game.HelperClasses;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Random;

import javax.swing.ImageIcon;

public class DiceObj implements Comparable<DiceObj>, Serializable
{
	private static final long serialVersionUID = -7837440278529266232L;

	private boolean isHeld = false;
	private int value;
	
	SecureRandom r = new SecureRandom();
	
	public DiceObj()
	{
		this.reset();
	}
	
	public void generateNewValue()
	{
		if (isHeld)
		{
			isHeld = false;
			return;
		}
		this.reset();
	}
	
	public void setIsHeld(boolean value)
	{
		isHeld = value;
	}
	
	public boolean isHeld()
	{
		return isHeld;
	}
	
	public void reset()
	{
		isHeld = false;
		value = r.nextInt(6) +  1;
	}
	
	public int getValue()
	{
		return value;
	}
	
	@Override
	public int compareTo(DiceObj o)
	{
		// switch values if sort order is wrong
		return this.value - o.value;
	}
}
