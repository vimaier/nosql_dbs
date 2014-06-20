package de.graphdb.de;

public class TinkerpopDB extends GraphDB
{

	/**
	 * Konstruktor
	 */
	public TinkerpopDB()
	{
		this.databaseName = "Tinkerpop, TinkerGraph";
		
		//Setze Pfad zum Text-File mit den Leseperationen
		this.pathToTxtFileReadQueries = "/home/cls/workspaces/eclipsejee/"
				+ "LoadTest/src/de/graphdb/de/tinkerpop_queries.txt";
		
		//setze Pfad zum Text-File mit den Schreiboperationen
		this.pathToTxtFileWriteQueries = "/home/cls/workspaces/eclipsejee/"
				+ "LoadTest/src/de/graphdb/de/tinkerpop_queries.txt";
	}

	@Override
	public void connect() 
	{
		//verbinde zur Datenbank
	}

	@Override
	public void executeQuery(String query)
	{
		//mache irgendwas..
	    for (long i = 0; i < 10000000l; i++) {}				      	
	}

	@Override
	public void disconnect() 
	{
		//schließe Datenbankverbindung
	}
}
