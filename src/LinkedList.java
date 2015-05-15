class Node<E>{
	E node;
	Node<E> next;
	Node<E> prev;
	public void setNode(E e){
		node = e;
		next = this;
		prev = this;
	}
	public void addNext(Node<E> n){
		n.next = next;
		n.prev = this;
		next = n;
		n.next.prev = n;
	}
	public void addPrev(Node<E> p){
		p.prev = prev;
		prev.next = p;
		p.next = this;
		prev = p;
	}
	/*public Node<E> getNext(){
		return next;
	}
	public Node<E> getPrev(){
		return prev;
	}
	 */
}
class LinkedList<E extends Comparable>{

	private Node<E> head;
	private Node<E> current;

	public LinkedList(){
		head = null;
		current = null;
	}
	public void set(E e){
		current.node = e;
	}
	//add at head
	public void add(E vertex){
		Node<E> v = new Node<E>();
		v.setNode(vertex);
		if(head == null) //first element being added
		{
			head = v;
			current = v;
		}
		else{
			v.next = head;
			v.prev = head.prev;
			head.prev = v;
			v.prev.next = v;
			head = v;
		}
	}
	//deletes the current node and places current to the node previous to this node
	public void delete(E r){
		while(current.node.compareTo(r) != 0){
			current = current.next;
		}
		if(current == current.next) //only one node in the list
		{
			clear();
		}
		else{
			if (head == current)
				head = head.prev;
			current.prev.next = current.next;
			current.next.prev = current.prev;
			current = current.prev;
		}
	}
	public void setCurrent(Node<E> e){
		current = e;
	}
	public void setHead(Node<E> e){
		head = e;
	}
	public void clear(){
		head = null;
		current = null;
	}
	public Node<E> getPrev(){
		return current.prev;
	}
	public Node<E> getNext(){
		return current.next;
	}
	public Node<E> getCurrent(){
		return current;
	}
	public boolean isEmpty() {
		return head == null;
	}
	public boolean contains(E r) {
		if(head == null)
			return false;
		if(head.node.compareTo(r)==0)
			return true;
		Node<E> temp = head.next;
		while(temp != head){
			if(temp.node.compareTo(r) == 0)
				return true;
			temp = temp.next;
		}
		return false;
	}
	public Node<E> getHead() {
		return head;
	}
	public void add(E r, Node<E> afterNode) {
		Node<E> v = new Node<E>();
		v.setNode(r);
		afterNode.addPrev(v);
	}

}