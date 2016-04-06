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

import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


class MyUserInterface extends JFrame implements GameNet_UserInterface, ActionListener
{
    private GamePlayer myGamePlayer;

    private MyGameInput myGameInput;
    
    private MainGamePanel mainGamePanel = new MainGamePanel();

    public MyUserInterface()
    {
        super("Jahtzee!");
        this.myLayout();
        this.add(mainGamePanel, BorderLayout.CENTER);
        // Add secondary panels to center... ?
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
        this.setLayout(new BorderLayout());
        this.setSize(800, 600);
        this.setVisible(true);
    }
    
    // Inner Class
    class Termination extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            exitProgram();
        }
    }
    
    private class MainGamePanel extends JPanel
    {
    	ScoreCardPanel scoreCardPanel = new ScoreCardPanel();
    	
    	public MainGamePanel()
		{
    		super();
    		this.setLayout(new GridLayout(0, 2));
    		this.add(scoreCardPanel);
		}
    }
    
    private class JahtzeeBonusPanel extends JPanel
    {
    	JCheckBox firstBonus = null,
    			  secondBonus = null,
    			  thirdBonus = null;
    	
    	// Don't want to instantiate this constructor (debug code)
    	private JahtzeeBonusPanel() { throw new RuntimeException("JahtzeeBonusPanel: Don't instantiate via argumentless constructor"); }
    	
    	public JahtzeeBonusPanel(JCheckBox first, JCheckBox second, JCheckBox third)
		{
    		firstBonus = first;
    		secondBonus = second;
    		thirdBonus = third;
		}
    }
    
    private class ScoreCardPanel extends JPanel
    {
    	ImageIcon iconOne 			= new ImageIcon("Resources\\One.png"),
    			  iconTwo 			= new ImageIcon("Resources\\Two.png"),
    			  iconThree 		= new ImageIcon("Resources\\Three.png"),
    			  iconFour 			= new ImageIcon("Resources\\Four.png"),
    			  iconFive 			= new ImageIcon("Resources\\Five.png"),
    			  iconSix 			= new ImageIcon("Resources\\Six.png");
    	
    	JLabel  lblAces 			= new JLabel("Aces", SwingConstants.RIGHT),
    			lblTwos 			= new JLabel("Twos", SwingConstants.RIGHT),
    			lblThrees 			= new JLabel("Threes", SwingConstants.RIGHT),
    			lblFours 			= new JLabel("Fours", SwingConstants.RIGHT),
    			lblFives 			= new JLabel("Fives", SwingConstants.RIGHT),
    			lblSixes 			= new JLabel("Sixes", SwingConstants.RIGHT),
    			lblTotal 			= new JLabel("TOTAL SCORE", SwingConstants.RIGHT),
    			lblBonus 			= new JLabel("BONUS", SwingConstants.RIGHT),
    			lblUpperGrand 		= new JLabel("TOTAL", SwingConstants.RIGHT),
    			lbl3Kind 			= new JLabel("3 of a kind", SwingConstants.RIGHT),
    			lbl4Kind 			= new JLabel("4 of a kind", SwingConstants.RIGHT),
    			lblFullHouse 		= new JLabel("Full House", SwingConstants.RIGHT),
    			lblSmStraight 		= new JLabel("Sm Straight", SwingConstants.RIGHT),
    			lblLgStraight 		= new JLabel("Lg Straight", SwingConstants.RIGHT),
    			lblJahtzee 			= new JLabel("JAHTZEE", SwingConstants.RIGHT),
    			lblChance 			= new JLabel("Chance", SwingConstants.RIGHT),
    			lblJahtzeeBonus 	= new JLabel("JAHTZEE BONUS", SwingConstants.RIGHT),
    			lblUpperTotal 		= new JLabel("Upper Section Total", SwingConstants.RIGHT),
    			lblLowerTotal 		= new JLabel("Lower Section Total", SwingConstants.RIGHT),
    			lblGrandTotal 		= new JLabel("Grand Total", SwingConstants.RIGHT);
    	
    	JTextField txtAces 			= new JTextField(),
    			   txtTwos 			= new JTextField(),
    			   txtThrees 		= new JTextField(),
    			   txtFours 		= new JTextField(),
    			   txtFives 		= new JTextField(),
    			   txtSixes 		= new JTextField(),
    			   txtTotal 		= new JTextField(),
    			   txtBonus 		= new JTextField(),
    			   txtUpperGrand	= new JTextField(),
    			   txt3Kind 		= new JTextField(),
    			   txt4Kind 		= new JTextField(),
    			   txtFullHouse 	= new JTextField(),
    			   txtSmStraight 	= new JTextField(),
    			   txtLgStraight 	= new JTextField(),
    			   txtJahtzee 		= new JTextField(),
    			   txtChance 		= new JTextField(),
    			   txtJahtzeeBonus 	= new JTextField(),
    			   txtUpperTotal 	= new JTextField(),
    			   txtLowerTotal 	= new JTextField(),
    			   txtGrandTotal 	= new JTextField();
    	
    	public ScoreCardPanel()
    	{
    		super();
    		this.setLayout(new GridLayout(0, 2));
    		this.add(lblAces);
    		this.add(txtAces);
    		this.add(lblTwos);
    		this.add(txtTwos);
    		this.add(lblThrees);
    		this.add(txtThrees);
    		this.add(lblFours);
    		this.add(txtFours);
    		this.add(lblFives);
    		this.add(txtFives);
    		this.add(lblSixes);
    		this.add(txtSixes);
    		this.add(lblTotal);
    		this.add(txtTotal);
    		this.add(lblBonus);
    		this.add(txtBonus);
    		this.add(lblUpperGrand);
    		this.add(txtUpperGrand);
    		this.add(lbl3Kind);
    		this.add(txt3Kind);
    		this.add(lbl4Kind);
    		this.add(txt4Kind);
    		this.add(lblFullHouse);
    		this.add(txtFullHouse);
    		this.add(lblSmStraight);
    		this.add(txtSmStraight);
    		this.add(lblLgStraight);
    		this.add(txtLgStraight);
    		this.add(lblJahtzee);
    		this.add(txtJahtzee);
    		this.add(lblChance);
    		this.add(txtChance);
    		this.add(lblJahtzeeBonus);
    		this.add(txtJahtzeeBonus);
    		this.add(lblUpperTotal);
    		this.add(txtUpperTotal);
    		this.add(lblLowerTotal);
    		this.add(txtLowerTotal);
    		this.add(lblGrandTotal);
    		this.add(txtGrandTotal);
    	}
    }
    
    private class LobbyPanel extends JPanel
    {
    	public LobbyPanel()
    	{
    		super();
    	}
    }

}
