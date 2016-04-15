package my_networked_game.HelperClasses;

import java.awt.Color;

import javax.swing.JTextField;

public class SelectableTextField extends JTextField
{
	private final Color COLOR_SELECTED = Color.BLUE,
						COLOR_SELECTABLE = Color.GREEN,
						COLOR_NOT_SELECTABLE = Color.LIGHT_GRAY;
	
	private SelectableTextFieldState state = new SelectableTextFieldState();

	public SelectableTextField(SelectableTextFieldState st)
	{
		this.setEditable(false);
		setState(st);
	}
	
	public void setState(SelectableTextFieldState st)
	{
		state = st;
		update();
	}
	
	public SelectableTextFieldState getState()
	{
		return state;
	}
	
	private void update()
	{
		if (state.isSelectable)
		{
			if (state.isSelected)
			{
				this.setText(state.fieldValue);
				this.setBackground(COLOR_SELECTED);
			}
			else
			{
				this.setText("");
				this.setBackground(COLOR_SELECTABLE);
			}
			return;
		}
		this.setText(state.fieldValue);
		this.setBackground(COLOR_NOT_SELECTABLE);
	}
	
	public void unselectField()
	{
		if (state.isSelected)
			state.isSelected = false;
		update();
	}
	
	public void selectField()
	{
		state.isSelected = true;
		update();
	}
	
	public boolean isSelectable()
	{
		return state.isSelectable;
	}
	
	public boolean isSelected()
	{
		return state.isSelected;
	}
	
	public String getValue()
	{
		return state.fieldValue;
	}
	
	// Never enabled.
	@Override
	public void setEditable(boolean value)
	{
		return;
	}
}
