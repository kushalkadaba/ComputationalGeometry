import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class DelaunayTriangulation{
	private ArrayList<DelVertex> vertices;
	private int outputStep;
	BufferedWriter bw;
	private static boolean test = false;
	public void readFile(){
		outputStep = 1;
		vertices = new ArrayList<DelVertex>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File("input.txt")));

			String line = null;
			while((line = br.readLine())!=null){
				double x = Double.parseDouble(line.split(" ")[0]);
				double y = Double.parseDouble(line.split(" ")[1]);
				DelVertex v = new DelVertex(x,y);
				insertIntoList(v,0,vertices.size());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void insertIntoList(DelVertex v, int start, int end) {
		int middle = (start+end)/2;
		if(start > end || start >= vertices.size())
			vertices.add(start, v);
		else if(vertices.get(middle).compareTo(v) > 0){
			insertIntoList(v, start, middle-1);
		}
		else if(vertices.get(middle).compareTo(v) < 0){
			insertIntoList(v, middle+1, end);
		}
		else{
			vertices.add(middle,v);
		}
	}
	public void divideAndMerge(){
		LinkedList<DelVertex> ch = divide(0,vertices.size());
		writeToFile(outputStep++, ch);
	}
	private void writeToFile(int step, LinkedList<DelVertex> ch) {
		if(test )
			return;
		try {
			bw = new BufferedWriter(new FileWriter(new File(
"C:\\Users\\Kushal\\Desktop\\Spring 2015\\Computational Geometry\\project\\processing-2.2.1\\DisplayFile\\output2\\output"+step+".txt")));
			for(int i =0;i<vertices.size();i++){
				bw.write("Point "+vertices.get(i).v.x+" "+vertices.get(i).v.y+"\n");
				bw.write("Ellipse "+vertices.get(i).v.x+" "+vertices.get(i).v.y+" 3 3"+"\n");
				LinkedList<DelVertex> adjacencyList = vertices.get(i).adjacencyList;
				Node<DelVertex> head = adjacencyList.getHead();
				Node<DelVertex> temp = adjacencyList.getHead();
				if(head == null)continue;
				bw.write("Point "+head.node.v.x+" "+head.node.v.y+"\n");
				bw.write("Ellipse "+head.node.v.x+" "+head.node.v.y+" 3 3"+"\n");
				bw.write("Line "+vertices.get(i).v.x+" "+vertices.get(i).v.y
						+" "+head.node.v.x+" "+head.node.v.y+"\n");
				temp = temp.next;
				while(temp != head){
					bw.write("Point "+temp.node.v.x+" "+temp.node.v.y+"\n");
					bw.write("Ellipse "+temp.node.v.x+" "+temp.node.v.y+" 3 3"+"\n");
					bw.write("Line "+vertices.get(i).v.x+" "+vertices.get(i).v.y
							+" "+temp.node.v.x+" "+temp.node.v.y+"\n");

					temp = temp.next;
				}
			}
			Node<DelVertex> head = ch.getHead();
			Node<DelVertex> temp = ch.getHead();
			if(head != null){
				temp = temp.next;
				while(temp != head){
					//bw.write("Point "+temp.node.v.x+" "+temp.node.v.y+"\n");
					//bw.write("Ellipse "+temp.node.v.x+" "+temp.node.v.y+" 3 3"+"\n");
					bw.write("Line "+temp.prev.node.v.x+" "+temp.prev.node.v.y
							+" "+temp.node.v.x+" "+temp.node.v.y+"\n");
					temp = temp.next;
				}
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void writeToFile(int step){
		try {
			if(test )
				return;
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
"C:\\Users\\Kushal\\Desktop\\Spring 2015\\Computational Geometry\\project\\processing-2.2.1\\DisplayFile\\output2\\output"+step+".txt")));
			for(int i =0;i<vertices.size();i++){
				bw.write("Point "+vertices.get(i).v.x+" "+vertices.get(i).v.y+"\n");
				bw.write("Ellipse "+vertices.get(i).v.x+" "+vertices.get(i).v.y+" 3 3"+"\n");
				LinkedList<DelVertex> adjacencyList = vertices.get(i).adjacencyList;
				Node<DelVertex> head = adjacencyList.getHead();
				Node<DelVertex> temp = adjacencyList.getHead();
				if(head == null)continue;
				bw.write("Point "+head.node.v.x+" "+head.node.v.y+"\n");
				bw.write("Ellipse "+head.node.v.x+" "+head.node.v.y+" 3 3"+"\n");
				bw.write("Line "+vertices.get(i).v.x+" "+vertices.get(i).v.y
						+" "+head.node.v.x+" "+head.node.v.y+"\n");
				temp = temp.next;
				while(temp != head){
					bw.write("Point "+temp.node.v.x+" "+temp.node.v.y+"\n");
					bw.write("Ellipse "+temp.node.v.x+" "+temp.node.v.y+" 3 3"+"\n");
					bw.write("Line "+vertices.get(i).v.x+" "+vertices.get(i).v.y
							+" "+temp.node.v.x+" "+temp.node.v.y+"\n");

					temp = temp.next;
				}
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public LinkedList<DelVertex> divide(int start, int end){
		if(end - start <= 3){
			LinkedList<DelVertex> ch = new LinkedList<DelVertex>();
			//make convex hull and add in adjacency list.
			for(int i = start; i<end; i++){
				if(end - start == 3 && i == end-1){
					Node<DelVertex> beforeNode = (Node<DelVertex>) 
							ch.getHead().node.findPos(vertices.get(i));
					while(ch.getCurrent().node.compareTo(beforeNode.node)!=0){
						ch.setCurrent(ch.getNext());
					}
					Node<DelVertex> v = new Node<DelVertex>();
					v.setNode(vertices.get(i));
					ch.getCurrent().addPrev(v);
				}else{
					ch.add(vertices.get(i));
				}
				for(int j = start; j<i; j++){
					DelaunayTriangulation.insertIntoAdjacency(vertices.get(j), 
							vertices.get(i));
				}
			}
			writeToFile(outputStep++);
			return ch;
		}
		int middle = (start+end)/2;
		LinkedList<DelVertex> chL = divide(start,middle);
		LinkedList<DelVertex> chR = divide(middle,end);
		Line bottomTangent = lowerHull(chL,chR);
		Line upperTangent = upperHull(chL,chR);
		LinkedList<DelVertex> ch  = convexHull(chL,chR,bottomTangent,upperTangent);
		merge(start,middle,end,bottomTangent,upperTangent);
		return ch;
	}

	private void merge(int start, int middle, int end, Line bottomTangent,
			Line upperTangent) {
		DelVertex L = bottomTangent.start;
		DelVertex R = bottomTangent.end;
		while(!upperTangent.compareTo(bottomTangent)){
			boolean A =false, B=false;
			insertIntoAdjacency(L,R);
			DelVertex R1 = R.pred(L);
			if(R1.isLeftof(new Line(L,R))){
				DelVertex R2 = R.pred(R1);
				while(DelVertex.inCircleTest(R1, L, R, R2)){
					deleteFromAdjacency(R,R1);
					R1= R2;
					R2 = R.pred(R1);
				}
			}else{
				A = true;
			}
			DelVertex L1 = L.succ(R);
			if(L1.isRightof(new Line(R,L))){
				DelVertex L2 = L.succ(L1);
				while(DelVertex.inCircleTest(L, R, L1, L2)){
					DelaunayTriangulation.deleteFromAdjacency(L, L1);
					L1 = L2;
					L2 = L.succ(L1);
				}
			}
			else{
				B = true;
			}
			if(A){
				L = L1;
			}else{
				if(B){
					R = R1;
				}
				else{
					if(DelVertex.outsideCircleTest(L, R, R1, L1)){
						R = R1;
					}else{
						L = L1;
					}
				}
			}
			bottomTangent = new Line(L,R);
			writeToFile(outputStep++);
		}
		insertIntoAdjacency(L, R);
	}
	static void deleteFromAdjacency(DelVertex r, DelVertex r1) {
		r.adjacencyList.delete(r1);
		r1.adjacencyList.delete(r);
	}
	static void insertIntoAdjacency(DelVertex l, DelVertex r) {
		if(l==r)
			return;
		if(l.adjacencyList.contains(r))
			return;
		if(l.adjacencyList.isEmpty())
		{
			l.adjacencyList.add(r);
		}
		else{
			Node<DelVertex> beforeNode = (Node<DelVertex>) l.findPos(r);
			l.adjacencyList.add(r, beforeNode);
			/*if(afterNode == l.adjacencyList.getHead()){
				if(r.isLeftof(new Line(l,afterNode.node))){
					l.adjacencyList.getHead().addPrev(new Node<DelVertex>());
					l.adjacencyList.getHead().prev.node = r;
					l.adjacencyList.setHead(l.adjacencyList.getHead().prev);
				}
				else{
					l.adjacencyList.add(r,afterNode);
				}
			}else{
				l.adjacencyList.add(r,afterNode);
			}*/
		}
		if(r.adjacencyList.isEmpty())
		{
			r.adjacencyList.add(l);
		}else{
			Node<DelVertex> beforeNode = (Node<DelVertex>) r.findPos(l);
			r.adjacencyList.add(l,beforeNode);
			/*if(afterNode == r.adjacencyList.getHead()){
				if(l.isLeftof(new Line(r,afterNode.node))){
					r.adjacencyList.getHead().addPrev(new Node<DelVertex>());
					r.adjacencyList.getHead().prev.node = l;
					r.adjacencyList.setHead(r.adjacencyList.getHead().prev);
				}
				else{
					r.adjacencyList.add(l,afterNode);
				}
			}else{
				r.adjacencyList.add(l,afterNode);
			}*/
		}
	}
	private Line upperHull(LinkedList<DelVertex> chL, LinkedList<DelVertex> chR) {
		DelVertex X = findRightMost(chL);
		while(chL.getCurrent().node.compareTo(X)!=0)
			chL.setCurrent(chL.getNext());
		DelVertex Y = findLeftMost(chR);
		while(chR.getCurrent().node.compareTo(Y)!=0)
			chR.setCurrent(chR.getNext());
		DelVertex Z = chL.getNext().node;
		DelVertex Z1 = chR.getNext().node;
		DelVertex Z11 = Y.pred(Z1);
		while (true) {
			if (Z.isRightof(new Line(Y, X))) {
				DelVertex temp = Z;
				Z = Z.succ(X);
				X = temp;

			} else {
				if (Z11.isRightof(new Line(Y,X))) {
					DelVertex temp = Z11;
					Z11 = Z11.pred(Y);
					Y = temp;
				} else{
					//insertIntoAdjacency(Y,X);
					return new Line(Y, X);
				}
			}
		}
	}
	private Line lowerHull(LinkedList<DelVertex> chL, LinkedList<DelVertex> chR) {
		DelVertex X = findRightMost(chL);
		while(chL.getCurrent().node.compareTo(X)!=0)
			chL.setCurrent(chL.getNext());
		DelVertex Y = findLeftMost(chR);
		while(chR.getCurrent().node.compareTo(Y)!=0)
			chR.setCurrent(chR.getNext());
		DelVertex Z = chR.getNext().node;
		DelVertex Z1 = chL.getNext().node;
		DelVertex Z11 = X.pred(Z1);
		while (true) {
			if (Z.isRightof(new Line(X, Y))) {
				DelVertex temp = Z;
				Z = Z.succ(Y);
				Y = temp;
			} else {
				if (Z11.isRightof(new Line(X, Y))) {
					DelVertex temp = Z11;
					Z11 = Z11.pred(X);
					X = temp;
				} else{
					//insertIntoAdjacency(Y,X);
					return new Line(X, Y);
				}
			}
		}
	}
	private DelVertex findLeftMost(LinkedList<DelVertex> chR) {
		while(chR.getCurrent().node.v.compareTo(chR.getNext().node.v)> 0 
				|| chR.getCurrent().node.v.compareTo(chR.getPrev().node.v)> 0)
			chR.setCurrent(chR.getNext());
		return chR.getCurrent().node;
	}
	private DelVertex findRightMost(LinkedList<DelVertex> chL) {
		while(chL.getCurrent().node.v.compareTo(chL.getNext().node.v)< 0 
				|| chL.getCurrent().node.v.compareTo(chL.getPrev().node.v)< 0)
			chL.setCurrent(chL.getNext());
		return chL.getCurrent().node;
	}
	private LinkedList<DelVertex> convexHull(LinkedList<DelVertex> chL,
			LinkedList<DelVertex> chR, Line bottomTangent, Line upperTangent) {
		//save the upperTangents end point
		LinkedList<DelVertex> tempChL = new LinkedList<>();
		tempChL.setCurrent(chL.getCurrent());
		while(tempChL.getCurrent().node != upperTangent.end)
			tempChL.setCurrent(tempChL.getNext());

		//connect the bottom
		while(chL.getCurrent().node != bottomTangent.start){
			chL.setCurrent(chL.getNext());
		}
		while(chR.getCurrent().node != bottomTangent.end){
			chR.setCurrent(chR.getNext());
		}
		chL.getCurrent().next = chR.getCurrent();
		chR.getCurrent().prev = chL.getCurrent();

		//connect the top
		while(chL.getCurrent().node != upperTangent.start){
			chL.setCurrent(chL.getNext());
		}
		chL.getCurrent().next = tempChL.getCurrent();
		tempChL.getCurrent().prev = chL.getCurrent();
		return chL;
	}
	public static void main(String[] args){
		DelaunayTriangulation dt = new DelaunayTriangulation();
		dt.readFile();
		dt.divideAndMerge();

	} 
}