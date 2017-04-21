package goLA.test;

import java.io.*;
import java.util.List;

import goLA.io.*;
import goLA.manage.*;

public class demo {

	public static void main(String[] args) {
		
		String src_path = "../files/dataset.txt";
		String query_path = "../files/queries.txt";
		
		Manager manager = new ManagerImpl();
		DataImporter di = new DataImporterImpl();
		DataExporter dx = new DataExporterImpl();
		
		manager.makeStructure(di, src_path);
		
		List<List<String>> result = manager.findResult(query_path);
		
		manager.printResult(dx, result);
	}

}
