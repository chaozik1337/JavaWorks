import java.util.Random;

class DeckPile extends CardPile {

    private DiscardPile discardPile;                        // Having a link allows us to swap decks if DeckPile will become empty
    private final int DECK_SIZE = 52;


	DeckPile(final int x, final int y) {
		// first initialize parent
		super(x, y);

        Card toShuffle[] = new Card[DECK_SIZE];

        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j <= 12; ++j){
                toShuffle[i*13 + j] = new Card(i,j);                                    // fill the array with sorted cards
            }
        }

        for(int i = DECK_SIZE-1; i != 0; --i){                                 // takes O(n) instead of prev shuffle algorithm
                                                                               // KANSAS CITY SHUFFLE
            int randomNum = randInt(1, DECK_SIZE-1);
            System.out.println(randomNum);

            Card tmp = toShuffle[randomNum];
            toShuffle[randomNum] = toShuffle[i];
            toShuffle[i] = tmp;
        }

        // Shuffled array goes to deck now

        for(int i = 0; i < DECK_SIZE; ++i){
            System.out.println(toShuffle[i].getRank());
            addCard(toShuffle[i]);
        }
	}

    public void setDiscardPile(DiscardPile _discardPile){
        discardPile = _discardPile;
    }

	public PlayerHand select(final int tx, final int ty) {
		if (empty()) {
			switchDecks(discardPile);
            return null;
		}
		Solitaire.discardPile.addCard(pop());
        return null;
	}

    private void switchDecks(DiscardPile discardPile){
        Card cur = discardPile.top();

        while(cur != null){
            Card tmp = discardPile.pop();
            tmp.flip();

            cur = tmp.link;

            this.addCard(tmp);
        }
    }

    private int randInt(int min, int max){
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}