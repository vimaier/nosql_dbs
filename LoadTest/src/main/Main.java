package main;

import loadTest.LoadTest;
import de.graphdb.de.Neo4jDB;
import de.graphdb.de.OrientDB;
import de.graphdb.de.TinkerpopDB;

public class Main {

	public static void main(String[] args) 
	{
		//Erstelle Datenbank-Objekte
		TinkerpopDB graphDB = new TinkerpopDB();
		//Neo4jDB graphDB = new Neo4jDB();
		//OrientDB graphDB = new OrientDB();
		
		//Erstelle LoadTest-Objekt
		LoadTest loadTest = new LoadTest(graphDB);
		
		//führe Lesetest durch
		loadTest.performReadTest(1000);
		
		//gibt Report auf der Konsole aus
		loadTest.printReport();
		
		//fügt Report einer CSV Datei hinzu
		loadTest.reportToCsv("/home/cls/workspaces/eclipsejee/"
				+ "nosql_dbs/LoadTest/src/reports/"
				+ graphDB.getDatabaseName().toLowerCase() 
				+ "-" + loadTest.getAction()
				//+ "-" + System.currentTimeMillis() 
				+ ".txt");
		
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
