import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import models.Agent;
import models.Game;
import models.GameVariation;
import models.RandomPlayer;

public class Main {

    public static void main(String[] args) {
        Agent agent=new Agent(Side.WHITE);
        Game game = new Game(agent, new RandomPlayer(Side.BLACK), GameVariation.Light);
        System.out.println(game.play());
        System.out.println("-----------------move counter XX:"+agent.moveCounter);
//        Board board = new Board();
//        board.loadFromFen("3k4/8/8/8/8/3q4/2P5/2K5 w KQkq - 0 1");
//        System.out.println(board.legalMoves());
    }

}
