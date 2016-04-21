package my_networked_game.HelperClasses;

import java.io.Serializable;
import java.util.ArrayList;

import my_networked_game.Enums.ScoreTypes;

// Used to identify players and to populate the list boxes

public class Player implements Serializable, Comparable<Player>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5653482748891502216L;
	
	ArrayList<SelectableTextFieldState> scoreData = null;
	
	String playerName = "",
		   playerID = "";
	
	int playerScore = 0;
	
	public Player(String name, String ID, int score)
	{
		playerName = name;
		playerID = ID;
		playerScore = score;
		
		scoreData = ScoreSheetBuilder.getNewPlayerScoreSheet();
	}
	
	/**
	 * Copy constructor
	 * @param p
	 * Player object to copy
	 */
	public Player(Player p)
	{
		scoreData = new ArrayList<SelectableTextFieldState>(ScoreTypes.values().length);
		
		
		// this assumes going from first to last element. will it?
		for (SelectableTextFieldState state : p.scoreData)
			scoreData.add(new SelectableTextFieldState(state));
		
		scoreData = p.scoreData;
		
		playerID = p.playerID;
		playerName = p.playerName;
		playerScore = p.playerScore;
	}
	
	public String getName()
	{
		return playerName;
	}
	
	public String getID()
	{
		return playerID;
	}
	
	public int getScore()
	{
		return playerScore;
	}

	@Override
	public int compareTo(Player o)
	{
		return o.playerID.compareTo(this.playerID);
	}
	
	
	// We override these methods so that equals() will work how we want
	@Override
	public int hashCode()
	{
		return playerID.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
	       if (!(obj instanceof Player))
	            return false;
	        if (obj == this)
	            return true;

	        return (((Player)obj).playerID == this.playerID);
	}
}
