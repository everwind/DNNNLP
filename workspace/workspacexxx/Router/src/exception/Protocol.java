package exception;

public interface Protocol {

	// file split sending and receiving
	public static final String DATA_SPLIT = "split tran";

	// split data sending and receiving
	public static final String DATA = "data tran";

	// vertex search
	public static final String VERTEX = "vertex";
	// attribute search 
	public static final String ATTRIBUTE = "attribute";

	// "location xxx.xxx.x.x" (used for vertex location search)
	public static final String LOCATION = "location";

	public static final String FAILDED = "failed";

	public static final String DONE = "done";
	
	// null 
	public static final String NULL = "null";
	
	public static final String QUERY = "query";
	
	// disconnected
	public static final String DISCONN = "disconn";

	// Data prepared, ready for work
	public static final String DATANODE_READY = "datanode ready";

	public static final String LOCALHOST = "127.0.0.1";

	// true - active; false - inactive (used in job tracker for superstep
	// synchronize)
	public static final String WORKER_STATUS = "worker-status";
	public static final String SUPERSTEP = "superstep";
	public static final String JOB_DONE = "job done";
	public static final String JOB_STOP = "job stop";
	public static final String SEED_NODE = "seed";

	public static final int SOCKET_TIMEOUT = 200;

	// thread sleep long time
	public static final int THREAD_SLEEP = 100;

	// thread sleep short time
	public static final int THREAD_SHORT_SLEEP = 50;

	
	public final String HEART_BEAT = "heartbeat";
	// heart beat interval
	public final int BEAT_INTER = 5000;
	
	// search
	//public final String SEARCH = "search";
	
	//public final int MAX_NODE_ID = 40000000;
	
	public final String BLANK = " ";
	
	public final String SPLIT_NAME_PRE = "split-";
	public final String SPLIT_INFO_PRE = "info-";
	
	public final String GRAPH_DATA = "graph";
}
