import java.util.*;
/** A player that acts random but has its rates manipulated by history.
  *
  * @author Ben Pelczynski, Elliott Polin
  */
public class FakeRandomBot implements RoShamBot {

    public double diverge = .0005;
    public double converge = .001;
    public double[] odds = new double[5];
    public ArrayList<Action> actList = new ArrayList<Action>();
    public Action lastMove;

    /** Initializes the odds and action arrays.
      *
      */
    public FakeRandomBot() {
      lastMove = Action.ROCK;
      for (int i=0; i < 5; i++){
        odds[i] = .2;
      }
      actList.add(Action.ROCK);
      actList.add(Action.PAPER);
      actList.add(Action.SCISSORS);
      actList.add(Action.LIZARD);
      actList.add(Action.SPOCK);
    }

    /** Returns an action according to initally random rates.
      *
      * @param lastOpponentMove the action that was played by the opponent on
      *        the last round.
      * @return the next action to play.
      */
    public Action getNextMove(Action lastOpponentMove) {
      boolean outcome = moveCheck(lastMove, lastOpponentMove);
      if (lastMove != lastOpponentMove) {
        if (outcome && (odds[actList.indexOf(lastMove)] + converge <= 1)) {
          if (odds[actList.indexOf(lastMove)] < .2) {
            for (int i=0; i < 5; i++){
              if (i == actList.indexOf(lastMove)) {
                odds[i] += converge;
              }
              else {
                odds[i] -= (converge * .25);
              }
            }
          }
          else {
            for (int i=0; i < 5; i++){
              if (i == actList.indexOf(lastMove)) {
                odds[i] += diverge;
              }
              else {
                odds[i] -= (diverge * .25);
              }
            }
          }
        }
        else if (odds[actList.indexOf(lastMove)] - converge >= 0){
          if (odds[actList.indexOf(lastMove)] > .2) {
            for (int i=0; i < 5; i++){
              if (i == actList.indexOf(lastMove)) {
                odds[i] -= converge;
              }
              else {
                odds[i] += (converge * .25);
              }
            }
          }
          else {
            for (int i=0; i < 5; i++){
              if (i == actList.indexOf(lastMove)) {
                odds[i] -= diverge;
              }
              else {
                odds[i] += (diverge * .25);
              }
            }
          }
        }
      }

      double coinFlip = Math.random();
      if (coinFlip <= odds[0]) {
        lastMove = Action.ROCK;
        return Action.ROCK;
      }
      else if (coinFlip <= odds[0] + odds[1]) {
        lastMove = Action.PAPER;
        return Action.PAPER;
      }
      else if (coinFlip <= odds[0] + odds[1] + odds[2]) {
        lastMove = Action.SCISSORS;
        return Action.SCISSORS;
      }
      else if (coinFlip <= odds[0] + odds[1] + odds[2] + odds[3]) {
        lastMove = Action.LIZARD;
        return Action.LIZARD;
      }
      else {
        lastMove = Action.SPOCK;
        return Action.SPOCK;
      }
  }

    /** Checks to see who won the last match.
      *
      * @param a1 p1's action.
      * @param a2 p2's action.
      * @return boolean of if p1 won.
      */
    public boolean moveCheck(Action a1, Action a2) {
      boolean p1Win = (((a1 == Action.SCISSORS) && (a2 == Action.PAPER)) ||
                       ((a1 == Action.PAPER) && (a2 == Action.ROCK)) ||
                       ((a1 == Action.ROCK) && (a2 == Action.LIZARD)) ||
                       ((a1 == Action.LIZARD) && (a2 == Action.SPOCK)) ||
                       ((a1 == Action.SPOCK) && (a2 == Action.SCISSORS)) ||
                       ((a1 == Action.SCISSORS) && (a2 == Action.LIZARD)) ||
                       ((a1 == Action.LIZARD) && (a2 == Action.PAPER)) ||
                       ((a1 == Action.PAPER) && (a2 == Action.SPOCK)) ||
                       ((a1 == Action.SPOCK) && (a2 == Action.ROCK)) ||
                       ((a1 == Action.ROCK) && (a2 == Action.SCISSORS)));
      return p1Win;
    }

}
