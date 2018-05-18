package common.gameobjects;

public enum Type {
	MONSTER, RITUAL,  SOURCE, NONE;
	
	public String toString()
	{
		switch(this)
		{
			case MONSTER:
				return "MONSTER";
			case RITUAL:
				return "RITUAL";
			case SOURCE:
				return "SOURCE";
			case NONE:
				return "NONE";
		}
		return null;
	}
	
	public static Type fromString(String s)
	{
		switch(s)
		{
			case "MONSTER":
				return MONSTER;
			case "RITUAL":
				return RITUAL;
			case "SOURCE":
				return SOURCE;
		}
		return NONE;
	}
}
