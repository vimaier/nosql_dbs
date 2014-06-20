package de.graphdb.de;

public class OrientDB extends GraphDB 
{

	/**
	 * Konstruktor
	 */
	public OrientDB()
	{
		this.databaseName = "OrientDB";
	}
	
	@Override
	public void connect() {
		//verbinde zur Datenbank und speichere client in this.client
	}

	@Override
	public void disconnect() {
		//schließe Datenbankverbindung
	}
	
	@Override
	public void clearDatabase() {
		//lösche alle Datenbankeinträge		
	}	

	@Override
	public void executeQuery(String query) {
		//Führe Query aus query in Datenbank aus
	}

	@Override
	public void executeReadVertex(int vertexName) {
		//lese Knoten mit property 'name' und Wert vertexName aus aus der 
		//Datenbank aus
	}

	@Override
	public void executeWriteVertex(int vertexName) {
		//Füge Knoten mit Property 'name' und Wert aus vertexName in die 
		//Datenbank ein
	}

}