package RingStarProblem;


import java.io.IOException;
import java.util.ArrayList;


public class Main {

	private static int[] alphas = {3,5,7,9};
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		GeneticAlgorithm GA = new GeneticAlgorithm("data/berlin52.tsp",alphas[0]);
		System.out.println(GA.evaluate(GA.algoGenetique()));
//		System.out.println(GA.getPopulation());
		
		
//		Cycle c1 = GA.getPopulation().get(0);
//		Cycle c2 = GA.getPopulation().get(4);
//		
//		Cycle result = GA.mutation(c1);
		
//		System.out.println("*********"+c1);
		//System.out.println(c2);
//		System.out.println("++++++++++++++"+result);
		
	}

}
