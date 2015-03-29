import java.awt.Graphics;
import java.util.Vector;

class TablePile extends CardPile {

    private int deckDepth;                              // allows us to use some functions in O(1)

	TablePile(final int x, final int y, final int _deckDepth) {
		// initialize the parent class
		super(x, y);
		// then initialize our pile of cards

		for (int i = 0; i < _deckDepth; i++) {
			addCard(Solitaire.deckPile.pop());
		}

		// flip topmost card face up
		top().flip();
	}

    public int getDeckDepth() { return deckDepth; }

	public boolean canTake(final Card aCard) {
		if (empty()) {
			return aCard.isKing();
		}
		Card topCard = top();
		return (aCard.color() != topCard.color())
				&& (aCard.getRank() == topCard.getRank() - 1);
	}

	public void display(final Graphics g) {
		stackDisplay(g, top());
	}

	public boolean includes(final int tx, final int ty) {
		// don't test bottom of card
		//return x <= tx && tx <= x + Card.width && y <= ty;
        int openCardsCount = getOpenCardsCount();

        int lastCardY = getDepthY();
        int yShift;

        if(openCardsCount == 0)
            yShift = 0;
        else
            yShift = (openCardsCount - 1) * (Card.height / 2);              // How many cards are face up (except first) * Card.height/2


        return tx >= x && tx <= x + Card.width && ty >= (lastCardY-yShift) && ty <= lastCardY + Card.height;

	}

	public PlayerHand select(final int tx, final int ty) {
		if (empty()) {
			return null;                                    // nothing to do with empty deck
		}

        Card topCard = top();
        if (!topCard.isFaceUp()) {
            topCard.flip();
            return null;                                    // just flip card and leave
        }

        PlayerHand tmp = new PlayerHand();                  // hand to return

        int cardIndex = getCardIndex(tx, ty);

        Card cur = topCard;

        for(int i = 0; i <= cardIndex; ++i){
            tmp.playerHand.add(pop());
        }

        return tmp;
	}

    public void addCard(final Card aCard) {
        super.addCard(aCard);
        deckDepth++;
        System.out.println(this.getDeckDepth());
    }

    public Card pop(){
        deckDepth--;
        return super.pop();
    }

	private int stackDisplay(final Graphics g, final Card aCard) {
		int localy;
		if (aCard == null) {
			return y;
		}
		localy = stackDisplay(g, aCard.link);
		aCard.draw(g, x, localy);
		return localy + 35;
	}

    private int getDepthY() {
        // used to find depth Y for include() func
        // we don't care for x cuz TablePile grows down

        if(deckDepth == 1)
        {
            // if there's only one card in a deck remaining then return deck's Y coordinate
            return y;
        }

        int lastCardY = y + ((deckDepth - 1) * (Card.height / 2));
        return lastCardY;
    }

    private int getOpenCardsCount(){
        Card cur = top();
        int count = 0;

        while(cur != null){
            if(!cur.isFaceUp()){
                break;
            }

            ++count;
            cur = cur.link;
        }

        return count;
    }

    private int getCardIndex(int tx, int ty){                         // returns index of card with (@tx,@ty) coords
        for(int i = 0; i < getDeckDepth(); ++i){
            if(isHere(tx, ty, i)){
                return i;
            }
        }
        return -1;                                                   // will never happen
    }

    private boolean isHere(int tx, int ty, int cardsNum){                   // current (@tx,@ty) is in first @cardsNum cards
        return tx >= x && tx <= x + Card.width && ty >= getDepthY() - (Card.height/2 * cardsNum)  && ty <= getDepthY() + Card.height;
    }

}