package matchaNEAT;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class NeuralNet {

	private double[][] adjMap;
	private double[][] neurons; //First row is neuron activations, second row is activations input, third row is p-values
	private int numInputs;
	private int numOutputs;
	private int depth;
	
	public NeuralNet(HashMap<Integer, NeuronGene> neurons, HashMap<Integer, LinkGene> links, int numInputs, int numOutputs){
		
		this.neurons = new double[3][neurons.size()];
		this.adjMap = new double[neurons.size()][neurons.size()];
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		
		depth = 0;
		HashMap<Double, Double> checker = new HashMap<Double, Double>();
		
		Comparator<NeuronGene> neuronComparator = new Comparator<NeuronGene>(){
			@Override
			public int compare(NeuronGene o1, NeuronGene o2){
				
				if(o1.getTier() < o2.getTier())
					return -1;
				else if(o1.getTier() > o2.getTier())
					return 1;
				else{
					
					if(o1.getType() == o2.getType())
						return Integer.compare(o1.getID(), o2.getID());
					else if(o1.getType() == NeuronGene.NeuronType.INPUT)
						return -1;
					else if(o1.getType() == NeuronGene.NeuronType.OUTPUT)
						return 1;
					else if(o2.getType() == NeuronGene.NeuronType.INPUT)
						return 1;
					else if(o2.getType() == NeuronGene.NeuronType.OUTPUT)
						return -1;
					else
						return 0;
					
				}
				
			}
		};
		
		NeuronGene[] neuronsArr = neurons.values().toArray(new NeuronGene[0]);
		Arrays.sort(neuronsArr, neuronComparator);
		
		HashMap<Integer, Integer> idIndexer = new HashMap<Integer, Integer>();
		for(int i = 0; i < neurons.size(); i++){
			
			if(!checker.containsKey(neuronsArr[i].getTier())){
				
				depth++;
				checker.put(neuronsArr[i].getTier(), neuronsArr[i].getTier());
				
			}
			
			this.neurons[0][i] = 0;
			this.neurons[1][i] = 0;
			this.neurons[2][i] = neuronsArr[i].getP();
			idIndexer.put(neuronsArr[i].getID(), i);
			
		}
		
		LinkGene[] linksArr = links.values().toArray(new LinkGene[0]);
		for(int i = 0; i < links.size(); i++)
			adjMap[idIndexer.get(linksArr[i].getFrom())][idIndexer.get(linksArr[i].getTo())] = linksArr[i].getWeight();
		
	}
	
	//Getters
	public double[][] getAdjMap(){
		
		return adjMap;
		
	}
	
	public double[] output(double[] inputs, boolean snapshot){
		
		double[] outputs = new double[numOutputs];
		
		for(int i = 0; i < numInputs; i++)
			neurons[0][i] = inputs[i];
		
		int flushCount;
		if(snapshot)
			flushCount = depth;
		else
			flushCount = 1;
		
		for(int i = 0; i < flushCount; i++){
			
			for(int j = 0; j < adjMap.length; j++){
				
				for(int k = 0; k < adjMap[j].length; k++){
					
					neurons[0][j] = 1.0/(1 + Math.exp(-1.0*neurons[1][j]/neurons[2][j]));
					neurons[1][j] = 0;
					
					neurons[1][k] += neurons[0][j] * adjMap[j][k];
					
				}
				
			}
			
		}
		
		for(int i = neurons[0].length - numOutputs; i < neurons[0].length; i++)
			outputs[i - neurons[0].length + numOutputs] = neurons[0][i];
		
		if(snapshot)
			for(int i = 0; i < neurons[0].length; i++){
				
				neurons[0][i] = 0;
				neurons[1][i] = 0;
				
			}
		
		return outputs;
		
	}
	
}
