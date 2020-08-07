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
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Game board window that provides a view of the board;
 * This class is not supposed to be initiated without a game controller panel or
 * outside of a game controller panel;
 * This class is package private;
 */
class GameVisualizerFrame extends JFrame implements GameChangeListener {

	//var
	private static final String TITLE = "Game Board";
	private static final int[] FRAME_POS = new int[]{700, 0};//w,h
//	private static final int[] FRAME_SIZE = new int[]{1400, 1400};//w,h
	private static final int[] FRAME_SIZE = new int[]{(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.95),
		(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.95)};//w,h
	private static final int MAX_TILES_SIDE = 25; //can be increased to bigger values, however a bigger screen is recommended;

	//Operating Comp
	private final Carcassonne gameREF;//REFERENCED
	private final ImageParser imageParserREF;//REFERENCED

	//Graphic Comp
	private final TileLabel[][] tileLabels;
	private JScrollPane boardPanel;
	private JPanel boardPanelInner;

	//Data Comp
	private Map<Position,TileDisplayInfo> tileDisplayMapREF;//REFERENCED
	private Color[] playerColorsREF;
	private List<Position> posListMem;
	private int currentPlayerIndex;
	private List<String> playerNames;

	/**
	 * create a windows of the board;
	 * all variables should be passed by reference;
	 * @param tileDisplayMap reference passed display information map
	 * @param game reference passed game
	 * @param imageParser reference passed game image;
	 * @param playerColors reference passed player colors;
	 */
	GameVisualizerFrame(Map<Position,TileDisplayInfo> tileDisplayMap, Carcassonne game, ImageParser imageParser, Color []playerColors){
		//graphic init
		super(TITLE);
		this.setBounds(FRAME_POS[0], FRAME_POS[1], FRAME_SIZE[0], FRAME_SIZE[1]);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

		//data init
		tileDisplayMapREF = tileDisplayMap;
		gameREF = game;
		imageParserREF = imageParser;
		playerColorsREF = playerColors;
		game.addGameChangeListener(this);
		posListMem = new ArrayList<>();

		//graphics init
		boardPanelInner = new JPanel();
		boardPanel = new JScrollPane(boardPanelInner);
		this.add(boardPanel);
		boardPanel.setPreferredSize(new Dimension(MAX_TILES_SIDE*90, MAX_TILES_SIDE*90));
		boardPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		boardPanel.getVerticalScrollBar().setUnitIncrement(16);
		boardPanel.getHorizontalScrollBar().setUnitIncrement(16);
		boardPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		boardPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		boardPanelInner.setLayout(new GridLayout(MAX_TILES_SIDE,MAX_TILES_SIDE,0,0));
		boardPanelInner.setBounds(0,0,MAX_TILES_SIDE*90,MAX_TILES_SIDE*90);
		tileLabels = new TileLabel[MAX_TILES_SIDE][MAX_TILES_SIDE];
		for(int j=0;j<MAX_TILES_SIDE;j++){
			for(int i=0;i<MAX_TILES_SIDE;i++){
				tileLabels[i][j] = new TileLabel(i,j);
				boardPanelInner.add(tileLabels[i][j]);
			}
		}
		//do not pack this module, will cause the scrollpane to resize to preferred size;
	}

	/**
	 * Labels that contains graphic component necessary for each tile display block;
	 */
	private class TileLabel extends JLabel {
		private BufferedImage bufferedImage;
		private JButton choosePosButton;
		private int posX;
		private int posY;

		/**
		 * Construct a tile display block intended to be positioned at (x,y);
		 * @param x the x index of the tile position; can be neg;
		 * @param y the y index of the tile position; can be pos;
		 */
		TileLabel(int x, int y){//x and y are index (start with 0)
			super();
			this.setLayout(null);
			posX = x - (int)(MAX_TILES_SIDE/2);
			posY = - y + (int)(MAX_TILES_SIDE/2);
			bufferedImage = null;
			choosePosButton = new JButton("Place");
			choosePosButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameREF.playerPlaceTileOnBoard(new Position(posX,posY));
				}
			});
			this.add(choosePosButton);
			choosePosButton.setBounds(0, 0, 90, 90);
			choosePosButton.setVisible(false);
		}

		/**
		 * set image of the tile
		 * @param bfi bufferimage
		 */
		void setTileImage(BufferedImage bfi){
			bufferedImage = bfi;
			this.setIcon(new ImageIcon(bfi));
		}

		/**
		 * show the choosing button for tile choosing
		 */
		void showButton(){
			choosePosButton.setVisible(true);
		}

		/**
		 * hide the choosing button for tile choosing
		 */
		void hideButton(){
			choosePosButton.setVisible(false);
		}
	}

	/**
	 * converts the tile position to label rotation;
	 * @param position position of the tile in 2D coordination with standard directions
	 * @return 2 element array of the array position based information in the label 2D array;
	 */
	private int[] pos2Lab(Position position){
		return new int[]{position.getX()+MAX_TILES_SIDE/2, -position.getY()+MAX_TILES_SIDE/2};
	}

	/**
	 * place the starter tile on the board;
	 * @param tileType first tile that was placed on the board
	 * @param playerNames names of the players
	 */
	@Override
	public void gameStartEvent(TileType tileType, List<String> playerNames) {
		tileLabels[pos2Lab(new Position(0, 0))[0]][pos2Lab(new Position(0, 0))[1]].
				setTileImage(imageParserREF.getTileImage(tileType, Edge.NORTH));
		this.playerNames = playerNames;
		currentPlayerIndex = playerNames.size()-1;
	}

	/**
	 * record player change info;
	 * @param player the player of the new turn;
	 * @param turn the number of the current turn, starting from 1;
	 */
	@Override
	public void currentPlayerChangeEvent(Player player, int turn) {
		currentPlayerIndex = (currentPlayerIndex+1)%playerNames.size();
	}

	/**
	 * not used;
	 * @param player the player who is playing at this new turn;
	 * @param tileType the type of the tile that was being dealt to this player;
	 */
	@Override
	public void tileDealtEvent(Player player, TileType tileType) {

	}

	/**
	 * render the tile placement options as buttons on the board
	 * @param player the current player;
	 * @param positionsAllowed the positions that are allowed for placement;
	 */
	@Override
	public void tilePlacementOptionsEvent(Player player, List<Position> positionsAllowed) {
		posListMem.clear();
		for(Position pos:positionsAllowed){
			posListMem.add(pos);
			if(pos2Lab(pos)[0]<tileLabels.length && pos2Lab(pos)[1]<tileLabels[0].length &&
				pos2Lab(pos)[0]>=0 && pos2Lab(pos)[1]>=0){
				tileLabels[pos2Lab(pos)[0]][pos2Lab(pos)[1]].showButton();
			}
		}
	}

	/**
	 * render that tile that was placed to with the tile placed at its first possible rotation;
	 * hide the placement option buttons;
	 * @param player the current player;
	 * @param tileType the current placed tile type;
	 * @param position the position where the current tile is placed;
	 */
	@Override
	public void tilePlacementEvent(Player player, TileType tileType, Position position) {
		for(Position pos:posListMem){
			if(pos2Lab(pos)[0]<tileLabels.length && pos2Lab(pos)[1]<tileLabels[0].length &&
				pos2Lab(pos)[0]>=0 && pos2Lab(pos)[1]>=0){
				tileLabels[pos2Lab(pos)[0]][pos2Lab(pos)[1]].hideButton();
			}
		}
		tileLabels[pos2Lab(position)[0]][pos2Lab(position)[1]].setTileImage(imageParserREF.getTileImage(tileType, Edge.NORTH));
		tileDisplayMapREF.put(position, new TileDisplayInfo(tileType, Edge.NORTH));
	}

	/**
	 * tile rotation information recorded;
	 * @param player the current player;
	 * @param tileType the current tile type;
	 * @param position the position where this tile is already placed;
	 * @param edgesAllowed the edges where the tile is allowed to rotate; note that the edges shows
	 *                     the possible rotations of the north face, for instance, a tile that is rotated
	 *                     once clockwise is considered facing east as the north side of the tile is now
	 */
	@Override
	public void tileRotationOptionsEvent(Player player, TileType tileType, Position position, List<Edge> edgesAllowed) {
		tileLabels[pos2Lab(position)[0]][pos2Lab(position)[1]].setTileImage(imageParserREF.getTileImage(tileType, edgesAllowed.get(0)));
		tileDisplayMapREF.put(position, new TileDisplayInfo(tileType, edgesAllowed.get(0)));
	}

	/**
	 * refresh the tile with new rotation
	 * @param player the current player;
	 * @param tileType the current tile type;
	 * @param position the position of the tile that was being rotated;
	 * @param edge the edge that the tile is After the rotation;
	 */
	@Override
	public void tileRotationChangedEvent(Player player, TileType tileType, Position position, Edge edge) {
		tileLabels[pos2Lab(position)[0]][pos2Lab(position)[1]].setTileImage(imageParserREF.getTileImage(tileType, edge));
		tileDisplayMapREF.put(position, new TileDisplayInfo(tileType, edge));
	}

	/**
	 * not used
	 * @param player the current player;
	 * @param tileType the type of the tile that was being finalized;
	 * @param position the positon of the tile that was being finalized;
	 * @param edge the rotation of the tile at position at the time of finalization;
	 */
	@Override
	public void tileFinalizeConfirmationEvent(Player player, TileType tileType, Position position, Edge edge) {

	}

	/**
	 * not used
	 * @param player the current player;
	 * @param tileType the current tile type;
	 * @param position the position of the current working tile;
	 * @param edges the edges that can be placed on
	 */
	@Override
	public void meepleOptionsEvent(Player player, TileType tileType, Position position, List<Edge> edges) {

	}

	/**
	 * refresh the tile with current meeple placement;
	 * call end turn for the game after meeple placed;
	 * @param player the current player;
	 * @param tileType the current tile type;
	 * @param position the position of the current working tile;
	 * @param edge the edge of the meeple placement;
	 */
	@Override
	public void meeplePlacementEvent(Player player, TileType tileType, Position position, Edge edge) {
		TileDisplayInfo tileDisplayInfo = tileDisplayMapREF.get(position);
		//add edge to display info
		if(tileDisplayInfo.getMeeplePlacements()==null){
			tileDisplayInfo.setMeeplePlacements(new ArrayList<>());
		}
		if(!tileDisplayInfo.getMeeplePlacements().contains(edge)){
			tileDisplayInfo.getMeeplePlacements().add(edge);
		}
		//redraw the label
		TileLabel tileLabel = tileLabels[pos2Lab(position)[0]][pos2Lab(position)[1]];
		tileLabel.setTileImage(imageParserREF.getTileImage(tileDisplayInfo, playerColorsREF[currentPlayerIndex]));
		gameREF.turnEnd();
	}

	/**
	 * call end turn for the game after meeple skipped;
	 * @param player the current player;
	 */
	@Override
	public void meepleSkippedEvent(Player player) {
		gameREF.turnEnd();
	}

	/**
	 * remove the meeple as requested from the board
	 * @param tileType the type of the tile where the meeple is removed from;
	 * @param position the position of the tile where the meeple is removed from;
	 * @param edge the edge where the meeple originally was;
	 */
	@Override
	public void meepleRemovalEvent(TileType tileType, Position position, Edge edge) {
		TileDisplayInfo tileDisplayInfo = tileDisplayMapREF.get(position);
		//add edge to display info
		if(tileDisplayInfo.getMeeplePlacements().contains(edge)){
			tileDisplayInfo.getMeeplePlacements().remove(edge);
		}
		//redraw the label
		TileLabel tileLabel = tileLabels[pos2Lab(position)[0]][pos2Lab(position)[1]];
		tileLabel.setTileImage(imageParserREF.getTileImage(tileDisplayInfo, playerColorsREF[currentPlayerIndex]));

	}

	/**
	 * not used
	 * @param list not used
	 */
	@Override
	public void scoreUpdateEvent(List<Integer> list) {

	}

	/**
	 * not used
	 * @param winners the list of winning player;
	 * @param winningScore the winning score;
	 */
	@Override
	public void gameEndedEvent(List<Player> winners, int winningScore) {
	}
}
