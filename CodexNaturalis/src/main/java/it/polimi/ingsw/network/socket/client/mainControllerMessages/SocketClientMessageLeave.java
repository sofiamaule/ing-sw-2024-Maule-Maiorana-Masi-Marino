package it.polimi.ingsw.network.socket.client.mainControllerMessages;

import it.polimi.ingsw.listener.GameListener;
import it.polimi.ingsw.network.rmi.GameControllerInterface;
import it.polimi.ingsw.network.rmi.MainControllerInterface;
import it.polimi.ingsw.network.socket.client.SocketClientGenericMessage;

import java.rmi.RemoteException;

/**
 * SocketClientMessageLeave class.
 * Extends SocketClientGenericMessage and is used to send a message to the server
 * indicating the request to leave a game.
 */
public class SocketClientMessageLeave extends SocketClientGenericMessage {
    int idGame;

    /**
     * Constructor of the class.
     * @param nickname the player's nickname
     * @param idGame the ID of the game to leave
     */
    public SocketClientMessageLeave(String nickname, int idGame) {
        this.idGame = idGame;
        this.nickname = nickname;
        this.isMessageForMainController = true;
    }

    /**
     * Method to execute the corresponding action for the message.
     * @param lis the game listener
     * @param mainController the main controller of the application
     * @return the game controller interface
     * @throws RemoteException if there is an error in remote communication
     */
    @Override
    public GameControllerInterface execute(GameListener lis, MainControllerInterface mainController) throws RemoteException {
        return mainController.leaveGame(lis, nickname, idGame);
    }

    /**
     * Method to execute the corresponding action for the message.
     * @param mainController the game controller interface
     * @throws RemoteException if there is an error in remote communication
     */
    @Override
    public void execute(GameControllerInterface mainController) throws RemoteException {

    }
}
