package matchaNEAT;

public class NeuronGene extends Gene {

	public enum NeuronType {
		
		INPUT, HIDDEN, BIAS, OUTPUT, NONE;
		
	}
	
	private int id; //Neuron ID
	private NeuronType type; //This is the type of neuron
	private double tier, p; //p in 1/(1 + e^(- a / p)), the logistic sigmoid activation function
	private boolean recurrent; //Is this a recurrent neuron
	
	public NeuronGene(int geneNum, int id, NeuronType type, double tier, double p, boolean recurrent) {
		
		super(geneNum);
		this.id = id;
		this.type = type;
		this.tier = tier;
		this.p = p;
		this.recurrent = recurrent;
		
	}

	//Getters
	public int getID(){
		
		return id;
		
	}
	
	public NeuronType getType(){
		
		return type;
		
	}
	
	public double getTier(){
		
		return tier;
		
	}
	
	public double getP(){
		
		return p;
		
	}
	
	public boolean getRecurrent(){
		
		return recurrent;
		
	}

	//Setters
	public void setP(double p){
		
		this.p = p;
		
	}
	
	public void setRecurrent(boolean recurrent){
		
		this.recurrent = recurrent;
		
	}
	
	//Cloners
	public NeuronGene clone(){
		
		return new NeuronGene(geneNum, id, type, tier, p, recurrent);
		
	}
	
}
