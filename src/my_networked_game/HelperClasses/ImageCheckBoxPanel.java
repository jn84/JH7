package my_networked_game.HelperClasses;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPanel;

import my_networked_game.HelperClasses.DiceObj;


// Listens to the contained ImageCheckBox for changes, then changes the background color accordingly
public class ImageCheckBoxPanel extends JPanel
{
	private final Color HIGHLIGHT_COLOR = Color.RED;
	
	DiceCheckBox imageCheckBox = null;
	
	public ImageCheckBoxPanel()
	{
		super();
		
		imageCheckBox = new DiceCheckBox(new DiceObj());
		
		imageCheckBox.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (imageCheckBox.isSelected())
					setBackground(HIGHLIGHT_COLOR);
				else
					// TODO Make sure this works. This is supposed to set the background color of the dice check to the default value.
					setBackground(null);
			}
		});
		
	}
}
