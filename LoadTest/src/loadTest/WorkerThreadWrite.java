package loadTest;

import de.graphdb.de.GraphDB;

public class WorkerThreadWrite extends WorkerThread
{
	
	/**
	 * Wert von Property "name"
	 */
	private int vertexName;
	
	/**
	 * Konstruktor
	 * @param graphDB
	 */
	public WorkerThreadWrite(
			GraphDB graphDB, int vertexName, MeasurementData measurement) 
	{
		super(graphDB);
		this.vertexName = vertexName;	
		this.measurement = measurement;
	}
	
	public void run()
	{
		long elapsedTime = this.executeWriteVertex();
		this.measurement.add(elapsedTime);
	}
	
	/**
	 * Führt eine Schreib-Anfrage an den Datenbankserver aus und fügt den 
	 * Vertex mit dem Namen aus vertexName ein.
	 * @param vertexName
	 * @return
	 */
	public long executeWriteVertex()
	{		
		//Starte Timer der die Zeit in Millisekunden misst, die für die 
		//Ausführung der Query benötigt wird. Die Zeit des Verbindungsaufbaus 
		//wird nicht gemessen!!!
		Stopwatch timer = new Stopwatch();		
		timer.start();
		
		//verbinde zur Datenbank
		this.graphDB.connect();		
		
		//führe Query aus.
		this.graphDB.executeWriteVertex(this.vertexName);	
		
		//stoppe timer und erhalte vergangene Zeit in Millisekunden.
		long elapsedTime = timer.stop();
		
		//verbinde zur Datenbank
		this.graphDB.disconnect();
		
		return elapsedTime;
	}	

}
