package common.gameobjects;

import java.awt.Color;
import java.awt.Image;
import java.util.UUID;

public class Card{

	// intrinsic variables
        public UUID uuid;
	public String name;
	public String cost;
	public Color color;
	public String text;
	public Image image;
	public Type type;

	//software variables
	public boolean tapable;
	public boolean tapped;
	public boolean markable;
	public boolean marked;

	

	public Card( String name, String cost, Color color, String text, Image image, Type type,
			int width, int height, boolean tapable, boolean markable)
	{
		uuid = UUID.randomUUID();
		this.name = name;
		this.cost = cost;
		this.color = color;
		this.text = text;
		this.image = image;
		this.type = type;

		this.tapable = tapable;
		tapped = false;
		this.markable = markable;
		marked = false;
	}

	public Card (Card c)
	{
		uuid = UUID.randomUUID();
		this.name = c.name;
		this.cost = c.cost;
		this.color = c.color;
		this.text = c.text;
		this.image = c.image;
		this.type = c.type;

		this.tapable = c.tapable;
		this.tapped = c.tapped;
		this.markable = c.markable;
		this.marked = c.marked;
	}

	void resolve(){}

	public void markUnmark()
	{
		marked = !marked;
	}

	public void tapUntap()
	{
		tapped = !tapped;
	}

	public void displayOnly()
	{
		tapable = false;
		tapped = false;
		markable = false;
		marked = false;
	}

	public void makeSource()
	{
		type = Type.SOURCE;
		name = "SOURCE";
	}
}