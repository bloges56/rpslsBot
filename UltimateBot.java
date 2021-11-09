public class UltimateBot {
    
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