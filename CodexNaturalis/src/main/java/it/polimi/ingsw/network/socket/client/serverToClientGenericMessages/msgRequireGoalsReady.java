package it.polimi.ingsw.network.socket.client.serverToClientGenericMessages;

import it.polimi.ingsw.listener.GameListenerInterface;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.game.GameImmutable;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class msgRequireGoalsReady extends SocketServerGenericMessage {
    private GameImmutable model;

    public msgRequireGoalsReady(GameImmutable model) {
        this.model = model;
    }

    /**
     * Method to execute the corresponding action for the message.
     * @param lis the game listener
     * @throws RemoteException if there is a remote exception
     */
    @Override
    public void execute(GameListenerInterface lis) throws RemoteException {
        lis.requireGoalsReady(model);
    }
}
