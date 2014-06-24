package loadTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;

public class MeasurementData 
{
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
	 * Anzhal der Threads die für diese Messung verwendet werden.
	 */
	private int numberOfThreads;
	
	/**
	 * Anzahl abgearbeiteter Threads.
	 */
	private int cnt = 0;
	
	/**
	 * Name der Datenbank die gemessen wird
	 */
	private String databasename;
	
	/**
	 * Entweder "Schreibtest" oder "Lesetest". Wir durch die perform-Methoden
	 * gesetzt.
	 */
	private String action;
		
	/**
	 * Konstruktor setzt die Anzahl der Threads die für diesen Messung 
	 * verwendet werden.
	 * @param numberOfThreads
	 */
	public MeasurementData(
			int numberOfThreads, String datebasename, String action)
	{
		this.numberOfThreads = numberOfThreads;
		this.databasename = datebasename;
		this.action = action;
	}
	
	/**
	 * Berechne die Standardabweichung der Zeitmessungen über alle Queries.
	 * @return Standardabweichung
	 */
	public void calcStandardabweichung()
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
	 * Gibt das Ergebnis eines Tests auf die Konsole aus.
	 */
	public void printReport()
	{
		//Action und Datenbankname
		System.out.println(this.action + " für " 
				+ this.databasename);
		
		//Anzahl der Threads
		System.out.println("Anzahl Threads gestartet: " 
				+ this.numberOfThreads);
		
		//Vergangene Gesamtzeit
		System.out.println("Gesamtzeit: " + this.elapsedTimeTotal + " ms");		
		
		//Arithmetisches Mittel der Gesamtzeit
		System.out.println("Arithmetisches Mittel: " 
				+ this.arithmetischesMittel + " ms");	
		
		//Varianz
		System.out.println("Varianz: " + this.varianz + " ms");
		
		//Standardabweichung aller Anfragen dieses Tests
		System.out.println("Standardabweichung: " + this.standardabweichung 
				+ " ms");
		
		System.out.println("\n==========================================\n");
	}
	
	/**
	 * Füge den Report einer CSV-Datei hinzu.
	 */
	public void reportToCsv(String pathToCsvFile)
	{
		
		Writer writer = null;
		
		try 
		{
	        File reportFile = new File(pathToCsvFile);
	        
	        boolean fileExists = false;
	        if(reportFile.exists() && false == reportFile.isDirectory())
	        {
	        	fileExists = true;
	        }
	        
	        FileOutputStream os = new FileOutputStream(reportFile, true);
	        OutputStreamWriter osw = new OutputStreamWriter(os);    
	        writer = new BufferedWriter(osw);
	        
	        if(false == fileExists)
	        {
		        //Schreibe Header
		        writer.write("anzThreads, ");
		        writer.write("gesamtzeit, ");
		        writer.write("mittel, ");
		        writer.write("varianz, ");
		        writer.write("abweichung");
		        writer.write("\n");
	        }
	        
	        //schreibe Daten
	        writer.write(this.numberOfThreads + ", ");
	        writer.write(this.elapsedTimeTotal + ", ");
	        writer.write(this.arithmetischesMittel + ", ");
	        writer.write(this.varianz + ", ");
	        writer.write(this.standardabweichung + "");
	        writer.write("\n");
	               
	    } 
		catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
		finally
		{
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Fügt einen neuen Messwert zu this.elapsedTimes hinzu. Wenn alle 
	 * Threads abgearbeitet sind und cnt == numberOfThreads, wird der 
	 * Report erstellt.
	 * @param elapsedTime
	 */
	public synchronized void add(long elapsedTime) 
	{
		this.elapsedTimes.add((int) elapsedTime);
		System.out.println("\nNeue Messung: " + elapsedTime + "\n");
		this.incrementCnt();
		
		if(this.cnt == numberOfThreads)
		{
			//berechnet Ergebnis
			this.calcStandardabweichung();
			
			//Erstelle Reports
			printReport();
			this.reportToCsv("/home/cls/workspaces/eclipsejee/"
					+ "nosql_dbs/LoadTest/src/reports/"
					+ this.databasename.toLowerCase() 
					+ "-" + this.action + ".txt");			
		}
	}
	
	/**
	 * Erhöht this.cnt um eins.
	 */
	private synchronized void incrementCnt()
	{
		this.cnt++;
	}
}
