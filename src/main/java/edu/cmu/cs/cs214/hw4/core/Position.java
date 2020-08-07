package edu.cmu.cs.cs214.hw4.core;

/**
 * A position class that define a position in a 2D board;
 * This class uses an equals method based on contain only;
 * This class is public for later GUI implementations;
 * Positions follow a XY 2D coordinate in MATH;
 */
public class Position {
	private final int x;
	private final int y;

	//hashcode function optimization options
	private static final int OPTIMIZATION_LENGTH = 256;
	private static final int OPTIMIZATION_WIDTH = 10; //hashcode optimized for (-256~255)

	/**
	 * Construct a new position identifier on 2d board
	 * @param x xcoordinate
	 * @param y ycoordinate
	 */
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}

	/**
	 *
	 * @return xcoordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 *
	 * @return ycoordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * check if this position is adjacent to another position;
	 * if includeDiagonal, include diagonal adjacency;
	 * @param other the other position
	 * @param includeDiagonal if include diagonal adjacency
	 * @return boolean value for adjacency
	 */
	public boolean isAdjacent(Position other, boolean includeDiagonal){
		if(other==null){
			return false;
		}
		int otherX = other.getX();
		int otherY = other.getY();
		return (x==otherX+1 && y==otherY) ||
				(x==otherX-1 && y==otherY) ||
				(x==otherX && y==otherY-1) ||
				(x==otherX && y==otherY+1) ||
				(x==otherX+1 && y==otherY+1 && includeDiagonal) ||
				(x==otherX-1 && y==otherY-1 && includeDiagonal) ||
				(x==otherX-1 && y==otherY+1 && includeDiagonal) ||
				(x==otherX+1 && y==otherY-1 && includeDiagonal);
	}

	/**
	 *
	 * @return hashcode
	 */
	@Override
	public int hashCode() {
		return ((x+OPTIMIZATION_LENGTH)<<OPTIMIZATION_WIDTH)+(y+OPTIMIZATION_LENGTH);
	}

	/**
	 * check if the content of this position is equal to the other
	 * @param o  the other object
	 * @return if content is equal
	 */
	@Override
	public boolean equals(Object o) {
		if(o == this){
			return true;//same ref
		}
		if(!(o instanceof Position)){
			return false;
		}
		Position c = (Position) o;
		if(this.x==c.x&&this.y==c.y){
			return true;
		}
		return false;
	}

	/**
	 *
	 * @return override to string
	 */
	@Override
	public String toString() {
		return "(" + String.valueOf(x) + "," +
				String.valueOf(y) + ")";
	}


}
