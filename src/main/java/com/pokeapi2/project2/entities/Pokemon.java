package com.pokeapi2.project2.entities;

import java.util.ArrayList;

public class Pokemon {
	private String name;
	private int id;
	private ArrayList<String> types;
	private String weight;
	private ArrayList<Ability> abilities;
	private String sprite;

	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getTypes() {
		return types;
	}
	public void setTypes(ArrayList<String> types) {
		this.types = types;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public ArrayList<Ability> getAbilities() {
		return abilities;
	}
	public void setAbilities(ArrayList<Ability> abilities) {
		this.abilities = abilities;
	}
	public String getSprite() {
		return sprite;
	}
	public void setSprite(String sprite) {
		this.sprite = sprite;
	}
	

	
	
}
