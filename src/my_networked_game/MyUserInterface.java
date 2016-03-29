package my_networked_game;

import gameNet.GameNet_UserInterface;
import gameNet.GamePlayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


class MyUserInterface extends JFrame 
    implements GameNet_UserInterface, ActionListener
{
    private GamePlayer myGamePlayer;

    private MyGameInput myGameInput;

    public MyUserInterface()
    {
        super("Chat Room");
    }
    
    
    public void startUserInterface (GamePlayer player)
    {
        myGamePlayer = player;
        //myGameInput = new MyGameInput(player.getPlayerName());
        
        // Boring screen things
        myLayout();
        
    }
    
    
    public void receivedMessage(Object ob)
    {
    	
    }
    
    public void actionPerformed(ActionEvent e) 
    {
    	
    }
    
    // Nice to let people know you are leaving
    private void exitProgram()
    {
        myGamePlayer.doneWithGame();
        System.exit(0);
    }
    
    private void myLayout()
    {
        setLayout(new BorderLayout());
        setSize(800, 600);
        setVisible(true);
    }
    
    // Inner Class
    class Termination extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            exitProgram();
        }
    }

}
