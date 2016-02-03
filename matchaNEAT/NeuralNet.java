package matchaNEAT;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class NeuralNet {

	private double[][] adjMap;
	private double[][] neurons;
	private int numInputs;
	private int numOutputs;
	
	public NeuralNet(HashMap<Integer, NeuronGene> neurons, HashMap<Integer, LinkGene> links, int numInputs, int numOutputs){
		
		this.neurons = new double[2][neurons.size()];
		this.adjMap = new double[neurons.size()][neurons.size()];
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		
		Comparator<NeuronGene> neuronComparator = new Comparator<NeuronGene>(){
			@Override
			public int compare(NeuronGene o1, NeuronGene o2){
				return Integer.compare(o1.getID(), o2.getID());
				}
			};
		
		NeuronGene[] neuronsArr = neurons.values().toArray(new NeuronGene[0]);
		Arrays.sort(neuronsArr, neuronComparator);
		
		HashMap<Integer, Integer> idIndexer = new HashMap<Integer, Integer>();
		for(int i = 0; i < neurons.size(); i++){
			
			this.neurons[1][i] = neuronsArr[i].getP();
			idIndexer.put(neuronsArr[i].getID(), i);
			
		}
		
		LinkGene[] linksArr = links.values().toArray(new LinkGene[0]);
		for(int i = 0; i < links.size(); i++)
			adjMap[idIndexer.get(linksArr[i].getFrom())][idIndexer.get(linksArr[i].getTo())] = linksArr[i].getWeight();
		
	}
	
}
