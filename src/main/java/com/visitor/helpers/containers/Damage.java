package com.visitor.helpers.containers;

public class Damage {
	public int amount;
	public boolean mayKill;

	public Damage (int amount, boolean mayKill) {
		this.amount = amount;
		this.mayKill = mayKill;
	}

	public Damage (int amount) {
		this.amount = amount;
		this.mayKill = true;
	}

}
