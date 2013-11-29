#!/usr/bin/python
#
# K-means clustering using Lloyd's algorithm in pure Python.
# Written by Lars Buitinck. This code is in the public domain.
#
# The main program runs the clustering algorithm on a bunch of text documents
# specified as command-line arguments. These documents are first converted to
# sparse vectors, represented as lists of (index, value) pairs.
 
from collections import defaultdict
from math import sqrt
import random
 
 
def densify(x, n):
    """Convert a sparse vector to a dense one."""
    d = [0] * n
    for i, v in x:
        d[i] = v
    return d
 
 
def dist(x, c):
    """Euclidean distance between sample x and cluster center c.
 
    Inputs: x, a sparse vector
            c, a dense vector
    """
    sqdist = 0.
    for i, v in x:
        sqdist += (v - c[i]) ** 2
    return sqrt(sqdist)
 
 
def mean(xs, l):
    """Mean (as a dense vector) of a set of sparse vectors of length l."""
    c = [0.] * l
    n = 0
    for x in xs:
        for i, v in x:
            c[i] += v
        n += 1
    for i in xrange(l):
		if n == 0:
			n = 0.001
		c[i] /= n
    return c
 
 
def kmeans(k, xs, l, n_iter=10):
    # Initialize from random points.
    centers = [densify(xs[i], l) for i in random.sample(xrange(len(xs)), k)]
    target.write("\n")
    #target.write(centers)
    target.write(''.join(str(elem) for elem in centers))
    cluster = [None] * len(xs)

    for _ in xrange(n_iter):
        for i, x in enumerate(xs):
            cluster[i] = min(xrange(k), key=lambda j: dist(xs[i], centers[j]))
        for j, c in enumerate(centers):
            members = (x for i, x in enumerate(xs) if cluster[i] == j)
            centers[j] = mean(members, l)
 
    return cluster
 
 
if __name__ == '__main__':
    # Cluster a bunch of text documents.
    import re
    import sys
    import os.path
 
    def usage():
        print("usage: %s k docs..." % sys.argv[0])
        print("    The number of documents must be >= k.")
        sys.exit(1)
 
    try:
        k = int(sys.argv[1])
    except ValueError():
        usage()
    #if len(sys.argv) < 1 + k:
    #   usage()

 
    vocab = {}
    xs = []
    file_list = []
    
    #filenames = os.listdir('New_Folder/')
    #print filenames
    args = sys.argv[2:]
    args = os.listdir('xbox/files1')
    if not os.path.isfile("xbox/cluster_train.txt"):
        target = open("xbox/cluster_train.txt", 'w')
    target = open("xbox/cluster_train.txt", 'a')
    #print args
    for a in args:
        x = defaultdict(float)
        with open("xbox/files1/"+a) as f:
            for w in re.findall(r"\w+", f.read()):
                vocab.setdefault(w, len(vocab))
                x[vocab[w]] += 1
        xs.append(x.items())
 
    cluster_ind = kmeans(k, xs, len(vocab))
    clusters = [set() for _ in xrange(k)]
    for i, j in enumerate(cluster_ind):
        clusters[j].add(i)
 
    for j, c in enumerate(clusters):
        print("\ncluster %d:" % j)
        target.write("cluster %d:" % j)
        for i in c:
            print("\t%s" % args[i])
            target.write("%s" % args[i])
            target.write(",")
        target.write("\n")
