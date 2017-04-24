/**
 *
 * 
 * @author alfredo soto
 * class to handle if a user wants to see their score
 */

class ScoreCommand extends Command{
    private int score = GameState.instance().getScore();

/**
 * constructor of scorecommand
 */    
    ScoreCommand(){

    }
/**
 * prints out player's score
 * @return score
 */
    public String execute(){
        String scoreString = String.valueOf(score);
        return "You have " + scoreString + " points.";
    }
}