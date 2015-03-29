package connectFour;

import java.util.List;
import java.util.ArrayList;

/**
 * An instance represents a Solver that intelligently determines
 * Moves using the Minimax algorithm.
 */
public class AI implements Solver {

    private Player player; // the current player

    /**
     * The depth of the search in the game space when evaluating moves.
     */
    private int depth;

    /**
     * Constructor: an instance with player p who searches to depth d
     * when searching the game space for moves.
     */
    public AI(Player p, int d) {
        player = p;
        depth = d;
    }

    /**
     * See Solver.getMoves for the specification.
     */
    @Override
    public Move[] getMoves(Board b) {
    	
        State state = new State(player,b,null);
        createGameTree(state,depth);
        minimax(state);
        
        List<Move> moveList = new ArrayList<>();
        for(State s : state.getChildren()){
            if(s.getValue()==state.getValue()){
                moveList.add(s.getLastMove());
            }
        }

        Move[] result = {};

        return moveList.toArray(result);
        
    }

    /**
     * Generate the game tree with root s of depth d.
     * The game tree's nodes are State objects that represent the state of a game
     * and whose children are all possible States that can result from the next move.
     * <p/>
     * NOTE: this method runs in exponential time with respect to d.
     * With d around 5 or 6, it is extremely slow and will start to take a very
     * long time to run.
     * <p/>
     * Note: If s has a winner (four in a row), it should be a leaf.
     */
    public static void createGameTree(State s, int d) {
        // Note: This method must be recursive, recurse on d,
        // which should get smaller with each recursive call
    	
    	if(d == 0){
    		return;
    	}else{
            s.initializeChildren();
            for(State child : s.getChildren()){
            	// recursion
                createGameTree(child,d-1);
            }

        }
    }

    /**
     * Call minimax in ai with state s.
     */
    public static void minimax(AI ai, State s) {
        ai.minimax(s);
    }

    /**
     * State s is a node of a game tree (i.e. the current State of the game).
     * Use the Minimax algorithm to assign a numerical value to each State of the
     * tree rooted at s, indicating how desirable that java.State is to this player.
     */
    public void minimax(State s) {
    	
    	// at a leaf node
        if(s.getChildren().length == 0) { 
            s.setValue(evaluateBoard(s.getBoard()));
        } else { 
        	int val;
        	// if we are playing then we need to determine the maximum value of the child states
            if(s.getPlayer() == player){
            	// find the max value from the children states
            	val = Integer.MIN_VALUE;
            	for(State children : s.getChildren()){
                    minimax(children);      
                    if (children.getValue() > val)
                    	val = children.getValue();
                }
            // if we aren't playing then we need to determine the minimum value of the child states
            } else {
            	// find the min value from the children states
            	val = Integer.MAX_VALUE;
            	for(State children : s.getChildren()){
                    minimax(children);      
                    if (children.getValue() < val)
                    	val = children.getValue();
            	}
            	// allocate value to parent node 
            	
            	
            	
            	
            }
            s.setValue(val);
        }
    }

    /**
     * Evaluate the desirability of Board b for this player
     * Precondition: b is a leaf node of the game tree (because that is most
     * effective when looking several moves into the future).
     */
    public int evaluateBoard(Board b) {
        Player winner = b.hasConnectFour();
        int value = 0;
        if (winner == null) {
            // Store in sum the value of board b. 
            List<Player[]> locs = b.winLocations();
            for (Player[] loc : locs) {
                for (Player p : loc) {
                    value += (p == player ? 1 : p != null ? -1 : 0);
                }
            }
        } else {
            // There is a winner
            int numEmpty = 0;
            for (int r = 0; r < Board.NUM_ROWS; r = r + 1) {
                for (int c = 0; c < Board.NUM_COLS; c = c + 1) {
                    if (b.getTile(r, c) == null) numEmpty += 1;
                }
            }
            value = (winner == player ? 1 : -1) * 10000 * numEmpty;
        }
        return value;
    }
}
