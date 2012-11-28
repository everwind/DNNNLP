"""
Automatically load the wordmap, if available.
"""

import cPickle
from common.file import myopen
from hyperparameters import HYPERPARAMETERS
import sys

def _wordmap_filename():
    return HYPERPARAMETERS["VOCABULARY_IDMAP_FILE"]

wordmap = None

try:
    wordmap = cPickle.load(myopen(_wordmap_filename()))
    wordmap.str = wordmap.key
except: print sys.exc_info()[0],sys.exc_info()[1] 

def write(wordmap):
    """
    Write the word ID map, passed as a parameter.
    """
    print >> sys.stderr, "Writing word map to %s..." % _wordmap_filename()
    cPickle.dump(wordmap, myopen(_wordmap_filename(), "w"))
