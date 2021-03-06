# -*- coding: utf-8 -*-
"""
Created on Sun May 18 16:41:08 2014

@author: viktor
"""
import matplotlib.pyplot as pyplot


THREADS = [10, 50, 100, 200, 300, 400, 500, 750, 1000]

NEO4J_TIMES = [719, 3366, 4741, 7676, 9645, 10855, 12369, 16778, 23009]
NEO4J_STANDARD_DEVIATIONS = [21.29, 66.83, 48.58, 63.77, 77.49, 104.00, 116.11, 178.11, 174.55]

TINKERPOP_TIMES = [14517, 15447, 15852, 16282, 16673, 17044, 17389, 18462, 18814]
TINKERPOP_STANDARD_DEVIATIONS = [1510.05, 440.13, 223.45, 116.94, 90.58, 81.42, 103.43, 105.16, 95.60]

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
    pyplot.title('Load Test (writes)')
    pyplot.legend(loc='upper left', shadow=True)
    pyplot.show()
   

if  __name__ =='__main__':
    main() 
