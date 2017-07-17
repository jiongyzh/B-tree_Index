import java.io.*;
import java.io.FileReader;


public class Index {
	protected int pageSize;
	protected Node root;
	
	
	public Index(int pageSize) {
		this.pageSize = pageSize;
	} 
	
	public void buildIndex() {
		root = new Node(this.pageSize);
		String line = "";
		int lineCount = 0;
		int recCount = 0;

		try (BufferedReader br = new BufferedReader(new FileReader("Pedestrian_volume__updated_monthly.csv"))) {

            while ((line = br.readLine()) != null) {
            	if (lineCount == 0) {
            	} else {
            		String[] values = line.split(",");
    				int hourlyCount = Integer.valueOf(values[9]);
    				recCount++;
    				root = root.insert(hourlyCount, recCount);
            	}
            	lineCount ++;
			}

			br.close();
// write B-Tree to a file
		} catch (Exception e) {
			System.out.println("Error! in makeBtree(), err = " + e.getMessage());
//			System.out.println();
		}
		root.indexBFS(root);
	}

}
