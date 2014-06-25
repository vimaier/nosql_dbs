package de.graphdb.de;

import java.io.IOException;

import org.apache.commons.configuration.BaseConfiguration;

import com.tinkerpop.rexster.client.RexProException;
import com.tinkerpop.rexster.client.RexsterClient;
import com.tinkerpop.rexster.client.RexsterClientFactory;
import com.tinkerpop.rexster.client.RexsterClientTokens;
import com.tinkerpop.rexster.protocol.serializer.msgpack.MsgPackSerializer;

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
			BaseConfiguration configuration = new BaseConfiguration() {{
		        addProperty(RexsterClientTokens
		        		.CONFIG_HOSTNAME, "10.0.107.174");
		        addProperty(RexsterClientTokens
		        		.CONFIG_PORT, 8184);
		        addProperty(RexsterClientTokens
		        		.CONFIG_TIMEOUT_CONNECTION_MS, 999999999);
		        addProperty(RexsterClientTokens
		        		.CONFIG_TIMEOUT_WRITE_MS, 4000);
		        addProperty(RexsterClientTokens
		        		.CONFIG_TIMEOUT_READ_MS, 16000);
		        addProperty(RexsterClientTokens
		        		.CONFIG_MAX_ASYNC_WRITE_QUEUE_BYTES, 5120000);
		        addProperty(RexsterClientTokens
		        		.CONFIG_MESSAGE_RETRY_COUNT, 16);
		        addProperty(RexsterClientTokens
		        		.CONFIG_MESSAGE_RETRY_WAIT_MS, 50);
		        addProperty(RexsterClientTokens
		        		.CONFIG_LANGUAGE, "groovy");
		        addProperty(RexsterClientTokens
		        		.CONFIG_GRAPH_OBJECT_NAME, "g");
		        addProperty(RexsterClientTokens
		        		.CONFIG_GRAPH_NAME, "neo4jsample");
		        addProperty(RexsterClientTokens
		        		.CONFIG_TRANSACTION, true);
		        addProperty(RexsterClientTokens
		        		.CONFIG_SERIALIZER, MsgPackSerializer.SERIALIZER_ID);
		    }};
		    
		    
			this.client = RexsterClientFactory.open("10.0.107.174", "neo4jsample");
		    //this.client = RexsterClientFactory.open(configuration);
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
