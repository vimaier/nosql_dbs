package de.graphdb.de;

import java.io.IOException;

import com.tinkerpop.rexster.client.RexProException;
import com.tinkerpop.rexster.client.RexsterClient;
import com.tinkerpop.rexster.client.RexsterClientFactory;

public class TinkerpopDB extends GraphDB
{

	/**
	 * Konstruktor
	 */
	public TinkerpopDB()
	{
		this.databaseName = "Tinkerpop-neo4j2graph";
		
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
		try 
		{
			this.client = RexsterClientFactory.open(
					"192.168.93.128", "neo4jsample");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void disconnect() 
	{
		//schließe Datenbankverbindung
		try 
		{
			((RexsterClient) this.client).close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}	

	@Override
	public void clearDatabase() 
	{		
		try
		{	
			this.connect();
			
			//lösche alle Datenbankeinträge
			((RexsterClient) this.client).execute("g.V.remove()");			
		} 
		catch (RexProException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			this.disconnect();
		}
	}	
	
	@Override
	public void executeQuery(String query)
	{
		try 
		{
			//Führe Query aus query in Datenbank aus
			((RexsterClient) this.client).execute(query);
		} 
		catch (RexProException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}			      	
	}
	
	@Override
	public void executeReadVertex(int vertexName) 
	{
		try 
		{
			//lese Knoten mit property 'name' und Wert vertexName aus der 
			//Datenbank aus
			((RexsterClient) this.client).execute("g.V.has('name', '" + 
					vertexName +"')");
		} 
		catch (RexProException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}			
	}

	@Override
	public void executeWriteVertex(int vertexName) 
	{
		//Füge Knoten mit Property 'name' und Wert aus vertexName in die 
		//Datenbank ein
		try 
		{
			((RexsterClient) this.client).execute("g.addVertex(['name', '" + 
					vertexName +"'])");
		} 
		catch (RexProException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}				
	}
}
