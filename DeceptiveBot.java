import java.util.concurrent.ThreadLocalRandom;
/** An optmized RPSLS player.
  * 
  * @author Paul, Brady 
  */
  public class DeceptiveBot implements RoShamBot {
    private Action[] moves = {Action.ROCK, Action.PAPER,Action.SCISSORS,Action.LIZARD,Action.SPOCK};
    private Action deception; //random move selection 
    private Action deceptionCounter; 
    private int deceptionCount = 0; //deception made from 0-5, 10-15

    private int strategyCount = 0; //number of moves needed before changin strategy
    private int intervalCount = 0;
    //store counterations for each action
    private float[] probabilites = {1.f, 0.f, 0.f, 0.f, 0.f};
    private boolean equilibrium = false;
    //track results of past five actions
    private Action[] oppActions = new Action[intervalCount];
    //switch to some random strategy for set amount of moves
        //pick two moves at random and set probabilty to .5
    private Boolean optimal_strategy =true;

    /** Returns an action according to the mixed strategy (0.5, 0.5, 0, 0, 0).
      * 
      * @param lastOpponentMove the action that was played by the opponent on
      *        the last round (this is disregarded).
      * @return the next action to play.
      */
    
    public Action getNextMove(Action lastOpponentMove) {
        if (strategyCount == 20){
            strategyCount =0;
            double coinFlip = Math.random();
            if (coinFlip<0.5){
                optimal_strategy =false;
                deceptionCount =0;
            }
            optimal_strategy = true;
        }
        //find current pure strategy being used by opponent
        //use the optimizing pure strategy based on that
        if (optimal_strategy){
            strategyCount++;
            return optimalStrategy();
        }
        else{
            strategyCount++;
            return deceptiveStrategy();
        }
        

    }
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
        if(intervalCount == 5)
        {
            computeProbabilities();
            oppActions = new Action[intervalCount];
            intervalCount = 0;
            equilibrium = false;
        }
        intervalCount++;
        checkEquilibrium();
        
        //when we hit equilibrium, probabilites all equal 0.2, then reset to only one move
        if(equilibrium)
        {
            return Action.ROCK;
        }

        //return an action depending on current probabilites
        double coinFlip = Math.random();
        
        if (coinFlip <= probabilites[0])
            return Action.ROCK;
        else if (coinFlip <= probabilites[1])
            return Action.PAPER;
        else if (coinFlip <= probabilites[2])
            return Action.SCISSORS;
        else if (coinFlip <= probabilites[3])
            return Action.LIZARD;
        else 
            return Action.SPOCK;
    }
    //compute current pure strategy
        //calculate oppenent's probablity of what action they take next turn
        //assign probability of our action based on that
    //order: rock, paper, scissors, lizard, and spock
    private void computeProbabilities()
    {
        int[] actionCounts = {0, 0, 0, 0, 0};
        for(int i = 0; i<oppActions.length; i++)
        {
            if(oppActions[i] == Action.ROCK)
            {
                actionCounts[1]++;
                actionCounts[4]++;
            }
            if(oppActions[i] == Action.PAPER)
            {
                actionCounts[2]++;
                actionCounts[3]++;
            }
            if(oppActions[i] == Action.SCISSORS)
            {
                actionCounts[0]++;
                actionCounts[4]++;
            }
            if(oppActions[i] == Action.LIZARD)
            {
                actionCounts[0]++;
                actionCounts[2]++;
            }
            if(oppActions[i] == Action.SPOCK)
            {
                actionCounts[1]++;
                actionCounts[3]++;
            }
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

    //method to check if the bot has hit equilibrium
    private void checkEquilibrium()
    {
        int count = 0;
        for(int i = 0; i<probabilites.length; i++)
        {
            if((int)(probabilites[i] * 10) == 2)
            {
                count++;
            }
        }
        if(count==5)
        {
            equilibrium = true;
        }
    }
}