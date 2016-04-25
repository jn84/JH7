package my_networked_game.HelperClasses;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import my_networked_game.Enums.ScoreTypes;
import my_networked_game.HelperClasses.SelectableTextFieldState;
import my_networked_game.Interfaces.SelectableTextFieldEventGenerator;
import my_networked_game.Interfaces.SelectableTextFieldListener;

public class SelectableTextFieldGroup extends MouseAdapter implements Iterable<SelectableTextField>, SelectableTextFieldEventGenerator
{
	private ArrayList<SelectableTextField> sstfList = 
			new ArrayList<SelectableTextField>(ScoreTypes.values().length);
	
	private ArrayList<SelectableTextFieldListener> selectableTextFieldListeners = 
			new ArrayList<SelectableTextFieldListener>();

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
					public void mousePressed(MouseEvent e)
					{
						super.mousePressed(e);
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
				field.selectField();
				triggerSelectableTextFieldEvent(new SelectableTextFieldEvent(field, true));
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

	
	/**
	 * Get the current states of the score sheet elements
	 * 
	 * @return
	 */
	public ArrayList<SelectableTextFieldState> getStates()
	{
		ArrayList<SelectableTextFieldState> states = new ArrayList<SelectableTextFieldState>(sstfList.size());

		for (int i = 0; i < sstfList.size(); i++)
			states.add(sstfList.get(i).getState());		
		return states;
	}
	
	public void setStates(ArrayList<SelectableTextFieldState> states)
	{
		if (states == null)
		{
			System.out.println("SelectableTextFieldGroup.setStates: passed array is null");
			return;
		}
		
		if (states.size() != sstfList.size())
		{
			System.out.println("SelectableTextFieldGroup.setStates: size of states array is invalid");
			return;
		}
		
		for (int i = 0; i < states.size(); i++)
			sstfList.get(i).setState(states.get(i));
		triggerSelectableTextFieldEvent(new SelectableTextFieldEvent(this, false));
	}

	@Override
	public Iterator<SelectableTextField> iterator()
	{
		return sstfList.iterator();
	}


	// If there are no listeners, forget about it and return.
	// Otherwise, send the event to each listener
	private void triggerSelectableTextFieldEvent(SelectableTextFieldEvent e) 
	{
		if (selectableTextFieldListeners == null)
			return;
		for (int i = 0; i < selectableTextFieldListeners.size(); i++) 
			selectableTextFieldListeners.get(i).handleSelectableTextFieldEvent(e);
	}

	@Override
	public void addSelectableTextFieldEventListener(SelectableTextFieldListener listener)
	{
		if (!selectableTextFieldListeners.contains(listener))
			selectableTextFieldListeners.add(listener);
	}
}
