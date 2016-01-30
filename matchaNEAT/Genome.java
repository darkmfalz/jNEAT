package matchaNEAT;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
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
	public void addLink(double mutationRate, double chanceRecurrent, HashMap<Integer, LinkGene> linkGeneList, int numTriesLoopFind, int numTriesLinkAdd){
	
		Random random = new Random();
		
		double willMutate = random.nextDouble();
		if(willMutate < mutationRate)
			addLink(chanceRecurrent, linkGeneList, numTriesLoopFind, numTriesLinkAdd);
	
	}
	
	public void addLink(double chanceRecurrent, HashMap<Integer, LinkGene> linkGeneList, int numTriesLoopFind, int numTriesLinkAdd){
		
		Random random = new Random();
		
		NeuronGene[] neuronsArr = neurons.values().toArray(new NeuronGene[0]);
		
		boolean isRecurrent = (random.nextDouble() < chanceRecurrent);
		int numTries = numTriesLinkAdd;
		if(isRecurrent)
			numTries = numTriesLoopFind;
		
		for(int i = 0; i < numTries; i++){
		
			//Randomly select neurons
			NeuronGene neuronGene1 = neuronsArr[random.nextInt(neuronsArr.length)];
			NeuronGene neuronGene2;
			
			//If this is a recurrent edge and the type of the neuron is not bias or an input
			while(isRecurrent && i < numTries && (neuronGene1.getRecurrent() || neuronGene1.getType() == NeuronGene.NeuronType.BIAS || neuronGene1.getType() == NeuronGene.NeuronType.INPUT)){
				
				neuronGene1 = neuronsArr[random.nextInt(neuronsArr.length)];
				i++;
				
			}
			//If we've exceeded the number of tries, break
			if(i >= numTries - 1)
				break;
				
			//Determine whether or not this link will be recurrent
			if(isRecurrent){
				
				while(i < numTries && (neuronGene1.getRecurrent() || neuronGene1.getType() == NeuronGene.NeuronType.BIAS || neuronGene1.getType() == NeuronGene.NeuronType.INPUT)){
					
					neuronGene1 = neuronsArr[random.nextInt(neuronsArr.length)];
					i++;
					
				}
				//If we've exceeded the number of tries, break
				if(i >= numTries - 1)
					break;
				
				neuronGene2 = neuronGene1;
				neuronGene1.setRecurrent(true);
			
			}
			//if not, select another neuron
			else{

				neuronGene2 = neuronsArr[random.nextInt(neuronsArr.length)];
				
				while(i < numTries && (neuronGene2.equals(neuronGene1) || neuronGene2.getType() == NeuronGene.NeuronType.INPUT || neuronGene2.getType() == NeuronGene.NeuronType.BIAS)){
					
					neuronGene2 = neuronsArr[random.nextInt(neuronsArr.length)];
					i++;
				
				}
				//If we've exceeded the number of tries, break
				if(i >= numTries - 1)
					break;
				
			}
			
			//If NeuronGene1 has a higher tier than NeuronGene2, then swap them
			/*if(neuronGene1.getTier() > neuronGene2.getTier() && !neuronGene1.equals(neuronGene2)){
				
				NeuronGene temp = neuronGene1;
				neuronGene1 = neuronGene2;
				neuronGene2 = temp;
				
			}*/
			
			LinkGene linkGene = new LinkGene(linkGeneList.size(), neuronGene1.getGeneNum(), neuronGene2.getGeneNum(), 1.0, neuronGene1.equals(neuronGene2));
			//Find whether the link already exists
			boolean linkExists = false;
			for(int j = 0; j < linkGeneList.size(); j++)
				if(linkGeneList.get(j).getFrom() == neuronGene1.getGeneNum() && linkGeneList.get(j).getTo() == neuronGene2.getGeneNum()){
					
					linkExists = true;
					linkGene.setGeneNum(linkGeneList.get(j).getGeneNum());
					
				}
			
			//If the generated linkGene already exists
			if(linkExists && !isDuplicateLink(linkGene.getFrom(), linkGene.getTo())){
					
				linkGene.setWeight(random.nextDouble()*2.0 - 1.0);
				links.put(linkGene.getGeneNum(), linkGene);
				break;
			
			}
			//otherwise, add the link to the links genome and the linkGeneList
			else if(!linkExists){
				
				linkGene.setGeneNum(linkGeneList.size());
				linkGeneList.put(linkGeneList.size(), linkGene);
				linkGene = linkGene.clone();
				linkGene.setWeight(random.nextDouble()*2.0 - 1.0);
				links.put(linkGene.getGeneNum(), linkGene);
				break;
				
			}
			
		}
		
	}
	
	public boolean isDuplicateLink(int id1, int id2){
		
		LinkGene[] linksArr = links.values().toArray(new LinkGene[0]);
		boolean linkExists = false;
		for(int j = 0; j < linksArr.length; j++)
			if(linksArr[j].getFrom() == id1 && linksArr[j].getTo() == id2)
				linkExists = true;
		
		return linkExists;
		
	}
	
	public void addNeuron(double mutationRate, HashMap<Integer, NeuronGene> neuronGeneList, int numTriesOldLoopFind){
		
		Random random = new Random();
		
		double willMutate = random.nextDouble();
		if(willMutate < mutationRate)
			addNeuron(neuronGeneList, numTriesOldLoopFind);
	
		
	}
	
	public void addNeuron(HashMap<Integer, NeuronGene> neuronGeneList, int numTriesOldLoopFind){
		
		Random random = new Random();
		
	}
	
	
	public void mutateWeights(double mutationRate, double probNewMutation, double maxPerturbation){
		
		
		
	}
	
	public void mutateP(double mutationRate, double maxPerturbation){
		
		
		
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
	
	//Miscellaneous
	public static double getDistance(Genome gen1, Genome gen2){
		
		
		
	}
	
	public static PriorityQueue<Genome> sortGenesFitness(HashMap<Integer, Genome> genomes){
		
		Comparator<Genome> fitnessComparator = new Comparator<Genome>(){
			@Override
			public int compare(Genome o1, Genome o2){
				return -1*Double.compare(o1.getFitness(), o2.getFitness());
				}
			};
			
		PriorityQueue<Genome> queue = new PriorityQueue<Genome>(fitnessComparator);
		
		Genome[] values = genomes.values().toArray(new Genome[0]);
		for(int i = 0; i < values.length; i++)
			queue.add(values[i]);
		
		return queue;
		
	}
	
	public static PriorityQueue<Genome> sortGenesSharedFitness(HashMap<Integer, Genome> genomes){
		
		Comparator<Genome> sharedFitnessComparator = new Comparator<Genome>(){
			@Override
			public int compare(Genome o1, Genome o2){
				return -1*Double.compare(o1.getSharedFitness(), o2.getSharedFitness());
				}
			};
			
		PriorityQueue<Genome> queue = new PriorityQueue<Genome>(sharedFitnessComparator);
		
		Genome[] values = genomes.values().toArray(new Genome[0]);
		for(int i = 0; i < values.length; i++)
			queue.add(values[i]);
		
		return queue;
		
	}
	
}
