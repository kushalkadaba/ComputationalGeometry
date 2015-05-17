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
	private static boolean test = true;
	public DelaunayTriangulation(){
		try {
			bw = new BufferedWriter(new FileWriter(new File("C:\\Users\\Kushal\\Desktop\\Spring 2015\\Computational Geometry\\project\\processing-2.2.1\\DisplayFile\\output2\\output.txt")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
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
			//show all the points.
			writeToFile(0,vertices.size());
			bw.write("*");
			bw.newLine();
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
	public void divideAndMerge() throws IOException{
		LinkedList<DelVertex> ch;
		try {
			ch = divide(0,vertices.size());
			bw.write("Reload");
			bw.newLine();
			writeToFile(0,vertices.size());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			bw.close();
		}
	}
	private void writePointsToFile(int start, int end) throws IOException {
		for(int i =start;i<end;i++){
			bw.write("Point "+vertices.get(i).v.x+" "+vertices.get(i).v.y);
			bw.newLine();
		}
}
//writes all the vertices from start to end
public void writeToFile(int start, int end) throws IOException{
	for(int i = start;i<end;i++){
		bw.write("Point "+vertices.get(i).v.x+" "+vertices.get(i).v.y);
		bw.newLine();
		LinkedList<DelVertex> adjacencyList = vertices.get(i).adjacencyList;
		Node<DelVertex> head = adjacencyList.getHead();
		Node<DelVertex> temp = adjacencyList.getHead();
		if(head == null)continue;
		bw.write("Stroke 1");
		bw.newLine();
		bw.write("Line "+vertices.get(i).v.x+" "+vertices.get(i).v.y
				+" "+head.node.v.x+" "+head.node.v.y);
		bw.newLine();
		temp = temp.next;
		while(temp != head){
			bw.write("Line "+vertices.get(i).v.x+" "+vertices.get(i).v.y
					+" "+temp.node.v.x+" "+temp.node.v.y+"\n");
			bw.newLine();
			temp = temp.next;
		}
		bw.write("Stroke 3");
		bw.newLine();
	}
}
public LinkedList<DelVertex> divide(int start, int end) throws IOException{
	if(end - start <= 3){
		LinkedList<DelVertex> ch = new LinkedList<DelVertex>();
		//make convex hull and add in adjacency list.
		for(int i = start; i<end; i++){
			if(end - start == 3 && i == end-1){
				Node<DelVertex> v = new Node<DelVertex>();
				v.setNode(vertices.get(i));
				if(v.node.isLeftof(new Line(ch.getHead().node,
						ch.getHead().next.node))){
					ch.add(vertices.get(i));
				}else{
					v.next = ch.getHead().next;
					v.prev = ch.getHead();
					v.next.prev = v;
					ch.getHead().next = v;
				}
			}else{
				ch.add(vertices.get(i));
			}
			for(int j = start; j<i; j++){
				DelaunayTriangulation.insertIntoAdjacency(vertices.get(j), 
						vertices.get(i));
			}
		}
		writeLinesToFile(ch);
		bw.write("*");
		bw.newLine();
		//writeToFile(outputStep++);
		return ch;
	}
	int middle = (start+end)/2;
	/*------colouring of divided points ---------*/
	bw.write("Color 0 0 0");
	bw.newLine();
	writeToFile(0, vertices.size());
	bw.write("Color 48 200 203");
	bw.newLine();
	writeToFile(start, middle);
	bw.write("Color 148 90 203");
	bw.newLine();
	writeToFile(middle,end);
	bw.write("*");
	bw.newLine();
	/*------------------------------------------*/
	bw.write("Color 48 200 203");
	bw.newLine();
	LinkedList<DelVertex> chL = divide(start,middle);
	bw.write("Color 148 90 203");
	bw.newLine();
	LinkedList<DelVertex> chR = divide(middle,end);
	Line bottomTangent = lowerHull(chL,chR);
	Line upperTangent = upperHull(chL,chR);
	/*-----Draw bottom and top tangents--------*/
	bw.write("Color 255 0 255");
	bw.newLine();
	bw.write("Stroke 1");
	bw.newLine();
	bw.write("Line "+bottomTangent.start.v.x+" "+bottomTangent.start.v.y
			+" "+bottomTangent.end.v.x+" "+bottomTangent.end.v.y);
	bw.newLine();
	bw.write("Line "+upperTangent.start.v.x+" "+upperTangent.start.v.y
			+" "+upperTangent.end.v.x+" "+upperTangent.end.v.y);
	bw.newLine();
	bw.write("Stroke 3");
	bw.newLine();
	bw.write("*");
	bw.newLine();
	/*-----------------------------------------*/
	LinkedList<DelVertex> ch  = convexHull(chL,chR,bottomTangent,upperTangent);
	bw.write("Color 0 0 0");
	bw.newLine();
	writeLinesToFile(ch);
	writePointsToFile(start, end);
	bw.write("*");
	bw.newLine();
	merge(start,middle,end,bottomTangent,upperTangent);
	return ch;
}

//Draw the convex hull
private void writeLinesToFile(LinkedList<DelVertex> ch) throws IOException {
	bw.write("Stroke 1");
	bw.newLine();
	Node<DelVertex> temp =ch.getHead().next;
	while(temp!=ch.getHead()){
		bw.write("Line "+temp.prev.node.v.x+" "+temp.prev.node.v.y
				+" "+temp.node.v.x+" "+temp.node.v.y);
		bw.newLine();
		temp = temp.next;
	}
	bw.write("Line "+temp.prev.node.v.x+" "+temp.prev.node.v.y
			+" "+temp.node.v.x+" "+temp.node.v.y);
	bw.newLine();
	bw.write("Stroke 3");
	bw.newLine();
}
private void merge(int start, int middle, int end, Line bottomTangent,
		Line upperTangent) throws IOException {
	DelVertex L = bottomTangent.start;
	DelVertex R = bottomTangent.end;
	while(!upperTangent.compareTo(bottomTangent)){
		boolean A =false, B=false;
		insertIntoAdjacency(L,R);
		/*--------------Draw inserted edge------------------*/
		bw.write("Color 0 255 0");
		bw.newLine();
		bw.write("Stroke 1.5");
		bw.newLine();
		bw.write("Line "+L.v.x+" "+L.v.y+" "+R.v.x+" "+R.v.y);
		bw.newLine();
		bw.write("Stroke 3");
		bw.newLine();
		bw.write("*");
		bw.newLine();
		/*--------------------------------------------------*/
		DelVertex R1 = R.pred(L);
		/*--------------Draw point references---------------*/
		bw.write("Color 0 0 255");
		bw.newLine();
		bw.write("Point "+R.v.x+" "+R.v.y);
		bw.newLine();
		bw.write("Point "+L.v.x+" "+L.v.y);
		bw.newLine();
		bw.write("Color 255 69 0");
		bw.newLine();
		bw.write("Point "+R1.v.x+" "+R1.v.y);
		bw.newLine();
		bw.write("*");
		bw.newLine();
		/*--------------------------------------------------*/
		if(R1.isLeftof(new Line(L,R))){
			DelVertex R2 = R.pred(R1);
			//Vertex center = findCircleCenter(L,R,R1);
			//double radius = findRadius(center,L.v);
			/*---------------------Draw Circle----------------*/
			bw.write("Point "+R2.v.x+" "+R2.v.y);
			bw.newLine();
			//bw.write("Color 255 69 0");
			//bw.newLine();
			//bw.write("Ellipse "+center.x+" "+center.y+" "+radius+" "+radius);
			//bw.newLine();
			/*--------------------------------------------------*/
			while(DelVertex.inCircleTest(R1, L, R, R2)){
				deleteFromAdjacency(R,R1);
				/*-------------Delete Line---------------------*/
				bw.write("Color 255 0 0");
				bw.newLine();
				bw.write("Stroke 2");
				bw.newLine();
				bw.write("Line "+R.v.x+" "+R.v.y+" "+R1.v.x+" "+R1.v.y);
				bw.newLine();
				//bw.write("Stroke 3");
				//bw.newLine();
				bw.write("*");
				bw.newLine();
				//bw.write("Reload");
				//bw.newLine();
				//writeToFile(0, vertices.size());
				bw.write("Color 255 255 255");
				bw.newLine();
				bw.write("Stroke 3");
				bw.newLine();
				bw.write("Line "+R.v.x+" "+R.v.y+" "+R1.v.x+" "+R1.v.y);
				bw.newLine();
				bw.write("Color 0 0 0");
				bw.newLine();
				bw.write("Point "+R1.v.x+" "+R1.v.y);
				bw.newLine();
				bw.write("Color 0 0 255");
				bw.newLine();
				bw.write("Point "+R.v.x+" "+R.v.y);
				bw.newLine();
				/*---------------------------------------------*/
				R1= R2;
				R2 = R.pred(R1);
				/*-------------------Color R1 & R2-------------*/
				bw.write("Color 255 69 0");
				bw.newLine();
				bw.write("Point "+R1.v.x+" "+R1.v.y);
				bw.newLine();
				bw.write("Point "+R2.v.x+" "+R2.v.y);
				bw.newLine();
				bw.write("*");
				bw.newLine();
				/*---------------------------------------------*/
			}
			/*-------------------Decoloring points-------------*/
			if(!(R2.equals(L)||R2.equals(R))){
			bw.write("Color 0 0 0");
			bw.newLine();
			bw.write("Point "+R2.v.x+" "+R2.v.y);
			bw.newLine();
			}
			/*-------------------------------------------------*/			
		}else{
			A = true;
		}
		DelVertex L1 = L.succ(R);
		/*--------------Draw point references---------------*/
		bw.write("Color 0 128 128");
		bw.newLine();
		bw.write("Point "+L1.v.x+" "+L1.v.y);
		bw.newLine();
		bw.write("*");
		bw.newLine();
		/*--------------------------------------------------*/
		if(L1.isRightof(new Line(R,L))){
			DelVertex L2 = L.succ(L1);
			//Vertex center = findCircleCenter(L,R,L1);
			//double radius = findRadius(center,L.v);
			bw.write("Point "+L2.v.x+" "+L2.v.y);
			bw.newLine();
			while(DelVertex.inCircleTest(L, R, L1, L2)){
				DelaunayTriangulation.deleteFromAdjacency(L, L1);
				/*-------------Delete Line---------------------*/
				bw.write("Color 255 0 0");
				bw.newLine();
				bw.write("Stroke 2");
				bw.newLine();
				bw.write("Line "+L.v.x+" "+L.v.y+" "+L1.v.x+" "+L1.v.y);
				bw.newLine();
				bw.write("*");
				bw.newLine();
				//bw.write("Reload");
				//bw.newLine();
				//writeToFile(0, vertices.size());
				bw.write("Color 255 255 255");
				bw.newLine();
				bw.write("Stroke 3");
				bw.newLine();
				bw.write("Line "+L.v.x+" "+L.v.y+" "+L1.v.x+" "+L1.v.y);
				bw.newLine();
				bw.write("Color 0 0 0");
				bw.newLine();
				bw.write("Point "+L1.v.x+" "+L1.v.y);
				bw.newLine();
				bw.write("Color 0 0 255");
				bw.newLine();
				bw.write("Point "+L.v.x+" "+L.v.y);
				bw.newLine();
				/*---------------------------------------------*/
				L1 = L2;
				L2 = L.succ(L1);
				/*------------------Re-Color L1 & L2----------*/
				bw.write("Color 0 128 128");
				bw.newLine();
				bw.write("Point "+L1.v.x+" "+L1.v.y);
				bw.newLine();
				bw.write("Point "+L2.v.x+" "+L2.v.y);
				bw.newLine();
				bw.write("*");
				bw.newLine();
				/*---------------------------------------------*/
			}
			/*-------------------Decoloring points-------------*/
			bw.write("Color 0 0 0");
			bw.newLine();
			bw.write("Point "+L2.v.x+" "+L2.v.y);
			bw.newLine();
			/*-------------------------------------------------*/
		}
		else{
			B = true;
		}
		/*-------------------Decoloring points-------------*/
		bw.write("Color 0 0 0");
		bw.newLine();
		bw.write("Point "+L1.v.x+" "+L1.v.y);
		bw.newLine();
		bw.write("Point "+R1.v.x+" "+R1.v.y);
		bw.newLine();
		bw.write("Point "+L.v.x+" "+L.v.y);
		bw.newLine();
		bw.write("Point "+R.v.x+" "+R.v.y);
		bw.newLine();
		/*-------------------------------------------------*/
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
	}
	insertIntoAdjacency(L, R);
	/*--------------Draw inserted edge------------------*/
	bw.write("Color 0 255 0");
	bw.newLine();
	bw.write("Stroke 1.5");
	bw.newLine();
	bw.write("Line "+L.v.x+" "+L.v.y+" "+R.v.x+" "+R.v.y);
	bw.newLine();
	bw.write("Stroke 3");
	bw.newLine();
	bw.write("*");
	bw.newLine();
	/*--------------------------------------------------*/
}
private double findRadius(Vertex center, Vertex v) {
	double rad = Math.sqrt(Math.pow((v.x-center.x),2)+Math.pow((v.y-center.y),2));
	return rad;
}
private Vertex findCircleCenter(DelVertex l, DelVertex r, DelVertex l1) {
	double ma = (r.v.y-l.v.y)/(r.v.x-l.v.x);
	double mb = (r.v.y-l1.v.y)/(r.v.x-l1.v.x);
	double x = (ma*mb*(l.v.y-l1.v.y)+mb*(l.v.x+r.v.x)-ma*(r.v.x-l1.v.x))/(2*(mb-ma));
	double y = ((2*x-r.v.x+l1.v.x)+r.v.y+l1.v.y)/-2*mb;
	return new Vertex(x,y);
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
	DelVertex Z11 = chR.getPrev().node;//Y.pred(Z1);
	while (true) {
		if (Z.isRightof(new Line(Y, X))) {
			DelVertex temp = Z;
			while(chL.getCurrent().node.compareTo(Z)!=0)
				chL.setCurrent(chL.getNext());
			Z = chL.getNext().node;//Z.succ(X);
			X = temp;

		} else {
			if (Z11.isRightof(new Line(Y,X))) {
				DelVertex temp = Z11;
				while(chR.getCurrent().node.compareTo(Z11)!=0)
					chR.setCurrent(chR.getNext());
				Z11 = chR.getPrev().node;//Z11.pred(Y);
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
	DelVertex Z11 = chL.getPrev().node;//X.pred(Z1);
	while (true) {
		if (Z.isRightof(new Line(X, Y))) {
			DelVertex temp = Z;
			while(chR.getCurrent().node.compareTo(Z)!=0)
				chR.setCurrent(chR.getNext());
			Z = chR.getNext().node;//Z.succ(Y);
			Y = temp;
		} else {
			if (Z11.isRightof(new Line(X, Y))) {
				DelVertex temp = Z11;
				while(chL.getCurrent().node.compareTo(Z11)!=0)
					chL.setCurrent(chL.getNext());
				Z11 = chL.getPrev().node;//Z11.pred(X);
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
	chL.setHead(chL.getCurrent());
	return chL;
}
public static void main(String[] args){
	DelaunayTriangulation dt = new DelaunayTriangulation();
	dt.readFile();
	try {
		dt.divideAndMerge();
	} catch (IOException e) {
		e.printStackTrace();
	}
} 
}