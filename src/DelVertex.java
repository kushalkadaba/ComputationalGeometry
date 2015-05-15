class DelVertex implements Comparable<DelVertex>{
	Vertex v;
	LinkedList<DelVertex> adjacencyList;
	public DelVertex(double x, double y){
		v = new Vertex(x,y);
		adjacencyList = new LinkedList<DelVertex>();
	}
	@Override
	public int compareTo(DelVertex o) {
		return v.compareTo(o.v);
	}
	public static boolean inCircleTest(DelVertex r1,DelVertex l,DelVertex r, DelVertex r2){
		if(r1.equals(r2)|| l.equals(r2)|| r.equals(r2))
			return false;
		Vertex rV = r.v;
		Vertex r1V = r1.v;
		Vertex r2V = r2.v;
		Vertex lV = l.v;

		double[][] A = {{r1V.x,r1V.y,(Math.pow(r1V.x, 2)+Math.pow(r1V.y, 2)),1},
				{lV.x,lV.y,(Math.pow(lV.x, 2)+Math.pow(lV.y, 2)),1},
				{rV.x,rV.y,(Math.pow(rV.x, 2)+Math.pow(rV.y, 2)),1},
				{r2V.x,r2V.y,(Math.pow(r2V.x, 2)+Math.pow(r2V.y, 2)),1}

		};
		double determinant = det(A);
		if(Math.abs(determinant) < 1E-10)
			determinant = 0;
		//return determinant>0;
		if(determinant <0)//outside the circle
			return false;
		else if (determinant == 0)
			return false;
		else
			return true;
	}
	public static boolean outsideCircleTest(DelVertex r1,DelVertex l,DelVertex r, DelVertex r2){
		if(r1.equals(r2)|| l.equals(r2)|| r.equals(r2))
			return false;
		Vertex rV = r.v;
		Vertex r1V = r1.v;
		Vertex r2V = r2.v;
		Vertex lV = l.v;

		double[][] A = {{r1V.x,r1V.y,(Math.pow(r1V.x, 2)+Math.pow(r1V.y, 2)),1},
				{lV.x,lV.y,(Math.pow(lV.x, 2)+Math.pow(lV.y, 2)),1},
				{rV.x,rV.y,(Math.pow(rV.x, 2)+Math.pow(rV.y, 2)),1},
				{r2V.x,r2V.y,(Math.pow(r2V.x, 2)+Math.pow(r2V.y, 2)),1}

		};
		double determinant = det(A);
		if(Math.abs(determinant) < 1E-10)
			determinant = 0;
		//return determinant>0;
		if(determinant <0)//outside the circle
			return true;
		else if (determinant == 0)
			return false;
		else
			return false;
	}
	/*public static boolean QTEST(DelVertex L, DelVertex R, DelVertex R1, DelVertex L1){

	}*/
	public static double det(double[][] A){
		if(A.length == 1)
			return A[0][0];
		else{
			double sum = 0;
			for(int j = 0; j < A[0].length; j++){
				sum += Math.pow(-1, j)*A[0][j]*det(removeRowColumn(A,0,j));
			}
			return sum;
		}
	}
	private static double[][] removeRowColumn(double[][] a, int i, int j) {
		double[][] splicedArray = new double[a.length-1][];
		int rowN = 0;
		for(int row = 0; row<a.length;row++){
			if(row == i)
				continue;
			splicedArray[rowN] = new double[a.length-1];
			int colN = 0;
			for(int col = 0;col<a[row].length;col++){
				if(col == j)
					continue;
				splicedArray[rowN][colN++] = a[row][col];
			}
			rowN++;
		}
		return splicedArray;
	}
	public boolean isLeftof(Line line){
		double[][] A = {{line.start.v.x,line.start.v.y,1},
				{line.end.v.x,line.end.v.y,1},
				{v.x,v.y,1}};
		double determinant = det(A);
		if(Math.abs(determinant) < 1E-15)
			determinant = 0;
		return determinant > 0;
	}
	public boolean isRightof(Line line){
		double[][] A = {{line.start.v.x,line.start.v.y,1},
				{line.end.v.x,line.end.v.y,1},
				{v.x,v.y,1}};
		double determinant = det(A);
		if(Math.abs(determinant) < 1E-15)
			determinant = 0;
		return determinant < 0;
	}
	public DelVertex pred(DelVertex z1) {
		if(adjacencyList.isEmpty())
			return z1;
		while(adjacencyList.getCurrent().node != z1){
			adjacencyList.setCurrent(adjacencyList.getPrev());
		}
		adjacencyList.setCurrent(adjacencyList.getPrev());
		return adjacencyList.getCurrent().node;
	}
	public DelVertex succ(DelVertex z1) {
		if(adjacencyList.isEmpty())
			return z1;
		while(adjacencyList.getCurrent().node != z1){
			adjacencyList.setCurrent(adjacencyList.getNext());
		}
		adjacencyList.setCurrent(adjacencyList.getNext());
		return adjacencyList.getCurrent().node;
	}
	public Node<DelVertex> findPos(DelVertex r) {
		Line tempLine = new Line(this,r);
		Node<DelVertex> pivot;
		Node<DelVertex> temp = adjacencyList.getHead();
		Node<DelVertex> head = adjacencyList.getHead();
		/*if(!r.isLeftof(new Line(this,temp.node)))
			return adjacencyList.getHead().prev;
		 */

		double min_area = det(tempLine, new Line(this,head.node));
		pivot = head;
		temp = temp.next;
		while(temp != head){
			double check = det(tempLine, new Line(this,temp.node));
			if(check<0){
				if(min_area <0 && min_area > check ){
					pivot = temp;
					min_area = check;
				}
			}else{
				if(min_area < 0){
					pivot = temp;
					min_area = check;
				}
				else if(check < min_area){
					pivot = temp;
					min_area = check;
				}
			}
			temp = temp.next;
		}
		return pivot;

	}
	private double det(Line tempLine, Line line) {
		double x1 = (tempLine.end.v.x-tempLine.start.v.x);
		double y1 = (tempLine.end.v.y-tempLine.start.v.y);
		double x2 = (line.end.v.x-line.start.v.x);
		double y2 = (line.end.v.y-line.start.v.y);
		double dot = x1*x2 + y1*y2;
		double det = x1*y2 - y1*x2;
		double angle = Math.atan2(det, dot);
		return angle;
		/*
		 * return (x1*y2 - x2*y1)/2;
		return (((tempLine.start.v.x - tempLine.end.v.x)
		 * (line.end.v.y - tempLine.end.v.y))
			-((line.end.v.x - tempLine.end.v.x )*
			(tempLine.start.v.y - tempLine.end.v.y)));
		 */
	}
}