package edu.cmu.cs.cs214.hw4.core;

import org.junit.*;

import static org.junit.Assert.*;

public class BoardTesting {

	@Before
	public void setUp(){
	}

	@Test
	public void testDeck(){
		//this is a observational test
		//every run should be different due to shuffling
		Deck deck = new Deck();
		System.out.println(deck);
		System.out.println(deck.pop());
		System.out.println(deck);
		System.out.println(deck.pop());
		System.out.println(deck);
		System.out.println(deck.pop());
		System.out.println(deck);
		System.out.println(deck.pop());
		System.out.println(deck);
		System.out.println(deck.pop());
		System.out.println(deck);
		System.out.println(deck.pop());
		System.out.println(deck);
		//annotated deck testing
		deck = new Deck(TileType.TILE_A);
		System.out.println(deck);
		assertTrue(deck.pop().getTileType()==TileType.TILE_A);
		System.out.println(deck);
		System.out.println(deck.pop());
		System.out.println(deck);
		System.out.println(deck.pop());
		System.out.println(deck);
		System.out.println(deck.pop());
		System.out.println(deck);
		System.out.println(deck.pop());
		System.out.println(deck);
		System.out.println(deck.pop());
		System.out.println(deck);
		//stress test popping
		Deck deck2 = new Deck();
		for(int i=0;i<72;i++){
			assertNotNull(deck2.pop());
		}
		assertNull(deck2.pop());
		System.out.println(deck);
	}

	@Test
	public void testBasicBoardFunction(){
		//test basic functionalities
		Tile tileD1 = new Tile(TileType.TILE_D);
		Tile tileD2 = new Tile(TileType.TILE_D);
		Tile tileD3 = new Tile(TileType.TILE_D);
		Tile tileD4 = new Tile(TileType.TILE_D);
		Board board = new Board();
		System.out.println(board.getPlaceablePositions());
		//test for empty board conditions
		assertTrue(board.getPlaceablePositions().size()==1);
		assertEquals(board.getPlaceablePositions().get(0),(new Position(0,0)));
		System.out.println(board.placeTile(tileD1, new Position(0, 0)));
		System.out.println(board.getPlaceablePositions()); //essential print out
		assertTrue(board.getPlaceablePositions().size()==4);
		assertTrue(board.getPlaceablePositions().contains(new Position(0, 1)));
		assertTrue(board.getPlaceablePositions().contains(new Position(1, 0)));
		assertTrue(board.getPlaceablePositions().contains(new Position(-1, 0)));
		assertTrue(board.getPlaceablePositions().contains(new Position(0, -1)));
		assertNull(board.placeTile(tileD2, new Position(0, 0)));//cannot place
		assertNull(board.placeTile(tileD2, new Position(1, 1)));
		assertNotNull(board.placeTile(tileD2, new Position(0, 1)));
		System.out.println(board);
		System.out.println(board.unfinalizedTile());
		assertNotNull(board.unfinalizedTile());
		System.out.println(board.testTile(tileD3, new Position(0, -1)));//essential print out
		System.out.println(board.placeTile(tileD3, new Position(0, -1)));//essential print out
		System.out.println(board);
		assertFalse(tileD2.rotateTile(Edge.SOUTH));
		assertNull(board.placeTile(tileD3, new Position(0, -1)));
		System.out.println(board);
		System.out.println(board.getPlaceablePositions());
		board.finalizeLast();
		//test backend placement violations
		System.out.println(board.testTile(tileD4, new Position(1,0)));
		System.out.println(board.placeTile(tileD4, new Position(1,0)));
		System.out.println(board);
		assertFalse(tileD2.rotateTile(Edge.NORTH));
		System.out.println(board);
		assertTrue(tileD4.rotateTile(Edge.SOUTH));
		System.out.println(board);
		assertTrue(board.finalizeLast());
		System.out.println(board);
		assertNull(board.placeTile(new Tile(TileType.TILE_E), new Position(0, -1)));
		assertNull(board.testTile(new Tile(TileType.TILE_E), new Position(0, -1)));
	}

	@Test
	public void testBoardRelativity_Simple(){
		Tile tileD1 = new Tile(TileType.TILE_D);
		Tile tileD2 = new Tile(TileType.TILE_D);
		Tile tileD3 = new Tile(TileType.TILE_D);
		Tile tileD4 = new Tile(TileType.TILE_D);
		Board board = new Board();
		board.placeTile(tileD1, new Position(0, 0));
		board.placeTile(tileD2, new Position(0, 1));
		board.placeTile(tileD3, new Position(1, 0));
		tileD3.rotateTile(Edge.SOUTH);
		board.placeTile(tileD4, new Position(1, 1));
		tileD4.rotateTile(Edge.SOUTH);
		assertTrue(board.finalizeLast());
		//test adjacent tile analysis
		assertTrue(board.isAdjacentTile(tileD1, tileD2, false));
		assertTrue(board.isAdjacentTile(tileD1, tileD2, true));
		assertFalse(board.isAdjacentTile(tileD1, tileD4, false));
		assertTrue(board.isAdjacentTile(tileD1, tileD4, true));
		assertFalse(board.isAdjacentTile(new Tile(TileType.TILE_E), tileD2, true));
		System.out.println(board.getAdjacentTiles(new Position(0, 0), false));
		assertTrue(board.getAdjacentTiles(new Position(0, 0), false).size()==2);
		System.out.println(board.getAdjacentTiles(new Position(0, 0), true));
		assertTrue(board.getAdjacentTiles(new Position(0, 0), true).size()==4);
		System.out.println(board.getAdjacentTiles(tileD1, false,false));
		assertTrue(board.getAdjacentTiles(tileD1, false,false).size()==2);
		System.out.println(board.getAdjacentTiles(tileD1, false,true));
		assertTrue(board.getAdjacentTiles(tileD1, false,true).size()==8);
		System.out.println(board.getAdjacentTiles(tileD1, true,false));
		assertTrue(board.getAdjacentTiles(tileD1, true,false).size()==3);
		System.out.println(board.getAdjacentTiles(tileD1, true,true));
		assertTrue(board.getAdjacentTiles(tileD1, true,true).size()==8);

		//test tile getters at board level
		assertNotNull(board.getTileInDirection(tileD1, Edge.NORTH));
		assertNotNull(board.getTileInDirection(tileD2, Edge.EAST));
		assertNotNull(board.getTileInDirection(tileD4, Edge.WEST));
		assertNotNull(board.getTileInDirection(tileD4, Edge.SOUTH));
		assertNull(board.getTileInDirection(tileD2, Edge.NORTH));
		assertNull(board.getTileInDirection(tileD2, Edge.CENTER));
		assertNotNull(board.getTileInDirection(tileD1, Corner.NORTH_EAST));
		assertNotNull(board.getTileInDirection(tileD2, Corner.SOUTH_EAST));
		assertNotNull(board.getTileInDirection(tileD4, Corner.SOUTH_WEST));
		assertNotNull(board.getTileInDirection(tileD3, Corner.NORTH_WEST));
	}

	@Test
	public void testBoardRelativity_FeaturesSimple(){
		Tile tileD1 = new Tile(TileType.TILE_D);
		Tile tileD2 = new Tile(TileType.TILE_D);
		Tile tileD3 = new Tile(TileType.TILE_D);
		Tile tileD4 = new Tile(TileType.TILE_D);
		Board board = new Board();
		board.placeTile(tileD1, new Position(0, 0));
		board.placeTile(tileD2, new Position(0, 1));
		board.placeTile(tileD3, new Position(1, 0));
		tileD3.rotateTile(Edge.SOUTH);
		board.placeTile(tileD4, new Position(1, 1));
		tileD4.rotateTile(Edge.SOUTH);
		assertTrue(board.finalizeLast());
		//test adjacent tile analysis, essential printout, read printout;
		System.out.println(board.getConnectedFeatureStartingFrom(tileD1, Edge.NORTH));
		assertNotNull(board.getConnectedFeatureStartingFrom(tileD1, Edge.NORTH));
		System.out.println(board.getConnectedFeatureStartingFrom(tileD1, Edge.SOUTH));
		assertNotNull(board.getConnectedFeatureStartingFrom(tileD1, Edge.SOUTH));
		System.out.println(board.getConnectedFeatureStartingFrom(tileD1, Edge.EAST));
		assertNotNull(board.getConnectedFeatureStartingFrom(tileD1, Edge.EAST));
		System.out.println(board.getConnectedFeatureStartingFrom(tileD1, Edge.WEST));
		assertNull(board.getConnectedFeatureStartingFrom(tileD1, Edge.WEST));
	}

	@Test
	public void testCase1(){//city exh
		//see picture for board of testcase 1
		//essential printout, can change assert to printout
		Tile tileC1 = new Tile(TileType.TILE_C);
		Tile tileC2 = new Tile(TileType.TILE_G);
		Tile tileC3 = new Tile(TileType.TILE_I);
		Tile tileC4 = new Tile(TileType.TILE_N);
		Tile tileC5 = new Tile(TileType.TILE_H);
		Tile tileC6 = new Tile(TileType.TILE_N);
		Tile tileC7 = new Tile(TileType.TILE_I);
		Board board = new Board();
		System.out.println(board.placeTile(tileC1, new Position(0, 0)));
		System.out.println(board.placeTile(tileC2, new Position(0, 1)));
		System.out.println(board.placeTile(tileC3, new Position(1,1 )));
		System.out.println(board.placeTile(tileC4, new Position(1,0 )));
		System.out.println(board.placeTile(tileC5, new Position(1,-1 )));
		System.out.println(board.placeTile(tileC7, new Position(-1,0 )));
		System.out.println(board.placeTile(tileC6, new Position(-1,-1 )));
		//change this following line to obtain printout, check printout with picture of case1
		System.out.println(board.getConnectedFeatureStartingFrom(tileC1, Edge.CENTER));
		assertTrue(board.getConnectedFeatureStartingFrom(tileC1, Edge.CENTER).size()==5);
		assertTrue(board.getConnectedFeatureStartingFrom(tileC2, Edge.CENTER).size()==5);
		assertTrue(board.getConnectedFeatureStartingFrom(tileC3, Edge.SOUTH).size()==5);
		assertTrue(board.getConnectedFeatureStartingFrom(tileC3, Edge.EAST).size()==1);
		assertTrue(board.getConnectedFeatureStartingFrom(tileC5, Edge.EAST).size()==1);
		assertTrue(board.getConnectedFeatureStartingFrom(tileC5, Edge.WEST).size()==1);
		assertTrue(board.getConnectedFeatureStartingFrom(tileC6, Edge.CENTER).size()==2);
	}

	@Test
	public void testCase2(){//road exh
		//see picture for board of testcase 2
		//essential printout, can change assert to printout
		Tile tileR1 = new Tile(TileType.TILE_U);
		Tile tileR2 = new Tile(TileType.TILE_W);
		Tile tileR3 = new Tile(TileType.TILE_V);
		Tile tileR4 = new Tile(TileType.TILE_D);
		Tile tileR6 = new Tile(TileType.TILE_X);
		Tile tileR5 = new Tile(TileType.TILE_K);
		Board board = new Board();
		System.out.println(board.placeTile(tileR1, new Position(0, 0)));
		System.out.println(board.placeTile(tileR2, new Position(0, 1)));
		System.out.println(board.placeTile(tileR3, new Position(1,1 )));
		System.out.println(board.placeTile(tileR4, new Position(1,0 )));
		System.out.println(board.placeTile(tileR6, new Position(0,-1 )));
		System.out.println(board.placeTile(tileR5, new Position(1,-1 )));
		//change this following line to obtain printout, check printout with picture of case1
		System.out.println(board.getConnectedFeatureStartingFrom(tileR1, Edge.SOUTH));
		assertTrue(board.getConnectedFeatureStartingFrom(tileR1, Edge.SOUTH).size()==3);
		assertNull(board.getConnectedFeatureStartingFrom(tileR2, Edge.NORTH));
		assertTrue(board.getConnectedFeatureStartingFrom(tileR6, Edge.SOUTH).size()==1);
		assertTrue(board.getConnectedFeatureStartingFrom(tileR4, Edge.SOUTH).size()==5);
		assertTrue(board.getConnectedFeatureStartingFrom(tileR3, Edge.SOUTH).size()==5);
		assertTrue(board.getConnectedFeatureStartingFrom(tileR1, Edge.NORTH).size()==3);
		assertTrue(board.getConnectedFeatureStartingFrom(tileR2, Edge.WEST).size()==1);
		assertTrue(board.getConnectedFeatureStartingFrom(tileR2, Edge.EAST).size()==5);
	}

	//no need to test monastery, not included in feature connection system, monasteries are never connected
}
