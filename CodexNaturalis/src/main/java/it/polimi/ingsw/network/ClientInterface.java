package it.polimi.ingsw.network;
import it.polimi.ingsw.model.Chat.Message;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

//interface for common client actions
public interface ClientInterface  {

    /**
     * Creates a new game
     *
     * @param nick
     * @throws IOException
     * @throws InterruptedException
     * @throws NotBoundException
     */
    void createGame(String nick) throws IOException, InterruptedException, NotBoundException;

    /**
     * Joins the first game found in the list of games
     *
     * @param nick
     * @throws IOException
     * @throws InterruptedException
     * @throws NotBoundException
     */
    void joinFirstAvailable(String nick) throws IOException, InterruptedException, NotBoundException;

    /**
     * Adds the player to the game
     *
     * @param nick
     * @param idGame
     * @throws IOException
     * @throws InterruptedException
     * @throws NotBoundException
     */
    void joinGame(String nick, int idGame) throws IOException, InterruptedException, NotBoundException;

    /**
     * Reconnect the player to the game
     *
     * @param nick
     * @param idGame
     * @throws IOException
     * @throws InterruptedException
     * @throws NotBoundException
     */
    void reconnect(String nick, int idGame) throws IOException, InterruptedException, NotBoundException;
    //FUNZIONALITA AGGIUNTIVA
    /**
     * Leaves the game
     *
     * @param nick
     * @param idGame
     * @throws IOException
     * @throws NotBoundException
     */
    void leave(String nick, int idGame) throws IOException, NotBoundException;

    /**
     * Sets the invoker as ready
     *
     * @throws IOException
     */
    void setAsReady() throws IOException;

    /**
     * Checks if it's the invoker's turn
     *
     * @return
     * @throws RemoteException
     */
    boolean isMyTurn() throws RemoteException;

    /**
     * Sends a message in chat
     *
     * @param msg message
     * @throws RemoteException
     */
    void sendMessage(Message msg) throws RemoteException;


    /**
     * Pings the server
     *
     * @throws RemoteException
     */
    void heartbeat() throws RemoteException;
}
