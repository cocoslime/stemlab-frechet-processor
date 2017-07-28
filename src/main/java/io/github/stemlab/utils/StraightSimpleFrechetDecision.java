package io.github.stemlab.utils;

import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;

/**
 * Created by dong on 2017. 7. 28..
 */
public class StraightSimpleFrechetDecision {
    public static boolean decisionIsInResult(Query query, Trajectory trajectory) {
        Trajectory simplifiedQuery = StraightForward.getReduced(query.getTrajectory(), query.getDistance());
        Trajectory simplifiedTrajectory = StraightForward.getReduced(trajectory, query.getDistance());
        if (isFiltered(simplifiedQuery, simplifiedTrajectory, query.getDistance())) { // Decide whether simple_trajectory is sure in out of result.
            if (isResult(simplifiedQuery, trajectory, query.getDistance())) { // Decide whether trajectory is sure in result by using simplification.
                return true;
            } else if (isTrajectoryInQueryRange(query, trajectory)) { // Decide whether frechet distance is lower than query distance.
                return true;
            }
        }
        return false;
    }

    public static boolean isResult(Trajectory simple_query, Trajectory trajectory, double dist) {
        double modified_dist_2 = dist - 1 * dist * StraightForward.Epsilon * StraightForward.Constant;
        if (DiscreteFrechetDistance.decision(simple_query, trajectory, modified_dist_2)) {

    /**
     * Decide whether trajectory is sure in result by using simplification.
     */
    public static boolean isResult(Trajectory simpleQuery, Trajectory trajectory, double distance) {
        double modifiedDistance = distance - (1 * distance * StraightForward.Epslion * StraightForward.Constant);
        if (DiscreteFrechetDistance.decision(simpleQuery, trajectory, modifiedDistance)) {
            return true;
        } else {
            return FrechetDistance.decision(simple_query, trajectory, modified_dist_2);
        } else {
            return FrechetDistance.decision(simpleQuery, trajectory, modifiedDistance);
        }
    }

    /**
     * Decide whether simpleTrajectory is sure in out of resultl
     * .
     */
    public static boolean isFiltered(Trajectory simpleQuery, Trajectory simpleTrajectory, double distance) {
        double modifiedDistance = distance + (2 * distance * StraightForward.Epslion * StraightForward.Constant);
        return FrechetDistance.decision(simpleQuery, simpleTrajectory, modifiedDistance);
    }

    /**
     * First, decide whether discrete Frechet Distance is lower than parameter, if not check real Frechet Distance.
     *
     * @param query
     * @param trajectory
     */
    public static boolean isTrajectoryInQueryRange(Query query, Trajectory trajectory) {
        if (DiscreteFrechetDistance.decision(query.getTrajectory(), trajectory, query.getDistance())) {
            return true;
        } else
            return FrechetDistance.decision(query.getTrajectory(), trajectory, query.getDistance());
    }

}
