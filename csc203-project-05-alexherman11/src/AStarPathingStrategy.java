import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy
{
    /*
    Select the node from our open set that has the lowest estimated total score
    Remove this node from the open set
    Add to the open set all of the nodes that we can reach from it
    work out the new score from this node to each new one to see if it's an
        improvement on what we've got so far
        if it is, then we update what we know about that node.
    This then repeats until the node in our open set
        that has the lowest estimated total score is our destination,
        at which point we've got our route.

     tip --
    to filter which points
    potentialneighbors.apply(Point)
    make sure it is not start
    make sure canPassThrough
    .filter (Point) openSet.contains()
    collect it

    while openset isnt empty
    pick point with smallest f value
    openset isn't checked
    once picked
        do everything else a star
    remove point from open
    add point to closed

    1. Assume start and end points are known
    2. Add start point to the open set
    3. Choose a current point from the open set with the smallest f value
    4. If the current point is the end point, go to step 8
    5. Otherwise, analyze all valid neighbor points that are not in the closed set
        a. If the neighbor is not in the open set, add it to the open set
        b. Calculate the neighbor’s g value:
            i. g = distance of current point from the start point + distance from current point
            to the neighbor point
            (Note: in this example, a neighbor point is always distance 1 away from the
            current point)
        c. If the neighbor’s g value is better than a previously calculated g value (or is the first one
        calculated), record the neighbor’s new g value and:
            i. Calculate the distance from the neighbor to the end point
                  1. This is an “h value” or heuristic. This can be a Euclidian distance,
                    Manhattan distance, etc.
            ii. Calculate the neighbor’s f value
                  1  . f value = g value + h value
            iii. Record the neighbor’s previous point (i.e., the current point)
    6. Move the current node to the closed set
    7. Go to step 4
    8. Reconstruct the path using the previous points mapping
     */
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<>();
        ArrayList<Point> openSet = new ArrayList<>();
        ArrayList<Point> closedSet = new ArrayList<>();
        closedSet.add(new Point(-1, -1));
        HashMap<Point, Double> fscores = new HashMap<>();
        HashMap<Point, Integer> gscores = new HashMap<>();
        gscores.put(start, 0);
        fscores.put(start, fScore(start, end, 0));
        openSet.add(start);
        Point current;

        while (!openSet.isEmpty()) {
            current = openSet.get(0);
            for (Point p: openSet) {
                if (fscores.get(p) < fscores.get(current)) {
                    current = p;
                }
            }
            ArrayList<Point> neighbours = (CARDINAL_NEIGHBORS.apply(current)
                    .filter(canPassThrough)
                    .filter(p -> !closedSet.contains(p))
                    .collect(Collectors.toCollection(ArrayList:: new)));
            for (Point p: neighbours) {
                if (!openSet.contains(p)) {
                    openSet.add(p);
                }
                int gscore = gscores.get(current) + 1;
                if (!gscores.containsKey(p) || (gscores.containsKey(p) && gscores.get(p) > gscore)) {
                    gscores.put(p, gscore);
                    fscores.put(p, fScore(p, end, gscore));
                    p.parent = current;
                }
            }
            if (Functions.adjacent(end, current)) {
                path.add(current);
                while (current != null && current.parent != start) {
                    path.add(current.parent);
                    current = current.parent;
                }
                break;
            }
            closedSet.add(current);
            openSet.remove(current);
        }
        for (int k = 0, j = path.size() - 1; k < j; k++)
        {
            path.add(k, path.remove(j));
        }
        return path;
    }

    public double fScore(Point p1, Point goal, int gscore) {
        return Math.pow((p1.x-goal.x), 2) + Math.pow((p1.y-goal.y), 2) + gscore;
    }
}
