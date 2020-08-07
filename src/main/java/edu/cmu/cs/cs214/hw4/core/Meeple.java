package edu.cmu.cs.cs214.hw4.core;

/**
 * Represents a meeple that is placed on the board;
 * Per representational gap, the meeple should only know the tile and edge
 * it sits on; nothing else;
 * Player own meeples, meeples do not know their owners;
 */
public class Meeple {
	//do NOT use equals() here, will be unintuitive;
	//avoid .indexOf() or .contains() in list calls unless you understand how this works;
	//intentionally made to return true only on m.equals(m);

	private Tile tile;
	private Edge edge;

	/**
	 * construct a meeple on the tile and edge specified
	 * @param tile the tile specified
	 * @param edge the edge specified
	 */
	public Meeple(Tile tile, Edge edge){
		this.tile = tile;
		this.edge = edge;
	}

	/**
	 *
	 * @return the tile occupied
	 */
	public Tile getOccupiedTile(){
		return tile;
	}

	/**
	 *
	 * @return the position occupied
	 */
	public Position getOccupiedPosition(){
		return tile.getPosition();
	}

	/**
	 *
	 * @return the edge occupied
	 */
	public Edge getOccupiedEdge(){
		return edge;
	}

	/**
	 *
	 * @return overrides to string
	 */
	@Override
	public String toString() {
		return "Meeple @ <"+getOccupiedEdge()+"> of Tile:\n" +
				getOccupiedTile();
	}

}
