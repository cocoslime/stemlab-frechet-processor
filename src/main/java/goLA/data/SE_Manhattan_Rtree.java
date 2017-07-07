package goLA.data;

import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import goLA.data.tree.ElkiRStarTree;
import goLA.model.Coordinates;
import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;
import goLA.utils.ManhattanDistance;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by dong on 2017. 5. 10..
 */
public class SE_Manhattan_Rtree implements Tree {

    private ElkiRStarTree manh_tree;
    private Coordinates[] criteria;
    private int size;

    //TODO avoid data copy
    private HashMap<String, Trajectory> holder;

    public SE_Manhattan_Rtree() {
        this.manh_tree = new ElkiRStarTree();
        this.holder = new HashMap<>();
    }

    @Override
    public void addTrajectory(String id, Trajectory tr) {
        List<Coordinates> list = tr.getCoordinates();
        if (size == 0) {
            criteria = new Coordinates[2];
            criteria[0] = list.get(0);
            criteria[1] = list.get(list.size() - 1);
        }
        Double start_man_dist = ManhattanDistance.getSignedDistance(list.get(0), criteria[0]);
        Double end_man_dist = ManhattanDistance.getSignedDistance(list.get(list.size() - 1), criteria[1]);

        manh_tree.add(tr.getName(), new double[]{start_man_dist, end_man_dist});

        size++;

        //TODO avoid data copy
        holder.put(tr.getName(), tr);
    }

    @Override
    public void initialize() {
        manh_tree.initialize();
    }

    @Override
    public TrajectoryHolder getPossible(TrajectoryQuery query) {
        Coordinates q_start = query.getTrajectory().getCoordinates().get(0);
        Coordinates q_end = query.getTrajectory().getCoordinates().get(query.getTrajectory().getCoordinates().size() - 1);
        double dist = query.dist;

        Double s_q_dist = ManhattanDistance.getSignedDistance(q_start, criteria[0]);
        Double e_q_dist = ManhattanDistance.getSignedDistance(q_end, criteria[1]);

        DoubleDBIDList results = manh_tree.search(new double[]{s_q_dist, e_q_dist}, dist * 1.5);
        TrajectoryHolder poss = new TrajectoryHolder();

        int i = 0;
        for (DoubleDBIDListIter res = results.iter(); res.valid(); res.advance(), i++) {
            poss.addTrajectory(manh_tree.getRecordName(res), holder.get(manh_tree.getRecordName(res)));
        }

        return poss;
    }

    @Override
    public ConcurrentHashMap<String, Trajectory> getHolder() {
        return null;
    }

    @Override
    public int size() {
        return size;
    }
}
