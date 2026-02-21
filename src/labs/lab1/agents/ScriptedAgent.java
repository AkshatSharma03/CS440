package src.labs.lab1.agents;

// SYSTEM IMPORTS
import edu.bu.labs.lab1.Coordinate;
import edu.bu.labs.lab1.Direction;
import edu.bu.labs.lab1.State.StateView;
import edu.bu.labs.lab1.Tile;
import edu.bu.labs.lab1.agents.Agent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ScriptedAgent extends Agent {
  private Integer myUnitId;
  private Coordinate coinLocation;
  private Coordinate exitLocation;
  private boolean hasPickedUpCoin;

  public ScriptedAgent(final int agentId) {
    super(agentId);
    this.myUnitId = null;
    this.coinLocation = null;
    this.exitLocation = null;
    this.hasPickedUpCoin = false;
    System.out.println("Constructed ScriptedAgent");
  }

  @Override
  public void initializeFromState(final StateView stateView) {
    Set<Integer> myUnitIds = stateView.getUnitIds(this.getAgentId());
    this.myUnitId = myUnitIds.iterator().next();

    for (int x = 0; x < stateView.getNumCols(); x++) {
      for (int y = 0; y < stateView.getNumRows(); y++) {
        Coordinate current = new Coordinate(y, x);
        Tile.State type = stateView.getTileState(current);
        if (type == Tile.State.COIN) {
          this.coinLocation = current;
        } else if (type == Tile.State.FINISH) {
          this.exitLocation = current;
        }
      }
    }
  }

  @Override
  public Map<Integer, Direction> assignActions(final StateView state) {
    Map<Integer, Direction> actions = new HashMap<>();
    Coordinate myPos = state.getUnitView(this.getAgentId(), this.myUnitId).currentPosition();

    if (this.coinLocation != null && myPos.equals(this.coinLocation)) {
      this.hasPickedUpCoin = true;
    }

    Coordinate currentTarget =
        (!hasPickedUpCoin && coinLocation != null) ? coinLocation : exitLocation;

    if (currentTarget != null) {
      Direction nextMove = null;
      if (myPos.col() < currentTarget.col()) nextMove = Direction.RIGHT;
      else if (myPos.col() > currentTarget.col()) nextMove = Direction.LEFT;
      else if (myPos.row() < currentTarget.row()) nextMove = Direction.DOWN;
      else if (myPos.row() > currentTarget.row()) nextMove = Direction.UP;

      if (nextMove != null) {
        actions.put(this.myUnitId, nextMove);
      }
    }
    return actions;
  }
}
