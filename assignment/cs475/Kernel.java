package cs475;

import java.io.Serializable;

public interface Kernel extends Serializable {
	public double value(Instance i1, Instance i2);
}
