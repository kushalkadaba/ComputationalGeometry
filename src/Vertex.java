class Vertex implements Comparable<Vertex>{
	double x;
	double y;
	//private Node<Vertex> p;
	public Vertex(double x, double y){
		this.x = x;
		this.y = y;
	}
	public Vertex(Vertex v){
		x = v.x;
		y = v.y;
	}
	@Override
	public int compareTo(Vertex o) {
		double diff =  (x == o.x)? y - o.y: x- o.x;
		return diff<0?-1:(diff>0?1:0);
	}
	public float getAngle(Vertex target) {
		return (float) Math.toDegrees(Math.atan2(target.x - x, target.y - y));
	}
}