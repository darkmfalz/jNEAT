package matchaNEAT;

import java.util.ArrayList;
import java.util.HashMap;

public class Test {

	public static void main(String[] args){
		
		Fitness fitness = new Fitness(){
			@Override
			public double fitnessFunction(NeuralNet brain) {
				
				double score = 0.0;
				
				for(double i = 0.0; i <= 1.0; i++)
					for(double j = 0.0; j <= 1.0; j++){
						
						double[] inputs = {i, j};
						double[] outputs = brain.output(inputs, true);
						double correctResponse;
						if(i == 0.0 && j == 0.0)
							correctResponse = 0.0;
						else if(i == 0.0 && j == 1.0)
							correctResponse = 1.0;
						else if(i == 1.0 && j == 0.0)
							correctResponse = 1.0;
						else
							correctResponse = 0.0;
						
						score += 1.0 - Math.abs(outputs[0] - correctResponse);
						
					}
				
				return score;
				
			}
		};
		Breeder breeder = new Breeder(100, 2, 1, true, fitness);
		
		/*NeuronGene neuronGene0 = new NeuronGene(0, -1, -1, 0, NeuronGene.NeuronType.INPUT, 0.0, 1.0, false);
		NeuronGene neuronGene1 = new NeuronGene(1, -1, -1, 1, NeuronGene.NeuronType.INPUT, 0.0, 1.0, false);
		NeuronGene neuronGene2 = new NeuronGene(2, -1, -1, 2, NeuronGene.NeuronType.OUTPUT, 1.0, 1.0, false);
		NeuronGene neuronGene3 = new NeuronGene(3, -1, -1, 3, NeuronGene.NeuronType.OUTPUT, 1.0, 1.0, false);
		
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
		
		Genome genome = new Genome(0, neurons, links, 2, 2);
		Genome genome2 = genome.clone();
		for(int i = 0; i < 50; i++)
			genome.addLink(1.0, 0.5, innovations, 10, 10);
		for(int i = 0; i < 10; i++)
			genome.addNeuron(1.0, innovations, 10);
		
		for(int i = 0; i < 40; i++)
			genome2.addLink(1.0, 0.5, innovations, 10, 10);
		for(int i = 0; i < 10; i++)
			genome2.addNeuron(1.0, innovations, 10);
		
		for(int i = 0; i < 10; i++)
			genome.addLink(1.0, 0.5, innovations, 10, 10);
		for(int i = 0; i < 10; i++)
			genome.addNeuron(1.0, innovations, 10);
		
		LinkGene[] blah = genome.getLinks().values().toArray(new LinkGene[0]);
		for(int i = 0; i < blah.length; i++)
			System.out.println(blah[i].getGeneNum() + ": " + blah[i].getFrom() + " to " + blah[i].getTo());
		System.out.println();
		NeuronGene[] blah2 = genome.getNeurons().values().toArray(new NeuronGene[0]);
		for(int i = 0; i < blah2.length; i++)
			System.out.println(blah2[i].getID() + ": " + blah2[i].getFrom() + " to " + blah2[i].getTo() + " @ " + blah2[i].getTier());
		System.out.println();
		
		System.out.println(genome.getDistance(genome2));
		genome2 = Genome.breedChild(genome, genome2.clone(), 1);
		System.out.println(genome.getDistance(genome2));
		genome2 = Genome.breedChild(genome, genome2.clone(), 1);
		System.out.println(genome.getDistance(genome2));
		genome2 = Genome.breedChild(genome, genome2.clone(), 1);
		System.out.println(genome.getDistance(genome2));
		genome2 = Genome.breedChild(genome, genome2.clone(), 1);
		System.out.println(genome.getDistance(genome2));
		
		System.out.println();
		blah = genome2.getLinks().values().toArray(new LinkGene[0]);
		for(int i = 0; i < blah.length; i++)
			System.out.println(blah[i].getGeneNum() + ": " + blah[i].getFrom() + " to " + blah[i].getTo());
		System.out.println();
		blah2 = genome2.getNeurons().values().toArray(new NeuronGene[0]);
		for(int i = 0; i < blah2.length; i++)
			System.out.println(blah2[i].getID() + ": " + blah2[i].getFrom() + " to " + blah2[i].getTo() + " @ " + blah2[i].getTier());
		System.out.println();*/
		
	}
	
}
