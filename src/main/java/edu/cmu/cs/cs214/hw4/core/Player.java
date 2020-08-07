package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.List;

/**
 * The type that represent a player;
 * Player have a name, a score, and a list of placed meeples;
 */
public class Player {
	private String name;
	private int score;
	private List<Meeple> placedMeeples;

	/**
	 * Construct a player with the specified name and not meeples placed
	 * @param name the name of the player
	 */
	Player(String name){
		this.name = name;
		score = 0;
		placedMeeples = new ArrayList<>();
	}

	/**
	 * get the name of the player
	 * @return name of the player
	 */
	public String getName(){
		return name;
	}

	/**
	 * get the score of the player
	 * @return score of the player
	 */
	int getScore(){
		return score;
	}

	/**
	 * get the numbers of meeples placed by the player
	 * @return numbers of meeples placed by the player
	 */
	int getMeepleCount(){
		return placedMeeples.size();
	}

	/**
	 * Add a score the player's current score
	 * @param ad score to add;
	 * @return player score after adding;
	 */
	int addScore(int ad){
		score+=ad;
		return score;
	}

	/**
	 * get the list of meeples placed by the player;
	 * @return list of meeples placed by the player
	 */
	List<Meeple> getPlacedMeeples(){
		return placedMeeples;//direct return
	}

	/**
	 * get the meeple placed at a specific tile;
	 * if there is no such meeple return null;
	 * @param tile the specified tile
	 * @return the meeple placed here or null if nothing is placed here
	 */
	Meeple getMeepleAtTile(Tile tile){
		if(tile==null){
			return null;
		}
		for(Meeple meeple:placedMeeples){
			if(tile.getPosition().equals(meeple.getOccupiedPosition())){
				return meeple;
			}
		}
		return null;
	}

	/**
	 * get the meeple placed at a specific position;
	 * if there is no such meeple return null;
	 * @param position the specified position
	 * @return the meeple placed here or null if nothing is placed here
	 */
	Meeple getMeepleAtPosition(Position position){
		for(Meeple meeple:placedMeeples){
			if(position.equals(meeple.getOccupiedPosition())){
				return meeple;
			}
		}
		return null;
	}

	/**
	 * Place a meeple at the specified edge of the specified tile;
	 * If the player has already placed over 7 meeples, this function will
	 * not place a meeple for the player and will return false;
	 * @param tile the specified tile to place the meeple
	 * @param edge the specified edge to place the meeple
	 * @return whether the meeple is placeable; (info only)
	 */
	boolean placeMeeple(Tile tile, Edge edge){
		//invariants enforced by overlord, not here
		if(placedMeeples.size()>=7){
			return false;//too much meeples
		}
		if(getMeepleAtTile(tile)!=null){
			return false;//one meeple at one tile ONLY
		}
		placedMeeples.add(new Meeple(tile, edge));
		return true;
	}

	/**
	 * removed the meeple specified;
	 * note that this meeple is removed by reference, as we do not and
	 * likely will not be able to have a equals methods for the meeples;
	 * The function will return false if there is no meeple to remove or
	 * the meeple does not belong to this player;
	 * @param meeple the specified meeple
	 * @return whether the meeple can be removed (info only)
	 */
	boolean removeMeeple(Meeple meeple){
		if(placedMeeples.size()==0){
			return false;//nothing to remove
		}
		if(placedMeeples.indexOf(meeple)==-1){
			//only m.equals(m) return true, see Meeple.java for more;
			return false;//nothing to remove
		}
		placedMeeples.remove(placedMeeples.indexOf(meeple));
		return true;
	}

	/**
	 * removed the meeple specified from the location specified;
	 * The function will return false if there is no meeple at this position
	 * to remove for the player;
	 * @param position the specified position
	 * @return whether the meeple can be removed (info only)
	 */
	boolean removeMeeple(Position position){
		if(placedMeeples.size()==0){
			return false;//nothing to remove
		}
		if(getMeepleAtPosition(position)==null){
			//only m.equals(m) return true, see Meeple.java for more;
			return false;//nothing to remove
		}
		placedMeeples.remove(getMeepleAtPosition(position));
		return true;
	}

	/**
	 *
	 * @return override to string
	 */
	@Override
	public String toString() {
		String tmp = "";
		for(Meeple meeple:placedMeeples){
			tmp+=meeple+"\n";
		}
		return "Player ## "+getName()+" :: "+getScore()+" with Meeples x"+getMeepleCount()+": \n"+tmp;
	}

}
