package loadTest;

import de.graphdb.de.GraphDB;

public class WorkerThreadQuery extends WorkerThread
{
	/**
	 * Query die durch executeQuery ausgeführt werden soll.
	 */
	private String query;
	
	/**
	 * Konstruktor
	 * @param graphDB
	 */
	public WorkerThreadQuery(
			GraphDB graphDB, String query, MeasurementData measurement) 
	{
		super(graphDB);
		this.query = query;		
		this.measurement = measurement;		
	}
	
	public void run()
	{
		long elapsedTime = this.executeQuery();
		this.measurement.add(elapsedTime);
	}
	
	/**
	 * Führt eine Anfrage an den Datenbankserver aus und gibt die Zeit in
	 * Millisekunden zurück, die für die Ausführung benötigt wurde. 
	 * @return
	 */
	public long executeQuery()
	{
		//verbinde zur Datenbank
		this.graphDB.connect();
		
		//Starte Timer der die Zeit in Millisekunden misst, die für die 
		//Ausführung der Query benötigt wird. Die Zeit des Verbindungsaufbaus 
		//wird nicht gemessen!!!
		Stopwatch timer = new Stopwatch();		
		timer.start();
		
		//führe Query aus.
		this.graphDB.executeQuery(this.query);	
		
		//stoppe timer und erhalte vergangene Zeit in Millisekunden.
		long elapsedTime = timer.stop();
		
		//verbinde zur Datenbank
		this.graphDB.disconnect();
		
		return elapsedTime;
	}	
}
