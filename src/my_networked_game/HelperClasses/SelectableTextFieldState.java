package my_networked_game.HelperClasses;


// These objects will be passed around the network
public class SelectableTextFieldState
{
	// I don't like making these member variables public, but I ran into strange issues relating to program design.
	// I'll try to resolve this if time allows.
	
	public String fieldValue = "";
	
	public boolean isSelected = false,
					isSelectable = false;

	public SelectableTextFieldState() {}
	
	public SelectableTextFieldState(String value, boolean selected, boolean selectable)
	{
		fieldValue = value;
		isSelected = selected;
		isSelectable = selectable;
	}
	
	public void unselectField()
	{
		if (this.isSelected)
			isSelected = false;
	}
}
