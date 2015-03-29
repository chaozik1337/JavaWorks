/**
 * Created by cha0ztw on 29.03.2015.
 */

import java.awt.*;
import java.util.Vector;

// Something more than vector that we might need

public class PlayerHand{

    public Vector<Card> playerHand;
    public int fromIndex;                                                                    // from which deck cards were taken

    private int x;
    private int y;

    PlayerHand(){
        playerHand = new Vector<Card>();
    }

    public void setX(int _x) { x = _x; }
    public void setY(int _y) { y = _y; }

    public int getX() { return x; }
    public int getY() { return y; }

    public void replaceHand(CardPile from, CardPile to){                                // used in non Drag-n-Drop version
        popFromDeck(from);
        pushToDeck(to);
    }

    public void pushToDeck(CardPile cardPile){
        for (int i = playerHand.size() - 1; i >= 0; --i){
            cardPile.addCard(playerHand.elementAt(i));                                  // push to the new deck
        }
    }

    public void popFromDeck(CardPile cardPile) {
        for(int i = 0; i < playerHand.size(); ++i) {
            cardPile.pop();                                                             // clear all the elements from prev deck cuz player did correct move
        }
    }

    public void display(Graphics g){
        for(int i = playerHand.size() - 1; i >= 0; --i){
            playerHand.elementAt(i).draw(g, x, y);
            y += 35;
        }
    }

    public int autoSolve(){

        Card topCard = playerHand.lastElement();

        for(int i = 2 ; i <= 12; ++i ){
            if(Solitaire.allPiles[i].canTake(topCard))
                return i;
        }

        return -1;                                                            // no such corresponding deck found
    }

}
