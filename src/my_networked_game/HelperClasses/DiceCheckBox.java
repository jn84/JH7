package my_networked_game.HelperClasses;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;

public class DiceCheckBox extends JCheckBox
{
	Image checkboxImageDefault = null,
		  checkboxImageSelected = null;
	
	DiceObj diceObj = null;
	
	public DiceCheckBox(DiceObj die)
	{
		super();
		diceObj = die;
	}
	
	@Override
	public void paint(Graphics g)
	{
		
	}
}
