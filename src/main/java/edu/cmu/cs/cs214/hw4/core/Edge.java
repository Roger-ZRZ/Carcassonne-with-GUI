package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Edge locations Enums
 * This is the enums for edge location, using standard direction templates;
 * Note that for the sake of simplicity and uniformity we consider center as a edge as well, as
 * edge can be a feature for the monastery and possibly other features in other expansion packs;
 * This class should contain all necessary enums and should not be changed;
 * Note that this enum is made public for the GUI;
 * */
public enum Edge {
	NORTH{
		/**
		 * toString override to display more usable names;
		 */
		@Override
		public String toString() {
			return "North";
		}
	},
	EAST{
		/**
		 * toString override to display more usable names;
		 */
		@Override
		public String toString() {
			return "East";
		}
	},
	SOUTH{
		/**
		 * toString override to display more usable names;
		 */
		@Override
		public String toString() {
			return "South";
		}
	},
	WEST{
		/**
		 * toString override to display more usable names;
		 */
		@Override
		public String toString() {
			return "West";
		}
	},
	CENTER{
		/**
		 * toString override to display more usable names;
		 */
		@Override
		public String toString() {
			return "Center";
		}
	};

	/**
	 * Get the adjacent corners of a edge, in the form of in order clockwise;
	 * @return the adjacent corners of a edge, in the form of in order clockwise;
	 */
	public List<Corner> getAdjCorners(){
		List<Corner> out = new ArrayList<>();
		if(this==NORTH){
			out.add(Corner.NORTH_WEST);
			out.add(Corner.NORTH_EAST);
		}else if(this==SOUTH){
			out.add(Corner.SOUTH_EAST);
			out.add(Corner.SOUTH_WEST);
		}else if(this==WEST){
			out.add(Corner.SOUTH_WEST);
			out.add(Corner.NORTH_WEST);
		}else if(this==EAST){
			out.add(Corner.NORTH_EAST);
			out.add(Corner.SOUTH_EAST);
		}
		return out;
	}

	/**
	 * Get the adjacent edges of a edge, in the form of in order clockwise;
	 * @return the adjacent edges of a edge, in the form of in order clockwise;
	 */
	public List<Edge> getAdjEdges(){
		List<Edge> out = new ArrayList<>();
		if(this==NORTH){
			out.add(WEST);
			out.add(EAST);
		}else if(this==SOUTH){
			out.add(EAST);
			out.add(WEST);
		}else if(this==WEST){
			out.add(SOUTH);
			out.add(NORTH);
		}else if(this==EAST){
			out.add(NORTH);
			out.add(SOUTH);
		}
		return out;
	}

	/**
	 * Get the opposite edge of a edge;
	 * @return the opposite edge of a edge;
	 */
	public Edge getOppositeEdge(){
		if(this==NORTH){
			return SOUTH;
		}else if(this==SOUTH){
			return NORTH;
		}else if(this==WEST){
			return EAST;
		}else if(this==EAST){
			return WEST;
		}else{
			return CENTER;
		}
	}

}
