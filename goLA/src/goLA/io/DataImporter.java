package goLA.io;

import goLA.model.*;

public interface DataImporter {
	public void loadFiles(String src, Trajectories trj);
}
