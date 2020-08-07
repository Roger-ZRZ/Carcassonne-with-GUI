package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.List;

/**
 * ==This class was phrased as the scoreController in the design documents==
 * ==This is only a name change==
 *
 * A Controller class that features all the control elements regarding scoring mechanisms and meeple
 * distribution and removal of the board, including:
 * 1. Scoring features and score distribution;
 * 2. Meeple options for players;
 * 3. Meeple removal after scoring features;
 *
 * Most of the functions in this class are designed to be extensible for new expansions;
 * Some functions can be made protected without voiding information hiding, but are not done so;
 *
 * NOTE:
 * There is another way of making each feature as objects, and update these objects as the gameplay
 * proceeds; I choose not to use this design as this would make the scoring process more embedded and
 * coupled, and less likely to be extended and modified for performance;
 * Also, such an approach can have a heavy performance impact, especially if there are many player meeples
 * and many features to constantly maintain;
 * My approach seeks to represent feature as nothing more than a collection of tiles in a stateless format;
 * This gives great flexibility in both general scoring processing and applying specific scoring rules
 * that will give the most optimal performance;
 * In general, although it makes the code slightly more complex, I do believe that the flexibility and
 * performance is worth the trade off;
 */
class GameRuleController {

	//NOTE: These are just REFERENCE COPIES
	private final List<Player> playerList;
	private final Board board;
	private final Deck deck;
	private List<Meeple> meepleRemovedThisRound;

	//numbers tiles that surround a monastery
	private static final int MONASTERY_MAX_EDGE_COUNT = 8;

	/**
	 * Construct a game rule controller class with the following reference arguments;
	 *
	 * NOTE that ALL of the arguments should be passed in as reference;
	 * The reference will be stored;
	 * Changes in the objects should be reflected in the controller;
	 * Reference shedding is not allowed when using the game rule controller as it will
	 * render the controller detached from the game;
	 * @param playerListREF reference of the player list;
	 * @param boardREF reference of the board
	 * @param deckREF reference of the deck
	 */
	GameRuleController(List<Player> playerListREF, Board boardREF, Deck deckREF){
		this.playerList = playerListREF;
		this.board = boardREF;
		this.deck = deckREF;
		meepleRemovedThisRound = new ArrayList<>();
	}

	/**
	 * Update the player score according to the recently changed tile on the board;
	 *
	 * NOTE:
	 * This methods should only be called AFTER the player has placed his meeple;
	 * This methods should be called everytime after the player places his meeple;
	 * In the case of the last round of the game, this methods should be called before
	 * the scoreFinalBoard() method is called; (although not doing so will likely still yield
	 * the correct scoring but will make the scoring slower)
	 * @param tile the most recently changed tile
	 * @return the list of integers representing how much score each player should receive
	 * (this return value only serves as information, all changes are done in the methods already)
	 */
	List<Integer> scoreUpdate(Tile tile){
		meepleRemovedThisRound.clear();
		List<Integer> updateList= updateBoardFromTile(tile, true);
		for(int i=0;i<playerList.size();i++){
			playerList.get(i).addScore(updateList.get(i));
		}
		return updateList;
	}

	/**
	 * Update the player scores for the final board at the end of the game;
	 * @return the list of integers representing how much score each player should receive
	 * (this return value only serves as information, all changes are done in the methods already)
	 */
	List<Integer> scoreFinalBoard(){
		meepleRemovedThisRound.clear();
		List<Integer> updateList= updateBoardEndGame();
		for(int i=0;i<playerList.size();i++){
			playerList.get(i).addScore(updateList.get(i));
		}
		return updateList;
	}

	/**
	 * Return the meeples that was removed this round;
	 * This method should be called only after the scoring is done;
	 * This information is kept until the next scoring event at the end
	 * of this next turn;
	 * This methods is not intended for serious use;
	 * DO NOT RELY ON THIS FEATURE;
	 * @return meeples removed this round;
	 */
	List<Meeple> getMeepleRemovedThisRound(){
		return meepleRemovedThisRound;
	}

	/**
	 * Score the final board at the end of the game;
	 * Player scores are not changed in this method
	 * @return the list of integers representing how much score each player should receive
	 */
	private List<Integer> updateBoardEndGame(){
		List<Tile> tileList = new ArrayList<>();
		List<Integer> playerScores = new ArrayList<>();
		for(Player player:playerList){
			playerScores.add(0);
		}
		for(Player player:playerList){
			for(Meeple meeple:player.getPlacedMeeples()){
				tileList.add(meeple.getOccupiedTile());
			}
		}
		for(Tile tile:tileList){
			playerScores = mergeScoreList(updateBoardFromTile(tile,false),playerScores);
		}
		//ah, retirement;
		return playerScores;
	}

	/**
	 * score the changes in the board according to the recently changed tile on the board;
	 * Player scores are not changed in this method
	 *
	 * NOTE:
	 * This methods should only be called AFTER the player has placed his meeple;
	 * This methods should be called everytime after the player places his meeple;
	 * In the case of the last round of the game, this methods should be called before
	 * the scoreFinalBoard() method is called; (although not doing so will likely still yield
	 * the correct scoring but will make the scoring slower)
	 * @param tile the most recently changed tile
	 * @return the list of integers representing how much score each player should receive
	 */
	private List<Integer> updateBoardFromTile(Tile tile, boolean forceComplete){
		//with meeple removal
		//need to distinguish features
		List<Integer> playerScores = new ArrayList<>();
		for(Player player:playerList){
			playerScores.add(0);
		}
		//score center first
		if(tile.getEdgeSegment(Edge.CENTER)!=EdgeSegment.FIELD){
			if(tile.getEdgeSegment(Edge.CENTER)==EdgeSegment.MONASTERY){
				playerScores = mergeScoreList(scoreMonasteryFeature(tile, forceComplete),playerScores);
			}else{//must be city_guarded or city
				playerScores = mergeScoreList(scoreCityFeature(tile, Edge.CENTER, forceComplete),playerScores);
			}
		}
		//score edges
		for(Edge edge:List.of(Edge.NORTH,Edge.EAST,Edge.SOUTH,Edge.WEST)){
			if(tile.getEdgeSegment(edge)==EdgeSegment.ROAD_CONNECT ||
					tile.getEdgeSegment(edge)==EdgeSegment.ROAD_END ){
				//score road
				playerScores = mergeScoreList(scoreRoadFeature(tile, edge, forceComplete),playerScores);
			}else if(tile.getEdgeSegment(edge)==EdgeSegment.CITY_CONNECT&&
					tile.getEdgeSegment(Edge.CENTER)!=EdgeSegment.CITY&&
					tile.getEdgeSegment(Edge.CENTER)!=EdgeSegment.CITY_GUARDED) {
				playerScores = mergeScoreList(scoreCityFeature(tile, edge, forceComplete),playerScores);
			}
		}

		return  playerScores;
	}

	/**
	 * Score the monastery features either complete or uncomplete, and specified by the starting of the passed in tile;
	 * 	If we enforce feature completeness, all features that are not complete will receive a score of 0;
	 * 	If we do not make such enforcements. all features will be scored automatically base on their completeness;
	 *
	 * 	NOTE that the scores are NOT directly distributed to the player;
	 * 	NOTE that the meeples are removed directly in this method;
	 *
	 * @param tile the specified tile
	 * @param forceComplete whether we enforce that the feature must be complete for scoring
	 * @return the list of score that each player should receive;
	 */
	private List<Integer> scoreMonasteryFeature(Tile tile, boolean forceComplete){

		List<Integer> playerScores = new ArrayList<>();
		for(Player player:playerList){
			playerScores.add(0);
		}
		//scoreMonastary
		int fullscore = scoreCompleteFeature(List.of(new TileToEdgeAssociation(tile,new ArrayList<>())),EdgeSegment.MONASTERY);
		//distribute score
		if(fullscore==0&&(!forceComplete)){
			fullscore = scoreUncompleteFeature(List.of(new TileToEdgeAssociation(tile,new ArrayList<>())),EdgeSegment.MONASTERY);
		}
		if(fullscore!=0){
			for(Player player:playerList){
				if(player.getMeepleAtTile(tile)!=null &&
						player.getMeepleAtTile(tile).getOccupiedEdge()==Edge.CENTER){
					playerScores.set(playerList.indexOf(player),
							playerScores.get(playerList.indexOf(player))+fullscore);
					//remove meeple from monastary
					player.removeMeeple(player.getMeepleAtTile(tile));
				}
			}
		}
		return playerScores;
	}

	/**
	 * Score the city features either complete or uncomplete, and specified by the starting of the passed in tile and pathed,
	 * 	from the passed in edges of such tile;
	 * 	If we enforce feature completeness, all features that are not complete will receive a score of 0;
	 * 	If we do not make such enforcements. all features will be scored automatically base on their completeness;
	 *
	 * 	NOTE that the scores are NOT directly distributed to the player;
	 * 	NOTE that the meeples are removed directly in this method;
	 *
	 * @param tile the specified tile
	 * @param edge the specified edge
	 * @param forceComplete whether we enforce that the feature must be complete for scoring
	 * @return the list of score that each player should receive;
	 */
	private List<Integer> scoreCityFeature(Tile tile, Edge edge, boolean forceComplete){

		List<TileToEdgeAssociation> tileToEdgeAssociationList = board.getConnectedFeatureStartingFrom(tile,edge);
		List<Tile> tileList = new ArrayList<>();
		List<List<Edge>> edgeListREF = new ArrayList<>();

		for(TileToEdgeAssociation t2e:tileToEdgeAssociationList){
			tileList.add(t2e.getTile());
			edgeListREF.add(t2e.getAssociatedEdges());
		}

		int fullscore = scoreCompleteFeature(tileToEdgeAssociationList,EdgeSegment.CITY);
		if(fullscore==0&&(!forceComplete)){
			fullscore = scoreUncompleteFeature(tileToEdgeAssociationList,EdgeSegment.CITY);
		}

		List<Integer> playerMeepleCount = new ArrayList<>();
		List<Meeple> removableMeeples = new ArrayList<>();

		//distribute score
		if(fullscore!=0){
			for(Player player:playerList){
				playerMeepleCount.add(0);
			}
			for(Player player:playerList){
				for(Meeple mp:player.getPlacedMeeples()) {
					//occupies a tile in this feature and has a meeple at connected edges and is on city
					boolean isInFeature = tileList.contains(mp.getOccupiedTile());
					boolean isInEdgeRange = isInFeature&&(edgeListREF.
							get(tileList.indexOf(mp.getOccupiedTile())).contains(mp.getOccupiedEdge())||
							mp.getOccupiedTile().getEdgeSegment(Edge.CENTER)==EdgeSegment.CITY ||
							mp.getOccupiedTile().getEdgeSegment(Edge.CENTER)==EdgeSegment.CITY_GUARDED);
					boolean isOccupyingCity = mp.getOccupiedTile().getEdgeSegment(mp.getOccupiedEdge())==EdgeSegment.CITY ||
							mp.getOccupiedTile().getEdgeSegment(mp.getOccupiedEdge())==EdgeSegment.CITY_GUARDED ||
							mp.getOccupiedTile().getEdgeSegment(mp.getOccupiedEdge())==EdgeSegment.CITY_CONNECT ;
					if(isInEdgeRange&&isOccupyingCity) {
						playerMeepleCount.set(playerList.indexOf(player),
								playerMeepleCount.get(playerList.indexOf(player))+1);
						removableMeeples.add(mp);
					}
				}
			}
		}

		return removeMeeplesAndCalculatePlayerGainedScore(playerMeepleCount,removableMeeples, fullscore);
	}

	/**
	 * Score the road features either complete or uncomplete, and specified by the starting of the passed in tile and pathed,
	 * 	from the passed in edges of such tile;
	 * 	If we enforce feature completeness, all features that are not complete will receive a score of 0;
	 * 	If we do not make such enforcements. all features will be scored automatically base on their completeness;
	 *
	 * 	NOTE that the scores are NOT directly distributed to the player;
	 * 	NOTE that the meeples are removed directly in this method;
	 *
	 * @param tile the specified tile
	 * @param edge the specified edge
	 * @param forceComplete whether we enforce that the feature must be complete for scoring
	 * @return the list of score that each player should receive;
	 */
	private List<Integer> scoreRoadFeature(Tile tile, Edge edge, boolean forceComplete){

		List<TileToEdgeAssociation> tileToEdgeAssociationList = board.getConnectedFeatureStartingFrom(tile,edge);
		List<Tile> tileList = new ArrayList<>();
		List<List<Edge>> edgeListREF = new ArrayList<>();

		for(TileToEdgeAssociation t2e:tileToEdgeAssociationList){
			tileList.add(t2e.getTile());
			edgeListREF.add(t2e.getAssociatedEdges());
		}

		int fullscore = scoreCompleteFeature(tileToEdgeAssociationList,EdgeSegment.ROAD_CONNECT);
		if(fullscore==0&&(!forceComplete)){
			fullscore = scoreUncompleteFeature(tileToEdgeAssociationList,EdgeSegment.ROAD_CONNECT);
		}

		List<Integer> playerMeepleCount = new ArrayList<>();
		List<Meeple> removableMeeples = new ArrayList<>();

		//distribute score
		if(fullscore!=0){
			for(Player player:playerList){
				playerMeepleCount.add(0);
			}
			for(Player player:playerList){
				for(Meeple mp:player.getPlacedMeeples()) {
					//occupies a tile in this feature and has a meeple at connected edges and is on road
					boolean isInFeature = tileList.contains(mp.getOccupiedTile());
					boolean isInEdgeRange = isInFeature&&edgeListREF.
							get(tileList.indexOf(mp.getOccupiedTile())).contains(mp.getOccupiedEdge());
					boolean isOccupyingRoad = mp.getOccupiedTile().getEdgeSegment(mp.getOccupiedEdge())==EdgeSegment.ROAD_END ||
							mp.getOccupiedTile().getEdgeSegment(mp.getOccupiedEdge())==EdgeSegment.ROAD_CONNECT ;
					if(isInEdgeRange&&isOccupyingRoad) {//TODO bug?
						System.out.println(isInEdgeRange);
						playerMeepleCount.set(playerList.indexOf(player),
								playerMeepleCount.get(playerList.indexOf(player))+1);
						removableMeeples.add(mp);
					}
				}
			}
		}

		return removeMeeplesAndCalculatePlayerGainedScore(playerMeepleCount,removableMeeples, fullscore);
	}

	/**
	 * This module exclusively handles the process of determining which player(s) gets the score for the scored feature, base on
	 * the game rules;
	 * Meeples will be removed after the player gained score is calculated;
	 * This methods includes both workflows to decrease unnecessary overhead, and to make sure that the two very related
	 * actions are associated and coupled together;
	 *
	 * NOTE that the scores are NOT directly distributed to the player;
	 * NOTE that the meeples are removed directly in this method;
	 *
	 * @param playerMeepleCount the meeple counts of each player in list format;
	 * @param removableMeeples the list of meeples that will be removed;
	 * @param fullscore the full score that will be distributed;
	 * @return the list of scores that each player should gain from scoring this feature;
	 */
	private List<Integer> removeMeeplesAndCalculatePlayerGainedScore(List<Integer> playerMeepleCount, List<Meeple> removableMeeples, int fullscore){
		//check which player has the most meeples
		List<Integer> playerScores = new ArrayList<>();
		for(Player player:playerList){
			playerScores.add(0);
		}

		if(fullscore==0){
			return playerScores;
		}

		int maxMeeple = 0;
		for(int i=0;i<playerMeepleCount.size();i++){
			if(maxMeeple<=playerMeepleCount.get(i)){
				maxMeeple = playerMeepleCount.get(i);
			}
		}
		//add score to that player
		for(int i=0;i<playerMeepleCount.size();i++){
			if(maxMeeple!=0&&maxMeeple==playerMeepleCount.get(i)){
				//player get score
				playerScores.set(i,	playerScores.get(i)+fullscore);
			}
		}
		//remove all removeable meeples
		while (!removableMeeples.isEmpty()){
			Meeple tmpM = removableMeeples.get(0);
			for(Player player:playerList){
				if(player.removeMeeple(tmpM)){
					break;
				}
			}
			removableMeeples.remove(tmpM);
			//add this meeple to be recorded
			if(!meepleRemovedThisRound.contains(tmpM)){
				meepleRemovedThisRound.add(tmpM);
			}
		}
		return playerScores;
	}

	/**
	 * Calculate the score of a complete feature specified by tileToEdgeAssociationList;
	 * NOTE that this methods is NOT responsible for meeple removal;
	 * NOTE that this module does NOT detect if the feature is truly complete or complete;
	 * It will only adhere to the scoring rubrics of complete features;
	 * Please use the right method, otherwise the scoring will be wrong;
	 * @param tileToEdgeAssociationList the tileToEdgeAssociationList of the specified feature
	 * @param feature can ONLY be road_connect, city, monastery, anything else will cause return 0;
	 * @return the score of the feature, based on the complete feature scoring rubric;
	 */
	private int scoreCompleteFeature(List<TileToEdgeAssociation> tileToEdgeAssociationList, EdgeSegment feature){

		List<Tile> tileList = new ArrayList<>();

		for(TileToEdgeAssociation t2e:tileToEdgeAssociationList){
			tileList.add(t2e.getTile());
		}

		//score only complete features, uncomplete ones return 0
		if(feature==EdgeSegment.CITY){
			//scoring a city, check if complete:
			if(!board.checkCityCompleted(tileToEdgeAssociationList)){
				return 0;
			}
			int out=tileList.size()*2;
			for(Tile tile:tileList){
				if(tile.getEdgeSegment(Edge.CENTER)==EdgeSegment.CITY_GUARDED){
					out+=2;
				}
			}
			return out;
		}
		else if(feature==EdgeSegment.MONASTERY){
			//score a monastary if complete
			//in here, tileList should only have 1 element, and ref we dont care
			if(board.getAdjacentTiles(tileList.get(0),true,false).size()== MONASTERY_MAX_EDGE_COUNT){
				return (MONASTERY_MAX_EDGE_COUNT +1);
			}else{
				return 0;
			}
		}
		else if(feature==EdgeSegment.ROAD_CONNECT){
			//score a road
			if(!board.checkRoadCompleted(tileToEdgeAssociationList)){
				return 0;
			}
			return tileList.size();
		}
		else{
			return 0;
		}
	}

	/**
	 * Calculate the score of a uncomplete feature specified by tileToEdgeAssociationList;
	 * NOTE that this methods is NOT responsible for meeple removal;
	 * NOTE that this module does NOT detect if the feature is truly complete or uncomplete;
	 * It will only adhere to the scoring rubrics of uncomplete features;
	 * Please use the right method, otherwise the scoring will be wrong;
	 * @param tileToEdgeAssociationList the tileToEdgeAssociationList of the specified feature
	 * @param feature can ONLY be road_connect, city, monastery, anything else will cause return 0;
	 * @return the score of the feature, based on the uncomplete feature scoring rubric;
	 */
	private int scoreUncompleteFeature(List<TileToEdgeAssociation> tileToEdgeAssociationList, EdgeSegment feature){

		List<Tile> tileList = new ArrayList<>();

		for(TileToEdgeAssociation t2e:tileToEdgeAssociationList){
			tileList.add(t2e.getTile());
		}

		//score complete and uncomplete features
		if(feature==EdgeSegment.CITY){
			int out=tileList.size()*1;
			for(Tile tile:tileList){
				if(tile.getEdgeSegment(Edge.CENTER)==EdgeSegment.CITY_GUARDED){
					out+=1;
				}
			}
			return out;
		}
		else if(feature==EdgeSegment.MONASTERY){
			return board.getAdjacentTiles(tileList.get(0),true,false).size()+1;
		}
		else if(feature==EdgeSegment.ROAD_CONNECT){
			//score a road
			return tileList.size();
		}
		else{
			return 0;
		}
	}


	/**
	 * All the edges where a player can place a meeple on a given tile;
	 * If a player already has more than 6 meeples on the board, it will not be able to place
	 * any new meeples;
	 * If a player cannot place any meeples on the tile, the list will be empty;
	 * @param tile the specified tile;
	 * @param player the specified player;
	 * @return the list of edges where the player can place a meeple on the tile;
	 */
	protected List<Edge> getPlaceableMeepleEdges(Tile tile, Player player){
		if(player.getMeepleCount()>=7){
			return new ArrayList<>();
		}
		//meeple must be placed on center if there is anything in center
		if(tile.getEdgeSegment(Edge.CENTER)==EdgeSegment.MONASTERY){
			//check if the monastery is complete:
			boolean monasteryComplete = board.getAdjacentTiles(tile.getPosition(), false).size()==MONASTERY_MAX_EDGE_COUNT;
			List<Edge> endRoadEdges = tile.findRoadEnd();
			Edge endRoad;
			if(endRoadEdges.isEmpty()){
				//only monastery
				if(monasteryComplete){
					//cannot place as will be removed immediately
					return new ArrayList<>();
				}else{
					return List.of(Edge.CENTER);
				}
			}else{
				List<Edge> out = new ArrayList<>();
				endRoad = endRoadEdges.get(0);
				if((!board.checkRoadCompleted(board.getConnectedFeatureStartingFrom(tile,endRoad))) &&
						(!isFeatureOccupied(board.getConnectedFeatureStartingFrom(tile,endRoad)))){
					out.add(endRoad);
				}
				if(!monasteryComplete){
					out.add(Edge.CENTER);
				}
				return out;
			}
		}
		List<Edge> out = new ArrayList<>();
		if(tile.getEdgeSegment(Edge.CENTER)==EdgeSegment.CITY||
				tile.getEdgeSegment(Edge.CENTER)==EdgeSegment.CITY_GUARDED){

			List<TileToEdgeAssociation> featuredTilesToEdgeAssoc = board.getConnectedFeatureStartingFrom(tile,Edge.CENTER);
			if((!isFeatureOccupied(featuredTilesToEdgeAssoc))&&(!board.checkCityCompleted(featuredTilesToEdgeAssoc))){
				out.add(Edge.CENTER);
			}
		}
		//center is worked with
		for(Edge e:Edge.values()){
			if(e==Edge.CENTER||tile.getEdgeSegment(e)==EdgeSegment.FIELD_CONNECT||
					(tile.getEdgeSegment(e)==EdgeSegment.CITY_CONNECT &&
							(tile.getEdgeSegment(Edge.CENTER)==EdgeSegment.CITY ||
									tile.getEdgeSegment(Edge.CENTER)==EdgeSegment.CITY_GUARDED))){
				continue;
			}
			List<TileToEdgeAssociation> featuredTilesToEdgeAssoc = board.getConnectedFeatureStartingFrom(tile,e);
			if(tile.getEdgeSegment(e)==EdgeSegment.ROAD_CONNECT||tile.getEdgeSegment(e)==EdgeSegment.ROAD_END){
				if((!isFeatureOccupied(featuredTilesToEdgeAssoc))&&(!board.checkRoadCompleted(featuredTilesToEdgeAssoc))){
					out.add(e);
				}
			}else if(tile.getEdgeSegment(e)==EdgeSegment.CITY_CONNECT){
				if((!isFeatureOccupied(featuredTilesToEdgeAssoc))&&(!board.checkCityCompleted(featuredTilesToEdgeAssoc))){
					out.add(e);
				}
			}
		}
		return out;
	}

	/**
	 * If a feature specified by a tileToEdgeAssociation List is already occupied;
	 * @param tileToEdgeAssociationList the specified tileToEdgeAssociationList;
	 * @return boolean value of if the list is already occupied;
	 */
	private boolean isFeatureOccupied(List<TileToEdgeAssociation> tileToEdgeAssociationList){

		List<Tile> tileList = new ArrayList<>();
		List<List<Edge>> edgeListREF = new ArrayList<>();

		for(TileToEdgeAssociation t2e:tileToEdgeAssociationList){
			tileList.add(t2e.getTile());
			edgeListREF.add(t2e.getAssociatedEdges());
		}

		for(Player p:playerList){
			for(Meeple mp:p.getPlacedMeeples()){
				if(tileList.contains(mp.getOccupiedTile())){
					Edge meepleEdge = mp.getOccupiedEdge();
					List<Edge> includedEdges=edgeListREF.get(tileList.indexOf(mp.getOccupiedTile()));
					if(includedEdges.contains(meepleEdge)) {
						return true;
					}else if(meepleEdge==Edge.CENTER){
						//not monastery, can only be city
						return includedEdges.containsAll(mp.getOccupiedTile().findCityConnect());
					}
				}
			}
		}
		return false;
	}

	/**
	 * Return a list that is the same length as the first and second list were each element is the sum
	 * of the elements of list1 and list2 in the same place;
	 * @param list1 the specified list1
	 * @param list2 the specified list2
	 * @return the returned list that is the same length as the first and second list were each element is the sum
	 * of the elements of list1 and list2 in the same place;
	 */
	private List<Integer> mergeScoreList(List<Integer> list1, List<Integer> list2){
		List<Integer> out = new ArrayList<>();
		if(list1.size()!=list2.size()){
			return null;
		}
		for(int i=0;i<list1.size();i++){
			out.add(list1.get(i)+list2.get(i));
		}
		return out;
	}

}
