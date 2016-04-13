package my_networked_game;

import gameNet.GameCreator;
import gameNet.GameNet_CoreGame;

public class MyMain extends GameCreator
{   
	public GameNet_CoreGame createGame()
	{
		return new MyGame();
	}

	public static void main(String[] args) 
	{   
		MyMain myMain = new MyMain();
		MyUserInterface myUserInterface = new MyUserInterface();

		myMain.enterGame(myUserInterface); 
	}
}
