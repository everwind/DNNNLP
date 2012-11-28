package job;



public class JobConf {

	private String name = null; // Job name
	private String inputSrc = null;
	private int splitNum = 0;
	private String outputDir = null;
	private int maxStep=Integer.MAX_VALUE;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInputSrc() {
		return inputSrc;
	}
	public void setInputSrc(String inputSrc) {
		this.inputSrc = inputSrc;
	}
	public int getSplitNum() {
		return splitNum;
	}
	public void setSplitNum(int splitNum) {
		this.splitNum = splitNum;
	}
	public String getOutputDir() {
		return outputDir;
	}
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	public int getMaxStep() {
		return maxStep;
	}
	public void setMaxStep(int maxStep) {
		this.maxStep = maxStep;
	}

	
	
}
