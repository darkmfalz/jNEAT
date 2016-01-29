package matchaNEAT;

import java.util.HashMap;

public class Genome {
	
	private int id, numInputs, numOutputs, species;
	private HashMap<Integer, NeuronGene> neurons;
	private HashMap<Integer, LinkGene> links;
	private NeuralNet phenome;
	private double fitness, sharedFitness, offspring;
	
	public Genome(int id, HashMap<Integer, NeuronGene> neurons, HashMap<Integer, LinkGene> links, int numInputs, int numOutputs){
		
		this.id = id;
		this.neurons = neurons;
		this.links = links;
		this.phenome = new NeuralNet(neurons, links);
		this.fitness = 0;
		this.sharedFitness = 0;
		this.offspring = -1;
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		this.species = -1;
		
	}

	//Getters
	public int getID(){
		
		return id;
		
	}
	
	public HashMap<Integer, NeuronGene> getNeurons(){
		
		return neurons;
		
	}
	
	public HashMap<Integer, LinkGene> getLinks(){
		
		return links;
		
	}
	
	public NeuralNet getPhenome(){
		
		return phenome;
		
	}
	
	public double getFitness(){
		
		return fitness;
		
	}
	
	public double getSharedFitness(){
		
		return sharedFitness;
		
	}
	
	public double getOffspring(){
		
		return offspring;
		
	}
	
	public int getSpecies(){
		
		return species;
		
	}
	
	//Setters
	public void setFitness(double fitness){
		
		this.fitness = fitness;
		
	}
	
	public void setSharedFitness(double fitness, double n){
		
		this.fitness = fitness;
		this.sharedFitness = fitness/n;
		
	}
	
	public void setOffspring(double avgFitness){
		
		offspring = sharedFitness/avgFitness;
		
	}
	
	public void setSpecies(int species){
		
		this.species = species;
		
	}

	//Cloners
	public Genome clone(){
		
		return new Genome(id, cloneNeuronGenome(), cloneLinkGenome(), numInputs, numOutputs);
		
	}
	
	public HashMap<Integer, NeuronGene> cloneNeuronGenome(){
		
		HashMap<Integer, NeuronGene> clone = new HashMap<Integer, NeuronGene>();
		
		Integer[] keys = neurons.keySet().toArray(new Integer[0]);
		for(int i = 0; i < keys.length; i++)
			clone.put(keys[i], neurons.get(keys[i]).clone());
		
		return clone;
		
	}
	
	public HashMap<Integer, LinkGene> cloneLinkGenome(){
		
		HashMap<Integer, LinkGene> clone = new HashMap<Integer, LinkGene>();
		
		Integer[] keys = links.keySet().toArray(new Integer[0]);
		for(int i = 0; i < keys.length; i++)
			clone.put(keys[i], links.get(keys[i]).clone());
		
		return clone;
		
	}
	
}
