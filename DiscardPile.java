class DiscardPile extends CardPile {

	DiscardPile(final int x, final int y) {
		super(x, y);
	}

	public void addCard(final Card aCard) {
		if (!aCard.isFaceUp()) {
			aCard.flip();
		}
		super.addCard(aCard);
	}

	public PlayerHand select(final int tx, final int ty) {
		if (empty()) {
			return null;
		}

        PlayerHand tmp = new PlayerHand();

        tmp.playerHand.add(pop());

        return tmp;
	}
}