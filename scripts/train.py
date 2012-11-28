#!/usr/bin/env python

import sys
import string
from common.file import myopen
from common.stats import stats
from hyperparameters import HYPERPARAMETERS
import miscglobals
import logging

import examples
import state

def validate(cnt):
    import math
    logranks = []
    logging.info("BEGINNING VALIDATION AT TRAINING STEP %d" % cnt)
    logging.info(stats())
    i = 0
    for (i, ve) in enumerate(examples.get_validation_example()):
#        logging.info([wordmap.str(id) for id in ve])
        logranks.append(math.log(m.validate(ve)))
        if (i+1) % 10 == 0:
            logging.info("Training step %d, validating example %d, mean(logrank) = %.2f, stddev(logrank) = %.2f" % (cnt, i+1, numpy.mean(numpy.array(logranks)), numpy.std(numpy.array(logranks))))
            logging.info(stats())
    logging.info("FINAL VALIDATION AT TRAINING STEP %d: mean(logrank) = %.2f, stddev(logrank) = %.2f, cnt = %d" % (cnt, numpy.mean(numpy.array(logranks)), numpy.std(numpy.array(logranks)), i+1))
    logging.info(stats())
#    print "FINAL VALIDATION AT TRAINING STEP %d: mean(logrank) = %.2f, stddev(logrank) = %.2f, cnt = %d" % (cnt, numpy.mean(numpy.array(logranks)), numpy.std(numpy.array(logranks)), i+1)
#    print stats()

if __name__ == "__main__":
   

    import sys
    import noise
    import os
    indexed_weights = noise.indexed_weights()

    
    rundir='rundir'
    newkeystr='dnn'
    logfile = os.path.join(rundir, "log")
    import random, numpy
    random.seed(miscglobals.RANDOMSEED)
    numpy.random.seed(miscglobals.RANDOMSEED)

    import vocabulary   
    import model

    print >> sys.stderr, ("INITIALIZING")

    m = model.Model()
    cnt = 0
    epoch = 1
    get_train_minibatch = examples.TrainingMinibatchStream()
    logging.basicConfig(filename=logfile, filemode="w", level=logging.DEBUG)
    logging.info("INITIALIZING TRAINING STATE")

  

    #validate(0)

#    diagnostics.visualizedebug(cnt, m, rundir)
    while 1:
        logging.info("STARTING EPOCH #%d" % epoch)
        print "STARTING EPOCH #%d" % epoch
        if epoch>10:
            break
        for ebatch in get_train_minibatch:
            cnt += len(ebatch)
            #for e in ebatch:
                #print [wordmap.str(id) for id in e]
                #print e
            m.train(ebatch)
            #validate(cnt)
            if cnt % (int(1000./HYPERPARAMETERS["MINIBATCH SIZE"])*HYPERPARAMETERS["MINIBATCH SIZE"]) == 0:
                logging.info("Finished training step %d (epoch %d)" % (cnt, epoch))
#                print ("Finished training step %d (epoch %d)" % (cnt, epoch))
            if cnt % (int(100000./HYPERPARAMETERS["MINIBATCH SIZE"])*HYPERPARAMETERS["MINIBATCH SIZE"]) == 0:
                if os.path.exists(os.path.join(rundir, "BAD")):
                    logging.info("Detected file: %s\nSTOPPING" % os.path.join(rundir, "BAD"))
                    sys.stderr.write("Detected file: %s\nSTOPPING\n" % os.path.join(rundir, "BAD"))
                    sys.exit(0)
            if cnt % (int(HYPERPARAMETERS["VALIDATE_EVERY"]*1./HYPERPARAMETERS["MINIBATCH SIZE"])*HYPERPARAMETERS["MINIBATCH SIZE"]) == 0:
                state.save(m, cnt, epoch, get_train_minibatch, rundir, newkeystr)                       
                validate(cnt)
        get_train_minibatch = examples.TrainingMinibatchStream()
        epoch += 1
    #output the embedding
    outfile=open(HYPERPARAMETERS["EMBEDDING_FILE"],'w')
    from vocabulary import wordmap
    for i in range(m.parameters.vocab_size):
        outfile.write(wordmap.str(i)+'\t')
        for v in m.parameters.embeddings[i]:
            outfile.write(str(v)+'\t')
        outfile.write('\n')
    outfile.flush()
    outfile.close()
