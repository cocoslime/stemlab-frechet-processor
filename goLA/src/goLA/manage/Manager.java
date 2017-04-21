package goLA.manage;

import java.util.List;

import goLA.io.DataExporter;
import goLA.io.DataImporter;

public interface Manager {
	
	public void makeStructure(DataImporter dti, String src_path);
	
	public List<List<String>> findResult(String query_path);

	public void printResult(DataExporter dx, List<List<String>> result);
}
