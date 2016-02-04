package matchaNEAT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

public class Breeder {
	
	private int numInputs;
	private int numOutputs;
	private boolean feedForward;
	private ArrayList<Genome> population;
	private ArrayList<Gene> innovations;
	private HashMap<Integer, Species> species;
	private int numSpecies;
	private Fitness fitness;
	private double avgFitness;
	private double adoptionRate;
	private int expSpeciesSize = 20;
	
	public Breeder(int populationSize, int numInputs, int numOutputs, boolean feedForward, Fitness fitness){
		
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		this.feedForward = feedForward;
		this.fitness = fitness;
		avgFitness = 0.0;
		population = new ArrayList<Genome>(populationSize);
		innovations = new ArrayList<Gene>();
		species = new HashMap<Integer, Species>();
		numSpecies = 0;
		adoptionRate = -1.0;
		
		for(int i = 0; i < numInputs; i++)
			innovations.add(new NeuronGene(innovations.size(), -1, -1, innovations.size(), NeuronGene.NeuronType.INPUT, 0.0, 1.0, false));
		for(int i = 0; i < numOutputs; i++)
			innovations.add(new NeuronGene(innovations.size(), -1, -1, innovations.size(), NeuronGene.NeuronType.OUTPUT, Double.MAX_VALUE, 1.0, false));
		
		breedFirstGen(populationSize);
		
	}
	
	public Breeder(int populationSize, int numInputs, int numOutputs, boolean feedForward, Fitness fitness, double adoptionRate){
		
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		this.feedForward = feedForward;
		this.fitness = fitness;
		avgFitness = 0.0;
		population = new ArrayList<Genome>(populationSize);
		innovations = new ArrayList<Gene>();
		species = new HashMap<Integer, Species>();
		numSpecies = 0;
		adoptionRate = Math.min(Math.max(adoptionRate, 0.0), 1.0);
		
		for(int i = 0; i < numInputs; i++)
			innovations.add(new NeuronGene(i + innovations.size(), -1, -1, i + innovations.size(), NeuronGene.NeuronType.INPUT, 0.0, 1.0, false));
		for(int i = 0; i < numOutputs; i++)
			innovations.add(new NeuronGene(i + innovations.size(), -1, -1, i + innovations.size(), NeuronGene.NeuronType.OUTPUT, Double.MAX_VALUE, 1.0, false));
		
		breedFirstGen(populationSize);
		
	}
	
	private void breedFirstGen(int populationSize){
		
		Random random = new Random();
		
		for(int i = 0; i < populationSize; i++){
			
			HashMap<Integer, NeuronGene> neurons = new HashMap<Integer, NeuronGene>();
			HashMap<Integer, LinkGene> links = new HashMap<Integer, LinkGene>();
			
			for(int j = 0; j < numInputs + numOutputs; j++){
				
				NeuronGene put = ((NeuronGene) innovations.get(j)).clone();
				put.setP(Math.max(random.nextGaussian()*0.5 + 1.0, 0.1));
				neurons.put(put.getID(), put);
				
			}
			
			Genome newGenome = new Genome(i, neurons, links, numInputs, numOutputs);
			
			int numLinks = (int) Math.round(random.nextGaussian()*((double)numInputs*numOutputs)/4.0 + ((double)numInputs*numOutputs)/2.0);
			if(!feedForward)
				numLinks = (int) Math.round(random.nextGaussian()*((double)numInputs*numOutputs)/2.0 + ((double)numInputs*numOutputs));
			numLinks = Math.max(1, numLinks);
			numLinks = Math.min(numLinks, numInputs*numOutputs);
			
			for(int j = 0; j < numLinks; j++)
				if(feedForward)
					newGenome.addLinkFeedForward(1.0, innovations, 10*numInputs);
				else
					newGenome.addLink(1.0, 0.0, innovations, 0, 10*numInputs);
			
			population.add(newGenome);
			
		}
		
		fitness();
		speciateFirstGen();
		
		System.out.println("Species: " + numSpecies);
		System.out.println("");
		
	}
	
	private void speciateFirstGen(){
		
		//Select Delta
		double delta = 0.0;
		PriorityQueue<Double> distances = new PriorityQueue<Double>(Collections.reverseOrder());
		for(int i = 0; i < population.size(); i++){
			
			PriorityQueue<Double> queue = new PriorityQueue<Double>();
			
			for(int j = 0; j < population.size(); j++)
				queue.add(population.get(i).getDistance(population.get(j)));
			
			for(int j = 0; j < population.size(); j++)
				if(j == expSpeciesSize){

					distances.add(queue.poll());
					break;
					
				}
				else
					queue.poll();
			
		}
		for(int i = 0; i < population.size(); i++)
			if(i == population.size()/32)
				delta = distances.poll();
			else
				distances.poll();

		//Speciate appropriately
		species.put(0, new Species(population.get(0), 0));
		numSpecies++;
		
		for(int i = 1; i < population.size(); i++){
			
			Species[] values = species.values().toArray(new Species[0]);
			shuffleArray(values);
			for(int j = 0; j < values.length; j++)
				if(values[j].getLeader().getDistance(population.get(i)) < delta){
					
					values[j].addMember(population.get(i));
					break;
					
				}
				else if(j >= values.length - 1){
					
					species.put(numSpecies, new Species(population.get(i), numSpecies));
					numSpecies++;
					break;
					
				}
			
		}
		
		Species[] values = species.values().toArray(new Species[0]);
		for(int i = 0; i < values.length; i++){
			
			values[i].adjustFitness(-1, 1.0, -1, 1.0);
			avgFitness += ((double) values[i].getNumMembers())/((double) population.size())*values[i].getAverageFitness();
			
		}
		for(int i = 0; i < values.length; i++){
			
			values[i].calculateOffspringNum(avgFitness);
			System.out.println("Species: " + values[i].getID() + ", NumMembers: " + values[i].getNumMembers() + ", NumOffspring: " + values[i].getNumOffspring() + ", AvgFitness: " + values[i].getAverageFitness());
			
		}
		
		System.out.println("Delta: " + delta);
		
	}
	
	private void fitness(){
		
		for(int i = 0; i < population.size(); i++){
			
			population.get(i).updatePhenome();
			population.get(i).setFitness(fitness.fitnessFunction(population.get(i).getPhenome()));
			
		}
		
	}

	public static <T> void shuffleArray(T[] array){
		
		Random random = new Random();
		for (int i = array.length - 1; i > 0; i--){
			
	    	int index = random.nextInt(i + 1);
	    	// Simple swap
	    	T a = array[index];
	    	array[index] = array[i];
	    	array[i] = a;
	    	
		}
		
	}
	
}
