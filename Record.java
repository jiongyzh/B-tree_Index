import java.util.Arrays;

public class Record {
	protected String recID;
	protected String dateTime; 
	protected String year; 
	protected String month; 
	protected String mdate; 
	protected String day; 
	protected String time; 
	protected String sensorID; 
	protected String sensorName; 
	protected String hourlyCounts; 
	
	public Record(String line) {
		String[] items = line.split(",");
		this.recID = items[0].trim();
		this.dateTime = items[1].trim();
		this.year = items[2].trim();
		this.month = items[3].trim();
		this.mdate = items[4].trim();
		this.day = items[5].trim();
		this.time = items[6].trim();
		this.sensorID = items[7].trim();
		this.sensorName = items[8].trim();
		this.hourlyCounts = items[9].trim();
	} 
	
	public byte[] createRecord() {
		String recID = getFixedLenString(this.recID, 8, '_');
		String dateTime = getFixedLenString(this.dateTime, 18, '_');
        String year = getFixedLenString(this.year, 5, '_');
        String month = getFixedLenString(this.month, 10, '_');
        String mdate = getFixedLenString(this.mdate, 3, '_');
        String day = getFixedLenString(this.day, 10, '_');
        String time = getFixedLenString(this.time, 3, '_');
        String sensorID = getFixedLenString(this.sensorID, 3, '_');
        String sensorName = getFixedLenString(this.sensorName, 45, '_');
        String hourlyCounts = getFixedLenString(this.hourlyCounts, 6, '_');
        
        byte[] rec = new byte[getRecSize()];
        int StartOff = 0;
        System.arraycopy(recID.getBytes(), 0, rec, StartOff, recID.length());
        System.arraycopy(dateTime.getBytes(), 0, rec, StartOff = StartOff + recID.length(), dateTime.length());
        System.arraycopy(year.getBytes(), 0, rec, StartOff = StartOff + dateTime.length(), year.length());
        System.arraycopy(month.getBytes(), 0, rec, StartOff = StartOff + year.length(), month.length());
        System.arraycopy(mdate.getBytes(), 0, rec, StartOff = StartOff + month.length(), mdate.length());
        System.arraycopy(day.getBytes(), 0, rec, StartOff = StartOff + mdate.length(), day.length());
        System.arraycopy(time.getBytes(), 0, rec, StartOff = StartOff + day.length(), time.length());
        System.arraycopy(sensorID.getBytes(), 0, rec, StartOff = StartOff + time.length(), sensorID.length());
        System.arraycopy(sensorName.getBytes(), 0, rec, StartOff = StartOff + sensorID.length(), sensorName.length());
        System.arraycopy(hourlyCounts.getBytes(), 0, rec, StartOff = StartOff + sensorName.length(), hourlyCounts.length());
        
        return rec;
	}
	
	public static int getRecSize() {
		return 111;
	}
	
	public static String getFixedLenString(String str, int len, char paddingChar) {
        if (str == null) {
            return null;
        }

        if (str.length() >= len) {
            return str.substring(0, len);
        }

        char[] cs = new char[len];
        str.getChars(0, str.length(), cs, 0);
        Arrays.fill(cs, str.length(), len, paddingChar);
        return new String(cs);
    }
	
	
	
}