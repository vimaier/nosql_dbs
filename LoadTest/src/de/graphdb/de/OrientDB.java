package de.graphdb.de;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import de.graphdb.db.orientdb.OrientDbConnection;

public class OrientDB extends GraphDB {

  private static Logger log = LoggerFactory.getLogger(OrientDB.class);

  /**
   * Konstruktor
   */
  public OrientDB() {
    super.databaseName = "OrientDB";
  }

  @Override
  public void connect() {
    super.client = OrientDbConnection.getInstance();
  }

  @Override
  public void disconnect() {
    ((OrientDbConnection) super.client).close();
  }

  @Override
  public void clearDatabase() {
    OrientGraph g = getGraph();
    if (g != null) {
      try {
        /**
         * TODO
         */
        g.commit();
      } catch (Exception e) {
        log.error("executeQuery");
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
        log.error("executeQuery");
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
        log.error("Error executeReadVertex with vertexName=" + vertexName);
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
      log.error("Error writing Vertex", e);
      g.rollback();
    } finally {
      g.shutdown();
    }
  }

  private OrientGraph getGraph() {
    try {
      return ((OrientDbConnection) super.client).getGraphFactory().getTx();
    } catch (Exception e) {
      log.error("Couldn't create graphTx", e);
      return null;
    }
  }

}
