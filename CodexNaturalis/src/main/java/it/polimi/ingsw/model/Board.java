package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.DeckEmptyException;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.interfaces.ObjectiveCardIC;

import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.network.PrintAsync.printAsync;


/**
 * Board model class
 * This class represents the game board that contains decks of cards and cards placed on the board.
 * @author Irene Pia Masi
 */
public class Board implements Serializable {
    private final ArrayList<PlayableCard[]> goldCards;
    private final ArrayList<PlayableCard[]> resourceCards;
    private final ObjectiveCard[] objectiveCards; //commonGoals
    private final Deck goldCardsDeck;
    private final Deck resourcesCardsDeck;
    private final ObjectiveDeck objectiveCardsDeck;


    /**
     * Constructor
     */
    public Board()  {
        this.goldCards = new ArrayList<>();
        this.resourceCards = new ArrayList<>();
        this.objectiveCards = new ObjectiveCard[2];
        this.goldCardsDeck = new Deck(CardType.GoldCard);
        this.resourcesCardsDeck = new Deck(CardType.ResourceCard);
        this.objectiveCardsDeck = new ObjectiveDeck();

        initializeBoard();
    }

    /**
     * Initializes the board by placing cards on it.
     * Sets the common Goals
     */
    public void initializeBoard()  {
        //posiziono due carte oro e due carte risorsa sul tavolo
        //perchè la board ha 2 coppie di carte per ciascuna tipologia di carta
        try {

            int MAX_SIZE = 2;
            for (int i = 0; i < MAX_SIZE; i++) {
                PlayableCard[] goldCards = goldCardsDeck.returnCard();
                PlayableCard[] resourceCards = resourcesCardsDeck.returnCard();
                this.goldCards.add(goldCards);
                this.resourceCards.add(resourceCards);
            }
            //posiziono le carte obbiettivo comune
            for (int i = 0; i < MAX_SIZE; i++) {
                ObjectiveCard objectiveCard = objectiveCardsDeck.returnCard();
                this.objectiveCards[i] = objectiveCard;
            }
        }catch (DeckEmptyException e){
            System.err.println("Error during board initialization");
        }
    }
    public ObjectiveDeck getObjectiveCardsDeck(){
        return this.objectiveCardsDeck;
    }
    public ObjectiveCard[] getObjectiveCards() {
        return this.objectiveCards;
    } //OBBIETTIVI COMUNI
    public ArrayList<PlayableCard[]> getGoldCards() {
        return goldCards;
    }
    public ArrayList<PlayableCard[]> getResourceCards() {
        return resourceCards;
    }
    public Deck getGoldCardsDeck() {
        return goldCardsDeck;
    }
    public Deck getResourcesCardsDeck() {
        return resourcesCardsDeck;
    }


    /**
     * RETRIEVES A CARD FROM THE OBJECTIVECARDS DECK
     * @return an ObjectiveCard picked from Objective Cards' deck
     */
    public ObjectiveCard takeObjectiveCard() throws DeckEmptyException {
        return objectiveCardsDeck.returnCard();
    }




    /**
     * Takes a card from the board.
     *
     * @param cardType     The type of card to take
     * @param drawFromDeck True if the card should be drawn from the deck, false if it should be taken from the board.
     * @param pos          The position of the card to take if drawing from the board.
     * @return The card taken from the board, or null.
     */
    public PlayableCard[] takeCardfromBoard(CardType cardType, boolean drawFromDeck, int pos) throws IllegalArgumentException, DeckEmptyException, IndexOutOfBoundsException  {
        if (drawFromDeck) {
            if(cardType== CardType.InitialCard){
                throw new IllegalArgumentException();
            }
            if (cardType == CardType.GoldCard && goldCardsDeck.checkEndDeck()) {
                throw new DeckEmptyException("Gold cards' deck is empty");
            } else if (cardType == CardType.ResourceCard && resourcesCardsDeck.checkEndDeck()) {
                throw new DeckEmptyException("Resource cards' deck is empty");
            }
            switch (cardType) {
                case GoldCard:
                    return goldCardsDeck.returnCard();
                case ResourceCard:
                    return resourcesCardsDeck.returnCard();
                default:
                    return null;
            }
        } else {
            ArrayList<PlayableCard[]> cardsOnBoard =null;
            PlayableCard[] selectedCards = null;
            switch (cardType) {
                case GoldCard:
                    cardsOnBoard = goldCards;
                    break;
                case ResourceCard:
                    cardsOnBoard = resourceCards;
                    break;
            }
            // Controllo che la posizione sia valida
            if (pos < 0 || pos >= cardsOnBoard.size()) {
                throw new IndexOutOfBoundsException("Position not valid on the board");
            }
            switch (pos){
                case 0-> selectedCards= cardsOnBoard.get(0);
                case 1-> selectedCards= cardsOnBoard.get(1);
            }

            // Rimuovi front e back card dall'array
            cardsOnBoard.remove(pos);

            // Aggiorno l'array e restituisco la carta selezionata
            updateArray(cardsOnBoard, cardType);

            return selectedCards;
        }
    }


        /**
         * Update array.
         * @param cards The array of cards to update.
         * @param cardType The type of card for which to update the array.
         */
    public void updateArray(ArrayList<PlayableCard[]> cards, CardType cardType) throws DeckEmptyException {
        Deck deck ;
        switch (cardType) {
            case GoldCard:
                deck = goldCardsDeck;
                break;
            case ResourceCard:
                deck = resourcesCardsDeck;
                break;
            default:
                return;
        }
        PlayableCard[] newCard = deck.returnCard();
        cards.add(newCard);
    }


    /**
     * Verify the number of Gold cards on the Board
     * @return TRUE if there's a correct number of Gold Cards on the Board
     */
    public boolean verifyGoldCardsNumber() {
        return goldCards.size() == 2;
    }

    /**
     * Verify the number of Resource cards on the Board
     * @return TRUE if there's a correct number of Resource Cards on the Board
     */
    public boolean verifyResourceCardsNumber() {
        return resourceCards.size() == 2;
    }

    /**
     * Verify the number of Objective cards on the Board
     * @return TRUE if there's a correct number of Objective Cards on the Board
     */
    public boolean verifyObjectiveCardsNumber() {
        return objectiveCards.length == 2;
    }

    /**
     * Verify the number of Gold cards on the GoldDeck
     * @return TRUE if there's a correct number of Gold Cards on the Board
     * @param playersNumber to calculate the right number of Cards
     */
    public boolean verifyGoldDeckSize(int playersNumber) {
        return (goldCardsDeck.getNumCards())  == DefaultValue.NumOfGoldCards- 2 - playersNumber; //su Deck numCards va da 0 a 40
    }

    /**
     * Verify the number of Resource cards in the relative Deck
     * @return TRUE if there's a correct number of Resource Cards in the Deck on the Board
     * @param playersNumber to calculate the right number of Cards
     */
    public boolean verifyResourceDeckSize(int playersNumber) {
        return (resourcesCardsDeck.getNumCards())  == DefaultValue.NumOfResourceCards- 2 - (2* playersNumber); //su Deck numCards va da 0 a 40

    }

    /**
     * Verify the number of Objective cards in the relative Deck
     * @return TRUE if there's a correct number of Objective Cards in the Deck on the Board
     * @param playersNumber to calculate the right number of Cards
     */
    public boolean verifyObjectiveDeckSize(int playersNumber) {
        return objectiveCardsDeck.getNumCards() == DefaultValue.NumOfObjectiveCards - 2 - playersNumber;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("***********BOARD***********\n");
        result.append("\n");

        // Inizializziamo le righe con StringBuilder
        ArrayList<StringBuilder> rowBuilders = new ArrayList<>();
        for (int k = 0; k < DefaultValue.printHeight + 2; k++) {
            rowBuilders.add(new StringBuilder());
        }

        // GOLDCARDS
        result.append("***GOLDCARDS***: \n");
        result.append("\n");
        result.append(cardsGoldToString());
        result.append("\n");
        // RESOURCECARDS
        result.append("***RESOURCECARDS***: \n");
        result.append("\n");
        result.append(cardsResourceToString());
        result.append("\n");
        // OBJECTIVE
        result.append("*******COMMON GOALS*******: \n");
        result.append("\n");
        result.append(cardsObjectiveToString());
        result.append("\n");
        return result.toString();
    }

    public String cardsObjectiveToString() {
        ObjectiveCard[] objectiveCards = getObjectiveCards();

        // Lista per accumulare le stringhe delle righe
        ArrayList<StringBuilder> rowBuilders = new ArrayList<>();

        // Inizializziamo le righe con StringBuilder
        for (int k = 0; k < DefaultValue.printHeight + 2; k++) {
            rowBuilders.add(new StringBuilder());
        }

        for (int i = 0; i < objectiveCards.length; i++) {
            ObjectiveCard card = objectiveCards[i];
            String[] lines = card.toString().split("\n");

            for (int k = 0; k < lines.length; k++) {
                rowBuilders.get(k).append(lines[k]).append(" ");
            }
        }

        // Costruiamo l'output finale unendo tutte le righe
        StringBuilder result = new StringBuilder();
        for (StringBuilder sb : rowBuilders) {
            result.append(sb.toString().stripTrailing()).append("\n");
        }
        return result.toString();
    }

    public String cardsGoldToString() {
        // Lista per accumulare le stringhe delle righe
        ArrayList<StringBuilder> rowBuilders = new ArrayList<>();

        // Inizializziamo le righe con StringBuilder
        for (int k = 0; k < DefaultValue.printHeight + 2; k++) {
            rowBuilders.add(new StringBuilder());
        }

        PlayableCard card = goldCardsDeck.getBackCards().get(0);
        String[] lines = card.toString().split("\n");

        for (int k = 0; k < lines.length; k++) {
            rowBuilders.get(k).append(lines[k]).append(" ");
        }

        // Iteriamo attraverso le carte
        for (int i = 0; i < 2; i++) {
            card = goldCards.get(i)[0];
            lines = card.toString().split("\n");

            for (int k = 0; k < lines.length; k++) {
                rowBuilders.get(k).append(lines[k]).append(" ");
            }
        }

        // Costruiamo l'output finale unendo tutte le righe
        StringBuilder result = new StringBuilder();
        for (StringBuilder sb : rowBuilders) {
            result.append(sb.toString().stripTrailing()).append("\n");
        }

        return result.toString();
    }


    public String cardsResourceToString() {
        // Lista per accumulare le stringhe delle righe
        ArrayList<StringBuilder> rowBuilders = new ArrayList<>();

        // Inizializziamo le righe con StringBuilder
        for (int k = 0; k < DefaultValue.printHeight + 2; k++) {
            rowBuilders.add(new StringBuilder());
        }

        PlayableCard card = resourcesCardsDeck.getBackCards().get(0);
        String[] lines = card.toString().split("\n");

        for (int k = 0; k < lines.length; k++) {
            rowBuilders.get(k).append(lines[k]).append(" ");
        }

        // Iteriamo attraverso le carte
        for (int i = 0; i < 2; i++) {
            card = resourceCards.get(i)[0];
            lines = card.toString().split("\n");

            for (int k = 0; k < lines.length; k++) {
                rowBuilders.get(k).append(lines[k]).append(" ");
            }
        }

        // Costruiamo l'output finale unendo tutte le righe
        StringBuilder result = new StringBuilder();
        for (StringBuilder sb : rowBuilders) {
            result.append(sb.toString().stripTrailing()).append("\n");
        }

        return result.toString();
    }



    public String cardsVisibleGoldToString() {
        // Lista per accumulare le stringhe delle righe
        ArrayList<StringBuilder> rowBuilders = new ArrayList<>();

        // Inizializziamo le righe con StringBuilder
        for (int k = 0; k < DefaultValue.printHeight + 2; k++) {
            rowBuilders.add(new StringBuilder());
        }


        // Iteriamo attraverso le carte
        for (int i = 0; i < 2; i++) {
            PlayableCard card = goldCards.get(i)[0];
            String[] lines = card.toString().split("\n");

            for (int k = 0; k < lines.length; k++) {
                if (k == 0) {
                    // Aggiungiamo l'indicatore solo alla prima riga di ogni carta
                    rowBuilders.get(k).append(i).append(" ").append(lines[k]).append(" ");
                } else {
                    rowBuilders.get(k).append("  ").append(lines[k]).append(" ");
                }
            }
        }

        // Costruiamo l'output finale unendo tutte le righe
        StringBuilder result = new StringBuilder();
        for (StringBuilder sb : rowBuilders) {
            result.append(sb.toString().stripTrailing()).append("\n");
        }
        return result.toString();
    }


public String cardsVisibleResourceToString() {
    // Lista per accumulare le stringhe delle righe
    ArrayList<StringBuilder> rowBuilders = new ArrayList<>();

    // Inizializziamo le righe con StringBuilder
    for (int k = 0; k < DefaultValue.printHeight + 2; k++) {
        rowBuilders.add(new StringBuilder());
    }

    for (int i = 0; i < 2; i++) {
        PlayableCard card = resourceCards.get(i)[0];
        String[] lines = card.toString().split("\n");

        for (int k = 0; k < lines.length; k++) {
            if (k == 0) {
                // Aggiungiamo l'indicatore solo alla prima riga di ogni carta
                rowBuilders.get(k).append(i).append(" ").append(lines[k]).append(" ");
            } else {
                rowBuilders.get(k).append("  ").append(lines[k]).append(" ");
            }
        }
    }

    // Costruiamo l'output finale unendo tutte le righe
    StringBuilder result = new StringBuilder();
    for (StringBuilder sb : rowBuilders) {
        result.append(sb.toString().stripTrailing()).append("\n");
    }

    return result.toString();
    }
}

