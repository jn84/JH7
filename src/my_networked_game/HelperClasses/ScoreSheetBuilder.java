package my_networked_game.HelperClasses;

import java.util.ArrayList;

import my_networked_game.DiceSet;
import my_networked_game.Enums.ScoreTypes;

public class ScoreSheetBuilder
{

	/**
	 * Get an empty score sheet data array
	 * 
	 * @return
	 * Returns a new empty score sheet data array
	 */
	public static ArrayList<SelectableTextFieldState> getNewPlayerScoreSheet()
	{
		ArrayList<SelectableTextFieldState> states = 
				new ArrayList<SelectableTextFieldState>(ScoreTypes.values().length);
		for (ScoreTypes value : ScoreTypes.values())
			states.add(new SelectableTextFieldState());
		return states;
	}


	public static void UpdatePlayerScoreSheet(DiceSet diceSet, Player player, boolean isTurnSkip)
	{
		ArrayList<SelectableTextFieldState> newStates = null;

		newStates = DiceSetScoreProcessor.processDiceSet(diceSet, player, isTurnSkip);

		FillTotals(newStates);

		player.scoreData = newStates;

	}

	public static void FinalizeScore(Player player)
	{
		for (int i = 0; i < player.scoreData.size(); i++)
		{
			if (player.scoreData.get(i).isUsed)
				continue;
			
			if (player.scoreData.get(i).isSelected)
			{
				player.scoreData.get(i).isUsed = true;
				player.scoreData.get(i).isSelectable = false;
				player.scoreData.get(i).isSelected = false;
			}
			
			else
			{
				player.scoreData.get(i).fieldValue = "";
				player.scoreData.get(i).isSelectable = false;
				player.scoreData.get(i).isSelected = false;
			}
		}
		
		FillTotals(player.scoreData);
	}
	
	private static void FillTotals(ArrayList<SelectableTextFieldState> states)
	{
		//UPPER_SUB_TOTAL(6),     
		int 	upper_sub_total 	= 0,
				upper_bonus 		= 0,
				upper_grand_total 	= 0,
				jahtzee_bonus_total = 0,
				final_upper_grand   = 0,
				final_lower_grand   = 0,
				final_grand			= 0;

		for (int s = ScoreTypes.ACES.ordinal(); s <= ScoreTypes.SIXES.ordinal(); s++)
		{
			try
			{
				if (states.get(s).isUsed)
					upper_sub_total += Integer.parseInt(states.get(s).fieldValue);
			}
			catch (NumberFormatException e) {}
		}


		//UPPER_BONUS(7),        
		if (upper_sub_total >= 63)
			upper_bonus = 35;

		//UPPER_GRAND_TOTAL(8),   
		upper_grand_total = upper_sub_total + upper_bonus;


		//JAHTZEE_BONUS_TOTAL(19), 
		if (states.get(ScoreTypes.JAHTZEE_BONUS_1.ordinal()).isUsed)
		{
			jahtzee_bonus_total += 100;
			if (states.get(ScoreTypes.JAHTZEE_BONUS_2.ordinal()).isUsed)
			{
				jahtzee_bonus_total += 100;
				if (states.get(ScoreTypes.JAHTZEE_BONUS_3.ordinal()).isUsed)
				{
					jahtzee_bonus_total += 100;
				}
			}
		}

		//FINAL_UPPER_GRAND_TOTAL(20),
		final_upper_grand = upper_grand_total;

		//FINAL_LOWER_GRAND_TOTAL(21),   
		for (int s = ScoreTypes.KIND_3.ordinal(); s <= ScoreTypes.CHANCE.ordinal(); s++)
		{
			if (!states.get(s).isUsed)
				continue;
			try
			{
				final_lower_grand += Integer.parseInt(states.get(s).fieldValue);
			}
			catch (NumberFormatException e) {}
		}

		//FINAL_GRAND_TOTAL(22);
		final_grand = final_upper_grand + final_lower_grand + jahtzee_bonus_total;

		states.set(ScoreTypes.UPPER_SUB_TOTAL.ordinal(), 
				new SelectableTextFieldState(Integer.toString(upper_sub_total), false, false, true));
		
		states.set(ScoreTypes.UPPER_BONUS.ordinal(), 
				new SelectableTextFieldState(Integer.toString(upper_bonus), false, false, true));
		
		states.set(ScoreTypes.UPPER_GRAND_TOTAL.ordinal(), 
				new SelectableTextFieldState(Integer.toString(upper_grand_total), false, false, true));
		
		states.set(ScoreTypes.JAHTZEE_BONUS_TOTAL.ordinal(), 
				new SelectableTextFieldState(Integer.toString(jahtzee_bonus_total), false, false, true));
		
		states.set(ScoreTypes.FINAL_UPPER_GRAND_TOTAL.ordinal(), 
				new SelectableTextFieldState(Integer.toString(final_upper_grand), false, false, true));
		
		states.set(ScoreTypes.FINAL_LOWER_GRAND_TOTAL.ordinal(), 
				new SelectableTextFieldState(Integer.toString(final_lower_grand), false, false, true));
		
		states.set(ScoreTypes.FINAL_GRAND_TOTAL.ordinal(), 
				new SelectableTextFieldState(Integer.toString(final_grand), false, false, true));
	}

}
