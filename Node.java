package RingStarProblem;


import java.io.Serializable;
import java.util.*;


public class Node implements Serializable {

	private double x;
	private double y;
	private int nodeId;
	private Node affectedNode;
	private ArrayList<Node> nodesDistances;
	
	public Node() {
            this.nodesDistances = new ArrayList<Node>();
	}
	
	public Node(int id, double x, double y) {
            this();
            this.nodeId = id;
            this.x = x;
            this.y = y;
            this.affectedNode = null;
	}
	
	public Node getAffectedNode() {
		return affectedNode;
	}
	public void setAffectedNode(Node affectedNode) {
		this.affectedNode = affectedNode;
	}
	public int getNumber() {
		return nodeId;
	}
	public void setNumber(int number) {
		this.nodeId = number;
	}
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public String toString() {
		String result = this.getNumber()+" "+this.getX()+" "+this.getY();
		return result;
	}
	
	
	
	public ArrayList<Node> getNodesDistances() {
		return nodesDistances;
	}

	public void setNodesDistances(ArrayList<Node> nodesDistances) {
		this.nodesDistances = nodesDistances;
	}

	/**
	 * 
	 * @param n
	 * @param coeff : alpha ou (10-alpha)
	 * @return coeff * distance
	 */
	public int computeDistance(Node n,int coeff) {
		
		double a = Math.pow(Math.abs(this.getX()-n.getX()), 2);
		double b = Math.pow(Math.abs(this.getY()-n.getY()), 2);
		return (int)Math.ceil(coeff* Math.sqrt(a+b));
	}
	
	public int computeDistance(Node n) {
		
		double a = Math.pow(Math.abs(this.getX()-n.getX()), 2);
		double b = Math.pow(Math.abs(this.getY()-n.getY()), 2);
		return (int)Math.ceil(Math.sqrt(a+b));
	}
	
	public void sortNodes(){
		Collections.sort(this.nodesDistances, new Comparator(){
			  
            public int compare(Object o1, Object o2) {
                Node p1 = (Node) o1;
                Node p2 = (Node) o2;
                Integer distanceP1 = computeDistance(p1);
                Integer distanceP2 = computeDistance(p2);
               return distanceP1.compareTo(distanceP2);
            }
 
        });
	}
	
	
}
