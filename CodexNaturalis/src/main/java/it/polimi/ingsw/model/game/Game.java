package it.polimi.ingsw.model.game;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.exceptions.IllegalArgumentException;
import it.polimi.ingsw.exceptions.GameEndedException;
import it.polimi.ingsw.listener.GameListenerInterface;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.CardType;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.Chat.Chat;
import it.polimi.ingsw.Chat.Message;

import it.polimi.ingsw.listener.ListenersHandler;


import java.lang.IllegalStateException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Game model
 *  GameModel is the class that represents the game, it contains all the information about the game, and it's based on a MVC pattern
 *  It manages the game state, including players, cards, decks, and game progression
 *   It also contains the current player that is playing
 *   It also contains the listenersHandler that handles the listeners
 * It implements the singleton pattern to ensure only one instance of the game exists.
 */
public class Game {

	private int gameID;
	private static Game instance;
	private int playersNumber;
	protected ArrayList<Player> players;
	protected ScoreTrack scoretrack;
	private Player currentPlayer;
	private final Deck initialCardsDeck;
	protected Board board;
	protected boolean gameEnded = false;
	protected boolean gameStarted = false;
	private GameStatus status;
	private int[] orderArray;
	private final ArrayList<PlayableCard[]> temporaryInitialCard;
	private final ArrayList<ObjectiveCard[]> temporaryObjectiveCards;


	private final transient ListenersHandler listenersHandler; //transient: non può essere serializzato
	private int currentCardPoints;
	private Chat chat;

	/**
	 * Private Constructor
	 * @param playersNumber The number of players in the game.
	 * @throws IllegalArgumentException If the number of players is not between 1 and 4.
	 */
	public Game(int playersNumber) {
		this.playersNumber = playersNumber;
		this.initialCardsDeck = new Deck(CardType.InitialCard);
		this.players = new ArrayList<>();
		this.scoretrack = new ScoreTrack();
		this.currentPlayer = null;
		this.board = new Board();
		this.orderArray = new int[playersNumber];
		this.status = GameStatus.WAIT;
		this.temporaryInitialCard=new ArrayList<>();
		this.temporaryObjectiveCards= new ArrayList<>();
		this.listenersHandler = new ListenersHandler();

	}
	public Game() {
		this.playersNumber = 0;
		this.initialCardsDeck = new Deck(CardType.InitialCard);
		this.players = new ArrayList<>();
		this.scoretrack = new ScoreTrack();
		this.currentPlayer = null;
		this.board = new Board();
		this.orderArray = new int[playersNumber];
		this.status = GameStatus.WAIT;
		chat = new Chat();
		this.listenersHandler = new ListenersHandler();
		this.temporaryInitialCard=new ArrayList<>();
		this.temporaryObjectiveCards= new ArrayList<>();
	}
	/**
	 * Singleton class
	 * @param playersNumber The number of players in the game
	 * @return the game instance.
	 */
	public static Game getInstance(int playersNumber) {
		try {
			if (instance == null ) {
				instance = new Game(playersNumber);
			}
			return instance;
		} catch (IllegalArgumentException e) {
			System.err.println("Error: " + e.getMessage());
			return null;
		}
	}


	/**
	 * Checks if the game is started.
	 * @return True if the game is started
	 */
	public boolean isGamestarted(){
		return gameStarted;
	}

	/**
	 * Checks if the game is ended.
	 * @return True if the game is ended
	 */
	public boolean isEnded(){
		return gameEnded;
	}
	public ScoreTrack getScoretrack(){
		return this.scoretrack;
	}

	/**
	 * Returns the number of players registered for the game
	 * @return the number of players as an integer.
	 */
	public int getNumPlayers(){
		return this.playersNumber;
	}

	/**
	 * @return the number of player's connected
	 */
	public int getNumOfOnlinePlayers() {
		return players.stream().filter(Player::isConnected).toList().size();
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	/**
	 * return the player who is playing the current turn
	 * @return the current player as a `Player` object	 */
	public Player getCurrentPlayer(){
		return this.currentPlayer;
	}

	/**
	 * @return the game id
	 */
	public int getGameId() {
		return gameID;
	}

	/**
	 * Sets the game id
	 *
	 * @param gameID new game id
	 */
	public void setGameId(int gameID) {
		System.out.println("Model: method setGameId()");
		this.gameID = gameID;
	}

	public void setPlayersNumber(int playersNumber) {
		this.playersNumber = playersNumber;
		System.out.println("Model: method setGameId()");
	}

	/**
	 * * @return the list of listeners
	 * */
	public List<GameListenerInterface> getListeners() {
		return listenersHandler.getListeners();
	}

	/**
	 * @param listener adds the listener to the list
	 * */
	public void addListener(GameListenerInterface listener) {
		listenersHandler.addListener(listener);
	}

	/**
	 * @param listener removes listener from list
	 */
	public void removeListener(GameListenerInterface listener) {
		listenersHandler.removeListener(listener);
	}



	/**
	 * @param player is set as ready, then everyone is notified
	 */
	public void playerIsReadyToStart(Player player) {
		System.out.println("in Game- playerIsReadyToStart");
		player.setReadyToStart();
		//listenersHandler.notify_PlayerIsReadyToStart(this, p.getNickname());
		player.notify_playerReady(this);
	}

	/**
	 * Checks if the game is full or if the provided nickname is already taken.
	 *
	 * @return true if the game is full (i.e., there are already 4 players),
	 */
	public boolean isFull() {
		return playersNumber == DefaultValue.MaxNumOfPlayer;
	}
	/**
	 * @param nickname nickname to check
	 * @return true if already exist
	 */
	public boolean checkNickname(String nickname)  {
		for(Player p : this.players) {
			if(nickname.equals(p.getNickname())) {
				return true;
			}
		}
		return false;
	}
	public void createGame( GameListenerInterface lis, String nickname){
		listenersHandler.notify_requireNumPlayersGameID(lis, this);
	}
	/**
	 * Adds a new player to the game.
	 * after checking if there is space in the match and if the nickname is available
	 * @param nickname the nickname of the player to be added.
	 */
	public void addPlayer(GameListenerInterface lis, String nickname, Color playerColor) {
		System.out.println("Game - addPlayer");
		// Check if the game is not full and the nickname is not taken
		// Check if the game is not full
		if (isFull()) {
			// Game is full
			listenersHandler.notify_JoinUnableGameFull(lis, getPlayerByNickname(nickname), this);
		}

		// Check if the nickname is already taken
		if (checkNickname(nickname)) {
			if(!getPlayerByNickname(nickname).isConnected()){
				System.out.println("Game - addPlayer: sending notify_AskForReconnection ");
				listenersHandler.notify_AskForReconnection(lis, getPlayerByNickname(nickname), this); //chiede se sta provando a riconnettersi
			} else {
				System.out.println("Game - addPlayer: sending notify_JoinUnableNicknameAlreadyIn ");
				listenersHandler.notify_JoinUnableNicknameAlreadyIn(lis, getPlayerByNickname(nickname), this);
			}

		} else{
			// Create a new player with the given nickname
			Player newPlayer = new Player(nickname, playerColor);
			System.out.println("player added: "+nickname+playerColor);
			newPlayer.addListener(lis); //LISTENER DEL SINGOLO PLAYER
			players.add(newPlayer);

			System.out.println("Player " + nickname + " added to the game.");
			System.out.println("Current players: " + players.stream().map(Player::getNickname).collect(Collectors.joining(", ")));

			addListener(lis);

			scoretrack.addPlayer(newPlayer);
			System.out.println("Game: player added to the Scoretrack");


			// Notify listeners that a player has joined the game
			listenersHandler.notify_PlayerJoined(this, nickname, playerColor);
			System.out.println("Game: sent notify_PlayerJoined");
		}

	}


	/**
	 * Chooses a random first player from the array of players, assigns them to currentPlayer,
	 * and saves the order of players for the next turns starting from the first player.
	 */
	//ORDINE DEI GIOCATORI: scelgo a caso il primo, gli altri sONO QUELLI SUCCESSIVI AL PRIMO in ordine di inserimento
	public void chooseOrderPlayers() {

		Random random = new Random();
		int randomIndex = random.nextInt(players.size());
		this.currentPlayer = players.get(randomIndex);

		// Create an array to store the order of players starting from the chosen first player
		int[] orderArray = new int[players.size()];

		// Fill the order array starting from the chosen first player
		for (int i = 0; i < players.size(); i++) {
			orderArray[i] = (randomIndex + i) % players.size();
		}
		// Return the order array
		this.orderArray= orderArray;
	}
	/**
	 * The function removes the player with username "username" from the game.
	 * @param nickname is the nickname of the Player that you want to remove from the game.
	 */
	public void removePlayer (String nickname) {
		players.remove(players.stream().filter(x -> x.getNickname().equals(nickname)).toList().getFirst());
		listenersHandler.notify_PlayerLeft(this, nickname);

		if (this.status.equals(GameStatus.RUNNING) && players.stream().filter(Player::isConnected).toList().size() <= 1) {
			//Not enough players to keep playing
			this.setStatus(GameStatus.ENDED);
		}
	}



	/**
	 * @return true if there are enough players to start, and if every one of them is ready
	 */
	public boolean arePlayersReadyToStartAndEnough() {
		//If every player is ready, the game starts
		int numReady=0;
		for(Player p: players) {
			if (p.getReadyToStart()) {
				numReady++;
			}
		}
		return numReady == playersNumber;
	}
	/**
	 * @return the game status
	 */
	public GameStatus getStatus() {
		return status;
	}
	public int[] getOrderArray(){
		return this.orderArray;
	}
	public void setCurrentPlayer(Player p){
		this.currentPlayer=p;
	}

	public void initializeBoard() {
		board.initializeBoard();
	}

	/**
	 * Sets the initial RUNNING game status
	 * If I want to set the gameStatus to "RUNNING", there needs to be at least
	 * DefaultValue.minNumberOfPlayers -> (2) in lobby, the right number of Cards on the Board and a valid currentPlayer
	 */
	public void setInitialStatus() {
		try {
			if (this.status == GameStatus.WAIT &&
					players.size() == playersNumber &&
					checkBoard() &&
					currentPlayer != null) {

				this.status = GameStatus.RUNNING;
				listenersHandler.notify_GameStarted(this);
			}
		} catch (BoardSetupException e) {
			System.err.println("Error during Board setup: " + e.getMessage());
		}
	}
	public boolean allPlayersHaveChosenGoals() {
		return players.stream().allMatch(player -> player.getGoal() != null);
	}
	/**
	 * Sets the game status in case of ENDED or LAST_CIRCLE
	 * @param status is the status of the game
	 */
	public void setStatus(GameStatus status) {
		this.status = status;

		if (status == GameStatus.ENDED) {
			listenersHandler.notify_GameEnded(this);

		} else if (status == GameStatus.LAST_CIRCLE) {
			listenersHandler.notify_LastCircle(this);
		}
	}
	/**
	 * Check the board for correct setup
	 * @throws BoardSetupException if any of the board setups is incorrect
	 */
	public boolean checkBoard() throws BoardSetupException {
		// Verifying all conditions together
		System.out.println("numero Gold="+ board.getGoldCards().size());
		System.out.println("numero Resource="+ board.getResourceCards().size());
		System.out.println("numero Objective="+ board.getObjectiveCards().length);
		if (!(board.verifyGoldCardsNumber() &&
				board.verifyResourceCardsNumber() &&
				board.verifyObjectiveCardsNumber()) &&
				board.verifyGoldDeckSize(playersNumber) &&
				board.verifyResourceDeckSize(playersNumber) &&
				board.verifyObjectiveDeckSize(playersNumber)) {
			throw new BoardSetupException("Board setup is incorrect");
		}
		// All verifications passed, return true
		return true;
	}


	public void nextTurn(int currentIndex) throws GameEndedException {
		if(currentIndex == playersNumber- 1 && status.equals(GameStatus.LAST_CIRCLE) ){
			throw new GameEndedException();
		}
		else {
			// Calcola l'indice del giocatore successivo
			int nextIndex = (currentIndex + 1) % orderArray.length; //se è l'ultimo riparte dall'inizio
			// Imposta il nuovo currentPlayer
			currentPlayer = players.get(orderArray[nextIndex]);
			listenersHandler.notify_nextTurn(this);
		}
	}

	/**
	 * Checks the personal goal and common goals of the currentPlayer.
	 * This method first checks the personal goal of the current player, and then
	 * verifies the common goals present on the board. If any errors occur during the
	 * checks, such as invalid points or player not found, the exceptions
	 * `InvalidPointsException` and `PlayerNotFoundException` will be thrown.
	 */
	public void lastTurnGoalCheck(){ //
		// Controlla l'obiettivo del giocatore corrente
		checkGoal(currentPlayer.getGoal());

		// Ottieni le carte degli obiettivi dal board e controlla gli obiettivi
		ObjectiveCard[] commonGoals = board.getObjectiveCards();
		for (ObjectiveCard commonGoal : commonGoals) {
			checkGoal(commonGoal);
		}

	}

	//risale al playerbook del giocatore corrente e
	//chiama il metodo checkGoal del giocatore corrente per aggiungere i punti
	public void checkGoal(ObjectiveCard goalToCheck)  {
		Book currentBook = currentPlayer.getPlayerBook();
		int goalPoints = currentBook.checkGoal(goalToCheck);
		scoretrack.addPoints(currentPlayer, goalPoints);
	}


	/**
	 * Initializes the cards for the game.
	 * Each player picks
	 * Distributes initial cards, objective cards, resource cards, and gold cards to each player.
	 * @param lis listener who has to be notified about model update
	 * @param player that is involved in model update
	 * @param index indexPlayer
	 */
	public void initializeCards(GameListenerInterface lis, Player player, int index) {
		try {
			PlayableCard[] initial = initialCardsDeck.returnCard();
			this.temporaryInitialCard.add(initial);
			player.notify_requireInitial(this, index);

			//GOLD CARD E RESOURCE CARD
			for (int i = 0; i < 2; i++) {
				PlayableCard[] newCard= board.takeCardfromBoard(CardType.ResourceCard, true, 0);
				player.getPlayerDeck().addCard(newCard);
			}
			PlayableCard[] newCard= board.takeCardfromBoard(CardType.GoldCard, true, 0);
			player.getPlayerDeck().addCard(newCard);

			//OBJECTIVE CARDS
			ObjectiveCard[] objective= drawObjectiveCards();
			this.temporaryObjectiveCards.add(objective);
			player.notify_requireGoals(this, index); //view richiede le 2 carte obbiettivo da mostrar con il metodo drawObjectiveCards()


		} catch (DeckEmptyException e) {
			System.err.println("Error: deck empty during cards initialization - " + e.getMessage());

		} catch (DeckFullException e) {
            throw new RuntimeException(e);
        }
    }

	public ArrayList<ObjectiveCard[]> getObjectiveCard(){
		return temporaryObjectiveCards;
	}

	/**
	 * Sets the initial card front or back chosen
	 * in the book of the given player
	 * @param player The player for whom the initial card will be set.
	 * @param pos The position of the temporary initial card to choose from.
	 *            This index is used to fetch the card from the `temporaryInitialCard` array.
	 */
	public void setInitialCard(Player player, int pos){
		int indexPlayer=getIndexPlayer(player);
		PlayableCard chosenInitialCard = temporaryInitialCard.get(indexPlayer)[pos];
		Book playerBook= player.getPlayerBook();
		playerBook.addInitial(chosenInitialCard);
	}
	public int getIndexPlayer(Player p){
		for(int i=0; i< playersNumber; i++){
			if(players.get(i).equals(p))
				return i;
		}
		return -1;
	}


	/**
	 * Draws two objective cards from the board's objective card deck.
	 * to be chosen by the player
	 * @return An ArrayList containing two drawn objective cards from the deck.
	 * @throws IllegalStateException If the game has already started and cards cannot be drawn.
	 * @throws DeckEmptyException If the objective card deck is empty.
	 */
	public ObjectiveCard[] drawObjectiveCards() throws IllegalStateException, DeckEmptyException {
		ObjectiveCard[] drawnCards = new ObjectiveCard[2];

		// Controlla che il gioco sia in uno stato valido per pescare carte obiettivo
		if (status.equals(GameStatus.WAIT)) {
			// Pesca due carte obiettivo
			for (int i = 0; i < 2; i++) {
				drawnCards[i]=board.takeObjectiveCard();;
			}
		}
		else
			throw new IllegalStateException("Game already started");
		return drawnCards;
	}
	/**
	 * Assigns the chosen objective card to a specific player.
	 *
	 * @param player The player to whom the chosen objective card will be assigned.
	 * @param index the idenx of the objective card to be assigned to the player.
	 */
	public void setPlayerGoal(Player player, int index) {
		int indexPlayer=getIndexPlayer(player);
		ObjectiveCard chosenObjectiveCard = temporaryObjectiveCards.get(indexPlayer)[index];
		player.setGoal(chosenObjectiveCard);
		player.notify_cardsReady(this);
	}


	/** Determines the winner of the game based on the score thanks to the Board.
	 * @return The player WINNER
	 */
	public Player getWinner() throws NoPlayersException {
		return scoretrack.getWinner();

	}

	/**
	 * Handles the player's turn to place a card on the board.
	 * This method allows a player to place a specified card in a cell on his book.
	 * If the card is successfully placed, it returns the number of points of the card.
	 * or sends notifies to tje listeners to ask to choose another card/cell
	 *
	 * @param p         the player who is placing the card.
	 * @param chosenCard the card chosen by the player to place on the board.
	 * @param rowCell   the row index of the cell in which to place the card.
	 * @param colCell   the column index of the cell in which to place the card.
	 * @return          the number of points gained from placing the card successfully.
	 */
	public int placeCardTurn( Player p, int chosenCard, int rowCell, int colCell)  {
		try {
			currentCardPoints=  p.placeCard(chosenCard, rowCell, colCell);
			//p.notify_CardPlaced(this);
			return currentCardPoints;
		}catch(PlacementConditionViolated | IndexOutOfBoundsException| CellNotAvailableException e   ){
			String msg = e.getMessage();
			p.notify_NotCorrectChosenCard(this, msg);
			return -1;
		}

	}
	public int getCurrentCardPoints(){
		return this.currentCardPoints;

	}
	public void addPoints(Player p, int points) {
		scoretrack.addPoints(p, points);
		listenersHandler.notify_PointsAdded(p.getListeners(), this);
	}


	public void pickCardTurn(Player p, CardType cardType, boolean drawFromDeck, int pos)  {
		try{
			PlayableCard[] newCard = board.takeCardfromBoard(cardType, drawFromDeck, pos);
			if (newCard != null) {
				p.getPlayerDeck().addCard(newCard);
			}
			p.notify_CardDrawn(this);

		}catch (DeckEmptyException e){
			listenersHandler.notify_GameEnded(this);
		} catch (DeckFullException e) {
            throw new RuntimeException(e);
        }

    }



	public Board getBoard(){
		return this.board;
	}


	public ArrayList<PlayableCard[]> getTemporaryInitialCardsDeck() {
		return temporaryInitialCard;
	}
	public ArrayList<ObjectiveCard[]> getTemporaryObjectiveCardsDeck() {return temporaryObjectiveCards;}


	/**
	 * @param playerNickname the nickname of the player to find in the list
	 * @return player by nickname
	 */
	public Player getPlayerByNickname(String playerNickname) {
		if (playerNickname == null) {
			throw new IllegalArgumentException("Nickname cannot be null");
		}
		// Utilizza lo stream per cercare un giocatore con il nickname specificato
		Optional<Player> optionalPlayer = players.stream()
				.filter(player -> player.getNickname().equals(playerNickname))
				.findFirst();

		// Restituisce il giocatore se trovato, altrimenti lancia un'eccezione
		return optionalPlayer.orElseThrow(() -> new NoSuchElementException("Player not found for nickname: " + playerNickname));
	}



	/**
	 * @param nick player to set as disconnected
	 */
	public void setAsDisconnected(String nick) {
		System.out.println("in Game- setAsDisconnected ");
		getPlayerByNickname(nick).setConnected(false);
		getPlayerByNickname(nick).setNotReadyToStart();
		if (getNumOfOnlinePlayers() != 0) {
			listenersHandler.notify_playerDisconnected(this, nick);


			if (getNumOfOnlinePlayers() != 1 && !isTheCurrentPlayerOnline()) {
				try {
					int currentIndex = -1;
					for (int i = 0; i < this.getOrderArray().length; i++) {
						if (this.getPlayers().get(this.getOrderArray()[i]).equals(this.getCurrentPlayer())) {
							currentIndex = i;
							break;
						}
					}
					nextTurn(currentIndex);
				} catch (GameEndedException e) {

				}
			}
			if ((this.status.equals(GameStatus.RUNNING) || this.status.equals(GameStatus.LAST_CIRCLE)) && getNumOfOnlinePlayers() == 1) {
				listenersHandler.notify_onlyOnePlayerConnected(this, DefaultValue.secondsToWaitReconnection);
			}
		}//else the game is empty
	}

	/**
	 * @param p player is reconnected
	 * @throws GameEndedException       the game has ended
	 */
	public boolean reconnectPlayer(Player p) throws GameEndedException {
		Player pIn = players.stream().filter(x -> x.equals(p)).toList().get(0);

		if (!pIn.isConnected()) {
			pIn.setConnected(true);
			listenersHandler.notify_playerReconnected(this, p.getNickname());

			if (!isTheCurrentPlayerOnline()) {
				int currentIndex = -1;
				for (int i = 0; i < this.getOrderArray().length; i++) {
					if (this.getPlayers().get(this.getOrderArray()[i]).equals(this.getCurrentPlayer())) {
						currentIndex = i;
						break;
					}
				}
				nextTurn(currentIndex);
			}
			return true;

		} else {
			System.out.println("ERROR: Trying to reconnect a player not offline!");
			return false;
		}

	}


	/**
	 * @return true if the player in turn is online
	 */
	private boolean isTheCurrentPlayerOnline() {
		return this.getCurrentPlayer().isConnected();
	}

	public Chat getChat(){
		return this.chat;
	}

	public void sentMessage(Message msg){
		Player sender = msg.getSender();
		System.out.println("Game received message from player: " + sender.getNickname());
		System.out.println("Current players in the game: " + players.stream().map(Player::getNickname).collect(Collectors.joining(", ")));


		//controllo se il mittente del messaggio è effettivamente un giocatore che partecipa alla partita
		long count = players.stream().filter(x -> x.equals(sender)).count();
		if (count == 1) {
			chat.addMsg(msg);
			System.out.println("Message added to chat: " + msg.getText());
			listenersHandler.notify_SentMessage(this, chat.getLastMessage());
		} else {
			System.err.println("Player " + sender.getNickname() + " is not in the game.");
			throw new ActionByAPlayerNotInTheGameException();
		}
	}

}


