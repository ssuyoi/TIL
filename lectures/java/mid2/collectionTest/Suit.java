package mid2.collectionTest;

public enum Suit {
    SPADES("♠︎"),
    HEARTS("♥︎"),
    DIAMONDS("♦︎"),
    CLUB("♣︎");

    private String icon;

    Suit(String suit) {
        this.icon = suit;
    }

    public String getIcon() {
        return icon;
    }
}
