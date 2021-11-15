
public class Bot2 implements RoShamBot{
    public Action getNextMove(Action lastOpponentMove) {
    // This function returns the opposite of the opponent's last move
    double coinFlip = Math.random();

        if (lastOpponentMove == Action.SCISSORS){
            if (coinFlip <= 0.5){
                lastOpponentMove = Action.ROCK;
                return lastOpponentMove;
            }
            else{
                lastOpponentMove = Action.SPOCK;
                return lastOpponentMove;
            }
        }
        else if (lastOpponentMove == Action.PAPER){
            if (coinFlip <= 0.5){
                lastOpponentMove = Action.LIZARD;
                return lastOpponentMove;
            }
            else{
                lastOpponentMove = Action.SCISSORS;
                return lastOpponentMove;
            }
        }
        else if (lastOpponentMove == Action.ROCK){
            if (coinFlip <= 0.5){
                lastOpponentMove = Action.SPOCK;
                return lastOpponentMove;
            }
            else{
                lastOpponentMove = Action.PAPER;
                return lastOpponentMove;
            }
        }
        else if (lastOpponentMove == Action.SPOCK){
            if (coinFlip <= 0.5){
                lastOpponentMove = Action.PAPER;
                return lastOpponentMove;
            }
            else{
                lastOpponentMove = Action.LIZARD;
                return lastOpponentMove;
            }

        }
        else{
            if (coinFlip <= 0.5){
                lastOpponentMove = Action.SCISSORS;
                return lastOpponentMove;
            }
            else {
                lastOpponentMove = Action.ROCK;
                return lastOpponentMove;
            }

        }
    }
}
