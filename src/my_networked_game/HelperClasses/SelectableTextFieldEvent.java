package my_networked_game.HelperClasses;

import java.util.EventObject;

public class SelectableTextFieldEvent extends EventObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8199720796016104126L;
	boolean isValidSelection = false;
	
	public SelectableTextFieldEvent(Object source, boolean validSelection)
	{
		super(source);
		isValidSelection = validSelection;
	}
	
	public boolean getIsValidSelection()
	{
		return isValidSelection;
	}
}


