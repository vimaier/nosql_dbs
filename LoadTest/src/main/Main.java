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
		
		//führe Schreibtest durch
		//loadTest.performWriteTest(3);
		
		//führe Lesetest durch
		loadTest.performReadTest(2);	
	}
}
