package matchaNEAT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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

	//Mutators
	public void addLink(double mutationRate, double chanceRecurrent, ArrayList<LinkGene> linkGeneList, int numLoopFind, int numLinkAdd){
		
		Random random = new Random();
		
		NeuronGene[] neuronsArr = neurons.values().toArray(new NeuronGene[0]);
		
		for(int i = 0; i < numLoopFind; i++){
		
			//Randomly select neurons
			NeuronGene NeuronGene1 = neuronsArr[random.nextInt(neuronsArr.length)];
			NeuronGene NeuronGene2;
			
			//Determine whether or not this link will be recurrent
			double isRecurrent = random.nextDouble();
			if(isRecurrent < chanceRecurrent){
				
				NeuronGene2 = NeuronGene1;
				NeuronGene1.setRecurrent(true);
			
			}
			//if not, select another neuron
			else
				NeuronGene2 = neuronsArr[random.nextInt(neuronsArr.length)];
			
			//If NeuronGene1 has a higher tier than NeuronGene2, then swap them
			if(NeuronGene1.getTier() > NeuronGene2.getTier() && !NeuronGene1.equals(NeuronGene2)){
				
				NeuronGene temp = NeuronGene1;
				NeuronGene1 = NeuronGene2;
				NeuronGene2 = temp;
				
			}
			
			LinkGene linkGene = new LinkGene(linkGeneList.size(), NeuronGene1.getID(), NeuronGene2.getID(), 1.0, NeuronGene1.equals(NeuronGene2));
			//Find whether the link already exists
			boolean linkExists = false;
			for(int j = 0; j < linkGeneList.size(); j++)
				if(linkGeneList.get(j).getFrom() == NeuronGene1.getID() && linkGeneList.get(j).getTo() == NeuronGene2.getID()){
					
					linkExists = true;
					linkGene.setGeneNum(linkGeneList.get(j).getGeneNum());
					
				}
			
			//If the generated linkGene already exists
			if(linkExists){
				
				LinkGene[] linksArr = links.values().toArray(new LinkGene[0]);
				//linkExists now corresponds to whether the link is in the genome
				linkExists = false;
				for(int j = 0; j < linksArr.length; j++)
					if(linksArr[j].getFrom() == NeuronGene1.getID() && linksArr[j].getTo() == NeuronGene2.getID())
						linkExists = true;
				
				//if the link isn't in the genome
				if(!linkExists){
					
					links.put(linkGene.getGeneNum(), linkGene);
					break;
					
				}
				//else just continue searching
				
			}
			//otherwise, add the link to the links genome and the linkGeneList
			else{
				
				links.put(linkGene.getGeneNum(), linkGene);
				linkGeneList.add(linkGene);
				break;
				
			}
			
		}
		
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
