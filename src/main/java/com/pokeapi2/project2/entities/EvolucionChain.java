package com.pokeapi2.project2.entities;


public class EvolucionChain{
	private Integer id;
	private String name;
	private String sprite;
	
	
	
	public EvolucionChain(Integer id, String name, String sprite) {
		super();
		this.id = id;
		this.name = name;
		this.sprite = sprite;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSprite() {
		return sprite;
	}
	public void setSprite(String sprite) {
		this.sprite = sprite;
	}

	
	
	
}
