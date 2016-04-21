package my_networked_game.HelperClasses;

import java.io.Serializable;

// These objects will be passed around the network
public class SelectableTextFieldState implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 821919119973228309L;

	// I don't like making these member variables public, but I ran into strange issues relating to program design.
	// I'll try to resolve this if time allows.
	
	
	// A blank field value means it's unused
	public String fieldValue = "";
	
	public boolean isSelected = false,
				   isSelectable = false,
				   isUsed = false;

	public SelectableTextFieldState() {}
	
	
	/**
	 * Copy constructor
	 */
	public SelectableTextFieldState(SelectableTextFieldState state)
	{
		this.fieldValue = state.fieldValue;
		this.isSelected = state.isSelected;
		this.isSelectable = state.isSelectable;
		this.isUsed = state.isUsed;
	}
	
	public SelectableTextFieldState(String value, boolean selected, boolean selectable, boolean used)
	{
		fieldValue = value;
		isSelected = selected;
		isSelectable = selectable;
		isUsed = used;
	}
	
	public void unselectField()
	{
		if (this.isSelected)
			isSelected = false;
	}
}
