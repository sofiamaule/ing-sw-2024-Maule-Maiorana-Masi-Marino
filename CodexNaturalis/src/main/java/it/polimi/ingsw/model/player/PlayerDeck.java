package it.polimi.ingsw.model.player;


import it.polimi.ingsw.exceptions.DeckFullException;
import it.polimi.ingsw.model.cards.PlayableCard;

import java.util.ArrayList;

public class PlayerDeck {
     public  ArrayList<PlayableCard> miniDeck;

     public int actualNumCards;

     public PlayerDeck(){
         miniDeck= new ArrayList<>(6);
         actualNumCards=0;
     }

     public ArrayList<PlayableCard> getMiniDeck(){
          return this.miniDeck;
     }
     public int getNumCards(){
          return this.actualNumCards;
     }

     /**
      * Adds a new card to the mini deck if there is space available.
      * remember: a card is identified by an array of PlayableCards
      * (first the front then the back) and they are both added to the playerDeck one after the other
      *
      * @param newCard The new card to be added to the mini deck.
      */
     public void addCard(PlayableCard[] newCard) throws DeckFullException, IllegalArgumentException {
          // Verifica che l'array di nuove carte abbia esattamente due elementi (front e back)
          if (newCard == null || newCard.length != 2) {
               throw new IllegalArgumentException("Invalid card array: A new card should contain exactly two cards (front and back).");
          }

          // Verifica se ci sono meno di 6 carte nel mini deck
          if (actualNumCards >= 6) {
               throw new DeckFullException("The playerDeck is full. Cannot add more cards.");
          }

          // Aggiungi la prima PlayableCard (fronte) al mini deck
          miniDeck.add(newCard[0]);
          // Aggiungi la seconda PlayableCard (retro) al mini deck
          miniDeck.add(newCard[1]);
          // Incrementa il numero effettivo di carte
          actualNumCards += 2;
     }

     /**
      * Removes a card from the mini deck at the specified position.
      * remember: both the chosen card and its front/back must be removed
      * if the card is a front -> its back is in the next position
      *   (and opposite)
      * @param pos The position of the card to be removed.
      * @throws IndexOutOfBoundsException If the position is out of range (pos < 0 || pos >= miniDeck.size()).
      */
     public void removeCard(int pos) throws IndexOutOfBoundsException {
          // Verifies if the position is valid
          // Verifies if there are enough cards to remove
          if (pos < 0 || pos > miniDeck.size()) {
               throw new IndexOutOfBoundsException("Position is out of range");
          }

          /*If the card at the specified position is the front
          if (miniDeck.get(pos).isFront()) {
               // Ensures there is a card at the next position
               if (pos <= miniDeck.size() - 1) {
                    miniDeck.remove(pos + 1); // Removes the back
               }
          } else { // If the card at the specified position is the back
               // Ensures there is a card at the previous position
               if (pos > 0) {
                    miniDeck.remove(pos - 1); // Removes the front
               }
          }*/
          if (pos % 2 == 0) {
               // Se la posizione specificata è pari, si tratta di un fronte (posizione 0, 2, 4, ...)
               // Assicurati che la prossima posizione (pos + 1) sia valida
               if (pos + 1 < miniDeck.size()) {
                    miniDeck.remove(pos + 1); // Rimuovi il retro
                    miniDeck.remove(pos); // Removes the current card
               }
          } else {
               miniDeck.remove(pos); // Removes the current card
               // Se la posizione specificata è dispari, si tratta di un retro (posizione 1, 3, 5, ...)
              miniDeck.remove(pos - 1); // Rimuovi il fronte (posizione precedente)
          }

          actualNumCards -= 2; // Decrements the effective number of cards (two cards removed)
     }

}
