package loadTest;

import de.graphdb.de.GraphDB;

public class WorkerThread extends Thread
{
	/**
	 * Datenbankobjekt über das die Query mit this.executeQuery() 
	 * ausgeführt wird.
	 */
	protected GraphDB graphDB;
	
	/**
	 * Geteilstes Messdaten-Objekt über alle Threads
	 */
	protected MeasurementData measurement;	
			
	/**
	 * Konstruktor
	 * @param graphDB
	 */
	public WorkerThread(GraphDB graphDB)
	{
		this.graphDB = graphDB;
	}
}
