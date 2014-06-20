package loadTest;

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
	 * Führt einen Lesetest mit allen Operationen aus this.queryList aus
	 * und gibt die Dauer bis zum Ende der Operation zurück.
	 * @return
	 */
	public double performReadTest(int numberOfThreads)
	{
		//Merke die Anzhal der Threads um das arithmetische Mittel zu 
		//berechnen nachdem alle queries ausgeführt wurden.
		this.numberOfThreads = numberOfThreads;
		
		//Stelle Verbindung zur Datenbank her.
		graphDB.connect();
		
		//lade spezifische Datenbank-Queries in den Speicher.
		this.queryList = graphDB.loadReadQueries();
		
		Iterator<String> it = this.queryList.iterator();
		
		while(true == it.hasNext())
		{
			String query = it.next();
			
			//Übergebe Ausführung der Query an Qorker-Thread und erhalte
			//benötigte Zeit in Millisekunden.
			long elapsedTime = new WorkerThread(graphDB)
				.executeQuery(query);
			
			//Füge Zeitmessung zum Array mit allen Zeiten hinzu
			this.elapsedTimes.add((int) elapsedTime);			
		}
		
		//berechne Standardabweichung
		this.calcStandardabweichung();
				
		return 0;
	}
	
	/**
	 * Führt einen Schreibtest mit allen Operationen aus this.queryList aus
	 * und gibt die Dauer bis zum Ende der Operation zurück.
	 * @return
	 */
	public double performWriteTest(int numberOfThreads)
	{
		return 0;
		
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
		System.out.println("Gesamtzeit: " + this.elapsedTimeTotal);
		
		//berechne arithmetisches Mittel über die Messungen aller Queries.
		this.arithmetischesMittel = this.elapsedTimeTotal / numberOfThreads; 
		System.out.println("Arithmetisches Mittel: " 
				+ this.arithmetischesMittel);
		
		//berechne die Varianz
		double tmp = 0;
		for (int elapsedTime : this.elapsedTimes) {
			tmp += Math.pow(
					elapsedTime - this.arithmetischesMittel, 2);
		}
		this.varianz = tmp / this.numberOfThreads;
		System.out.println("Varianz: " + this.varianz);
		
		//berechne Standardabweichung 
		this.standardabweichung = Math.sqrt(this.varianz / numberOfThreads);
		System.out.println("Standardabweichung: " + this.standardabweichung);			
	}
}
