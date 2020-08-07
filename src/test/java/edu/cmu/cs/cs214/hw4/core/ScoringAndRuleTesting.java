package edu.cmu.cs.cs214.hw4.core;

import org.junit.*;
import edu.cmu.cs.cs214.hw4.core.Position;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class ScoringAndRuleTesting {
	@Before
	public void setUp(){
	}

	@Test
	public void scoringCombinedTestCase1(){
		//see picture for board of testcase 1
		//essential printout, can change assert to printout
		Board board = new Board();
		List<Player> playerList = new ArrayList<>();
		for(String string:List.of("PlayerA","PlayerB")){
			playerList.add(new Player(string));
		}
		GameRuleController gameRuleController = new GameRuleController(playerList, board, null);
		Tile tileC1 = new Tile(TileType.TILE_C);
		Tile tileC2 = new Tile(TileType.TILE_G);
		Tile tileC3 = new Tile(TileType.TILE_I);
		Tile tileC4 = new Tile(TileType.TILE_N);
		Tile tileC5 = new Tile(TileType.TILE_H);
		Tile tileC6 = new Tile(TileType.TILE_N);
		Tile tileC7 = new Tile(TileType.TILE_I);
		System.out.println(board.placeTile(tileC1, new Position(0, 0)));
		System.out.println(board.placeTile(tileC2, new Position(0, 1)));
		System.out.println(board.placeTile(tileC3, new Position(1,1 )));
		System.out.println(board.placeTile(tileC4, new Position(1,0 )));
		System.out.println(board.placeTile(tileC5, new Position(1,-1 )));
		System.out.println(board.placeTile(tileC7, new Position(-1,0 )));
		System.out.println(board.placeTile(tileC6, new Position(-1,-1 )));
		board.finalizeLast();
		//do with uncomplete feature
		System.out.println(gameRuleController.scoreUpdate(tileC1));
		assertTrue(gameRuleController.scoreUpdate(tileC1).get(0)==0);
		assertTrue(gameRuleController.scoreUpdate(tileC1).get(1)==0);
		playerList.get(0).placeMeeple(tileC1, Edge.CENTER);
		System.out.println(playerList.get(0));
		System.out.println(gameRuleController.scoreUpdate(tileC1));
		assertTrue(gameRuleController.scoreUpdate(tileC1).get(0)==0);
		assertTrue(gameRuleController.scoreUpdate(tileC1).get(1)==0);
		List<Integer> list = gameRuleController.scoreFinalBoard();
		System.out.println(list);
		assertTrue(list.get(0)==6);
		assertTrue(list.get(1)==0);
		//change to variant 2 of testcase 1 see picture
		Tile tileC8 = new Tile(TileType.TILE_N);
		System.out.println(board.placeTile(tileC8, new Position(0,-1 )));
		tileC8.rotateTile();
		Tile tileC9 = new Tile(TileType.TILE_I);
		System.out.println(board.placeTile(tileC9, new Position(0,2 )));
		assertTrue(board.finalizeLast());
		//redo with complete feature
		playerList.get(0).placeMeeple(tileC1, Edge.CENTER);
		list = gameRuleController.scoreUpdate(tileC1);
		System.out.println(list);
		assertTrue(list.get(0)==18);
		assertTrue(list.get(1)==0);
		assertTrue(playerList.get(0).getMeepleCount()==0);
		playerList.get(0).placeMeeple(tileC2, Edge.CENTER);
		list = gameRuleController.scoreUpdate(tileC1);
		System.out.println(list);
		assertTrue(list.get(0)==18);
		assertTrue(list.get(1)==0);
		assertTrue(playerList.get(0).getMeepleCount()==0);
		playerList.get(0).placeMeeple(tileC7, Edge.EAST);
		list = gameRuleController.scoreUpdate(tileC1);
		System.out.println(list);
		assertTrue(list.get(0)==18);
		assertTrue(list.get(1)==0);
		assertTrue(playerList.get(0).getMeepleCount()==0);
		playerList.get(0).placeMeeple(tileC3, Edge.SOUTH);
		list = gameRuleController.scoreUpdate(tileC1);
		System.out.println(list);
		assertTrue(list.get(0)==18);
		assertTrue(list.get(1)==0);
		assertTrue(playerList.get(0).getMeepleCount()==0);
		playerList.get(0).placeMeeple(tileC8, Edge.CENTER);
		list = gameRuleController.scoreUpdate(tileC1);
		System.out.println(list);
		assertTrue(list.get(0)==18);
		assertTrue(list.get(1)==0);
		assertTrue(playerList.get(0).getMeepleCount()==0);
		playerList.get(0).placeMeeple(tileC8, Edge.CENTER);
		playerList.get(1).placeMeeple(tileC1, Edge.CENTER);
		list = gameRuleController.scoreUpdate(tileC1);
		System.out.println(list);
		assertTrue(list.get(0)==18);
		assertTrue(list.get(1)==18);
		assertTrue(playerList.get(0).getMeepleCount()==0);
		playerList.get(0).placeMeeple(tileC8, Edge.CENTER);
		playerList.get(0).placeMeeple(tileC3, Edge.SOUTH);
		playerList.get(1).placeMeeple(tileC1, Edge.CENTER);
		list = gameRuleController.scoreUpdate(tileC1);
		System.out.println(list);
		assertTrue(list.get(0)==18);
		assertTrue(list.get(1)==0);
		assertTrue(playerList.get(0).getMeepleCount()==0);
		playerList.get(0).placeMeeple(tileC8, Edge.CENTER);
		playerList.get(0).placeMeeple(tileC3, Edge.SOUTH);
		playerList.get(1).placeMeeple(tileC1, Edge.CENTER);
		list = gameRuleController.scoreFinalBoard();
		System.out.println(list);
		assertTrue(list.get(0)==18);
		assertTrue(list.get(1)==0);
		assertTrue(playerList.get(0).getMeepleCount()==0);
	}

	@Test
	public void scoringCombinedTestCase3(){
		//see picture for board of testcase 3 monastery and road surround
		//essential printout, can change assert to printout
		Board board = new Board();
		List<Player> playerList = new ArrayList<>();
		for(String string:List.of("PlayerA","PlayerB")){
			playerList.add(new Player(string));
		}
		GameRuleController gameRuleController = new GameRuleController(playerList, board, null);
		Tile tileC1 = new Tile(TileType.TILE_A);
		Tile tileC2 = new Tile(TileType.TILE_W);
		Tile tileC3 = new Tile(TileType.TILE_V);
		Tile tileC4 = new Tile(TileType.TILE_U);
		Tile tileC5 = new Tile(TileType.TILE_V);
		Tile tileC6 = new Tile(TileType.TILE_U);
		Tile tileC7 = new Tile(TileType.TILE_V);
		Tile tileC8 = new Tile(TileType.TILE_U);
		Tile tileC9 = new Tile(TileType.TILE_V);
		System.out.println(board.placeTile(tileC1, new Position(0, 0)));
		System.out.println(board.placeTile(tileC2, new Position(0, -1)));
		tileC2.rotateTile(Edge.SOUTH);
		System.out.println(board.placeTile(tileC3, new Position(1,-1 )));
		tileC3.rotateTile(Edge.EAST);
		System.out.println(board.placeTile(tileC4, new Position(1,0 )));
		System.out.println(board.placeTile(tileC5, new Position(1,1 )));
		System.out.println(board.placeTile(tileC6, new Position(0,1 )));
		tileC6.rotateTile(Edge.EAST);
		System.out.println(board.placeTile(tileC7, new Position(-1,1 )));
		tileC7.rotateTile(Edge.WEST);
		System.out.println(board.placeTile(tileC8, new Position(-1,0 )));
		System.out.println(board.placeTile(tileC9, new Position(-1,-1 )));
		tileC9.rotateTile(Edge.SOUTH);
		assertTrue(board.finalizeLast());
		List<Integer> list = new ArrayList<>();

		//do with complete feature
		playerList.get(0).placeMeeple(tileC1, Edge.CENTER);
		System.out.println(gameRuleController.getPlaceableMeepleEdges(tileC1,playerList.get(0)));
		list = gameRuleController.scoreUpdate(tileC1);
		System.out.println(list);
		assertTrue(list.get(0)==9);
		assertTrue(list.get(1)==0);
		assertTrue(playerList.get(0).getMeepleCount()==0);

		playerList.get(0).placeMeeple(tileC1, Edge.SOUTH);
		list = gameRuleController.scoreUpdate(tileC1);
		System.out.println(list);
		assertTrue(list.get(0)==2);
		assertTrue(list.get(1)==0);
		assertTrue(playerList.get(0).getMeepleCount()==0);

		playerList.get(0).placeMeeple(tileC2, Edge.WEST);
		System.out.println(gameRuleController.getPlaceableMeepleEdges(tileC2,playerList.get(0)));
		list = gameRuleController.scoreUpdate(tileC2);
		System.out.println(list);
		assertTrue(list.get(0)==8);
		assertTrue(list.get(1)==0);
		assertTrue(playerList.get(0).getMeepleCount()==0);

		playerList.get(0).placeMeeple(tileC2, Edge.WEST);
		System.out.println(gameRuleController.getPlaceableMeepleEdges(tileC2,playerList.get(0)));
		System.out.println(gameRuleController.getPlaceableMeepleEdges(tileC2,playerList.get(1)));
		playerList.get(1).placeMeeple(tileC2, Edge.NORTH);
		list = gameRuleController.scoreUpdate(tileC2);
		System.out.println(list);
		assertTrue(list.get(0)==8);
		assertTrue(list.get(1)==2);
		assertTrue(playerList.get(0).getMeepleCount()==0);
		assertTrue(playerList.get(1).getMeepleCount()==0);

		playerList.get(0).placeMeeple(tileC1, Edge.CENTER);
		playerList.get(1).placeMeeple(tileC1, Edge.SOUTH);
		list = gameRuleController.scoreUpdate(tileC1);
		System.out.println(list);
		assertTrue(list.get(0)==9);
		assertTrue(list.get(1)==2);
		assertTrue(playerList.get(0).getMeepleCount()==0);
		assertTrue(playerList.get(1).getMeepleCount()==0);
	}

}
