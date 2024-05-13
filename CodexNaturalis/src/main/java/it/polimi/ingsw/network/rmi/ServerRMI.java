package it.polimi.ingsw.network.rmi;

//import it.polimi.ingsw.model.Chat.Message; (CHAT)
import it.polimi.ingsw.exceptions.FileReadException;
import it.polimi.ingsw.listener.GameListenerInterface;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.cards.CardType;
//import it.polimi.ingsw.network.HeartbeatSender;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.network.socket.Messages.clientToServerMessages.*;
import it.polimi.ingsw.network.socket.Messages.serverToClientMessages.ServerGenericMessage;
import it.polimi.ingsw.model.DefaultValue;

import it.polimi.ingsw.network.socket.client.GameListenersClient;
import it.polimi.ingsw.view.flow.Flow;
import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static it.polimi.ingsw.network.PrintAsync.printAsync;
import static it.polimi.ingsw.view.TUI.PrintAsync.printAsyncNoLine;

/**
 * RMIServer Class
 * Handle all the incoming network requests that clients can require to create,join,leave or reconnect to a game
 * by the RMI Network protocol
 */

/*
public class ServerRMI extends UnicastRemoteObject implements MainControllerInterface, ServerInterface {

    private static final int PORT_RMI =1099 ;
    private Registry registry;
    private MainControllerInterface server;

    public ServerRMI() throws RemoteException {
        super();
    }

    @Override
    public void start() {
        try {
            server = new ServerRMI() ;
            GameControllerInterface stub = (GameControllerInterface) UnicastRemoteObject.exportObject(server, 0);

            registry = LocateRegistry.createRegistry(PORT_RMI);
            registry.rebind("ServerCommunicationInterface", stub);

        } catch (RemoteException e) {
            System.err.println("Another server is already running. Closing this instance...");
            System.exit(0);
        }

        System.out.println("RMI server started on port " + PORT_RMI + ".");
    }

    @Override
    public void stop() {
        try {
            registry.unbind("ServerCommunicationInterface");
            UnicastRemoteObject.unexportObject(server, true);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            System.err.println("Unable to stop the RMI server.");
        }
        System.out.println("RMI server stopped.");
    }


}*/

