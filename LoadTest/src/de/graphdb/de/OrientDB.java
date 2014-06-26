package de.graphdb.de;

import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

public class OrientDB extends GraphDB {

  private OrientGraphFactory graphFactory;

  private final String username = "admin";
  private final String password = "admin";
  private final String databaseProtocoll = "remote";
  private final String databaseHost = "10.0.3.44";
  private final String databaseName = "test";
  private final String databaseUrl = databaseProtocoll + ":" + databaseHost + "/" + databaseName;

  /**
   * Konstruktor
   */
  public OrientDB() {
    super.databaseName = "OrientDB";
  }

  private enum SINGLETON {
    INSTANCE;
    private final OrientDB connectionFactory;

    private SINGLETON() {
      connectionFactory = new OrientDB();
    }

    private OrientDB getConnection() {
      return connectionFactory;
    }
  }

  public static OrientDB getInstance() {
    return new OrientDB();
  }

  @Override
  public void connect() {
    super.client = OrientDB.getInstance();
  }

  @Override
  public void disconnect() {
    if (this.graphFactory != null) {
      graphFactory.close();
    }
  }

  @Override
  public void clearDatabase() {
    OrientGraph g = getGraph();
    if (g != null) {
      try {
        g.command(new OSQLSynchQuery<Vertex>("delete from V")).execute();
        g.commit();
      } catch (Exception e) {
        e.printStackTrace();
        g.rollback();
      } finally {
        g.shutdown();
      }
    }
  }

  @Override
  public void executeQuery(String query) {
    OrientGraph g = getGraph();
    if (g != null) {
      try {
        g.command(new OSQLSynchQuery<Vertex>(query)).execute();
        g.commit();
      } catch (Exception e) {
        e.printStackTrace();
        g.rollback();
      } finally {
        g.shutdown();
      }
    }
  }

  @Override
  public void executeReadVertex(int vertexName) {
    OrientGraph g = getGraph();
    if (g != null) {
      try {
        g.getVertex(vertexName);
        g.commit();
      } catch (Exception e) {
        e.printStackTrace();
        g.rollback();
      } finally {
        g.shutdown();
      }
    }
  }

  @Override
  public void executeWriteVertex(int vertexName) {
    OrientGraph g = getGraph();
    try {
      Vertex v = g.addVertex("V");
      v.setProperty("name", vertexName);
      g.commit();
    } catch (Exception e) {
      e.printStackTrace();
      g.rollback();
    } finally {
      g.shutdown();
    }
  }

  private OrientGraph getGraph() {
    try {
      return ((OrientDB) super.client).getGraphFactory().getTx();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  protected OrientGraphFactory getGraphFactory() {
    if (this.graphFactory == null || this.graphFactory.getDatabase().isClosed()) {
      OGlobalConfiguration.CLIENT_CHANNEL_MAX_POOL.setValue(50000);
      this.graphFactory = new OrientGraphFactory(this.databaseUrl, this.username, this.password);
    }
    return this.graphFactory;
  }
}
