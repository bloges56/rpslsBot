import java.util.concurrent.ThreadLocalRandom;

import java.util.List;
import java.util.ArrayList;
/** An optmized RPSLS player.
  * 
  * @author Paul, Brady 
  */
  public class UltimateBot implements RoShamBot {
    private int numFreqLoss = 0; // number of wins for frequency strategy
    private int numPredLoss = 0; // number of wins for deceptive strategy
    private boolean begin = true;


    
    private Action[] moves = {Action.ROCK, Action.PAPER,Action.SCISSORS,Action.LIZARD,Action.SPOCK};


    private boolean deceptive = false; // deceptive strategy is occassionally introduced
    private Action deception; //random move selection 
    private Action deceptionCounter; //counter
    private int deceptionCount = 0; //deception made from 0-5

    private int freqCount = 0;
    private int predCount = 0;

    private int[] predictCounts = {0,0,0,0,0}; //store counters to my action
    private float[] predictFreq =  {0.f, 0.f, 0.f, 0.f, 0.f}; //probabilities for predict strategy

    
    private int[] actionCounts = {0, 0, 0, 0, 0}; //store counterations for each action
    private float[] probabilites = {0.f, 0.f, 0.f, 0.f, 0.f}; // probabilites for optimal strategy
    
    
    private List<Action> totalOppMoves = new ArrayList<Action>(); //tracks opponent moves

    private List<Action> totalFreqMoves = new ArrayList<Action>(); //tracks my moves for optimal strategy
    
    private List<Action> totalPredMoves = new ArrayList<Action>(); //tracks my moves for predict strategy
    

    private boolean apeCounter = false;
    private List<Action> totalMoves = new ArrayList<Action>(); //my moves

    private Action myLastMove;
    private Action opponentMove;


    private int nashCounter = 0;
    private boolean nash = false;
    /** Returns an action according to the mixed strategy (0.5, 0.5, 0, 0, 0).
      * 
      * @param lastOpponentMove the action that was played by the opponent on
      *        the last round (this is disregarded).
      * @return the next action to play.
      */
    
    public Action getNextMove(Action lastOpponentMove) {
        
        if (begin){
            totalFreqMoves.add(Action.ROCK);
            totalPredMoves.add(Action.ROCK);
            totalMoves.add(Action.ROCK);
        }
        begin = false;
        totalOppMoves.add(lastOpponentMove);
        if (totalOppMoves.size()>=3){
            int temp =0;
            int index = totalMoves.size()-1;
            int index2 = totalOppMoves.size()-1;
            if (totalMoves.get(index-1) == totalOppMoves.get(index2)){
                temp++;
            }
            if (totalMoves.get(index-2) == totalOppMoves.get(index2-1)){
                temp++;
            }
            if (temp==2){
                apeCounter = true;
            }
            else{
                apeCounter = false;
            }
        }
        if (apeCounter){
            if(myLastMove == Action.ROCK){
                totalFreqMoves.add(Action.PAPER);
                totalPredMoves.add(Action.PAPER);
                totalMoves.add(Action.PAPER);
                myLastMove = Action.PAPER;
                return Action.PAPER;
            }
            else if (myLastMove == Action.PAPER){
                totalFreqMoves.add(Action.SCISSORS);
                totalPredMoves.add(Action.SCISSORS);
                totalMoves.add(Action.SCISSORS);
                myLastMove = Action.SCISSORS;
                return Action.SCISSORS;
            }
            else if(myLastMove == Action.SCISSORS){
                totalFreqMoves.add(Action.ROCK);
                totalPredMoves.add(Action.ROCK);
                totalMoves.add(Action.ROCK);
                myLastMove = Action.ROCK;
                return Action.ROCK;
            }
            else if(myLastMove == Action.LIZARD){
                totalFreqMoves.add(Action.SCISSORS);
                totalPredMoves.add(Action.SCISSORS);
                totalMoves.add(Action.SCISSORS);
                myLastMove = Action.SCISSORS;
                return Action.SCISSORS;
            }
            else{
                totalFreqMoves.add(Action.LIZARD);
                totalPredMoves.add(Action.LIZARD);
                totalMoves.add(Action.LIZARD);
                myLastMove = Action.LIZARD;
                return Action.LIZARD;
            }
        }
        
        if (nash==true){
            nashCounter++;
            if (nashCounter ==5){
                nashCounter =0;
                nash = false;
                totalOppMoves = new ArrayList<Action>();
                totalFreqMoves =new ArrayList<Action>();
                totalFreqMoves.add(myLastMove);
                totalPredMoves  = new ArrayList<Action>();
                totalPredMoves.add(myLastMove);
            }
            else{
                myLastMove = nashEq();
                totalMoves.add(myLastMove);
                return myLastMove;
            }
        }
        opponentMove = lastOpponentMove;
        computeLoss();
        
        freqCount++;
        predCount++;
        computePredFreq();
        computeOptimalFreq();
        Action tempPred = predictStrategy();
        Action tempOpt = optimalStrategy();
        if (numPredLoss<numFreqLoss){
            myLastMove = tempPred;
        }
        else{
        myLastMove = tempOpt;}
        if (totalOppMoves.size() ==10){
            double tempFreq = numFreqLoss/freqCount; // frequency of loss
            double tempPFreq = numPredLoss/predCount; // frequency of loss
            totalOppMoves = new ArrayList<Action>();
            totalFreqMoves =new ArrayList<Action>();
            totalPredMoves  = new ArrayList<Action>();
            totalPredMoves.add(tempPred);
            totalFreqMoves.add(tempOpt);
            if (tempFreq>=0.4 && tempPFreq >=0.4){
                numPredLoss =0;
                numFreqLoss =0;
                predCount =0;
                freqCount =0;
                
                for (int i=0;i<predictCounts.length;i++){
                    predictCounts[i] = 0;
                }
                for (int i=0;i<predictFreq.length;i++){
                    predictFreq[i] = 0.f;
                }
                for (int i=0;i<actionCounts.length;i++){
                    actionCounts[i] = 0;
                }
                for (int i=0;i<probabilites.length;i++){
                    probabilites[i] = 0.f;
                }
                nashCounter++;
                nash = true;
                myLastMove = nashEq();
            }
            else if(tempPFreq >= 0.4){
                numPredLoss =0;
                predCount = 0;

                for (int i=0;i<predictCounts.length;i++){
                    predictCounts[i] = 0;
                }
                for (int i=0;i<predictFreq.length;i++){
                    predictFreq[i] = 0.f;
                }
                myLastMove = tempOpt;

            }
            else{
                numFreqLoss =0;
                freqCount =0;
                for (int i=0;i<actionCounts.length;i++){
                    actionCounts[i] = 0;
                }
                for (int i=0;i<probabilites.length;i++){
                    probabilites[i] = 0.f;
                }
                myLastMove = tempPred;

            }
            
        }
        if (deceptive==false){
            double coinFlip = Math.random();
            if (coinFlip <=0){
                deceptive = true;
            }
        }
        if (deceptive ==true){
            if (deceptionCount==10){
                deceptionCount =0;
                deceptive = false;
           }
            myLastMove =deceptiveStrategy();
            totalMoves.add(myLastMove);
            return myLastMove;
        }
        totalMoves.add(myLastMove);
        return myLastMove;
        // defaultstrategy is optimal strategy, but switches to mixed strategy if optimal strategy doesn't work
        
        // switches to mixed strategy if defaultStrategy is false
     
        //find current pure strategy being used by opponent
        //use the optimizing pure strategy based on that
    }
    // Lures opponents to make certain moves and then counter those moves under the
    // assumption that the opponent is scanning our moves
    private Action deceptiveStrategy(){
        if (deceptionCount<=5){
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
        deceptionCount++;
        return deception;
        }
    
        deceptionCount++;
        return deceptionCounter;
       
        
        
    }



    private Action predictStrategy(){
        //return an action depending on current probabilites
        double coinFlip = Math.random();
        
        if (coinFlip <= predictFreq[0]){
            
            
            totalPredMoves.add(Action.ROCK);
            
        
            return Action.ROCK;}
        else if (coinFlip <= predictFreq[1]){
            
            totalPredMoves.add(Action.PAPER);
            
            return Action.PAPER;
        }
        else if (coinFlip <= predictFreq[2]){
            
            totalPredMoves.add(Action.SCISSORS);
            
            return Action.SCISSORS;
        }
        else if (coinFlip <= probabilites[3]){
            
            totalPredMoves.add(Action.LIZARD);
            
            return Action.LIZARD;
        }
        else{ 
            
            totalPredMoves.add(Action.SPOCK);
            
            return Action.SPOCK;
        }
    }
    private void computePredFreq()
    {
        
        
            if(myLastMove == Action.ROCK)
            {
                predictCounts[3]++;
            }
            if(myLastMove == Action.PAPER)
            {
                predictCounts[0]++;
            }
            if(myLastMove == Action.SCISSORS)
            {
                predictCounts[1]++;
            }
            if(myLastMove == Action.LIZARD)
            {
                predictCounts[4]++;
            }
            if(myLastMove == Action.SPOCK)
            {
                predictCounts[2]++;
            }
        

        float first_prob = (float)predictCounts[0]/2.f/(float)predCount;
        float sec_prob = first_prob + (float)predictCounts[1]/2.f/(float)predCount;
        float third_prob = sec_prob + (float)predictCounts[2]/2.f/(float)predCount;
        float fourth_prob = third_prob + (float)predictCounts[3]/2.f/(float)predCount;
        float fifth_prob = fourth_prob + (float)predictCounts[4]/2.f/(float)predCount;
        predictFreq = new float[]{
            first_prob, 
            sec_prob, 
            third_prob, 
            fourth_prob, 
            fifth_prob};
    }

    private Action optimalStrategy(){
       
    

        //return an action depending on current probabilites
        double coinFlip = Math.random();
        
        if (coinFlip <= probabilites[0]){
            
            
            totalFreqMoves.add(Action.ROCK);
        
        
            return Action.ROCK;}
        else if (coinFlip <= probabilites[1]){
            
            totalFreqMoves.add(Action.PAPER);
            
            return Action.PAPER;
        }
        else if (coinFlip <= probabilites[2]){
            
            totalFreqMoves.add(Action.SCISSORS);
            
            return Action.SCISSORS;
        }
        else if (coinFlip <= probabilites[3]){
            
            totalFreqMoves.add(Action.LIZARD);
            
            return Action.LIZARD;
        }
        else{ 
            
            totalFreqMoves.add(Action.SPOCK);
            
            return Action.SPOCK;
        }
    }
    //compute current pure strategy
        //calculate oppenent's probablity of what action they take next turn
        //assign probability of our action based on that
    //order: rock, paper, scissors, lizard, and spock
    private void computeOptimalFreq()
    {
        
        
            if(opponentMove == Action.ROCK)
            {
                actionCounts[1]++;
                actionCounts[4]++;
            }
            if(opponentMove == Action.PAPER)
            {
                actionCounts[2]++;
                actionCounts[3]++;
            }
            if(opponentMove == Action.SCISSORS)
            {
                actionCounts[0]++;
                actionCounts[4]++;
            }
            if(opponentMove == Action.LIZARD)
            {
                actionCounts[0]++;
                actionCounts[2]++;
            }
            if(opponentMove == Action.SPOCK)
            {
                actionCounts[1]++;
                actionCounts[3]++;
            }
        

        float first_prob = (float)actionCounts[0]/2.f/(float)freqCount;
        float sec_prob = first_prob + (float)actionCounts[1]/2.f/(float)freqCount;
        float third_prob = sec_prob + (float)actionCounts[2]/2.f/(float)freqCount;
        float fourth_prob = third_prob + (float)actionCounts[3]/2.f/(float)freqCount;
        float fifth_prob = fourth_prob + (float)actionCounts[4]/2.f/(float)freqCount;
        probabilites = new float[]{
            first_prob, 
            sec_prob, 
            third_prob, 
            fourth_prob, 
            fifth_prob};
    }
    //compute losses based on opponent and my moves
    private void computeLoss(){
    int tempFreqLoss = 0;
    int tempPredLoss = 0;
    for (int i=0; i<totalOppMoves.size();i++){
        if(totalOppMoves.get(i)==Action.ROCK){ 
             
        if (totalFreqMoves.get(i)==Action.SCISSORS || totalFreqMoves.get(i)==Action.LIZARD){
            tempFreqLoss++;
        }
        if (totalPredMoves.get(i)==Action.SCISSORS || totalPredMoves.get(i)==Action.LIZARD){
            tempPredLoss++;
        }
        }
        else if(totalOppMoves.get(i)==Action.PAPER){ 
        if (totalFreqMoves.get(i)==Action.ROCK || totalFreqMoves.get(i)==Action.SPOCK){
            tempFreqLoss++;
        }
         if (totalPredMoves.get(i)==Action.ROCK || totalPredMoves.get(i)==Action.SPOCK){
            tempPredLoss++;
        }
        }
        else if(totalOppMoves.get(i)==Action.SCISSORS){
        if (totalFreqMoves.get(i)==Action.PAPER || totalFreqMoves.get(i)==Action.LIZARD){
            tempFreqLoss++;
        }
        if (totalPredMoves.get(i)==Action.PAPER || totalPredMoves.get(i)==Action.LIZARD){
            tempPredLoss++;
        }
        }
        else if(totalOppMoves.get(i)==Action.LIZARD){ 
        if (totalFreqMoves.get(i)==Action.PAPER || totalFreqMoves.get(i)==Action.SPOCK){
            tempFreqLoss++;
        }
        if (totalPredMoves.get(i)==Action.PAPER || totalPredMoves.get(i)==Action.SPOCK){
            tempPredLoss++;
        }
        }
        else{
        if (totalFreqMoves.get(i)==Action.ROCK || totalFreqMoves.get(i)==Action.SCISSORS){
            tempFreqLoss++;
        }
        if (totalPredMoves.get(i)==Action.ROCK || totalPredMoves.get(i)==Action.SCISSORS){
            tempPredLoss++;
        }
        }
        
    }
    numFreqLoss =tempFreqLoss;
    numPredLoss = tempPredLoss;
    

    }
    private Action nashEq(){
        double coinFlip = Math.random();
        
        if (coinFlip <= 1.0/5.0)

            return Action.ROCK;
        else if (coinFlip <= 2.0/5.0)
            return Action.PAPER;
        else if (coinFlip <= 3.0/5.0)
            return Action.SCISSORS;
        else if (coinFlip <= 4.0/5.0)
            return Action.LIZARD;
        else 
            return Action.SPOCK; 
    }
    

}
