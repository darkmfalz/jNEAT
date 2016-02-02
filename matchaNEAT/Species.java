package matchaNEAT;

import java.util.HashMap;

public class Species {
	
	private Genome leader;
	private HashMap<Integer, Genome> members;
	private int speciesID;
	private double bestFitness;
	private double avgFitness;
	private int generationsNoImprovement;
	private int age;
	private double numOffspring;
	
	public Species(Genome leader, int speciesID){
		
		this.leader = leader;
		members.put(leader.getID(), leader);
		this.speciesID = speciesID;
		bestFitness = 0.0;
		avgFitness = 0.0;
		generationsNoImprovement = 0;
		age = 0;
		numOffspring = 0.0;
		
	}
	
	//Getters
	public Genome getLeader(){
		
		return leader;
		
	}
	
	public double getNumOffspring(){
		
		return numOffspring;
		
	}
	
	public int getNumMembers(){
		
		return members.size();
		
	}
	
	public int getGenerationsNoImprovement(){
		
		return generationsNoImprovement;
		
	}
	
	public int getID(){
		
		return speciesID;
		
	}
	
	public double getLeaderFitness(){
		
		return leader.getFitness();
		
	}
	
	public double getBestFitness(){
		
		return bestFitness;
		
	}
	
	public double getAverageFitness(){
		
		return avgFitness;
		
	}
	
	public int getAge(){
		
		return age;
		
	}
	
	public void adjustFitness(int youngAgeThreshold, int youngAgeBonus, int oldAgeThreshold, int oldAgePenalty){
		
		Genome[] membersArr = members.values().toArray(new Genome[0]);
		
		for(int gen = 0; gen < members.size(); gen++){
			
			double fitness = membersArr[gen].getFitness();
			
			if(age < youngAgeThreshold)
				fitness *= youngAgeBonus;
			else if(age > oldAgeThreshold)
				fitness *= oldAgePenalty;
			
			membersArr[gen].setSharedFitness(fitness, members.size());
			
		}
		
	}
	
	public void addMember(Genome newMember){
		
		members.put(newMember.getID(), newMember);
		avgFitness = (members.size() - 1)/members.size()*avgFitness + newMember.getFitness();
		
	}
	
	public void purge(){
		
		
		
	}
	
	public void calculateOffspringNum(double popAvgFitness){
		
		numOffspring = avgFitness*members.size()/popAvgFitness;
		
	}

}
