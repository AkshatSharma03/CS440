package src.labs.routing.agents;

// SYSTEM IMPORTS
import edu.bu.labs.routing.Coordinate;
import edu.bu.labs.routing.Direction;
import edu.bu.labs.routing.Path;
import edu.bu.labs.routing.State.StateView;
import edu.bu.labs.routing.Tile;
import edu.bu.labs.routing.agents.MazeAgent;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class DFSMazeAgent extends MazeAgent {

  public DFSMazeAgent(final int agentId) {
    super(agentId);
  }

  @Override
  public void initializeFromState(final StateView stateView) {
    Coordinate finishCoord = null;
    for (int rowIdx = 0; rowIdx < stateView.getNumRows(); ++rowIdx) {
      for (int colIdx = 0; colIdx < stateView.getNumCols(); ++colIdx) {
        if (stateView.getTileState(new Coordinate(rowIdx, colIdx)) == Tile.State.FINISH) {
          finishCoord = new Coordinate(rowIdx, colIdx);
        }
      }
    }
    this.setFinishCoordinate(finishCoord);
    super.initializeFromState(stateView);
  }

  @Override
  public boolean shouldReplacePlan(final StateView stateView) {
    return false;
  }

  @Override
  public Path<Coordinate> search(
      final Coordinate src, final Coordinate goal, final StateView stateView) {
    Stack<Path<Coordinate>> frontier = new Stack<>();
    Set<Coordinate> visited = new HashSet<>();

    // Initialize search
    frontier.push(new Path<>(src));
    visited.add(src);

    while (!frontier.isEmpty()) {
      Path<Coordinate> currentPath = frontier.pop();
      Coordinate currentCoord = currentPath.current();

      // Goal check
      if (currentCoord.equals(goal)) {
        return currentPath;
      }

      for (Direction dir : Direction.values()) {
        Coordinate neighbor = currentCoord.getNeighbor(dir);

        // --- INLINED VALIDATION LOGIC ---
        // 1. Bounds check
        boolean inBounds =
            neighbor.row() >= 0
                && neighbor.row() < stateView.getNumRows()
                && neighbor.col() >= 0
                && neighbor.col() < stateView.getNumCols();

        if (!inBounds) continue;

        // 2. Visited check
        if (visited.contains(neighbor)) continue;

        // 3. Wall check
        if (stateView.getTileState(neighbor) == Tile.State.WALL) continue;

        // If it passes all checks, it's a valid move
        visited.add(neighbor);
        Path<Coordinate> nextPath = new Path<>(currentPath, neighbor, 1d);
        frontier.push(nextPath);
      }
    }
    return null;
  }
}
