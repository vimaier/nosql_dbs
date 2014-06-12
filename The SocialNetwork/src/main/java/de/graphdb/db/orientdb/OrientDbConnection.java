package de.graphdb.db.orientdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

public class OrientDbConnection
{
	private static Logger log = LoggerFactory.getLogger(OrientDbConnection.class);
	
	private OrientGraphFactory graphFactory;
	
	private final String username = "admin";
	private final String password = "admin";
	private final String databaseProtocoll = "remote";
	private final String databaseHost = "10.0.3.44";
	private final String databaseName = "test";
	private final String databaseUrl = databaseProtocoll + ":" + databaseHost + "/" + databaseName;
	
	private enum SINGLETON
	{
		INSTANCE;
		private final OrientDbConnection connectionFactory;

		private SINGLETON()
		{
			connectionFactory = new OrientDbConnection();
		}

		private OrientDbConnection getConnection()
		{
			return connectionFactory;
		}
	}
	
	private OrientDbConnection()
	{
		
	}

	public static OrientDbConnection getInstance()
	{
		log.debug("get instance of conncetionfactory");
		return SINGLETON.INSTANCE.getConnection();
	}
	
	public boolean close()
	{
		log.debug("try to close database");
		
		if(this.graphFactory != null)
		{
			graphFactory.close();
		}
		else
		{
			log.debug("GraphDatabaseConnection is null");
		}

		log.debug("Closed database");
		
		return true;
	}	
	
	protected OrientGraphFactory getGraphFactory()
	{
		log.debug("try to get GraphFactory");
		
		if(this.graphFactory == null || this.graphFactory.getDatabase().isClosed())
		{
			open();
		}
		
		log.debug("return GraphFactory");
		
		return this.graphFactory;
	}
	
	protected void open()
	{	
		log.debug("try to open database");
		
		this.graphFactory = new OrientGraphFactory(this.databaseUrl,this.username,this.password).setupPool(10,10);		
		
		OSchema schema = this.graphFactory.getDatabase().getMetadata().getSchema();
		
		OClass oClass = schema.getClass("V");
		
		if(!oClass.areIndexed("mailadress"))
		{
			 oClass.createProperty("mailadress", OType.STRING).setNotNull(true);
			 oClass.createIndex("mailadressIndex",OClass.INDEX_TYPE.UNIQUE,"mailadress");	
		}
		
		log.debug("Succesfully opened database");
	}
}