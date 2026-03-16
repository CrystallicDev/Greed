package com.natsu.greed.server.enchants;

public enum EnchantmentTableState {

	DEFAULT("default"),
	LAPIS_STATE("lapis"),
	AMETHYST_STATE("amethyst")
	
	;
	
	String name;
	
	EnchantmentTableState(String n){
		this.name = n;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
