# -*- coding: utf-8 -*-
"""
Created on Sun May 18 16:41:08 2014

@author: viktor
"""
import matplotlib.pyplot as pyplot


THREADS = [500, 600, 700, 800, 900, 1000]

NEO4J_TIMES = [100, 90, 80, 70, 60, 50]
NEO4J_STANDARD_DEVIATIONS = [1, 2, 1, 2, 1, 2]

TINKERPOP_TIMES = [95, 85, 75, 65, 55, 45]
TINKERPOP_STANDARD_DEVIATIONS = [1, 2, 1, 2, 1, 2]

ORIENT_DB_TIMES = [90, 80, 70, 60, 50, 40]
ORIENT_DB_STANDARD_DEVIATIONS = [1, 2, 1, 2, 1, 2]


def main():
    plot_times()
                  
def plot_times():
    pyplot.errorbar(THREADS, NEO4J_TIMES, yerr=NEO4J_STANDARD_DEVIATIONS, 
                    marker='o', linestyle='--', color='b', label='neo4j')
    pyplot.errorbar(THREADS, TINKERPOP_TIMES, yerr=TINKERPOP_STANDARD_DEVIATIONS,
                    marker='o', linestyle='--', color='r', label='tinkerpop')
    pyplot.errorbar(THREADS, ORIENT_DB_TIMES, yerr=ORIENT_DB_STANDARD_DEVIATIONS,
                    marker='o', linestyle='--', color='g', label='orientdb')
    
    pyplot.xlabel('#Threads')
    pyplot.ylabel('Query time')
    pyplot.title('Inserts')
    pyplot.legend(loc='upper right', shadow=True)
    pyplot.show()
   

if  __name__ =='__main__':
    main() 
