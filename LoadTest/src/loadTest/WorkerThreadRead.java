package loadTest;

import de.graphdb.de.GraphDB;

public class WorkerThreadRead extends WorkerThread
{
	/**
	 * Wert von Property "name"
	 */
	private int vertexName;
		
	/**
	 * Konstruktor
	 * @param graphDB
	 */
	public WorkerThreadRead(
			GraphDB graphDB, int vertexName, MeasurementData measurement) 
	{
		super(graphDB);
		this.vertexName = vertexName;	
		this.measurement = measurement;
	}
	
	public void run()
	{
		long elapsedTime = this.executeReadVertex();
		this.measurement.add(elapsedTime);
	}
	
	/**
	 * Führt eine Lese-Anfrage an den Datenbankserver aus und list den 
	 * Vertex mit dem Namen aus vertexName aus.
	 * @param vertexName
	 * @return
	 */
	public long executeReadVertex()
	{
		//verbinde zur Datenbank
		this.graphDB.connect();
		
		//Starte Timer der die Zeit in Millisekunden misst, die für die 
		//Ausführung der Query benötigt wird. Die Zeit des Verbindungsaufbaus 
		//wird nicht gemessen!!!
		Stopwatch timer = new Stopwatch();		
		timer.start();
		
		//führe Query aus.
		this.graphDB.executeReadVertex(this.vertexName);	
		
		//stoppe timer und erhalte vergangene Zeit in Millisekunden.
		long elapsedTime = timer.stop();
		
		//verbinde zur Datenbank
		this.graphDB.disconnect();
		
		return elapsedTime;
	}	
}
