/*
 Simple Solitaire Card Game in Java
 Written by Tim Budd, Oregon State University, 1996
 */

import java.awt.*;
import java.applet.*;

public class Solitaire extends Applet {
	static CardPile allPiles[];
	static DeckPile deckPile;
	static DiscardPile discardPile;
	static SuitPile suitPile[];
	static TablePile tableau[];
    static PlayerHand userChoice;                                                                                       // Vector that stores all the info about current user's card choise


    private boolean isMouseDown;

    @Deprecated
    public boolean mouseDrag(Event evt, int x, int y) {
        if (isMouseDown) {
            if(userChoice != null && !userChoice.playerHand.isEmpty() && userChoice.fromIndex != 0){
                userChoice.setX(evt.x);
                userChoice.setY(evt.y);

                repaint();
            }
        }
        return true;
    }

	public void init(){
		// first allocate the arrays

		allPiles = new CardPile[13];
		suitPile = new SuitPile[4];
		tableau = new TablePile[7];

		// then fill them in

        allPiles[0] = deckPile = new DeckPile(335, 5);
        allPiles[1] = discardPile = new DiscardPile(268, 5);

        deckPile.setDiscardPile(discardPile);                                                                           // used for refreshing DeckPile

		for (int i = 0; i < 4; i++) {
			allPiles[2 + i] = suitPile[i] = new SuitPile(15 + 60 * i, 5);
		}
		for (int i = 0; i < 7; i++) {
			allPiles[6 + i] = tableau[i] = new TablePile(5 + 55 * i, 80, i + 1);
		}
	}

	public boolean mouseDown(final Event evt, final int x, final int y) {
        isMouseDown = true;

        if(evt.clickCount == 2 && (userChoice == null || userChoice.playerHand.isEmpty())){
            for(int i = 0; i < 13; ++i){
                if(allPiles[i].includes(x,y)){

                    if(i == 0)                                                          // deckPile does not run autoSolve()
                    {
                        return true;
                    }

                    userChoice = allPiles[i].select(x,y);
                    int index = userChoice.autoSolve();

                    if(index == -1) {                                                   // no such deck was found => clear tmp and leave
                        userChoice.pushToDeck(allPiles[i]);
                        userChoice.playerHand.clear();
                        return true;
                    }

                    userChoice.pushToDeck(allPiles[index]);
                    userChoice.playerHand.clear();
                    repaint();
                    return true;
                }
            }


            return true;
        }

        for (int i = 0; i < 13; i++) {                                                                                   // do it for every deck
            if (allPiles[i].includes(x, y)) {                                                                            // if user did hit some open card
                if (allPiles[i] instanceof DeckPile || (!allPiles[i].empty() && !allPiles[i].top().isFaceUp())) {                                  // DeckPile doesn't require any Buffer then just make simple action and repaint();
                    allPiles[i].select(x, y);
                    repaint();
                    return true;
                }
                userChoice = allPiles[i].select(x, y);                                                                   // add to Buffer cards that user chose
                userChoice.fromIndex = i;
            }
        }

        return true;
    }

    @Deprecated
    public boolean mouseUp(Event evt, int x, int y) {
        isMouseDown = false;

        if(userChoice != null && !userChoice.playerHand.isEmpty()) {
            for (int i = 0; i < 13; i++) {                                                                                   // do it for every deck
                if (allPiles[i].includes(x, y)) {                                                                            // if user did hit some open card
                    if (allPiles[i].canTake(userChoice.playerHand.lastElement())) {                                           // try to place it where user wants to
                        userChoice.pushToDeck(allPiles[i]);
                    } else {                                                                // clear buffer
                        userChoice.pushToDeck(allPiles[userChoice.fromIndex]);
                    }
                    userChoice.playerHand.clear();
                    repaint();
                    return true;
                }
                }
            }
        if(userChoice != null) {
            userChoice.pushToDeck(allPiles[userChoice.fromIndex]);
            userChoice.playerHand.clear();
        }
        repaint();
        return  true;
    }

	public void paint(final Graphics g) {
        if(userChoice != null) {
            userChoice.display(g);
        }
		for (int i = 0; i < 13; i++) {
			allPiles[i].display(g);
		}
	}

}