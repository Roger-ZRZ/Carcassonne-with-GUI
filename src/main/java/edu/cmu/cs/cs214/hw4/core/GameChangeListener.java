package edu.cmu.cs.cs214.hw4.core;

import java.util.List;

/**
 * The listener interface supporting the observer pattern in the core game implementation;
 * This interface should be implemented by any external classes with the wish to act as a
 * subscriber of the core carcassonne implementation and obtain information of the game
 * progression in real time;
 *
 * Note that some methods will pass an abundance of information that might go unused,
 * this is due to design with reuse and extensibility in mind;
 * In these cases, most of the arguments passed are generic argument tiles and enums,
 * with one exception of the Player class, which is passed out as an object;
 * Note that for the Player class, only the method p.getName() is public and can
 * be used out of the package; the reason way a String was not used instead of the
 * player class was due to possible expansions in the future where other information
 * of the player may be needed;
 */
public interface GameChangeListener {

	/**
	 * Event for the game starting;
	 * The information of the first tile that was placed on the board before the players are involved and
	 * the names of the players are passed to the observers;
	 * @param tileType first tile that was placed on the board
	 * @param playerNames names of the players
	 */
	void gameStartEvent(TileType tileType, List<String> playerNames);

	/**
	 * Event for the turn changing and the player changing;
	 * this method is only called at the start of each turn after the player is changed;
	 * @param player the player of the new turn;
	 * @param turn the number of the current turn, starting from 1;
	 */
	void currentPlayerChangeEvent(Player player, int turn);

	/**
	 * Event where the player was granted a tile by the controller;
	 * this methods is only called at the tile dealing event in the game at the start of
	 * each turn; in event when there is no tile that can be dealt anymore, this event
	 * should not be called; call gameEnd() instead;
	 * @param player the player who is playing at this new turn;
	 * @param tileType the type of the tile that was being dealt to this player;
	 */
	void tileDealtEvent(Player player, TileType tileType);

	/**
	 * Event where the possible placements of the current player with the current tile is
	 * computed and returned for the listeners; this function should be helpful for drawing
	 * the board to show the positions on the GUI where a tile can be placed at this stage;
	 * @param player the current player;
	 * @param positionsAllowed the positions that are allowed for placement;
	 */
	void tilePlacementOptionsEvent(Player player, List<Position> positionsAllowed);

	/**
	 * Event where the player placed the tile on the position specified;
	 * The observers will be notified once the placement is complete;
	 * Note that this is only the initial placement notification, as the player is able and
	 * likely will rotate the tile after the initial placement;
	 * Nothing is finalized in this step and no permanent features should be drawn;
	 * @param player the current player;
	 * @param tileType the current placed tile type;
	 * @param position the position where the current tile is placed;
	 */
	void tilePlacementEvent(Player player, TileType tileType, Position position);

	/**
	 * Event where the possible rotations of the current player with the current tile is
	 * computed and returned for the listeners; this function should be helpful for drawing
	 * the board to show the rotation options on the GUI where a tile can be placed at this
	 * stage;
	 * @param player the current player;
	 * @param tileType the current tile type;
	 * @param position the position where this tile is already placed;
	 * @param edgesAllowed the edges where the tile is allowed to rotate; note that the edges shows
	 *                     the possible rotations of the north face, for instance, a tile that is rotated
	 *                     once clockwise is considered facing east as the north side of the tile is now
	 *                     facing east;
	 */
	void tileRotationOptionsEvent(Player player, TileType tileType, Position position, List<Edge> edgesAllowed);

	/**
	 * Event where the tile is being rotated to some edge by the player;
	 * The notification is made after the tile is rotated;
	 * Note that the tile is not final after this rotation, and can still be rotated;
	 * This method may be called as many times as needed in each turn;
	 * @param player the current player;
	 * @param tileType the current tile type;
	 * @param position the position of the tile that was being rotated;
	 * @param edge the edge that the tile is After the rotation;
	 */
	void tileRotationChangedEvent(Player player, TileType tileType, Position position, Edge edge);

	/**
	 * Event where the player confirms that rotation and finalizes the tile on the board;
	 * The notification is made after the finalization of the tile;
	 * Note that after the finalization no changes should be and can be made to the tile
	 * that is currently on the board;
	 * @param player the current player;
	 * @param tileType the type of the tile that was being finalized;
	 * @param position the positon of the tile that was being finalized;
	 * @param edge the rotation of the tile at position at the time of finalization;
	 */
	void tileFinalizeConfirmationEvent(Player player, TileType tileType, Position position, Edge edge);

	/**
	 * Event where the computation of the meeple placements acceptable for this tile is done;
	 * The observers are now notified of the choices for meeple placements;
	 * No meeples were harmed or subjected to the absolute horror of being placed on the board in
	 * the midst of this function; nothing is finalized;
	 * @param player the current player;
	 * @param tileType the current tile type;
	 * @param position the position of the current working tile;
	 * @param edges the edges where the meeple can be placed;
	 */
	void meepleOptionsEvent(Player player, TileType tileType, Position position, List<Edge> edges);

	/**
	 * The event where the player places a meeple at the edge of a tile;
	 * The notification is made after the finalization of the meeple placement;
	 * Note that after the event is notified the tile and the meeple will become final and should not
	 * be changed anymore;
	 * @param player the current player;
	 * @param tileType the current tile type;
	 * @param position the position of the current working tile;
	 * @param edge the edge of the meeple placement;
	 */
	void meeplePlacementEvent(Player player, TileType tileType, Position position, Edge edge);

	//Player skipped meeple placement

	/**
	 * Event where the player skipped the meeple placement at this event;
	 * Note that there is no auto detection for max meeples;
	 * the detection should be implemented in the meepleOptionEvent();
	 * @param player the current player;
	 */
	void meepleSkippedEvent(Player player);

	/**
	 * Event where a meeple is being evicted from the board;
	 * The function is called after the meeple is being removed from the board;
	 * Note that this should only be called in the scoring process;
	 * Placing and removing meeples to show player choice is not advised;
	 * @param tileType the type of the tile where the meeple is removed from;
	 * @param position the position of the tile where the meeple is removed from;
	 * @param edge the edge where the meeple originally was;
	 */
	void meepleRemovalEvent(TileType tileType, Position position, Edge edge);

	/**
	 * Event wher the score list updates;
	 * @param scoreList the new score list;
	 */
	void scoreUpdateEvent(List<Integer> scoreList);

	/**
	 * The event where the game ends either due to premature rejection (due to turn
	 * limits specified) or due to no tiles can be played anymore;
	 * @param winners the list of winning player;
	 * @param winningScore the winning score;
	 */
	void gameEndedEvent(List<Player> winners, int winningScore);
}
