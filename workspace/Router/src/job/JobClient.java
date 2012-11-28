package job;
import java.io.IOException;
import java.net.InetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class JobClient {
	static final Logger logger = LoggerFactory.getLogger(JobClient.class);
	
	public static void runJob(JobConf jobConf, String[] args) {
		Configuration config = new Configuration(args);
		try {
			String machineName = InetAddress.getLocalHost().getHostName()
					.toString();
			System.out.println(machineName);
			if (machineName.startsWith(config.HOST)) {
				logger.info("Start " + machineName + " (master)");
				JobTracker jobTracker = new JobTracker(config, jobConf);
				jobTracker.run();
			} else {
				logger.info("Start " + machineName + " (worker)");
				WorkTracker worker = new WorkTracker(config, jobConf);
				worker.run();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String args[]) throws IOException {
		JobConf jobConf=new JobConf();
		String []array=new String[1];
		array[0]="./conf";
		JobClient.runJob(jobConf,array );
		
	}
}
