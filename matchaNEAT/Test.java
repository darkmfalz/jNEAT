package matchaNEAT;

import java.util.ArrayList;
import java.util.HashMap;

public class Test {

	public static void main(String[] args){
		
		
		NeuronGene neuronGene0 = new NeuronGene(0, -1, -1, 0, NeuronGene.NeuronType.INPUT, 0.0, 1.0, false);
		NeuronGene neuronGene1 = new NeuronGene(1, -1, -1, 1, NeuronGene.NeuronType.NONE, 1.0, 1.0, false);
		NeuronGene neuronGene2 = new NeuronGene(2, -1, -1, 2, NeuronGene.NeuronType.NONE, 2.0, 1.0, false);
		NeuronGene neuronGene3 = new NeuronGene(3, -1, -1, 3, NeuronGene.NeuronType.INPUT, 0.0, 1.0, false);
		
		HashMap<Integer, NeuronGene> neurons = new HashMap<Integer, NeuronGene>();
		HashMap<Integer, LinkGene> links = new HashMap<Integer, LinkGene>();
		
		neurons.put(0, neuronGene0);
		neurons.put(1, neuronGene1);
		neurons.put(2, neuronGene2);
		neurons.put(3, neuronGene3);
		
		ArrayList<Gene> innovations = new ArrayList<Gene>();
		innovations.add(neuronGene0);
		innovations.add(neuronGene1);
		innovations.add(neuronGene2);
		innovations.add(neuronGene3);
		
		Genome genome = new Genome(0, neurons, links, 1, 1);
		for(int i = 0; i < 40; i++)
			genome.addLink(1.0, 0.5, innovations, 10, 10);
		
		LinkGene[] blah = genome.getLinks().values().toArray(new LinkGene[0]);
		for(int i = 0; i < blah.length; i++)
			System.out.println(blah[i].getGeneNum() + ": " + blah[i].getFrom() + " to " + blah[i].getTo());
		
	}
	
}
