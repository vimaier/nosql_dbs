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
	 * Entweder "Schreibtest" oder "Lesetest". Wir durch die perform-Methoden
	 * gesetzt.
	 */
	private String action;
	
	/**
	 * Zeitmessugnen der einzelnen Queries.
	 */
	LinkedList<Integer> elapsedTimes = new LinkedList<Integer>();
	
	/**
	 * Zeit die während eines Tests vergangen ist.
	 */
	private long elapsedTimeTotal = 0;
	
	/**
	 * Aritmetisches Mittel über alle Zeitmessungen
	 */
	private double arithmetischesMittel = 0.0;
	
	/**
	 * Varianz aller Zeitmessungen.
	 */
	private double varianz = 0.0;
	
	/**
	 * Standardabweichung aller Messungen.
	 */
	private double standardabweichung = 0.0;
	
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
		
		this.action = "Lesetest";
					
		int vertexName = 1;
		while(vertexName++ <= numberOfThreads)
		{			
			//Übergebe Ausführung der Query an Worker-Thread und erhalte
			//benötigte Zeit in Millisekunden.
			long elapsedTime = new WorkerThread(graphDB)
				.executeReadVertex(vertexName);
			
			//Füge Zeitmessung zum Array mit allen Zeiten hinzu
			this.elapsedTimes.add((int) elapsedTime);			
		}
		
		//berechne Standardabweichung
		this.calcStandardabweichung();
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
		
		this.action = "Lesetest";		
				
		//lade spezifische Datenbank-Queries in den Speicher.
		this.queryList = graphDB.loadReadQueries();
		
		Iterator<String> it = this.queryList.iterator();
		
		while(true == it.hasNext())
		{
			String query = it.next();
			
			//Übergebe Ausführung der Query an Worker-Thread und erhalte
			//benötigte Zeit in Millisekunden.
			long elapsedTime = new WorkerThread(graphDB)
				.executeQuery(query);
			
			//Füge Zeitmessung zum Array mit allen Zeiten hinzu
			this.elapsedTimes.add((int) elapsedTime);			
		}
		
		//berechne Standardabweichung
		this.calcStandardabweichung();
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
		
		this.action = "Schreibtest";		
					
		int vertexName = 1;
		while(vertexName++ <= numberOfThreads)
		{			
			//Übergebe Ausführung der Query an Worker-Thread und erhalte
			//benötigte Zeit in Millisekunden.
			long elapsedTime = new WorkerThread(graphDB)
				.executeWriteVertex(vertexName);
			
			//Füge Zeitmessung zum Array mit allen Zeiten hinzu
			this.elapsedTimes.add((int) elapsedTime);			
		}
		
		//berechne Standardabweichung
		this.calcStandardabweichung();
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
		
		this.action = "Schreibtest";		
				
		//lade spezifische Datenbank-Queries in den Speicher.
		this.queryList = graphDB.loadWriteQueries();
		
		Iterator<String> it = this.queryList.iterator();
		
		while(true == it.hasNext())
		{
			String query = it.next();
			
			//Übergebe Ausführung der Query an Worker-Thread und erhalte
			//benötigte Zeit in Millisekunden.
			long elapsedTime = new WorkerThread(graphDB)
				.executeQuery(query);
			
			//Füge Zeitmessung zum Array mit allen Zeiten hinzu
			this.elapsedTimes.add((int) elapsedTime);			
		}
		
		//berechne Standardabweichung
		this.calcStandardabweichung();
	}	
	
	/**
	 * Berechne die Standardabweichung der Zeitmessungen über alle Queries.
	 * @return Standardabweichung
	 */
	private void calcStandardabweichung()
	{
		//Summiere Zeiten der einzelnen Queries
		for (int elapsedTime : this.elapsedTimes) {
			this.elapsedTimeTotal += elapsedTime;
		}
		
		//berechne arithmetisches Mittel über die Messungen aller Queries.
		this.arithmetischesMittel = this.elapsedTimeTotal / numberOfThreads; 
		
		//berechne die Varianz
		double tmp = 0;
		for (int elapsedTime : this.elapsedTimes) {
			tmp += Math.pow(
					elapsedTime - this.arithmetischesMittel, 2);
		}
		this.varianz = tmp / this.numberOfThreads;
	
		//berechne Standardabweichung 
		this.standardabweichung = Math.sqrt(this.varianz / numberOfThreads);			
	}
	
	/**
	 * Setzt Testdaten zurück.
	 */
	public void reset()
	{
		this.elapsedTimes = new LinkedList<Integer>();		
		this.arithmetischesMittel = 0.0;
		this.elapsedTimeTotal = 0;
		this.standardabweichung = 0.0;
		this.varianz = 0.0;
	}
	
	/**
	 * Gibt das Ergebnis eines Tests auf die Konsole aus.
	 */
	public void printReport()
	{
		//Action und Datenbankname
		System.out.println(this.action + " für " 
				+ this.graphDB.getDatabaseName());
		
		//Anzahl der Threads
		System.out.println("Anzahl Threads gestartet: " 
				+ this.numberOfThreads);
		
		//Vergangene Gesamtzeit
		System.out.println("Gesamtzeit: " + this.elapsedTimeTotal);		
		
		//Arithmetisches Mittel der Gesamtzeit
		System.out.println("Arithmetisches Mittel: " 
				+ this.arithmetischesMittel);	
		
		//Varianz
		System.out.println("Varianz: " + this.varianz);
		
		//Standardabweichung aller Anfragen dieses Tests
		System.out.println("Standardabweichung: " + this.standardabweichung);
		
		System.out.println("\n==========================================\n");
	}
	
	/**
	 * Füge den Report einer CSV-Datei hinzu.
	 */
	public void reportToCsv(String pathToCsvFile)
	{
		try 
		{
	        File reportFile = new File(pathToCsvFile);
	        
	        FileOutputStream os = new FileOutputStream(reportFile, true);
	        OutputStreamWriter osw = new OutputStreamWriter(os);    
	        Writer writer = new BufferedWriter(osw);
	        
	        //Schreibe Header
	        //writer.write("anzThreads, ");
	        //writer.write("gesamtzeit, ");
	        //writer.write("mittel, ");
	        //writer.write("varianz, ");
	        //writer.write("abweichung");
	        //writer.write("\n");
	        
	        //schreibe Daten
	        writer.write(this.numberOfThreads + ", ");
	        writer.write(this.elapsedTimeTotal + ", ");
	        writer.write(this.arithmetischesMittel + ", ");
	        writer.write(this.varianz + ", ");
	        writer.write(this.standardabweichung + "");
	        writer.write("\n");
	        
	        writer.close();
	       
	    } catch (IOException e) {
	        System.err.println("Problem writing to the file statsTest.txt");
	    }
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
}
