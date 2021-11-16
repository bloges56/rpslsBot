import java.util.*;
/** A player that identifies patterns in opponent's move history to predict their next move.
  *
  * @author Ben Pelczynski, Elliott Polin
  */
public class PatternBot implements RoShamBot {

    public String oppohis = "";
    private int PATTERNSIZE = 5;

    /** Returns an action according to the identified pattern (or random if no pattern is identified).
      *
      * @param lastOpponentMove the action that was played by the opponent on
      *        the last round.
      * @return the next action to play.
      */
    public Action getNextMove(Action lastOpponentMove) {
      if(lastOpponentMove == Action.ROCK){
        oppohis = oppohis.concat("R");
      }
      else if(lastOpponentMove == Action.PAPER){
        oppohis = oppohis.concat("P");
      }
      else if(lastOpponentMove == Action.SCISSORS){
        oppohis = oppohis.concat("S");
      }
      else if(lastOpponentMove == Action.LIZARD){
        oppohis = oppohis.concat("L");
      }
      else {
        oppohis = oppohis.concat("O");
      }
      if(oppohis.length() > PATTERNSIZE){
        String recent = oppohis.substring(oppohis.length() - PATTERNSIZE);
        String rest = oppohis.substring(0, oppohis.length() - PATTERNSIZE);
        int i = rest.lastIndexOf(recent);
        double coinFlip = Math.random();
        if(i != - 1){
          char next = oppohis.charAt(i + PATTERNSIZE);
          if(next == 'R'){
            if (coinFlip <= 1.0/2.0){
              return Action.PAPER;
            }
            else {
              return Action.SPOCK;
            }
          }
          else if(next == 'P'){
            if (coinFlip <= 1.0/2.0){
              return Action.SCISSORS;
            }
            else {
              return Action.LIZARD;
            }
          }
          else if(next == 'S'){
            if (coinFlip <= 1.0/2.0){
              return Action.ROCK;
            }
            else {
              return Action.SPOCK;
            }
          }
          else if(next == 'L'){
            if (coinFlip <= 1.0/2.0){
              return Action.ROCK;
            }
            else {
              return Action.SCISSORS;
            }
          }
          else {
            if (coinFlip <= 1.0/2.0){
              return Action.PAPER;
            }
            else {
              return Action.LIZARD;
            }
          }
        }
        else {
          return randomMove();
        }
      }
      return randomMove();
    }

    /** Returns a random action.
      *
      * @return a random action.
      */
    public Action randomMove() {
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
