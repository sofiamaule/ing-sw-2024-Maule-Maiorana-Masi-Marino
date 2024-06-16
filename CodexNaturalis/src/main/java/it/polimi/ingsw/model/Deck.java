
package it.polimi.ingsw.model;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.exceptions.DeckEmptyException;
import it.polimi.ingsw.exceptions.FileCastException;
import it.polimi.ingsw.exceptions.FileReadException;
import it.polimi.ingsw.exceptions.JSONParsingException;
import it.polimi.ingsw.model.cards.*;


import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.util.Objects;
import java.util.Random;


public class Deck implements Serializable {
    private int numCards;
    private final CardType cardType;
    private final ArrayList<PlayableCard> frontCards;
    private final ArrayList<PlayableCard> backCards;
    private int randomIndex; //serve per la Board nella GUI così appare nel deck il back della prossima immagine che viene restituita

    public Deck(CardType cardType) {
        this.cardType = cardType;
        this.frontCards = new ArrayList<>();
        this.backCards = new ArrayList<>();

        // numero di carte varia in base al tipo di carta
        switch (cardType) {
                case GoldCard, ResourceCard -> this.numCards = DefaultValue.NumOfGoldCards;
                case InitialCard -> this.numCards = DefaultValue.NumOfInitialCards;
        }
        // Gestisci le eccezioni sollevate da initializeDeck
        try {
            initializeDeck(cardType);
        } catch (FileNotFoundException e) {
            // Gestisci l'eccezione FileNotFoundException
            System.err.println("Error JSON file not found - " + e.getMessage());
        } catch (FileReadException e) {
            // Gestisci l'eccezione FileReadException
            System.err.println("Error during JSON file reading - " + e.getMessage());
        }
        //inizializza il primo numero random (primo numero che viene restituito
        generateRandomIndex();

    }
    public int getRandomIndex(){
        return this.randomIndex;
    }
    public int getNumCards() {
        return numCards;
    }

    public ArrayList<PlayableCard> getFrontCards() {
        return frontCards;
    }

    public ArrayList<PlayableCard> getBackCards() {
        return backCards;
    }
    public CardType getCardType(){
        return cardType;
    }

    /** Initializes the deck of cards (arrayList of PlayableCards) of the specified type.
     *  Reads the JSON files of the specified Card Type containing the front and back cards,
     * and populates the frontCards and backCards lists with the read cards.
     *
     * @author Sofia Maule
     * @param cardType the type of cards to initialize the deck
     *  @throws FileNotFoundException if any of the JSON files are not found.
     *  @throws FileReadException if there is an error while reading the files.
     */

    public void initializeDeck(CardType cardType) throws FileReadException, FileNotFoundException {
        Reader frontReader ;
        Reader backReader ;

        Gson gson = new Gson();

        try {
            // Leggi dal file JSON frontCards
            ArrayList<InitialCard> frontCardList;
            ArrayList<InitialCard> backCardList ;
            switch (cardType) {
                case InitialCard:
                    frontReader = new InputStreamReader(Objects.requireNonNull(Deck.class.getResourceAsStream("/json/InitialCardsFront.json")), StandardCharsets.UTF_8);
                    backReader = new InputStreamReader(Objects.requireNonNull(Deck.class.getResourceAsStream("/json/InitialCardsBack.json")), StandardCharsets.UTF_8);
                    Type initialCardType = new TypeToken<ArrayList<InitialCard>>() {
                    }.getType();
                    frontCardList = gson.fromJson(frontReader, initialCardType);
                    frontCards.addAll(frontCardList);
                    frontReader.close();

                    backCardList = gson.fromJson(backReader, initialCardType);
                    backCards.addAll(backCardList);
                    backReader.close();
                    break;

                case ResourceCard:
                    frontReader = new InputStreamReader(Objects.requireNonNull(Deck.class.getResourceAsStream("/json/ResourceCardsFront.json")), StandardCharsets.UTF_8);
                    backReader = new InputStreamReader(Objects.requireNonNull(Deck.class.getResourceAsStream("/json/ResourceCardsBack.json")), StandardCharsets.UTF_8);
                    Type resourceCardType = new TypeToken<ArrayList<ResourceCard>>() {
                    }.getType();
                    frontCardList = gson.fromJson(frontReader, resourceCardType);
                    frontCards.addAll(frontCardList);
                    frontReader.close();

                    backCardList = gson.fromJson(backReader, resourceCardType);
                    backCards.addAll(backCardList);
                    backReader.close();
                    break;

                case GoldCard:
                    frontReader = new InputStreamReader(Objects.requireNonNull(Deck.class.getResourceAsStream("/json/GoldCardsFront.json")), StandardCharsets.UTF_8);
                    backReader = new InputStreamReader(Objects.requireNonNull(Deck.class.getResourceAsStream("/json/GoldCardsBack.json")), StandardCharsets.UTF_8);
                    Type goldCardType = new TypeToken<ArrayList<GoldCard>>() {
                    }.getType();
                    frontCardList = gson.fromJson(frontReader, goldCardType);
                    frontCards.addAll(frontCardList);
                    frontReader.close();

                    backCardList = gson.fromJson(backReader, goldCardType);
                    backCards.addAll(backCardList);
                    backReader.close();
                    break;
            }
        } catch (FileNotFoundException e) {
            // Eccezione lanciata se il file non viene trovato
            throw new FileNotFoundException("File not found: " + e.getMessage());

        } catch (IOException e) {
            // Eccezione lanciata in caso di problemi durante la lettura del file
            throw new FileReadException("Error reading file: " + e.getMessage());

        } catch (JsonSyntaxException | JsonIOException e) {
            // Eccezione lanciata se ci sono problemi di parsing JSON
            throw new JSONParsingException("JSON file parsing error: " + e.getMessage());

        } catch (ClassCastException e) {
            // Eccezione lanciata se ci sono problemi di casting durante l'accesso ai dati JSON
            throw new FileCastException("CASTING error accessing JSON file data: " + e.getMessage());

        } catch (NullPointerException e) {
            // Eccezione lanciata se ci sono valori nulli non gestiti correttamente
            throw new NullPointerException("Error null values not handled correctly: " + e.getMessage());

        } catch (Exception e) {
            // Eccezione generica per gestire altri tipi di eccezioni
            throw new FileReadException("Generic exception: " + e.getMessage());
        }
    }

    /** @author Sofia Maule
     * Checks the deck's numCards to see if the deck has ended
     * @return { true} if the deck has ended (no more cards are available),
     *         {@code false} otherwise.
     */
    public boolean checkEndDeck() {
        return numCards <= 0;
    }
    private void generateRandomIndex() {
        if (numCards > 0) {
            randomIndex = new Random().nextInt(numCards);
        } else {
            randomIndex = -1; // O un altro valore per indicare che non c'è più alcuna carta
        }
    }


    /**
     * @author Sofia Maule
     * @return an array containing two cards (one front and one back) drawn from the deck
     * Returns two cards from the deck at a random position (position = CardID).
     * Decreases the numCards attribute of the deck.
     * Removes the cards from the arrays
     *
     */
    public PlayableCard[] returnCard() throws DeckEmptyException {
        if (numCards == 0) {
            throw new DeckEmptyException("The deck is empty. No more cards to draw.");
        }

        System.out.println("Numero di carte prima della rimozione: " + numCards);
        System.out.println("Indice casuale: " + randomIndex);

        PlayableCard frontCard = frontCards.get(randomIndex);
        PlayableCard backCard = backCards.get(randomIndex);

        numCards--;

        frontCards.remove(randomIndex);
        backCards.remove(randomIndex);

        if (numCards > 0) {
            generateRandomIndex();
        } else {
            randomIndex = -1; // O un altro valore per indicare che non c'è più alcuna carta
        }

        System.out.println("Numero di carte dopo la rimozione: " + numCards);

        return new PlayableCard[]{frontCard, backCard};
    }

}




