package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The representation of the Deck in the game;
 * This deck is a self shuffling container that contains all the card in the game
 * at the time of initiation;
 * The top tile can be specified to accommodate for a specific
 * Each tile popped is random by default; */
class Deck {
	private List<Tile> deckTiles;
	//default deck composition, each entry relating to the in place tile in the .config file
	private static final int []DEFAULT_DECK_COMPOSITE = {2,4,1,4,5,2,1,3,2,3,3,3,2,3,2,3,1,3,2,1,8,9,4,1};
	private static final TileType DEFAULT_STARTING_TILE = TileType.TILE_D;

	/**
	 * Constructor for the deck, with the top element being tile U;
	 */
	Deck(){
		deckTiles = new ArrayList<>();
		for(int i=0;i<TileType.values().length;i++){ //each tile
			for(int j=0;j<DEFAULT_DECK_COMPOSITE[i]+((i==DEFAULT_STARTING_TILE.ordinal())?-1:0);
			    j++){ //assigned number
				deckTiles.add(new Tile(TileType.values()[i]));
			}
		}
		shuffle();
		deckTiles.add(0,new Tile(DEFAULT_STARTING_TILE));
	}

	/**
	 * Constructor for the deck, with the top element being tile with the starttype;
	 * @param startType specified start type for the deck
	 */
	Deck(TileType startType){
		deckTiles = new ArrayList<>();
		for(int i=0;i<TileType.values().length;i++){ //each tile
			for(int j=0;j<DEFAULT_DECK_COMPOSITE[i]+((i==startType.ordinal())?-1:0);
			    j++){ //assigned number
				deckTiles.add(new Tile(TileType.values()[i]));
			}
		}
		shuffle();
		deckTiles.add(0,new Tile(startType));
	}

	/**
	 * randomize the deck
	 */
	private void shuffle(){
		Collections.shuffle(deckTiles);
	}

	/**
	 * remove the top element of the deck and return it
	 * @return top element of the deck
	 */
	Tile pop(){
		if(deckTiles.isEmpty()){
			return null;
		}
		return deckTiles.remove(0);
	}

	/**
	 *
	 * @return override to string
	 */
	@Override
	public String toString() {
		String tmp = "";
		for(Tile tile:deckTiles){
			tmp+=tile+"\n";
		}
		return "=====DECK=====\n" +
				//"Shuffled Yet: " + String.valueOf(deckShuffled) + "\n" +
				"Remaining Tiles: " + deckTiles.size() + "\n" +
				 tmp+"\n";
	}

}
