package RingStarProblem;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class NodesManager implements Serializable {

	private ArrayList<Node> listNodes;
	private String path;

	public NodesManager(String path) throws IOException {
		listNodes = new ArrayList<Node>();
		this.path = path;
		this.readFile(path);
		this.initializeNodesDistances();
	}

	public NodesManager(){
		listNodes = new ArrayList<Node>();
	}

	public NodesManager getInstance(){
                NodesManager n = null;
		if(this.path!=null && this.path.length()>0){
			n = new NodesManager();
			n.getListNodes().addAll(listNodes);
		}else{
			System.out.println("Impossible de creer un NodesManager avant de parser le ficher");
		}
		return n;
	}
	
	public ArrayList<Node> getListNodes() {
		return listNodes;
	}

	public void setListNodes(ArrayList<Node> listNodes) {
		this.listNodes = listNodes;
	}
        
        
	public void addNode(Node node) {
		listNodes.add(node);
	}

        @Override
	public String toString() {
		String result ="";
		for(Node n : listNodes) {
			result+=n.toString()+"\n";
		}
		return result;	
	}

	public double getDistance(int nodeId, int nodeId2){
		return 0.0;
	}

	public double getDistance(Node node, Node node2){
		return 0.0;
	}
	
	public void initializeNodesDistances(){
		for(Node n: this.listNodes){
			n.getNodesDistances().addAll(listNodes);
			n.sortNodes();
		}
		
	}
	
	public void readFile(String path) throws IOException {
		
		FileReader file = new FileReader(new File(path));
		BufferedReader buffer = new BufferedReader(file);
		String line;
		String[] data = new String[3];
		boolean sectionChange = false;
		
		while((line=buffer.readLine())!=null) {
			if(sectionChange) {
				
				StringTokenizer st = new StringTokenizer(line);
				int i=0;
				while(st.hasMoreTokens()) {
					data[i] = st.nextToken();
					i++;
				}
				Node n = new Node(Integer.valueOf(data[0].trim()), Integer.valueOf(data[1].trim()), Integer.valueOf(data[2].trim()));
				this.addNode(n);
			} else {	
				if(line.compareTo("NODE_COORD_SECTION")==0) {
					sectionChange = true;
				}
			}	
		}
	}
		
}
