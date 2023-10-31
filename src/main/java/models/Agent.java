package models;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import java.util.LinkedList;

import java.util.List;
import java.util.Random;

public class Agent extends Player {

    private final static int maxDepth = 3;
    public int moveCounter=0;
    private int turn=1;//1=max turn, 0=min turn
    private int cutoff;
    private int unsafePoints[][];

    public Agent(Side side) {
        super(side);
    }

    public int stateEval(Board board) {//asb=600,vazir=1400,sarbaz=200,shah=10000 ;
       
        int blackScore=0;
        int whiteScore=0;
        for (int i = 0; i < 64; i++) {
            if(board.getPiece(Square.squareAt(i)).getPieceSide()==Side.WHITE){
                int iReverse=63-i;
                if(board.getPiece(Square.squareAt(i)).getPieceType().value().equalsIgnoreCase("PAWN")){
                    whiteScore+=200;
                    //position scores:
                    
                    if(iReverse>=0 && iReverse<=7){
                        whiteScore+=0;
                    }else if(iReverse>=8 && iReverse<=15){
                        whiteScore+=50;
                    }else if(iReverse==16 || iReverse==17 || iReverse==22 || iReverse==23 || iReverse==26 || iReverse==29 || iReverse==49 || iReverse==50 || iReverse==53 || iReverse==54){
                        whiteScore+=10;
                    }else if(iReverse==18 || iReverse==21 || iReverse==35 || iReverse==36){
                        whiteScore+=20;
                    }else if(iReverse==19 || iReverse==20){
                        whiteScore+=30;
                    }else if(iReverse==24 || iReverse==25 || iReverse==40 || iReverse==47 || iReverse==48 || iReverse==56){
                        whiteScore+=5;
                    }else if(iReverse==27 || iReverse==28 || iReverse==30 || iReverse==31){
                        whiteScore+=25;
                    }else if(iReverse==41 || iReverse==46){
                        whiteScore-=5;
                    }else if(iReverse==42 || iReverse==45){
                        whiteScore-=10;
                    }else if(iReverse==51 || iReverse==52){
                        whiteScore-=20;
                    }
                    
                }else if(board.getPiece(Square.squareAt(i)).getPieceType().value().equalsIgnoreCase("QUEEN")){
                    whiteScore+=1400;
                    //position scores:
                    if(i==0 || i==7 || i==8 || i==15 || i==48 || i==55 || i==58 || i==61){
                        whiteScore-=40;
                    }else if(i==1 ||i==3 || i==6 || i==9 || i==14 ||
                            i==16 || i==23){
                        whiteScore+=0;
                    }else if(i==2 || i==5 || i==50 || i==53){
                        whiteScore-=20;
                    }else if(i==4 || i==17){
                        whiteScore-=10;
                    }else if(i==10 || i==13 || i==18 || i==21 || i==40 || i==47){
                        whiteScore+=20;
                    }else if(i==11 || i==12 || i==22 || i==24 || i==31 || i==32 || i==39){
                        whiteScore+=10;
                    }else if(i==19 || i==20 || i==25 || i==30 || i==33 || i==38 || 
                            i==41 || i==46){
                        whiteScore+=30;
                    }else if(i==26 || i==29 || (i>=42 && i<=45)){
                        whiteScore+=40;
                    }else if(i==27 || i==28 || (i>=34 && i<=37) ){
                        whiteScore+=50;
                    }else if(i==49 || i==54 || i==59 || i==60){
                        whiteScore-=30;
                    }else if(i==51 || i==52 || i==56 || i==57 || i==62 || i==63){
                        whiteScore-=50;
                    }
                }else if(board.getPiece(Square.squareAt(i)).getPieceType().value().equalsIgnoreCase("KING")){
                    whiteScore+=10000;
                    //position scores:
                    if(iReverse==0 || iReverse==7 || iReverse==8 || iReverse==15 || iReverse==16 || iReverse==23 
                            || iReverse==24 || iReverse==31 || iReverse==33 || iReverse==34 || iReverse==37 || iReverse==38){
                        whiteScore-=30;
                    }else if(iReverse==1 || iReverse==2 || iReverse==5 || iReverse==6 || iReverse==9 ||
                            iReverse==10 || iReverse==13 || iReverse==14 || iReverse==17 || iReverse==18 ||
                            iReverse==21 || iReverse==22 || iReverse==25 || iReverse==26 || iReverse==29 || iReverse==30 ||
                            iReverse==35 || iReverse==36){
                        whiteScore-=40;
                    }else if(iReverse==3 || iReverse==4 || iReverse==1 || iReverse==12 || iReverse==19 
                            || iReverse==20 || iReverse==27 || iReverse==28){
                        whiteScore-=50;
                    }else if(iReverse==32 || iReverse==39 || (iReverse>=41&&iReverse>=46) ){
                        whiteScore-=20;
                    }else if(iReverse==40 || iReverse==47){
                        whiteScore-=10;
                    }else if(iReverse==48 || iReverse==49 || iReverse==54 || iReverse==55 || iReverse==56 || iReverse==63){
                        whiteScore+=20;
                    }else if(iReverse==57 || iReverse==62){
                        whiteScore+=30;
                    }else if(iReverse==58 || iReverse==61){
                        whiteScore+=10;
                    }
                    
                }
            }else if(board.getPiece(Square.squareAt(i)).getPieceSide()==Side.BLACK){
                if(board.getPiece(Square.squareAt(i)).getPieceType().value().equalsIgnoreCase("PAWN")){
                    blackScore+=200;
                    //position scores:
                    if(i>=0 && i<=7){
                        blackScore+=0;
                    }else if(i>=8 && i<=15){
                        blackScore+=50;
                    }else if(i==16 || i==17 || i==22 || i==23 || i==26 || i==29 || i==49 || i==50 || i==53 || i==54){
                        blackScore+=10;
                    }else if(i==18 || i==21 || i==35 || i==36){
                        blackScore+=20;
                    }else if(i==19 || i==20){
                        blackScore+=30;
                    }else if(i==24 || i==25 || i==40 || i==47 || i==48 || i==56){
                        blackScore+=5;
                    }else if(i==27 || i==28 || i==30 || i==31){
                        blackScore+=25;
                    }else if(i==41 || i==46){
                        blackScore-=5;
                    }else if(i==42 || i==45){
                        blackScore-=10;
                    }else if(i==51 || i==52){
                        blackScore-=20;
                    }
                }else if(board.getPiece(Square.squareAt(i)).getPieceType().value().equalsIgnoreCase("KNIGHT")){
                    blackScore+=600;
                    //position scores:
                    if(i==0 || i==7 || i==48 || i==55){
                        blackScore-=50;
                    }else if(i==1 || i==3 || i==4 || i==6 || i==49 || i==54){
                        blackScore-=40;
                    }else if(i==2 || i==5 || i==8 || i==15 || i==50 || i==53){
                        blackScore-=30;
                    }else if(i==9 || i==12 || i==51 || i==52){
                        blackScore-=20;
                    }else if(i==10 || i==14 || i==20 || i==25 || i==30 || (i>=34 && i<=37)){
                        blackScore+=40;
                    }else if(i==11 || i==13 || i==16 || i==17 || i==22 ||i==23 || i==60){
                        blackScore-=10;
                    }else if(i==18 || i==24 || i==31 || i==32 || i==39 || i==41 || i==46){
                        blackScore+=20;
                    }else if(i==19 || i==21 || (i>=26 && i<=29)){
                        blackScore+=50;
                    }else if(i==33 || i==38 || (i>=42 && i<=45)){
                        blackScore+=30;
                    }else if(i==40 || i==47){
                        blackScore+=10;
                    }
                }else if(board.getPiece(Square.squareAt(i)).getPieceType().value().equalsIgnoreCase("KING")){
                    blackScore+=10000;
                    //position scores:
                    if(i==0 || i==7 || i==8 || i==15 || i==16 || i==23 || i==24 || i==31 ||
                            i==33 || i==34 || i==37 || i==38){
                        blackScore-=30;
                    }else if(i==1 || i==2 || i==5 || i==6 || i==9 || i==10 || i==13 || i==14 ||
                            i==17 || i==18 || i==21 || i==22 || i==25 || i==26 || i==29 || i==30 ||
                            i==35 || i==36){
                        blackScore-=40;
                    }else if(i==3 || i==4 || i==1 || i==12 || i==19 || i==20 || i==27 || i==28){
                        blackScore-=50;
                    }else if(i==32 || i==39 || (i>=41&&i>=46) ){
                        blackScore-=20;
                    }else if(i==40 || i==47){
                        blackScore-=10;
                    }else if(i==48 || i==49 || i==54 || i==55 || i==56 || i==63){
                        blackScore+=20;
                    }else if(i==57 || i==62){
                        blackScore+=30;
                    }else if(i==58 || i==61){
                        blackScore+=10;
                    }
                }
            }
        }
        
        return whiteScore-blackScore;
//        return 0;
    }
    
    public int MoveEval(Board board,Move move){
        int moveScore=0;
        if(board.doMove(move)){
            if(board.isMated()){
                moveScore-=10000;
            }else if(board.isKingAttacked()){
                moveScore-=100;
            }
            moveScore+=attackScore(board.getPiece(move.getTo()),move.getTo(),board);
            board.undoMove();
        }
        return moveScore;
    }

    public int attackScore(Piece piece, Square pieceSquare, Board board){ //return threat score
        PieceType pieceType = piece.getPieceType();
        Side pieceSide=piece.getPieceSide();
        int indexOfSquare=0;
        int resultScore=0;
        for ( int i = 0 ; i < 63 ; i++ ) {
            if(Square.squareAt(i) == pieceSquare){
                indexOfSquare=i;
            }
        }
        int[] IJ= IndexToIJ(indexOfSquare);
        int row=IJ[0];
        int col=IJ[1];
        switch (pieceType){
            case PAWN:
                //first possible capture
                int index = IJtoIndex(row-1, col-1);
                if ( index!= -1 ) {
                    Piece thePiece = board.getPiece(Square.squareAt(index));
                    if ( thePiece.getPieceSide() != pieceSide ) {
                        if ( thePiece.getPieceType() == PieceType.PAWN ) {
                            resultScore += 200;
                        } else if ( thePiece.getPieceType() == PieceType.KNIGHT ) {
                            resultScore += 600;
                        } else if ( thePiece.getPieceType() == PieceType.QUEEN ) {
                            resultScore += 1400;
                        } else if ( thePiece.getPieceType() == PieceType.KING ) {
                            resultScore += 10000;
                        }
                    }
                }
                //second possible capture
                index = IJtoIndex(row-1, col+1);
                if ( index!= -1 ) {
                    Piece thePiece = board.getPiece(Square.squareAt(index));
                    if ( thePiece.getPieceSide() != pieceSide ) {
                        if ( thePiece.getPieceType() == PieceType.PAWN ) {
                            resultScore += 200;
                        } else if ( thePiece.getPieceType() == PieceType.KNIGHT ) {
                            resultScore += 600;
                        } else if ( thePiece.getPieceType() == PieceType.QUEEN ) {
                            resultScore += 1400;
                        } else if ( thePiece.getPieceType() == PieceType.KING ) {
                            resultScore += 10000;
                        }
                    }
                }
                break;
            case KNIGHT:
                int[] possibleIndex =new int[8];
                possibleIndex[0]=IJtoIndex(row-2, col-1);
                possibleIndex[1]=IJtoIndex(row-2, col+1);
                possibleIndex[2]=IJtoIndex(row-1, col+2);
                possibleIndex[3]=IJtoIndex(row+1, col+2);
                possibleIndex[4]=IJtoIndex(row+2, col+1);
                possibleIndex[5]=IJtoIndex(row+2, col-1);
                possibleIndex[6]=IJtoIndex(row+1, col-2);
                possibleIndex[7]=IJtoIndex(row-1, col-2);
                for ( int i = 0 ; i < possibleIndex.length ; i++ ) {
                    if ( possibleIndex[i] != -1 ){
                        Piece thePiece = board.getPiece(Square.squareAt(possibleIndex[i]));
                        if ( thePiece.getPieceSide() != pieceSide ) {
                            if ( thePiece.getPieceType() == PieceType.PAWN ) {
                                resultScore += 200;
                            } else if ( thePiece.getPieceType() == PieceType.KNIGHT ) {
                                resultScore += 600;
                            } else if ( thePiece.getPieceType() == PieceType.QUEEN ) {
                                resultScore += 1400;
                            } else if ( thePiece.getPieceType() == PieceType.KING ) {
                                resultScore += 10000;
                            }
                        }
                    }
                }
                break;
            case QUEEN:
                int idx=1;
                while ( IJtoIndex(row-idx, col)!= -1) {
                    Piece thePiece = board.getPiece(Square.squareAt(IJtoIndex(row-idx, col)));
                    if ( thePiece.getPieceSide() != pieceSide ) {
                        if ( thePiece.getPieceType() == PieceType.PAWN ) {
                            resultScore += 200;
                        } else if ( thePiece.getPieceType() == PieceType.KNIGHT ) {
                            resultScore += 600;
                        } else if ( thePiece.getPieceType() == PieceType.QUEEN ) {
                            resultScore += 1400;
                        } else if ( thePiece.getPieceType() == PieceType.KING ) {
                            resultScore += 10000;
                        }
                        break;
                    }else if ( thePiece.getPieceSide() == pieceSide  ){
                        break;
                    }
                    idx++;
                }idx=1;
                while ( IJtoIndex(row+idx, col)!= -1) {
                    Piece thePiece = board.getPiece(Square.squareAt(IJtoIndex(row+idx, col)));
                    if ( thePiece.getPieceSide() != pieceSide ) {
                        if ( thePiece.getPieceType() == PieceType.PAWN ) {
                            resultScore += 200;
                        } else if ( thePiece.getPieceType() == PieceType.KNIGHT ) {
                            resultScore += 600;
                        } else if ( thePiece.getPieceType() == PieceType.QUEEN ) {
                            resultScore += 1400;
                        } else if ( thePiece.getPieceType() == PieceType.KING ) {
                            resultScore += 10000;
                        }
                        break;
                    }else if ( thePiece.getPieceSide() == pieceSide  ){
                        break;
                    }
                    idx++;
                }idx=1;
                while ( IJtoIndex(row, col-idx)!= -1) {
                    Piece thePiece = board.getPiece(Square.squareAt(IJtoIndex(row, col-idx)));
                    if ( thePiece.getPieceSide() != pieceSide ) {
                        if ( thePiece.getPieceType() == PieceType.PAWN ) {
                            resultScore += 200;
                        } else if ( thePiece.getPieceType() == PieceType.KNIGHT ) {
                            resultScore += 600;
                        } else if ( thePiece.getPieceType() == PieceType.QUEEN ) {
                            resultScore += 1400;
                        } else if ( thePiece.getPieceType() == PieceType.KING ) {
                            resultScore += 10000;
                        }
                        break;
                    }else if ( thePiece.getPieceSide() == pieceSide  ){
                        break;
                    }
                    idx++;
                }idx=1;
                while ( IJtoIndex(row, col+idx)!= -1) {
                    Piece thePiece = board.getPiece(Square.squareAt(IJtoIndex(row, col+idx)));
                    if ( thePiece.getPieceSide() != pieceSide ) {
                        if ( thePiece.getPieceType() == PieceType.PAWN ) {
                            resultScore += 200;
                        } else if ( thePiece.getPieceType() == PieceType.KNIGHT ) {
                            resultScore += 600;
                        } else if ( thePiece.getPieceType() == PieceType.QUEEN ) {
                            resultScore += 1400;
                        } else if ( thePiece.getPieceType() == PieceType.KING ) {
                            resultScore += 10000;
                        }
                        break;
                    }else if ( thePiece.getPieceSide() == pieceSide  ){
                        break;
                    }
                    idx++;
                }idx=1;
                while ( IJtoIndex(row-idx, col-idx)!= -1) {
                    Piece thePiece = board.getPiece(Square.squareAt(IJtoIndex(row-idx, col-idx)));
                    if ( thePiece.getPieceSide() != pieceSide ) {
                        if ( thePiece.getPieceType() == PieceType.PAWN ) {
                            resultScore += 200;
                        } else if ( thePiece.getPieceType() == PieceType.KNIGHT ) {
                            resultScore += 600;
                        } else if ( thePiece.getPieceType() == PieceType.QUEEN ) {
                            resultScore += 1400;
                        } else if ( thePiece.getPieceType() == PieceType.KING ) {
                            resultScore += 10000;
                        }
                        break;
                    }else if ( thePiece.getPieceSide() == pieceSide  ){
                        break;
                    }
                    idx++;
                }idx=1;
                while ( IJtoIndex(row+idx, col-idx)!= -1) {
                    Piece thePiece = board.getPiece(Square.squareAt(IJtoIndex(row+idx, col-idx)));
                    if ( thePiece.getPieceSide() != pieceSide ) {
                        if ( thePiece.getPieceType() == PieceType.PAWN ) {
                            resultScore += 200;
                        } else if ( thePiece.getPieceType() == PieceType.KNIGHT ) {
                            resultScore += 600;
                        } else if ( thePiece.getPieceType() == PieceType.QUEEN ) {
                            resultScore += 1400;
                        } else if ( thePiece.getPieceType() == PieceType.KING ) {
                            resultScore += 10000;
                        }
                        break;
                    }else if ( thePiece.getPieceSide() == pieceSide  ){
                        break;
                    }
                    idx++;
                }idx=1;
                while ( IJtoIndex(row+idx, col+idx)!= -1) {
                    Piece thePiece = board.getPiece(Square.squareAt(IJtoIndex(row+idx, col+idx)));
                    if ( thePiece.getPieceSide() != pieceSide ) {
                        if ( thePiece.getPieceType() == PieceType.PAWN ) {
                            resultScore += 200;
                        } else if ( thePiece.getPieceType() == PieceType.KNIGHT ) {
                            resultScore += 600;
                        } else if ( thePiece.getPieceType() == PieceType.QUEEN ) {
                            resultScore += 1400;
                        } else if ( thePiece.getPieceType() == PieceType.KING ) {
                            resultScore += 10000;
                        }
                        break;
                    }else if ( thePiece.getPieceSide() == pieceSide  ){
                        break;
                    }
                    idx++;
                }idx=1;
                while ( IJtoIndex(row-idx, col+idx)!= -1) {
                    Piece thePiece = board.getPiece(Square.squareAt(IJtoIndex(row-idx, col+idx)));
                    if ( thePiece.getPieceSide() != pieceSide ) {
                        if ( thePiece.getPieceType() == PieceType.PAWN ) {
                            resultScore += 200;
                        } else if ( thePiece.getPieceType() == PieceType.KNIGHT ) {
                            resultScore += 600;
                        } else if ( thePiece.getPieceType() == PieceType.QUEEN ) {
                            resultScore += 1400;
                        } else if ( thePiece.getPieceType() == PieceType.KING ) {
                            resultScore += 10000;
                        }
                        break;
                    }else if ( thePiece.getPieceSide() == pieceSide  ){
                        break;
                    }
                    idx++;
                }idx=1;
                break;
            case KING:
                int[] possibleIndex1 =new int[8];
                possibleIndex1[0]=IJtoIndex(row-1, col-1);
                possibleIndex1[1]=IJtoIndex(row-1, col+1);
                possibleIndex1[2]=IJtoIndex(row+1, col-1);
                possibleIndex1[3]=IJtoIndex(row+1, col+1);
                possibleIndex1[4]=IJtoIndex(row, col+1);
                possibleIndex1[5]=IJtoIndex(row, col-1);
                possibleIndex1[6]=IJtoIndex(row+1, col);
                possibleIndex1[7]=IJtoIndex(row-1, col);
                for ( int i = 0 ; i < possibleIndex1.length ; i++ ) {
                    if ( possibleIndex1[i] != -1 ){
                        Piece thePiece = board.getPiece(Square.squareAt(possibleIndex1[i]));
                        if ( thePiece.getPieceSide() != pieceSide ) {
                            if ( thePiece.getPieceType() == PieceType.PAWN ) {
                                resultScore += 200;
                            } else if ( thePiece.getPieceType() == PieceType.KNIGHT ) {
                                resultScore += 600;
                            } else if ( thePiece.getPieceType() == PieceType.QUEEN ) {
                                resultScore += 1400;
                            } else if ( thePiece.getPieceType() == PieceType.KING ) {
                                resultScore += 10000;
                            }
                        }
                    }
                }
                break;
        }
        return resultScore;
    }
    
    public List<Move> sortMoves(Board board,List<Move> moves){//for node ordering
        int scores[]=new int[moves.size()];
        int scoresCopy[]=new int[moves.size()];
        List<Move> sortedMoves=new LinkedList<>();
        int minScore=0,maxScore=0;
        for (int i = 0; i < moves.size(); i++) {
            scoresCopy[i]=MoveEval(board.clone(), moves.get(i));
        }
        if(turn==1){//max turn
            int sortedMovesIndex=0;
            for (int k = 0; k < moves.size(); k++) {
                int bestScore=scoresCopy[0];
                int index=0;
                for (int i = 1; i < moves.size(); i++) {      
                    if(scoresCopy[i]>bestScore){
                        bestScore=scoresCopy[i];
                        index=i;
                    }
                }
                if(k==0){
                    maxScore=scoresCopy[k];
                }else if(k==moves.size()-1){
                    minScore=scoresCopy[k];
                }
                scores[sortedMovesIndex]=scoresCopy[index];
                scoresCopy[index]=-1000000;
                sortedMoves.add(moves.get(index));
                sortedMovesIndex++;
            }

            cutoff=(maxScore+minScore)/2;
            for (int i = sortedMoves.size()-1; i>=0; i--) {
                if(scores[i]<cutoff){
                    sortedMoves.remove(i);
                }else{
                    break;
                }
            }
        }else{//min turn
            int sortedMovesIndex=0;
            for (int k = 0; k < moves.size(); k++) {
                int bestScore=scoresCopy[0];
                int index=0;
                for (int i = 1; i < moves.size(); i++) {      
                    if(scoresCopy[i]<bestScore){
                        bestScore=scoresCopy[i];
                        index=i;
                    }
                }
                if(k==moves.size()-1){
                    maxScore=scoresCopy[k];
                }else if(k==0){
                    minScore=scoresCopy[k];
                }
                scores[sortedMovesIndex]=scoresCopy[index];
                scoresCopy[index]=1000000;
                sortedMoves.add(moves.get(index));
                sortedMovesIndex++;
            }
            cutoff=(maxScore+minScore)/2;
            for (int i = sortedMoves.size()-1; i>=0; i--) {
                if(scores[i]>cutoff){
                    sortedMoves.remove(i);
                }else{
                    break;
                }
            }
        }
        
        return sortedMoves;
    }
    
    public List<Move> sortMovesByStateEval(Board board,List<Move> moves){//for node ordering
        int stateScore[]=new int[moves.size()];
        List<Move> sortedMoves=new LinkedList<>();
        int minScore=0,maxScore=0;
        for (int i = 0; i < moves.size(); i++) {
            if (board.doMove(moves.get(i))) {
                stateScore[i]=stateEval(board.clone());
                board.undoMove();
            }   
        }
            int sortedMovesIndex=0;
            for (int k = 0; k < moves.size(); k++) {
                int bestScore=stateScore[0];
                int index=0;
                for (int i = 1; i < moves.size(); i++) {      
                    if(stateScore[i]>bestScore){
                        bestScore=stateScore[i];
                        index=i;
                    }
                }
                if(k==0){
                    maxScore=stateScore[k];
                }else if(k==moves.size()-1){
                    minScore=stateScore[k];
                }
                stateScore[index]=-1000000;
                sortedMoves.add(moves.get(index));
                sortedMovesIndex++;
            }
       
        return sortedMoves;
    }
    
    public int isQueenThreated(Board board){
        List<Square> queenSquares=board.getPieceLocation(Piece.WHITE_QUEEN);

        if ( queenSquares.size()>0 ){
            for ( int i = 0 ; i < queenSquares.size() ; i++ ) {
                int indexOfSquare=0;
                for ( int j = 0 ; j < 63 ; j++ ) {
                    if(Square.squareAt(j) == queenSquares.get(i)){
                        indexOfSquare=j;
                    }
                }
                int[] iANDj= IndexToIJ(indexOfSquare);
                int row=iANDj[0];
                int col=iANDj[1];
                //KNIGHT
                int[] possibleIndex =new int[8];
                possibleIndex[0]=IJtoIndex(row-2, col-1);
                possibleIndex[1]=IJtoIndex(row-2, col+1);
                possibleIndex[2]=IJtoIndex(row-1, col+2);
                possibleIndex[3]=IJtoIndex(row+1, col+2);
                possibleIndex[4]=IJtoIndex(row+2, col+1);
                possibleIndex[5]=IJtoIndex(row+2, col-1);
                possibleIndex[6]=IJtoIndex(row+1, col-2);
                possibleIndex[7]=IJtoIndex(row-1, col-2);
                for ( int j = 0 ; j < possibleIndex.length ; j++ ) {
                    if ( possibleIndex[j] != -1 ) {
                        Piece thePiece = board.getPiece(Square.squareAt(possibleIndex[j]));
                        if ( thePiece.getPieceType() == PieceType.KNIGHT ) {
                            return indexOfSquare;
                        }
                    }
                }
                //PAWN
                //first possible capture
                int index = IJtoIndex(row+1, col-1);
                if ( index!= -1 ) {
                    Piece thePiece = board.getPiece(Square.squareAt(index));
                    if ( thePiece.getPieceSide() == Side.BLACK ) {
                        if ( thePiece.getPieceType() == PieceType.PAWN ) {
                            return indexOfSquare;
                        }
                    }
                }
                //second possible capture
                index = IJtoIndex(row+1, col+1);
                if ( index!= -1 ) {
                    Piece thePiece = board.getPiece(Square.squareAt(index));
                    if ( thePiece.getPieceSide() == Side.BLACK ) {
                        if ( thePiece.getPieceType() == PieceType.PAWN ) {
                            return indexOfSquare;
                        }
                    }
                }
                //King
                int[] possibleIndex1 =new int[8];
                possibleIndex1[0]=IJtoIndex(row-1, col-1);
                possibleIndex1[1]=IJtoIndex(row-1, col+1);
                possibleIndex1[2]=IJtoIndex(row+1, col-1);
                possibleIndex1[3]=IJtoIndex(row+1, col+1);
                possibleIndex1[4]=IJtoIndex(row, col+1);
                possibleIndex1[5]=IJtoIndex(row, col-1);
                possibleIndex1[6]=IJtoIndex(row+1, col);
                possibleIndex1[7]=IJtoIndex(row-1, col);
                for ( int j = 0 ; j < possibleIndex1.length ; j++ ) {
                    if ( possibleIndex1[j] != -1 ){
                        Piece thePiece = board.getPiece(Square.squareAt(possibleIndex1[j]));
                        if ( thePiece.getPieceSide() == Side.BLACK ) {
                            if ( thePiece.getPieceType() == PieceType.KING ) {
                                return indexOfSquare;
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }
    
    public void unsafeSquares(Board board){
        unsafePoints=new int[8][8];
        for ( int i = 0 ; i < unsafePoints.length ; i++ ) {
            for ( int j = 0 ; j < unsafePoints.length ; j++ ) {
                unsafePoints[i][j]=0;
            }
        }
        List<Square> pawns=board.getPieceLocation(Piece.BLACK_PAWN);
        for ( int i = 0 ; i < pawns.size() ; i++ ) {
            Piece pawnPiece=board.getPiece(pawns.get(i));
            PieceType pieceType = pawnPiece.getPieceType();
            Side pieceSide=pawnPiece.getPieceSide();
            int indexOfSquare=0;
            for ( int j = 0 ; j < 63 ; j++ ) {
                if(Square.squareAt(j) == pawns.get(i)){
                    indexOfSquare=j;
                }
            }
            int[] iANDj= IndexToIJ(indexOfSquare);
            int row=iANDj[0];
            int col=iANDj[1];

            //first possible capture
            int index = IJtoIndex(row-1, col-1);
            if ( index!= -1 ) {
                unsafePoints[7-(row-1)][col-1]=1;
            }
            //second possible capture
            index = IJtoIndex(row-1, col+1);
            if ( index!= -1 ) {
                unsafePoints[7-(row-1)][col+1]=1;

            }
        }
        List<Square> knights=board.getPieceLocation(Piece.BLACK_KNIGHT);
        for ( int i = 0 ; i < knights.size(); i++ ) {
            Piece knightPiece=board.getPiece(knights.get(i));
            PieceType pieceType = knightPiece.getPieceType();
            Side pieceSide=knightPiece.getPieceSide();
            int indexOfSquare=0;
            for ( int j = 0 ; j < 63 ; j++ ) {
                if(Square.squareAt(j) == knights.get(i)){
                    indexOfSquare=j;
                }
            }
            int[] iANDj= IndexToIJ(indexOfSquare);
            int row=iANDj[0];
            int col=iANDj[1];
            if ( IJtoIndex(row-2, col-1) != -1 ){
                unsafePoints[7-(row-2)][col-1]=1;
            }
            if ( IJtoIndex(row-2, col+1) != -1 ){
                unsafePoints[7-(row-2)][col+1]=1;
            }
            if ( IJtoIndex(row-1, col+2) != -1 ){
                unsafePoints[7-(row-1)][col+2]=1;
            }
            if ( IJtoIndex(row+1, col+2) != -1 ){
                unsafePoints[7-(row+1)][col+2]=1;
            }
            if ( IJtoIndex(row+2, col+1) != -1 ){
                unsafePoints[7-(row+2)][col+1]=1;
            }
            if ( IJtoIndex(row+2, col-1) != -1 ){
                unsafePoints[7-(row+2)][col-1]=1;
            }
            if ( IJtoIndex(row+1, col-2) != -1 ){
                unsafePoints[7-(row+1)][col-2]=1;
            }
            if ( IJtoIndex(row-1, col-2) != -1 ){
                unsafePoints[7-(row-1)][col-2]=1;
            }
        }
        List<Square> king=board.getPieceLocation(Piece.BLACK_KING);
        Piece kingPiece=board.getPiece(king.get(0));
        PieceType pieceType = kingPiece.getPieceType();
        Side pieceSide=kingPiece.getPieceSide();
        int indexOfSquare=0;
        for ( int j = 0 ; j < 63 ; j++ ) {
            if(Square.squareAt(j) == king.get(0)){
                indexOfSquare=j;
            }
        }
        int[] iANDj= IndexToIJ(indexOfSquare);
        int row=iANDj[0];
        int col=iANDj[1];
        if ( IJtoIndex(row-1, col-1) != -1 ){
            unsafePoints[7-(row-1)][col-1]=1;
        }
        if ( IJtoIndex(row-1, col+1) != -1 ){
            unsafePoints[7-(row-1)][col+1]=1;
        }
        if ( IJtoIndex(row+1, col-1) != -1 ){
            unsafePoints[7-(row+1)][col-1]=1;
        }
        if ( IJtoIndex(row+1, col+1) != -1 ){
            unsafePoints[7-(row+1)][col+1]=1;
        }
        if ( IJtoIndex(row, col+1) != -1 ){
            unsafePoints[7-(row)][col+1]=1;
        }
        if ( IJtoIndex(row, col-1) != -1 ){
            unsafePoints[7-(row)][col-1]=1;
        }
        if ( IJtoIndex(row+1, col) != -1 ){
            unsafePoints[7-(row+1)][col]=1;
        }
        if ( IJtoIndex(row-1, col) != -1 ){
            unsafePoints[7-(row-1)][col]=1;
        }
    }
    
    public Move makeMove(Board board,List<Move> moves,int indexFrom){
        unsafeSquares(board.clone());
        List<Move> bestMoves=new LinkedList<>();
        for (int i = 0; i < moves.size(); i++) {
            Square moveSquareFrom=moves.get(i).getFrom();
            int MoveIndexFrom=0;
            for ( int j = 0 ; j < 63 ; j++ ) {
                    if(Square.squareAt(j) == moveSquareFrom){
                        MoveIndexFrom=j;
                    }
                }
            if(MoveIndexFrom==indexFrom){
                Square moveSqureTo=moves.get(i).getTo();
                int moveIndexTo=0;
                for ( int j = 0 ; j < 63 ; j++ ) {
                    if(Square.squareAt(j) == moveSqureTo){
                        moveIndexTo=j;
                    }
                }
                int[] iANDj= IndexToIJ(moveIndexTo);
                int row=iANDj[0];
                int col=iANDj[1];
                if(unsafePoints[7-row][col] == 0 ){
                    bestMoves.add(moves.get(i));
                }
                
            }
        }
        if(bestMoves.size()==0){
            return null;
        }
        bestMoves=sortMovesByStateEval(board.clone(), bestMoves);
        return bestMoves.get(0);
        
    }
    
    @Override
    public Move play(Board board) {

        turn=1;
        List<Move> moves = board.legalMoves();
        int queenIndex=isQueenThreated(board.clone());
        moveCounter++;
        if(queenIndex!=-1){
            Move semiTableMove=makeMove(board.clone(), moves, queenIndex);
            if(semiTableMove != null){
                System.out.println(".................................................................................................... semi table");
                return semiTableMove;
                
            }
        }
        moves=sortMoves(board,moves);
        Move bestMove = null;
        int bestMoveValue = Integer.MIN_VALUE;
        int alfa=Integer.MIN_VALUE;
        int beta=Integer.MAX_VALUE;
        for (Move move : moves) {
            if (board.doMove(move)) {
                int temp = min_alfa_beta(board.clone(), 1,alfa,beta);
                if (temp > bestMoveValue) {
                    bestMove = move;
                    bestMoveValue = temp;
                }
                board.undoMove();
            }
        }
        if (bestMove == null) {
            Random random = new Random();
            return moves.get(random.nextInt(moves.size()));
        }
        
        return bestMove;
    }

    public int max(Board board, int depth) {
        if (depth == maxDepth) {
            return stateEval(board);
        }
        List<Move> moves = board.legalMoves();
        int bestValue = Integer.MIN_VALUE;
        for (Move move : moves) {
            if (board.doMove(move)) {
                if (board.isMated()) {
                    return Integer.MAX_VALUE;
                }
                bestValue = Integer.max(min(board.clone(), depth + 1), bestValue);
                board.undoMove();
            }
        }
        return bestValue;
    }
    
    public int max_alfa_beta(Board board, int depth,int alfa,int beta) {
        turn=1;
        if (depth == maxDepth) {
            return stateEval(board);
        }
        List<Move> moves = board.legalMoves();
        moves=sortMoves(board,moves);
        int bestValue = Integer.MIN_VALUE;
        for (Move move : moves) {
            if (board.doMove(move)) {
                if (board.isMated()) {
                    return Integer.MAX_VALUE;
                }
                bestValue = Integer.max(min_alfa_beta(board.clone(), depth + 1,alfa,beta), bestValue);
                if(bestValue>=beta){
                    return bestValue;
                }
                alfa=Integer.max(alfa,bestValue);
                board.undoMove();
            }
        }
        return bestValue;
    }
    
    public int min_alfa_beta(Board board, int depth,int alfa,int beta) {
            turn=0;
        if (depth == maxDepth) {
            return stateEval(board);
        }
        List<Move> moves = board.legalMoves();
        moves=sortMoves(board,moves);
        int bestValue = Integer.MAX_VALUE;
        for (Move move : moves) {
            if (board.doMove(move)) {
                if (board.isMated()) {
                    return Integer.MIN_VALUE;
                }
                bestValue = Integer.min(max_alfa_beta(board.clone(), depth + 1,alfa,beta), bestValue);
                if(bestValue<=alfa){
                    return bestValue;
                }
                beta=Integer.min(beta,bestValue);
                board.undoMove();
            }
        }
        return bestValue;
    }

    public int min(Board board, int depth) {
        if (depth == maxDepth) {
            return stateEval(board);
        }
        List<Move> moves = board.legalMoves();
        int bestValue = Integer.MAX_VALUE;
        for (Move move : moves) {
            if (board.doMove(move)) {
                if (board.isMated()) {
                    return Integer.MIN_VALUE;
                }
                bestValue = Integer.min(max(board.clone(), depth + 1), bestValue);
                board.undoMove();
            }
        }
        return bestValue;
    }
    
 

    public int[] IndexToIJ(int index){
        int[] IJ=new int[2];
        IJ[0]= index/8;//row
        IJ[1]= index%8;//column
        return IJ;
    }
    
    public int IJtoIndex(int row, int col){
        if ( row>=0 && row<=7 && col>=0 && col<=7 ){
            return ((row*8)+col);
        }
        return -1;//if its out of bound
    }

}
