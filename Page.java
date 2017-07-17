
public class Page {
	protected int pageSize; 
	protected int recCount = 0; 
	protected int recSize = 0; 
	protected byte[] records; 
	protected String fillers = "";
	
	public Page(int pageSize, byte[] rec) {

		int fillerLen = pageSize - rec.length;
		for(int i = 0; i < fillerLen; i++) {
			this.fillers = fillers + "_";
		}
		this.pageSize = pageSize;
		this.records = rec;

	} 
	
	public Page(int pageSize, byte[] rec, int recCount, int recSize) {

		int fillerLen = pageSize - recCount * recSize;
		for(int i = 0; i < fillerLen; i++) {
			this.fillers = fillers + "_";
		}
		this.pageSize = pageSize;
		this.records = rec;
		this.recCount = recCount;
		this.recSize = recSize;
	} 
	
	public byte[] getPageContent() {

		byte[] pageContent = new byte[this.pageSize];
		int recLen = this.recCount * this.recSize;
		if (recCount == 0) {
			System.arraycopy(records, 0, pageContent, 0, records.length);
			System.arraycopy(fillers.getBytes(), 0, pageContent, records.length, fillers.length());
		} else {
			System.arraycopy(records, 0, pageContent, 0, recLen);
			System.arraycopy(fillers.getBytes(), 0, pageContent, recLen, fillers.length());
		}
		return pageContent;
	}
}
