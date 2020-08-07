package edu.cmu.cs.cs214.hw4.core;

import org.junit.*;

import static org.junit.Assert.*;

public class PlayerTesting {

	Tile tile1;
	Tile tile2;
	Tile tile3;
	Tile tile4;
	Tile tile5;
	Tile tile6;
	Tile tile7;
	Player player;

	@Before
	public void setUp(){
		tile4 = new Tile(TileType.TILE_F);
		tile5 = new Tile(TileType.TILE_L);
		tile6 = new Tile(TileType.TILE_L);
		tile7 = new Tile(TileType.TILE_L);
		//setup player and tiles
		tile1 = new Tile(TileType.TILE_X);
		tile2 = new Tile(TileType.TILE_G);
		tile3 = new Tile(TileType.TILE_A);
		tile1.putTile(new Position(1, 1));
		tile2.putTile(new Position(-1, 1));
		tile3.putTile(new Position(1, -1));
		String name = ">?>";
		player = new Player(name);
		assertEquals(player.getName(),name);
		assertTrue(player.getScore()==0);
	}

	@Test
	public void testMeepleAccess(){
		//place meeples
		player.placeMeeple(tile1, Edge.NORTH);
		player.placeMeeple(tile2, Edge.NORTH);
		player.placeMeeple(tile3, Edge.NORTH);
		System.out.println(player);
		//test meeple count
		assertTrue(player.getMeepleCount()==3);
		player.removeMeeple(player.getPlacedMeeples().get(1));
		assertTrue(player.getMeepleCount()==2);
		player.placeMeeple(tile2, Edge.NORTH);
		assertTrue(player.getMeepleCount()==3);
		//test get meeple at position
		assertNotNull(player.getMeepleAtPosition(new Position(-1, 1)));
		assertNotNull(player.getMeepleAtPosition(new Position(1, 1)));
		assertNull(player.getMeepleAtPosition(new Position(-1, -1)));
		//test get meeple at tile
		assertNotNull(player.getMeepleAtTile(tile1));
		assertNotNull(player.getMeepleAtTile(tile2));
		assertNull(player.getMeepleAtTile(null));



	}
	@Test
	public void testMeeplePlacementAndRemoval(){
		assertTrue(player.getMeepleCount()==0);
		assertFalse(player.removeMeeple(new Meeple(tile1, Edge.NORTH)));
		assertFalse(player.removeMeeple(new Position(1, 1)));
		//place meeples
		player.placeMeeple(tile1, Edge.NORTH);
		assertFalse(player.removeMeeple(new Meeple(tile1, Edge.SOUTH)));
		player.placeMeeple(tile2, Edge.NORTH);
		player.placeMeeple(tile3, Edge.NORTH);
		System.out.println(player);
		//test meeple count
		assertTrue(player.getMeepleCount()==3);
		player.removeMeeple(player.getPlacedMeeples().get(1));
		assertTrue(player.getMeepleCount()==2);
		player.placeMeeple(tile2, Edge.NORTH);
		assertTrue(player.getMeepleCount()==3);
		//test get meeple at position
		assertNotNull(player.getMeepleAtPosition(new Position(-1, 1)));
		assertNotNull(player.getMeepleAtPosition(new Position(1, 1)));
		assertNull(player.getMeepleAtPosition(new Position(-1, -1)));
		//test get meeple at tile
		assertNotNull(player.getMeepleAtTile(tile1));
		assertNotNull(player.getMeepleAtTile(tile2));
		assertNull(player.getMeepleAtTile(null));
		//test remove meeple
		System.out.println(player.getMeepleCount());
		assertTrue(player.removeMeeple(new Position(1, 1)));
		assertFalse(player.removeMeeple(new Position(1, 1)));
		assertNull(player.getMeepleAtTile(tile1));
		assertTrue(player.getMeepleCount()==2);
		System.out.println(player);
		System.out.println(player.placeMeeple(tile3, Edge.NORTH));
		assertTrue(player.getMeepleCount()==2);
		System.out.println(player.placeMeeple(tile3, Edge.SOUTH));
		assertTrue(player.getMeepleCount()==2);
	}

	@Test
	public void testPlayerScore(){
		assertTrue(player.getScore()==0);
		player.addScore(10);
		assertTrue(player.getScore()==10);

	}

	@Test
	public void testLimit(){
		//setup player and tiles
		tile1 = new Tile(TileType.TILE_X);
		tile2 = new Tile(TileType.TILE_G);
		tile3 = new Tile(TileType.TILE_A);
		tile4 = new Tile(TileType.TILE_F);
		tile5 = new Tile(TileType.TILE_L);
		tile6 = new Tile(TileType.TILE_L);
		tile7 = new Tile(TileType.TILE_L);
		Tile tile8 = new Tile(TileType.TILE_L);
		Tile tile9 = new Tile(TileType.TILE_L);
		tile1.putTile(new Position(1, 1));
		tile2.putTile(new Position(11, 1));
		tile3.putTile(new Position(1, 11));
		tile4.putTile(new Position(111, 1));
		tile5.putTile(new Position(111, 11));
		tile6.putTile(new Position(111, 111));
		tile7.putTile(new Position(1, 111));
		tile8.putTile(new Position(11, 111));
		tile9.putTile(new Position(-1, -1));

		assertTrue(player.getMeepleCount()==0);

		System.out.println(player.placeMeeple(tile1, Edge.NORTH));
		System.out.println(player.placeMeeple(tile2, Edge.NORTH));
		System.out.println(player.placeMeeple(tile3, Edge.NORTH));
		System.out.println(player.placeMeeple(tile4, Edge.NORTH));
		System.out.println(player.placeMeeple(tile5, Edge.NORTH));
		System.out.println(player.placeMeeple(tile6, Edge.NORTH));
		System.out.println(player.placeMeeple(tile7, Edge.NORTH));
		System.out.println(player.placeMeeple(tile8, Edge.NORTH));
		System.out.println(player.placeMeeple(tile9, Edge.NORTH));

		assertTrue(player.getMeepleCount()==7);
	}

	@Test
	public void testMeeple(){
		Tile testTile = new Tile(TileType.TILE_X);
		Meeple m1 = new Meeple(testTile, Edge.NORTH);
		Meeple m2 = new Meeple(testTile, Edge.NORTH);
		Meeple m3 = new Meeple(testTile, Edge.SOUTH);
		assertTrue(m1==m1);
		assertTrue(m1.equals(m1));
		assertFalse(m1==m2);
		assertFalse(m1.equals(m2));
		assertFalse(m1==m3);
		assertFalse(m1.equals(m3));
		System.out.println(m1);
	}

}
