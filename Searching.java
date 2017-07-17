import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class Searching {
	public static void main(String[] args) throws Exception {

//		searchByIndex(args[0],args[1]);
		searchByIndex("250","39");
		System.out.println("Done");
		
	}
	
	public static void searchByIndex(String pageSize, String index) throws Exception {
		
		int idx = 0;
		int pgSize = 0;
		
		try {
			pgSize = Integer.parseInt(pageSize);
		} catch (Exception e) {
			System.out.println("pageSize must be an Integer");
			return;
		}
		
		
		try{
			idx = Integer.parseInt(index);
		} catch (Exception e) {
			System.out.println("Index must be an Integer");
			return;
		}
		
		if (pgSize != 4096 && pgSize != 8192 && pgSize != 250) {
			
			System.out.println("pageSize must be 4096 or 8192");
		} else {
			
	    	System.out.println(
                    "ID"+
    				"\t"+"Date_Time"+
    				"\t"+"        Year"+
    				"\t"+"Month"+
    				"\t"+"Mdate"+
    				"\t"+"Day"+
    				"\t"+"Time"+
    				"\t"+"Sensor_ID"+
    				"\t"+"Sensor_Name"+
    				"\t"+"        Hourly_Count");
	    	
			while(true) {
				int pageId = 0;
				int offset = pgSize * pageId;
				int count = 0;
				
				byte[] aPage = readFromFile("index.txt", offset, pgSize);
				String pageContent = new String(aPage, StandardCharsets.UTF_8);
				String[] pgConts = pageContent.split(",");
				String keys = stripPadding(pgConts[0]);
				String vals = stripPadding(pgConts[1]);
				String pois = stripPadding(pgConts[2]);
				String[] arrayKey = keys.split("_");
				String[] arrayVal = vals.split("_");
				String[] arrayPoi = pois.split("_");
				for (int i = 0; i < arrayKey.length; i++) {
					int key = Integer.parseInt(arrayKey[i]);
					if (key == idx) {
						heapSearch(arrayVal[i], pgSize);
						count++;
						return;
					} else if (key > idx) {
						if (arrayPoi[i] != "") {
							//keep searching index
							pageId = Integer.parseInt(arrayPoi[i]);
							break;
						} else {
							System.out.println(count +" results found");
							return;
						}
					} else {
						if (i == arrayKey.length - 1) {
							if (arrayPoi[arrayKey.length] != "") {
								//keep searching index
								pageId = Integer.parseInt(arrayPoi[i]);
								break;
							} else {
								System.out.println(count +" results found");
								return;
								
							}
						}
					}
				}
				
			}
		}
	}
		
	public static String stripPadding(String input) throws Exception {
			
		String temp1 = input.replace("__", "_");
		int temp2 = input.length();
		while (temp1.length() < temp2) {
			temp2 = temp1.length();
			temp1 = temp1.replace("__", "_");
		}
           
		return temp1;
			
	}
	
	public static void heapSearch(String value, int pageSize) throws Exception {
		int recNumber = 0;
		int pageID = 0;
		int recID = 0;
		int recSize = 111;
    	if (pageSize == 4096) {
    		recNumber = 36;
    	} else {
//    		recNumber = 72;
    		recNumber = 2;
    	}
    	
    	int valueInt = Integer.parseInt(value);
    	
    	recID = valueInt % recNumber;
    	
    	if (recID == 0) {
    		recID = recNumber;
    		pageID = valueInt/recNumber;
    	} else  {
    		pageID = (int)Math.floor(valueInt/recNumber) + 1;
    	}
    	
    	int offset = (pageID - 1) * pageSize + recSize * (recID - 1);
		
    	byte[] aRecord = readFromFile("data1.txt", offset, recSize);
        
    	String recContent = new String(aRecord, StandardCharsets.UTF_8);
    	
    	String record = stripPadding(recContent);
    	
    	String[] arrayRec = record.split("_");
    	
    	System.out.println(
    			arrayRec[0]+
				"\t"+arrayRec[1]+
				"\t"+arrayRec[2]+
				"\t"+arrayRec[3]+
				"\t"+arrayRec[4]+
				"\t"+arrayRec[5]+
				"\t"+arrayRec[6]+
				"\t"+arrayRec[7]+
				"\t"+"\t"+arrayRec[8]+
				"\t"+arrayRec[9]);
	}
	
	
	public static byte[] readFromFile(String filePath, int position, int recordLen) {
		byte[] records = new byte[recordLen];
		try { RandomAccessFile fileStore = new RandomAccessFile(filePath, "r"); 
		fileStore.seek(position);
		fileStore.readFully(records);
		fileStore.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return records;
	}
	
}