package my_networked_game.HelperClasses;

import java.util.ArrayList;
import java.util.Collections;

import my_networked_game.DiceSet;
import my_networked_game.Enums.ScoreTypes;

public class DiceSetScoreProcessor
{
	private final static String CROSS_OUT_TEXT = "----------------------------";

	public static ArrayList<SelectableTextFieldState> processDiceSet(DiceSet diceSet, Player player, boolean isTurnSkip)
	{
		ArrayList<SelectableTextFieldState> states = new ArrayList<SelectableTextFieldState>(ScoreTypes.values().length);

		ArrayList<Integer> diceValues = diceSet.getDiceValues();

		// The list should already be sorted, but just in case.
		Collections.sort(diceValues);

		int sum = sumIntArrayList(diceValues);

		ScoreTypes type = ScoreTypes.ACES;

		// If the field is already used, then we skip it

		//ACES(0),
		states.set(type.ordinal(), processSingleValue(type, player, isTurnSkip, diceValues));

		//TWOS(1),
		type = ScoreTypes.TWOS;
		states.set(type.ordinal(), processSingleValue(type, player, isTurnSkip, diceValues));

		//THREES(2)
		type = ScoreTypes.THREES;
		states.set(type.ordinal(), processSingleValue(type, player, isTurnSkip, diceValues));

		//FOURS(3)
		type = ScoreTypes.FOURS;
		states.set(type.ordinal(), processSingleValue(type, player, isTurnSkip, diceValues));

		//FIVES(4)
		type = ScoreTypes.FIVES;
		states.set(type.ordinal(), processSingleValue(type, player, isTurnSkip, diceValues));

		//SIXES(5)
		type = ScoreTypes.SIXES;
		states.set(type.ordinal(), processSingleValue(type, player, isTurnSkip, diceValues));

		//KIND_3(9),            
		type = ScoreTypes.KIND_3;
		if (!player.scoreData.get(type.ordinal()).isUsed)
		{
			if (isTurnSkip)
				states.set(type.ordinal(), new SelectableTextFieldState(CROSS_OUT_TEXT, false, true, false));
			else
			{
				if (distinct(diceValues) <= 3 && occurrencesOf(diceValues.get(2), diceValues) >= 3)
					states.set(type.ordinal(), new SelectableTextFieldState(Integer.toString(sum), false, true, false));					
				else
					states.set(type.ordinal(), new SelectableTextFieldState("", false, false, false));
			}
		}
		else
			states.set(type.ordinal(), player.scoreData.get(type.ordinal()));


		//KIND_4(10),      
		type = ScoreTypes.KIND_4;
		if (!player.scoreData.get(type.ordinal()).isUsed)
		{
			if (isTurnSkip)
				states.set(type.ordinal(), new SelectableTextFieldState(CROSS_OUT_TEXT, false, true, false));
			else
			{
				if (distinct(diceValues) <= 2 && occurrencesOf(diceValues.get(2), diceValues) >= 4)
					states.set(type.ordinal(), new SelectableTextFieldState(Integer.toString(sum), false, true, false));					
				else
					states.set(type.ordinal(), new SelectableTextFieldState("", false, false, false));
			}
		}
		else
			states.set(type.ordinal(), player.scoreData.get(type.ordinal()));


		//FULL_HOUSE(11),        
		type = ScoreTypes.FULL_HOUSE;
		if (!player.scoreData.get(type.ordinal()).isUsed)
		{
			if (isTurnSkip)
				states.set(type.ordinal(), new SelectableTextFieldState(CROSS_OUT_TEXT, false, true, false));
			else
			{
				if (distinct(diceValues) == 2 && occurrencesOf(diceValues.get(2), diceValues) == 3)
					states.set(type.ordinal(), new SelectableTextFieldState(Integer.toString(25), false, true, false));					
				else
					states.set(type.ordinal(), new SelectableTextFieldState("", false, false, false));
			}
		}
		else
			states.set(type.ordinal(), player.scoreData.get(type.ordinal()));


		//SMALL_STRAIGHT(12),        
		type = ScoreTypes.SMALL_STRAIGHT;
		if (!player.scoreData.get(type.ordinal()).isUsed)
		{
			ArrayList<Integer> list = removeDuplicates(diceValues);

			if (isTurnSkip)
				states.set(type.ordinal(), new SelectableTextFieldState(CROSS_OUT_TEXT, false, true, false));
			else if (list.size() >= 4 && isInSequence(list))
				states.set(type.ordinal(), new SelectableTextFieldState("30", false, true, false));
			else
				states.set(type.ordinal(), new SelectableTextFieldState("", false, false, false));
		}
		else
			states.set(type.ordinal(), player.scoreData.get(type.ordinal()));

		//LARGE_STRAIGHT(13),
		type = ScoreTypes.LARGE_STRAIGHT;
		if (!player.scoreData.get(type.ordinal()).isUsed)
		{
			ArrayList<Integer> list = removeDuplicates(diceValues);

			if (isTurnSkip)
				states.set(type.ordinal(), new SelectableTextFieldState(CROSS_OUT_TEXT, false, true, false));
			else if (list.size() == 5 && isInSequence(list))
				states.set(type.ordinal(), new SelectableTextFieldState("40", false, true, false));
			else
				states.set(type.ordinal(), new SelectableTextFieldState("", false, false, false));
		}
		else
			states.set(type.ordinal(), player.scoreData.get(type.ordinal()));

		//CHANCE(15),            
		type = ScoreTypes.CHANCE;
		if (!player.scoreData.get(type.ordinal()).isUsed)
		{
			if (isTurnSkip)
				states.set(type.ordinal(), new SelectableTextFieldState(CROSS_OUT_TEXT, false, true, false));
			else // Anything is valid.
				states.set(type.ordinal(), new SelectableTextFieldState(Integer.toString(sum), false, true, false));					
		}
		else
			states.set(type.ordinal(), player.scoreData.get(type.ordinal()));


		//JAHTZEE(14), 
		type = ScoreTypes.JAHTZEE;
		// If Jahtzee not used
		if (!player.scoreData.get(type.ordinal()).isUsed)
		{
			// If skip mode, apply skip state
			if (isTurnSkip)
				states.set(type.ordinal(), new SelectableTextFieldState(CROSS_OUT_TEXT, false, true, false));
			
			// If only one distinct value in the set, must all be the same
			// Set Jahtzee selectable
			else if (distinct(diceValues) == 1)
				states.set(type.ordinal(), new SelectableTextFieldState("50", false, true, false));
			
			// Otherwise, not selectable
			else
				states.set(type.ordinal(), new SelectableTextFieldState("", false, false, false));
		}
		
		// Jahtzee was used
		else
		{
			// Check if Jahtzee was scored or crossed out and make sure we're not in cross out mode
			// No crossing out Jahtzee bonuses
			if (player.scoreData.get(type.ordinal()).fieldValue.equals("50") && !isTurnSkip)
			{
				
				// Jahtzee was scored, not crossed out
				// Check for Jahtzee
				if (distinct(diceValues) == 1)
				{
					
					// Is Jahtzee, see if first bonus is used. If not, allow selection and copy old values over
					if (!player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_1.ordinal()).isUsed)
					{
						states.set(ScoreTypes.JAHTZEE_BONUS_1.ordinal(), new SelectableTextFieldState("X", false, true, false));
						states.set(ScoreTypes.JAHTZEE_BONUS_1.ordinal(), player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_2.ordinal()));
						states.set(ScoreTypes.JAHTZEE_BONUS_1.ordinal(), player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_3.ordinal()));
					}

					// First bonus used, see if second bonus is used. If not, allow selection and copy old values over 
					else if (!player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_2.ordinal()).isUsed)
					{
						states.set(ScoreTypes.JAHTZEE_BONUS_2.ordinal(), new SelectableTextFieldState("X", false, true, false));
						states.set(ScoreTypes.JAHTZEE_BONUS_1.ordinal(), player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_2.ordinal()));
						states.set(ScoreTypes.JAHTZEE_BONUS_3.ordinal(), player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_3.ordinal()));
					}
					
					// Second bonus used, see if third bonus is used. If not, allow selection and copy old values over					
					else if (!player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_3.ordinal()).isUsed)
					{
						states.set(ScoreTypes.JAHTZEE_BONUS_3.ordinal(), new SelectableTextFieldState("X", false, true, false));
						states.set(ScoreTypes.JAHTZEE_BONUS_1.ordinal(), player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_2.ordinal()));
						states.set(ScoreTypes.JAHTZEE_BONUS_2.ordinal(), player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_3.ordinal()));
					}
					
					// All bonuses used, copy all old values over to new array
					else
					{
						states.set(ScoreTypes.JAHTZEE_BONUS_1.ordinal(), player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_1.ordinal()));
						states.set(ScoreTypes.JAHTZEE_BONUS_2.ordinal(), player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_2.ordinal()));
						states.set(ScoreTypes.JAHTZEE_BONUS_3.ordinal(), player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_3.ordinal()));
					}
				}
				
				// Not a Jahtzee. Copy old values over.
				else
				{
					states.set(ScoreTypes.JAHTZEE_BONUS_1.ordinal(), player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_1.ordinal()));
					states.set(ScoreTypes.JAHTZEE_BONUS_2.ordinal(), player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_2.ordinal()));
					states.set(ScoreTypes.JAHTZEE_BONUS_3.ordinal(), player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_3.ordinal()));
				}
			}
			
			// Jahtzee was crossed out. Can't use bonuses. Copy old values over.
			else
			{
				states.set(ScoreTypes.JAHTZEE_BONUS_1.ordinal(), player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_1.ordinal()));
				states.set(ScoreTypes.JAHTZEE_BONUS_2.ordinal(), player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_2.ordinal()));
				states.set(ScoreTypes.JAHTZEE_BONUS_3.ordinal(), player.scoreData.get(ScoreTypes.JAHTZEE_BONUS_3.ordinal()));
			}
		}
		return states;
	}

	// Utility methods
	
	public static SelectableTextFieldState processSingleValue(ScoreTypes type, Player player, boolean isSkip, ArrayList<Integer> diceValues)
	{
		if (!player.scoreData.get(type.ordinal()).isUsed)
		{
			if (isSkip)
				return new SelectableTextFieldState(CROSS_OUT_TEXT, false, true, false);
			else
			{
				int count = 0;
				for (Integer elem : diceValues)
					if (elem.equals(type.ordinal() + 1))
						count++;
				if (count == 0)
					return new SelectableTextFieldState("", false, false, false);
				else
					return new SelectableTextFieldState(Integer.toString(count * (type.ordinal() + 1)), false, true, false);
			}
		}
		else // Preserve the original data
			return player.scoreData.get(ScoreTypes.ACES.ordinal());
	}

	public static int sumIntArrayList(ArrayList<Integer> list)
	{
		int sum = 0;

		for (Integer elem : list)
			sum += elem.intValue();

		return sum;
	}

	/**
	 * Get the number of distinct values in the array
	 * @param list
	 * The list to search through
	 * @return
	 */
	public static int distinct(ArrayList<Integer> list)
	{
		int total = 0;
		int[] counts = new int[] { 0, 0, 0, 0, 0, 0 };

		for (Integer elem : list)
			counts[elem - 1]++;

		for (Integer elem : counts)
			if (elem > 0)
				total++;

		return total;
	}


	/**
	 * Get the number of times value occurs in list
	 * @param value
	 * The value to search for
	 * @param list
	 * The list to search through
	 * @return
	 */
	public static int occurrencesOf(Integer value, ArrayList<Integer> list)
	{
		int total = 0;
		for (Integer elem : list)
			if (elem.equals(value))
				total++;
		return total;
	}

	/**
	 * Returns the array passed to the method with duplicates removed
	 * @param list
	 * The ArrayList to remove duplicates from
	 * @return
	 * The array without duplicates
	 */
	public static ArrayList<Integer> removeDuplicates(ArrayList<Integer> list)
	{
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		for (Integer elem : list)
			if (!tempList.contains(elem))
				tempList.add(elem);
		// Shouldn't be needed, but doing it anyway
		Collections.sort(tempList);

		return tempList;
	}

	/**
	 * Check if the array is in numerical sequence { 1, 2, 3, 4, 5, ..., n - 2, n - 1, n }
	 * @param list
	 * The list to check
	 * @return
	 * Returns true if each nth integer's value is one less than the (n+1)th integer's value
	 * Return false otherwise
	 * O(n) runtime if returning true 
	 */
	public static boolean isInSequence(ArrayList<Integer> list)
	{
		for (int i = 0 ; i < list.size() - 2; i++)
			if (list.get(i) != (list.get(i + 1) - 1))
				return false;
		return true;
	}
}
