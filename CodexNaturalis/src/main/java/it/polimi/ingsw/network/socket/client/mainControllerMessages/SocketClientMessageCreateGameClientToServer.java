package it.polimi.ingsw.network.socket.client.mainControllerMessages;

import it.polimi.ingsw.listener.GameListenerInterface;
import it.polimi.ingsw.network.rmi.GameControllerInterface;
import it.polimi.ingsw.network.rmi.MainControllerInterface;
import it.polimi.ingsw.network.socket.client.MessageClientToServer;

import java.rmi.RemoteException;

/**
 * SocketClientMessageCreateGame class.
 * Extends SocketClientGenericMessage and is used to send a message to the server
 * indicating the request to create a new game.
 */
public class SocketClientMessageCreateGameClientToServer extends MessageClientToServer {
    /**
     * Constructor of the class.
     * @param nickname the player's nickname
     */
    public SocketClientMessageCreateGameClientToServer(String nickname) {
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
    public GameControllerInterface execute(GameListenerInterface lis, MainControllerInterface mainController) throws RemoteException {
        return mainController.createGame(lis, nickname);
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
