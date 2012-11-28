package job;
import java.io.*;
import java.net.*;
import java.util.BitSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import utility.IOUtil;
import exception.Protocol;





public class JobTracker {
	final Logger logger = LoggerFactory.getLogger(JobTracker.class);
	
	// the number of current step
	private int stepCount;

	// How many workers finish step 'stepCount';
	private int stepNum;

	private int workerNum;
	// indicate the active status of each worker
	private BitSet workerStatus;
	// connected worker number
	private int conWorkerNum;
	// how many threads that output result;
	private int outputNum;
	
	private Configuration config;

	private JobConf jobConf;
	public JobTracker(Configuration config, JobConf jobConf) {
		this.config = config;
		this.jobConf = jobConf;
	}
	public synchronized void addStepNum() {
		stepNum++;
	}
	public synchronized void setWorkerStatus(int workerId, boolean status) {
		if (status)
			workerStatus.set(workerId);
		else
			workerStatus.clear(workerId);
	}
	public synchronized void addOutputNum() {
		outputNum++;
	}
	public int run() throws Exception {
		System.out.println("jobTracker run..............");
		try {
			// Tracking job
			// 1: initialization	
			stepCount = 0;
			stepNum = 0;		
			workerStatus = new BitSet(workerNum);
			workerNum = config.getWorkerNum();
			System.out.println("worker num:"+workerNum);
			for (int i = 0; i < workerNum; i++)
				workerStatus.set(i);

			// 2: set up job runner for each worker
			try {
				ServerSocket listener = new ServerSocket(config.JOB_PORT);
				conWorkerNum = 0;
				for (; conWorkerNum < workerNum; conWorkerNum++) {
					Socket socket = listener.accept();
					JobRunner jober = new JobRunner(socket, conWorkerNum);
					Thread t = new Thread(jober);
					t.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		
			// 3: start working
			logger.info("Start working");
			logger.info(Protocol.SUPERSTEP + " 1 start");
			stepNum = 0;
			stepCount = 1;
			
			while (true) {
				if (stepNum >= config.getWorkerNum()) {
					logger.info(Protocol.SUPERSTEP + " " + stepCount + " done");
					if (!workerStatus.isEmpty()) {
						// start next step
						// System.out.println(workerStatus.toString());
						stepNum = 0;
						stepCount++;
						System.out.println(Protocol.SUPERSTEP + " " + stepCount
								+ " start");
						logger.info(Protocol.SUPERSTEP + " " + stepCount
								+ " start");
					} else
						break;

				} else {
					Thread.sleep(Protocol.THREAD_SHORT_SLEEP);
				}
			}

			// 4: output result
			logger.debug("Outputing results");
			while (outputNum < workerNum)
				Thread.sleep(Protocol.THREAD_SLEEP);

			// 5: Finish the job
	
			// monitor.close();

			logger.info("Job done!");
			Thread.sleep(Protocol.THREAD_SLEEP);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public class JobRunner implements Runnable {
		private int id;
		private Socket server;
		private String location;


		public JobRunner(Socket server, int id) {
			this.server = server;
			this.id = id;			
			this.location = server.getInetAddress().toString().substring(1);
			logger.debug("start worker " + location);
				
		}

		/**
		 * If a worker finished its work, it first send "superstep x" and send
		 * worker status
		 */
		public void run() {
			try {
				BufferedReader bf = new BufferedReader(new InputStreamReader(
						server.getInputStream()));
				PrintWriter pw = new PrintWriter(server.getOutputStream(), true);

				if (stepCount == 0) {		
					addStepNum();
				}

				while (stepCount < 1)
					Thread.sleep(Protocol.THREAD_SHORT_SLEEP);

				// start step 1 (notify workers to work)
				pw.println(Protocol.SUPERSTEP + Protocol.BLANK + stepCount);
				boolean revFlag = false;
				int currentStep = 1;
				while (true) {
					if (!revFlag) {
						// this worker just finished a superstep
						String request = bf.readLine();					
						if (request.startsWith(Protocol.WORKER_STATUS)) {
							String[] tokens = request.split(Protocol.BLANK);
							currentStep = Integer.parseInt(tokens[1]);
							setWorkerStatus(id,
									Boolean.parseBoolean(tokens[2]));
							addStepNum();
							revFlag = true;
						}
						// Thread.sleep(Protocol.THREAD_SLEEP);
					} else {
						if (!workerStatus.isEmpty()) {
							if(stepCount>jobConf.getMaxStep()){
								// kill all the workers
								pw.println(Protocol.JOB_DONE);
								setWorkerStatus(id,false);
								addStepNum();
								break;
							}
							if (stepCount != currentStep) {
								revFlag = false;
								pw.println(Protocol.SUPERSTEP + Protocol.BLANK
										+ stepCount);
							} else
								Thread.sleep(Protocol.THREAD_SHORT_SLEEP);
						} else {
							if (stepNum >= config.getWorkerNum()) {
								pw.println(Protocol.JOB_DONE);
								break;
							} else
								Thread.sleep(Protocol.THREAD_SHORT_SLEEP);
						}
					}
				}

				// wait for worker output finish
				String request = bf.readLine();
				if (request.equals(Protocol.DONE))
					System.out.println("add outputnum");
					addOutputNum();

				IOUtil.closeWriter(pw);
				IOUtil.closeReader(bf);
				IOUtil.closeSocket(server);
				System.out.println("server done");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String args[]) throws IOException {

		
	}
}
