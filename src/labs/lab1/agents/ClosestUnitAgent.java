package src.labs.lab1.agents;


// SYSTEM IMPORTS
import edu.bu.labs.lab1.Direction;
import edu.bu.labs.lab1.State.StateView;
import edu.bu.labs.lab1.agents.Agent;

import java.util.HashMap;
import java.util.Map;


// JAVA PROJECT IMPORTS
import edu.bu.labs.lab1.Tile;
import java.util.Set;
import edu.bu.labs.lab1.Coordinate;

public class ClosestUnitAgent
    extends Agent
{
    private Set<Integer> myUnitIds;
    private Coordinate exitLocation;

    // put your fields here! You will probably want to remember the following information:
    //      - all friendly unit ids (there may be more than one!)
    //      - the location(s) of COIN(s) on the map


    /**
     * The constructor for this type. Each agent has a unique ID that you will need to use to request info from the
     * state about units it controls, etc.
     */
	public ClosestUnitAgent(final int agentId)
	{
		super(agentId); // make sure to call parent type (Agent)'s constructor!

        // initialize your fields here!

        // helpful printout just to help debug
		System.out.println("Constructed ClosestUnitAgent");
	}

    /////////////////////////////// GETTERS AND SETTERS (this is Java after all) ///////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method is called by our game engine once: before any moves are made. You are provided with the state of
     * the game before any actions have been taken. This is in case you have some fields you need to set but are
     * unable to in the constructor of this class (like keeping track of units on the map, etc.).
     */
	@Override
	public void initializeFromState(final StateView stateView)
	{
        // TODO: identify units, set fields that couldn't be initialized in the constructor because
        // of a lack of game data in the constructor.
        this.myUnitIds = stateView.getUnitIds(this.getAgentId());
        for (int x = 0; x < stateView.getNumCols(); x++) {
            for (int y = 0; y < stateView.getNumRows(); y++) {
                Coordinate current = new Coordinate(y, x); // FIXED: (row, col)
                if (stateView.getTileState(current) == Tile.State.FINISH) {
                    this.exitLocation = current;
                }
              }
            }
                
	}

    /**
     * This method is called every turn (or "frame") of the game. Your agent is responsible for assigning
     * actions to each of the unit(s) your agent controls. The return type of this method is a mapping
     * from unit ID (that your agent controls) to the Direction you want that unit to move in.
     *
     * If you are trying to collect COIN(s), you do so by walking into the same square as a COIN. Your agent
     * will pick it up automatically (and the COIN will dissapear from the map).
     */
	@Override
	public Map<Integer, Direction> assignActions(final StateView state)
    {
        Map<Integer, Direction> actions = new HashMap<>();

        // TODO: your code to give your unit(s) actions for this turn goes here!
        Integer closestUnitId = null;
        double minDistance = Double.MAX_VALUE;

        // 1. Calculate distance for each unit and pick the smallest
        for (Integer unitId : myUnitIds) {
            Coordinate unitPos = state.getUnitView(this.getAgentId(), unitId).currentPosition();
            
            // Euclidean Formula: sqrt( (x1-x2)^2 + (y1-y2)^2 )
            // Since it's a Record, use .col() and .row()
            double dist = Math.sqrt(Math.pow(unitPos.col() - exitLocation.col(), 2) + 
                                    Math.pow(unitPos.row() - exitLocation.row(), 2));

            if (dist < minDistance) {
                minDistance = dist;
                closestUnitId = unitId;
            }
        }

        // 2. Move the winner (X-coordinate first, then Y)
        if (closestUnitId != null) {
            Coordinate pos = state.getUnitView(this.getAgentId(), closestUnitId).currentPosition();
            Direction nextMove = null;

            if (pos.col() < exitLocation.col()) {
                nextMove = Direction.RIGHT;
            } else if (pos.col() > exitLocation.col()) {
                nextMove = Direction.LEFT;
            } else if (pos.row() > exitLocation.row()) {
                nextMove = Direction.UP;
            } else if (pos.row() < exitLocation.row()) {
                nextMove = Direction.DOWN;
            }

            if (nextMove != null) {
                actions.put(closestUnitId, nextMove);
            }
        }

        return actions;
        
	}

}

