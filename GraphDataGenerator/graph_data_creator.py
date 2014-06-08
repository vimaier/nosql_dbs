'''
Created on 06.06.2014

@author: vimaier
'''

from __future__ import print_function
import sys
import os
import traceback
import json
import random

_EMAIL_SCHEME = "{0}.{1}@graphdb.fh"

CYPHER_FILE_PATH = "cypher_queries.txt"
# '{{' and '}}' are escape characters for '{' and '}'
_CYPHER_CREATE_NODE_STMNT = "CREATE ( :USER{{ mailadress:\"{0}\", password:\"{1}\", forename:\"{2}\", surname:\"{3}\", city:\"{4}\" }} )"
_CYPHER_CREATE_EDGE_STMT = "MATCH (first:USER{{mailadress:\"{0}\"}}), (second:USER{{mailadress:\"{1}\"}}) CREATE (first)-[:FRIENDS]->(second)"

_ORIENTDB_FILE_PATH = "orientdb_queries.txt"
_ORIENTDB_CREATE_NODE_STMNT = "TODO: {0}{1}{2}{3}{4}"
_ORIENTDB_CREATE_EDGE_STMT = "TODO: {0}{1}"

_GREMLIN_FILE_PATH = "tinkerpop_queries.txt"
_GREMLIN_CREATE_NODE_STMNT = "TODO: {0}{1}{2}{3}{4}"
_GREMLIN_CREATE_EDGE_STMT = "TODO: {0}{1}"


def main():
    create_graphdb_statements()
    
def create_graphdb_statements():
    graph_data_loader = GraphDataLoader()
    graph_data = graph_data_loader.get_graph_data()
    create_cypher_queries(graph_data)
    create_orientdb_queries(graph_data)
    create_gremlin_queries(graph_data)
    
def create_cypher_queries(graph_data):
    query_list = _create_queries(graph_data, _CYPHER_CREATE_NODE_STMNT, _CYPHER_CREATE_EDGE_STMT)
    _save_queries_into_file(query_list, CYPHER_FILE_PATH)
	    
def create_orientdb_queries(graph_data):
    query_list = _create_queries(graph_data, _ORIENTDB_CREATE_NODE_STMNT, _ORIENTDB_CREATE_EDGE_STMT)
    _save_queries_into_file(query_list, ORIENTDB_FILE_PATH)
	    
def create_gremlin_queries(graph_data):
    query_list = _create_queries(graph_data, _GREMLIN_CREATE_NODE_STMNT, _GREMLIN_CREATE_EDGE_STMT)
    _save_queries_into_file(query_list, GREMLIN_FILE_PATH)
    
        
def _create_queries(graph_data, create_node_stmt, create_edge_stmt):
    queries = []
    for node_entry in graph_data["nodes"]:
        forename = node_entry[0]
        surname = node_entry[1]
        mailadress = _EMAIL_SCHEME.format(forename, surname).lower()
        city = node_entry[2]
        password =  forename
        stmt = create_node_stmt.format(mailadress,
                                       password,
                                       forename,
                                       surname,
                                       city
                                       )
        queries.append(stmt)
    for edge_entry in graph_data["edges"]:
        first_node = graph_data["nodes"][edge_entry[0]]
        second_node = graph_data["nodes"][edge_entry[1]]
        first_mailadress = _EMAIL_SCHEME.format(first_node[0], first_node[1]).lower()
        second_mailadress = _EMAIL_SCHEME.format(second_node[0], second_node[1]).lower()
        stmt = create_edge_stmt.format(first_mailadress, second_mailadress)
        queries.append(stmt)
    return queries

def _save_queries_into_file(query_list, file_path):
    try:
        with open(file_path, "w") as f :
            f.write("\n".join(query_list))
    except EnvironmentError: # parent of IOError, OSError *and* WindowsError where available
        print("Error saving file: {0}".format(file_path), file=sys.stderr)
        
    
    

class GraphDataLoader(object):
    '''
    Responsible for creating the graph data
    '''
    _CREATED_GRAPH_DATA_FILENAME = "created_graph_data.json"
    _FIRST_NAMES_JSON_FILENAME = "first-names.json"
    _NAMES_JSON_FILENAME = "names.json"
    _PLACES_JSON_FILENAME = "places.json"
    
    _NODE_NUMBER = 1000
    _EDGE_NUMBER = 1500

    def __init__(self):
        '''
        Constructor
        '''
        self._first_names_list = []
        self._names_list = []
        self._places_list = []
        
    def get_graph_data(self):
        """
         graph_data: {
                "nodes":list_of_node_entry
                            node_entry: [first_name, name, place]
                "edges":list_of_edge_entry
                    edge_entry: [index_first_node, index_second_node]
                    }
        """
        if(os.path.exists(self._CREATED_GRAPH_DATA_FILENAME)):
            return self._load_json_file(self._CREATED_GRAPH_DATA_FILENAME)
        return self._create_and_get_graph_data()
    
    def _create_and_get_graph_data(self):    
        self._load_json_files()
        graph_data = self._create_graph_data()
        self._save_graph_data(graph_data)
        return graph_data
    
    def _create_graph_data(self):
        """
        Creates a dictionary with random data:
            "nodes":list_of_node_entry
                node_entry: [first_name, name, place]
            "edges":list_of_edge_entry
                edge_entry: [index_first_node, index_second_node]
        """ 
        list_of_node_entries = self._create_nodes()
        list_of_edge_entries = self._create_edges(list_of_node_entries)
        graph_data = { "nodes": list_of_node_entries,
                        "edges": list_of_edge_entries}
        return graph_data
        
    def _create_nodes(self):
        # We need to create _NODE_NUMBER nodes by using FIRST_NAMES, NAMES and PLACES
        # first name and name together should be unique (will be used as mailaddress)
        num_created = 0
        list_of_node_entries = []
        while(num_created < self._NODE_NUMBER):
            rdm_idx_first_name = random.randrange(len(self._first_names_list))
            rdm_idx_name = random.randrange(len(self._names_list))
            rdm_idx_place = random.randrange(len(self._places_list))
            node_entry = [
                          self._first_names_list[rdm_idx_first_name],
                          self._names_list[rdm_idx_name],
                          self._places_list[rdm_idx_place]
                          ]
            # Check if firstnam and name combination is already included
            if (self._node_is_already_included(list_of_node_entries, node_entry)):
                continue
            list_of_node_entries.append(node_entry)
            num_created = num_created + 1
        return list_of_node_entries
    def _node_is_already_included(self, list_of_node_entries, node_entry):
        for included_node_entry in list_of_node_entries:
            if(included_node_entry[0] == node_entry[0] and included_node_entry[1] == node_entry[1]):
                return True
        return False
        
    def _create_edges(self, list_of_node_entries):
        # We create _EDGE_NUMBER random edges
        # We assure that an edge will not occur twice between two nodes
        num_created = 0
        list_of_edge_entries = []
        while(num_created < self._EDGE_NUMBER):
            first_rdm_idx = random.randrange(len(list_of_node_entries))
            second_rdm_idx = random.randrange(len(list_of_node_entries))
            edge_entry = [first_rdm_idx, second_rdm_idx]
            if (self._edge_is_not_valid_or_already_included(list_of_edge_entries, edge_entry)):
                continue
            list_of_edge_entries.append(edge_entry)
            num_created = num_created + 1
        return list_of_edge_entries
    def _edge_is_not_valid_or_already_included(self, list_of_edge_entries, edge_entry):
        if(edge_entry[0] == edge_entry[1]):
            return False
        for included_edge in list_of_edge_entries:
            if(included_edge == edge_entry or 
                    (included_edge[1] == edge_entry[0] and included_edge[0] == edge_entry[1]) ):
                return True
        return False
      
    def _load_json_files(self):
        self._load_first_names()
        self._loadt_names()
        self._load_places()    
    def _load_first_names(self):
        self._first_names_list = self._load_json_file(GraphDataLoader._FIRST_NAMES_JSON_FILENAME)
    def _loadt_names(self):
        self._names_list = self._load_json_file(GraphDataLoader._NAMES_JSON_FILENAME)
    def _load_places(self):
        self._places_list = self._load_json_file(GraphDataLoader._PLACES_JSON_FILENAME)    
    def _load_json_file(self, file_path):
        try:
            with open(file_path, "r") as f :
                parsed_content = json.load(f)
        except EnvironmentError: # parent of IOError, OSError *and* WindowsError where available
            print("Error loading file: {0}".format(file_path), file=sys.stderr)
            return None
        return parsed_content
    
    def _save_graph_data(self, graph_data):
        try:
            with open(self._CREATED_GRAPH_DATA_FILENAME, "w") as f :
                json.dump(graph_data, f)
        except EnvironmentError: # parent of IOError, OSError *and* WindowsError where available
            print("Error saving graph data", file=sys.stderr)
            traceback.print_exc()
        

if __name__ == '__main__':
    main()