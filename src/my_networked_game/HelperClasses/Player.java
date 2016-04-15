package my_networked_game.HelperClasses;

import java.io.Serializable;

import my_networked_game.Enums.ScoreTypes;

// Used to identify players and to populate the list boxes

public class Player implements Serializable, Comparable<Player>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5653482748891502216L;
	
	SelectableTextFieldState[] scoreData = new SelectableTextFieldState[ScoreTypes.values().length];
	
	String playerName = "",
		   playerID = "";
	
	int playerScore = 0;
	
	public Player(String name, String ID, int score)
	{
		playerName = name;
		playerID = ID;
		playerScore = score;
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
