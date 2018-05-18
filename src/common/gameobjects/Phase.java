package common.gameobjects;

public enum Phase {
	BEGINNING, PLAY_SOURCE, MAIN_CAST, MAIN_RESOLVE, END, NONE;

        @Override
	public String toString()
	{
		switch(this)
		{
			case BEGINNING:
				return "BEGINNING";
			case PLAY_SOURCE:
				return "PLAY_SOURCE";
			case MAIN_CAST:
				return "MAIN_CAST";
                        case MAIN_RESOLVE:
				return "MAIN_RESOLVE";
			case END:
				return "END";
			case NONE:
				break;
		}
		return null;
	}
	
	static public Phase fromString(String s)
	{
		switch(s)
		{
			case "BEGINNING":
				return BEGINNING;
			case "PLAY_SOURCE":
				return PLAY_SOURCE;
			case "MAIN1_CAST":
				return MAIN_CAST;
			case "MAIN_RESOLVE":
				return MAIN_RESOLVE;
			case "END":
				return END;
		}
		return null;
	}
}
