/** A Nash Equilibrium Rock-Paper-Scissors player.

  * FinalBot6 with Random Deception to counter match-history
  
  * @author MK and AD
  */
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import static java.lang.Math.min;


  public class RoBot implements RoShamBot {
    //Used to calculate winrate
    int NashScore;
    int FAScore_V;
    int FAScore_X;
    int counterFAScore_V;
    int counterFAScore_X;
    int doubleCounterFAScore_V;
    int doubleCounterFAScore_X;
    int MHScore;
    int CounterMHScore;
    int CApeScore;


    String OpponentStr = "";
    String FAStr_V = "";
    String FAStr_X = "";
    String counterFAStr_V = "";
    String counterFAStr_X = "";
    String doubleCounterFAStr_V = "";
    String doubleCounterFAStr_X = "";
    String MHStr = "";
    String CounterMHStr = "";
    String CApeStr = "";
    String bestStr = "";
    String NashStr = "";

    public int[] countMove(String Q, int size){ //Size defines what index we are reaching back to
        //Used for frequency analysis
        int rockCount = 0;
        int paperCount = 0;
        int scissorCount = 0;
        int lizardCount = 0;
        int spockCount = 0;
        int counter = 0;
        for (int i=0; i< size; i++){
           if (Q.charAt(i) == 'R'){
                rockCount += 1;
            }
            else if (Q.charAt(i) == 'P'){
                paperCount += 1;
            }
            else if (Q.charAt(i) == 'S'){
                scissorCount += 1;
            }
            else if (Q.charAt(i) == 'L'){
                lizardCount += 1;
            }
            else {
                spockCount += 1;
            } 
        }
        int [] count = new int[] {rockCount, paperCount, scissorCount, lizardCount, spockCount};
        return count;
    }
 
    /** Returns an action using match history.
      * 
      * @param lastOpponentMove the action that was played by the opponent on
      *        the last round (this is disregarded).
      * @return the next action to play.
      */
    public Action getNextMove(Action lastOpponentMove) {
        /**Employ best strategy based on winrate of each strategy
         * 
         */

        OpponentStr = convertToString(lastOpponentMove) + OpponentStr;
        

        //Decide our bestMove
        Action bestMove;
        FAScore_V = 0;
        FAScore_X = 0;
        NashScore = 0;
        counterFAScore_V = 0;
        counterFAScore_X = 0;
        doubleCounterFAScore_V = 0;        
        doubleCounterFAScore_X = 0;
        MHScore = 0;
        CounterMHScore = 0;
        CApeScore = 0;

        int bestStrategy = -100; //Since any strategy will have performed better than this
        int bestIndex = 0;
        int cap = 10; //Cap for available data
        int[] caps = new int [] {1,5,10}; //Cap for performance assessment
        int p_cap = 3;
        int weight = 0;
        
        if (OpponentStr.length() > 1){
            if (NashStr.length() > cap) { //Caps the size of strategy string to 5
                NashStr = NashStr.substring(0,cap);
                FAStr_V = FAStr_V.substring(0,cap);
                FAStr_X = FAStr_X.substring(0,cap);
                counterFAStr_V = counterFAStr_V.substring(0,cap);
                counterFAStr_X = counterFAStr_X.substring(0,cap);
                doubleCounterFAStr_V = doubleCounterFAStr_V.substring(0,cap);
                doubleCounterFAStr_X = doubleCounterFAStr_X.substring(0,cap);
                MHStr = MHStr.substring(0,cap);
                CounterMHStr = CounterMHStr.substring(0,cap);
                CApeStr = CApeStr.substring(0,cap);
            }

            //Compare with first five OpponentStr
            for (int i =0; i < p_cap; i++){ //Runs p_cap times
                if (NashStr.length() < i+1){
                    break;
                }
                if (i==0){
                    weight = 5;
                }
                else if (i==1){
                    weight = 2;
                }
                else{
                    weight = 1;
                }
                
                FAScore_V += weight * compareString(FAStr_V.charAt(i), OpponentStr.charAt(i));
                FAScore_X += weight * compareString(FAStr_X.charAt(i), OpponentStr.charAt(i));
                NashScore += weight * compareString(NashStr.charAt(i), OpponentStr.charAt(i));
                counterFAScore_V += weight * compareString(counterFAStr_V.charAt(i), OpponentStr.charAt(i));
                counterFAScore_X += weight * compareString(counterFAStr_X.charAt(i), OpponentStr.charAt(i));
                doubleCounterFAScore_V += weight * compareString(doubleCounterFAStr_V.charAt(i), OpponentStr.charAt(i));
                doubleCounterFAScore_X += weight * compareString(doubleCounterFAStr_X.charAt(i), OpponentStr.charAt(i));
                MHScore += weight * compareString(MHStr.charAt(i), OpponentStr.charAt(i));
                CounterMHScore += weight * compareString(CounterMHStr.charAt(i), OpponentStr.charAt(i));                
                CApeScore += weight * compareString(CApeStr.charAt(i), OpponentStr.charAt(i));
//                 if (i < 1){
//                     FAScore_1 += compareString(FAStr.charAt(i), OpponentStr.charAt(i));
//                     NashScore_1 += compareString(NashStr.charAt(i), OpponentStr.charAt(i));
//                     counterFAScore_1 += compareString(counterFAStr.charAt(i), OpponentStr.charAt(i));
//                     doubleCounterFAScore_1 += compareString(doubleCounterFAStr.charAt(i), OpponentStr.charAt(i));
//                     MHScore_1 += compareString(MHStr.charAt(i), OpponentStr.charAt(i));
//                     CApeScore_1 += compareString(CApeStr.charAt(i), OpponentStr.charAt(i));
//                 }
//                 if (i < 5){
//                     FAScore_5 += compareString(FAStr.charAt(i), OpponentStr.charAt(i));
//                     NashScore_5 += compareString(NashStr.charAt(i), OpponentStr.charAt(i));
//                     counterFAScore_5 += compareString(counterFAStr.charAt(i), OpponentStr.charAt(i));
//                     doubleCounterFAScore_5 += compareString(doubleCounterFAStr.charAt(i), OpponentStr.charAt(i));
//                     MHScore_5 += compareString(MHStr.charAt(i), OpponentStr.charAt(i));
//                     CApeScore_5 += compareString(CApeStr.charAt(i), OpponentStr.charAt(i));
//                 }
//                 FAScore_X += compareString(FAStr.charAt(i), OpponentStr.charAt(i));
//                 NashScore_X += compareString(NashStr.charAt(i), OpponentStr.charAt(i));
//                 counterFAScore_X += compareString(counterFAStr.charAt(i), OpponentStr.charAt(i));
//                 doubleCounterFAScore_X += compareString(doubleCounterFAStr.charAt(i), OpponentStr.charAt(i));
//                 MHScore_X += compareString(MHStr.charAt(i), OpponentStr.charAt(i));
//                 CApeScore_X += compareString(CApeStr.charAt(i), OpponentStr.charAt(i));
                
            }
//             System.out.println("FAScore is "+ String.valueOf(FAScore));                
//             System.out.println("counterFAScore is "+ String.valueOf(counterFAScore));
//             System.out.println("doubleCounterFAScore is "+ String.valueOf(doubleCounterFAScore));
//             System.out.println("MHScore is "+ String.valueOf(MHScore));
//             System.out.println("CApeScore is "+ String.valueOf(CApeScore));
//             System.out.println("NashScore is "+ String.valueOf(NashScore));

            int [] strategies = new int [] {FAScore_V, counterFAScore_V, doubleCounterFAScore_V, FAScore_X, counterFAScore_X, doubleCounterFAScore_X, MHScore, CounterMHScore, CApeScore, NashScore};
            
            for (int i =0; i<strategies.length; i++){ //i < number of strategies we employ
                if (strategies[i] > bestStrategy){
                    bestStrategy = strategies[i];
                    bestIndex = i;
                }
            }

            Action FAMove_X = FrequencyAnalysisMove(OpponentStr,min(OpponentStr.length(),10));
            FAStr_X = convertToString(FAMove_X) + FAStr_X; //The most recent move is added to the left of the string
            
            Action FAMove_V = FrequencyAnalysisMove(OpponentStr,min(OpponentStr.length(),5));
            FAStr_V = convertToString(FAMove_V) + FAStr_V; //The most recent move is added to the left of the string
            
            Action counterFAMove_V = counterFrequencyAnalysis(5);
            counterFAStr_V = convertToString(counterFAMove_V) + counterFAStr_V;
            
            Action counterFAMove_X = counterFrequencyAnalysis(10);
            counterFAStr_X = convertToString(counterFAMove_X) + counterFAStr_X;
            
            Action doubleCounterFAMove_V = doubleCounterFA(5);
            doubleCounterFAStr_V = convertToString(doubleCounterFAMove_V) + doubleCounterFAStr_V;
            
            Action doubleCounterFAMove_X = doubleCounterFA(10);
            doubleCounterFAStr_X = convertToString(doubleCounterFAMove_X) + doubleCounterFAStr_X;
            
            Action MHMove = MatchHistoryMove(OpponentStr, min(OpponentStr.length(),80));
            MHStr = convertToString(MHMove) + MHStr;
            
            Action CounterMHMove = CounterMatchHistoryMove(OpponentStr, min(OpponentStr.length(),80));
            CounterMHStr = convertToString(CounterMHMove) + CounterMHStr; 
            
            Action CApeMove = FrequencyAnalysisMove(bestStr,min(bestStr.length(),1));
            CApeStr = convertToString(CApeMove) + CApeStr;
            
            Action NashMove = getNashMove();
            NashStr = convertToString(NashMove) + NashStr;
            
            if (bestStrategy < 0){ //If we are losing more than we should
                bestMove = getNashMove();
                bestStr = convertToString(bestMove) + bestStr; //This updates the history of the moves actually used by our bot                
                return bestMove;
            }
            
            if (bestIndex == 0){
                System.out.println("FA_V used");
                bestStr = convertToString(FAMove_V) + bestStr; //This updates the history of the moves actually used by our bot                
                return FAMove_V;
            }
            else if (bestIndex == 1){
                System.out.println("Counter FA_V used");
                bestStr = convertToString(counterFAMove_V) + bestStr;                
                return counterFAMove_V;
            }
            else if (bestIndex == 2){
                System.out.println("Double Counter FA_V used");
                bestStr = convertToString(doubleCounterFAMove_V) + bestStr;                  
                return doubleCounterFAMove_V;
            }
            
            else if (bestIndex == 3){
                System.out.println("FA_X used");
                bestStr = convertToString(FAMove_X) + bestStr; //This updates the history of the moves actually used by our bot                
                return FAMove_X;
            }
            else if (bestIndex == 4){
                System.out.println("Counter FA_X used");
                bestStr = convertToString(counterFAMove_X) + bestStr;                
                return counterFAMove_X;
            }
            else if (bestIndex == 5){
                System.out.println("Double Counter FA_X used");
                bestStr = convertToString(doubleCounterFAMove_X) + bestStr;                  
                return doubleCounterFAMove_X;
            }
            
            
            else if (bestIndex == 6){
                System.out.println("Match History used");
                bestStr = convertToString(MHMove) + bestStr;                
                return MHMove;
            }    
            else if (bestIndex == 7){
                System.out.println("Counter Match History used");
                bestStr = convertToString(CounterMHMove) + bestStr;                
                return CounterMHMove;
            } 
            else if (bestIndex == 8){
                System.out.println("Counter Ape used");
                bestStr = convertToString(CApeMove) + bestStr;
                return CApeMove;
            }
            else {
                System.out.println("Nash used");
                bestStr = convertToString(NashMove) + bestStr;
                return NashMove;
            }
        }

        else{
            //For our first move, we use Nash move
            bestMove = getNashMove();
            //bestStr = convertToString(bestMove) + bestStr;
        }
        //System.out.println("Opponent move is "+ lastOpponentMove);
        //System.out.println("Bot move is" + bestMove);
        bestStr = convertToString(bestMove) + bestStr;
        return bestMove;
    }

    public String convertToString(Action A){
        if (A == Action.ROCK){
            return "R";
        }
        else if (A == Action.PAPER){
            return "P";
        }
        else if (A == Action.SCISSORS){
            return "S";
        }
        else if (A == Action.LIZARD){
            return "L";
        }
        else {
            return "K";
        }
    }

    public int compareString(char challenger, char opponent){
        if (challenger == 'R'){
            if (opponent == 'R'){
                return 0;
            }
            else if (opponent == 'P'){
                return -1;
            }
            else if (opponent == 'S'){
                return 1;
            }
            else if (opponent == 'L'){
                return 1;
            }
            else {
                return -1;
            }
        }
        else if (challenger == 'P'){
            if (opponent == 'R'){
                return 1;
            }
            else if (opponent == 'P'){
                return 0;
            }
            else if (opponent == 'S'){
                return -1;
            }
            else if (opponent == 'L'){
                return -1;
            }
            else {
                return 1;
            }        
        }
        else if (challenger == 'S'){
            if (opponent == 'R'){
                return -1;
            }
            else if (opponent == 'P'){
                return 1;
            }
            else if (opponent == 'S'){
                return 0;
            }
            else if (opponent == 'L'){
                return 1;
            }
            else {
                return -1;
            }
        }
        else if (challenger == 'L'){
            if (opponent == 'R'){
                return -1;
            }
            else if (opponent == 'P'){
                return 1;
            }
            else if (opponent == 'S'){
                return -1;
            }
            else if (opponent == 'L'){
                return 0;
            }
            else {
                return 1;
            }
        }
        else { //THIS IS SPOCK
            if (opponent == 'R'){
                return 1;
            }
            else if (opponent == 'P'){
                return -1;
            }
            else if (opponent == 'S'){
                return 1;
            }
            else if (opponent == 'L'){
                return -1;
            }
            else {
                return 0;
            }   
        }
    }

    public Action counterFrequencyAnalysis(int size){
        Action opponent = FrequencyAnalysisMove(bestStr,min(bestStr.length(),size));
        if (opponent == Action.ROCK){
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(1);
            if (randomInt == 0){
                return Action.PAPER;
            }
            else{
                return Action.SPOCK;
            }
        }
        else if (opponent == Action.PAPER){
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(1);
            if (randomInt == 0){
                return Action.SCISSORS;
            }
            else{
                return Action.LIZARD;
            }
        }
        else if (opponent == Action.SCISSORS){
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(1);
            if (randomInt == 0){
                return Action.ROCK;
            }
            else{
                return Action.SPOCK;
            }
        }
        else if (opponent == Action.LIZARD){
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(1);
            if (randomInt == 0){
                return Action.ROCK;
            }
            else{
                return Action.SCISSORS;
            }
        }
        else {
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(1);
            if (randomInt == 0){
                return Action.PAPER;
            }
            else{
                return Action.LIZARD;
            }
        }
    }
      
     public Action doubleCounterFA(int size){
        Action opponent = FrequencyAnalysisMove(bestStr,min(bestStr.length(),size));
        //System.out.println(opponent);
        if (opponent == Action.ROCK){
            return Action.LIZARD;
        }
        else if (opponent == Action.PAPER){
            return Action.ROCK;
        }
        else if (opponent == Action.SCISSORS){
            return Action.PAPER;
        }
        else if (opponent == Action.LIZARD){
            return Action.SPOCK;
        }
        else {
            return Action.SCISSORS;
        }
    }
    

    public Action getNashMove() {
        double coinFlip = Math.random();
        
        if (coinFlip <= 1.0/5.0){
            return Action.ROCK;
        }
        else if (coinFlip <= 2.0/5.0){
            return Action.PAPER;
        }
        else if (coinFlip <= 3.0/5.0){
            return Action.SCISSORS;
        }
        else if (coinFlip <= 4.0/5.0){
            return Action.LIZARD;
        }
        else {
            return Action.SPOCK;
        }
    }

    public Action FrequencyAnalysisMove(String Q, int size) {
        //Frequency analysis
        int [] opponent = countMove(Q, size); //Get the frequency for each move given match history
        int rockPoints; 
        int paperPoints; 
        int scissorPoints; 
        int lizardPoints; 
        int spockPoints; 
        
        //Calculate the scores for each move
        rockPoints = opponent[2] + opponent[3] - opponent[1] - opponent[4]; //Reward for wins, penalize for loss
        paperPoints = opponent[0] + opponent[4] - opponent[2] - opponent[3]; // Beats rock and Spock, loses to scissors and lizard
        scissorPoints = opponent[1] + opponent[3] - opponent[0] - opponent[4];
        lizardPoints = opponent[1] + opponent[4] - opponent[0] - opponent[2];
        spockPoints = opponent[0] + opponent[2] - opponent[1] - opponent[3];

        //Store the points in an array so we can iterate through and compare
        int [] movePoints = new int[] {rockPoints, paperPoints, scissorPoints, lizardPoints, spockPoints};
        int currentBestScore = 0;
        int currentBestIndex =0;

        //Locate the best move in the array
        for (int i = 0; i < 5; i++){
            if (movePoints[i] >= currentBestScore){
                currentBestScore = movePoints[i];
                currentBestIndex = i;
            }
        }

        //Return the best move based on the index
        if (currentBestIndex == 0){
            return Action.ROCK;
        }
        else if (currentBestIndex == 1){
            return Action.PAPER;
        }
        else if (currentBestIndex == 2){
            return Action.SCISSORS;
        }
        else if (currentBestIndex == 3){
            return Action.LIZARD;
        }
        else {
            return Action.SPOCK;
        }
        //IS this returning the prediction or the counter?
    }

    public Action MatchHistoryMove(String Q, int cap) {
        int size;
        if (Q.length() < cap){
            size = Q.length();
        }
        else{
            size = cap;
        }

        String Recent = Q.substring(0,size);
        String MH = Q.substring(size,Q.length());
        
        int index = MH.indexOf(Recent); //Where we find the history string

        if (index == -1 & cap <= 3){
            //System.out.println("MH not found! FA is called!");
            return FrequencyAnalysisMove(OpponentStr, min(OpponentStr.length(),10));
        }
        else if (index == -1 & cap > 3){
            //System.out.println("MH not found! FA is called!");
            return MatchHistoryMove(Q, cap/2);
        }

        char prediction;
        if (index != 0){
            prediction = MH.charAt(index-1);
        }
        else{
            prediction = Recent.charAt(cap-1);
        }

        if (prediction == 'R'){
            //Find a way to choose between the two countermoves
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(1);
            if (randomInt == 0){
                return Action.PAPER;
            }
            else{
                return Action.SPOCK;
            }
        }
        else if (prediction == 'P'){
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(1);
            if (randomInt == 0){
                return Action.SCISSORS;
            }
            else{
                return Action.LIZARD;
            }
        }
        else if (prediction == 'S'){            
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(1);
            if (randomInt == 0){
                return Action.ROCK;
            }
            else{
                return Action.SPOCK;
            }
        }
        else if (prediction == 'L'){            
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(1);
            if (randomInt == 0){
                return Action.ROCK;
            }
            else{
                return Action.SCISSORS;
            }
        }
        else{ //this is SPOCK            
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(1);
            if (randomInt == 0){
                return Action.PAPER;
            }
            else{
                return Action.LIZARD;
            }
        }
      }
      
        public Action CounterMatchHistoryMove(String Q, int size) {
           Action opponent = MatchHistoryMove(bestStr, size);
            if (opponent == Action.ROCK){
                return Action.PAPER;
            }
            else if (opponent == Action.PAPER){
                return Action.SCISSORS;
            }
            else if (opponent == Action.SCISSORS){
                return Action.ROCK;
            }
            else if (opponent == Action.LIZARD){
                return Action.ROCK;
            }
            else {
                return Action.PAPER;
            }
        }
    }