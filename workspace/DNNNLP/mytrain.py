
HYPERPARAMETERS={}
HYPERPARAMETERS["MINIBATCH SIZE"]=10


def corrupt_examples(self, correct_sequences):
    noise_sequences = []
    weights = []
    for e in correct_sequences:
        noise_sequence, weight = self.corrupt_example(e)
        noise_sequences.append(noise_sequence)
        weights.append(weight)
    return noise_sequences, weights

def corrupt_example(self, e):
    """
    Return a corrupted version of example e, plus the weight of this example.
    """
    import random
    import copy
    e = copy.copy(e)
    last = e[-1]
    cnt = 0
    while e[-1] == last:
        e[-1] = random.randint(0, self.parameters.vocab_size-1)
        pr = 1./self.parameters.vocab_size
        cnt += 1
        # Backoff to 0gram smoothing if we fail 10 times to get noise.
        if cnt > 10: e[-1] = random.randint(0, self.parameters.vocab_size-1)
    weight = 1./pr
    return e, weight


def train(self,correct_sequences):

	noise_sequences, weights = corrupt_examples(correct_sequences)
	# All weights must be the same, if we first multiply by the learning rate
	for w in weights: assert w == weights[0]

	r = graph.train(self.embeds(correct_sequences), self.embeds(noise_sequences), learning_rate * weights[0])
	(dcorrect_inputss, dnoise_inputss, losss, unpenalized_losss, l1penaltys, correct_scores, noise_scores) = r

#ebatch： 句子序列
epoch=1
while 1:
    logging.info("STARTING EPOCH #%d" % epoch)
    for ebatch in get_train_minibatch:
        cnt += len(ebatch)
    #    print [wordmap.str(id) for id in e]
        m.train(ebatch)

        #validate(cnt)
        if cnt % (int(1000./HYPERPARAMETERS["MINIBATCH SIZE"])*HYPERPARAMETERS["MINIBATCH SIZE"]) == 0:
            logging.info("Finished training step %d (epoch %d)" % (cnt, epoch))
#                print ("Finished training step %d (epoch %d)" % (cnt, epoch))
        
    get_train_minibatch = examples.TrainingMinibatchStream()
    epoch += 1
