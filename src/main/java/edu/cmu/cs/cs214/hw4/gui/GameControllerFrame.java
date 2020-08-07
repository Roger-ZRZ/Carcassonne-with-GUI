package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.Carcassonne;
import edu.cmu.cs.cs214.hw4.core.Edge;
import edu.cmu.cs.cs214.hw4.core.GameChangeListener;
import edu.cmu.cs.cs214.hw4.core.Player;
import edu.cmu.cs.cs214.hw4.core.Position;
import edu.cmu.cs.cs214.hw4.core.TileType;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The GUI component of the Game Controller GUI;
 * The game controller is responsible for name inputs, tile rotation, meeple placement,
 * and displaying information;
 * A game object is instantiated in this module, and the board GUI is also instantiated in
 * this module;
 */
public class GameControllerFrame extends JFrame implements GameChangeListener {

	//var
	private static final String TITLE = "Game Controller";
	private static final int[] FRAME_POS = new int[]{100, 100};//w,h
	private static final int[] FRAME_SIZE = new int[]{425, 650};//w,h
	private static final Color[] PLAYER_COLORS = new Color[]{Color.RED, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.BLACK};
	private static final int MAX_PARTICIPANT_COUNT = 5;
	private static final int MIN_PARTICIPANT_COUNT = 2;


	//Operating Comp
	private Carcassonne game;
	private final ImageParser imageParser;

	//Graphic Comp
	private JFrame gameVisuals;
	private TurnInfoPanel turnInfoPanel;//panel for tile dealt picture
	private RotationControlPanel rotationControlPanel;//rotation control
	private MeepleControlPanel meepleControlPanel;//rotation control
	private ScorePanel scorePanel;
	private NameEntrancePanel nameEntrancePanel;
	private JLabel winnerLabel;

	//Data Comp
	private Map<Position,TileDisplayInfo> tileDisplayMap;//store everything
	private List<String> playerNames;
	private List<JLabel> playerDisplayList;
	private String currentPlayerName;
	private int currentTurn;
	private List<Edge> currentRotationOptions;
	private int nextRotationOptionIndex;

	/**
	 * Construct a game controller window class that included a game instance and a board window;
	 * The name input panel will pop up automatically;
	 * The game will not start until the player enters the correct amount of names;
	 * The board window will pop up automatically when the game starts;
	 * @throws IOException file for tlie parsing cannot be found, check resource folder;
	 */
	public GameControllerFrame() throws IOException {
		//graphic init
		super(TITLE);
		this.setBounds(FRAME_POS[0], FRAME_POS[1], FRAME_SIZE[0], FRAME_SIZE[1]);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null); //custom layout

		//data init
		imageParser = new ImageParser();
		tileDisplayMap = new HashMap<>();
		currentRotationOptions = new ArrayList<>();
		playerNames = new ArrayList<>();
		playerDisplayList = new ArrayList<>();


		//comp init
		nameEntrancePanel = new NameEntrancePanel();
		this.add(nameEntrancePanel);
		nameEntrancePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		nameEntrancePanel.setBounds(0, 0, 400, 300);
		nameEntrancePanel.setVisible(true);

		turnInfoPanel = new TurnInfoPanel(imageParser.getTileImage(TileType.TILE_U,Edge.NORTH), "");
		this.add(turnInfoPanel);
		turnInfoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		turnInfoPanel.setBounds(0, 0, 400, 600);
		turnInfoPanel.setVisible(false);

		rotationControlPanel = new RotationControlPanel();
		this.add(rotationControlPanel);
		rotationControlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		rotationControlPanel.setBounds(0, 100, 200, 300);
		rotationControlPanel.setVisible(false);

		meepleControlPanel = new MeepleControlPanel();
		this.add(meepleControlPanel);
		meepleControlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		meepleControlPanel.setBounds(200, 100, 200, 300);
		meepleControlPanel.setVisible(false);

		scorePanel = new ScorePanel();
		this.add(scorePanel);
		scorePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		scorePanel.setBounds(0, 400, 400, 200);
		scorePanel.setVisible(false);

		winnerLabel = new JLabel();
		this.add(winnerLabel);
		winnerLabel.setBounds(0,0,400,500);
		winnerLabel.setBorder(BorderFactory.createTitledBorder("WINNERS"));
		winnerLabel.setVisible(false);
	}

	/**
	 * Start the game from the GUI;
	 * the game will be started when the names are filled in correctly;
	 * this method is private and should not be called by any external classes;
	 * Game play components will be displayed from this class;
	 * @throws FileNotFoundException Resources for the Carcassonne Core framework is
	 * not found, check the resources folder;
	 */
	private void guiGameStart() throws FileNotFoundException {

		game = new Carcassonne(playerNames);
		game.addGameChangeListener(this);
		gameVisuals = new GameVisualizerFrame(tileDisplayMap, game, imageParser,PLAYER_COLORS);
		gameVisuals.setVisible(true);
		turnInfoPanel.setVisible(true);
		rotationControlPanel.setVisible(true);
		meepleControlPanel.setVisible(true);
		scorePanel.setVisible(true);
		game.gameStart();

	}

	/**
	 * A private embedded class for entering player names;
	 * Note that this module is a reference from the starter code provided in
	 * recitation 8 the chat subscriber module;
	 */
	private class NameEntrancePanel extends JPanel {

		/**
		 * Construct a instance of the name entrance panel;
		 */
		NameEntrancePanel() {
			// Create the components to add to the panel.
			JLabel participantLabel = new JLabel("Name: ");

			// Must be final to be accessible to the anonymous class.
			final JTextField participantText = new JTextField(20);

			JButton participantButton = new JButton("Add participant");
			JPanel participantPanel = new JPanel();
			participantPanel.setLayout(new BorderLayout());
			participantPanel.add(participantLabel, BorderLayout.WEST);
			participantPanel.add(participantText, BorderLayout.CENTER);
			participantPanel.add(participantButton, BorderLayout.EAST);

			JPanel participantDisplayPanel = new JPanel();
			participantDisplayPanel.setLayout(new BoxLayout(participantDisplayPanel,BoxLayout.PAGE_AXIS));
			for(int i=0;i<MAX_PARTICIPANT_COUNT;i++){
				JLabel personLabel = new JLabel("_");
				personLabel.setHorizontalAlignment(JLabel.CENTER);
				personLabel.setFont(new Font("TimesRoman", Font.PLAIN, 18));
				personLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				playerDisplayList.add(personLabel);
				participantDisplayPanel.add(personLabel);
			}

			//button to start game session
			JButton startGameButton = new JButton("INSUFFICIENT PLAYER COUNT");
			startGameButton.addActionListener(e -> {
				// Starts a new chat when the createButton is clicked.
				if (playerNames.size()>=MIN_PARTICIPANT_COUNT) {
					try {
						startGameSession();
					} catch (FileNotFoundException ex) {
						ex.printStackTrace();
					}
				}
			});


			// Defines an action listener to handle the addition of new participants
			ActionListener newParticipantListener = e -> {
				String name = participantText.getText().trim();
				if (!name.isEmpty() && !playerNames.contains(name) && playerNames.size()<MAX_PARTICIPANT_COUNT) {
					playerNames.add(name);
					int unfilledIndex = playerNames.size();
					if (unfilledIndex <= MAX_PARTICIPANT_COUNT) {
						playerDisplayList.get(unfilledIndex - 1).setText(name);
					}
				}
				//change button color if max player count is reached
				if(playerNames.size()<MIN_PARTICIPANT_COUNT){
					startGameButton.setText("INSUFFICIENT PLAYER COUNT");
				}else if(playerNames.size()==MAX_PARTICIPANT_COUNT){
					startGameButton.setBackground(Color.green);
					startGameButton.setText("<html>Max Player Count Reached<br/>START GAME HERE!</html>");
				}else{
					startGameButton.setText("START GAME");
				}



				participantText.setText("");
				participantText.requestFocus();
				//add person to the participantDisplayPanel

			};

			// notify the action listener when participant Button is pressed
			participantButton.addActionListener(newParticipantListener);

			// notify the action listener when "Enter" key is hit
			participantText.addActionListener(newParticipantListener);



			// Adds the components we've created to the panel (and to the window).
			setLayout(new BorderLayout());
			add(participantPanel, BorderLayout.NORTH);
			add(startGameButton, BorderLayout.CENTER);
			add(participantDisplayPanel, BorderLayout.SOUTH);
			setVisible(true);
		}

		/**
		 * Calls a gui game start;
		 * this method should be called from the start game button and the name entrance
		 * panel will become hidden;
		 * @throws FileNotFoundException Resources for the Carcassonne Core framework is
		 * 	 * not found, check the resources folder;
		 */
		private void startGameSession() throws FileNotFoundException {
			// Creates a new window for each participant.
			this.setVisible(false);
			guiGameStart();
		}
	}


	/**
	 * a private embedded class for showing turn information;
	 * shows image of the current dealt tile and the turn info;
	 */
	private class TurnInfoPanel extends JPanel{

		private JLabel tileImageLabel;
		private JLabel turnInfoLabel;
		private String txtInfo;

		/**
		 * Construct a turn info class that shows a image and some text
		 * @param initialBfImg image
		 * @param turnInfoText text
		 */
		TurnInfoPanel(BufferedImage initialBfImg, String turnInfoText){
			super();
			this.setLayout(null);
			tileImageLabel = new JLabel(new ImageIcon(initialBfImg));
			turnInfoLabel = new JLabel(turnInfoText, JLabel.LEFT);
			turnInfoLabel.setBorder(BorderFactory.createLineBorder(Color.gray));
			this.add(tileImageLabel);
			tileImageLabel.setBounds(5,5,90,90);
			this.add(turnInfoLabel);
			turnInfoLabel.setBounds(100, 5,295,90);
			txtInfo = "";
		}

		/**
		 * set text for the turn in html syntax;
		 * @param turnInfoText string of the turn info text, in html style
		 */
		void setText(String turnInfoText){
			txtInfo = turnInfoText;
			turnInfoLabel.setText(turnInfoText);
			turnInfoLabel.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(PLAYER_COLORS[game.getCurrentPlayerIndex()]),"Turn Info"));
		}

		/**
		 * add text for the turn; this does not need to be in html style;
		 * do not add text to empty turn infos;
		 * @param addedText added text;
		 */
		void addText(String addedText){
			if(txtInfo.lastIndexOf("</html>")==-1){
				return;
			}
			txtInfo = txtInfo.substring(0,txtInfo.lastIndexOf("</html>"));
			txtInfo += "<br/>" + addedText + "</html>";
			turnInfoLabel.setText(txtInfo);
		}

		/**
		 * set image
		 * @param bfi bufferimage
		 */
		void setImage(BufferedImage bfi){
			tileImageLabel.setIcon(new ImageIcon(bfi));
		}
	}

	/**
	 * panel that holds buttons responsible for rotation control
	 */
	private class RotationControlPanel extends JPanel{

		private JButton rotateButton;
		private JButton confirmButton;

		/**
		 * constructor for the rotation control panel;
		 */
		RotationControlPanel(){
			super();
			this.setLayout(null);

			rotateButton = new JButton("Rotate");//v1...rotate!
			rotateButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					game.playerChangeRotation(currentRotationOptions.get(nextRotationOptionIndex));
				}
			});
			this.add(rotateButton);
			rotateButton.setBounds(5, 5, 190, 140);
			rotateButton.setVisible(false);

			confirmButton = new JButton("Confirm");//v1...confirm!
			confirmButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					game.playerConfirmRotation();
				}
			});
			this.add(confirmButton);
			confirmButton.setBounds(5, 155, 190, 140);
			confirmButton.setVisible(false);
		}

		/**
		 * show buttons
		 */
		void showButton(){
			rotateButton.setVisible(true);
			confirmButton.setVisible(true);
		}

		/**
		 * hide buttons
		 */
		void hideButton(){
			rotateButton.setVisible(false);
			confirmButton.setVisible(false);
		}

	}

	/**
	 * panel that holds meeple control buttons
	 */
	private class MeepleControlPanel extends JPanel{

		private JButton meepleNorthButton;
		private JButton meepleEastButton;
		private JButton meepleSouthButton;
		private JButton meepleWestButton;
		private JButton meepleCenterButton;
		private JButton meepleSkipButton;
		private List<JButton> meepleControlButtons;

		/**
		 * construct a panel that holds meeple control buttons;
		 */
		MeepleControlPanel(){
			super();
			this.setLayout(null);


			meepleNorthButton = new JButton("North");
			meepleNorthButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					game.playerPlaceMeeple(Edge.NORTH);
				}
			});
			this.add(meepleNorthButton);
			meepleNorthButton.setBounds(5, 5, 190, 40);
			meepleNorthButton.setVisible(false);


			meepleEastButton = new JButton("East");
			meepleEastButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					game.playerPlaceMeeple(Edge.EAST);
				}
			});
			this.add(meepleEastButton);
			meepleEastButton.setBounds(5, 55, 190, 40);
			meepleEastButton.setVisible(false);

			meepleSouthButton = new JButton("South");
			meepleSouthButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					game.playerPlaceMeeple(Edge.SOUTH);
				}
			});
			this.add(meepleSouthButton);
			meepleSouthButton.setBounds(5, 105, 190, 40);
			meepleSouthButton.setVisible(false);

			meepleWestButton = new JButton("West");
			meepleWestButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					game.playerPlaceMeeple(Edge.WEST);
				}
			});
			this.add(meepleWestButton);
			meepleWestButton.setBounds(5, 155, 190, 40);
			meepleWestButton.setVisible(false);

			meepleCenterButton = new JButton("Center");
			meepleCenterButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					game.playerPlaceMeeple(Edge.CENTER);
				}
			});
			this.add(meepleCenterButton);
			meepleCenterButton.setBounds(5, 205, 190, 40);
			meepleCenterButton.setVisible(false);

			meepleSkipButton = new JButton("Skip");
			meepleSkipButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					game.playerSkipMeeple();
				}
			});
			this.add(meepleSkipButton);
			meepleSkipButton.setBounds(5, 255, 190, 40);
			meepleSkipButton.setVisible(false);

			meepleControlButtons = new ArrayList<>();
			meepleControlButtons.add(meepleNorthButton);
			meepleControlButtons.add(meepleEastButton);
			meepleControlButtons.add(meepleSouthButton);
			meepleControlButtons.add(meepleWestButton);
			meepleControlButtons.add(meepleCenterButton);
			meepleControlButtons.add(meepleSkipButton);
		}

		/**
		 * show the buttons that are necessary;
		 * @param edgeList buttons of the edges that will be shown
		 */
		void showButton(List<Edge> edgeList){
			meepleControlButtons.get(Edge.values().length).setVisible(true);
			for(Edge edge:edgeList){
				meepleControlButtons.get(edge.ordinal()).setVisible(true);
			}
		}

		/**
		 * hide all buttons;
		 */
		void hideButton(){
			for(JButton button:meepleControlButtons){
				button.setVisible(false);
			}
		}
	}

	/**
	 * panel that shows the score of the players
	 */
	private class ScorePanel extends JPanel{
		private JLabel scoreInfoLabel;

		/**
		 * construct a score panel at the start of the turn;
		 */
		ScorePanel(){
			super();
			this.setLayout(null);
			scoreInfoLabel = new JLabel("All Players Have Score 0; ", JLabel.LEFT);
			scoreInfoLabel.setBorder(BorderFactory.createTitledBorder("Score Board"));
			this.add(scoreInfoLabel);
			scoreInfoLabel.setBounds(5, 5,390,190);
		}

		/**
		 * set the score information with integers
		 * @param scoreList list of scores of the players;
		 */
		void setText(List<Integer> scoreList){
			String scoreInfo = "<html>";
			for(int i = 0;i<scoreList.size();i++){
				scoreInfo += playerNames.get(i)+": ";
				scoreInfo += scoreList.get(i);
				scoreInfo += ";  <br/>";
			}
			scoreInfo += " </html>";
			scoreInfoLabel.setText(scoreInfo);
		}
	}

	/**
	 * not used;
	 * @param tileType first tile that was placed on the board
	 * @param playerNames names of the players
	 */
	@Override
	public void gameStartEvent(TileType tileType, List<String> playerNames) {

	}

	/**
	 * 	change player, change stored information;
	 * 	redraw turnInfoPanel text part
	 * @param player the player of the new turn;
	 * @param turn the number of the current turn, starting from 1;
	 */
	@Override
	public void currentPlayerChangeEvent(Player player, int turn) {
		currentPlayerName = player.getName();
		currentTurn = turn;
		turnInfoPanel.setText("<html>"+"Turn: "+turn+"<br/>Player: "+currentPlayerName+"</html>");
		game.requestTileFromDeck();
	}

	/**
	 * redraw turninfopanel graphic image part
	 * @param player the player who is playing at this new turn;
	 * @param tileType the type of the tile that was being dealt to this player;
	 */
	@Override
	public void tileDealtEvent(Player player, TileType tileType) {
		turnInfoPanel.setImage(imageParser.getTileImage(tileType,Edge.NORTH));
	}

	/**
	 * not used
	 * @param player the current player;
	 * @param positionsAllowed the positions that are allowed for placement;
	 */
	@Override
	public void tilePlacementOptionsEvent(Player player, List<Position> positionsAllowed) {

	}

	/**
	 * change the tile shown in the turn info panel to blank;
	 * add information about placement to the turn info panel;
	 * @param player the current player;
	 * @param tileType the current placed tile type;
	 * @param position the position where the current tile is placed;
	 */
	@Override
	public void tilePlacementEvent(Player player, TileType tileType, Position position) {
		turnInfoPanel.setImage(imageParser.makeColoredEmptySquare(Color.white));
		turnInfoPanel.addText("Position: "+position.toString());
	}

	/**
	 * show button to allow rotations;
	 * @param player the current player;
	 * @param tileType the current tile type;
	 * @param position the position where this tile is already placed;
	 * @param edgesAllowed the edges where the tile is allowed to rotate; note that the edges shows
	 *                     the possible rotations of the north face, for instance, a tile that is rotated
	 *                     once clockwise is considered facing east as the north side of the tile is now
	 */
	@Override
	public void tileRotationOptionsEvent(Player player, TileType tileType, Position position, List<Edge> edgesAllowed) {
		currentRotationOptions.clear();
		for(Edge edge:edgesAllowed){
			currentRotationOptions.add(edge);
		}
		nextRotationOptionIndex = (currentRotationOptions.size()==1)?0:1;//next rotation after change
		rotationControlPanel.showButton();
	}

	/**
	 * not used, rotation information stored;
	 * @param player the current player;
	 * @param tileType the current tile type;
	 * @param position the position of the tile that was being rotated;
	 * @param edge the edge that the tile is After the rotation;
	 */
	@Override
	public void tileRotationChangedEvent(Player player, TileType tileType, Position position, Edge edge) {
		nextRotationOptionIndex = (nextRotationOptionIndex ==currentRotationOptions.size()-1)?0: nextRotationOptionIndex +1;
	}

	/**
	 * rotation done and hide the rotation buttons;
	 * @param player the current player;
	 * @param tileType the type of the tile that was being finalized;
	 * @param position the positon of the tile that was being finalized;
	 * @param edge the rotation of the tile at position at the time of finalization;
	 */
	@Override
	public void tileFinalizeConfirmationEvent(Player player, TileType tileType, Position position, Edge edge) {
		rotationControlPanel.hideButton();
	}

	/**
	 * show meeple placement buttons according to allowed placements;
	 * @param player the current player;
	 * @param tileType the current tile type;
	 * @param position the position of the current working tile;
	 * @param edges the edges where the meeple can be placed;
	 */
	@Override
	public void meepleOptionsEvent(Player player, TileType tileType, Position position, List<Edge> edges) {
		meepleControlPanel.showButton(edges);
	}

	/**
	 * hide meeple buttons after meeple placement;
	 * @param player the current player;
	 * @param tileType the current tile type;
	 * @param position the position of the current working tile;
	 * @param edge the edge of the meeple placement;
	 */
	@Override
	public void meeplePlacementEvent(Player player, TileType tileType, Position position, Edge edge) {
		meepleControlPanel.hideButton();

	}

	/**
	 * hide meeple buttons after meeple placement skipped;
	 * @param player the current player;
	 */
	@Override
	public void meepleSkippedEvent(Player player) {
		meepleControlPanel.hideButton();

	}

	/**
	 * not used;
	 * @param tileType the type of the tile where the meeple is removed from;
	 * @param position the position of the tile where the meeple is removed from;
	 * @param edge the edge where the meeple originally was;
	 */
	@Override
	public void meepleRemovalEvent(TileType tileType, Position position, Edge edge) {
	}

	/**
	 * show updates in the score panel
	 * @param scoreList score list
	 */
	@Override
	public void scoreUpdateEvent(List<Integer> scoreList) {
		scorePanel.setText(scoreList);
	}

	/**
	 * set winnerInfo panel to display eng game info;
	 * set all other graphics component to not display;
	 * @param winners the list of winning player;
	 * @param winningScore the winning score;
	 */
	@Override
	public void gameEndedEvent(List<Player> winners, int winningScore) {
		turnInfoPanel.setVisible(false);
		rotationControlPanel.setVisible(false);
		meepleControlPanel.setVisible(false);
		scorePanel.setVisible(false);
		gameVisuals.setVisible(false);
		String winnerInfo = "<html>";//=====Winners=====<br/>";
		for(int i = 0;i<winners.size();i++){
			winnerInfo += winners.get(i).getName()+": ";
			winnerInfo += winningScore;
			winnerInfo += ";  <br/>";
		}
		winnerInfo += " </html>";
		winnerLabel.setText(winnerInfo);
		winnerLabel.setVisible(true);
		System.out.println("Thank you. ");
	}
}
