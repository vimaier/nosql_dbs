# -*- coding: utf-8 -*-
"""
Created on Sun May 18 16:41:08 2014

@author: viktor
"""
import matplotlib.pyplot as pyplot


THREADS = [1, 2, 3, 4, 5, 10, 20, 50, 100]

NEO4J_TIMES = [1539, 1718, 2379, 2488, 2582, 4521, 8178, 16014, 0]
NEO4J_STANDARD_DEVIATIONS = [0.0, 1.12, 2.33, 8.48, 4.42, 11.58, 41.28, 276.9, 0]

TINKERPOP_TIMES = [1281, 1504, 1681, 5455, 11019, 12496, 15690, 16968, 17041]
TINKERPOP_STANDARD_DEVIATIONS = [0, 0, 0.33, 3414.09, 3435.28, 2291.76, 1063.45, 313.33, 221.58]

ORIENT_DB_TIMES = [90, 80, 70, 60, 50, 40, 0, 0, 0]
ORIENT_DB_STANDARD_DEVIATIONS = [1, 2, 1, 2, 1, 2, 0, 0, 0]


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
    pyplot.ylabel('Query time (ms)')
    pyplot.title('Reads')
    pyplot.legend(loc='lower right', shadow=True)
    pyplot.show()
   

if  __name__ =='__main__':
    main() 
