import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;


public class Heap {
	protected int pageSize;
	
	public Heap(int pageSize) {
		this.pageSize = pageSize;
	} 
	
	
	public void importData() throws FileNotFoundException {
		

		String line = "";
    	int recCount = 0;
    	int recCount1 = 0;
    	int pageCount = 0;
    	int lineCount = 0;
    	int recNumber = 0;
    	if (this.pageSize == 4096) {
    		recNumber = 36;
    	} else {
    		recNumber = 72;
//    		recNumber = 2;

    	}
    	int recSize = Record.getRecSize();
    	byte[] records = new byte[recSize * recNumber];
    	
		try (BufferedReader br = new BufferedReader(new FileReader("Pedestrian_volume__updated_monthly.csv"))) {

            while ((line = br.readLine()) != null) {
            	
            	if (lineCount == 0) {
            	} else {
                    Record record = new Record(line);
                    byte[] rec = record.createRecord();

                    if(recCount < recNumber - 1) {
                    	System.arraycopy(rec, 0, records, recSize * recCount, recSize);
                        recCount++;
                        recCount1++;
                    } else {
                    	System.arraycopy(rec, 0, records, recSize * recCount, recSize);
                    	Page page = new Page(this.pageSize, records);
                    	byte[] pageContent = page.getPageContent();
                    	int offset = this.pageSize * pageCount;
                    	writeToFile("data.txt", offset, pageContent);
                    	pageCount ++;
                    	records = new byte[recSize * recNumber];
                    	recCount1++;
                    	recCount = 0;
                    }
            	}
            	lineCount ++;
                
            }
            br.close();
            
            if(recCount == 0) {
            	
            } else {
            	Page page = new Page(this.pageSize, records, recCount, recSize);
            	byte[] pageContent = page.getPageContent();
            	int offset = this.pageSize * pageCount;
            	writeToFile("data.txt", offset, pageContent);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
		System.out.println("count: " + recCount1);
		

	}
	
	public static void writeToFile(String filePath, int position, byte[] record) {
		try { RandomAccessFile fileStore = new RandomAccessFile(filePath, "rw"); 
		fileStore.seek(position);
		fileStore.write(record);
		fileStore.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
