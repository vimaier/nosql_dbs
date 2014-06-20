package loadTest;

import de.graphdb.de.GraphDB;

public class WorkerThread 
{
	/**
	 * Datenbankobjekt über das die Query mit this.executeQuery() 
	 * ausgeführt wird.
	 */
	private GraphDB graphDB;
	
	/**
	 * Konstruktor
	 * @param graphDB
	 */
	public WorkerThread(GraphDB graphDB)
	{
		this.graphDB = graphDB;
	}
	
	/**
	 * Führt eine Anfrage an den Datenbankserver aus und gibt die Zeit in
	 * Millisekunden zurück, die für die Ausführung benötigt wurde. 
	 * @return
	 */
	public long executeQuery(String query)
	{
		//verbinde zur Datenbank
		this.graphDB.connect();
		
		//Starte Timer der die Zeit in Millisekunden misst, die für die 
		//Ausführung der Query benötigt wird. Die Zeit des Verbindungsaufbaus 
		//wird nicht gemessen!!!
		Stopwatch timer = new Stopwatch();		
		timer.start();
		
		//führe Query aus.
		this.graphDB.executeQuery(query);	
		
		//stoppe timer und erhalte vergangene Zeit in Millisekunden.
		long elapsedTime = timer.stop();
		
		//verbinde zur Datenbank
		this.graphDB.disconnect();
		
		return elapsedTime;
	}
}
