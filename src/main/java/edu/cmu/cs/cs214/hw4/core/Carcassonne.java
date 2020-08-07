package edu.cmu.cs.cs214.hw4.core;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the top module for Carcassonne core game implementation;
 * The module should act primarily as both a controller of the controller pattern
 * and a subject in the observer pattern;
 *
 * At current stage, the main methods and the embedded txt game play features are
 * removed, to access those features, please revert to an eariler version of the core
 * implementation;
 *
 * The current game can either be played with the GUI package included in this resp or
 * by using the decorator pattern that contains an instance of the game core itself;
 *
 * The main implementation will maintain a list of subscriber objects that can be set
 * by calling the add subscriber methods; these subscribers will be notified of all the
 * game changes in every step; for more information, place refer to the GameChangeListener
 * interface;
 *
 * This module has gone through extensive testing, and nothing should be broken or incorrect;
 * If you do spot any bugs, please contact ruizhezh@andrew;
 */
public class Carcassonne {

	//main field
	private static final List<String> DEFAULT_PLAYER_NAMES = new ArrayList<>(
			Arrays.asList("PlayerA","PlayerB","PlayerC","PlayerD"));
	private List<String> playerNames;
	private List<Player> playerList;
	private Board board;
	private Deck deck;
	private GameRuleController gameRuleController;
	private int turnCounter;
	private Tile currentTile;
	private Player currentPlayer;

	//interactive visualizer: show options, update every turn when needed
	private List<Position> positionOptions;
	private List<List<Edge>> allEdgeOptions;
	private List<Edge> currentPositionEdgeOptions;//changed after tile is placed
	private List<Edge> meepleOptions;

	//Subscriber list to notify;
	private final List<GameChangeListener> gameChangeListeners  = new ArrayList<>();

	/**
	 * Initiate game with full configurations;
	 * @param playerNames names of the player;
	 * @throws FileNotFoundException config files in the resource folder are not found or corrupted;
	 */
	public Carcassonne(List<String> playerNames) throws FileNotFoundException {
		this.playerNames = playerNames;
		if(playerNames.size()<2 || playerNames.size()>5){
			throw new IllegalArgumentException("ILLEGAL NAME COUNT");
		}
		playerList = new ArrayList<>();
		for(String string:playerNames){
			playerList.add(new Player(string));
		}
		board = new Board();
		deck = new Deck();

		//local reference passing to gameRuleController
		gameRuleController = new GameRuleController(playerList,board,deck);
		positionOptions = new ArrayList<>();
		meepleOptions = new ArrayList<>();
		allEdgeOptions = new ArrayList<>();

		//Play begins with a designated tile placed face up
		board.placeTile(deck.pop(),new Position(0, 0));
		board.finalizeLast();
		turnCounter = 0;
	}

	/**
	 *
	 * @return print the game
	 */
	@Override
	public String toString() {
		String tmp = "";
		for(Player player:playerList){
			tmp+=player+"\n";
		}
		return "======Players======\n" +tmp+
				deck+board;
	}

	//============Linking============//

	/**
	 * Get the winners who scored the highest;
	 * This should be called after the game has ended;
	 * @return list of winning players;
	 */
	private List<Player> getWinner(){
		int maxScore = -1;
		for(Player player: playerList){
			if(player.getScore()>maxScore){
				maxScore = player.getScore();
			}
		}
		List<Player> out = new ArrayList<>();
		for(Player player: playerList){
			if(player.getScore()==maxScore){
				out.add(player);
			}
		}
		return out;
	}

	/**
	 * Get the list of scores of all the players in order;
	 * @return list of integers representing scores;
	 */
	private List<Integer> getScores(){
		List<Integer> out = new ArrayList<>();
		for(Player player: playerList){
			out.add(player.getScore());
		}
		return out;
	}

	/**
	 * Get the index of the current player;
	 * @return index of the current player, starting from 0;
	 */
	public int getCurrentPlayerIndex(){
		return playerList.indexOf(currentPlayer);
	}

	//============GUI Helpers - Control Flow============//

	/**set turn counter and player, notify game start and notify player change;*/
	public void gameStart(){
		turnCounter = 1;
		currentPlayer = playerList.get(0);
		notifyGameStart();
		notifyPlayerChange();
		return;
	}

	/**<can call gameEnd> set current tile, notify tile dealt, notify position options*/
	public void requestTileFromDeck(){
		//loops until placeable tile found or deck empty
		while(true){
			Tile peekTop = deck.pop();//ref
			if(peekTop==null){//ran out of tiles
				gameEnd();
				return;
			}
			List<Position> pPotn = board.getPlaceablePositions();//ref
			positionOptions.clear();
			allEdgeOptions.clear();

			//run out of space in board, should never happen (unless memory full or thread cut)
			if(pPotn.size()==0){
				System.out.println("\n\nFATAL:Placeable Positions Empty Exception;\n\nCrashing Main(); \n\n");
			}

			//test each pos for valid placements
			for(Position pos:pPotn){
				if(board.testTile(peekTop, pos)!=null){
					positionOptions.add(pos);
					allEdgeOptions.add(board.testTile(peekTop, pos));
				}
			}

			if(!positionOptions.isEmpty()){
				currentTile = peekTop;
				notifyTileDealt();
				notifyTilePlacementOption();
				return;
			}

		}
	}

	/**place tile on board, set curntPosEdgeOptn, notify tile placement, notify rotationOption
	 * @param posChoice position of choice
	 * */
	public void playerPlaceTileOnBoard(Position posChoice){
		if(positionOptions.contains(posChoice)){
			board.placeTile(currentTile, posChoice);
			currentPositionEdgeOptions = allEdgeOptions.get(positionOptions.indexOf(posChoice));
			currentTile.rotateTile(currentPositionEdgeOptions.get(0));//set to first as default
			notifyTilePlacement();
			notifyTileRotationOptions();
			return;
		}
		return;
	}

    /**rotate tile, notify rotation
     * @param edge rotation after the rotation*/
	public void playerChangeRotation(Edge edge){
		if(currentPositionEdgeOptions.contains(edge)){
			currentTile.rotateTile(edge);
			notifyTileRotationChange();
			return;
		}
		return;
	}

	/**finalize tile, get meeple options, notify placement confirmation, notify meeple options*/
	public void playerConfirmRotation(){
		if(board.finalizeLast()){
			//max meeple limit enforced in getPlaceableMeepleEdges;
			meepleOptions = gameRuleController.getPlaceableMeepleEdges(currentTile, currentPlayer);
			notifyTileFinalizeConfirmation();
			notifyMeepleOptions();//if empty, means cant place
		}
		return;
	}

	/**branch 1: player place meeple, notify meeple placed
	 * @param edge the player placed the meeple*/
	public void playerPlaceMeeple(Edge edge){

		if(meepleOptions.contains(edge)){

			currentPlayer.placeMeeple(currentTile, edge);
			notifyMeeplePlacement(edge);
		}
		return;
	}

	/**branch 2: player will not place meeple, notify meeple skipped*/
	public void playerSkipMeeple(){
		notifyMeepleSkipped();
		return;
	}

	/**turn is done, notify meeple removal and player change at last*/
	public void turnEnd(){
		//score update and remove meeple
		gameRuleController.scoreUpdate(currentTile);
		notifyScoreUpdate();
		List<Meeple> removedMeeples = gameRuleController.getMeepleRemovedThisRound();
		for(Meeple meeple:removedMeeples){
			notifyMeepleRemoved(meeple);
		}
		//advance turn
		int curPI = playerList.indexOf(currentPlayer);
		currentPlayer = playerList.get( (curPI==playerList.size()-1)?0:curPI+1 );
		turnCounter++;
		notifyPlayerChange();
		return;
	}

	/**game ended, notify meeple removal and winners*/
	private void gameEnd(){
		//remove meeples (should be all meeples left)
		gameRuleController.scoreFinalBoard();
		notifyScoreUpdate();
		List<Meeple> removedMeeples = gameRuleController.getMeepleRemovedThisRound();
		for(Meeple meeple:removedMeeples){
			notifyMeepleRemoved(meeple);
		}
		notifyGameEnd(getWinner());
		return;
	}

	//===========Publishing (Do at Last)===========//

	/**
	 * add listener that will be notified for all the gameplay events;
	 * @param listener a listener object;
	 */
	public void addGameChangeListener(GameChangeListener listener) {
		gameChangeListeners.add(listener);
	}

	/**
	 * Notify game started;
	 * Not substitute for player changed;
	 */
	private void notifyGameStart() {
		for (GameChangeListener listener : gameChangeListeners) {
			listener.gameStartEvent(board.getTile(new Position(0, 0)).getTileType(), playerNames);
		}
	}

	/**
	 * Notify player changed; called at the beginning of each turn;
	 */
	private void notifyPlayerChange() {
		for (GameChangeListener listener : gameChangeListeners) {
			listener.currentPlayerChangeEvent(currentPlayer, turnCounter);
		}
	}

	/**
	 * Notify when a tile is being dealt
	 */
	private void notifyTileDealt() {
		for (GameChangeListener listener : gameChangeListeners) {
			listener.tileDealtEvent(currentPlayer, currentTile.getTileType());
		}
	}

	/**
	 * notify the tile placement options
	 */
	private void notifyTilePlacementOption() {
		for (GameChangeListener listener : gameChangeListeners) {
			listener.tilePlacementOptionsEvent(currentPlayer, positionOptions);
		}
	}

	/**
	 * notify initial tile placement
	 */
	private void notifyTilePlacement() {
		for (GameChangeListener listener : gameChangeListeners) {
			listener.tilePlacementEvent(currentPlayer, currentTile.getTileType(), currentTile.getPosition());
		}
		//System.out.println(currentTile);
	}

	/**
	 * notify tile rotation options
	 */
	private void notifyTileRotationOptions(){
		for (GameChangeListener listener : gameChangeListeners) {
			listener.tileRotationOptionsEvent(currentPlayer, currentTile.getTileType(), currentTile.getPosition(), currentPositionEdgeOptions);
		}
	}

	/**
	 * notify tile rotation change
	 */
	private void notifyTileRotationChange(){
		for (GameChangeListener listener : gameChangeListeners) {
			listener.tileRotationChangedEvent(currentPlayer, currentTile.getTileType(),currentTile.getPosition(), currentTile.getRotation());
		}
	}

	/**
	 * notify finalization
	 */
	private void notifyTileFinalizeConfirmation(){
		for (GameChangeListener listener : gameChangeListeners) {
			listener.tileFinalizeConfirmationEvent(currentPlayer, currentTile.getTileType(),currentTile.getPosition(), currentTile.getRotation());
		}
	}

	/**
	 * notify meeple options
	 */
	private void notifyMeepleOptions(){
		for (GameChangeListener listener : gameChangeListeners) {
			listener.meepleOptionsEvent(currentPlayer, currentTile.getTileType(),currentTile.getPosition(), meepleOptions);
		}
	}

	/**
	 * notify meeple placement
	 * @param edge edge that the meeple was placed
	 */
	private void notifyMeeplePlacement(Edge edge){
		Position p = currentTile.getPosition();//storing this as the currentTile will soon change
		for (GameChangeListener listener : gameChangeListeners) {
			listener.meeplePlacementEvent(currentPlayer, currentTile.getTileType(),p, edge);
		}
	}

	/**
	 * notify the meeple is skipped
	 */
	private void notifyMeepleSkipped(){
		for (GameChangeListener listener : gameChangeListeners) {
			listener.meepleSkippedEvent(currentPlayer);
		}
	}

	/**
	 * notify the meeple is removed
	 * @param meeple meeple removed
	 */
	private void notifyMeepleRemoved(Meeple meeple){
		for (GameChangeListener listener : gameChangeListeners) {
			listener.meepleRemovalEvent(meeple.getOccupiedTile().getTileType(),meeple.getOccupiedPosition(),meeple.getOccupiedEdge());
		}
	}

	/**
	 * notify the score is being updated
	 */
	private void notifyScoreUpdate(){
		for (GameChangeListener listener : gameChangeListeners) {
			listener.scoreUpdateEvent(this.getScores());
		}
	}

	/**
	 * notify the game end and winner
	 * @param winners winners
	 */
	private void notifyGameEnd(List<Player> winners){
		for (GameChangeListener listener : gameChangeListeners) {
			listener.gameEndedEvent(winners, winners.get(0).getScore());
		}
	}
}
