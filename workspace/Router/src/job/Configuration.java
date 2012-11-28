package job;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import exception.ConnectionLostException;




public class Configuration {

	// master
	public String HOST;
	
	// worker's info
	private List<String> workerList;
	
	// the directory that stores temporarily files
	public String SEDGE_TEMP_DIR;
	public String SEDGE_DATA_DIR;
	
	// max node id, default is 100000000
	public int MAX_VTX_ID;
	
	// max duplicates per vertex, default is 3
	public int MAX_VTX_DUPS;
	
	// checkpoint interval (number of supersteps) for recovery
	public int CHECK_INTER;
	public String CHECK_REPOS;
	
	// default data transmission port, default is 14030
	public int DATA_PORT;

	// default working port, default is 14070
	public int JOB_PORT;

	// default message port, default is 14090
	public int MSG_PORT;

	// search port, default is 14050
	public int SEARCH_PORT;
	
	// monitoring port, default is 45303
	public int MONITOR_PORT;
	
	// line separator, default is ","
	public String LINE_SPTOR;
	

	public Configuration(String[] args) {
		int last = args.length - 1;
		
		// load configuration properties
		Properties p = new Properties();
		try {
			InputStream inputStream = new FileInputStream(args[last] + "/config.properties");
			p.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String param = null;
		if((param = p.getProperty("HOST")) != null)
			HOST = param;
		else
			throw new ConnectionLostException("No Host Exception");
		
		if((param = p.getProperty("SEDGE_TEMP_DIR", "temp")) != null)
			SEDGE_TEMP_DIR = param;
		if((param = p.getProperty("SEDGE_DATA_DIR", "temp/data")) != null)
			SEDGE_DATA_DIR = param;
		if((param = p.getProperty("MAX_VTX_ID", "100000000")) != null)
			MAX_VTX_ID = Integer.parseInt(param);
		if((param = p.getProperty("MAX_VTX_DUPS", "3")) != null)
			MAX_VTX_DUPS = Integer.parseInt(param);
		if((param = p.getProperty("DATA_PORT", "14030")) != null)
			DATA_PORT = Integer.parseInt(param);
		if((param = p.getProperty("JOB_PORT", "14070")) != null)
			JOB_PORT = Integer.parseInt(param);
		if((param = p.getProperty("MSG_PORT", "14090")) != null)
			MSG_PORT = Integer.parseInt(param);
		if((param = p.getProperty("SEARCH_PORT", "14050")) != null)
			SEARCH_PORT = Integer.parseInt(param);
		if((param = p.getProperty("MONITOR_PORT", "45303")) != null)
			MONITOR_PORT = Integer.parseInt(param);
		if((param = p.getProperty("checkinter")) != null)
			CHECK_INTER = Integer.parseInt(param);
		if((param = p.getProperty("checkrepos")) != null)
			CHECK_REPOS = param;
		if((param = p.getProperty("LINE_SPTOR", ",")) != null)
			LINE_SPTOR = param;
			
		
		// load worker list
		workerList = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(args[last] + "/workers"));
			String line = null;
			while((line=br.readLine()) != null) {
				if(line.length() > 0 && !line.startsWith("#")) {
					String worker = line.trim();
					if(workerList.indexOf(worker) < 0)
						workerList.add(worker);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getWorkers() {
		return workerList;
	}

	public int getWorkerNum() {
		return workerList.size();
	}
}
