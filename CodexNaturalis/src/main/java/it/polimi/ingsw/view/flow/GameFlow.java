package it.polimi.ingsw.view.flow;


import it.polimi.ingsw.exceptions.FileReadException;
import it.polimi.ingsw.model.DefaultValue;
import it.polimi.ingsw.model.game.GameImmutable;
import it.polimi.ingsw.model.game.GameStatus;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.rmi.ClientRMI;
import it.polimi.ingsw.network.socket.client.ClientSocket;
import it.polimi.ingsw.view.Utilities.InputController;
import it.polimi.ingsw.view.Utilities.InputReader;
import it.polimi.ingsw.view.Utilities.InputTUI;
import it.polimi.ingsw.view.Utilities.UI;
import it.polimi.ingsw.view.TUI.TUI;
import it.polimi.ingsw.view.events.Event;
import it.polimi.ingsw.view.events.EventList;
import it.polimi.ingsw.view.events.EventType;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;

//Capire come parte il pescaggio delle carte nel 1°Truno di gioco

//Gestisce il flusso di gioco e l'interazione tra client e server
public class GameFlow extends Flow implements Runnable, ClientInterface {
    private String nickname;
    private final EventList events = new EventList();
    private ClientInterface clientActions;
    private String lastPlayerReconnected;
    private final UI ui;
    protected InputController inputController;
    protected InputReader inputReader;
    protected List<String> importantEvents;
    private boolean ended = false;


    /**
     * Constructor of the class, based on the connection type it creates the clientActions and initializes the UI,
     * the FileDisconnection, the InputReader and the InputController
     *
     * @param connectionType the connection type
     */
    public GameFlow(ConnectionType connectionType) {
        //Invoked for starting with TUI
        switch (connectionType) {
            case SOCKET -> clientActions = new ClientSocket(this);
            case RMI -> clientActions = new ClientRMI(this);
        }
        ui = new TUI();

        importantEvents = new ArrayList<>();
        nickname = "";
        //fileDisconnection = new FileDisconnection();
        this.inputReader = new InputTUI();
        this.inputController = new InputController(this.inputReader.getBuffer(), this);

        new Thread(this).start();

    }


    /*

    //Costruttore per la GUI

    public GameFlow(GUIApplication guiApplication, ConnectionType connectionType) {
        //Invoked for starting with GUI
        switch (connectionType) {
            case SOCKET -> clientActions = new ClientSocket(this);
            case RMI -> clientActions = new ClientRMI(this);
        }
        this.inputReader = new inputGUI();

        ui = new GUI(guiApplication, (inputGUI) inputReader);
        importantEvents = new ArrayList<>();
        nickname = "";
        fileDisconnection = new FileDisconnection();

        this.inputController = new InputController(this.inputReader.getBuffer(), this);
        new Thread(this).start();
    }
    */


    @Override
    public void run() {
        Event event;
        try {
            //inizializza l'interfaccia utente per mostrare la schermata iniziale
            ui.show_publisher();
            events.add(null, EventType.BACK_TO_MENU);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        while (!Thread.interrupted()) {
            if (events.isJoined()) {
                //Get one event
                event = events.pop();
                if (event != null) {
                    //if something happened
                    switch (event.getModel().getStatus()) {
                        case WAIT -> {
                            try {
                                statusWait(event);
                            } catch (IOException | InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        case RUNNING, LAST_CIRCLE -> {
                            try {
                                statusRunning(event);
                            } catch (IOException | InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        case ENDED -> statusEnded(event);
                    }
                }
            } else {
                event = events.pop();
                if (event != null) {
                    statusNotInAGame(event);
                }
            }
            try {
                //dopo ogni ciclo il thread dorme per 100 ms
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void statusWait(Event event) throws IOException, InterruptedException{
        String nicknameLastPlayer = event.getModel().getLastPlayer().getNickname();
        switch (event.getType()) {
            case PLAYER_JOINED -> {
                //Se l'evento è di tipo player joined significa che un giocatore si è unito alla lobby
                //verifico che il giocatore in lobby è l'ultimo giocatore ad aver eseguito l'azione
                if (nicknameLastPlayer.equals(nickname)) {
                    ui.show_playerJoined(event.getModel(), nickname);
                    askReadyToStart();
                }
            }
        }
    }

    public void statusRunning(Event event) throws IOException, InterruptedException{
        switch (event.getType()) {
            case GAME_STARTED -> {
                ui.show_gameStarted(event.getModel());

                this.inputController.setPlayer(event.getModel().getPlayerEntity(nickname));
                this.inputController.setGameID(event.getModel().getGameId());

            }
            case NEXT_TURN -> {
                if (event.getModel().getNicknameCurrentPlaying().equals(nickname)) {

                    askPlaceCards(event.getModel());


                }
            }

            case CARD_PLACED ->{
                askPickCard(event.getModel());
            }

            case CARD_PLACED_NOT_CORRECT -> {

            }






        }

    }


    //metodo chiamato quando un giocatore non viene aggiunto alla partita correttamente
    public void statusNotInAGame(Event event) throws NotBoundException, IOException, InterruptedException {
        switch (event.getType()) {

            //caso: game non valido -> back to menu
            case BACK_TO_MENU -> {
                //ciclo per chiedere al giocatore di selezionare una partita valida
                askNickname();
                joinGame(nickname); //non gli faccio scegliere l'ID

            }

            case NICKNAME_ALREADY_IN -> {
                nickname = null;
                events.add(null, EventType.BACK_TO_MENU); //aggiunge evento nullo per tornare al menu principale
                ui.addImportantEvent("ERROR: Nickname already used!");
            }

            case GAME_FULL -> {
                nickname = null;
                events.add(null, EventType.BACK_TO_MENU);
                ui.addImportantEvent("ERROR: Game is full!");
            }

            case GENERIC_ERROR -> {
                nickname = null;
                ui.show_returnToMenuMsg(); //mostra un messaggio di ritorno al menu sull'interfaccia utente
                try {
                    this.inputController.getUnprocessedData().popInputData(); //rimuovo il dato non elaborato dal buffer
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                events.add(null, EventType.BACK_TO_MENU);
            }
        }
    }

    public void statusEnded(Event event) throws NotBoundException, IOException {
        switch (event.getType()) {
            case GAME_ENDED -> {
                ui.show_returnToMenuMsg();
                //rimuove tutt i dati non elaborati dal buffer
                this.inputController.getUnprocessedData().popAllData();
                try {
                    this.inputController.getUnprocessedData().popInputData();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                //il giocatore lascia la partita
                this.leave(nickname, event.getModel().getGameId());
                this.playerLeftForGameEnded(); //notifica l'utente che ha lasciato la partita
            }
        }

    }
    public void playerLeftForGameEnded(){
        ended = true;
        ui.resetImportantEvents();
        events.add(null,EventType.BACK_TO_MENU);

        this.inputController.setPlayer(null); //il giocatore non è più associato al flusso di gioco
        this.inputController.setGameID(null);
    }
    public boolean isEnded(){
        return ended;
    }
    public void setEnded(boolean ended) {
        this.ended = ended;
    }



    /* METODI ASK DA FARE */
    //Azioni che il Server richiede al Client di eseguire


    private void askNickname() {
        ui.show_insertNicknameMessage();
        try {
            nickname = this.inputController.getUnprocessedData().popInputData();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ui.show_chosenNickname(nickname);
    }

    /**
     * The method repeatedly checks for user input until the user confirms they're ready by entering "yes".
     * If any other input is received, the method continues to wait for the correct input.

     * Once the user confirms their readiness, the `setAsReady()` method is called to proceed with the next step.
     */
    public void askReadyToStart(){
        String answer;
        do {
            try {
                answer = this.inputController.getUnprocessedData().popInputData();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (!answer.equals("yes"));
        setAsReady();
    }

    /**
     * Sets the client as ready by calling the server methods.
     * @throws IOException if there is a communication error during the operation.
     */
    @Override
    public void setAsReady() {
        try {
            clientActions.setAsReady();
        } catch (IOException e){
            noConnectionError();
        }
    }

    //metodo per chiedere il numero della carta (scelta fronte o retro, o carta 1 o 2)
    private Integer askNum(String message, GameImmutable model){
        String temp;
        int num = -1;
        do {
            try {
                ui.show_askNum(message, model, nickname);
                //System.out.flush();

                try {
                    temp = this.inputController.getUnprocessedData().popInputData();
                    if (ended) return null; //il giocatore non può fare mosse
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                num = Integer.parseInt(temp); //traduco il numero in integer
            } catch (InputMismatchException | NumberFormatException e) {
                ui.show_NotValidMessage();
            }
        } while (num < 0);
        return num;
    }


    public void askPlaceCards(GameImmutable model){
        int posChosenCard;
        ui.show_askPlaceCardsMainMsg(); //è il TUO TURNO //PlayerDeck gli rimane sempre mostrata a video
                                        // Il Book del CURRENT PLAYER viene mostrato a video a TUTTI i Player appena si passa a NEXT_TURN
        posChosenCard = Objects.requireNonNullElse(askNum("> Choose which card to place: ", model), - 1);

        int rowCell;
        do {
            rowCell = Objects.requireNonNullElse(askNum("> Which Cell do you want to get?\n\t> Choose row: ", model), -1);
            if (ended) return;
        } while (rowCell > DefaultValue.BookSizeMax || rowCell < DefaultValue.BookSizeMin );

        int columnCell;
        do {
            columnCell = Objects.requireNonNullElse(askNum("> Which Cell do you want to get?\n\t> Choose row: ", model), -1);
            if (ended) return;
        } while (rowCell > DefaultValue.BookSizeMax || rowCell < DefaultValue.BookSizeMin);

        placeCardInBook(posChosenCard, rowCell, columnCell );
    }

    //TODO IMPLEMENTATION
    public void askPickCard (GameImmutable model) {
    }


    }
    @Override
    public void placeCardInBook(int ChosenCard, int rowCell, int columnCell ){
        try {
            clientActions.placeCardInBook(ChosenCard, rowCell, columnCell);
        } catch (IOException e) {
            noConnectionError();
        }
    }


    /* METODI CHE IL SERVER HA RICEVUTO DAL CLIENT */
    /**
     * A player has joined the game
     * @param gameModel game model {@link GameImmutable}
     */
    @Override
    public void playerJoined(GameImmutable gameModel, String nickname) {
        //shared.setLastModelReceived(gameModel);
        events.add(gameModel, EventType.PLAYER_JOINED);

        //Print also here because: If a player is in askReadyToStart is blocked and cannot showPlayerJoined by watching the events
        ui.show_playerJoined(gameModel, nickname);
        ui.addImportantEvent("[EVENT]: Player " + nickname + " joined the game!");

    }

    @Override
    public void playerLeft(GameImmutable gameImmutable, String nickname) throws RemoteException {
        if (gameImmutable.getStatus().equals(GameStatus.WAIT)) {
            ui.show_playerJoined(gameImmutable, nickname);
        } else {
            ui.addImportantEvent("[EVENT]: Player " + nickname + " decided to leave the game!");
        }
        //TODO

    }

    /**
     * A player wanted to join a game but the game is full
     * @param wantedToJoin player that wanted to join
     * @param gameModel game model {@link GameImmutable}
     * @throws RemoteException if the reference could not be accessed
     */
    @Override
    public void joinUnableGameFull(Player wantedToJoin, GameImmutable gameModel) throws RemoteException {
        events.add(null, EventType.GAME_FULL);
    }

    /**
     * A player wanted to join a game but the nickname is already in
     * @param tryToJoin player that wanted to join {@link Player}
     * @throws RemoteException if the reference could not be accessed
     */
    @Override
    public void joinUnableNicknameAlreadyIn(Player tryToJoin) throws RemoteException {
        //System.out.println("[EVENT]: "+ tryToJoin.getNickname() + " has already in");
        events.add(null, EventType.NICKNAME_ALREADY_IN);
    }


    @Override
    public void gameIdNotExists(int gameid) throws RemoteException {
        //TODO
    }

    @Override
    public void genericErrorWhenEnteringGame(String why) throws RemoteException {
        //TODO
    }

    /**
     * Print that a player is ready to start
     * @param model is the game model
     * @param nick is the nickname of the player that is ready to start
     */
    @Override
    public void playerIsReadyToStart(GameImmutable model, String nick) throws IOException {
        ui.show_playerJoined(model, nickname);
    }

    /**
     * The game started
     * @param gameImmutable game model {@link GameImmutable}
     */
    @Override
    public void gameStarted(GameImmutable gameImmutable) throws RemoteException {
        events.add(gameImmutable, EventType.GAME_STARTED);
    }

    @Override
    public void gameEnded(GameImmutable gameImmutable) throws RemoteException {
        events.add(gameImmutable, EventType.GAME_ENDED);
        ended = true;
        ui.show_gameEnded(gameImmutable);
        //TODO quando aggiungiamo la disconnessione fai metodo RESET gioco
    }

    /** Prompts the user to choose between the front or back of the available Initial Cards.
     *   The method continues prompting the user for a valid choice until one is provided.
     * If an invalid choice is made, an error message is displayed and the user is prompted again.
     *  Once a valid choice is made, the selected card side (front or back)
     *  is sent to the server using `clientActions.setInitialCard(index)`.
     *
     * @param model       is the game model
     * @throws IOException
     * @throws FileReadException
     */
//TODO gestire eccezioni
    @Override
    public void requireInitialReady(GameImmutable model) throws IOException, FileReadException {
        ui.show_whichInitialCards();
        Integer index;
        do {
            index = Objects.requireNonNullElse(askNum("\t> Choose the Front or the Back with 0 (Front) or 1 (Back) :", model), -1);
            ui.show_temporaryInitialCards(model);
            if (ended) return;
            if (index < 0 || index >= 2) {
                ui.show_wrongSelectionInitialMsg();
                index = null;
            }
        } while (index == null);
        clientActions.setInitialCard(index); //manda l'indice selezionato per far risalire al Controller la InitialCard selezionata
    }


    /**
     * This method requires the user to choose
     *  between two Objective Cards by displaying them, and then asks the user to select one by entering either 0 or 1.
     *
     *  If the user's selection is invalid, an error message is displayed and the user is prompted again until
     *  a valid choice is made. Once a valid choice is made, the selected card is sent to the server using
     *  clientActions.setGoalCard(index)`.
     * @param model is the game model
     * @throws RemoteException
     */
    @Override
    public void requireGoalsReady(GameImmutable model) throws RemoteException {
        ui.show_whichObjectiveCards();
        Integer index;
        do {
            index = Objects.requireNonNullElse(askNum("\t> Choose one of these  Objective Cards selecting 0 or 1:", model), -1);
            ui.show_ObjectiveCards();
            if (ended) return;
            if (index < 0 || index >= 2) {
                ui.show_wrongSelectionObjectiveMsg();
                index = null;
            }
        } while (index == null);
        try {
            clientActions.setGoalCard(index); //manda l'indice selezionato per far risalire al Controller la ObjectiveCard selezionata
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * A card has been placed on the book
     * @param model is the game model
     * @throws RemoteException
     */
    @Override
    public void cardPlaced(GameImmutable model) throws RemoteException {
        events.add(model, EventType.CARD_PLACED);
    }

    @Override
    public void cardDrawn(GameImmutable model) throws RemoteException {
    //TODO
    }

    @Override
    public void nextTurn(GameImmutable model) throws RemoteException {
    //TODO
    }

    @Override
    public void lastCircle(GameImmutable model) throws RemoteException {
        ui.addImportantEvent("Last circle begin!");
        //TODO
    }



    @Override
    public void playerDisconnected(GameImmutable model, String nick) throws RemoteException {
    //TODO
    }



    /* METODI CHE IL CLIENT RICHIEDE AL SERVER */

    /**
     * The client asks the server to join a specific game
     *
     * @param nick   nickname of the player
     * @param idGame id of the game to join
     */
    @Override
    public void joinGame(String nick) throws IOException, InterruptedException {
        ui.show_joiningToGameMsg(idGame, nick);
        try {
            clientActions.joinGame(nick);
        } catch (IOException | InterruptedException | NotBoundException e) {
            noConnectionError();
        }
    }




    @Override
    public void playerReconnected(GameImmutable model, String nickPlayerReconnected) throws RemoteException {
        //TODO
    }

    @Override
    public boolean isMyTurn() throws RemoteException {
        return false;
        //TODO
    }

    @Override
    public void heartbeat() throws RemoteException {
    //TODO
    }

    @Override
    public void noConnectionError() {
    //TODO
    }
    @Override
    public void reconnect(String nick, int idGame) throws IOException, InterruptedException, NotBoundException {
    //TODO
    }

    @Override
    public void leave(String nick, int GameID) throws IOException, NotBoundException {
        //TODO
    }
}
