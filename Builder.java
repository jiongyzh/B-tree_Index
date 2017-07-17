
public class Builder {
	//build heap files and indexes under different page sizes
	public static void main(String[] args) throws Exception {
		Heap heap = new Heap(4096);
		Index index = new Index(4096);
		long st = System.currentTimeMillis();
		index.buildIndex();

		heap.importData();
		long et = System.currentTimeMillis();
        System.out.println("building time : " + (et - st));
	}
}
