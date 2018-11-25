package game;

import java.io.Serializable;

public enum Phase implements Serializable {
	MULLIGAN, BEGIN, BEGIN_RESOLVING, MAIN, MAIN_RESOLVING, END, END_RESOLVING
}
