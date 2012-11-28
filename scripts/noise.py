"""
Sophisticated training noise.
"""

from vocabulary import wordmap
from hyperparameters import HYPERPARAMETERS
from common.myrandom import build
import sys

_indexed_weights = None
def indexed_weights():
    
    global _indexed_weights
    if _indexed_weights is not None:
        return _indexed_weights
    print >> sys.stderr, len(wordmap.map), "=?=", HYPERPARAMETERS["VOCABULARY_SIZE"]
    assert len(wordmap.map) == HYPERPARAMETERS["VOCABULARY_SIZE"]
    if HYPERPARAMETERS["NGRAM_FOR_TRAINING_NOISE"] == 0:
        _indexed_weights = [1 for id in range(wordmap.len)]
    elif HYPERPARAMETERS["NGRAM_FOR_TRAINING_NOISE"] == 1:
        from common.json import load
        from common.file import myopen
        ngrams_file = HYPERPARAMETERS["NGRAMS"][(HYPERPARAMETERS["NGRAM_FOR_TRAINING_NOISE"], HYPERPARAMETERS["VOCABULARY_SIZE"])]
        print >> sys.stderr, "Reading ngrams from", ngrams_file, "..."
        from collections import defaultdict
        ngramcnt = defaultdict(int)
        for (ngram, cnt) in load(myopen(ngrams_file)):
            assert len(ngram) == 1
            ngramcnt[ngram[0]] = cnt + HYPERPARAMETERS["TRAINING_NOISE_SMOOTHING_ADDITION"]
        _indexed_weights = [ngramcnt[wordmap.str(id)] for id in range(len(wordmap.map))]
        _indexed_weights = build(_indexed_weights)
    else: assert 0
    return _indexed_weights
