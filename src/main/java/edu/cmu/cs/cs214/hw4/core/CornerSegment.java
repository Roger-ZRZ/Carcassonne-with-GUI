package edu.cmu.cs.cs214.hw4.core;

/**
 * Enum for corner segment;
 * This is a enum storing all the possible states of a corner of a tile;
 * Please refer to the enum descriptions of each enum for more details;
 *
 * This is not really necessary for the base game, but considering extensions, or if we
 * need to actually score fields, this will be needed;
 * Add to this enum class if need extension;
 * This class is package private;
 */
enum CornerSegment {
	CITY_CORNER{//CORNER FOR DISCONNECTED CITY
		/**
		 * toString override to display more usable names;
		 */
		@Override
		public String toString() {
			return "CITY_CORNER";
		}
	},
	CITY_CROSSING{//CORNER FOR INTERCONNECTED CITY
		/**
		 * toString override to display more usable names;
		 */
		@Override
		public String toString() {
			return "CITY_CROSSING";
		}
	},
	FIELD_CORNER{//CORNER FOR ANYTHING ELSE
		/**
		 * toString override to display more usable names;
		 */
		@Override
		public String toString() {
			return "FIELD_CORNER";
		}
	};
}
