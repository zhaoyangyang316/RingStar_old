package RingStarProblem;


import java.io.IOException;
import java.util.*;


public class GeneticAlgorithm {

	private static double[] lambdas = {0.1, 0.3, 0.5, 0.7, 0.9};
	private ArrayList<Cycle> population;
	private int alpha;
	private NodesManager nodesManager ;
	
	public GeneticAlgorithm(String path,int alpha) throws IOException {
		nodesManager = new NodesManager(path);
		this.alpha = alpha;
		population = new ArrayList<Cycle>();
//		
		initializePopulation();
	}
	
	
	
	public static double[] getLambdas() {
		return lambdas;
	}



	public static void setLambdas(double[] lambdas) {
		GeneticAlgorithm.lambdas = lambdas;
	}



	public ArrayList<Cycle> getPopulation() {
		return population;
	}



	public void setPopulation(ArrayList<Cycle> population) {
		this.population = population;
	}

	
//
	public void initializePopulation() throws IOException {
		
		for(int i=0; i<lambdas.length; i++) {
			initializeOneIndividual(lambdas[i], nodesManager.getInstance());
//			break;
		}
	}
	
	public void initializeOneIndividual(double lambda, NodesManager nodesManager) {
		Cycle cycle  = new Cycle(nodesManager,alpha);
		Tuple min = new Tuple();	
		
		while (min.getCost()<0 || cycle.getNodesList().size()<3) {
			min.setCost(Double.MAX_VALUE);
			for(Node n : cycle.getNodesManager().getListNodes()) {
				if(!cycle.getNodesList().contains(n)){
					Tuple t = cycle.heuristique(n, lambda);
					if(t.getCost()<min.getCost()){
						min.setAllValues(t.getN1(), t.getN2(), t.getInsert(), t.getCost());
					}
				}
			}			
			if(((min.getCost()<0)||(cycle.getNodesList().size()<3 )&& min.getCost()!=Double.MAX_VALUE)){
//				System.out.println(min.getInsert());
				cycle.addNodeToCycle(cycle.getNodesList().indexOf(min.getN2()), min.getInsert());
			}
		}		
		cycle.updateAffectation();
		population.add(cycle);
	}
	
	/**
	 * Cette fonction permet d'évaluer la pertinence d'un cycle plus la valeur 
	 * retournée est grande moins la solution est bonne.
	 * @param c
	 * @return
	 */
	public double evaluate(Cycle c){
		double result=0;
		result = c.getCycleCost()+c.getAffectationCost();
		return result;
	}
	
	/**
	 * Effectue un croisement entre deux cycles; le nouveau cycle obtenu est composé de la
	 * moitié des sommets de chacun des cycles spécifiés
	 * @param c1
	 * @param c2
	 * @return
	 */
	public Cycle croisement(Cycle c1,Cycle c2){
		Cycle result = new Cycle(this.nodesManager.getInstance(),this.alpha);
		result.getNodesList().clear();
		
		ArrayList<Node> list = new ArrayList<Node>();
		list.addAll(c1.getNodesList().subList(0, c1.getNodesList().size()/2));
		int i=0;
		int nbreNoeudToInsert = c2.getNodesList().size()/2;
		while(i<c2.getNodesList().size() && nbreNoeudToInsert>0){
			if(!list.contains(c2.getNodesList().get(i))){
				list.add(c2.getNodesList().get(i));
				nbreNoeudToInsert--;
			}
			i++;
		}
		result.getNodesList().addAll(list);
		result.updateAffectation();
		return result;
	}
	
	
	/**
	 * Selectionne les n meilleurs individus de la liste d'individu spécifiée
	 * @param liste
	 * @param n
	 * @return
	 */
	public ArrayList<Cycle> selection(ArrayList<Cycle> liste,int n){
		ArrayList<Cycle> result = new ArrayList<Cycle>();
		//TODO
		Collections.sort(liste, new Comparator<Cycle>(){
			@Override
			public int compare(Cycle o1, Cycle o2) {
				Double eval1 = o1.getCycleCost()+o1.getAffectationCost();
				Double eval2 = o2.getCycleCost()+o2.getAffectationCost();
				return eval1.compareTo(eval2);
			}
		});
		Collections.reverse(liste);//trie par ordre decroissant
		//on recupere les n premier elements
		if(liste.size()>=n){
			List<Cycle> l = liste.subList(0, n);
			result.addAll(l);
		}else{
			result.addAll(liste);
		}
		return result;
	}
	
	public int random(int min, int max) {
		return (int)(Math.random()*(max-min)+min);
	}
	
	/**
	 * Mutation de l'individu spécifié : 1 noeuds du cycle à muter est remplacé par un noeud pris au
	 * hasard dans la liste des neouds
	 * @param cycleToMutate
	 * @param possibleValues
	 * @return
	 */
	public Cycle mutation(Cycle cycleToMutate){
		Cycle result = new Cycle(this.nodesManager.getInstance(),this.alpha);
		result.getNodesList().clear();
		result.getNodesList().addAll(cycleToMutate.getNodesList());
		//TODO
		int nodeNumberToRemove = random(0, result.getNodesList().size()-1);
		int nodeNumberToInsert;
		int nodeNotInCycle = result.getNodesManager().getListNodes().size()-result.getNodesList().size();
		//TODO
		if(nodeNotInCycle > 0) {
			result.getNodesList().remove(nodeNumberToRemove);
			nodeNumberToInsert = random(0,nodeNotInCycle+1);
			int counter =0;
			int i = 0;
			do {
				if(!result.getNodesList().contains(result.getNodesManager().getListNodes().get(i))) {
					counter++;
				}
				i++;
			} while(counter<nodeNumberToInsert);
			
			result.getNodesList().add(nodeNumberToRemove, result.getNodesManager().getListNodes().get(i-1));
				//modifier un noeud du cycle
			
			result.updateAffectation();
		}
		return result;
	}
	
	
	public Cycle algoGenetique(){
		ArrayList<Cycle> tempPopulation = new ArrayList<Cycle>();
		int taillePopulation = 100; 
		int nbreIteration = 100; 
		int nbreMutations = this.population.size();
		int nbreCroisement = this.population.size();
		
		int i = 0;
		while(i<nbreIteration){
			for(Cycle c : this.population){
				if(nbreMutations!=0){
					tempPopulation.add(mutation(population.get(random(0,population.size()))));
					nbreMutations--;
				}
				if(nbreCroisement!=0){
					tempPopulation.add(croisement(population.get(random(0,population.size())),population.get(random(0,population.size()))));
					nbreCroisement--;
				}
			}
			this.population.addAll(tempPopulation);
			this.population = selection(this.population, taillePopulation);
			i++;
		}
		return selection(this.population,1).get(0);
	}
	
	
	
	public ArrayList<Cycle> deleteDuplicates(ArrayList<Cycle> liste){
		ArrayList<Cycle> result  = new ArrayList<Cycle>();
		
//		for(int i=0;i<liste.size();i++){
//			result.add(liste.get(i));
//			for(int j=i+1;j<liste.size();j++){
//				if(!isPresent(liste.get(i),result)){
//					result.add()
//				}
//			}
//		}
		return null;
	}
	
	
	
	//TODO : calcul de la valeur d'un individu
	//pre rapport 
	//heuristique
	//structure de données utilisées
	//choix metaheuristique
	
}
