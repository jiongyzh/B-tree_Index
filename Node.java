import java.util.LinkedList;
import java.util.Queue;

public class Node {
	
	protected int pageSize;
	protected Node[] pointers;
	protected Node parent;
	protected Node root;
	protected int[] keys;
	protected int[] values;
	protected int nodeNum = 0;
	protected int nodeCount = 0;
	public Node(int pageSize) {
		this.pageSize = pageSize;
		if (pageSize == 4096){
			nodeNum = 201;
		} else {
			nodeNum = 401;
//			nodeNum = 3;

		}
		this.pointers = new Node[nodeNum+1];
		this.keys = new int[nodeNum];
		this.values = new int[nodeNum];
	} 
	

	
	public Node insert(int key, int value){
		int[] tempArrayKeys = new int[nodeNum];
		int[] tempArrayValues = new int[nodeNum];
		if (this.values[0] == 0) {
			this.keys[0] = key;
			this.values[0] = value;
		} else {
			for (int i = 0; i < nodeNum; i++){
				tempArrayKeys[i] = this.keys[i];
				tempArrayValues[i] = this.values[i];
				if (this.values[i] == 0) {
					break;
				}
			}

			for (int i = 0; i < nodeNum; i++){
				if (key < this.keys[i]) {
					if (this.pointers[i] != null){
						this.pointers[i] = this.pointers[i].insert(key, value);
						break;
					} else {
						tempArrayKeys[i] = key;
						tempArrayValues[i] = value;
						for (int j = i + 1; j < nodeNum; j++) {
							tempArrayKeys[j] = this.keys[j-1];
							tempArrayValues[j] = this.values[j-1];
						}
						this.keys = tempArrayKeys;
						this.values = tempArrayValues;
						break;
					}
					
				} else if (this.keys[i] == 0) {
					
					if (this.pointers[i] != null){
						this.pointers[i] = this.pointers[i].insert(key, value);
						break;
					} else {
						this.keys[i] = key;
						this.values[i] = value;
						break;
					}
				}
					
			}

			if (this.keys[nodeNum-1] != 0) {
				this.nodeSplit(this);
			} else {
				
			};
		};
		

		return this;

		
	}
	
	public Node nodeSplit(Node aNode) {
		int[] tempKeys = new int[nodeNum];
		int[] tempVals = new int[nodeNum];
		int midVal = (int)(Math.floor(nodeNum/2));
		int tempKey = aNode.keys[midVal];
		int tempVal = aNode.values[midVal];
		Node leftChild = new Node(pageSize);
		Node rightChild = new Node(pageSize);
		for (int i = 0; i < Math.floor(nodeNum/2); i ++) {
			if (i == midVal-1){
				leftChild.pointers[i+1] = aNode.pointers[i+1];
				if (leftChild.pointers[i+1] != null) {
					leftChild.pointers[i+1].parent = leftChild;
				}
				rightChild.pointers[i+1] = aNode.pointers[i + midVal +2];
				if (leftChild.pointers[i+1] != null) {
					rightChild.pointers[i+1].parent = rightChild;
				}
			}
			leftChild.keys[i] = aNode.keys[i];
			leftChild.values[i] = aNode.values[i];
			leftChild.pointers[i] = aNode.pointers[i];
			if (leftChild.pointers[i] != null) {
				leftChild.pointers[i].parent = leftChild;
			}
			rightChild.keys[i] = aNode.keys[i + midVal +1];
			rightChild.values[i] = aNode.values[i + midVal +1];
			rightChild.pointers[i] = aNode.pointers[i + midVal +1];
			if (leftChild.pointers[i] != null) {
				rightChild.pointers[i].parent = rightChild;
			}
		}
		
		if (aNode.parent == null) {
			leftChild.parent = aNode;
			rightChild.parent = aNode;
			aNode.keys = tempKeys;
			aNode.values = tempVals;
			aNode.keys[0] = tempKey;
			aNode.values[0] = tempVal;
			aNode.pointers[0] = leftChild;
			aNode.pointers[1] = rightChild;
			for (int i = 2; i < nodeNum+1; i++) {
				aNode.pointers[i] = null;
			}
			
		} else {
			leftChild.parent = aNode.parent;
			rightChild.parent = aNode.parent;
			int[] tempArrayKeys = new int[nodeNum];
			int[] tempArrayVals = new int[nodeNum];
			Node[] tempArrayPois = new Node[nodeNum+1];
			for (int i = 0; i < nodeNum; i++){
				if (aNode.parent.values[i] == 0) {
					break;
				}
				tempArrayKeys[i] = aNode.parent.keys[i];
				tempArrayVals[i] = aNode.parent.values[i];
				tempArrayPois[i] = aNode.parent.pointers[i];
			}
			for (int i = 0; i < nodeNum; i++) {
				if (tempKey < aNode.parent.keys[i]) {
					tempArrayKeys[i] = tempKey;
					tempArrayVals[i] = tempVal;
					tempArrayPois[i] = leftChild;
					tempArrayPois[i+1] = rightChild;
					for (int j = i + 1; j < nodeNum ; j++) {
						tempArrayKeys[j] = aNode.parent.keys[j-1];
						tempArrayVals[j] = aNode.parent.values[j-1];
						tempArrayPois[j+1] = aNode.parent.pointers[j];
					}
					break;
				} else if (aNode.parent.keys[i] == 0) {
					tempArrayKeys[i] = tempKey;
					tempArrayVals[i] = tempVal;
					tempArrayPois[i] = leftChild;
					tempArrayPois[i+1] = rightChild;
					break;
				} else {
					
				}
			}
			
			aNode.parent.keys = tempArrayKeys;
			aNode.parent.values = tempArrayVals;
			aNode.parent.pointers = tempArrayPois;

			if (aNode.parent.keys[nodeNum-1] != 0) {
				aNode.parent = aNode.parent.nodeSplit(aNode.parent);
			} else {
				
			}
		}
		return aNode;
	}
	
	public void indexBFS(Node root) {
		Queue<Node> q = new LinkedList<Node>();
		if (root == null)
			return;
		q.add(root);
		int pointerCount = 0;
		while (!q.isEmpty()) {
			Node n = (Node) q.remove();
			int offset = nodeCount * pageSize;
			nodeCount ++;
			RetCntRec retOb = n.getRec(n, pointerCount);
			byte[] records = retOb.getRec();
			pointerCount = retOb.getPointCnt();
			Page page = new Page(this.pageSize, records);
			byte[] pageContent = page.getPageContent();
			Heap.writeToFile("index.txt", offset, pageContent);
			for (int i = 0; i < nodeNum + 1; i++){
				if (n.pointers[i] != null){
					q.add(n.pointers[i]);
				}
			}
		}
	}
	
	
	public RetCntRec getRec(Node n, int pointerCount) {
		String[] strKeys = new String[nodeNum-1];
		String[] strVals = new String[nodeNum-1];
		String[] strPois = new String[nodeNum];
		String comma = ",";
		int recSize = (nodeNum-1) * (6+8) + (nodeNum) * 6 + 2;
		byte[] rec = new byte[recSize];
		RetCntRec ret = new RetCntRec();
		int keyLen = (nodeNum-1) * 6 + 1;
		int valLen = (nodeNum-1) * 8 + 1;
		int keyAndValLen = keyLen + valLen;
		for (int i = 0; i < nodeNum - 1; i++){
			if (n.values[i] == 0) {
				strKeys[i] = "______";
				System.arraycopy(strKeys[i].getBytes(), 0, rec, i* 6, strKeys[i].length());
			} else {
				strKeys[i] = Record.getFixedLenString(Integer.toString(n.keys[i]), 6, '_');
				System.arraycopy(strKeys[i].getBytes(), 0, rec, i* 6, strKeys[i].length());
			}
			if (i == nodeNum - 2){
				System.arraycopy(comma.getBytes(), 0, rec, (nodeNum -1)* 6, comma.length());
			}
		}

		for (int i = 0; i < nodeNum - 1 ; i++){
			if (n.values[i] == 0) {
				strVals[i] = "________";
				System.arraycopy(strVals[i].getBytes(), 0, rec, keyLen + i* 8, strVals[i].length());

			} else {
				strVals[i] = Record.getFixedLenString(Integer.toString(n.values[i]), 8, '_');
				System.arraycopy(strVals[i].getBytes(), 0, rec, keyLen + i* 8, strVals[i].length());

			}
			if (i == nodeNum - 2){
				System.arraycopy(comma.getBytes(), 0, rec, keyLen + (nodeNum -1)* 8, comma.length());
			}
		}
		
		for (int i = 0; i < nodeNum; i++){
			if (n.pointers[i] == null) {
				strPois[i] = "______";
				System.arraycopy(strPois[i].getBytes(), 0, rec, keyAndValLen + i* 6, strPois[i].length());
			} else {
				pointerCount++;
				ret.setPointCnt(pointerCount);
				strPois[i] = Record.getFixedLenString(Integer.toString(pointerCount), 6, '_');
				System.arraycopy(strPois[i].getBytes(), 0, rec, keyAndValLen + i* 6, strPois[i].length());
			}
		}
		
		ret.setRec(rec);
		return ret;
		
	}
	
	//for setting and getting an int and a byte[]
	class RetCntRec {
		protected int pointerCount;
		protected byte[] rec;
		
		public int getPointCnt() {
			return pointerCount;
		}
		
		public void setPointCnt(int pointCnt){
			this.pointerCount = pointCnt;
		}
		
		public byte[] getRec() {
			return this.rec;
		}
		
		public void setRec( byte[] record){
			this.rec = record;
		}
	}

}
