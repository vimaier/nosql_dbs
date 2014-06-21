package de.graphdb.de;

import de.graphdb.db.neo4j.Neo4jDaoWithWrappedRest;

public class Neo4jDB extends GraphDB
{
	
	private Neo4jDaoWithWrappedRest neo4jDao = null;
	
	/**
	 * Konstruktor
	 */
	public Neo4jDB()
	{
		this.databaseName = "Neo4j";		
	}

	@Override
	public void connect() {
		//verbinde zur Datenbank und speichere client in this.client
		neo4jDao = new Neo4jDaoWithWrappedRest();
		this.client = neo4jDao;
	}

	@Override
	public void disconnect() {
		neo4jDao = null;
	}
	
	@Override
	public void clearDatabase() {
		
		this.connect();
		
		final String queryToDeleteAllEdges = "MATCH (n)-[r]-() DELETE r";
		final String queryToDeleteAllVertices = "MATCH (n) DELETE n";
		neo4jDao.executeCypherQuery(queryToDeleteAllEdges);
		neo4jDao.executeCypherQuery(queryToDeleteAllVertices);
		
		this.disconnect();
	}	

	@Override
	public void executeQuery(String query) {
		neo4jDao.executeCypherQuery(query);
	}

	@Override
	public void executeReadVertex(int vertexName) {
		//lese Knoten mit property 'name' und Wert vertexName aus der 
		//Datenbank aus 
		final String query = "MATCH (n{name:\"" + vertexName + "\"}) RETURN n";
		neo4jDao.executeCypherQuery(query);
	}

	@Override
	public void executeWriteVertex(int vertexName) {
		//Füge Knoten mit Property 'name' und Wert aus vertexName in die 
		//Datenbank ein
		final String query = "CREATE ({name:\"" + vertexName + "\"})";
		neo4jDao.executeCypherQuery(query);
	}
}
