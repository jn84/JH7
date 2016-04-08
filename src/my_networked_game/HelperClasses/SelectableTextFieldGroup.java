package my_networked_game.HelperClasses;

import java.util.ArrayList;

import my_networked_game.CustomUserControls.SelectableScoreTextField;
import my_networked_game.Enums.ScoreFieldControlState;

public class SelectableTextFieldGroup
{
	private ArrayList<SelectableScoreTextField> sstfList = new ArrayList<SelectableScoreTextField>();
	
	public void addSelectableTextField(SelectableScoreTextField sstf)
	{
		if (sstfList.contains(sstf))
			return;
		sstfList.add(sstf);
	}
	
	public void fieldSelected(SelectableScoreTextField obj)
	{
		for (SelectableScoreTextField elem : sstfList)
		{
			if (!elem.getIsSelectable())
				continue;
			
			if (elem.equals(obj))
			{
				elem.setState(ScoreFieldControlState.SELECTED_MOVE);
				continue;
			}
			
			elem.setState(ScoreFieldControlState.VALID_MOVE_TO_SELECT);
		}
	}
}
