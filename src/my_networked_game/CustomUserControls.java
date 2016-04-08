package my_networked_game;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

import my_networked_game.Enums.ScoreFieldControlState;

public class CustomUserControls
{
	// Take various score inputs to determine the contents of the textfield
	// -1 : crossed out
	// null : no score set
	// 0 >= : set scoreValue, but only show it if FINAL_SCORE, NEVER_SELECTABLE, or SELECTED_MOVE
	
	
	// TODO: Only if time. Make font size in text field resize to fit the text field
	public class SelectableScoreTextField extends JTextField//implements MouseAdapter
	{
		
		private static final long serialVersionUID = -4428505272017083770L;
		
		private boolean isSelectable = false;
		
		private int scoreValue = 0;
		
		private ScoreFieldControlState state = ScoreFieldControlState.NEVER_SELECTABLE;
		
		public SelectableScoreTextField()
		{
			// TODO Auto-generated constructor stub
			super();
			this.setHorizontalAlignment(SwingConstants.RIGHT);
			this.setEditable(false);
		}
		
		public void setState(ScoreFieldControlState state, int score)
		{
			this.setBackground(state.getColor());
			this.scoreValue = score;
			switch (state)
			{
			case VALID_MOVE_TO_SELECT:
				this.isSelectable = true;
				this.setText("");
				break;
				
			case INVALID_MOVE_TO_SELECT:
				this.isSelectable = false;
				break;
				
			case SELECTED_MOVE:
				this.isSelectable = true;
				// show the possible score
				break;
			default:
				this.isSelectable = false;
				this.setText(Integer.toString(scoreValue));
			}
		}
		
		public ScoreFieldControlState getState()
		{
			return state;
		}
		
		public boolean getIsSelectable()
		{
			return isSelectable;
		}
	}
}
