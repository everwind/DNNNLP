package test;

import java.io.IOException;
import java.net.InetAddress;

import job.Configuration;
import job.JobConf;
import job.JobTracker;
import job.WorkTracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Work {
	static final Logger logger = LoggerFactory.getLogger(Work.class);

	public static void runJob(JobConf jobConf, String[] args) {
		Configuration config = new Configuration(args);
		try {
			String machineName = InetAddress.getLocalHost().getHostName()
					.toString();
			System.out.println(machineName);

			logger.info("Start " + machineName + " (worker)");
			WorkTracker worker = new WorkTracker(config, jobConf);
			worker.run();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws IOException {
		JobConf jobConf = new JobConf();
		String[] array = new String[1];
		array[0] = "./conf";
		Work.runJob(jobConf, array);

	}
}
