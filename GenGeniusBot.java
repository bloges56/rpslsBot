import java.util.ArrayList;
import java.util.Random;

/** A weird Rock-Paper-Scissors player.
  *
  * @author Umut
  */
  public class GenGeniusBot implements RoShamBot {

    Action myPrevMove;
    Action penultimateOppMov;
    Action penultimateMyMov;
    ArrayList <Action> myActions = new ArrayList<Action>();
    ArrayList <Action> enemyActions = new ArrayList<Action>();
    boolean firstRound = true;
    boolean secondRound = true;

    // initialize a qTable
    // Rows are in order of: (R,P,S,L,P) ^ Rock, Paper, Scissors, Lizard, Spock
    // First move is bot's move, second move is opponent's move
    // Columns are: Actions: Rock, Paper, Scissors, Lizard, Spock
    double[][] qTable = new double[25][5];
    double[][] qTable2 = new double[25][5];
    //presets
    double initialQVals = 10;
    double loseReward = -2;
    double winReward = 2;
    double tieReward = 0;
    double learningRate = 0.01;
    double discountFactor = 0.1;
    int noiseCounter = 0;
    int noiseStarter = 2;

    // finding the current state index for the qtable
    private int findState(Action myAct, Action oppAct){
        int myActInd = actionIndexer(myAct);
        int myOppInd = actionIndexer(oppAct);
        int curIndex = (myActInd * 5) + myOppInd;
        return curIndex;
    }
    // finding the given index of an action
    private int actionIndexer(Action move){
        if(move == Action.ROCK){
            return 0;
        }
        else if(move == Action.PAPER){
            return 1;
        }
        else if(move == Action.SCISSORS){
            return 2;
        }
        else if(move == Action.LIZARD){
            return 3;
        }
        return 4;
    }

    private Action playNash(){
        double fiveSidedFlip = Math.random();

        if (fiveSidedFlip <= 1.0/5.0){
            return Action.ROCK;
        }
        else if (fiveSidedFlip <= 2.0/5.0){
            return Action.PAPER;
        }
        else if (fiveSidedFlip <= 3.0/5.0){
            return Action.SCISSORS;
        }
        else if (fiveSidedFlip <= 4.0/5.0){
            return Action.LIZARD;
        }
        else{
            return Action.SPOCK;
        }
    }

    private double stateReward(int curInd){
        double reward;
        if (curInd == 0){
            reward = tieReward;
        }
        else if (curInd == 1){
            reward = loseReward;
        }
        else if (curInd == 2){
            reward = winReward;
        }
        else if (curInd == 3){
            reward = winReward;
        }
        else if (curInd == 4){
            reward = loseReward;
        }
        else if (curInd == 5){
            reward = winReward;
        }
        else if (curInd == 6){
            reward = tieReward;
        }
        else if (curInd == 7){
            reward = loseReward;
        }
        else if (curInd == 8){
            reward = loseReward;
        }
        else if (curInd == 9){
            reward = winReward;
        }
        else if (curInd ==10){
            reward = loseReward;
        }
        else if (curInd == 11){
            reward = winReward;
        }
        else if (curInd == 12){
            reward = tieReward;
        }
        else if (curInd == 13){
            reward = winReward;
        }
        else if (curInd == 14){
            reward = loseReward;
        }
        else if (curInd == 15){
            reward = loseReward;
        }
        else if (curInd == 16){
            reward = winReward;
        }
        else if (curInd == 17){
            reward = loseReward;
        }
        else if (curInd == 18){
            reward = tieReward;
        }
        else if (curInd == 19){
            reward = winReward;
        }
        else if (curInd == 20){
            reward = winReward;
        }
        else if (curInd == 21){
            reward = loseReward;
        }
        else if (curInd == 22){
            reward = winReward;
        }
        else if (curInd == 23){
            reward = loseReward;
        }
        else{
            reward = tieReward;
        }
        return reward;
    }

    private Action getNextAction(int actionIndex){
        if(actionIndex == 0){
            return Action.ROCK;
        }
        else if(actionIndex == 1){
            return Action.PAPER;
        }
        else if(actionIndex == 2){
            return Action.SCISSORS;
        }
        else if(actionIndex == 3){
            return Action.LIZARD;
        }else{
            return Action.SPOCK;
        }
    }

    public Action getNextMove(Action lastOpponentMove) {
        double coinFlip = Math.random();
        // play the first round randomly.
        if(firstRound || secondRound){

            if (firstRound == false){
                //at round 2...
                secondRound = false;
                enemyActions.add(lastOpponentMove);
                Action curAct = playNash();
                myActions.add(curAct);
                return curAct;
            }

            if(firstRound && secondRound){
                // at round 1
                //fill up the values in the qTable
                firstRound = false;
                for(int x = 0; x < 25; x++){
                    for(int y = 0; y < 5; y++){
                        qTable[x][y] = initialQVals;
                    }
                }
                for(int x = 0; x < 25; x++){
                    for(int y = 0; y < 5; y++){
                        qTable2[x][y] = initialQVals;
                    }
                }
                Action curAct = playNash();
                myActions.add(curAct);
                return curAct;
            }
        }

        // add some noise to the data
        // every 50 rounds, play 5 rounds of nash equilibrium
        if(noiseStarter%50 == 0){
            Action nextAction = playNash();
            noiseCounter++;
            if(noiseCounter > 5){
                noiseStarter++;
                return nextAction;
            }
            return nextAction;
        }

        if(noiseStarter%250 == 0){
            learningRate = learningRate * 2;
            discountFactor = discountFactor * 1.5;
            if(coinFlip >= 0.5){
                for(int x = 0; x < 25; x++){
                    for(int y = 0; y < 5; y++){
                        qTable[x][y] = initialQVals;
                    }
                }
           }else{
                for(int x = 0; x < 25; x++){
                    for(int y = 0; y < 5; y++){
                        qTable2[x][y] = initialQVals;
                    }
                }
           }


        }

        // if not the first round, start the qlearning process...
        // find the current state
        enemyActions.add(lastOpponentMove);
        int curStateInd = findState(myActions.get(myActions.size()-1), enemyActions.get(enemyActions.size()-1));

        // find the previous state
        int prevStateInd = findState(myActions.get(myActions.size()-2), enemyActions.get(enemyActions.size()-2));
        int prevActionInd = actionIndexer(myActions.get(myActions.size()-2));
        // update the qTable from the previous round
        double prevQVal = qTable[prevStateInd][prevActionInd];
        double prevQVal2 = qTable2[prevStateInd][prevActionInd];
        double estimateOptVal = qTable[curStateInd][0];
        double estimateOptVal2 = qTable2[curStateInd][0];

        for (int x = 1; x < 5; x++){

            if(estimateOptVal < qTable[curStateInd][x]){
                estimateOptVal = qTable[curStateInd][x];
            }
            if(estimateOptVal2 < qTable2[curStateInd][x]){
                estimateOptVal2 = qTable2[curStateInd][x];
            }
        }




        double previousStateReward = stateReward(prevStateInd);
        // update the qvalue of previous state on qTable


        if(coinFlip <= 0.5){
            qTable[prevStateInd][prevActionInd] = prevQVal + (learningRate * (previousStateReward + (discountFactor * estimateOptVal2) - prevQVal));
        }else{
            qTable2[prevStateInd][prevActionInd] = prevQVal2 + (learningRate * (previousStateReward + (discountFactor * estimateOptVal) - prevQVal2));
        }
        int nextActionIndex = 0;
        double optQVal = qTable[curStateInd][0];
        double optQVal2 = qTable2[curStateInd][0];

        if(coinFlip <= 0.5){
            for (int x = 1; x < 5; x++){

                if(optQVal < qTable[curStateInd][x]){
                    nextActionIndex = x;
                    optQVal = qTable[curStateInd][x];
                }
            }
        }else{
            for (int x = 1; x < 5; x++){

                if(optQVal2 < qTable2[curStateInd][x]){
                    nextActionIndex = x;
                    optQVal2 = qTable2[curStateInd][x];
                }
            }
        }

        ArrayList <Integer> candidateList = new ArrayList<Integer>();

        if(coinFlip <= 0.5){
            for (int x = 0; x < 5; x++){

                if(optQVal == qTable[curStateInd][x]){
                    candidateList.add(x);
                }
            }
        }else{
            for (int x = 0; x < 5; x++){
                if(optQVal2 == qTable2[curStateInd][x]){
                    candidateList.add(x);
                }
            }
        }

        int rnd = new Random().nextInt(candidateList.size());
        nextActionIndex = candidateList.get(rnd);

        // find the best optimal-future-value in this state from qtable
        Action myNextAction = getNextAction(nextActionIndex);
        myActions.add(myNextAction);
        myPrevMove = myNextAction;
        noiseStarter++;
        return myNextAction;
    }
}
