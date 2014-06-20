package main;

import loadTest.LoadTest;
import de.graphdb.de.GraphDB;
import de.graphdb.de.Neo4jDB;
import de.graphdb.de.OrientDB;
import de.graphdb.de.TinkerpopDB;

public class Main {

	public static void main(String[] args) 
	{
		//Anzahl der Threads die getestet werden sollen
		int[] numberOfThreads = {500, 600, 700, 800, 900, 1000};
		final int THREADS = numberOfThreads[0];
		
		//Erstelle Datenbank-Objekte
		TinkerpopDB graphDB = new TinkerpopDB();
		//Neo4jDB graphDB = new Neo4jDB();
		//OrientDB graphDB = new OrientDB();
				
		//Erstelle LoadTest-Objekt
		LoadTest loadTest = new LoadTest(graphDB);
		
		performWriteTest(loadTest, graphDB, THREADS);
		
		//setze Messdaten zurück
		loadTest.reset();
		
		performReadTest(loadTest, graphDB, THREADS);
		
		//setze Messdaten zurück
		loadTest.reset();		
	}
	
	/**
	 * Führe Schreibtest durch.
	 * @param loadTest
	 * @param graphDB
	 * @param threads
	 */
	public static void performWriteTest(
			LoadTest loadTest, 
			GraphDB graphDB,
			int threads)
	{
		//führe Schreibtest durch
		loadTest.performWriteTest(threads);
		
		//gibt Report auf der Konsole aus
		loadTest.printReport();
		
		//fügt Report einer CSV Datei hinzu
		loadTest.reportToCsv("/home/cls/workspaces/eclipsejee/"
				+ "nosql_dbs/LoadTest/src/reports/"
				+ graphDB.getDatabaseName().toLowerCase() 
				+ "-" + loadTest.getAction() + ".txt");
	}
	
	/**
	 * Führe Schreibtest durch.
	 * @param loadTest
	 * @param graphDB
	 * @param threads
	 */
	public static void performReadTest(
			LoadTest loadTest, 
			GraphDB graphDB,
			int threads)
	{
		//führe Lesetest durch
		loadTest.performReadTest(threads);
		
		//gibt Report auf der Konsole aus
		loadTest.printReport();
		
		//fügt Report einer CSV Datei hinzu
		loadTest.reportToCsv("/home/cls/workspaces/eclipsejee/"
				+ "nosql_dbs/LoadTest/src/reports/"
				+ graphDB.getDatabaseName().toLowerCase() 
				+ "-" + loadTest.getAction() + ".txt");
	}
}
