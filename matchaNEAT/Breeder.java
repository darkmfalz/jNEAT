package matchaNEAT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Breeder {
	
	private int numInputs;
	private int numOutputs;
	private boolean feedForward;
	private ArrayList<Genome> population;
	private ArrayList<Gene> innovations;
	private Fitness fitness;
	
	public Breeder(int populationSize, int numInputs, int numOutputs, boolean feedForward, Fitness fitness){
		
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		this.feedForward = feedForward;
		this.fitness = fitness;
		population = new ArrayList<Genome>(populationSize);
		innovations = new ArrayList<Gene>();
		
		for(int i = 0; i < numInputs; i++)
			innovations.add(new NeuronGene(i + innovations.size(), -1, -1, i + innovations.size(), NeuronGene.NeuronType.INPUT, 0.0, 1.0, false));
		for(int i = 0; i < numOutputs; i++)
			innovations.add(new NeuronGene(i + innovations.size(), -1, -1, i + innovations.size(), NeuronGene.NeuronType.OUTPUT, Double.MAX_VALUE, 1.0, false));
		
		breedFirstGeneration(populationSize);
		
	}
	
	private void breedFirstGeneration(int populationSize){
		
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
					newGenome.addLinkFeedForward(1.0, innovations, numInputs*numOutputs);
				else
					newGenome.addLink(1.0, 0.0, innovations, 0, numInputs*numOutputs);
			
			population.add(newGenome);
			
		}
		
		
		
	}
	
	private void

}
