package RingStarProblem;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class Cycle {

	// Noeuds appartenant au cycle
	private ArrayList<Node> nodesList;
	// Noeuds n'appartenant pas au cycle
	private NodesManager nodesManager;
	private int alpha;
	private HashMap<Integer, Integer> affectationTable;

	public Cycle(){
		this.nodesList = new ArrayList<Node>();
	}
	
	public Cycle(NodesManager nodesManager,int alpha) {
		this.alpha = alpha;
		this.nodesList = new ArrayList<Node>();
		this.nodesManager = nodesManager;
		if(nodesManager.getListNodes().size() > 0) {
//			this.addNodeFromNodesManagerToCycle(0,this.nodesManager.getListNodes().get(0));
			this.addNodeToCycle(0,this.nodesManager.getListNodes().get(0));
			initializeAffectation();
		} else {
			System.out.println("NodesManager is empty !");
		}
	}

	public ArrayList<Node> getNodesList() {
		return nodesList;
	}
//	public void addNodeFromNodesManagerToCycle(int i, Node n) {
//		nodesList.add(i, n);
//		nodesManager.getListNodes().remove(n);
//	}
	
//	public void removeNodeFromCycleToNodesManager(Node n) {
//		nodesList.remove(n);
//		nodesManager.getListNodes().add(n);
//	}
	
	
	
	public void addNodeToCycle(int i, Node n){
		this.nodesList.add(i,n);
	}
//	
	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public void removeNodeFromCycle(Node n){
		this.nodesList.remove(n);
	}
	
	public NodesManager getNodesManager() {
		return nodesManager;
	}

	public void setNodesManager(NodesManager nodesManager) {
		this.nodesManager = nodesManager;
	}

	public void setNodesList(ArrayList<Node> nodesList) {
		this.nodesList = nodesList;
	}
	
	/**
	 * Calcule le cout d'un cyle : c'est la somme des distances entre les sommets du cycle
	 * @return
	 */
	public double getCycleCost() {
		int coeff = 10-this.alpha;
		double cycleCost = 0;
		
		for(int i=0; i<nodesList.size()-1; i++) {
			cycleCost+=nodesList.get(i).computeDistance(nodesList.get(i+1),coeff);
		}
		if(nodesList.size() >1) {
			cycleCost+=nodesList.get(nodesList.size()-1).computeDistance(nodesList.get(0),coeff);
		}
		return cycleCost;
	}
	
//	
	/**
	 * Calcule les couts d'affectation: c'est la somme des distances entre chaque noeud n'appartenant pas au cycle
	 * et le noeud auquel il est affecté
	 * @return
	 */
	public double getAffectationCost(){
		double result=0;
		for(Node n : nodesManager.getListNodes()){
			if(!nodesList.contains(n))
				result+=n.computeDistance(n.getAffectedNode(), 10-alpha);
		}
		return result;
	}
	
	public Tuple inc(Node insert) {
		
		int coeff = 10-this.alpha;
		double costSub=0;
		double costAdd=0;
		double min = Double.MAX_VALUE;
		double costDiff=0;
		Node n1;
		Node n2;
		Tuple result = new Tuple();
		
		
		for(int i=0; i<nodesList.size(); i++) {
			
			if(i==nodesList.size()-1) {
				n2 = nodesList.get(0);
			} else {
				n2 = nodesList.get(i+1);	
			}
			n1 = nodesList.get(i);
			costSub = n1.computeDistance(n2,coeff);
			costAdd = insert.computeDistance(n1,coeff)+insert.computeDistance(n2,coeff);
			costDiff = costAdd-costSub;
			if(min>costDiff) {
				min = costDiff;
				result.setAllValues(n1, n2, insert, min);
			}
		}
		return result;
	}
	
	public double dec(Node insert) {
		double result = 0;
		double distanceInsert;
		double distanceAffectedNode;
		for(Node n : nodesManager.getListNodes()) {
			if(!this.nodesList.contains(n)){
				distanceInsert = n.computeDistance(insert,alpha);
				distanceAffectedNode = n.computeDistance(n.getAffectedNode(),alpha);
				if(distanceInsert < distanceAffectedNode) {
					result += distanceAffectedNode - distanceInsert;
				}
			}
		}
		//result += insert.computeDistance(insert.getAffectedNode(),alpha); 
		return result;
	}
	
	public Tuple heuristique(Node n,double ration){
		Tuple result=new Tuple();
		Tuple temp = this.inc(n);
		result.setCost(ration*temp.getCost()-(1-ration)*this.dec(n));
		result.setInsert(temp.getInsert());
		result.setN1(temp.getN1());
		result.setN2(temp.getN2());
		return result;
	}
	
        @Override
	public String toString(){
		String result = "";
		for(Node n: nodesList){
			result+=n.toString()+"\n";
		}
		return result;
	}
	
	public void initializeAffectation() {
		
		for(int i=0; i<nodesManager.getListNodes().size(); i++) {
			nodesManager.getListNodes().get(i).setAffectedNode(nodesList.get(0));
		}
	}
	
//	
	/*
	 * Met à jour les affectations apres modification du cycle
	 */
	public void updateAffectation(){
		for(Node n : nodesManager.getListNodes()){
			if(!nodesList.contains(n)){//pour chaque noeud n'appartenant pas au cycle
				for(Node node : n.getNodesDistances()){//il est affecté au noeud le plus proche
					if(nodesList.contains(node)){//se trouvant dans le cycle
						n.setAffectedNode(node);
						break;
					}
				}
				
			}
		}
	}
	
	public static Object copy(Object orig) {
        Object obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }
	
}
