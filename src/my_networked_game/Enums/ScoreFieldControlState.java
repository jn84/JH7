package my_networked_game.Enums;

import java.awt.Color;


// Used by MyGame (server), MyUserInterface, and SelectableTextFieldGroup

public enum ScoreFieldControlState 
{
	VALID_MOVE_TO_SELECT(0, Color.GREEN),	// A Valid move
	INVALID_MOVE_TO_SELECT(1, Color.RED), 	// Not a valid move
	SELECTED_MOVE(2, Color.BLUE),			// Currently selected move (how do we unset the others?)
	NEVER_SELECTABLE(3, Color.LIGHT_GRAY); 		// total score slots, for example, should never be selectable
	// NEVER_SELECTABLE_ALWAYS_SHOW
	// FINAL_SCORE (already selected and committed - should be LIGHT_GRAY)
	
	int fieldStateID;
	
	Color textFieldColor;

	
	private ScoreFieldControlState(int id, Color c)
	{
		fieldStateID = id;
		textFieldColor = c;
	}
	
	public int getID()
	{
		return fieldStateID;
	}
	
	public Color getColor()
	{
		return textFieldColor;
	}
};
