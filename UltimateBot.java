import java.util.concurrent.ThreadLocalRandom;

import java.util.List;
import java.util.ArrayList;
/** An optmized RPSLS player.
  * 
  * @author Paul, Brady 
  */
  public class UltimateBot implements RoShamBot {
    private int numWin = 0; // number of wins
    private boolean defaultStrategy = true; // default move is optimal strategy
    private Action[] moves = {Action.ROCK, Action.PAPER,Action.SCISSORS,Action.LIZARD,Action.SPOCK};
    private Action deception; //random move selection 
    private Action deceptionCounter; 
    private int deceptionCount = 0; //deception made from 0-5, 10-15

    private int[] actionCounts = {0, 0, 0, 0, 0};
    private int intervalCount = 0;
    //store counterations for each action
    private float[] probabilites = {1.f, 0.f, 0.f, 0.f, 0.f};
    
    private List<Action> moveTracker = new ArrayList<Action>();
    //switch to some random strategy for set amount of moves
        //pick two moves at random and set probabilty to .5
    private List<Action> totalOppMoves = new ArrayList<Action>();
    private List<Action> totalMyMoves = new ArrayList<Action>();
    /** Returns an action according to the mixed strategy (0.5, 0.5, 0, 0, 0).
      * 
      * @param lastOpponentMove the action that was played by the opponent on
      *        the last round (this is disregarded).
      * @return the next action to play.
      */
    
    public Action getNextMove(Action lastOpponentMove) {
        intervalCount++;
        // defaultstrategy is optimal strategy, but switches to mixed strategy if optimal strategy doesn't work
        if (defaultStrategy){
        totalOppMoves.add(lastOpponentMove);
        if (totalOppMoves.size() == 10){
        computeWins();  //checks how many wins we have using optimal strategy
        if (numWin<=3){
            defaultStrategy = false;
        }
        totalOppMoves = new ArrayList<Action>();
        totalMyMoves = new ArrayList<Action>();
        }
    
        computeProbabilities();
        
        return optimalStrategy();
        
        }
        // switches to mixed strategy if defaultStrategy is false
     
        //find current pure strategy being used by opponent
        //use the optimizing pure strategy based on that
   
        return deceptiveStrategy();
        
   
    }
    // Lures opponents to make certain moves and then counter those moves under the
    // assumption that the opponent is scanning our moves
    private Action deceptiveStrategy(){
        if (deceptionCount==0 || deceptionCount==10){
        int randomNum = ThreadLocalRandom.current().nextInt(0, 5);
        deception = moves[randomNum];
        if (deception==Action.ROCK){
            deceptionCounter = Action.LIZARD;
        }
        else if (deception==Action.PAPER){
            deceptionCounter = Action.ROCK;
        }
        else if (deception ==Action.SCISSORS){
            deceptionCounter = Action.PAPER;
        }
        else if (deception == Action.LIZARD){
            deceptionCounter = Action.SPOCK;
        }
        else{
            deceptionCounter = Action.SCISSORS;
        }
        }
        else if (deceptionCount <5){
            deceptionCount++;
            return deception;
        }
        else if (deceptionCount <10){
            deceptionCount++;
            return deceptionCounter;
        }
        else if (deceptionCount <15){
            deceptionCount++;
            return deception;
        }
        deceptionCount++;
        return deceptionCounter;
        
        
    }
    private Action optimalStrategy(){
       
    

        //return an action depending on current probabilites
        double coinFlip = Math.random();
        
        if (coinFlip <= probabilites[0]){
            
            
            totalMyMoves.add(Action.ROCK);
        
        
            return Action.ROCK;}
        else if (coinFlip <= probabilites[1]){
            
            totalMyMoves.add(Action.PAPER);
            
            return Action.PAPER;
        }
        else if (coinFlip <= probabilites[2]){
            
            totalMyMoves.add(Action.SCISSORS);
            
            return Action.SCISSORS;
        }
        else if (coinFlip <= probabilites[3]){
            
            totalMyMoves.add(Action.LIZARD);
            
            return Action.LIZARD;
        }
        else{ 
            
            totalMyMoves.add(Action.SPOCK);
            
            return Action.SPOCK;
        }
    }
    //compute current pure strategy
        //calculate oppenent's probablity of what action they take next turn
        //assign probability of our action based on that
    //order: rock, paper, scissors, lizard, and spock
    private void computeProbabilities()
    {
        
        
            if(lastOpponentMove == Action.ROCK)
            {
                actionCounts[1]++;
                actionCounts[4]++;
            }
            if(lastOpponentMove == Action.PAPER)
            {
                actionCounts[2]++;
                actionCounts[3]++;
            }
            if(lastOpponentMove == Action.SCISSORS)
            {
                actionCounts[0]++;
                actionCounts[4]++;
            }
            if(lastOpponentMove == Action.LIZARD)
            {
                actionCounts[0]++;
                actionCounts[2]++;
            }
            if(lastOpponentMove == Action.SPOCK)
            {
                actionCounts[1]++;
                actionCounts[3]++;
            }
        

        float first_prob = (float)actionCounts[0]/2.f/(float)intervalCount;
        float sec_prob = first_prob + (float)actionCounts[1]/2.f/(float)intervalCount;
        float third_prob = sec_prob + (float)actionCounts[2]/2.f/(float)intervalCount;
        float fourth_prob = third_prob + (float)actionCounts[3]/2.f/(float)intervalCount;
        float fifth_prob = fourth_prob + (float)actionCounts[4]/2.f/(float)intervalCount;
        probabilites = new float[]{
            first_prob, 
            sec_prob, 
            third_prob, 
            fourth_prob, 
            fifth_prob};
    }
    //compute wins based on opponent and my moves
    private void computeWins(){
    int tempWin = 0;
    for (int i=0; i<totalMyMoves.size();i++){
        if(totalMyMoves.get(i)==Action.ROCK && 
        (totalOppMoves.get(i)==Action.SCISSORS || totalOppMoves.get(i)==Action.LIZARD)){
            tempWin ++;
        }
        else if(totalMyMoves.get(i)==Action.PAPER && 
        (totalOppMoves.get(i)==Action.ROCK || totalOppMoves.get(i)==Action.SPOCK)){
            tempWin ++;
        }
        else if(totalMyMoves.get(i)==Action.SCISSORS && 
        (totalOppMoves.get(i)==Action.PAPER || totalOppMoves.get(i)==Action.LIZARD)){
            tempWin ++;
        }
        else if(totalMyMoves.get(i)==Action.LIZARD && 
        (totalOppMoves.get(i)==Action.PAPER || totalOppMoves.get(i)==Action.SPOCK)){
            tempWin ++;
        }
        else if(totalMyMoves.get(i)==Action.SPOCK && 
        (totalOppMoves.get(i)==Action.ROCK || totalOppMoves.get(i)==Action.SCISSORS)){
            tempWin ++;
        }
        
    }
    numWin = tempWin;
    }
}
