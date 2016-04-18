package my_networked_game.HelperClasses;

import java.util.Random;

import javax.swing.ImageIcon;

public class DiceObj implements Comparable<DiceObj>
{
	private boolean isHeld = false;
	private int value;
	
	private static ImageIcon dieDefault_1 = new ImageIcon("Resources\\One.png"),  
							 dieDefault_2 = new ImageIcon("Resources\\Two.png"),  
							 dieDefault_3 = new ImageIcon("Resources\\Three.png"),
							 dieDefault_4 = new ImageIcon("Resources\\Four.png"), 
							 dieDefault_5 = new ImageIcon("Resources\\Five.png"), 
							 dieDefault_6 = new ImageIcon("Resources\\Six.png");  
	
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
	
	public int getValue()
	{
		return value;
	}
	
	public ImageIcon getImageIcon()
	{
		switch (value)
		{
		case 1:
			return dieDefault_1;
		case 2:
			return dieDefault_2;
		case 3:
			return dieDefault_3;
		case 4:
			return dieDefault_4;
		case 5:
			return dieDefault_5;
		default:
			return dieDefault_6;
		}
	}
	
	@Override
	public int compareTo(DiceObj o)
	{
		// switch values if sort order is wrong
		return this.value - o.value;
	}
}
