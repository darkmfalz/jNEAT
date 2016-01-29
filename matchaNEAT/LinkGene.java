package matchaNEAT;

public class LinkGene extends Gene {
	
	private int from;
	private int to;
	private double weight;
	private boolean enabled;
	private boolean recurrent;

	public LinkGene(int geneNum, int from, int to, double weight, boolean recurrent) {
		
		super(geneNum);
		this.from = from;
		this.to = to;
		this.weight = weight;
		this.enabled = true;
		this.recurrent = recurrent;
	
	}
	
	public LinkGene(int geneNum, int from, int to, double weight, boolean enabled, boolean recurrent) {
		
		super(geneNum);
		this.from = from;
		this.to = to;
		this.weight = weight;
		this.enabled = enabled;
		this.recurrent = recurrent;
	
	}
	
	//Getters
	public int getFrom(){
		
		return from;
		
	}
	
	public int getTo(){
		
		return to;
		
	}
	
	public double getWeight(){
		
		return weight;
		
	}
	
	public boolean getEnabled(){
		
		return enabled;
		
	}
	
	public boolean getRecurretn(){
		
		return recurrent;
		
	}

	//Setters
	public void setWeight(double weight){
		
		this.weight = weight;
		
	}
	
	public void setEnabled(boolean enabled){
		
		this.enabled = enabled;
		
	}
	
	//Miscellaneous
	public LinkGene clone(){
		
		return new LinkGene(geneNum, from, to, weight, enabled, recurrent);
		
	}
	
}
