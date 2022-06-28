package com.pokeapi2.project2.entities;

import java.util.ArrayList;

public class PokemonSpecifics extends Pokemon{
	private ArrayList<EvolucionChain> evolucionChain;
	private String description;

	
	public ArrayList<EvolucionChain> getEvolucionChain() {
		return evolucionChain;
	}
	public void setEvolucionChain(ArrayList<EvolucionChain> evolucionChain) {
		this.evolucionChain = evolucionChain;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
