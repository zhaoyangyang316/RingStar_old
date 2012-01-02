package RingStarProblem;



public class Tuple {
	
	private double cost;
	private Node n1;
	private Node n2;
	private Node insert;
	
	public Tuple() {
		
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public Node getN1() {
		return n1;
	}
	public void setN1(Node n1) {
		this.n1 = n1;
	}
	public Node getN2() {
		return n2;
	}
	public void setN2(Node n2) {
		this.n2 = n2;
	}
	public Node getInsert() {
		return insert;
	}
	public void setInsert(Node insert) {
		this.insert = insert;
	}
	
	public void setAllValues(Node n1, Node n2, Node insert, double inc) {
		this.n1 = n1;
		this.n2 = n2;
		this.insert = insert;
		this.cost = inc;
	}
	
    @Override
	public String toString(){
		return n1.toString()+"\n"+n2.toString()+"\n"+insert.toString()+"\n"+" cost: "+this.cost; 
	}
	
	
	

}
