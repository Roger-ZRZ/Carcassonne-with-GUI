package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.Edge;
import edu.cmu.cs.cs214.hw4.core.TileType;
import java.util.ArrayList;
import java.util.List;

/**
 * A class to store the Display Information of each tile on the board;
 * Each Tile info stores the tile type, the rotation of the tile, and the meeple placements
 * on this tile; the information stored in each objects of this type will be useful for
 * drawing the tile image on the board;
 *
 * Note that this class support multiple meeple on one tile;
 * although this is not required for the base game, this is still left for extensibility;
 *
 * Note that this class does not use defensive copy and do not use immutible variables,
 * as the class is package private and most of the features for this class is for one
 * time use only; even in the case of a malicious attach that changes the tileDisplayInfo
 * information mid run, the board will not change as the tileDisplayInfo is logged after the
 * action to the board and the drawing of the board is already complete; this stays the same
 * for meeple addition and meeple removal;
 */
class TileDisplayInfo {
	private TileType tileType;//tile type/
	private Edge rotation;//rotation of the tile
	private List<Edge> meeplePlacements;//edges that have meeple on them

	/**
	 * Construct a instance of Tile display info;
	 * @param tileType type of the tile
	 * @param rotation rotation of the tile
	 * @param meeplePlacements meeple placement conditions of the tile;
	 */
	TileDisplayInfo(TileType tileType, Edge rotation, List<Edge> meeplePlacements){
		this.tileType = tileType;
		this.rotation = rotation;
		this.meeplePlacements = meeplePlacements;
	}

	/**
	 * Construct a instance of tile display info for tiles that does not yet have
	 * a meeple;
	 * @param tileType type of the tile
	 * @param rotation rotation of the tile
	 */
	TileDisplayInfo(TileType tileType, Edge rotation){
		this(tileType,rotation,new ArrayList<>());
	}

	/**
	 * Get the type of the tile stored
	 * @return tile type
	 */
	public TileType getTileType() {
		return tileType;
	}

	/**
	 * set the type of the tile
	 * @param tileType tile type
	 */
	public void setTileType(TileType tileType) {
		this.tileType = tileType;
	}

	/**
	 * get the rotation of the tile
	 * @return set the rotation of the tile
	 */
	public Edge getRotation() {
		return rotation;
	}

	/**
	 * set the rotation of the current tile;
	 * @param rotation the current rotation
	 */
	public void setRotation(Edge rotation) {
		this.rotation = rotation;
	}

	/**
	 * get the meeple placements on this tile;
	 * @return meeple placement;
	 */
	public List<Edge> getMeeplePlacements() {
		return meeplePlacements;
	}

	/**
	 * set the meeple placements to a new list
	 * @param meeplePlacements new list of meeple placements
	 */
	public void setMeeplePlacements(List<Edge> meeplePlacements) {
		this.meeplePlacements = meeplePlacements;
	}

	/**
	 * simple override to toString()
	 * @return a string representation of the tile display info;
	 */
	@Override
	public String toString() {
		return tileType+" "+rotation+" "+meeplePlacements;
	}
}
