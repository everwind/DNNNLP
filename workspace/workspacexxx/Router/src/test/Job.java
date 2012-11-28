package test;

import java.io.IOException;
import java.net.InetAddress;

import job.Configuration;
import job.JobConf;
import job.JobTracker;
import job.WorkTracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Job {
	static final Logger logger = LoggerFactory.getLogger(Job.class);

	public static void runJob(JobConf jobConf, String[] args) {
		Configuration config = new Configuration(args);
		try {
			String machineName = InetAddress.getLocalHost().getHostName()
					.toString();
			System.out.println(machineName);

			logger.info("Start " + machineName + " (master)");
			JobTracker jobTracker = new JobTracker(config, jobConf);
			jobTracker.run();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws IOException {
		JobConf jobConf = new JobConf();
		jobConf.setMaxStep(8);
		String[] array = new String[1];
		array[0] = "./conf";
		Job.runJob(jobConf, array);

	}
}
