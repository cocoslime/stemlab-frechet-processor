package goLA.utils;

import goLA.model.Coordinates;
import goLA.model.Trajectory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Azamat on 7/4/2017.
 * Implemented from pseudo code on https://en.wikipedia.org/wiki/Ramer%E2%80%93Douglas%E2%80%93Peucker_algorithm
 */
public class DouglasPeucker {
    private static List<Coordinates> reduce(List<Coordinates> coordinates, Double epsilon) {

        double maxDistance = 0.0;
        int index = 0;
        for (int i = 0; i < coordinates.size() - 1; i++) {
            double distance = EuclideanDistance.pointAndLine(coordinates.get(i), coordinates.get(0), coordinates.get(coordinates.size() - 1));
            if (distance > maxDistance) {
                index = i;
                maxDistance = distance;
            }
        }

        List<Coordinates> result = new ArrayList<>();
        if (maxDistance > epsilon) {
            List<Coordinates> firstRecursiveResult = reduce(coordinates.subList(0, index + 1), epsilon);
            List<Coordinates> secondRecursiveResult = reduce(coordinates.subList(index, coordinates.size()), epsilon);

            result.addAll(firstRecursiveResult);
            result.addAll(secondRecursiveResult.subList(1, secondRecursiveResult.size()));
        } else {
            result.add(coordinates.get(0));
            result.add(coordinates.get(coordinates.size() - 1));
        }
        return result;
    }

    public static Trajectory getReduced(Trajectory trajectory, Double epsilon) {
        List<Coordinates> coordinates = reduce(trajectory.getCoordinates(), epsilon);
        trajectory.setCoordinates(coordinates);
        return trajectory;
    }
}
