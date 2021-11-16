/** A Nash Equilibrium Rock-Paper-Scissors player.

  * FinalBot6 with Random Deception to counter match-history
  
  * @author MK and AD
  */
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import static java.lang.Math.min;
import static java.lang.Math.max;


  public class FullHistoryBot implements RoShamBot {
    //Used to calculate winrate
    int NashScore = 0;
    int FAScore_V =0;
    int FAScore_X =0;
    int counterFAScore_V =0;
    int counterFAScore_X =0;
    int doubleCounterFAScore_V=0;
    int doubleCounterFAScore_X=0;
    int MHScore=0;
    int CounterMHScore=0;
    int CApeScore=0;
    int bestScore=0;
        
    int doubleCApeScore=0;
      
//     FAScore_V = 0;
//     FAScore_X = 0;
//     NashScore = 0;
//     counterFAScore_V = 0;
//     counterFAScore_X = 0;
//     doubleCounterFAScore_V = 0;        
//     doubleCounterFAScore_X = 0;
//     MHScore = 0;
//     CounterMHScore = 0;
//     CApeScore = 0;
//     bestScore = 0;

//     doubleCApeScore = 0;

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
      
    String doubleCApeStr = "";

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

        
        String bestStrategy;
        int bestScore = -100; //Since any strategy will have performed better than this
        int bestIndex = 0;
        int cap = 10; //Cap for available data
        int p_cap = 10;
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
                doubleCApeStr = doubleCApeStr.substring(0,cap);

            }
            
            //Compare with first five OpponentStr
            if (FAStr_V.length() > 0){
                FAScore_V += compareString(FAStr_V.charAt(0), OpponentStr.charAt(0));
                FAScore_X += compareString(FAStr_X.charAt(0), OpponentStr.charAt(0));
                NashScore += compareString(NashStr.charAt(0), OpponentStr.charAt(0));
                counterFAScore_V += compareString(counterFAStr_V.charAt(0), OpponentStr.charAt(0));
                counterFAScore_X +=  compareString(counterFAStr_X.charAt(0), OpponentStr.charAt(0));
                doubleCounterFAScore_V += compareString(doubleCounterFAStr_V.charAt(0), OpponentStr.charAt(0));
                doubleCounterFAScore_X += compareString(doubleCounterFAStr_X.charAt(0), OpponentStr.charAt(0));
                MHScore += compareString(MHStr.charAt(0), OpponentStr.charAt(0));
                CounterMHScore +=  compareString(CounterMHStr.charAt(0), OpponentStr.charAt(0));                
                CApeScore +=  compareString(CApeStr.charAt(0), OpponentStr.charAt(0));
                doubleCApeScore +=  compareString(doubleCApeStr.charAt(0), OpponentStr.charAt(0));
            }

            int [] scores = new int [] {MHScore, FAScore_V, counterFAScore_V, doubleCounterFAScore_V, FAScore_X, counterFAScore_X, doubleCounterFAScore_X, CounterMHScore, CApeScore, doubleCApeScore, NashScore}; 
            
            String [] strategies = new String [] {MHStr, FAStr_V, counterFAStr_V, doubleCounterFAStr_V, FAStr_X, counterFAStr_X, doubleCounterFAStr_X, CounterMHStr, CApeStr, doubleCApeStr, NashStr}; //
            
            for (int i =0; i<scores.length; i++){ //i < number of strategies we employ
                if (scores[i] > bestScore){
                    bestScore = scores[i];
                    bestStrategy = strategies[i];
                    bestIndex = i;
                }
            }
            System.out.println(bestIndex);
            
            Action MHMove = MatchHistoryMove(OpponentStr, min(OpponentStr.length(),160));
            MHStr = convertToString(MHMove) + MHStr;
            
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
            
            Action CounterMHMove = CounterMatchHistoryMove(bestStr, min(bestStr.length(),160));
            CounterMHStr = convertToString(CounterMHMove) + CounterMHStr; 
            
            Action CApeMove = FrequencyAnalysisMove(bestStr,min(bestStr.length(),1));
            CApeStr = convertToString(CApeMove) + CApeStr;
            
            Action doubleCApeMove = counterFrequencyAnalysis(1);
            doubleCApeStr = convertToString(doubleCApeMove) + doubleCApeStr;
            
            Action NashMove = getNashMove();
            NashStr = convertToString(NashMove) + NashStr;
            
            Action [] moves = new Action [] {MHMove, FAMove_V, counterFAMove_V, doubleCounterFAMove_V, FAMove_X, counterFAMove_X, doubleCounterFAMove_X, CounterMHMove, CApeMove, doubleCApeMove, NashMove};
            
            /////////////
            Random randomGenerator = new Random();
            int randInt = randomGenerator.nextInt(5);
            if (randInt == 0){
                bestStr = convertToString(NashMove) + bestStr;
                return NashMove;
            }
            ////////////
            
//             for (int i =0; i < min(10, bestStr.length()); i++){ 
//                 //Nash Protection
//                 bestScore += compareString(bestStr.charAt(i), OpponentStr.charAt(i));
//             }
        
            
            if (bestScore <= 0){ //If all our strategies are losing
                bestMove = NashMove;
                bestStr = convertToString(bestMove) + bestStr; //This updates the history of the moves actually used by our bot                
                return bestMove;
            }
            
            if (NashProtection(OpponentStr, bestStr)){
                //bestStr = convertToString(NashMove) + bestStr;
                //return NashMove;
                bestStr = convertToString(NashMove) + bestStr;
                return NashMove;
            }
            else{
                bestMove = moves[bestIndex];
                bestStr = convertToString(bestMove) + bestStr;
                return bestMove;
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
    
    public Action counter(Action best){
        //System.out.println(opponent);
        if (best == Action.ROCK){
            return Action.LIZARD;
        }
        else if (best == Action.PAPER){
            return Action.ROCK;
        }
        else if (best == Action.SCISSORS){
            return Action.PAPER;
        }
        else if (best == Action.LIZARD){
            return Action.SPOCK;
        }
        else {
            return Action.SCISSORS;
        }
    }
      
    public boolean NashProtection(String opponent, String best){
        int score = 0;
        if (best.length() < 5){
            return false;
        }
        for (int i=0; i< min(10, best.length());i++){
               score += compareString(best.charAt(i), opponent.charAt(i));
        }
        if (score < -1){
            return true; // We want to use Nash 
        }
        else{
            return false;
        }
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
            if (Q.length() < size){
                size = Q.length();
            }

            String Recent = Q.substring(0,size);
            String MH = Q.substring(size,Q.length());

            int index = MH.indexOf(Recent); //Where we find the history string

            if (index == -1 & size <= 3){
                //System.out.println("MH not found! FA is called!");
                return FrequencyAnalysisMove(OpponentStr, min(OpponentStr.length(),10));
            }
            else if (index == -1 & size > 3){
                //System.out.println("MH not found! FA is called!");
                return MatchHistoryMove(Q, size/2);
            }

            char prediction;
            if (index != 0){
                prediction = MH.charAt(index-1);
            }
            else{
                prediction = Recent.charAt(size-1);
            }

            if (prediction == 'R'){
                //Find a way to choose between the two countermoves
                return Action.LIZARD;
            }
            else if (prediction == 'P'){
                return Action.ROCK;
            }
            else if (prediction == 'S'){            
                return Action.PAPER;
            }
            else if (prediction == 'L'){            
                return Action.SPOCK;
            }
            else{ //this is SPOCK            
                return Action.SCISSORS;
            }
          }
    }