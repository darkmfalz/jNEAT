package matchaNEAT;

import java.util.ArrayList;
import java.util.HashMap;

public class Test {

	public static void main(String[] args){
		
		HashMap<Integer, LinkGene> linkGeneList = new HashMap<Integer, LinkGene>();
		//linkGeneList.put(0, new LinkGene(linkGeneList.size(), 0, 1, 1.0, false));
		
		NeuronGene neuronGene0 = new NeuronGene(0, NeuronGene.NeuronType.NONE, 0.0, 1.0, false);
		NeuronGene neuronGene1 = new NeuronGene(1, NeuronGene.NeuronType.NONE, 1.0, 1.0, false);
		NeuronGene neuronGene2 = new NeuronGene(2, NeuronGene.NeuronType.NONE, 2.0, 1.0, false);
		NeuronGene neuronGene3 = new NeuronGene(3, NeuronGene.NeuronType.NONE, 0.0, 1.0, false);
		
		HashMap<Integer, NeuronGene> neurons = new HashMap<Integer, NeuronGene>();
		HashMap<Integer, LinkGene> links = new HashMap<Integer, LinkGene>();
		
		neurons.put(0, neuronGene0);
		neurons.put(1, neuronGene1);
		neurons.put(2, neuronGene2);
		neurons.put(3, neuronGene3);
		
		Genome genome = new Genome(0, neurons, links, 1, 1);
		for(int i = 0; i < 10; i++)
			genome.addLink(1.0, 0.5, linkGeneList, 10, 10);
		
		LinkGene[] blah = genome.getLinks().values().toArray(new LinkGene[0]);
		for(int i = 0; i < blah.length; i++)
			System.out.println(blah[i].getGeneNum() + ": " + blah[i].getFrom() + " to " + blah[i].getTo());
		
	}
	
}
