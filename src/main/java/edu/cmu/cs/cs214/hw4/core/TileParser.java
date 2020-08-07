package edu.cmu.cs.cs214.hw4.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Tile parser unit to parse the info of a tile from a .config file;
 */
class TileParser {
	//Uses Tileinfo.config file;
	//Line Num is the Tile Number, each line should be in format of
	//{North,East,South,West,Center,NorthEast,SouthEast,SouthWest,NorthWest}
	private static final String DEFAULT_PATH = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"TileInfo.config";
	private List<String> allLines;

	/**
	 * Constructor class for the tile parser
	 * @throws IOException file cannot be found
	 */
	TileParser() throws IOException {
		this(DEFAULT_PATH);
	}

	/**
	 * Constructor class for the tile parser
	 * @param filePath filepath for the config file
	 * @throws IOException file cannot be found
	 */
	TileParser(String filePath) throws IOException {
		allLines = Files.readAllLines(Paths.get(filePath));
	}

	/**
	 * get the specified edge segment for the requested tile type
	 * @param tileType the type of the tile;
	 * @param edge the edge specified;
	 * @return the edge segment to return;
	 */
	EdgeSegment getTileEdgeSegment(TileType tileType, Edge edge){
		String wkgStr = allLines.get(tileType.ordinal());
		int wkgPos = edge.ordinal();
		return EdgeSegment.valueOf(wkgStr.split(" ")[wkgPos]);
	}

	/**
	 * get the specified corner segment for the requested tile type
	 * @param tileType the type of the tile;
	 * @param corner the corner specified;
	 * @return the edge segment to return;
	 */
	CornerSegment getTileCornerSegment(TileType tileType, Corner corner){
		String wkgStr = allLines.get(tileType.ordinal());
		int wkgPos = corner.ordinal()+Edge.values().length;
		return CornerSegment.valueOf(wkgStr.split(" ")[wkgPos]);
	}

}
