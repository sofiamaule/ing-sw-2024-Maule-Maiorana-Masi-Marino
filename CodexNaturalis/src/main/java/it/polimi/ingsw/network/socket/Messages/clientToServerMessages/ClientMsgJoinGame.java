package it.polimi.ingsw.network.socket.Messages.clientToServerMessages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.listener.GameListenerInterface;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.rmi.GameControllerInterface;

import java.rmi.RemoteException;

/**
 * SocketClientMessageCreateGame class.
 * Extends SocketClientGenericMessage and is used to send a message to the server
 * indicating the request to create a new game.
 */
public class ClientMsgJoinGame extends ClientGenericMessage {
    /**
     * Constructor of the class.
     * @param nickname the player's nickname
     */
    private Color color;
    public ClientMsgJoinGame(String nickname, Color color) {
        this.nickname = nickname;
        this.isJoinGame= true;
        this.color= color;
    }

    @Override
    public void execute(GameListenerInterface lis, GameController gameController) throws RemoteException {
        gameController.joinGame(lis, this.nickname, this.color);
    }

    /**
     * Method to execute the corresponding action for the message.
     * @param gameController the main controller of the application
     * @return the game controller interface
     * @throws RemoteException if there is an error in remote communication
     */
    @Override
    public void execute(GameControllerInterface gameController) throws RemoteException {

    }


}
