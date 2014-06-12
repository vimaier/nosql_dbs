'''
Created on 07.06.2014

Loads all cypher queries in graph_data_creator.CYPHER_FILE_PATH and 
executes them on the server...

@author: Viktor
'''

import py2neo
from py2neo import cypher

import graph_data_creator

SERVER_URI = "http://10.0.107.174:7474/db/data/"

def main():
    query_list = load_cypher_queries()
    upload_nodes_to_neo4j(query_list)


def load_cypher_queries():
    with open(graph_data_creator.CYPHER_FILE_PATH, "r") as f:
        return f.readlines()
    
def upload_nodes_to_neo4j(query_list):
    graph_db = py2neo.neo4j.GraphDatabaseService(SERVER_URI)
    for cypher_query in query_list:
        cypher.execute(graph_db, cypher_query)
    
if __name__ == '__main__':
    main()