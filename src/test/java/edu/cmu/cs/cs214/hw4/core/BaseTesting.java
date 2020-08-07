package edu.cmu.cs.cs214.hw4.core;

import org.junit.*;
import java.io.*;

import static org.junit.Assert.*;

public class BaseTesting {
	Tile tile1;
	Tile tile1_OG;
	Tile tile2;
	Tile tile2_OG;
	Tile tile3;
	Tile tile3_OG;
	Tile tile4;
	Tile tile4_OG;
	Tile tile5;
	Tile tile5_OG;

	@Before
	public void setUp(){
		tile1 = new Tile(TileType.TILE_A);
		tile2 = new Tile(TileType.TILE_B);
		tile3 = new Tile(TileType.TILE_C);
		tile4 = new Tile(TileType.TILE_F);
		tile5 = new Tile(TileType.TILE_L);
		tile1_OG = new Tile(TileType.TILE_A);
		tile2_OG = new Tile(TileType.TILE_B);
		tile3_OG = new Tile(TileType.TILE_C);
		tile4_OG = new Tile(TileType.TILE_F);
		tile5_OG = new Tile(TileType.TILE_L);
		System.out.println(tile1);
		System.out.println(tile2);
		System.out.println(tile3);
		System.out.println(tile4);
		System.out.println(tile5);
		System.out.println(tile5.finalizeTile());
		System.out.println(tile5.getTileType());
		System.out.println(tile5.getRotation());
		assertTrue(tile5.getRotation()==Edge.NORTH);
		assertFalse(tile5.finalizeTile());
		System.out.println(tile1.getEdgeSegment(Edge.NORTH));
		System.out.println(tile1.getEdgeSegment(Edge.SOUTH));
		System.out.println(tile1.getEdgeSegment(Edge.EAST));
		System.out.println(tile1.getEdgeSegment(Edge.WEST));
		System.out.println(tile1.getEdgeSegment(Edge.CENTER));
		System.out.println(tile1.getCornerSegment(Corner.NORTH_EAST));
	}

	@Test(expected=IOException.class)
	public void testParser() throws IOException {
		for(TileType tt:TileType.values()){
			System.out.println(new Tile(tt));
		}
		TileParser tileParser1 = new TileParser("src/main/resources/TileInfo.config");
		TileParser tileParserFail2 = new TileParser("whatever");
	}

	@Test
	public void testInitialState(){
		assertFalse(tile1.isOnBoard());
		assertFalse(tile2.isOnBoard());
		assertFalse(tile3.isOnBoard());
		assertFalse(tile4.isOnBoard());
		assertFalse(tile5.isOnBoard());
		assertFalse(tile1.isFinal());
		assertFalse(tile2.isFinal());
		assertFalse(tile3.isFinal());
		assertFalse(tile4.isFinal());
		assertFalse(tile5.isFinal());
		assertFalse(tile1.rotateTile());
		assertFalse(tile2.rotateTile());
		assertFalse(tile3.rotateTile());
		assertFalse(tile4.rotateTile());
		assertFalse(tile5.rotateTile());
		assertFalse(tile1.rotateTile(Edge.SOUTH));
		assertFalse(tile2.rotateTile(Edge.SOUTH));
		assertFalse(tile3.rotateTile(Edge.SOUTH));
		assertFalse(tile4.rotateTile(Edge.SOUTH));
		assertFalse(tile5.rotateTile(Edge.SOUTH));
		assertNull(tile1.getPosition());
	}

	@Test
	public void testPutTile(){
		assertFalse(tile1.rotateTile());
		assertTrue(tile1.putTile(new Position(10, 11)));
		assertTrue(tile1.rotateTile());
		assertTrue(tile1.rotateTile());
		assertTrue(tile1.getRotation()==Edge.SOUTH);
		assertEquals(tile1.getPosition(), new Position(10, 11));
		assertFalse(tile1.putTile(new Position(21, -123)));
		assertTrue(tile1.rotateTile());
		assertTrue(tile1.getRotation()==Edge.WEST);
		assertTrue(tile1.rotateTile(Edge.WEST));
		assertTrue(tile1.getRotation()==Edge.WEST);
		assertTrue(tile1.finalizeTile());
	}

	@Test
	public void testTileInfoGetters(){
		System.out.println(tile1.findCityConnect());
		System.out.println(tile2.findCityConnect());
		System.out.println(tile3.findCityConnect());
		System.out.println(tile4.findCityConnect());
		System.out.println(tile5.findCityConnect());
		System.out.println(tile1.findRoadConnect());
		System.out.println(tile2.findRoadConnect());
		System.out.println(tile3.findRoadConnect());
		System.out.println(tile4.findRoadConnect());
		System.out.println(tile5.findRoadConnect());
		System.out.println(tile1.findRoadEnd());
		System.out.println(tile2.findRoadEnd());
		System.out.println(tile3.findRoadEnd());
		System.out.println(tile4.findRoadEnd());
		System.out.println(tile5.findRoadEnd());
		tile5 = new Tile(TileType.TILE_U);
		System.out.println(tile5.findRoadConnect());


	}

	@Test
	public void testPos(){
		Position p1 = new Position(-1, -1);
		Position p2 = new Position(0, 1);
		System.out.println(p1);
		System.out.println(p2);
		assertTrue(p1.getX()==-1);
		assertTrue(p1.getY()==-1);
		assertTrue(p2.getX()==0);
		assertTrue(p2.getY()==1);
		assertFalse(p1.isAdjacent(null, true));
		assertFalse(p1.isAdjacent(new Position(10, 10), true));
		assertFalse(p1.isAdjacent(new Position(10, 10), false));
		assertTrue(p1.hashCode()==new Position(-1, -1).hashCode());
		assertTrue(p2.hashCode()==new Position(0, 1).hashCode());
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));
		assertFalse(p2.equals(null));
		assertTrue(p1.equals(p1));
		assertTrue(p2.equals(p2));
		assertTrue(p2.equals(new Position(0, 1)));
	}

	@Test
	public void testTileAssociations(){

	}

	@Test
	public void testMisc(){
		for(Edge edge:Edge.values()){
			System.out.println(edge);
			System.out.println(edge.getAdjEdges());
			System.out.println(edge.getAdjCorners());
			System.out.println(edge.getOppositeEdge());
		}
		for(Corner corner:Corner.values()){
			System.out.println(corner);
		}
		for(EdgeSegment edgeSegment:EdgeSegment.values()){
			System.out.println(edgeSegment);
			for(EdgeSegment edgeSegment2:EdgeSegment.values()){
				System.out.println(edgeSegment.matches(edgeSegment2));
			}
		}
	}
}
