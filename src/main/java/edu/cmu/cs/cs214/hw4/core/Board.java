package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representation of a board, with each tile placement recorded;
 * The board is container for all of the tiles that are placed in the board;
 * The board have functions that automatically checks for tile placements to
 * determined whether a tile placement is valid or not;
 * The board also has an "verbose" return design, in that for function calls
 * that are relevant, the board methods will return items that will dictate
 * the right choices that you can make;
 * This class is package private;
 * For GUI implementations, wrappers will exist in the top module using the
 * controller pattern;
 *
 * Note that in this class, some methods may not be very necessary for this
 * implementation, but will aid greatly in adapting the class to expansions
 * and / or scoring fields if needed; I included them for such reasons;
 */
class Board {
	/*
	 * Note that methods relevant to the adjacency finding mechanisms are also
	 * built directly in to the board;
	 * this is a design choice to minimize information passing and follows the
	 * creator heuristic;
	 */

	private Map<Position,Tile> tileMap;//map of tiles and their placement;
	private List<Position> placeablePositions;//placeable positions for next tile;
	private Tile lastTile;//last placed tile;

	/**
	 * Constructor for a new and empty board;
	 * Note at this time there will be no tile on the board;
	 * You can ONLY place on the (0,0) position of the new board;
	 */
	Board(){
		tileMap = new HashMap<Position,Tile>();
		placeablePositions = new ArrayList<Position>();
		placeablePositions.add(new Position(0, 0));
		lastTile = null;
	}

	/**
	 * Get placeable positions;
	 * @return currently placeable positions;
	 */
	List<Position> getPlaceablePositions() {
		List<Position> out = new ArrayList<>();
		for(Position position:placeablePositions){
			out.add(position);
		}
		return out;
	}

	/**
	 * Place a tile on to the board;
	 * Return the possible rotations the tile can be to make the placement valid;
	 * Tile placement will fail if either the position you are placing to is going to
	 * break the rules of positioning, or the position you are placing to can not accept
	 * the tile with any rotation changes;
	 * Yes, this methods safe guards it for you;
	 * Note that after placing the tile you will have time to rotate the tile;
	 *
	 * If you follow the returns of the method getPlaceablePositions(), you will never
	 * need to worry about this method failing, otherwise, good luck;
	 *
	 * Note that if the tile placement fails, the function will return null;
	 *
	 * This method is package private;
	 *
	 * @param tile the tile you wish to be placed, note that this tile should be directly taken from the deck;
	 *             you are NOT allowed to move already place tiles;
	 * @param position the position you want this tile to be;
	 * @return possible rotations the tile can be to make the placement valid, if this placement is invalid,
	 * null will be returned and the tile will not be placed;
	 */
	List<Edge> placeTile(Tile tile, Position position){
		if(lastTile !=null){
			//must finalize before adding
			if(lastTile.isFinal()){
				lastTile = null;
			}else if(tilePlaceableAtPositionWithCurrentRotation(lastTile, lastTile.getPosition())){
				lastTile.finalizeTile();
				lastTile = null;
			}else{
				return null;//last tile is not properly placed yet
			}
		}
		List<Edge> placeableEdges = tilePlaceableAtPosition(tile,position);
		if(tileMap.get(position)!=null){
			return null; //position already used
		}
		if(!placeablePositions.contains(position)){
			return null; //cannot place here
		}
		if(tile.isOnBoard()||tile.isFinal()){
			return null; //cannot add open tile
		}
		if(placeableEdges.size()==0){
			return null; //no rotation can make this position work
		}
		tile.putTile(position);
		tileMap.put(position, tile);
		lastTile = tile;
		updatePlaceableList(tile);
		return placeableEdges;
	}

	/**
	 * Test a tile on to the board;
	 * Return the possible rotations the tile can be to make the placement valid;
	 * Tile placement will fail if either the position you are placing to is going to
	 * break the rules of positioning, or the position you are placing to can not accept
	 * the tile with any rotation changes;
	 * Yes, this methods safe guards it for you;
	 *
	 * If you follow the returns of the method getPlaceablePositions(), you will never
	 * need to worry about this method failing, otherwise, good luck;
	 *
	 * Note that if the tile placement fails, the function will return null;
	 * NOTE that this only tests the tile placement, and will NOT place the tile on the board;
	 * use the placeTile() method if you intend to place a tile on the board;
	 *
	 * This method is package private;
	 *
	 * @param tile the tile you wish to be placed, note that this tile should be directly taken from the deck;
	 *             you are NOT allowed to move already place tiles;
	 * @param position the position you want this tile to be;
	 * @return possible rotations the tile can be to make the placement valid, if this placement is invalid,
	 * null will be returned and the tile will not be placed;
	 */
	List<Edge> testTile(Tile tile, Position position){
		List<Edge> placeableEdges = tilePlaceableAtPosition(tile,position);
		if(tileMap.get(position)!=null){
			return null; //position already used
		}
		if(!placeablePositions.contains(position)){
			return null; //cannot place here
		}
		if(tile.isOnBoard()||tile.isFinal()){
			return null; //cannot add open tile
		}
		if(placeableEdges.size()==0){
			return null; //no rotation can make this position work
		}
		return placeableEdges;
	}

	/**
	 * This methods should be private, but was made package private
	 * for testing; do NOT rely on this;
	 * @return the last tile placed that is still being worked on;
	 */
	Tile unfinalizedTile() {
		return lastTile;
	}

	/**
	 * Finalize the last tile;
	 * return false if and only if the last tile is still violating
	 * the gamerules regarding its rotation;
	 * This method is package private;
	 * @return false if and only if the last tile is still violating
	 * 	 the gamerules regarding its rotation;
	 */
	boolean finalizeLast() {
		if(lastTile==null||lastTile.isFinal()){
			return true;
		}
		if (tilePlaceableAtPositionWithCurrentRotation(lastTile, lastTile.getPosition())) {
			lastTile.finalizeTile();
			lastTile = null;
			return true;
		}
		return false;//cannot finalize last tile yet, invalid placement
	}

	/**
	 * Update the list of placeable positions after a new tile is placed;
	 * This is a private method;
	 * @param tile the tile that was just placed;
	 */
	private void updatePlaceableList(Tile tile){
		//tile: newly placed tile;
		placeablePositions.remove(placeablePositions.indexOf(tile.getPosition()));
		int x = tile.getPosition().getX();
		int y = tile.getPosition().getY();
		if(getTile(new Position(x+1, y))==null && placeablePositions.indexOf(new Position(x+1, y))==-1){
			placeablePositions.add(new Position(x+1, y));
		}
		if(getTile(new Position(x-1, y))==null && placeablePositions.indexOf(new Position(x-1, y))==-1){
			placeablePositions.add(new Position(x-1, y));
		}
		if(getTile(new Position(x, y-1))==null && placeablePositions.indexOf(new Position(x, y-1))==-1){
			placeablePositions.add(new Position(x, y-1));
		}
		if(getTile(new Position(x, y+1))==null && placeablePositions.indexOf(new Position(x, y+1))==-1){
			placeablePositions.add(new Position(x, y+1));
		}
	}

	/**
	 * Check if a tile is placeable at some location with any rotation;
	 * This does NOT place any tiles or alter the board in any way;
	 * returns list of rotations that can make the tile fit;
	 * @param tile the tile specified
	 * @param position the position specified
	 * @return list of the rotations that this tile can fit in with; if nothing can
	 * make the tile fit, return empty list;
	 */
	private List<Edge> tilePlaceableAtPosition(Tile tile,Position position){
		if(tile==null){
			return null;
		}
		List<Edge> out = new ArrayList<>();
		EdgeSegment segNorth = tile.getEdgeSegment(Edge.NORTH);
		EdgeSegment segEast = tile.getEdgeSegment(Edge.EAST);
		EdgeSegment segSouth = tile.getEdgeSegment(Edge.SOUTH);
		EdgeSegment segWest = tile.getEdgeSegment(Edge.WEST);
		List<Tile> adjTiles = getAdjacentTiles(position,true);
		if(adjacentEdgeMatchAll(adjTiles.get(0),adjTiles.get(1),adjTiles.get(2),adjTiles.get(3),segNorth,segEast,segSouth,segWest)){
			out.add(Edge.NORTH);
		}
		if(adjacentEdgeMatchAll(adjTiles.get(0),adjTiles.get(1),adjTiles.get(2),adjTiles.get(3),segWest,segNorth,segEast,segSouth)){
			out.add(Edge.EAST);
		}
		if(adjacentEdgeMatchAll(adjTiles.get(0),adjTiles.get(1),adjTiles.get(2),adjTiles.get(3),segSouth,segWest,segNorth,segEast)){
			out.add(Edge.SOUTH);
		}
		if(adjacentEdgeMatchAll(adjTiles.get(0),adjTiles.get(1),adjTiles.get(2),adjTiles.get(3),segEast,segSouth,segWest,segNorth)){
			out.add(Edge.WEST);
		}
		return out;
	}

	/**
	 * Check if a tile is placeable at some location with THIS rotation;
	 * This does NOT place any tiles or alter the board in any way;
	 * returns boolean value;
	 * @param tile the tile specified
	 * @param position the position specified
	 * @return if a tile is placeable at some location with THIS rotation;
	 */
	private boolean tilePlaceableAtPositionWithCurrentRotation(Tile tile, Position position){
		if(tile==null){
			return false;
		}
		EdgeSegment segNorth = tile.getEdgeSegment(Edge.NORTH);
		EdgeSegment segEast = tile.getEdgeSegment(Edge.EAST);
		EdgeSegment segSouth = tile.getEdgeSegment(Edge.SOUTH);
		EdgeSegment segWest = tile.getEdgeSegment(Edge.WEST);
		List<Tile> adjTiles = getAdjacentTiles(position,true);
		return  adjacentEdgeMatchAll(adjTiles.get(0),adjTiles.get(1),adjTiles.get(2),adjTiles.get(3),segNorth,segEast,segSouth,segWest);
	}

	/**
	 * Get a tile at a position;
	 * returns null if no tile is at this location;
	 * @param position specified location;
	 * @return the tile itself if the position is occupied, null if the position is not occupied;
	 */
	Tile getTile(Position position){
		return tileMap.get(position);
	}

	/**
	 * check if two tiles is adjacent;
	 * note the the third argument provides a choice between whether to include
	 * diagonals in the definition set of adjacency;
	 * @param t1 first tile
	 * @param t2 second tile
	 * @param includeDiagonal whether include diagonal;
	 * @return boolean value for adjacency;
	 */
	boolean isAdjacentTile(Tile t1, Tile t2, boolean includeDiagonal){
		Position pos1 = t1.getPosition();
		Position pos2 = t2.getPosition();
		if(pos1==null || pos2==null){
			return false;
		}
		return pos1.isAdjacent(pos2, includeDiagonal);
	}

	/**
	 * get a list of all the adjacent tiles, with the first segment being in order clockwise
	 * for edges, and the second segment being in order clockwise for corners, starting from
	 * the northeast corner; center is not included;
	 * @param tile the tile we are specifying
	 * @param includeDiagonal whether to include diagonally adjacent tiles in the print out;
	 * @param includeNull whether to include null elements in the list as place holders for empty tiles;
	 *                    if this is false, the order is no longer strictly in order with all the elements;
	 * @return return the list of all the adjacent tiles, with the two params applied;
	 */
	List<Tile> getAdjacentTiles(Tile tile, boolean includeDiagonal, boolean includeNull){
		if((!tile.isOnBoard())||getTile(tile.getPosition())==null) {
			return null;
		}
		List<Tile> out = new ArrayList<>();
		int x = tile.getPosition().getX();
		int y = tile.getPosition().getY();

		if(includeNull||getTile(new Position(x, y+1))!=null){
			out.add(getTile(new Position(x, y+1)));
		}
		if(includeNull||getTile(new Position(x+1, y))!=null){
			out.add(getTile(new Position(x+1, y)));
		}
		if(includeNull||getTile(new Position(x, y-1))!=null){
			out.add(getTile(new Position(x, y-1)));
		}
		if(includeNull||getTile(new Position(x-1, y))!=null){
			out.add(getTile(new Position(x-1, y)));
		}

		if(includeNull||(includeDiagonal && getTile(new Position(x+1, y+1))!=null)){
			out.add(getTile(new Position(x+1, y+1)));
		}
		if(includeNull||(includeDiagonal && getTile(new Position(x-1, y-1))!=null)){
			out.add(getTile(new Position(x+1, y-1)));
		}
		if(includeNull||(includeDiagonal && getTile(new Position(x+1, -1))!=null)){
			out.add(getTile(new Position(x-1, y-1)));
		}
		if(includeNull||(includeDiagonal && getTile(new Position(x-1, y+1))!=null)){
			out.add(getTile(new Position(x-1, y+1)));
		}

		return out;
	}

	/**
	 * get a list of all the adjacent tiles, with the first segment being in order clockwise
	 * for edges, and the second segment being in order clockwise for corners, starting from
	 * the northeast corner; center is not included;
	 * @param position the position we are specify ing
	 * @param includeNull whether to include null elements in the list as place holders for empty tiles;
	 *                    if this is false, the order is no longer strictly in order with all the elements;
	 * @return return the list of all the adjacent tiles, with the two params applied;
	 */
	List<Tile> getAdjacentTiles(Position position, boolean includeNull){
		List<Tile> out = new ArrayList<>();
		int x = position.getX();
		int y = position.getY();

		if(includeNull||getTile(new Position(x, y+1))!=null){
			out.add(getTile(new Position(x, y+1)));
		}
		if(includeNull||getTile(new Position(x+1, y))!=null){
			out.add(getTile(new Position(x+1, y)));
		}
		if(includeNull||getTile(new Position(x, y-1))!=null){
			out.add(getTile(new Position(x, y-1)));
		}
		if(includeNull||getTile(new Position(x-1, y))!=null){
			out.add(getTile(new Position(x-1, y)));
		}

		return out;
	}

	/**
	 * if tile can accept the segment testAccept at edge
	 * @param tile specified tile
	 * @param edge specified edge
	 * @param testAccept specified segment
	 * @return if tile can accept the segment testAccept at edge
	 */
	private boolean adjacentEdgeMatch(Tile tile, Edge edge, EdgeSegment testAccept){
		if(tile==null){
			return true;//nothing can accept anything
		}
		return tile.getEdgeSegment(edge).matches(testAccept);
	}

	/**
	 * test if a tile with edge segment specified by arg 5-8 can be placed in the middle of the tiles
	 * specified by the arg 1-4;
	 * @param tileNorth specified tile
	 * @param tileEast specified tile
	 * @param tileSouth specified tile
	 * @param tileWest specified tile
	 * @param segNorth specified segment
	 * @param segEast specified segment
	 * @param segSouth specified segment
	 * @param segWest specified segment
	 * @return if a tile with edge segment specified by arg 5-8 can be placed in the middle of the tiles
	 * 	specified by the arg 1-4;
	 */
	private boolean adjacentEdgeMatchAll(Tile tileNorth,Tile tileEast,Tile tileSouth,Tile tileWest,
	            EdgeSegment segNorth,EdgeSegment segEast, EdgeSegment segSouth, EdgeSegment segWest){
		return adjacentEdgeMatch(tileNorth, Edge.SOUTH,segNorth) &&
				adjacentEdgeMatch(tileSouth, Edge.NORTH,segSouth) &&
				adjacentEdgeMatch(tileEast, Edge.WEST,segEast) &&
				adjacentEdgeMatch(tileWest, Edge.EAST,segWest);
	}

	/**
	 * get the tile on the board in the specified edge direction of the tile specified in the first argument
	 * @param tile the specified tile
	 * @param edge the edge of the direction
	 * @return the tile on the board in the specified edge direction of the tile specified in the first argument
	 */
	Tile getTileInDirection(Tile tile,Edge edge){
		if(tile==null||(!tile.isOnBoard())){
			//tile not existing or not on the board
			return null;
		}
		Position position = tile.getPosition();
		int tX = position.getX();
		int tY = position.getY();
		Position requestedPosition;
		if(edge==Edge.NORTH){
			requestedPosition = new Position(tX, tY+1);
		}else if(edge==Edge.SOUTH){
			requestedPosition = new Position(tX, tY-1);
		}else if(edge==Edge.WEST){
			requestedPosition = new Position(tX-1, tY);
		}else if(edge==Edge.EAST){
			requestedPosition = new Position(tX+1, tY);
		}else{
			return null;
		}
		return tileMap.get(requestedPosition);
	}

	/**
	 * get the tile on the board in the specified corner direction of the tile specified in the first argument
	 * @param tile the specified tile
	 * @param corner the corner of the direction
	 * @return the tile on the board in the specified corner direction of the tile specified in the first argument
	 */
	Tile getTileInDirection(Tile tile,Corner corner){
		if(tile==null||(!tile.isOnBoard())){
			//tile not existing or not on the board
			return null;
		}
		Position position = tile.getPosition();
		int tX = position.getX();
		int tY = position.getY();
		Position requestedPosition;
		if(corner==Corner.NORTH_EAST){
			requestedPosition = new Position(tX+1, tY+1);
		}else if(corner==Corner.NORTH_WEST){
			requestedPosition = new Position(tX-1, tY+1);
		}else if(corner==Corner.SOUTH_EAST){
			requestedPosition = new Position(tX+1, tY-1);
		}else{// if(corner==Corner.SOUTH_WEST){
			requestedPosition = new Position(tX-1, tY-1);
		}
		return tileMap.get(requestedPosition);
	}

	/**
	 * Find the Road Tiles directly adjacent with the passed in tile and connected to the passed in edges of such tile;
	 * Note that the tile passed in is not returned in the list of tileToEdgeAssociations;
	 * Definition of connected see game rules;
	 *
	 * @param tileToEdgeAssociation the tile starting from and which edges to start with
	 * @return adjacent and connected tiles excluding the passed in tile;
	 */
	private List<TileToEdgeAssociation> findAdjacentConnectedRoadTile(TileToEdgeAssociation tileToEdgeAssociation){
		Tile tile = tileToEdgeAssociation.getTile();
		List<Edge> passingEdgeListIn = tileToEdgeAssociation.getAssociatedEdges();
		List<TileToEdgeAssociation> out = new ArrayList<>();

		if(passingEdgeListIn.contains(Edge.CENTER)){
			return findAdjacentConnectedRoadTile(new TileToEdgeAssociation(tile, List.of(Edge.NORTH,Edge.EAST,Edge.SOUTH,Edge.WEST)));
		}
		if(passingEdgeListIn.size()==1&&tile.getEdgeSegment(passingEdgeListIn.get(0))==EdgeSegment.ROAD_CONNECT){
			return findAdjacentConnectedRoadTile(new TileToEdgeAssociation(tile, tile.findRoadConnect()));
		}

		for(Edge edge:passingEdgeListIn){
			if(tile.getEdgeSegment(edge)!=EdgeSegment.ROAD_CONNECT &&
					tile.getEdgeSegment(edge)!=EdgeSegment.ROAD_END){
				continue;
			}
			Tile adjE = getTileInDirection(tile, edge);
			if(adjE==null){
				continue;
			}
			List<Edge> adjEPathList = new ArrayList<>();
			if(adjE.getEdgeSegment(edge.getOppositeEdge())==EdgeSegment.ROAD_END){
				adjEPathList.add(edge.getOppositeEdge());
			}else{
				adjEPathList = adjE.findRoadConnect();
			}
			out.add(new TileToEdgeAssociation(adjE, adjEPathList));
		}
		return out;
	}

	/**
	 * Find the city Tiles directly adjacent with the passed in tile and connected to the passed in edges of such tile;
	 * Note that the tile passed in is not returned in the list of tileToEdgeAssociations;
	 * Definition of connected see game rules;
	 *
	 * @param tileToEdgeAssociation the tile starting from and which edges to start with
	 * @return adjacent and connected tiles excluding the passed in tile;
	 */
	private List<TileToEdgeAssociation> findAdjacentConnectedCityTile(TileToEdgeAssociation tileToEdgeAssociation){
		Tile tile = tileToEdgeAssociation.getTile();
		List<Edge> passingEdgeListIn = tileToEdgeAssociation.getAssociatedEdges();
		List<TileToEdgeAssociation> out = new ArrayList<>();

		if(passingEdgeListIn.contains(Edge.CENTER)){
			return findAdjacentConnectedCityTile(new TileToEdgeAssociation(tile, List.of(Edge.NORTH,Edge.EAST,Edge.SOUTH,Edge.WEST)));
		}

		for(Edge edge:passingEdgeListIn){
			if(tile.getEdgeSegment(edge)!=EdgeSegment.CITY_CONNECT){
				continue;
			}
			Tile adjE = getTileInDirection(tile, edge);
			if(adjE==null){
				continue;
			}
			List<Edge> adjEPathList = new ArrayList<>();
			if(adjE.getEdgeSegment(Edge.CENTER)!=EdgeSegment.CITY &&
					adjE.getEdgeSegment(Edge.CENTER)!=EdgeSegment.CITY_GUARDED){
				adjEPathList.add(edge.getOppositeEdge());
			}else{
				adjEPathList = adjE.findCityConnect();
			}
			out.add(new TileToEdgeAssociation(adjE, adjEPathList));
		}
		return out;
	}

	/**
	 * Find the list of tile to edge associations that contains and only contains the whole city feature, either complete
	 * or uncomplete, and specified by the starting of the passed in tile and pathed from the passed in edges of such tile;
	 * The returned list should contain the passed in tile;
	 * Definition of connected see game rules;
	 *
	 * This module is tested extensively on all ambiguities, especially cities associated with tile I and such;
	 * This module can be extended to score fields;
	 *
	 * @param tileToEdgeAssociation the tile starting from and which edges to start with
	 * @return the list of tile to edge associations that contains and only contains the whole city feature, either complete
	 * 	or uncomplete, and specified by the starting of the passed in tile and pathed from the passed in edges of such tile;
	 */
	private List<TileToEdgeAssociation> getCityFeatureFromStartingTile(TileToEdgeAssociation tileToEdgeAssociation){
		Tile tile = tileToEdgeAssociation.getTile();
		List<Edge> occupiedEdges = tileToEdgeAssociation.getAssociatedEdges();

		List<Tile> processedTiles = new ArrayList<>(); processedTiles.add(tile);
		List<TileToEdgeAssociation> out = new ArrayList<>();
		out.add(new TileToEdgeAssociation(tile, occupiedEdges));
		boolean searchUncomplete=false;

		while(true){
			for(int i=0;i<out.size();i++){
				List<TileToEdgeAssociation> newTileToEdgeAssoc = findAdjacentConnectedCityTile(out.get(i));

				for(int j=0;j<newTileToEdgeAssoc.size();j++){
					if(!processedTiles.contains(newTileToEdgeAssoc.get(j).getTile())){
						processedTiles.add(newTileToEdgeAssoc.get(j).getTile());
						out.add(newTileToEdgeAssoc.get(j));
						searchUncomplete = true;
					}else{
						//PATCH 10/21
						//update the tile to edge assoc
						int index = processedTiles.indexOf(newTileToEdgeAssoc.get(j).getTile());
						for(Edge newEdge:newTileToEdgeAssoc.get(j).getAssociatedEdges()){
							if(!out.get(index).getAssociatedEdges().contains(newEdge)){
								out.get(index).getAssociatedEdges().add(newEdge);
							}
						}
					}
				}
			}
			if(!searchUncomplete){
				return out;
			}
			searchUncomplete = false;
		}
	}

	/**
	 * Find the list of tile to edge associations that contains and only contains the whole road feature, either complete
	 * or uncomplete, and specified by the starting of the passed in tile and pathed from the passed in edges of such tile;
	 * The returned list should contain the passed in tile;
	 * Definition of connected see game rules;
	 *
	 * @param tileToEdgeAssociation the tile starting from and which edges to start with
	 * @return the list of tile to edge associations that contains and only contains the whole road feature, either complete
	 * 	or uncomplete, and specified by the starting of the passed in tile and pathed from the passed in edges of such tile;
	 */
	private List<TileToEdgeAssociation> getRoadFeatureFromStartingTile(TileToEdgeAssociation tileToEdgeAssociation){
		Tile tile = tileToEdgeAssociation.getTile();
		List<Edge> occupiedEdges = tileToEdgeAssociation.getAssociatedEdges();

		List<Tile> processedTiles = new ArrayList<>(); processedTiles.add(tile);
		List<TileToEdgeAssociation> out = new ArrayList<>();
		out.add(new TileToEdgeAssociation(tile, occupiedEdges));
		boolean searchUncomplete=false;

		while(true){
			for(int i=0;i<out.size();i++){
				List<TileToEdgeAssociation> newTileToEdgeAssoc = findAdjacentConnectedRoadTile(out.get(i));

				for(int j=0;j<newTileToEdgeAssoc.size();j++){
					if(!processedTiles.contains(newTileToEdgeAssoc.get(j).getTile())){
						processedTiles.add(newTileToEdgeAssoc.get(j).getTile());
						out.add(newTileToEdgeAssoc.get(j));
						searchUncomplete = true;
					}else{
						//PATCH 10/21
						//update the tile to edge assoc
						int index = processedTiles.indexOf(newTileToEdgeAssoc.get(j).getTile());
						for(Edge newEdge:newTileToEdgeAssoc.get(j).getAssociatedEdges()){
							if(!out.get(index).getAssociatedEdges().contains(newEdge)){
								//perform add
								Tile tmpTile = out.get(index).getTile();
								List<Edge> tmpEList = new ArrayList<>();
								for(Edge ee:out.get(index).getAssociatedEdges()){
									tmpEList.add(ee);
								}
								out.set(index, new TileToEdgeAssociation(tmpTile, tmpEList));
								//out.get(index).getAssociatedEdges().add(newEdge);
							}
						}
					}
				}
			}
			if(!searchUncomplete){
				return out;
			}
			searchUncomplete = false;
		}
	}

	/*checks for linked features
	 * this is not like scoring, e.g.
	 * Monastery do not have any linked features
	 * in theory, never should we call any getFeature with CENTER argument, there are safeguards though to process it*/

	/**
	 * Get the connect feature, either complete or uncomplete, and specified by the starting of the passed in tile and pathed,
	 * from the passed in edges of such tile;
	 * The tile specified is included in the list of tileToEdgeAssociations returned;
	 * Note that for Monasteries, it is not connected to anything, so empty list; (the tile itself is not connected either and
	 * therefore is not included either);
	 * For cases where the starting edge resembles fields, null is returned;
	 *
	 * @param startingTile starting tile to check for feature;
	 * @param startingEdge starting edge to check for feature;
	 * @returnthe connect feature, either complete or uncomplete, and specified by the starting of the passed in tile and pathed,
	 * from the passed in edges of such tile; Note that for Monasteries, it is not connected to anything, so empty list;
	 * (the tile itself is not connected either and
	 * 	  therefore is not included either);
	 * 	 For cases where the starting edge resembles fields, null is returned;
	 */
	List<TileToEdgeAssociation> getConnectedFeatureStartingFrom(Tile startingTile, Edge startingEdge){
		EdgeSegment occupiedFeature = startingTile.getEdgeSegment(startingEdge);
		if(occupiedFeature==EdgeSegment.MONASTERY){
			return new ArrayList<>();//monastery is connected to nothing
		}else if(occupiedFeature==EdgeSegment.ROAD_END){
			return getRoadFeatureFromStartingTile(new TileToEdgeAssociation(startingTile, List.of(startingEdge)));
		}else if(occupiedFeature==EdgeSegment.ROAD_CONNECT){
			return getRoadFeatureFromStartingTile(new TileToEdgeAssociation(startingTile, startingTile.findRoadConnect()));
		}else if(occupiedFeature==EdgeSegment.CITY_CONNECT){
			return getCityFeatureFromStartingTile(new TileToEdgeAssociation(startingTile, List.of(startingEdge)));
		}else if(occupiedFeature==EdgeSegment.CITY||occupiedFeature==EdgeSegment.CITY_GUARDED){
			return getCityFeatureFromStartingTile(new TileToEdgeAssociation(startingTile, startingTile.findCityConnect()));
		}else{
			return null;
		}
	}

	/**
	 * check if the city specified by the list of tileToEdgeAssociation is a complete city feature;
	 * note that the argument passed in must be a valid feature list generated by getConnectedFeatureStartingFrom()
	 * for this method to work as intended; otherwise exceptions may occur;
	 * @param tileToEdgeAssociationList the list of tileToEdgeAssociation showing an either complete or uncomplete city feature
	 * @return if the city feature is complete
	 */
	boolean checkCityCompleted(List<TileToEdgeAssociation> tileToEdgeAssociationList){
		//check all edges of the tileList with edge reference
		//nothing that has a city connect edge should be next to null

		List<Tile> tileList = new ArrayList<>();
		List<List<Edge>> edgeListREF = new ArrayList<>();

		for(TileToEdgeAssociation t2e:tileToEdgeAssociationList){
			tileList.add(t2e.getTile());
			edgeListREF.add(t2e.getAssociatedEdges());
		}

		for(int i=0;i<tileList.size();i++){
			for(Edge edge:edgeListREF.get(i)){
				//loop through all
				//special case if edge is center
				if(edge==Edge.CENTER){
					for(Edge edgeC:tileList.get(i).findCityConnect()){
						if(tileList.get(i).getEdgeSegment(edgeC)==EdgeSegment.CITY_CONNECT&&
								getTileInDirection(tileList.get(i),edgeC)==null){
							//means not complete
							return false;
						}
					}
					continue;
				}
				//normal case for every edge
				if(tileList.get(i).getEdgeSegment(edge)==EdgeSegment.CITY_CONNECT&&
						getTileInDirection(tileList.get(i),edge)==null){
					//means not complete
					return false;
				}
			}
		}
		//every cityconnect edge is complete
		return true;
	}

	/**
	 * check if the road specified by the list of tileToEdgeAssociation is a complete city feature;
	 * note that the argument passed in must be a valid feature list generated by getConnectedFeatureStartingFrom()
	 * for this method to work as intended; otherwise exceptions may occur;
	 * @param tileToEdgeAssociationList the list of tileToEdgeAssociation showing an either complete or uncomplete road feature
	 * @return if the road feature is complete
	 */
	boolean checkRoadCompleted(List<TileToEdgeAssociation> tileToEdgeAssociationList){
		//check all edges of the tileList with edge reference
		//nothing that has a road end or connect edge should be next to null

		List<Tile> tileList = new ArrayList<>();
		List<List<Edge>> edgeListREF = new ArrayList<>();

		for(TileToEdgeAssociation t2e:tileToEdgeAssociationList){
			tileList.add(t2e.getTile());
			edgeListREF.add(t2e.getAssociatedEdges());
		}

		for(int i=0;i<tileList.size();i++){
			for(Edge edge:edgeListREF.get(i)){
				//loop through all road connect edges
				if((tileList.get(i).getEdgeSegment(edge)==EdgeSegment.ROAD_CONNECT||
						tileList.get(i).getEdgeSegment(edge)==EdgeSegment.ROAD_END)&&
						getTileInDirection(tileList.get(i),edge)==null){
					//means not complete
					return false;
				}
			}
		}
		//every connection edge for road is complete
		return true;
	}

	/**
	 *
	 * @return print the board
	 */
	@Override
	public String toString() {
		String out="";
		for (Map.Entry<Position, Tile> entry : tileMap.entrySet()) {
			out+=(entry.getKey() + ":" + entry.getValue().toString()+"\n");
		}
		return "=====Board=====\n"+out;
	}
}
