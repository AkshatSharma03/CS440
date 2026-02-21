package src.labs.routing.agents;

// SYSTEM IMPORTS
import edu.bu.labs.routing.Coordinate;
import edu.bu.labs.routing.Direction;
import edu.bu.labs.routing.Path;
import edu.bu.labs.routing.State.StateView;
import edu.bu.labs.routing.Tile;
import edu.bu.labs.routing.agents.MazeAgent;
import java.util.HashSet; // will need for bfs
import java.util.LinkedList; // will need for bfs
import java.util.Queue; // will need for bfs
import java.util.Set;

// JAVA PROJECT IMPORTS

public class BFSMazeAgent extends MazeAgent {

  public BFSMazeAgent(final int agentId) {
    super(agentId);
  }

  @Override
  public void initializeFromState(final StateView stateView) {
    // find the FINISH tile
    Coordinate finishCoord = null;
    for (int rowIdx = 0; rowIdx < stateView.getNumRows(); ++rowIdx) {
      for (int colIdx = 0; colIdx < stateView.getNumCols(); ++colIdx) {
        if (stateView.getTileState(new Coordinate(rowIdx, colIdx)) == Tile.State.FINISH) {
          finishCoord = new Coordinate(rowIdx, colIdx);
        }
      }
    }
    this.setFinishCoordinate(finishCoord);

    // make sure to call the super-class' version!
    super.initializeFromState(stateView);
  }

  @Override
  public boolean shouldReplacePlan(final StateView stateView) {
    return false;
  }

  @Override
  public Path<Coordinate> search(
      final Coordinate src, final Coordinate goal, final StateView stateView) {
    // TODO: complete me!
    Queue<Path<Coordinate>> frontier = new LinkedList<>();
    Set<Coordinate> visited = new HashSet<>();

    // Start with path containing just source
    Path<Coordinate> startPath = new Path<>(src);
    frontier.offer(startPath);
    visited.add(src);

    while (!frontier.isEmpty()) {
      Path<Coordinate> currentPath = frontier.poll();
      Coordinate currentCoord = currentPath.current();

      // Goal check
      if (currentCoord.equals(goal)) {
        return currentPath;
      }

      // Explore all 4 cardinal directions
      for (Direction dir : Direction.values()) {

        Coordinate neighbor = currentCoord.getNeighbor(dir);

        // Bounds check
        if (neighbor.row() < 0
            || neighbor.row() >= stateView.getNumRows()
            || neighbor.col() < 0
            || neighbor.col() >= stateView.getNumCols()) {
          continue;
        }

        // Already visited?
        if (visited.contains(neighbor)) {
          continue;
        }

        // Wall check
        Tile.State tileState = stateView.getTileState(neighbor);
        if (tileState == Tile.State.WALL) {
          continue;
        }

        // Valid neighbor: create extended path
        Path<Coordinate> newPath =
            new Path<>(
                currentPath, // parent path
                neighbor, // new current coordinate
                1 // edge weight for unweighted grid
                );

        frontier.offer(newPath);
        visited.add(neighbor);
      }
    }
    return null;
  }
}
