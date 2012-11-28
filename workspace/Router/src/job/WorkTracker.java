package job;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.Protocol;
import utility.FileUtil;
import utility.IOUtil;
import utility.ThreadUtil;



public class WorkTracker{
	final Logger logger = LoggerFactory.getLogger(WorkTracker.class);

	private Configuration config;
	private JobConf jobConf;

	// the number of step
	private int stepCount;

	// how many threads have finished current step
	private int stepNum;

	// thread status (whether the vertices of a split are all inactive)
	private BitSet routerStatus;

	// control the thread
	private boolean working;

	// output directory
	private File outDir;

	public WorkTracker(Configuration config, JobConf jobConf) {
		this.config = config;
		this.jobConf = jobConf;
		// output directory
//		outDir = new File(jobConf.getOutputDir());
//		if (!outDir.exists())
//			outDir.mkdir();
//		else
//			FileUtil.deleteDirectory(outDir, false);
	}

	public synchronized void setSplitStatus(int workerId, boolean status) {
		if (status)
			routerStatus.set(workerId);
		else
			routerStatus.clear(workerId);
	}

	public synchronized void addStepNum() {
		stepNum++;
	}


	public int run() throws Exception {
	
		// 1: Initialize worker status
		working = true;
		stepCount = 0;
		stepNum = 0;
		int routerNum=5;
		routerStatus = new BitSet(routerNum);
		for (int i = 0; i < routerNum; i++)
			routerStatus.set(i);
		// 2: Set up threads for every router
		Set<RouterRunner> runners = new HashSet<RouterRunner>();
		
		int id = 0;
		while (id<routerNum) {
			
			RouterRunner worker = new RouterRunner(id);
			runners.add(worker);
			Thread t = new Thread(worker);
			t.setPriority(Thread.MIN_PRIORITY);
			t.start();
			id++;
		}

		// 3: Connect to master for future instruction
		Socket socket = null;
		while (socket == null) {
			try {
				socket = new Socket(config.HOST, config.JOB_PORT);
			} catch (IOException e) {
				Thread.sleep(Protocol.THREAD_SLEEP);
			}
		}

		// 4: start worker
		logger.info("start worker");
		PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		while (true) {
			String line = br.readLine();
			if (line.startsWith(Protocol.SEED_NODE)) {
				// get seed nodes from master
				//String[] tokens = line.split(Protocol.BLANK);
				System.out.println(line);
			
			} else if (line.startsWith(Protocol.SUPERSTEP)) {
				// receive instruction from master
				String[] tokens = line.split(Protocol.BLANK);
				stepCount = Integer.parseInt(tokens[1]);
				stepNum = 0;
				// start step "stepCount"
				logger.info("superstep " + stepCount + " start");

				while (stepNum < routerNum)
					Thread.sleep(Protocol.THREAD_SLEEP);

				// send message
				
				logger.info("superstep " + stepCount + " done");
		
				if (!routerStatus.isEmpty())
					pw.println(Protocol.WORKER_STATUS + Protocol.BLANK
							+ stepCount + Protocol.BLANK + "true");
				else
					pw.println(Protocol.WORKER_STATUS + Protocol.BLANK
							+ stepCount + Protocol.BLANK + "false");
			} else if (line.startsWith(Protocol.JOB_DONE)) {
				working = false;	
				break;
			}
		}

		// 5: Output result
		//logger.debug("Outputing result to " + outDir);
		
		for(RouterRunner runner : runners)
			runner.close();
		
		pw.println(Protocol.DONE);
		IOUtil.closeWriter(pw);
		IOUtil.closeReader(br);
		IOUtil.closeSocket(socket);

		// work done
		logger.info("Job done!");
		System.out.println("job done");


	
		return 0;
	}

	public class RouterRunner implements Runnable {
		private int id;
		public RouterRunner( int id) {
			this.id=id;
		}

		public void close() {
			// TODO Auto-generated method stub
			
		}
		public void run() {
			int currentStep = 0;
			while (working) {
				if (stepCount != currentStep) {
					currentStep = stepCount;	
					System.out.println("router "+this.id+": runs at "+currentStep);
					addStepNum();
				} else {
					ThreadUtil.sleep(Protocol.THREAD_SLEEP);
				}
			}
		}

	
	}

	
}
