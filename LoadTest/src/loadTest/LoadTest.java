package loadTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;

import de.graphdb.de.GraphDB;
import de.graphdb.de.Neo4jDB;
import de.graphdb.de.OrientDB;
import de.graphdb.de.TinkerpopDB;

public class LoadTest {

	/**
	 * Datenbankklasse für die ein Test durchgeführt werden soll.
	 */
	GraphDB graphDB;
	
	/**
	 * Queries die durch den Test abgearbeitet werden sollen.
	 */
	LinkedList<String> queryList;
	
	/**
	 * Anzahl der Threads die gestartet werden sollen um die Queries 
	 * abzuarbeiten.
	 */
	private int numberOfThreads;
	
	/**
	 * Messdatenobjekt speichert die Messdaten, berechnet die Ergebniswerte
	 * und erstellt die Reports.
	 */
	private MeasurementData measurement;
		
	/**
	 * Konstruktor
	 */
	public LoadTest(GraphDB graphDB)
	{
		this.graphDB = graphDB;
	}
	
	/**
	 * Führt einen Lesetest mit Operationen aus und berechnet die 
	 * Standardabweichung der Dauer über alle Operationen.
	 * @return
	 */
	public void performReadTest(int numberOfThreads)
	{
		//Merke die Anzhal der Threads um das arithmetische Mittel zu 
		//berechnen nachdem alle queries ausgeführt wurden.
		this.numberOfThreads = numberOfThreads;
		
		//Erstelle neues Messobjekt zum Messen der Performance
		this.measurement = new MeasurementData(
				this.numberOfThreads, 
				this.graphDB.getDatabaseName(),
				"Lesetest");
					
		int vertexName = 1;
		while(vertexName++ <= numberOfThreads)
		{			
			//erzeuge neues DatenbankObjekt abhängig vom typ von this.graphDB
			GraphDB g = createNewDBObject();
			
			//Übergebe Ausführung der Query an Worker-Thread und erhalte
			//benötigte Zeit in Millisekunden.
			new WorkerThreadRead(g, vertexName, this.measurement).start();
						
		}
	}
	
	/**
	 * Führt einen Lesetest mit allen Operationen aus this.queryList aus
	 * und berechnet die Standardabweichung der Dauer über alle Operationen.
	 * @return
	 */
	public void performReadTestFromFile(int numberOfThreads)
	{
		//Merke die Anzhal der Threads um das arithmetische Mittel zu 
		//berechnen nachdem alle queries ausgeführt wurden.
		this.numberOfThreads = numberOfThreads;		
		
		//Erstelle neues Messobjekt zum Messen der Performance
		this.measurement = new MeasurementData(
				this.numberOfThreads, 
				this.graphDB.getDatabaseName(),
				"Lesetest");		
				
		//lade spezifische Datenbank-Queries in den Speicher.
		this.queryList = this.graphDB.loadReadQueries();
		
		Iterator<String> it = this.queryList.iterator();
		
		while(true == it.hasNext())
		{
			String query = it.next();
			
			//erzeuge neues DatenbankObjekt abhängig vom typ von this.graphDB
			GraphDB g = createNewDBObject();
			
			//Übergebe Ausführung der Query an Worker-Thread und erhalte
			//benötigte Zeit in Millisekunden.
			new WorkerThreadQuery(g, query, this.measurement).start();			
		}
	}
	
	/**
	 * Führt einen Schreibtest mit Operationen aus  
	 * und berechnet die Standardabweichung der Dauer über alle Operationen.
	 * @return
	 */
	public void performWriteTest(int numberOfThreads)
	{
		//Merke die Anzhal der Threads um das arithmetische Mittel zu 
		//berechnen nachdem alle queries ausgeführt wurden.
		this.numberOfThreads = numberOfThreads;
		
		//Erstelle neues Messobjekt zum Messen der Performance
		this.measurement = new MeasurementData(
				this.numberOfThreads, 
				this.graphDB.getDatabaseName(),
				"Schreibtest");	
		
		//clear Database to start empty
		this.graphDB.clearDatabase();
					
		int vertexName = 1;
		while(vertexName++ <= numberOfThreads)
		{			
			//erzeuge neues DatenbankObjekt abhängig vom typ von this.graphDB
			GraphDB g = createNewDBObject();
			
			//Übergebe Ausführung der Query an Worker-Thread und erhalte
			//benötigte Zeit in Millisekunden.
			new WorkerThreadWrite(g, vertexName, this.measurement).start();		
		}
	}
	
	/**
	 * Führt einen Schreibtest mit allen Operationen aus this.queryList aus
	 * und berechnet die Standardabweichung der Dauer über alle Operationen.
	 * @return
	 */
	public void performWriteTestFromFile(int numberOfThreads)
	{
		//Merke die Anzhal der Threads um das arithmetische Mittel zu 
		//berechnen nachdem alle queries ausgeführt wurden.
		this.numberOfThreads = numberOfThreads;
		
		//Erstelle neues Messobjekt zum Messen der Performance
		this.measurement = new MeasurementData(
				this.numberOfThreads, 
				this.graphDB.getDatabaseName(),
				"Schreibtest");	
		
		//clear Database to start empty
		this.graphDB.clearDatabase();		
				
		//lade spezifische Datenbank-Queries in den Speicher.
		this.queryList = this.graphDB.loadWriteQueries();
		
		Iterator<String> it = this.queryList.iterator();
		
		while(true == it.hasNext())
		{
			String query = it.next();
			
			//erzeuge neues DatenbankObjekt abhängig vom typ von this.graphDB
			GraphDB g = createNewDBObject();			
			
			//Übergebe Ausführung der Query an Worker-Thread und erhalte
			//benötigte Zeit in Millisekunden.
			new WorkerThreadQuery(g, query, this.measurement).start();					
		}
	}
	
	/**
	 * Gibt ein neues DatenbankObjekt mit neuem Client zurück.
	 * @return
	 */
	private GraphDB createNewDBObject()
	{
		if(true == (this.graphDB instanceof TinkerpopDB))
		{
			return new TinkerpopDB();
		}
		else if(true == (this.graphDB instanceof Neo4jDB))
		{
			return new Neo4jDB();
		}
		else if(true == (this.graphDB instanceof OrientDB))
		{
			return new OrientDB();
		}
		
		return null;
	}
}
