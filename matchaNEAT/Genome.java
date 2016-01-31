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
	public void addLink(double mutationRate, double chanceOfLooped, ArrayList<Gene> innovations, int numTriesToFindLoop, int numTriesToAddLink){
		
		Random random = new Random();
		
		if(random.nextDouble() > mutationRate)
			return;
		
		NeuronGene[] neuronsArr = neurons.values().toArray(new NeuronGene[0]);
		
		NeuronGene neuron1 = null;
		NeuronGene neuron2 = null;
		
		boolean recurrent = false;
		
		if(random.nextDouble() < chanceOfLooped){
			
			for(int i = 0; i < numTriesToFindLoop; i++){
				
				NeuronGene temp = neuronsArr[random.nextInt(neuronsArr.length)];
				
				if(!temp.getRecurrent() && temp.getType() != NeuronGene.NeuronType.BIAS && temp.getType() != NeuronGene.NeuronType.INPUT){
					
					neuron1 = temp;
					neuron2 = neuron1;
					neuron1.setRecurrent(true);
					recurrent = true;
					break;
					
				}
				
			}
			
		}
		else{
			
			for(int i = 0; i < numTriesToAddLink; i++){
				
				neuron1 = neuronsArr[random.nextInt(neuronsArr.length)];
				neuron2 = neuronsArr[random.nextInt(neuronsArr.length)];
				
				if(!(isDuplicateLink(neuron1.getID(), neuron2.getID()) || neuron1.getID() == neuron2.getID()))
					break;
				else{
					
					neuron1 = null;
					neuron2 = null;
					
				}
				
			}
			
		}
		
		if(neuron1 == null || neuron2 == null)
			return;
		
		int id = checkInnovation(innovations, neuron1.getID(), neuron2.getID(), true);
		
		if(neuron1.getTier() > neuron2.getTier())
			recurrent = true;
		
		if(id < 0){
			
			LinkGene newLink = new LinkGene(innovations.size(), neuron1.getID(), neuron2.getID(), random.nextDouble()*2.0 - 1.0, recurrent);
			links.put(newLink.getGeneNum(), newLink);
			innovations.add(newLink);
			
		}
		else{
			
			LinkGene newLink = new LinkGene(id, neuron1.getID(), neuron2.getID(), random.nextDouble()*2.0 - 1.0, recurrent);
			links.put(newLink.getGeneNum(), newLink);
			
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
	
	public void addNeuron(double mutationRate, ArrayList<Gene> innovations, int numTriesToFindOldLink){
		
		Random random = new Random();
		
		if(random.nextDouble() > mutationRate)
			return;
		
		LinkGene[] linksArr = links.values().toArray(new LinkGene[0]);
		boolean done = false;
		LinkGene chosenLink = null;
		int sizeThreshold = numInputs + numOutputs + 5;
		
		if(links.size() < sizeThreshold){
			
			for(int i = 0; i < numTriesToFindOldLink; i++){
				
				chosenLink = linksArr[random.nextInt(links.size() - (int)Math.sqrt((double) links.size()))];
				NeuronGene fromNeuron = neurons.get(chosenLink.getFrom());
				
				if(chosenLink.getEnabled() && !chosenLink.getRecurrent() && fromNeuron.getType() != NeuronGene.NeuronType.BIAS){
					
					done = true;
					break;
					
				}
				
			}
			
			if(!done)
				return;
			
		}
		else{
			
			while(!done){
				
				chosenLink = linksArr[random.nextInt(links.size())];
				NeuronGene fromNeuron = neurons.get(chosenLink.getFrom());
				
				if(chosenLink.getEnabled() && !chosenLink.getRecurrent() && fromNeuron.getType() != NeuronGene.NeuronType.BIAS)
					done = true;
				
			}
			
		}
		
		if(chosenLink == null)
			return;
		
		chosenLink.setEnabled(false);
		
		double ogWeight = chosenLink.getWeight();
		NeuronGene from = neurons.get(chosenLink.getFrom());
		NeuronGene to = neurons.get(chosenLink.getTo());
		
		double newTier = Math.min(from.getTier(), to.getTier()) + Math.abs(from.getTier() - to.getTier())/2.0;
		int id = checkInnovation(innovations, from.getID(), to.getID(), false);
		
		if(id >= 0){
			
			int neuronID = ((NeuronGene) innovations.get(id)).getID();
			
			if(isDuplicateNeuron(id))
				id = -1;
			
		}
		
		if(id < 0){
			
			int newNeuronGeneNum = innovations.size();
			int newNeuronID = getNextNeuronID(innovations);
			NeuronGene newNeuron = new NeuronGene(newNeuronGeneNum, from.getID(), to.getID(), newNeuronID, NeuronGene.NeuronType.HIDDEN, newTier, 1.0, false);
			neurons.put(newNeuronID, newNeuron);
			innovations.add(newNeuron);
			
			int geneNumLink1 = innovations.size();
			LinkGene newLink1 = new LinkGene(geneNumLink1, from.getID(), newNeuronID, 1.0, false);
			links.put(geneNumLink1, newLink1);
			innovations.add(newLink1);
			
			int geneNumLink2 = innovations.size();
			LinkGene newLink2 = new LinkGene(geneNumLink2, newNeuronID, to.getID(), ogWeight, false);
			links.put(geneNumLink2, newLink2);
			innovations.add(newLink2);
			
		}
		else{
			
			int newNeuronID = ((NeuronGene) innovations.get(id)).getID();
			int geneNumLink1 = checkInnovation(innovations, from.getID(), newNeuronID, true);
			int geneNumLink2 = checkInnovation(innovations, newNeuronID, to.getID(), true);
			
			if(geneNumLink1 < 0 || geneNumLink2 < 0){
				
				System.out.println("ERROR, Null Edges in Neuron Creation");
				return;
				
			}
			
			LinkGene link1 = new LinkGene(geneNumLink1, from.getID(), newNeuronID, 1.0, false);
			LinkGene link2 = new LinkGene(geneNumLink2, newNeuronID, to.getID(), ogWeight, false);
			
			links.put(geneNumLink1, link1);
			links.put(geneNumLink2, link2);
			
			NeuronGene newNeuron = new NeuronGene(id, from.getID(), to.getID(), newNeuronID, NeuronGene.NeuronType.HIDDEN, newTier, 1.0, false);
			neurons.put(newNeuron.getID(), newNeuron);
			
		}
		
	}
	
	public boolean isDuplicateNeuron(int id){
		
		if(neurons.containsKey(id))
			return true;
		
		return false;
		
	}
	
	public static int getNextNeuronID(ArrayList<Gene> innovations){
		
		int id = 0;
		for(int i = 0; i < innovations.size(); i++)
			if(!innovations.get(i).getIsLink())
				id = Math.max(id, ((NeuronGene) innovations.get(i)).getID());
		
		return id;
		
	}
	
	public static int checkInnovation(ArrayList<Gene> innovations, int neuron1, int neuron2, boolean isLink){
		
		for(int i = 0; i < innovations.size(); i++)
			if(isLink == innovations.get(i).getIsLink() && neuron1 == innovations.get(i).getFrom() && neuron2 == innovations.get(i).getTo())
				return i;
		
		return -1;
		
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
