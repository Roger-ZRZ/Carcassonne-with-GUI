package edu.cmu.cs.cs214.hw4.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The representation of the tile;
 * Note that the tile represent tiles in the deck and tiles in the board;
 * Tile is opened when the tile is placed to a position on the board;
 * Tiles will not be able to be placed on to invalid locations (maintained by board class);
 * Tiles are finalized when player finishes rotating and makes the tile placement legal;
 * Finalized Tiles will not be able to be moved on the board;
 * For more information see board class for exact usage;
 */
public class Tile {
	private TileType tileType;
	private boolean isOnBoard;//false if in deck
	private boolean isFinal;//false if working with
	private List<EdgeSegment> edgeSegments;
	private List<CornerSegment> cornerSegments;
	private Edge contentRotation;//pos of original north
	private Position position;

	/*Tile is opened when the tile is placed to a position on the board
	 * Tiles will not be able to be placed on to invalid locations (maintained by board class)
	 * Tiles are finalized when player finishes rotating and makes the tile placement legal
	 * Finalized Tiles will not be able to be moved on the board*/

	/**
	 * construct a tile with the given tiletype
	 * @param tileType tile type;
	 */
	public Tile(TileType tileType){
		this.tileType = tileType;
		contentRotation = Edge.NORTH;
		position = null;
		isOnBoard = false;
		isFinal = false;
		edgeSegments = new ArrayList();
		cornerSegments = new ArrayList();
		try {
			TileParser tileParser = new TileParser();
			for(int j=0;j<Edge.values().length;j++){
				edgeSegments.add(tileParser.getTileEdgeSegment(tileType,Edge.values()[j]));
			}
			for(int j=0;j<Corner.values().length;j++){
				cornerSegments.add(tileParser.getTileCornerSegment(tileType,Corner.values()[j]));
			}
		} catch (IOException e) {}
	}

	/**
	 *
	 * @return if the tile is in the board
	 */
	boolean isOnBoard(){
		return isOnBoard;
	}


	/**
	 *
	 * @return if the tile is finalized
	 */
	boolean isFinal(){
		return isFinal;
	}

	/**
	 * get the segment of this tile at the edge
	 * @param edge specified edge
	 * @return segments at this edge;
	 */
	EdgeSegment getEdgeSegment(Edge edge){
		return edgeSegments.get(edge.ordinal());
	}

	/**
	 *
	 * @return the type of the tile
	 */
	public TileType getTileType(){
		return tileType;
	}

	/**
	 * get the segment of this tile at the corner
	 * @param corner specified corner
	 * @return segments at this corner;
	 */
	CornerSegment getCornerSegment(Corner corner){
		return cornerSegments.get(corner.ordinal());
	}

	/**
	 *
	 * @return the rotation of the tile
	 */
	public Edge getRotation(){
		return contentRotation;
	}

	/**
	 * put the tile at the position;
	 * this method have only basic protection;
	 * you should not use this method in the top level;
	 * use the board methods instead;
	 * @param position the position
	 * @return if the tile is puttable
	 */
	boolean putTile(Position position){
		if(isFinal|| isOnBoard){
			//cannot move tile that is finalized
			//cannot move deck tiles
			return false;
		}
		this.isOnBoard = true;
		this.position = new Position(position.getX(), position.getY());
		return true;
	}

	/**
	 * finalize the tile;
	 * this method have only basic protection;
	 * you should not use this method in the top level;
	 * use the board methods instead;
	 * once the tile is finalized nothing can change;
	 * @return if finalizing is successful
	 */
	boolean finalizeTile(){
		if((!isOnBoard)||this.position==null){
			//cannot finalize a deck tile
			//cannot finalize a tile without position
			return false;
		}
		this.isFinal = true;
		return true;
	}

	/**
	 *
	 * @return position of this tile on board; if the tile
	 * is not on the board, return null;
	 */
	public Position getPosition(){
		if(!isOnBoard){
			return null;
		}
		return new Position(position.getX(), position.getY());
	}

	/**
	 * rotate clockwise once;
	 * @return if rotation is allowed
	 */
	boolean rotateTile(){
		if(isFinal||(!isOnBoard)){
			//cannot rotate tile that is finalized
			//cannot rotate deck tiles
			return false;
		}
		edgeSegments.add(0,edgeSegments.remove(Edge.WEST.ordinal()));
		cornerSegments.add(0,cornerSegments.remove(Corner.NORTH_WEST.ordinal()));
		contentRotation = (contentRotation.equals(Edge.WEST)) ? Edge.NORTH :
				Edge.values()[contentRotation.ordinal()+1];
		return true;
	}

	/**
	 * rotate the tile to make the north edge facing the specified tile;
	 * note that this rotation is relative only to the original tile
	 * @param edge edge specified
	 * @return if rotation is allowed
	 */
	boolean rotateTile(Edge edge){
		if(isFinal||(!isOnBoard)){
			//cannot rotate tile that is finalized
			//cannot rotate deck tiles
			return false;
		}

		edgeSegments = new ArrayList();
		cornerSegments = new ArrayList();
		contentRotation = Edge.NORTH;
		try {
			TileParser tileParser = new TileParser();
			for(int j=0;j<Edge.values().length;j++){
				edgeSegments.add(tileParser.getTileEdgeSegment(tileType,Edge.values()[j]));
			}
			for(int j=0;j<Corner.values().length;j++){
				cornerSegments.add(tileParser.getTileCornerSegment(tileType,Corner.values()[j]));
			}
		} catch (IOException e) {}
		for(int i=0;i<edge.ordinal();i++){
			rotateTile();
		}
		return true;
	}

	/**
	 *
	 * @return list of edges on the tile that has the segment City_Connect
	 */
	List<Edge> findCityConnect(){
		List<Edge> out = new ArrayList<>();
		for(Edge edge:Edge.values()) {
			if (edge == Edge.CENTER) {
				continue;
			}
			if (this.getEdgeSegment(edge)==EdgeSegment.CITY_CONNECT){
				out.add(edge);
			}
		}
		return out;
	}

	/**
	 *
	 * @return list of edges on the tile that has the segment Road_Connect
	 */
	List<Edge> findRoadConnect(){
		List<Edge> out = new ArrayList<>();
		for(Edge edge:Edge.values()) {
			if (edge == Edge.CENTER) {
				continue;
			}
			if (this.getEdgeSegment(edge)==EdgeSegment.ROAD_CONNECT){
				out.add(edge);
			}
		}
		return out;
	}

	/**
	 *
	 * @return list of edges on the tile that has the segment Road_End
	 */
	List<Edge> findRoadEnd(){
		List<Edge> out = new ArrayList<>();
		for(Edge edge:Edge.values()) {
			if (edge == Edge.CENTER) {
				continue;
			}
			if (this.getEdgeSegment(edge)==EdgeSegment.ROAD_END){
				out.add(edge);
			}
		}
		return out;
	}

	/**
	 *
	 * @return override to string
	 */
	@Override
	public String toString() {
		return this.tileType+":"+this.edgeSegments.toString()+" & "+
				this.cornerSegments.toString() + " & " + (isOnBoard ?"Brd":"Dek") +
				" & " + (isFinal?"Fnl":"Tmp") +
				(position==null?"":" @ "+position+"["+contentRotation+"]");
	}

}
