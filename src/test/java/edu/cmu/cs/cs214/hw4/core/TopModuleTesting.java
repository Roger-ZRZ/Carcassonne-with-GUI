//package edu.cmu.cs.cs214.hw4.core;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class TopModuleTesting {
//
//	@Before
//	public void setUp(){
//	}
//
//	@Test
//	public void gameplayRandomSim1() throws FileNotFoundException {
//		//run random gameplay simulation analysis
//		//printout, make sure not crashing
//		// (there are self-error checking in the simulation method, will crash with exit(-1) if error encountered)
//		//this is a observational test
//		System.out.println("LOOP 1");
//		Carcassonne carcassonne = new Carcassonne(new ArrayList<>(
//				Arrays.asList("PlayerA","PlayerB","PlayerC","PlayerD")),false,true);
//		System.out.println(carcassonne);
//		//start playing;
//		System.out.println(carcassonne.gameCycleSimulated(100));
//
//		System.out.println("LOOP 2");
//		carcassonne = new Carcassonne(new ArrayList<>(
//				Arrays.asList("PlayerA","PlayerB","PlayerC","PlayerD")),false,true);
//		System.out.println(carcassonne);
//		//start playing;
//		System.out.println(carcassonne.gameCycleSimulated(100));
//
//		System.out.println("LOOP 3");
//		carcassonne = new Carcassonne(new ArrayList<>(
//				Arrays.asList("PlayerA","PlayerB","PlayerC","PlayerD")),false,true);
//		System.out.println(carcassonne);
//		//start playing;
//		System.out.println(carcassonne.gameCycleSimulated(100));
//
//		System.out.println("LOOP 4");
//		carcassonne = new Carcassonne(new ArrayList<>(
//				Arrays.asList("PlayerA","PlayerB","PlayerC","PlayerD")),false,true);
//		System.out.println(carcassonne);
//		//start playing;
//		System.out.println(carcassonne.gameCycleSimulated(100));
//
//		System.out.println("LOOP 5");
//		carcassonne = new Carcassonne(new ArrayList<>(
//				Arrays.asList("PlayerA","PlayerB","PlayerC","PlayerD")),false,true);
//		System.out.println(carcassonne);
//		//start playing;
//		System.out.println(carcassonne.gameCycleSimulated(100));
//
//		System.out.println("LOOP 6");
//		carcassonne = new Carcassonne(new ArrayList<>(
//				Arrays.asList("PlayerA","PlayerB","PlayerC","PlayerD")),true,true);
//		System.out.println(carcassonne);
//		//start playing;
//		System.out.println(carcassonne.gameCycleSimulated(100));
//
//		System.out.println("LOOP 7");
//		carcassonne = new Carcassonne(new ArrayList<>(
//				Arrays.asList("PlayerA","PlayerB","PlayerC","PlayerD")),true,true);
//		System.out.println(carcassonne);
//		//start playing;
//		System.out.println(carcassonne.gameCycleSimulated(100));
//
//		System.out.println("LOOP 8");
//		carcassonne = new Carcassonne(new ArrayList<>(
//				Arrays.asList("PlayerA","PlayerB","PlayerC","PlayerD")),true,true);
//		System.out.println(carcassonne);
//		//start playing;
//		System.out.println(carcassonne.gameCycleSimulated(100));
//	}
//}
