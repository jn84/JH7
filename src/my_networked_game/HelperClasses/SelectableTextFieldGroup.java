package my_networked_game.HelperClasses;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JTextField;

import my_networked_game.Enums.ScoreTypes;
import my_networked_game.HelperClasses.SelectableTextFieldState;

public class SelectableTextFieldGroup extends MouseAdapter implements Iterable<SelectableTextField>
{
	private ArrayList<SelectableTextField> sstfList = new ArrayList<SelectableTextField>(ScoreTypes.values().length);
	
	public SelectableTextFieldGroup()
	{
		for (int i = 0; i < ScoreTypes.values().length; i++)
		{
			sstfList.add(new SelectableTextField(new SelectableTextFieldState()));
			sstfList.get(i).addMouseListener
			(
					new MouseAdapter()
					{
						@Override
						public void mouseClicked(MouseEvent e)
						{
							super.mouseClicked(e);
							processClick(e);
						}
					}
			);
		}
	}
	
	private void processClick(MouseEvent e)
	{
		// Field not selectable. Just quit
		if (!((SelectableTextField)(e.getComponent())).isSelectable())
			return;
		
		// Clear any currently selected field
		clearSelected();
		
		// Set the newly selected field
		for (SelectableTextField field : sstfList)
		{
			if (field.equals(e.getComponent()))
			{
				field.setSelected(true);
				return;
			}
		}
	}
	
	private void clearSelected()
	{
		for (SelectableTextField field : sstfList)
		{
			if (!field.isSelected())
				continue;
			field.unselectField();
		}
	}
	
	public SelectableTextField getField(ScoreTypes type)
	{
		try
		{
			return sstfList.get(type.ordinal());
		}
		catch (NullPointerException e)
		{
			System.out.println("sstfList.get(type.ordinal()) is NULL");
			return null;
		}
	}
	
	public SelectableTextFieldState[] getStates()
	{
		SelectableTextFieldState[] states = new SelectableTextFieldState[sstfList.size()];
		
		for (int i = 0; i < sstfList.size(); i++)
			states[i] = sstfList.get(i).getState();		
		return states;
	}

	@Override
	public Iterator<SelectableTextField> iterator()
	{
		return sstfList.iterator();
	}
}
