package de.graphdb.de;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public abstract class GraphDB {

	/**
	 * Name der Datenbank z.B. Tinkerpop, TinkerGraph. Wird für die 
	 * Ausgabe der Statistik verwendet.  
	 */
	protected String databaseName;
	
	/**
	 * Pfad zu der datenbankspezifischen Textdatei 
	 * mit den Datenbankqueries für die Schreibperationen
	 */
	protected String pathToTxtFileWriteQueries;	
	
	/**
	 * Pfad zu der datenbankspezifischen Textdatei 
	 * mit den Datenbankqueries für die Leseoperationen
	 */
	protected String pathToTxtFileReadQueries;
	
	/**
	 * Encoding der Textdateien.
	 */
	protected final static Charset FILE_ENCODING = StandardCharsets.UTF_8;
	
	/**
	 * Connection Objekt zur Datenbank
	 */
	protected Object client;
	
	/**
	 * Konstruktor
	 */
	public GraphDB(){}
	

	/**
	 * Stellt eine Standard-Verbindung zur Datenbank her und speichert sie
	 * unter this.client. Anderfalls kann das Connection Objekt auch über 
	 * die Setter-Methode gesetzt werden.
	 */
	public abstract void connect();
	
	/**
	 * Schließt die Verbindung zur Datenbank.
	 */
	public abstract void disconnect();
		
	/**
	 * Führt eine Query über this.client aus.
	 */
	public abstract void executeQuery(String query);
	
	/**
	 * Liest einen Vertex mit dem Property 'name' aus vertexName 
	 * aus der Datenbank.
	 * @param vertexName
	 */
	public abstract void executeReadVertex(int vertexName);	
	
	/**
	 * Schreibt einen Vertex mit dem Property 'name' aus vertexName 
	 * in die Datenbank.
	 * @param vertexName
	 */
	public abstract void executeWriteVertex(int vertexName);	
	
	/**
	 * Wrapper nutzt this.loadFile und liest die Queries aus 
	 * this.pathToTxtFileReadQueries in eine LinkedList ein und gibt 
	 * diese zurück.
	 * @return LinkedList von read-queries.
	 */
	public LinkedList<String> loadReadQueries() 
	{
		Path path = Paths.get(this.pathToTxtFileReadQueries);
		LinkedList<String> linkedList = this.loadFile(path);
		
		return linkedList;
	}
	
	/**
	 * Wrapper nutzt this.loadFile und liest die Queries aus 
	 * this.pathToTxtFileWriteQueries in eine LinkedList ein und gibt 
	 * diese zurück.
	 * @return LinkedList von write-queries.
	 */
	public LinkedList<String> loadWriteQueries()
	{
		Path path = Paths.get(this.pathToTxtFileWriteQueries);
		LinkedList<String> linkedList = this.loadFile(path);
		
		return linkedList;
	}	
	
	/**
	 * List ein Text-File ein und gibt die Zeilen als LinkedList zurück.
	 * @return
	 */
	protected LinkedList<String> loadFile(Path path)
	{
		LinkedList<String> linkedList = new LinkedList<String>();	    
	    BufferedReader reader;
	    
	    try
	    {	    		    
			reader = Files.newBufferedReader(path, FILE_ENCODING);
					
			String line = null;
			while ((line = reader.readLine()) != null) 
			{
			  linkedList.add(line);
			}   
			
			reader.close();
	    } 
	    catch (IOException e) 
	    {
			e.printStackTrace();
		}
	
		return linkedList;
	}
		
	/**
	 * @param client the client to set
	 */
	public void setClient(Object client) 
	{
		this.client = client;
	}

}
