"""
@todo: WRITEME
"""

import theano.config
from theano.compile.sandbox import shared

floatX = theano.config.config.get('scalar.floatX')




class Parameters:
    """
    Parameters used by the L{Model}.
    @todo: Document these
    """


    import vocabulary
    def __init__(self, window_size=5, vocab_size=vocabulary.wordmap.len, embedding_size=100, hidden_size=10, seed=1):
        """
        Initialize L{Model} parameters.
        """

        self.vocab_size     = vocab_size
        self.window_size    = window_size
        self.embedding_size = embedding_size
        if 1==1:
            self.hidden_size    = hidden_size
            self.output_size    = 1

        import numpy
        import hyperparameters

        from pylearn.algorithms.weights import random_weights
        #numpy.random.seed(seed)
        self.embeddings = numpy.asarray((numpy.random.rand(self.vocab_size, embedding_size) - 0.5)*2 * 1.0, dtype=floatX)
        isnormalize=1
        if isnormalize==1: self.normalize(range(self.vocab_size))
        if 1==1:
            self.hidden_weights = shared(numpy.asarray(random_weights(self.input_size, self.hidden_size, scale_by=1.0), dtype=floatX))
            self.output_weights = shared(numpy.asarray(random_weights(self.hidden_size, self.output_size, scale_by=1.0), dtype=floatX))
            self.hidden_biases = shared(numpy.asarray(numpy.zeros((self.hidden_size,)), dtype=floatX))
            self.output_biases = shared(numpy.asarray(numpy.zeros((self.output_size,)), dtype=floatX))

    input_size = self.window_size * self.embedding_size
    
    def normalize(self, indices):
        """
        Normalize such that the l2 norm of the embeddings indices passed in.
        @todo: l1 norm?
        @return: The normalized embeddings
        """
        import numpy
        l2norm = numpy.square(self.embeddings[indices]).sum(axis=1)
        l2norm = numpy.sqrt(l2norm.reshape((len(indices), 1)))

        self.embeddings[indices] /= l2norm
        import math
        self.embeddings[indices] *= math.sqrt(self.embeddings.shape[1])
    
        # TODO: Assert that norm is correct
    #    l2norm = (embeddings * embeddings).sum(axis=1)
    #    print l2norm.shape
    #    print (l2norm == numpy.ones((vocabsize)) * HYPERPARAMETERS["EMBEDDING_SIZE"])
    #    print (l2norm == numpy.ones((vocabsize)) * HYPERPARAMETERS["EMBEDDING_SIZE"]).all()
