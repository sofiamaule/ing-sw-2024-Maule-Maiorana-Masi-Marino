package it.polimi.ingsw.listener;

import it.polimi.ingsw.Chat.Message;
import it.polimi.ingsw.exceptions.FileReadException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.game.GameImmutable;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Contains methods to NOTIFY the CLIENT about different game events
 */
public interface GameListenerInterface extends Remote {

    /**
     * This method is used to notify the client that a player has joined the game
     * @param model is the game model
     * @throws RemoteException if the reference could not be accessed
     */
    void playerJoined(GameImmutable model, String nickname, Color playerColor) throws RemoteException;
    void requireNumPlayersGameID(GameImmutable model)throws RemoteException;

    /**
     * This method is used to notify the client that a player has to choose again the CARD
     * @param model is the game model
     * @throws RemoteException if the reference could not be accessed
     */
    void wrongChooseCard(GameImmutable model) throws RemoteException;


    void pointsAdded(GameImmutable model)throws RemoteException;

    /**
     * This method is used to notify the client that a player has left the game
     * @param model is the game model {@link GameImmutable}
     * @param nickname is the nickname of the player that has left
     * @throws RemoteException if the reference could not be accessed
     */
    void playerLeft(GameImmutable model, String nickname) throws RemoteException;

    /**
     * This method is used to notify the client that a player has tried to join the game but the game is full
     * @param triedToJoin is the player that has tried to join the game
     * @param model is the game model
     * @throws RemoteException if the reference could not be accessed
     */
    void joinUnableGameFull(Player triedToJoin, GameImmutable model) throws RemoteException;


    /**
     * This method is used to notify the client that a player has tried to join the game but the nickname is already in use

     * @throws RemoteException if the reference could not be accessed
     */
    void joinUnableNicknameAlreadyIn(Player triedToJoin) throws RemoteException;


    /**
     * This method is used to notify the client that the game has started
     * @param model is the game model {@link GameImmutable}
     * @throws RemoteException if the reference could not be accessed
     */
    void gameStarted(GameImmutable model) throws RemoteException;

    /**
     * This method is used to notify the client that the game has ended
     * @param model is the game model
     * @throws RemoteException if the reference could not be accessed
     */
    void gameEnded(GameImmutable model) throws RemoteException;

    /**
     * Notifies the listeners that initial cards are ready to be displayed.
     *
     * @param model       is the game model
     * @throws RemoteException if the reference could not be accessed
     */
    void requireInitialReady(GameImmutable model) throws IOException, FileReadException;

    /**
     * Notifies the listeners that objective cards are ready to be chosen
     * @param model is the game model
     * @throws RemoteException if the reference could not be accessed
     */

    void requireGoalsReady(GameImmutable model) throws RemoteException;

    /**
     * This method is used to notify that a card has been placed on the book
     * @param model is the game model
     * @throws RemoteException if the reference could not be accessed
     */
    void cardPlaced(GameImmutable model) throws RemoteException;


    /**
     * This method is used to notify that a card has been drawn
     * @param model is the game model
     * @throws RemoteException if the reference could not be accessed
     */
    void cardDrawn(GameImmutable model) throws RemoteException;


    /**
     * This method is used to notify that the next turn triggered
     * @param model is the game model
     * @throws RemoteException if the reference could not be accessed
     */
    void nextTurn(GameImmutable model) throws RemoteException;

    /**
     * This method is used to notify that a player has disconnected
     * @param model is the game model
     * @param nick is the nickname of the player that has disconnected
     * @throws RemoteException if the reference could not be accessed
     */
    void playerDisconnected(GameImmutable model, String nick) throws RemoteException;

    /**
     * This method is used to notify that the last circle has started
     * @param model is the game model {@link GameImmutable}
     * @throws RemoteException if the reference could not be accessed
     */
    void lastCircle(GameImmutable model) throws RemoteException;

   void playerReady(GameImmutable gameImmutable, String nickname)throws RemoteException;

    void sentMessage(GameImmutable model, Message msg) throws RemoteException;


}
