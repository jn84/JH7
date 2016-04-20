package my_networked_game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import gameNet.GameNet_UserInterface;
import gameNet.GamePlayer;
import my_networked_game.Enums.MyGameInputType;
import my_networked_game.Enums.ScoreTypes;
import my_networked_game.HelperClasses.Player;
import my_networked_game.HelperClasses.SelectableTextField;
import my_networked_game.HelperClasses.SelectableTextFieldGroup;
import my_networked_game.HelperClasses.SelectableTextFieldState;


class MyUserInterface extends JFrame implements GameNet_UserInterface, ActionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1558976730059939642L;
	
	public static ImageIcon  dieDefault_1 = new ImageIcon("Resources\\One.png"),  
			 				 dieDefault_2 = new ImageIcon("Resources\\Two.png"),  
			 				 dieDefault_3 = new ImageIcon("Resources\\Three.png"),
			 				 dieDefault_4 = new ImageIcon("Resources\\Four.png"), 
			 				 dieDefault_5 = new ImageIcon("Resources\\Five.png"), 
			 				 dieDefault_6 = new ImageIcon("Resources\\Six.png"),
			 				 dieBlank 	  = new ImageIcon("Resources\\Blank.png");
	
	private boolean isMyTurn = false;
	
	private DiceSet localDiceSet = new DiceSet();

	private GamePlayer myGamePlayer;

    private MyGameInput myGameInput;
    
    private SelectableTextFieldGroup selectableTextFields = new SelectableTextFieldGroup();
    
    private PlayerListPanel playerListPanel = new PlayerListPanel();
    
    private Player thisPlayer = null;
    
    private LobbyPanel lobbyPanel = new LobbyPanel();
    
    private DicePanel dicePanel = new DicePanel();
    
    private JButton submitButton = new JButton("Submit Score"),
    				rollButton 	 = new JButton("Roll Dice"),
    				skipButton   = new JButton("Skip Turn");
    
    private ButtonPanel buttonPanel = new ButtonPanel();
    
    private GameStatusUpdatePanel gameStatusUpdatePanel = new GameStatusUpdatePanel();
    		    
    // Should be last
    private MainGamePanel mainGamePanel = new MainGamePanel();

    public MyUserInterface()
    {
        super("Jahtzee!");
    }
    
    
    public void startUserInterface (GamePlayer player)
    {
        myGamePlayer = player;
        registerPlayer();
        
        // Boring screen things 
        this.myLayout();
        //this.add(mainGamePanel, BorderLayout.CENTER);
        this.add(lobbyPanel, BorderLayout.CENTER);
        
        submitButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				submitClick();
			}
		});
        
        rollButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				rollClick();
			}
		});
        
        skipButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				skipClick();
			}
		});
    }

    /**
     * Disables or enables the various control in the user interface
     * Used to control which player's turn it is.
     * 
     * @param value
     * True to enable, false to disable
     */
    public void setUserInterfaceInteractable()
    {
    	submitButton.setEnabled(isMyTurn);
    	skipButton.setEnabled(isMyTurn);
    	rollButton.setEnabled(isMyTurn);
    	dicePanel.setEnabled(isMyTurn);
    }
    
    public void submitClick()
    {
    	//TODO Define what happens when the submit button is clicked.
		gameStatusUpdatePanel.addServerOutputText("SUBMIT CLICK!");
    }
    
    public void skipClick()
    {
    	int confirmResult = JOptionPane.showConfirmDialog(
    							this, 
    							"You have chosen to skip your turn.\nIf you do this, you will have to cross out one of your score boxes.\nAre you sure you want to skip?", 
    							"Confirm Skip Turn", 
    							JOptionPane.YES_NO_OPTION);
    	if (confirmResult != JOptionPane.YES_OPTION)
    	{
    		System.out.println("Skip aborted");
    		return;
    	}
    	else
    		System.out.println("Turn skipped");
    	
    	
    	
    	// TODO Define what happens when the skip button is clicked.
		gameStatusUpdatePanel.addServerOutputText("SKIP CLICK!");
    }
    
    public void rollClick()
    {
    	// when we roll, we want to make sure everything in the score sheet is unselected
    }
    
    public void registerPlayer()
    {
        Random r = new Random();
        thisPlayer = new Player(myGamePlayer.getPlayerName(), Integer.toString(r.nextInt(10000000)), 0); 
        myGamePlayer.sendMessage(new MyGameInput(thisPlayer));
    }
    
    public void receivedMessage(Object ob)
    {
    	MyGameOutput myGameOutput = (MyGameOutput)ob;
    	
    	switch (myGameOutput.getOutputType())
    	{
    	case PLAYER_REGISTERED:
    		playerListPanel.addPlayer(myGameOutput.getActivePlayer());
    		break;
    	case PLAYER_UNREGISTERED:
    		playerListPanel.removePlayer(myGameOutput.getActivePlayer());
    		break;
    	case GAME_BEGIN:
    		this.remove(lobbyPanel);
    		this.add(mainGamePanel);
    		// TODO also should get first player package (dice and scoresheets)
    		break;
		default:
			break;
    		
    	}
    }
    
    public void generateInput(MyGameInputType type)
    {
    	myGameInput = new MyGameInput(type);
    	
    	switch (type)
    	{
    	case BEGIN_GAME:
    		break;
    	default:
    		break;
    	}
    	
    	myGamePlayer.sendMessage(myGameInput);
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
    
    private void updateSelectableTextFields(SelectableTextFieldState[] st)
    {
    	selectableTextFields.setStates(st);
    }
    
    // Inner Class
    class Termination extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            exitProgram();
        }
    }
    
    private class LobbyPanel extends JPanel
    {
    	private JButton btnStartGame = new JButton("Start Game");
    	
    	public LobbyPanel()
    	{
    		super();
    		this.setLayout(new BorderLayout());
    		this.add(playerListPanel, BorderLayout.CENTER);
    		this.add(btnStartGame, BorderLayout.SOUTH);
    		// TODO If Time: Make disabled for everyone except host
    		
    		btnStartGame.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					MyUserInterface.this.buildInput(MyGameInputType.BEGIN_GAME);
				}
			});
    	}
    }
    
    // used in lobby and in main game
    private class PlayerListPanel extends JPanel
    {
		private static final long serialVersionUID = 2929935231853646535L;

		private DefaultListModel<Player> modelPlayers = new DefaultListModel<Player>();
    	
    	private JList<Player> lstPlayers = new JList<Player>(modelPlayers);
    	
    	public PlayerListPanel()
    	{
    		super();
    		lstPlayers.setCellRenderer(new PlayerListRenderer());
    		this.setLayout(new BorderLayout());
    		this.add(lstPlayers, BorderLayout.CENTER);
    	}
    	
    	public void addPlayer(Player p)
    	{
    		if (!modelPlayers.contains(p))
    			modelPlayers.addElement(p);
    	}
    	
    	public void removePlayer(Player p)
    	{
    		if (modelPlayers.contains(p))
    			modelPlayers.removeElement(p);
    	}
    	
    	public void updatePlayer(Player p)
    	{
    		if (!modelPlayers.contains(p))
    			return;
    		
    		// We can use indexOf since equals is defined in the Player class
    		modelPlayers.set(modelPlayers.indexOf(p), p);
    	}
    	
    	class PlayerListRenderer extends DefaultListCellRenderer
    	{

			/**
			 * 
			 */
			private static final long serialVersionUID = -5399505231573174050L;

			public Component getListCellRendererComponent(JList list, Object playerObj, int index, boolean isSelected, boolean cellHasFocus)
    	    {
    	        super.getListCellRendererComponent(list, playerObj, index, isSelected, cellHasFocus);

    	        Player player = (Player)playerObj;
    	        setText(player.getName() + " (" + player.getScore() + ")");

    	        return this;
    	    }
    	}
    }
    
    
    // Right side panel (dice, buttons, player list)
    private class MainGamePanel extends JPanel
    {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 8419567275250570950L;
		
		ScoreCardPanel scoreCardPanel = new ScoreCardPanel();
		GamePlayPanel gamePlayPanel = new GamePlayPanel(dicePanel);
    	
    	public MainGamePanel()
		{
    		super();
    		this.setLayout(new GridLayout(0, 2));
    		this.add(scoreCardPanel);
    		this.add(gamePlayPanel);
		}
    }
    
    private class ScoreCardPanel extends JPanel
    {
		private static final long serialVersionUID = -2349686691308606730L;
    	
    	JLabel  lblUpperHeader		= new JLabel("UPPER SECTION", SwingConstants.CENTER),
    			lblLowerHeader		= new JLabel("LOWER SECTION", SwingConstants.CENTER),
    			lblScoreHeader_1	= new JLabel("SCORE", SwingConstants.CENTER),
    			lblScoreHeader_2	= new JLabel("SCORE", SwingConstants.CENTER),
    			lblAces 			= new JLabel("Aces  ", SwingConstants.RIGHT),
    			lblTwos 			= new JLabel("Twos  ", SwingConstants.RIGHT),
    			lblThrees 			= new JLabel("Threes  ", SwingConstants.RIGHT),
    			lblFours 			= new JLabel("Fours  ", SwingConstants.RIGHT),
    			lblFives 			= new JLabel("Fives  ", SwingConstants.RIGHT),
    			lblSixes 			= new JLabel("Sixes  ", SwingConstants.RIGHT),
    			lblTotal 			= new JLabel("TOTAL SCORE  ", SwingConstants.RIGHT),
    			lblBonus 			= new JLabel("BONUS  ", SwingConstants.RIGHT),
    			lblUpperGrand 		= new JLabel("TOTAL  ", SwingConstants.RIGHT),
    			lbl3Kind 			= new JLabel("3 of a kind  ", SwingConstants.RIGHT),
    			lbl4Kind 			= new JLabel("4 of a kind  ", SwingConstants.RIGHT),
    			lblFullHouse 		= new JLabel("Full House  ", SwingConstants.RIGHT),
    			lblSmStraight 		= new JLabel("Sm Straight  ", SwingConstants.RIGHT),
    			lblLgStraight 		= new JLabel("Lg Straight  ", SwingConstants.RIGHT),
    			lblJahtzee 			= new JLabel("JAHTZEE  ", SwingConstants.RIGHT),
    			lblChance 			= new JLabel("Chance  ", SwingConstants.RIGHT),
    			lblJahtzeeBonus_1 	= new JLabel("JAHTZEE", SwingConstants.CENTER),
    			lblJahtzeeBonus_2 	= new JLabel("BONUS", SwingConstants.CENTER),
    			lblUpperTotal 		= new JLabel("Upper Section Total  ", SwingConstants.RIGHT),
    			lblLowerTotal 		= new JLabel("Lower Section Total  ", SwingConstants.RIGHT),
    			lblGrandTotal 		= new JLabel("Grand Total  ", SwingConstants.RIGHT);
    	
	    SelectableTextField txtAces 			= selectableTextFields.getField(ScoreTypes.ACES),
	    		   			txtTwos 			= selectableTextFields.getField(ScoreTypes.TWOS),
			    		    txtThrees 			= selectableTextFields.getField(ScoreTypes.THREES),
			    		    txtFours 			= selectableTextFields.getField(ScoreTypes.FOURS),
			    		    txtFives 			= selectableTextFields.getField(ScoreTypes.FIVES),
			    		    txtSixes 			= selectableTextFields.getField(ScoreTypes.SIXES),
			    		    txtTotal 			= selectableTextFields.getField(ScoreTypes.UPPER_SUB_TOTAL),
			    		    txtBonus 			= selectableTextFields.getField(ScoreTypes.UPPER_BONUS),
			    		    txtUpperGrand		= selectableTextFields.getField(ScoreTypes.UPPER_GRAND_TOTAL),
			    		    txt3Kind 			= selectableTextFields.getField(ScoreTypes.KIND_3),
			    		    txt4Kind 			= selectableTextFields.getField(ScoreTypes.KIND_4),
			    		    txtFullHouse 		= selectableTextFields.getField(ScoreTypes.FULL_HOUSE),
			    		    txtSmStraight 		= selectableTextFields.getField(ScoreTypes.SMALL_STRAIGHT),
			    		    txtLgStraight 		= selectableTextFields.getField(ScoreTypes.LARGE_STRAIGHT),
			    		    txtJahtzee 			= selectableTextFields.getField(ScoreTypes.JAHTZEE),
			    		    txtChance 			= selectableTextFields.getField(ScoreTypes.CHANCE),
			    		    txtJahtzeeBonus_1	= selectableTextFields.getField(ScoreTypes.JAHTZEE_BONUS_1),
			    		    txtJahtzeeBonus_2	= selectableTextFields.getField(ScoreTypes.JAHTZEE_BONUS_2),
			    		    txtJahtzeeBonus_3	= selectableTextFields.getField(ScoreTypes.JAHTZEE_BONUS_3),
		    			    txtJahtzeeBonusSum	= selectableTextFields.getField(ScoreTypes.JAHTZEE_BONUS_TOTAL),
			    		    txtUpperTotal 		= selectableTextFields.getField(ScoreTypes.FINAL_UPPER_GRAND_TOTAL),
			    		    txtLowerTotal 		= selectableTextFields.getField(ScoreTypes.FINAL_LOWER_GRAND_TOTAL),
			    		    txtGrandTotal 		= selectableTextFields.getField(ScoreTypes.FINAL_GRAND_TOTAL);
		    	            
    	JahtzeeBonusPanel jahtzeeBonusPanel = new JahtzeeBonusPanel(txtJahtzeeBonus_1, txtJahtzeeBonus_2, txtJahtzeeBonus_3);
    	
    	public ScoreCardPanel()
    	{
    		super();
    		
    		lblAces.setToolTipText("<html>Count and Add<br>Only Aces</html>");
    		lblTwos.setToolTipText("<html>Count and Add<br>Only Twos</html>"); 			
    		lblThrees.setToolTipText("<html>Count and Add<br>Only Threes</html>"); 			
    		lblFours.setToolTipText("<html>Count and Add<br>Only Fours</html>"); 			
    		lblFives.setToolTipText("<html>Count and Add<br>Only Fives</html>"); 			
    		lblSixes.setToolTipText("<html>Count and Add<br>Only Sixes</html>"); 			
    		lblTotal.setToolTipText("<html>Sum of scores<br>Aces through Sixes</html>"); 			
    		lblBonus.setToolTipText("<html>If total score is greater than 62<br>Add 35 bonus points</html>");			
    		lblUpperGrand.setToolTipText("<html>Total upper section score<br>Total Score + Bonus</html>"); 		
    		lbl3Kind.setToolTipText("<html>Add Total<br>Of All Dice</html>"); 			
    		lbl4Kind.setToolTipText("<html>Add Total<br>Of All Dice</html>"); 			
    		lblFullHouse.setToolTipText("<html>A Pair and A Triplet<br>SCORE 25</html>"); 		
    		lblSmStraight.setToolTipText("<html>Sequence of 4<br>SCORE 30</html>"); 		
    		lblLgStraight.setToolTipText("<html>Sequence of 5<br>SCORE 40</html>"); 		
    		lblJahtzee.setToolTipText("<html>5 of a kind<br>SCORE 50</html>"); 			
    		lblChance.setToolTipText("<html>Score Total OF All 5 Dice</html>"); 			
    		lblJahtzeeBonus_1.setToolTipText("<html>CHECK FOR<br>EACH BONUS</html>"); 	
    		lblJahtzeeBonus_2.setToolTipText("<html>SCORE 100<br>PER CHECK</html>");  
    		lblUpperTotal.setToolTipText("<html>Of Upper<br>Section</html>"); 		
    		lblLowerTotal.setToolTipText("<html>Of Lower<br>Section</html>"); 		
    		lblGrandTotal.setToolTipText("<html>Upper + Lower</html>"); 		
    		
    		this.setLayout(new GridLayout(0, 2));
    		this.add(lblUpperHeader);
    		this.add(lblScoreHeader_1);
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
    		this.add(lblLowerHeader);
    		this.add(lblScoreHeader_2);
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
    		this.add(lblJahtzeeBonus_1);
    		this.add(jahtzeeBonusPanel);
    		this.add(lblJahtzeeBonus_2);
    		this.add(txtJahtzeeBonusSum);
    		this.add(lblUpperTotal);
    		this.add(txtUpperTotal);
    		this.add(lblLowerTotal);
    		this.add(txtLowerTotal);
    		this.add(lblGrandTotal);
    		this.add(txtGrandTotal);
    		
    		// XXX Debug selectable text field
    		for (SelectableTextField field : selectableTextFields)
    		{
    			field.setState(new SelectableTextFieldState("///////////////////////////////", false, true));
    		}
    		// End Debug
    	
    	}
    }
    
    private class GamePlayPanel extends JPanel
    {
    	/**
		 * 
		 */
		private static final long serialVersionUID = -6209420231584759845L;

		public GamePlayPanel(DicePanel dp)
    	{
			super();
			
			this.setLayout(new GridLayout(4, 1));
			this.add(dicePanel);
			this.add(buttonPanel);
			this.add(gameStatusUpdatePanel);
			this.add(playerListPanel);
    	}
    }

    //
    //	Dice Panel classes
    //
    private class DicePanel extends JPanel
    {
    	ArrayList<JLabel> heldLabels = new ArrayList<JLabel>(5);
    	
    	ArrayList<JCheckBox> holdCheckBoxes = new ArrayList<JCheckBox>(5);
    	
    	DiceLabelPanel diceLabelPanel = null;
    	
    	DiceImagePanel diceImagePanel = null;
    	
    	DiceCheckBoxPanel diceCheckBoxPanel = null;

    	public DicePanel()
    	{
    		this.setLayout(new BorderLayout());
    		
    		for (int i = 0; i < 5; i++)
    		{
    			heldLabels.add(new JLabel("Held", JLabel.CENTER));
    			heldLabels.get(i).setVisible(false);
    			holdCheckBoxes.add(new JCheckBox("Hold"));
    		}
    		
    		diceLabelPanel = new DiceLabelPanel(heldLabels);
    		diceImagePanel = new DiceImagePanel();
    		diceCheckBoxPanel = new DiceCheckBoxPanel(holdCheckBoxes);
    		
    		this.add(diceLabelPanel, BorderLayout.NORTH);
    		this.add(diceImagePanel, BorderLayout.CENTER);
    		this.add(diceCheckBoxPanel, BorderLayout.SOUTH);
    		
    		for (JCheckBox elem : holdCheckBoxes)
    		{
    			elem.addActionListener(new ActionListener()
				{
					
					@Override
					public void actionPerformed(ActionEvent e)
					{
						JCheckBox obj = (JCheckBox) e.getSource();
						int index = holdCheckBoxes.indexOf(obj);

						diceImagePanel.setSelected(index, obj.isSelected());
						heldLabels.get(index).setVisible(obj.isSelected());
						
						localDiceSet.setHeld(index, obj.isSelected());
					}
				});
    		}
    	}
    	
    	public void updateDice()
    	{
    		// localDiceSet should have been changed
    		
    		for (int index = 0; index < 5; index++)
    		{
				diceImagePanel.setSelected(index, false);
				heldLabels.get(index).setVisible(false);
    		}
    		
    		diceImagePanel.updateImages();
    	}

    	/**
    	 * Set the control to a non-interactive state
    	 * true - If it is this player's turn
    	 * false - If it is NOT this player's turn
    	 */
    	public void setEnabled(boolean value)
    	{
    		for (JCheckBox elem : holdCheckBoxes)
    			elem.setEnabled(value);
    	}

    }
    
    private class DiceLabelPanel extends JPanel
    {
    	/**
		 * 
		 */
		private static final long serialVersionUID = -621269843615348679L;

		public DiceLabelPanel(ArrayList<JLabel> labels)
		{
    		super();
    		this.setLayout(new GridLayout(1, 5));
    		for (JLabel elem : labels)
    		{
    			this.add(elem);
    			elem.setVisible(false);
    		}
		}
    }
    
    // TODO If there are issues with dice being held, this might be the culprit
    private class DiceImagePanel extends JPanel
    {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 4284742933260010417L;
		
		ArrayList<DiceObjPanel> diceObjPanelList = new ArrayList<DiceObjPanel>(5);
    	
    	public DiceImagePanel()
    	{
    		this.setLayout(new GridLayout(1, 5));
    		
    		for (int i = 0; i < localDiceSet.getDice().size(); i++)
    			diceObjPanelList.add(new DiceObjPanel(localDiceSet.getDice().get(i).getValue()));
    		
    		for (int i = 0; i < diceObjPanelList.size(); i++)
    			this.add(diceObjPanelList.get(i));
    		
    	}
    	
    	public void setSelected(int index, boolean value)
    	{
    		diceObjPanelList.get(index).setSelected(value);
    	}
    	
    	public void updateImages()
    	{
    		for (int i = 0; i < 5; i++)
    		{
    			diceObjPanelList.get(i).updateImage(localDiceSet.getDice().get(i).getValue());
    		}
    			
    	}
    }
    
    
    // TODO Maybe make the images clickable if there's time.
    private class DiceObjPanel extends JPanel
    {
    	/**
		 * 
		 */
		private static final long serialVersionUID = -2604477054890798598L;
		JLabel imageLabel = null;
    	
    	public DiceObjPanel(int dieValue)
    	{
    		this.setLayout(new BorderLayout());
    		
    		imageLabel = new JLabel(getImage(dieValue));
    		this.add(imageLabel, BorderLayout.CENTER);
    	}
    	
    	public void updateImage(int dieValue)
    	{
    		imageLabel.setIcon(getImage(dieValue));
    		repaint();
    	}
    	
    	public ImageIcon getImage(int dieValue)
    	{
    		switch (dieValue)
    		{
    		case 1:
    			return dieDefault_1;
    		case 2:
    			return dieDefault_2;
    		case 3:
    			return dieDefault_3;
    		case 4:
    			return dieDefault_4;
    		case 5:
    			return dieDefault_5;
    		case 6:
    			return dieDefault_6;
    		default:
    			return dieBlank;
    		}
    	}
    	
    	public void setSelected(boolean value)
    	{
    		System.out.println("Setting die background");
    		if (value)
    		{
    			this.setBackground(Color.RED);
    			repaint();
    		}
    		else
    		{
    			this.setBackground(null);
    			repaint();
    		}
    	}
    }
    
    private class DiceCheckBoxPanel extends JPanel
    {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 6072981989061414353L;

		public DiceCheckBoxPanel(ArrayList<JCheckBox> checkList)
		{
    		super();
    		this.setLayout(new GridLayout(1, 5));
    		for (int i = checkList.size() - 1; i >= 0; i--)
    			this.add(checkList.get(i), JCheckBox.CENTER);
		}
    }
    
    private class ButtonPanel extends JPanel
    {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 2084941331920044377L;

    	public ButtonPanel()
    	{
    		this.setLayout(new GridLayout(1, 3));
    		submitButton.setToolTipText(
    			"Click to submit the scoring play that you have selected");
    		rollButton.setToolTipText(
    			"Click to to roll any unheld die");
    		skipButton.setToolTipText(
    			"CANNOT BE UNDONE");
    		
    		this.add(rollButton);
    		this.add(submitButton);
    		this.add(skipButton);
    	}
    }
    
    private class GameStatusUpdatePanel extends JPanel
    {
    	/**
		 * 
		 */
		private static final long serialVersionUID = -6187609582103465670L;
		JTextArea textArea = new JTextArea();
    	JScrollPane scrollPane = new JScrollPane(
    			textArea, 
    			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
    			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	
    	public GameStatusUpdatePanel()
		{
    		this.setLayout(new BorderLayout());
    		
    		textArea.setLineWrap(true);
    		textArea.setWrapStyleWord(true);
    		
    		this.add(scrollPane, BorderLayout.CENTER);
		}
    	
    	public void addServerOutputText(String text)
    	{
    		textArea.append(text +"\n\n");
    		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    	}
    }
    
    
    // Deals with yahtzee bonuses (2 or more yahtzees) 
    // Could be dynamic. If all rolls happened to be yahztees, we could keep adding bonus slots.
    // I opted to just go with three bonuses since those are the rules I always played with.
    private class JahtzeeBonusPanel extends JPanel
    {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 8349965766435856541L;
		SelectableTextField firstBonus = null,
    			  			secondBonus = null,
    			  			thirdBonus = null;
    	
    	// Don't want to instantiate this constructor (debug code)
    	private JahtzeeBonusPanel() { throw new RuntimeException("MyUserInterface.JahtzeeBonusPanel: Don't instantiate via default constructor"); }
    	
    	public JahtzeeBonusPanel(SelectableTextField first, SelectableTextField second, SelectableTextField third)
		{
    		super();
    		firstBonus = first;
    		secondBonus = second;
    		thirdBonus = third;
    		
    		firstBonus.setHorizontalAlignment(SwingConstants.CENTER);
    		secondBonus.setHorizontalAlignment(SwingConstants.CENTER);
    		thirdBonus.setHorizontalAlignment(SwingConstants.CENTER);
    		
    		this.setLayout(new GridLayout(1, 0));
    		this.add(firstBonus);
    		this.add(secondBonus);
    		this.add(thirdBonus);
		}
    }
}
