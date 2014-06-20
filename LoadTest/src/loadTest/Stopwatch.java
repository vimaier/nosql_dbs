package loadTest;

public class Stopwatch {

	/**
	 * Timestamp zum Zeitpunkt des Starts.
	 */
	private long startTime;
	
	/**
	 * Timestamp zum Zeitpunkt des Stops.
	 */
    private long stopTime;
    
    /**
     * Vergangene Zeit einer einzigen Zeitmessung.
     */
    private long elapsedTime;    
                
    /**
     * Startet eine Zeitmessung.
     * @return Startzeit
     */
    public long start()
    {
    	this.startTime = System.currentTimeMillis();
    	 
    	return this.startTime;
    }
    
    /**
     * Stopt eine Zeitmessung.
     * @return Gibt die Differenz zwischen Startzeitpunkt und Stopzeitpunkt
     * zurück.
     */
    public long stop()
    {
    	this.stopTime = System.currentTimeMillis();
    	this.elapsedTime = this.stopTime - this.startTime;
    	
    	return this.elapsedTime;
    }   
}
