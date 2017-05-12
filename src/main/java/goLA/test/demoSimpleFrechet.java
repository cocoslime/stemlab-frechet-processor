package goLA.test;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import goLA.compute.*;
import goLA.data.SE_MBR_Rtree;
import goLA.data.Start_End_Rtree;
import goLA.io.DataExporter;
import goLA.io.DataImporter;
import goLA.manage.Manager;
import goLA.manage.ManagerImpl;
import goLA.model.TrajectoryHolder;

public class demoSimpleFrechet {

    public static void main(String[] args) throws IOException {

        Instant start = Instant.now();
        System.out.println("Start Program");
        
        String src_path = "dataset.txt";
        String query_path = "queries.txt";

        Manager manager = new ManagerImpl(new SimpleFrechet(), new SE_MBR_Rtree(), new DataImporter());

        manager.makeStructure(src_path);

        //get all data trajectories
        Instant middle = Instant.now();
        System.out.println("\nGet " + manager.getTree().size() + " data and put into data structure : "+ Duration.between(start, middle));
        
        List<TrajectoryHolder> result = manager.findResult(query_path);

        DataExporter de = new DataExporter();
        for (int index = 0 ; index < result.size() ; index++){
        	result.get(index).printAllTrajectory(de, index);
        }

        Instant end = Instant.now();
        System.out.println("\nQuery Processing : "+ Duration.between(middle, end));
        System.out.println("\nProgram execution time : "+ Duration.between(start, end));


    }

}
