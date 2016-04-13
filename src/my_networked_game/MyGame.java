package my_networked_game;

import java.io.Serializable;
import java.util.ArrayList;

import gameNet.GameControl;
import gameNet.GameNet_CoreGame;

public class MyGame extends GameNet_CoreGame implements Serializable
{
	private static final long serialVersionUID = 1L;

	private ArrayList<String> playerID_List = new ArrayList<String>();
	
	private String currentPlayerID = "";
	
	public MyGame()
	{
		
	}
	
	/*
	 * Process takes one input of type Object. 
	 * Note that this input will actually be your game's MyGameInput class. 
	 * The method process returns a class of type Object which will actually be your MyGameOutput class. 
	 * Note that the GameNet_CoreGame abstract class contains your Server Code.
	 */
	@Override 
	public Object process(Object inputObj)
	{
        MyGameInput myGameInput = (MyGameInput)inputObj;
        
        switch (myGameInput.getInputType())
        {
        case REGISTER_PLAYER:
        	System.out.println("Player [" + myGameInput.getUsername() + "] registered with ID [" + myGameInput.getPlayerID() + "]");
        	
        case MAKE_PLAY:
        	
        }
        
        
        MyGameOutput myGameOutput = new MyGameOutput();

        return myGameOutput;
	}
	
	@Override
	public void startGame (GameControl gameControl)
	{
		
	}

}
