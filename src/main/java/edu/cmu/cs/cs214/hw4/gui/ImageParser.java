package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.Edge;
import edu.cmu.cs.cs214.hw4.core.TileType;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * This is a class responsible for parsing the image for the tiles;
 * Tile rotation and meeple placement drawing are also done in this class;
 * Note that the meeples are drawn with custom positioning that you will be able
 * to find in the resource folder;
 */
class ImageParser {
	//custom meeple drawing positioning;
	//north east south west center-north center-east center-south center-west;
	private static final String IMAGE_FILE_PATH = "Carcassonne.png";
	private BufferedImage image;
	private static final int TILE_PER_ROW = 6;
	private static final int TILE_PER_COL = 4;
	private static final int TILE_LENGTH = 90;
	private static final double AFFINE_PARAM = 2.0;
	//location of the path file for meeple drawing locations;
	private static final String DEFAULT_PATH = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"TileMeepleDisplay.config";
	private List<String> allLines;

	/**
	 * creates a new image parser
	 * @throws IOException resource file cannot be read
	 */
	ImageParser() throws IOException {
		image = ImageIO.read(new File(IMAGE_FILE_PATH));
		allLines = Files.readAllLines(Paths.get(DEFAULT_PATH));
	}

	/**
	 * Parse the image of the tile with tile type and rotation;
	 * This methods does not show a tile with any meeple placement;
	 * @param tileType the type of the tile
	 * @param edge the current rotation of the tile
	 * @return a bufferimage of the tile with the given tiletype
	 * and given rotation;
	 */
	BufferedImage getTileImage(TileType tileType, Edge edge){
		//get image
		int iX = tileType.ordinal()%TILE_PER_ROW;
		int iY = tileType.ordinal()/TILE_PER_ROW;
		BufferedImage src = image.getSubimage(iX*TILE_LENGTH, iY*TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);
		int n = edge.ordinal(); //times to rotate

		//get rotation
		int weight = src.getWidth();
		int height = src.getHeight();
		AffineTransform at = AffineTransform.getQuadrantRotateInstance(n, weight / AFFINE_PARAM, height / AFFINE_PARAM);
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage dest = new BufferedImage(weight, height, src.getType());
		op.filter(src, dest);

		//return rotated image
		return dest;
	}

	/**
	 * tile image of the tile with or with out meeple
	 * @param tileDisplayInfo tile display information containing all the information
	 *                        needed to draw a tile with rotation and the meeple placement
	 *                        of it;
	 * @param color colors of the meeple that will be placed;
	 * @return bufferimage of the meeple placed tile with rotation;
	 */
	BufferedImage getTileImage(TileDisplayInfo tileDisplayInfo, Color color){
		TileType tileType = tileDisplayInfo.getTileType();
		Edge tileRot = tileDisplayInfo.getRotation();
		List<Edge> meepleEdges = tileDisplayInfo.getMeeplePlacements();
		BufferedImage out = getTileImage(tileType, tileRot);

		for(Edge edge : meepleEdges){
			if(edge==Edge.CENTER){
				String wkgStr = allLines.get(tileType.ordinal());
				int wkgPos = tileRot.ordinal()+4;
				int drawX = Integer.parseInt(wkgStr.split(" ")[wkgPos].substring(0,2));
				int drawY = Integer.parseInt(wkgStr.split(" ")[wkgPos].substring(2));
				out = withCircle(out, color, drawX, drawY, 10);
			}else{
				String wkgStr = allLines.get(tileType.ordinal());
				int wkgPos = edge.ordinal();
				int drawX = Integer.parseInt(wkgStr.split(" ")[wkgPos].substring(0,2));
				int drawY = Integer.parseInt(wkgStr.split(" ")[wkgPos].substring(2));
				out = withCircle(out, color, drawX, drawY, 10);
			}

		}
		return out;

	}

	/**
	 * return a square the size of a tile filled with color;
	 * @param color filler color
	 * @return colored empty square
	 */
	BufferedImage makeColoredEmptySquare(Color color) {
		BufferedImage out = new BufferedImage(90,90,BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D g = (Graphics2D) out.getGraphics();
		g.setColor(color);
		g.fillRect(0,0,90,90);
		g.dispose();

		return out;
	}

	/**
	 * Draw a circle on a existing tile;
	 * Private helper method
	 * @param src src image
	 * @param color color of circle
	 * @param x x pos of the center of the circle
	 * @param y y pos of the center of the circle
	 * @param radius radius of the circle
	 * @return bufferimage of the image with the circle drawn as specified;
	 */
	private static BufferedImage withCircle(BufferedImage src, Color color, int x, int y, int radius) {
		BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());

		Graphics2D g = (Graphics2D) dest.getGraphics();
		g.drawImage(src, 0, 0, null);
		g.setColor(color);
		g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
		g.dispose();

		return dest;
	}
}
