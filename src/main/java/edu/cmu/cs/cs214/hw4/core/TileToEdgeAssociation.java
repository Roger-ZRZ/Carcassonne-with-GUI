package edu.cmu.cs.cs214.hw4.core;

import java.util.List;

/**
 * This is a data structure that captures an association between a tile and
 * the list of edges;
 * This data structure is mostly used for passing in and out arguments for
 * scoring;
 */
class TileToEdgeAssociation {
	private Tile tile;
	private List<Edge> associatedEdges;

	/**
	 * Construct a {@link TileToEdgeAssociation} with the specified tile and edgeLIst
	 * @param tile specified tile
	 * @param associatedEdges specified list of edges
	 */
	TileToEdgeAssociation(Tile tile, List<Edge> associatedEdges){
		this.tile = tile;
		this.associatedEdges = associatedEdges;
	}

	/**
	 * @return the tile component in the association
	 */
	Tile getTile(){
		return tile;
	}

	/**
	 * @return the edge coomponent in the tile to edge association;
	 */
	List<Edge> getAssociatedEdges(){
		return associatedEdges;
	}

//	void addEdgeToAssoc(Edge edge){
//		if(!associatedEdges.contains(edge)){
//			associatedEdges.add(edge);
//		}
//	}

	/**
	 *
	 * @return override to string
	 */
	@Override
	public String toString() {
		return getTile()+"with Assoc: \n" + getAssociatedEdges() + "\n";
	}
}
