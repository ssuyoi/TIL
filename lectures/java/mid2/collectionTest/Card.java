package mid2.collectionTest;


public class Card implements Comparable<Card>{

    private final int rank;
    private final Suit suit;

    public Card(int rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }


    @Override
    public int compareTo(Card anotherCard) {
        if(this.rank != anotherCard.rank) {
            return Integer.compare(this.rank, anotherCard.rank);
        } else {
            // enum은 compareTo를 가지고 있는데 enum에 적은 순서대로 비교한다
            return this.suit.compareTo(anotherCard.suit);
        }
    }

    @Override
    public String toString() {
        return rank + "(" + suit.getIcon() + ")";
    }
}
