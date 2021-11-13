/** A Nash Equilibrium Rock-Paper-Scissors player.
  *
  * @author MK and AD
  */
import java.util.LinkedList;
import java.util.Queue;
import java.util.Arrays;
import java.util.Collections;

  public class FinalBot5 implements RoShamBot {
    //Used to calculate winrate
    Queue<Action> OpponentQ = new LinkedList<>();
    Queue<Action> BestQ = new LinkedList<>();
   
    int maxStr = 100; //Size of match history string for opponent
    int NashScore;
    int FAScore;
    int counterFAScore;
    int doubleCounterFAScore;


    String OpponentStr = "";
    String OpponentFive = "";
    String FAStr = "";
    String NashStr = "";
    String counterFAStr = "";
    String doubleCounterFAStr = "";
    String bestStr = "";


    public int[] countMove(Queue<Action> Q){
        //Used for frequency analysis
        int rockCount = 0;
        int paperCount = 0;
        int scissorCount = 0;
        int lizardCount = 0;
        int spockCount = 0;
        int counter = 0;
        while (counter < Q.size()){
            Action removed = Q.remove();
            if (removed == Action.ROCK){
                rockCount += 1;
            }
            else if (removed == Action.PAPER){
                paperCount += 1;
            }
            else if (removed == Action.SCISSORS){
                scissorCount += 1;
            }
            else if (removed == Action.LIZARD){
                lizardCount += 1;
            }
            else {
                spockCount += 1;
            }
            Q.add(removed);
            counter += 1;
        }
        //System.out.println(Q.peek());
        int [] count = new int[] {rockCount, paperCount, scissorCount, lizardCount, spockCount};
        //System.out.println(Arrays. toString(count));
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

        //Update our data with the last opponent move
        if (OpponentQ.size() >= 50){ //Match history size =
            OpponentQ.add(lastOpponentMove);
            OpponentQ.remove();
        }
        else{
            OpponentQ.add(lastOpponentMove);
        }

        //Tracks the last five opponent moves
        // if (OpponentFive.length() >= 5){
        //     OpponentFive = convertToStr(lastOpponentMove) + OpponentFive;
        //     OpponentFive = OpponentFive.substring(0,5);
        // }
        // else{
        //     OpponentFive = convertToStr(lastOpponentMove) + OpponentFive;
        // }

        //Tracks the larger match history of opponent, capped by maxStr
        if (OpponentStr.length() >= maxStr){
            OpponentStr = convertToString(lastOpponentMove) + OpponentStr;
            OpponentStr = OpponentStr.substring(0,maxStr);
        }
        else{
            OpponentStr = convertToString(lastOpponentMove) + OpponentStr;
        }
       
        if (bestStr.length() >= 5){
            bestStr = bestStr.substring(0,5);
        }
       

        //Decide our bestMove
        Action bestMove;
        FAScore = 0;
        NashScore = 0;
        counterFAScore = 0;
        doubleCounterFAScore = 0;

        int bestStrategy = -100; //Since any strategy will have performed better than this
        int bestIndex = 0;
        int cap = 1;
        if (OpponentQ.size() > 1){
            if (NashStr.length() > cap) { //Caps the size of strategy string to 15
                NashStr = NashStr.substring(0,cap);
                FAStr = FAStr.substring(0,cap);
                counterFAStr = counterFAStr.substring(0,cap);
                doubleCounterFAStr = doubleCounterFAStr.substring(0,cap);
            }

            //Compare with first five OpponentStr
            for (int i =0; i < NashStr.length(); i++){
                FAScore += compareString(FAStr.charAt(i), OpponentStr.charAt(i));
                //System.out.println("FAScore is "+ String.valueOf(FAScore));
                NashScore += compareString(NashStr.charAt(i), OpponentStr.charAt(i));
                //System.out.println("NashScore is "+ String.valueOf(NashScore));
                counterFAScore += compareString(counterFAStr.charAt(i), OpponentStr.charAt(i));
                //System.out.println("counterFAScore is "+ String.valueOf(counterFAScore));
                doubleCounterFAScore += compareString(doubleCounterFAStr.charAt(i), OpponentStr.charAt(i));
            }

            int [] strategies = new int [] {FAScore, counterFAScore, doubleCounterFAScore, NashScore};

            for (int i =0; i<4; i++){ //i < number of strategies we employ
                if (strategies[i] > bestStrategy){
                    bestStrategy = strategies[i];
                    bestIndex = i;
                }
            }

            if (bestStrategy < 0){ //If we are losing more than we should
                return getNashMove();
            }

            Action FAMove = FrequencyAnalysisMove(OpponentQ);
            FAStr = convertToString(FAMove) + FAStr; //The most recent move is added to the left of the string
           
            Action NashMove = getNashMove();
            NashStr = convertToString(NashMove) + NashStr;
           
            Action counterFAMove = counterFrequencyAnalysis();
            counterFAStr = convertToString(counterFAMove) + counterFAStr;
           
            Action doubleCounterFAMove = doubleCounterFA();
            doubleCounterFAStr = convertToString(doubleCounterFAMove) + doubleCounterFAStr;

            //bestIndex = 0; We'll always use FA
            //Return the best move based on our best score
            if (NashProtection(OpponentStr, bestStr)){
                return NashMove;
            }
           
            if (bestIndex == 0){
                //System.out.println("FA used");
                bestStr = convertToString(FAMove) + bestStr;
                return FAMove;
            }
            else if (bestIndex == 1){
                //System.out.println("Counter FA used");
                bestStr = convertToString(counterFAMove) + bestStr;
                return counterFAMove;
            }
            else if (bestIndex == 2){
                //System.out.println("Double Counter FA used");
                bestStr = convertToString(doubleCounterFAMove) + bestStr;
                return doubleCounterFAMove;
            }
           
            else {
                //System.out.println("Nash used");
                bestStr = convertToString(NashMove) + bestStr;
                return NashMove;
            }
        }

        else{
            //For our first move, we use Nash move
            bestMove = getNashMove();
        }
        //System.out.println("Opponent move is "+ lastOpponentMove);
        //System.out.println("Bot move is" + bestMove);
        //BestQ.add(bestMove);
        return bestMove;
    }
   
    public boolean NashProtection(String opponent, String best){
        int score = 0;
        for (int i=0; i<best.length();i++){
               score += compareString(opponent.charAt(i), best.charAt(i));
        }
        if (score < 0){
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

    public Action counterFrequencyAnalysis(){
        Action opponent = FrequencyAnalysisMove(BestQ);
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
     
     public Action doubleCounterFA(){
        Action opponent = FrequencyAnalysisMove(BestQ);
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

    public Action FrequencyAnalysisMove(Queue<Action> Q) {
        //Frequency analysis
        int [] opponent = countMove(Q); //Get the frequency for each move given match history
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

     
    public Action MatchHistoryMove(String Q) {
        int size;
        if (Q.length() < 5){
            size = Q.length();
        }
        else{
            size = 20;
        }

        String Recent = Q.substring(0,size);
        String MH = Q.substring(21,Q.length());
        int index = MH.indexOf(Recent);

        if (index == -1){
            //System.out.println("FA is called!");
            return FrequencyAnalysisMove(OpponentQ);
        }

        char prediction;
        if (index != 0){
            prediction = MH.charAt(index-1);
        }
        else{
            prediction = Recent.charAt(4);
        }

        if (prediction == 'R'){
            //Find a way to choose between the two countermoves
            return Action.PAPER;
        }
        else if (prediction == 'P'){
            return Action.SCISSORS;
        }
        else if (prediction == 'S'){
            return Action.ROCK;
        }
        else if (prediction == 'L'){
            return Action.ROCK;
        }
        else{
            return Action.PAPER;
        }
      }
    }