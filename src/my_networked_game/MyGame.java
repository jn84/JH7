package my_networked_game;

import java.io.Serializable;

import gameNet.GameNet_CoreGame;

public class MyGame extends GameNet_CoreGame implements Serializable
{
	private static final long serialVersionUID = 1L;

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
        
        MyGameOutput myGameOutput = new MyGameOutput();

        return myGameOutput;
	}

}
