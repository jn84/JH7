package my_networked_game;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import javax.lang.model.element.Element;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.omg.CORBA.PUBLIC_MEMBER;

import gameNet.GameNet_UserInterface;
import gameNet.GamePlayer;
import my_networked_game.Enums.MyGameInputType;
import my_networked_game.Enums.MyGameOutputType;
import my_networked_game.Enums.ScoreTypes;
import my_networked_game.HelperClasses.DiceObj;
import my_networked_game.HelperClasses.Player;
import my_networked_game.HelperClasses.SelectableTextField;
import my_networked_game.HelperClasses.SelectableTextFieldEvent;
import my_networked_game.HelperClasses.SelectableTextFieldGroup;
import my_networked_game.HelperClasses.SelectableTextFieldState;
import my_networked_game.Interfaces.SelectableTextFieldListener;

class MyUserInterface extends JFrame implements GameNet_UserInterface, SelectableTextFieldListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1558976730059939642L;

	private static final String lobbyPanelID = "LOBBY",
			mainGamePanelID = "MAIN_GAME";

	public static ImageIcon  dieDefault_1 = new ImageIcon(MyMain.class.getResource("/resources/One.png")),  
							 dieDefault_2 = new ImageIcon(MyMain.class.getResource("/resources/Two.png")),  
							 dieDefault_3 = new ImageIcon(MyMain.class.getResource("/resources/Three.png")),
							 dieDefault_4 = new ImageIcon(MyMain.class.getResource("/resources/Four.png")), 
							 dieDefault_5 = new ImageIcon(MyMain.class.getResource("/resources/Five.png")), 
							 dieDefault_6 = new ImageIcon(MyMain.class.getResource("/resources/Six.png")),
							 dieBlank 	  = new ImageIcon(MyMain.class.getResource("/resources/Blank.png"));

	private boolean isMyTurn = false,
					gameBegan = false;

	private CardLayout mainPanelLayout = new CardLayout();

	private JPanel mainPanel = new JPanel(mainPanelLayout);

	private DiceSet localDiceSet = new DiceSet();

	private GamePlayer myGamePlayer;

	private JTextField inputField = new JTextField();

	private SelectableTextFieldGroup selectableTextFields = new SelectableTextFieldGroup();

	private PlayerListPanel playerListPanel = new PlayerListPanel();

	private Player thisPlayer = null;

	private LobbyPanel lobbyPanel = new LobbyPanel();

	private DicePanel dicePanel = new DicePanel();

	private JButton submitButton = new JButton("Submit Score"),
			rollButton 	 = new JButton("Roll Dice"),
			skipButton   = new JButton("Skip Turn");

	private ButtonPanel buttonPanel = new ButtonPanel();

	private GameStatusUpdatePanel gameStatusUpdatePanel = new GameStatusUpdatePanel(inputField);

	// Should be last
	private MainGamePanel mainGamePanel = new MainGamePanel();

	public MyUserInterface()
	{
		super("Jahtzee!");

		this.addWindowListener(new WindowAdapter()		
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				exitProgram();
			}
		});

		this.inputField.addFocusListener(new FocusAdapter()
		{
			
			@Override
			public void focusLost(FocusEvent e)
			{
				inputField.grabFocus();
			}
		});
	
		inputField.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (inputField.getText().equals(""))
					return;
				myGamePlayer.sendMessage(new MyGameInput(thisPlayer.getName(), inputField.getText()));
				inputField.setText("");				
			}
		});
	}
	

	public void startUserInterface (GamePlayer player)
	{
		myGamePlayer = player;
		registerPlayer();
		myGamePlayer.sendMessage(new MyGameInput(MyGameInputType.UPDATE_PLAYERS));

		// Boring screen things 
		this.myLayout();
		//this.add(mainGamePanel, BorderLayout.CENTER);
		lobbyPanel.setName(lobbyPanelID);
		this.mainPanel.add(lobbyPanel, lobbyPanelID);
		mainPanel.setName(mainGamePanelID);
		this.mainPanel.add(mainGamePanel, mainGamePanelID);
		this.add(mainPanel);
		mainPanelLayout.show(mainPanel, lobbyPanelID);


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

		selectableTextFields.addSelectableTextFieldEventListener(this);

	}


	@Override
	public void handleSelectableTextFieldEvent(SelectableTextFieldEvent event)
	{
		if (event.getIsValidSelection())
			validScoringPlaySelected();
	}

	//
	//
	//		Does the selected state reset after a new turn?
	//
	//

	public void submitClick()
	{
		myGamePlayer.sendMessage(new MyGameInput(thisPlayer, localDiceSet, MyGameInputType.PLAYER_SUBMIT));
	}

	public void skipClick()
	{
		int confirmResult = JOptionPane.showConfirmDialog(
				this, 
				"You have chosen to skip your turn.\nIf you do this, you will have to cross out one of your score boxes.\nAre you sure you want to skip?", 
				"Confirm Skip Turn", 
				JOptionPane.YES_NO_OPTION);
		if (confirmResult != JOptionPane.YES_OPTION)
			return;
		else
			myGamePlayer.sendMessage(new MyGameInput(thisPlayer, localDiceSet, MyGameInputType.PLAYER_SKIP));
	}

	public void rollClick()
	{
		//gameStatusUpdatePanel.addServerOutputText("ROLL CLICK!");
		myGamePlayer.sendMessage(new MyGameInput(thisPlayer, localDiceSet, MyGameInputType.PLAYER_ROLL));
	}


	public void validScoringPlaySelected()
	{
		if (!isMyTurn)
			return;
		submitButton.setEnabled(true);
	}

	public void registerPlayer()
	{
		Random r = new Random();
		thisPlayer = new Player(myGamePlayer.getPlayerName(), Integer.toString(r.nextInt(100000000)), 0);
		myGamePlayer.sendMessage(new MyGameInput(thisPlayer, MyGameInputType.REGISTER_PLAYER));
	}

	public void receivedMessage(Object ob)
	{
		MyGameOutput myGameOutput = (MyGameOutput)ob;
		
		Runnable doGameBegin = new Runnable()
		{
			
			@Override
			public void run()
			{
				gameBegan = true;
				mainGamePanel.init();
				mainPanelLayout.show(mainPanel, mainGamePanelID);
				inputField.grabFocus();
				updateInterfaceState(myGameOutput);
			}
		};

		if (myGameOutput.getOutputType() != MyGameOutputType.MESSAGE && !myGameOutput.getMessage().equals(""))
			gameStatusUpdatePanel.addServerOutputText(myGameOutput.getMessage());

		switch (myGameOutput.getOutputType())
		{
		case PLAYER_REGISTERED:
			playerListPanel.addPlayer(myGameOutput.getActivePlayer());
			if (myGameOutput.getActivePlayer().getIsHost())
				lobbyPanel.btnStartGame.setEnabled(true);
			else
				lobbyPanel.btnStartGame.setEnabled(false);

			break;
		case PLAYER_UNREGISTERED:
			playerListPanel.removePlayer(myGameOutput.getActivePlayer());
			break;
		case GAME_BEGIN:

			// This portion of code is so that each client window will only process GAME_BEGIN once
			// Spectators will need their own special GAME_BEGIN object to move on from the lobby window
			if (gameBegan)
				break;
			
			// Will invoke only after all other UI elements have been updated
			// If we don't do this, sometimes, because of the timings, the game window will not switch from the lobby 
			SwingUtilities.invokeLater(doGameBegin);
			
		case MAIN_GAME:
			updateInterfaceState(myGameOutput);
			break;
		case GAME_OVER:
			updateInterfaceState(myGameOutput);
			break;
		case MESSAGE:
			gameStatusUpdatePanel.addServerOutputText(
					"<" + myGameOutput.getMessageSendingPlayer() + "> " + myGameOutput.getMessage());
			break;
		case UPDATE_PLAYERS:
			playerListPanel.updatePlayers(myGameOutput.getPlayersMap());
			break;
		case DICE_HOLD_CHANGED:
			dicePanel.updateDie(myGameOutput.getDieHoldIndex(), myGameOutput.getIsDieHeld());
		default:
			break;

		}
	}

	private void updateInterfaceState(MyGameOutput myGameOutputObj)
	{
		boolean isGameOver = (myGameOutputObj.getActivePlayer() == null);  
		
		// The original idea was that each player could only access their own scoresheet data
		// That is why I used HashMap
		// Once feature creep set in, I needed a way to show all player scores without a ton of code changes
		
		for (Map.Entry<String, Player> elem : myGameOutputObj.getPlayersMap().entrySet())
		{
			Player p = elem.getValue();
			playerListPanel.updatePlayer(p);
		}
		
		if (!isGameOver)
		{
			isMyTurn = myGameOutputObj.getActivePlayer().equals(thisPlayer);
			dicePanel.resetDicePanels();

			// It's my turn
			if (isMyTurn)
			{
				thisPlayer = myGameOutputObj.getActivePlayer();
				updateSelectableTextFields(thisPlayer.getScoreData());
				dicePanel.updateDice(myGameOutputObj.getDice());
				dicePanel.setEnabled(true);

				// Maybe we want to make this disabled is the player is currently skipping?
				skipButton.setEnabled(true);
				rollButton.setEnabled(myGameOutputObj.canPlayerRollDice());
				// Submit button should only be enabled once we get the signal from the
				// scoresheet that a valid selection has been made.
				// see handleSelectableTextFieldEvent
				submitButton.setEnabled(false);

			}

			// It's not my turn
			else
			{
				thisPlayer = myGameOutputObj.getMyPlayer(thisPlayer.getID());
				updateSelectableTextFields(thisPlayer.getScoreData());
				dicePanel.updateDice(myGameOutputObj.getDice());


				// Disable Dice Panel
				dicePanel.setEnabled(false);
				buttonPanel.setEnabled(false);

			}
		}
		// Game is over
		else
		{
			thisPlayer = myGameOutputObj.getMyPlayer(thisPlayer.getID());
			updateSelectableTextFields(thisPlayer.getScoreData());
			dicePanel.setEnabled(false);
			buttonPanel.setEnabled(false);
		}
	}

	// Nice to let people know you are leaving
	private void exitProgram()
	{
		myGamePlayer.sendMessage(new MyGameInput(thisPlayer, MyGameInputType.UNREGISTER_PLAYER));
		myGamePlayer.doneWithGame();
		System.exit(0);
	}

	private void myLayout()
	{
		this.setLayout(mainPanelLayout);
		this.setSize(800, 600);
		this.setVisible(true);
	}

	private void updateSelectableTextFields(ArrayList<SelectableTextFieldState> states)
	{
		selectableTextFields.setStates(states);
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
		/**
		 * 
		 */
		private static final long serialVersionUID = 274775416214681073L;
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
					myGamePlayer.sendMessage(new MyGameInput());
				}
			});
		}
		
		/**
		 * Set if the player can use the start game button
		 * @param value
		 * Value to use.
		 * true - player can start the game from the lobby
		 * false - player cannot start the game from the lobby
		 */
		public void setStartEnabled(boolean value)
		{
			btnStartGame.setEnabled(value);
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

		/**
		 * Update the given player's data in the field
		 * @param p
		 * The player object to update
		 */
		public void updatePlayer(Player p)
		{
			if (!modelPlayers.contains(p))
				return;

			// We can use indexOf since equals is defined in the Player class
			modelPlayers.set(modelPlayers.indexOf(p), p);
		}
		
		public void updatePlayers(HashMap<String, Player> map)
		{
			for (Map.Entry<String, Player> elem : map.entrySet())
			{
				this.addPlayer(elem.getValue());
				this.updatePlayer(elem.getValue());
			}
		}
		

		class PlayerListRenderer extends DefaultListCellRenderer
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = -5399505231573174050L;

			public Component getListCellRendererComponent(JList<?> list, Object playerObj, int index, boolean isSelected, boolean cellHasFocus)
			{
				super.getListCellRendererComponent(list, playerObj, index, isSelected, cellHasFocus);

				Player player = (Player)playerObj;
				if (player.isSpectator())
					setText(player.getName() + " (spectator)");
				else
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

		/**
		 * For initializing this panel
		 * Initialize this panel before showing it
		 */
		public void init()
		{
			gamePlayPanel.init();
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
			
			// Don't bother trying to reorganize the cells.
			// playerListPanel needs to be the last one outside of a bunch of code changes
			this.add(gameStatusUpdatePanel);
		}

		/**
		 * Needs to be called before switching from the lobby to the main game panel.
		 */
		public void init()
		{
			this.add(playerListPanel);
		}
	}

	//
	//	Dice Panel classes
	//
	private class DicePanel extends JPanel
	{
		private static final long serialVersionUID = -1422825607652380712L;

		ArrayList<DiePanel> diePanels = new ArrayList<DiePanel>(5);

		public DicePanel()
		{
			this.setLayout(new GridLayout(1, 5));
			
			for (int i = 0; i < 5; i++)
			{
				diePanels.add(new DiePanel());
				this.add(diePanels.get(i));
				diePanels.get(i).updateIndex(i);
			}
		}

		public void updateDice(DiceSet ds)
		{
			localDiceSet = ds;
			
			for (int i = 0; i < diePanels.size(); i++)
			{	
				diePanels.get(i).setState(false);
				diePanels.get(i).updateDie(localDiceSet.getDice().get(i));
			}
		}
		
		public void updateDie(int index, boolean value)
		{
			diePanels.get(index).setSelected(value);
		}

		public void resetDicePanels()
		{
			for  (DiePanel elem : diePanels)
				elem.setSelected(false);
		}
		
		
		/**
		 * Set the control to a non-interactive state
		 * true - If it is this player's turn
		 * false - If it is NOT this player's turn
		 */
		public void setEnabled(boolean value)
		{
			for (DiePanel elem : diePanels)
				elem.setEnabled(value);
		}

	}
	
	private class DiePanel extends JPanel
	{
		private static final long serialVersionUID = 5883525763724073240L;

		boolean isEnabled = true;
		int myIndex = 0;
		DiceObj localDiceObj = new DiceObj();
		
		JLabel lblHeld = new JLabel("Held");
		DiceObjPanel diceObj = new DiceObjPanel(localDiceObj.getValue());
		JCheckBox chkHold = new JCheckBox("Hold");
		
		public DiePanel()
		{
			this.setLayout(new BorderLayout());
			this.add(lblHeld, BorderLayout.NORTH);
			this.add(diceObj, BorderLayout.CENTER);
			this.add(chkHold, BorderLayout.SOUTH);
			
			lblHeld.setVisible(false);
			
			chkHold.addActionListener(new ActionListener()
			{
				
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if (isEnabled)
					{
						diceObj.setSelected(chkHold.isSelected());
						lblHeld.setVisible(chkHold.isSelected());
						localDiceSet.setHeld(myIndex, chkHold.isSelected());
						myGamePlayer.sendMessage(new MyGameInput(myIndex, chkHold.isSelected()));
					}
				}
			});
			
			for (Component elem : diceObj.getComponents())
			{
				elem.addMouseListener(new MouseAdapter()
				{
					 public void mousePressed(MouseEvent e)
					 {
						 chkHold.doClick();
					 }
				});
			}
		}
		
		public void updateDie(DiceObj die)
		{
			localDiceObj = die;
			this.diceObj.updateImage(localDiceObj.getValue());
		}
		
		public void updateIndex(int index)
		{
			myIndex = index;
		}
		
		public void setEnabled(boolean value)
		{
			chkHold.setEnabled(value);
		}
		
		public void setState(boolean value)
		{
			lblHeld.setVisible(value);
			chkHold.setSelected(value);
			diceObj.setSelected(value);
		}
		
		public void setSelected(boolean value)
		{
			this.setState(value);
		}
	}

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
					"Skip your turn");

			this.add(rollButton);
			this.add(submitButton);
			this.add(skipButton);
		}

		public void setEnabled(boolean value)
		{
			rollButton.setEnabled(value);
			submitButton.setEnabled(value);
			skipButton.setEnabled(value);
		}
	}

	private class GameStatusUpdatePanel extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -6187609582103465670L;
		JTextArea textArea = new JTextArea();
		JTextField inputField = null;
		JScrollPane scrollPane = new JScrollPane(
				textArea, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		public GameStatusUpdatePanel(JTextField input)
		{
			this.inputField = input;
			
			this.setLayout(new BorderLayout());

			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);

			this.add(scrollPane, BorderLayout.CENTER);
			this.add(inputField, BorderLayout.SOUTH);
		}

		public void addServerOutputText(String text)
		{
			StringTokenizer tokenizer = new StringTokenizer(text, "\n");
			while (tokenizer.hasMoreTokens())
				textArea.append(tokenizer.nextToken() + "\n");
			
			Runnable runnable = new Runnable()
			{
				
				@Override
				public void run()
				{
					scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
				}
			};
			SwingUtilities.invokeLater(runnable);
		}
	}


	// Deals with Jahtzee bonuses (2 or more Jahtzees) 
	private class JahtzeeBonusPanel extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8349965766435856541L;
		SelectableTextField firstBonus = null,
				secondBonus = null,
				thirdBonus = null;

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
