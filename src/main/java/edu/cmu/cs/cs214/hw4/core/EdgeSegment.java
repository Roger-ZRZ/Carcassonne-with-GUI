package edu.cmu.cs.cs214.hw4.core;

/**
 * Enum for edge segment;
 * This is a enum storing all the possible states of a edge of a tile;
 * Please refer to the enum descriptions of each enum for more details;
 *
 * Add to this enum class if need extension;
 * This class is package private;
 */
enum EdgeSegment {
	ROAD_CONNECT{//EDGE ASSIGNMENT FOR ROAD CONNECTION
		@Override
		public String toString() {
			return "ROAD_CONNECT";
		}
	},
	ROAD_END{//EDGE ASSIGNMENT FOR ROAD THAT ENDS IN TILE
		@Override
		public String toString() {
			return "ROAD_END";
		}
	},
	CITY_CONNECT{//EDGE ASSIGNMENT FOR CITY CONNECTION
		@Override
		public String toString() {
			return "CITY_CONNECT";
		}
	},
	FIELD_CONNECT{//EDGE ASSIGNMENT FOR ANYTHING ELSE
		@Override
		public String toString() {
			return "FIELD_CONNECT";
		}
	},
	MONASTERY{//CENTER ASSIGNMENT FOR MONASTERY
		@Override
		public String toString() {
			return "MONASTERY";
		}
	},
	CITY{//CENTER ASSIGNMENT FOR CITIES MORE THAN 50% TILE
		@Override
		public String toString() {
			return "CITY";
		}
	},
	CITY_GUARDED{//CENTER ASSIGNMENT FOR GUARDED MORE THAN 50% TILE
		@Override
		public String toString() {
			return "CITY_GUARDED";
		}
	},
	FIELD{//CENTER ASSIGNMENT FOR ANYTHING ELSE
		@Override
		public String toString() {
			return "FIELD";
		}
	};

	/**
	 * Check if this edge segment can be placed adjacent to another edge segment;
	 * @param edgeSegment the other edge segment;
	 * @return boolean value for isplaceable;
	 */
	boolean matches(EdgeSegment edgeSegment){
		if(this==ROAD_CONNECT){
			return edgeSegment==ROAD_CONNECT || edgeSegment==ROAD_END;
		}else if(this==ROAD_END){
			return edgeSegment==ROAD_CONNECT || edgeSegment==ROAD_END;
		}else if(this==CITY_CONNECT){
			return edgeSegment==CITY_CONNECT;
		}else if(this==FIELD_CONNECT){
			return edgeSegment==FIELD_CONNECT;
		}else{
			return false;
		}
	}
}
