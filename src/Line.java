class Line{
	DelVertex start;
	DelVertex end;

	public Line(DelVertex s, DelVertex e){
		start = s;
		end = e;
		//s.adjacencyList.add(e);
		//e.adjacencyList.add(s);
	}

	public boolean compareTo(Line bottomTangent) {
		if(start.compareTo(bottomTangent.start)==0
				&& end.compareTo(bottomTangent.end) == 0)
			return true;
		else if (start.compareTo(bottomTangent.end)==0
				&& end.compareTo(bottomTangent.start)==0)
			return true;
		else
			return false;
	}
}
