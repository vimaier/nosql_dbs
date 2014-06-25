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
		//int[] numberOfThreads = {1, 5, 10, 20, 50, 100, 200, 300, 500}; //read
		int[] numberOfThreads = {10, 50, 100, 200, 300, 400, 500, 750, 1000}; //write
		final int THREADS = numberOfThreads[8];
		
		//Erstelle Datenbank-Objekte
		TinkerpopDB graphDB = new TinkerpopDB();
		//Neo4jDB graphDB = new Neo4jDB();
		//OrientDB graphDB = new OrientDB();
				
		//Erstelle LoadTest-Objekt
		LoadTest loadTest = new LoadTest(graphDB);
		
		//führe Schreibtest durch
		loadTest.performWriteTest(THREADS);
		
		//führe Lesetest durch
		//loadTest.performReadTest(THREADS);	
	}
}
