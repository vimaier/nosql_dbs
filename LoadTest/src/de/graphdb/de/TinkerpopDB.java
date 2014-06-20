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
		//verbinde zur Datenbank und speichere client in this.client
	}
	
	@Override
	public void disconnect() 
	{
		//schließe Datenbankverbindung
	}	

	@Override
	public void executeQuery(String query)
	{
		//Führe Query aus query in Datenbank aus
		
		//mache irgendwas zum Testen...
	    for (long i = 0; i < 10000000l; i++) {}				      	
	}
	
	@Override
	public void executeReadVertex(int vertexName) 
	{
		//lese Knoten mit property 'name' und Wert vertexName aus der 
		//Datenbank aus
	}

	@Override
	public void executeWriteVertex(int vertexName) 
	{
		//Füge Knoten mit Property 'name' und Wert aus vertexName in die 
		//Datenbank ein
	}	


}
