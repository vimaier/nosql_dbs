package main;

import loadTest.LoadTest;
import de.graphdb.de.Neo4jDB;
import de.graphdb.de.OrientDB;
import de.graphdb.de.TinkerpopDB;

public class Main {

	public static void main(String[] args) 
	{
		//Erstelle Datenbank-Objekte
		TinkerpopDB tinkerpopDB = new TinkerpopDB();
		Neo4jDB neo4jDB = new Neo4jDB();
		OrientDB orientDB = new OrientDB();
		
		//Erstelle LoadTest-Objekt
		LoadTest loadTest = new LoadTest(tinkerpopDB);
		
		//führe Lesetest durch
		loadTest.performReadTest(1000);
		
		//gibt Report auf der Konsole aus
		loadTest.printReport();
		
		//setze Testdaten zurück
		loadTest.reset();
		
		//führe Schreibtest durch
		loadTest.performWriteTest(1000);
		
		//gibt Report auf der Konsole aus
		loadTest.printReport();		
		
		//setze Testdaten zurück
		loadTest.reset();		
		
	}
}
