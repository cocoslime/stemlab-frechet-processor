package goLA.manage;

import goLA.compute.QueryProcessor;
import goLA.data.Tree;
import goLA.filter.Filter;
import goLA.io.DataExporter;
import goLA.io.DataImporter;
import goLA.model.Trajectory;
import goLA.model.TrajectoryQuery;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ManagerImpl implements Manager {

    private DataImporter di;
    private QueryProcessor q_processor;
    private Tree tree;
    private Filter filter = null;

    //Determine How to calculate Query by using constructor parameter
    public ManagerImpl(QueryProcessor qp_impl, Tree trs, DataImporter pdi, Filter ft) {
        q_processor = qp_impl;
        di = pdi;
        tree = trs;
        filter = ft;
    }

    public ManagerImpl(QueryProcessor qp_impl, Tree trs, DataImporter pdi) {
        q_processor = qp_impl;
        di = pdi;
        tree = trs;
    }

    @Override
    public void setFilter(Filter ft) {
        filter = ft;
    }

    @Override
    public void makeStructure(String path) {
        di.loadFiles(path, tree);
    }

    @Override
    public List<List<Trajectory>> findResult(String query_path, DataExporter de) {
        List<List<Trajectory>> result = new ArrayList<>();
        List<TrajectoryQuery> query = di.getQueries(query_path);

        int index = 0;
        for (TrajectoryQuery q : query){
            Instant start = Instant.now();
            System.out.println("\n\n---- Query processing : " + q.getTrajectory().getName() + ", " + q.dist + " -------");
            List<Trajectory> possible_trajectoryHolder = tree.getPossible(q);
            int size1 = possible_trajectoryHolder.size();
            System.out.println("---- candidate number : " + size1 + " -------");

            Instant middle1 = Instant.now();
            System.out.println("---- getPossible Time : " + Duration.between(start, middle1));

            List<Trajectory> possible_trajectoryHolder_filter;
            Instant middle2;

            if (this.filter != null) {
                possible_trajectoryHolder_filter = filter.doFilter(q, possible_trajectoryHolder);
                middle2 = Instant.now();
                System.out.println("---- Filtering Time : " + Duration.between(middle1, middle2));
                System.out.println("---- After Filtering number : " + possible_trajectoryHolder_filter.size() + " -------");
            } else {
                possible_trajectoryHolder_filter = possible_trajectoryHolder;
                middle2 = middle1;
            }

            List<Trajectory> q_res = q_processor.query(q, possible_trajectoryHolder_filter);
            result.add(q_res);
            int size2 = q_res.size();
            System.out.println("---- result number : " + size2 + " -------");

            Instant end = Instant.now();
            System.out.println("---- calculate Dist Time : " + Duration.between(middle2, end) + " -------");
            if (de != null)
                de.exportQuery(index, 
                        q, possible_trajectoryHolder.size(), this.filter != null,
                        possible_trajectoryHolder_filter.size(), size2,
                        start, middle1, middle2, end
                );
        }

        return result;
    }

    @Override
    public Tree getTree() {
        return this.tree;
    }


}
