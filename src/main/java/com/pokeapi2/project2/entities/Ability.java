package com.pokeapi2.project2.entities;

public class Ability {
	private String name;
	private boolean isHidden;
		
	
	public Ability(String name, boolean isHidden) {
		super();
		this.name = name;
		this.isHidden = isHidden;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isHidden() {
		return isHidden;
	}
	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}
		
	
}
