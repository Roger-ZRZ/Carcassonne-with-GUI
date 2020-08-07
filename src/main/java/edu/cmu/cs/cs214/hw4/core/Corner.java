package edu.cmu.cs.cs214.hw4.core;

/**
 * Corner locations Enums
 * This is the enums for corner location, using standard direction templates;
 *
 * This class should contain all necessary enums and should not be changed;
 *  * Note that this enum is made public for the GUI;
 * */
public enum Corner {
	NORTH_EAST{
		/**
		 * toString override to display more usable names;
		 */
		@Override
		public String toString() {
			return "NorthEast";
		}
	},
	SOUTH_EAST{
		/**
		 * toString override to display more usable names;
		 */
		@Override
		public String toString() {
			return "SouthEast";
		}
	},
	SOUTH_WEST{
		/**
		 * toString override to display more usable names;
		 */
		@Override
		public String toString() {
			return "SouthWest";
		}
	},
	NORTH_WEST{
		/**
		 * toString override to display more usable names;
		 */
		@Override
		public String toString() {
			return "NorthWest";
		}
	};

}
