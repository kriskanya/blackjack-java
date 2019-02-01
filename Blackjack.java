import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

enum Card {
  ACE_OF_SPADES(1, "A\u2660"), ACE_OF_DIAMONDS(1, "A\u2666"), ACE_OF_HEARTS(1, "A\u2764"), ACE_OF_CLUBS(1, "A\u2663"),
  TWO_OF_SPADES(2, "2\u2660"), TWO_OF_DIAMONDS(2, "2\u2666"), TWO_OF_HEARTS(2, "2\u2764"), TWO_OF_CLUBS(2, "2\u2663"),
  THREE_OF_SPADES(3, "3\u2660"), THREE_OF_DIAMONDS(3, "3\u2666"), THREE_OF_HEARTS(3, "3\u2764"), THREE_OF_CLUBS(3, "3\u2663"),
  FOUR_OF_SPADES(4, "4\u2660"), FOUR_OF_DIAMONDS(4, "4\u2666"), FOUR_OF_HEARTS(4, "4\u2764"), FOUR_OF_CLUBS(4, "4\u2663"),
  FIVE_OF_SPADES(5, "5\u2660"), FIVE_OF_DIAMONDS(5, "5\u2666"), FIVE_OF_HEARTS(5, "5\u2764"), FIVE_OF_CLUBS(5, "5\u2663"),
  SIX_OF_SPADES(6, "6\u2660"), SIX_OF_DIAMONDS(6, "6\u2666"), SIX_OF_HEARTS(6, "6\u2764"), SIX_OF_CLUBS(6, "6\u2663"),
  SEVEN_OF_SPADES(7, "7\u2660"), SEVEN_OF_DIAMONDS(7, "7\u2666"), SEVEN_OF_HEARTS(7, "7\u2764"), SEVEN_OF_CLUBS(7, "7\u2663"),
  EIGHT_OF_SPADES(8, "8\u2660"), EIGHT_OF_DIAMONDS(8, "8\u2666"), EIGHT_OF_HEARTS(8, "8\u2764"), EIGHT_OF_CLUBS(8, "8\u2663"),
  NINE_OF_SPADES(9, "9\u2660"), NINE_OF_DIAMONDS(9, "9\u2666"), NINE_OF_HEARTS(9, "9\u2764"), NINE_OF_CLUBS(9, "9\u2663"),
  TEN_OF_SPADES(10, "10\u2660"), TEN_OF_DIAMONDS(10, "10\u2666"), TEN_OF_HEARTS(10, "10\u2764"), TEN_OF_CLUBS(10, "10\u2663"),
  JACK_OF_SPADES(10, "J\u2660"), JACK_OF_DIAMONDS(10, "J\u2666"), JACK_OF_HEARTS(10, "J\u2764"), JACK_OF_CLUBS(10, "J\u2663"),
  QUEEN_OF_SPADES(10, "Q\u2660"), QUEEN_OF_DIAMONDS(10, "Q\u2666"), QUEEN_OF_HEARTS(10, "Q\u2764"), QUEEN_OF_CLUBS(10, "Q\u2663"),
  KING_OF_SPADES(10, "K\u2660"), KING_OF_DIAMONDS(10, "K\u2666"), KING_OF_HEARTS(10, "K\u2764"), KING_OF_CLUBS(10, "K\u2663");

  private int number;
  private String symbol;

  Card(int number, String symbol) {
    this.number = number;
    this.symbol = symbol;
  }

  public int getValue() { 
    return this.number; 
  }

  public String getSymbol() {
    return this.symbol;
  }
}

public class Blackjack {
  public ArrayList<Card> playersCards;
  public ArrayList<Card> dealersCards;
  public ArrayList<Card> cardsPulled;
  public boolean isDealersTurn;
  public int playerTotal;
  public int dealerTotal;
  public boolean dealerHasAce;

  private GameState currentState;
  private GameState playGameState;
  private GameState checkIfGameOverGameState;
  private GameState loseGameState;
  private GameState tiedGameState;
  private GameState winGameState;

  public Blackjack() {
    this.playersCards = new ArrayList<Card>();
    this.dealersCards = new ArrayList<Card>();
    this.cardsPulled = new ArrayList<Card>();

    this.currentState = new StartState(this);
    this.playGameState = new PlayGameState(this);
    this.checkIfGameOverGameState = new CheckIfGameOverGameState(this);
    this.loseGameState = new LoseGameState(this);
    this.tiedGameState = new TiedGameState(this);
    this.winGameState = new WinGameState(this);
  }

  public static void main(String[] args) {
    System.out.println("Welcome to Blackjack!\n");
    for (String answer; ; ) {
      System.out.println("Shall we play a game (y or n)?");
      answer = new Scanner(System.in).nextLine();
      if (answer.equals("y")) {
        new Blackjack().play();
      } else {
        break;
      }
    }
    System.out.println("---game over---");
  }

  GameState getPlayGameState() {
    return this.playGameState;
  }

  GameState getCheckIfGameOverState() {
    return this.checkIfGameOverGameState;
  }

  GameState getTiedGameState() {
    return this.tiedGameState;
  }

  GameState getLoseGameState() {
    return this.loseGameState;
  }

  GameState getWinGameState() {
    return this.winGameState;
  }

  void play() {
    this.currentState.handle();
  }

  void setState(GameState state) {
    this.currentState = state;
  }

  public Card getRandomCard() {
    Card randomCard = getRandomCardHelper();

    // draw another card if a duplicate is pulled
    while (this.cardsPulled.contains(randomCard)) {
      randomCard = getRandomCardHelper();
    }
    this.cardsPulled.add(randomCard);
    return randomCard;
  }

  private Card getRandomCardHelper() {
    Random random = new Random();
    // returns a pseudorandom, uniformly distributed int value between 0 (inclusive) and the specified value (exclusive)
    int index = random.nextInt(Card.values().length);
    return Card.values()[index];
  }

  @Override
  public String toString() {
    String str = new String("\nDealer's cards:");
    for (Card c : (ArrayList<Card>)this.dealersCards) {
      str += " " + c.getSymbol().toString() + ";";
    }
    str += " (total: " + this.dealerTotal + ")\n";
    str += "Player's cards:";
    for (Card c : (ArrayList<Card>)this.playersCards) {
      str += " " + c.getSymbol().toString() + ";";
    }
    return str + " (total: " + this.playerTotal + ")\n";
  }
}

abstract class GameState {
  protected Blackjack gameContext = null;

  // all GameState subclasses must implement handle()
  public abstract void handle();
}

class StartState extends GameState {
  public StartState(Blackjack game) {
    this.gameContext = game;
  }

  public void handle() {
    this.gameContext.playersCards.add(this.gameContext.getRandomCard());
    this.gameContext.playersCards.add(this.gameContext.getRandomCard());
    this.gameContext.dealersCards.add(this.gameContext.getRandomCard());
    this.gameContext.dealersCards.add(this.gameContext.getRandomCard());
    this.gameContext.setState(this.gameContext.getPlayGameState());
    this.gameContext.play();
  }
}

class CheckIfGameOverGameState extends GameState {
  public CheckIfGameOverGameState(Blackjack game) {
    this.gameContext = game;
  }

  public void handle() {
    // check to see if cards add up to more than 21 for either player or dealer
    if (this.gameContext.playerTotal > 21) {
      this.gameContext.setState(this.gameContext.getLoseGameState());
      this.gameContext.play();
    } else if (this.gameContext.dealerTotal > 21) {
      this.gameContext.setState(this.gameContext.getWinGameState());
      this.gameContext.play();
      // if dealer has soft 17, hit again
    } else if (this.gameContext.isDealersTurn && this.gameContext.dealerTotal == 17 && this.gameContext.dealerHasAce) {
      this.gameContext.setState(this.gameContext.getPlayGameState());
      this.gameContext.play();
    } else if (this.gameContext.isDealersTurn && this.gameContext.dealerTotal >= 17) {
      if (this.gameContext.dealerTotal > this.gameContext.playerTotal) {
        this.gameContext.setState(this.gameContext.getLoseGameState());
        this.gameContext.play();
      } else if (this.gameContext.dealerTotal == this.gameContext.playerTotal) {
        this.gameContext.setState(this.gameContext.getTiedGameState());
        this.gameContext.play();
      } else {
        this.gameContext.setState(this.gameContext.getWinGameState());
        this.gameContext.play();
      }
    } else {
      this.gameContext.setState(this.gameContext.getPlayGameState());
      this.gameContext.play();
    }
  }
}

class PlayGameState extends GameState {
  public PlayGameState(Blackjack game) {
    this.gameContext = game;
  }

  public void handle() {
    String answer = "";

    getCardTotals();
    System.out.println(this.gameContext.toString());
    
    if (this.someoneHasBlackjack()) {
      System.out.println("A\u2660J\u2660 A\u2666J\u2666 BLACKJACK! A\u2764J\u2764 A\u2663J\u2663\n");
      return;
    }

    if (!this.gameContext.isDealersTurn) {
      System.out.println("Would you like to hit? (y or n)");
      answer = new Scanner(System.in).nextLine();
    }
    
    if (answer.equals("y")) {
      this.gameContext.playersCards.add(this.gameContext.getRandomCard());
      getCardTotals();
      this.gameContext.isDealersTurn = false;
      this.gameContext.setState(this.gameContext.getCheckIfGameOverState());
      this.gameContext.play();
    } else {
      // dealer hits until win or bust, after player is finished; if dealer has soft 17, hit again
      if (this.gameContext.dealerTotal < 17 || (this.gameContext.dealerTotal == 17 && this.gameContext.dealerHasAce)) {
        this.gameContext.dealersCards.add(this.gameContext.getRandomCard());
        getCardTotals();
      }
      this.gameContext.isDealersTurn = true;
      this.gameContext.setState(this.gameContext.getCheckIfGameOverState());
      this.gameContext.play();
    }
  }

  private void getCardTotals() {
    // clone the ArrayLists so we don't alter the actual order on sort
    ArrayList<Card> clonedDealersCards = new ArrayList<Card>(this.gameContext.dealersCards);
    ArrayList<Card> clonedPlayersCards = new ArrayList<Card>(this.gameContext.playersCards);

    // place the aces at the end of the ArrayList so we can check the sum of the preceding values
    Collections.sort(clonedDealersCards, Collections.reverseOrder());
    Collections.sort(clonedPlayersCards, Collections.reverseOrder());

    this.gameContext.playerTotal = 0;
    this.gameContext.dealerTotal = 0;

    for (Card c : clonedPlayersCards) {
      int cardValue = Card.valueOf(c.toString()).getValue();
      // if the cards' sum total is less than or equal to 10, count the Ace as 11
      if (cardValue == 1 && this.gameContext.playerTotal <= 10) {
        this.gameContext.playerTotal += 11;
      } else {
        this.gameContext.playerTotal += cardValue;
      }
    }
    
    for (Card c : clonedDealersCards) {
      int cardValue = Card.valueOf(c.toString()).getValue();
      // checks if dealer has Ace, for the purposes of soft 17
      this.gameContext.dealerHasAce = (this.gameContext.dealerHasAce || cardValue == 1);
      // if the cards' sum total is less than or equal to 10, count the Ace as 11
      if (cardValue == 1 && this.gameContext.dealerTotal <= 10) {
        this.gameContext.dealerTotal += 11;
      } else {
        this.gameContext.dealerTotal += cardValue;
      }
    }
  }

  private boolean someoneHasBlackjack() {
    Card firstDealerCard = (Card)this.gameContext.dealersCards.get(0);
    Card secondDealerCard = (Card)this.gameContext.dealersCards.get(1);
    Card firstPlayerCard = (Card)this.gameContext.playersCards.get(0);
    Card secondPlayerCard = (Card)this.gameContext.playersCards.get(1);

    if (this.hasBlackjack(firstDealerCard, secondDealerCard) && this.hasBlackjack(firstPlayerCard, secondPlayerCard)) {
      this.gameContext.setState(this.gameContext.getTiedGameState());
      this.gameContext.play();
      return true;
    } else if (this.hasBlackjack(firstDealerCard, secondDealerCard)) {
      this.gameContext.setState(this.gameContext.getLoseGameState());
      this.gameContext.play();
      return true;
    } else if (this.hasBlackjack(firstPlayerCard, secondPlayerCard)) {
      this.gameContext.setState(this.gameContext.getWinGameState());
      this.gameContext.play();
      return true;
    }
    return false;
  }

  private boolean hasBlackjack(Card firstCard, Card secondCard) {
    if (!firstCard.equals(secondCard) && Card.valueOf(firstCard.toString()).getValue() == 1 || Card.valueOf(secondCard.toString()).getValue() == 1) {
      if (Card.valueOf(firstCard.toString()).getValue() == 10 || Card.valueOf(secondCard.toString()).getValue() == 10) {
        return true;
      }
    }
    return false;
  }
}

class WinGameState extends GameState {
  public WinGameState(Blackjack game) {
    this.gameContext = game;
  }

  public void handle() {
    System.out.println(this.gameContext.toString());
    System.out.print("PLAYER WINS!\n\n");
  }
}

class LoseGameState extends GameState {
  public LoseGameState(Blackjack game) {
    this.gameContext = game;
  }

  public void handle() {
    System.out.println(this.gameContext.toString());
    System.out.print("DEALER WINS!\n\n");
  }
}

class TiedGameState extends GameState {
  public TiedGameState(Blackjack game) {
    this.gameContext = game;
  }

  public void handle() {
    System.out.println(this.gameContext.toString());
    System.out.print("TIE GAME!\n\n");
  }
}

