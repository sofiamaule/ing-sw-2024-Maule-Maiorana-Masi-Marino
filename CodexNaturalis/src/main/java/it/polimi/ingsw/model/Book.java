package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.CellNotAvailableException;
import it.polimi.ingsw.exceptions.PlacementConditionViolated;
import it.polimi.ingsw.model.cards.*;

import java.io.Serializable;
import java.util.*;

/**
 * Book Class: contains the disposition of the cards of the player
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 2959887351940112342L;

    /**
     * matrix of cells
     */
    private final Cell[][] bookMatrix;

    /**
     * Map that contains the number of resources in the book, for each possible ResourceType
     */
    private Map<ResourceType, Integer> resourceMap;

    /**
     * Map that contains the number of symbols in the book, for each possible SymbolType
     */
    private Map<SymbolType, Integer> symbolMap;
    private String[][] matrix;
    private int placementOrderBook;


    /**
     * Constructs a Book object with the specified number of rows, columns, and a PlayableCard.
     * Initializes the resource and symbol maps and sets up the book matrix with Cell objects.
     * Each Cell object is initialized with its row and column indices and is marked as unavailable.
     *
     * @param rows    The number of rows in the book.
     * @param columns The number of columns in the book.
     */
    public Book(int rows, int columns){
        initializeMaps();

        this.bookMatrix = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                bookMatrix[i][j] = new Cell(i, j);
            }
        }
        initializePrintMatrix();
        placementOrderBook = -1;
    }

    /**
     * Initializes the matrix used for printing.
     * <p>
     * This method sets up a two-dimensional array of strings with dimensions
     * specified by the constant {@code DefaultValue.BookSizeMax}. Each element
     * of the array is initialized to a single space character (" ").
     * </p>
     * <p>
     * The matrix is a square array with both rows and columns equal to
     * {@code DefaultValue.BookSizeMax}.
     * </p>
     */
    public void initializePrintMatrix(){
        this.matrix = new String[DefaultValue.BookSizeMax][DefaultValue.BookSizeMax];
        for (int i = 0; i < DefaultValue.BookSizeMax; i++) {
            for (int j = 0; j < DefaultValue.BookSizeMax; j++) {
                matrix[i][j] = " ";
            }
        }
    }


    /**
     * Initializes the maps used for tracking resources and symbols.
     * This method sets up two HashMaps: {@code resourceMap} and {@code symbolMap}.
     * The {@code resourceMap} is initialized with keys from the {@code ResourceType}
     * enum (Animal, Fungi, Insect, Plant) and their corresponding values are set to 0.
     * The {@code symbolMap} is initialized with keys from the {@code SymbolType}
     * enum (Ink, Quill, Manuscript) and their corresponding values are set to 0.
     * </p>
     */
    public void initializeMaps(){
        this.resourceMap = new HashMap<>();
        resourceMap.put(ResourceType.Animal,0);
        resourceMap.put(ResourceType.Fungi,0);
        resourceMap.put(ResourceType.Insect,0);
        resourceMap.put(ResourceType.Plant,0);

        this.symbolMap = new HashMap<>();
        symbolMap.put(SymbolType.Ink,0);
        symbolMap.put(SymbolType.Quill,0);
        symbolMap.put(SymbolType.Manuscript,0);
    }


    /**
     * Retrieves a cell from the matrix that matches the specified cell.
     * This method searches through the {@code bookMatrix} for a cell that is
     * equal to the specified {@code findCell}. If a matching cell is found, it
     * is returned. If no matching cell is found, {@code null} is returned.
     * @param findCell the cell to find in the matrix
     * @return the matching cell if found, otherwise {@code null}
     */
    public Cell getCellinMatrix(Cell findCell) {
        for (int row = 0; row < bookMatrix.length; row++) {
            for (int col = 0; col < bookMatrix[row].length; col++) {
                Cell currentCell = bookMatrix[row][col];
                if (currentCell.equals(findCell)) {
                    return currentCell;
                }
            }
        }
        return null;
    }

    /**
     * Adds the initial card to the center of the player's book matrix.
     * This method adds the initial card to the center of the book matrix, which represents the player's book.
     * The book matrix has dimensions of 70x70 cells. The initial card is placed in the center cell (at index [35][35]).
     * After placing the card, the cell's availability is set to false to indicate it's occupied.
     * Then, the resource map is updated based on the resources present on the initial card, both on the corners
     * and in the central part, and the surrounding cells' availability is updated accordingly.
     *
     * @param initialCard The initial card to add to the center of the book matrix.
     */
    private PlayableCard initialCard;
    public PlayableCard getInitialCard(){
        return this.initialCard;
    }

    /**
     * Adds the initial playable card to a specific cell in the matrix.
     * <p>
     * This method places the initial card in the cell located at position (35, 35)
     * in the {@code bookMatrix}. It updates the cell's availability, sets the card
     * pointer, and marks the cell as occupied and as a wall. The method also updates
     * the resource map and the book based on the initial card's properties.
     *
     * @param initialCard the initial playable card to be placed in the matrix
     */
    public void addInitial(PlayableCard initialCard){
        this.initialCard=initialCard;
        Cell initialCell = bookMatrix[35][35];
        initialCell.setAvailable(true);
        initialCell.setCardPointer(initialCard);
        initialCell.setAvailable(false);
        initialCell.setWall(true);
        UpdateMapInitial(initialCard);
        updateBook(initialCard, initialCell);

        placementOrderBook = 0;
        initialCell.setPlacementOrder(placementOrderBook);

    }

    /**
     * Updates the resource map based on the provided initial card.
     * This method evaluates the presence of resources in the initial card, which can have resources
     * both on the corners of the card and in the central part.
     *
     * @param initialCard The initial card to update the resource map with.
     */
    public void UpdateMapInitial(PlayableCard initialCard){
        if(initialCard.getNumCentralResources() != 0) {
            if (!initialCard.getCentralResources().isEmpty()) {
                for (ResourceType resource : initialCard.getCentralResources()) {
                    increaseResource(resource);
                }
            }
        }
        if(initialCard.getNumResources() != 0){
            updateNewCardCorners(initialCard);
        }
    }


    /**
     * Places a resource card onto a cell in the game and returns the points earned by that card.
     *
     * @param resourceCard The resource card to be placed.
     * @param cell         The cell onto which the card will be placed.
     * @return The points earned by placing the card on the cell. Returns 0 if the card has no points.
     */
    public int addResourceCard(PlayableCard resourceCard, Cell cell) throws CellNotAvailableException { //metodo che piazza le carte nel gioco e restituisce i punti di quella carte (se non ha punti restituisce 0)
        int numPoints = 0;

        if(!cell.isAvailable()){
            throw new CellNotAvailableException("This Cell is not Available to Place the Card! Choose another CELL");
        }
        cell.setCardPointer(resourceCard);
        cell.setAvailable(false);
        cell.setWall(true);
        updateMaps(resourceCard, cell);
        updateBook(resourceCard, cell);
        numPoints = resourceCard.getVictoryPoints();
        placementOrderBook++;
        cell.setPlacementOrder(placementOrderBook);

        return numPoints;

    }

    /**
     * Adds a gold card to the specified cell and calculates the number of points gained.
     *
     * @param goldCard The gold card to be added.
     * @param cell The cell where the gold card will be placed.
     * @return The number of victory points gained by adding the gold card.
     * @throws PlacementConditionViolated If the placement condition for the gold card is not met.
     */
    public int addGoldCard(PlayableCard goldCard, Cell cell) throws PlacementConditionViolated, CellNotAvailableException {
        int numPoints;

        if (!checkPlacementCondition(goldCard)) {
            throw new PlacementConditionViolated("You don't have enough resources on the book! Choose another CARD");
        }

        if (!cell.isAvailable()) {
            throw new CellNotAvailableException("This Cell is not Available");
        }
        cell.setCardPointer(goldCard);
        cell.setAvailable(false);
        cell.setWall(true);
        updateMaps(goldCard, cell);
        updateBook(goldCard, cell);
        if (!goldCard.isPointsCondition()) {
            numPoints = goldCard.getVictoryPoints();
        } else {
            numPoints = checkGoldPoints(goldCard, cell);
        }

        placementOrderBook++;
        cell.setPlacementOrder(placementOrderBook);

        return numPoints;
    }

    /**
     * Checks if the placement condition of a given gold card is satisfied based on the available resources.
     *
     * @param goldCard The gold card whose placement condition needs to be checked.
     * @return True if the placement condition is satisfied, false otherwise.
     */
    public boolean checkPlacementCondition(PlayableCard goldCard){
        Map<ResourceType, Integer> conditionsMap = new HashMap<>();
        conditionsMap.put(ResourceType.Animal,0);
        conditionsMap.put(ResourceType.Fungi,0);
        conditionsMap.put(ResourceType.Insect,0);
        conditionsMap.put(ResourceType.Plant,0);
        boolean check = true;
        for (ResourceType resource : goldCard.getPlacementCondition()) {
            conditionsMap.put(resource, conditionsMap.get(resource) + 1);
        }
        for (ResourceType resourceType : conditionsMap.keySet()) {
            int conditionValue = conditionsMap.get(resourceType);
            int resourceValue = resourceMap.getOrDefault(resourceType, 0);
            if (conditionValue > resourceValue) {
                check = false;
            }
        }
        return check;
    }

    /**
     * Checks the points earned by a gold card based on its conditions.
     *
     * @param goldCard The gold card to be checked.
     * @param cell The cell associated with the gold card.
     * @return The number of points earned by the gold card.
     * @see #checkGoldCornerCondition(Cell)
     * @see #checkGoldSymbolCondition(PlayableCard)
     */
    public int checkGoldPoints(PlayableCard goldCard, Cell cell){
        int numPoints;
        if(goldCard.isCornerCondition()){
            numPoints = checkGoldCornerCondition(cell);
        }else{
            numPoints = checkGoldSymbolCondition(goldCard);
        }
        return numPoints;
    }

    /**
     * Calculates the points obtained from a GoldCard with the "cornerCondition" by counting the corners covered.
     * Returns two points for each corner covered by the GoldCard.
     *
     * @param cell The cell containing the GoldCard.
     * @return The points obtained from the GoldCard based on covered corners.
     */
    public int checkGoldCornerCondition(Cell cell){
        int cornerCovered = 0;
        int i = cell.getRow();
        int j = cell.getColumn();

        if(bookMatrix[i-1][j-1].getCard() != null){
            cornerCovered++;
        }
        if(bookMatrix[i-1][j+1].getCard() != null ){
            cornerCovered++;
        }
        if(bookMatrix[i+1][j+1].getCard() != null){
            cornerCovered++;
        }
        if(bookMatrix[i+1][j-1].getCard() != null){
            cornerCovered++;
        }
        return cornerCovered * 2;
    }

    /**
     * Checks the gold symbol condition of a playable gold card.
     *
     * @param goldCard The gold card to be checked.
     * @return The number of occurrences of the gold card's symbol condition in a symbol map.
     */
    public int checkGoldSymbolCondition(PlayableCard goldCard){
        return symbolMap.getOrDefault(goldCard.getSymbolCondition(), 0);
    }

    /**
     * Adds a playable card to a cell and returns the number of points earned.
     *
     * @param card The playable card to be added.
     * @param cell The cell to which the card will be added.
     * @return The number of points earned by adding the card to the cell.
     */
    public int addCard(PlayableCard card, Cell cell) throws PlacementConditionViolated, CellNotAvailableException {
        int numPoints = 0;
        switch (card.getCardType()){
            case GoldCard -> numPoints = addGoldCard(card, cell);
            case ResourceCard -> numPoints = addResourceCard(card, cell);
        }
        return numPoints;
    }

    /**
     * Updates the covered corners of the neighboring cards of the specified cell.
     * If a neighboring cell contains a card and its corner is not empty,
     * the corresponding resource or symbol in the book's resource/symbol map is decremented.
     *
     * @param cell The cell for which to update the covered corners of neighboring cards.
     * @return True if any corner was covered, false otherwise.
     */
    public boolean updateCoveredCorners(Cell cell){
        boolean cornerCovered = false;
        boolean skip0 = false;
        boolean skip1 = false;
        boolean skip2 = false;
        boolean skip3 = false;
        if(cell.getCard()!=null){
            int i = cell.getRow();
            int j = cell.getColumn();
            if(i==0){
                skip0 = true;
                skip1 = true;
            }
            if(j==0){
                skip0 = true;
                skip3 = true;
            }
            if(i==bookMatrix.length-1){
                skip2 = true;
                skip3 = true;
            }
            if(j==bookMatrix.length-1){
                skip1 = true;
                skip2 = true;
            }
            if(!skip0 && bookMatrix[i-1][j-1].getCard() != null && !(bookMatrix[i-1][j-1].getCard().getCornerContent(2).equals("Empty"))){
                coverCorner(bookMatrix[i-1][j-1].getCard(),2);
            }
            if(!skip1 && bookMatrix[i-1][j+1].getCard() != null && !(bookMatrix[i-1][j+1].getCard().getCornerContent(3).equals("Empty"))){
                coverCorner(bookMatrix[i-1][j+1].getCard(), 3);
            }
            if(!skip2 && bookMatrix[i+1][j+1].getCard() != null && !(bookMatrix[i+1][j+1].getCard().getCornerContent(0).equals("Empty"))){
                coverCorner(bookMatrix[i+1][j+1].getCard(), 0);
            }
            if(!skip3 && bookMatrix[i+1][j-1].getCard() != null && !(bookMatrix[i+1][j-1].getCard().getCornerContent(1).equals("Empty"))){
                coverCorner(bookMatrix[i+1][j-1].getCard(), 1);
            }
        }
        return cornerCovered;
    }

    /**
     * Covers the resource or symbol of a specified corner on a PlayableCard.
     * Decreases the quantity of the corresponding resource or symbol in the book's resource/symbol map.
     *
     * @param card    The PlayableCard for which the corner is to be covered.
     * @param corner  The type of corner to cover (0: TLCorner, 1: TRCorner, 2: BRCorner, 3: BLCorner).
     */
    public void coverCorner(PlayableCard card, int corner){ //funzione che "copre" la risorsa o il simbolo di una carta passata la carta e l'angolo coperto. decrementa il valore della risorsa/simbolo nella mappa dei simboli risorse del book
        if((card.getCornerContent(corner).equals("Fungi")) || (card.getCornerContent(corner).equals("Animal")) || (card.getCornerContent(corner).equals("Insect")) || (card.getCornerContent(corner).equals("Plant"))){
            String content = card.getCornerContent(corner);
            switch (content){
                case("Fungi"): {
                    decreaseResource(ResourceType.Fungi);
                    break;
                }
                case("Animal"):{
                    decreaseResource(ResourceType.Animal);
                    break;
                }
                case("Insect"):{
                    decreaseResource(ResourceType.Insect);
                    break;
                }
                case("Plant"):{
                    decreaseResource(ResourceType.Plant);
                    break;
                }
                default: break;
            }
        }else if((card.getCornerContent(corner).equals("Ink")) || (card.getCornerContent(corner).equals("Quill") || (card.getCornerContent(corner).equals("Manuscript")))){
            String content = card.getCornerContent(corner);
            switch (content) {
                case ("Ink"): {
                    decreaseSymbol(SymbolType.Ink);
                    break;
                }
                case ("Quill"): {
                    decreaseSymbol(SymbolType.Quill);
                    break;
                }
                case ("Manuscript"): {
                    decreaseSymbol(SymbolType.Manuscript);
                    break;
                }
                default: break;
            }
        }
    }

    /**
     * Decreases the quantity of the specified type of resource by one.
     *
     * @param resourceType The type of resource to decrease.
     */
    public void decreaseResource(ResourceType resourceType){ //funzione che decrementa la risorsa passata per parametro
        int numResources = resourceMap.get(resourceType);
        numResources = (numResources == 0) ? 0 : numResources - 1;
        resourceMap.put(resourceType, numResources);
    }

    /**
     * Decreases the quantity of the specified type of symbol by one.
     *
     * @param symbolType The type of symbol to decrease.
     */
    public void decreaseSymbol(SymbolType symbolType){ //funzione che decrementa la risorsa passata per parametro
        int numSymbols = symbolMap.get(symbolType);
        numSymbols = (numSymbols == 0) ? 0 : numSymbols - 1;
        symbolMap.put(symbolType, numSymbols);
    }

    /**
     * Increases the quantity of the specified type of resource by one.
     *
     * @param resourceType The type of resource to increase.
     */
    public void increaseResource(ResourceType resourceType){ //funzione che incrementa la risorsa passata per parametro
        int numResources = resourceMap.get(resourceType);
        numResources++;
        resourceMap.put(resourceType, numResources);
    }

    /**
     * Increases the quantity of the specified type of symbol by one.
     *
     * @param symbolType The type of symbol to increase.
     */
    public void increaseSymbol(SymbolType symbolType){ //funzione che incrementa il simbolo passato per parametro
        int numSymbols = symbolMap.get(symbolType);
        numSymbols++;
        symbolMap.put(symbolType, numSymbols);
    }

    /**
     * Updates the symbol map and the resource map when adding a new card to the book.
     * This method updates the symbol map and the resource map by calling the
     * {@link #updateNewCardCorners(PlayableCard)} method to incorporate symbols and/or resources
     * from the newly added card, and the {@link #updateCoveredCorners(Cell)} method to adjust
     * the resource and symbol counts based on the neighboring cards of the specified cell.
     *
     * @param card The PlayableCard to add to the book.
     * @param cell The cell where the card is placed.
     */
    public void updateMaps(PlayableCard card, Cell cell){
        updateNewCardCorners(card);
        updateCoveredCorners(cell);
    }

    /**
     * Updates the resource and symbol maps with the symbols and/or resources present on a card
     * when it is placed in the book.
     *
     * @param card The PlayableCard to update the resource and symbol maps with.
     */
    public void updateNewCardCorners(PlayableCard card) {
        for (int i = 0; i < 4; i++) {
            String content = card.getCornerContent(i);
            switch (content) {
                case "Fungi":
                    increaseResource(ResourceType.Fungi);
                    break;
                case "Animal":
                    increaseResource(ResourceType.Animal);
                    break;
                case "Insect":
                    increaseResource(ResourceType.Insect);
                    break;
                case "Plant":
                    increaseResource(ResourceType.Plant);
                    break;
                case "Ink":
                    increaseSymbol(SymbolType.Ink);
                    break;
                case "Quill":
                    increaseSymbol(SymbolType.Quill);
                    break;
                case "Manuscript":
                    increaseSymbol(SymbolType.Manuscript);
                    break;
                default:
                    // Handle the case when the content is not recognized or empty
                    break;
            }
        }
    }


    /**
     * Removes all cards from the book.
     * This method clears the book of all cards, leaving it empty.
     */
    public void clear(){
        for (int i = 0; i < bookMatrix.length; i++) {
            for (int j = 0; j < bookMatrix[i].length; j++) {
                bookMatrix[i][j].setCardPointer(null);
                bookMatrix[i][j].setAvailable(false);
                bookMatrix[i][j].setWall(false);
            }
        }
        initializeMaps();
    }


    /**
     * Updates the availability of cells surrounding the newly placed card.
     * This method adjusts the availability of cells surrounding the newly placed card
     * based on the presence of corners on the card. If a corner exists on the card and
     * the adjacent diagonal cell does not have its wall attribute set to true,
     * it sets the adjacent diagonal cell to available. If the corner does not exist (NoCorner),
     * the availability of the cell remains false, and the cell's wall attribute is set to true,
     * indicating that no further cards can be placed in that cell in the future.
     *
     * @param newCard The PlayableCard that has been placed.
     * @param cell The Cell where the card is placed.
     */
    public void updateBook(PlayableCard newCard, Cell cell){
        int i = cell.getRow();
        int j = cell.getColumn();
        boolean skip0 = false;
        boolean skip1 = false;
        boolean skip2 = false;
        boolean skip3 = false;
        if(i==0){
            skip0 = true;
            skip1 = true;
        }
        if(j==0){
            skip0 = true;
            skip3 = true;
        }
        if(i==bookMatrix.length-1){
            skip2 = true;
            skip3 = true;
        }
        if(j==bookMatrix.length-1){
            skip1 = true;
            skip2 = true;
        }
        if(!skip0 && newCard.getTLCorner() == CornerLabel.NoCorner){
            bookMatrix[i-1][j-1].setWall(true);
        }else if(!skip0 && !(newCard.getTLCorner() == CornerLabel.NoCorner) && !(bookMatrix[i-1][j-1].isWall())){
            bookMatrix[i-1][j-1].setAvailable(true);
        }

        if(!skip1 && newCard.getTRCorner() == CornerLabel.NoCorner){
            bookMatrix[i-1][j+1].setWall(true);
        }else if(!skip1 && !(newCard.getTRCorner() == CornerLabel.NoCorner) && !(bookMatrix[i-1][j+1].isWall())){
            bookMatrix[i-1][j+1].setAvailable(true);
        }

        if(!skip2 && newCard.getBRCorner() == CornerLabel.NoCorner){
            bookMatrix[i+1][j+1].setWall(true);
        }else if(!skip2 && !(newCard.getBRCorner() == CornerLabel.NoCorner) && !(bookMatrix[i+1][j+1].isWall())){
            bookMatrix[i+1][j+1].setAvailable(true);
        }

        if(!skip3 && newCard.getBLCorner() == CornerLabel.NoCorner){
            bookMatrix[i+1][j-1].setWall(true);
        }else if(!skip3 && !(newCard.getBLCorner() == CornerLabel.NoCorner) && !(bookMatrix[i+1][j-1]).isWall()){
            bookMatrix[i+1][j-1].setAvailable(true);
        }
    }

    /**
     * Returns an array of available cells in the book matrix.
     * A cell is considered available if its 'isAvailable' attribute is set to true.
     *
     * @return An array of Cell objects representing the available cells in the book matrix.
     */
    public ArrayList<Cell> showAvailableCells() {
        ArrayList<Cell> availableCellsList = new ArrayList<>();
        for (int i = 0; i < bookMatrix.length; i++) {
            for (int j = 0; j < bookMatrix.length; j++) {
                if (bookMatrix[i][j].isAvailable()){
                    availableCellsList.add(bookMatrix[i][j]);
                }
            }
        }
        return availableCellsList;
    }

    public Map<ResourceType, Integer> getResourceMap() {
        return resourceMap;
    }

    public void setResourceMap(Map<ResourceType, Integer> resourceMap) {
        this.resourceMap = resourceMap;
    }

    public Map<SymbolType, Integer> getSymbolMap() {
        return symbolMap;
    }

    public void setSymbolMap(Map<SymbolType, Integer> symbolMap) {
        this.symbolMap = symbolMap;
    }

    /**
     * This method checks on the player's Book how many times he achieved his own Goal.
     * It also calculates how many points the player obtained achieving his Goal.
     *
     * @param objectiveCard Is the player's own ObjectiveCard.
     * @throws IllegalArgumentException If an invalid GoalType label is set on the objectiveCard attribute.
     * @return Victory Points obtained by the player reaching the goal required by his Objective card.
     */
    public int checkGoal(ObjectiveCard objectiveCard) {
        return switch (objectiveCard.getGoalType()) {
            case ResourceCondition -> checkResourceCondition(objectiveCard);
            case SymbolCondition -> checkSymbolCondition(objectiveCard);
            case DiagonalPlacement -> checkDiagonalPlacement(objectiveCard);
            case LPlacement -> checkLPlacement(objectiveCard);
        };
    }


    /**
     * @return Victory Points obtained by the player reaching the Resource condition required by his Objective card.
     * @param objectiveCard The player's own ObjectiveCard.
     */
    public int checkResourceCondition(ObjectiveCard objectiveCard) {

        ResourceType mainResourceType = objectiveCard.getMainResource(); //ResourceType required by the card
        int numMainResources = resourceMap.getOrDefault(mainResourceType, 0);
        int numTriplets = numMainResources / 3; //Groups of 3 required Resource on the player's Book
        return numTriplets * objectiveCard.getVictoryPoints();

    }

    /**
     * @return Victory Points obtained by the player reaching the Symbol condition required by his Objective card.
     * @param objectiveCard The player's own ObjectiveCard.
     */
    public int checkSymbolCondition(ObjectiveCard objectiveCard) {

        switch (objectiveCard.getVictoryPoints()) {
            case 2:
                SymbolType symbolToCheck = objectiveCard.getSymbols().get(0);
                int numSymbol = symbolMap.getOrDefault(symbolToCheck, 0);
                int numPairs = numSymbol / 2;
                return numPairs * 2;
            case 3:
                int numQuill = symbolMap.getOrDefault(SymbolType.Quill, 0);
                int numInk = symbolMap.getOrDefault(SymbolType.Ink, 0);
                int numManuscript = symbolMap.getOrDefault(SymbolType.Manuscript, 0);
                int minSymbolCount = Math.min(numQuill, Math.min(numInk, numManuscript)); //gets the MINIMUM of the 3 symbols quantities
                return minSymbolCount * 3;

        }
        throw new IllegalArgumentException("Invalid victoryPoints");
    }

        /**
         * @return Victory Points obtained by the player reaching the diagonalPlacement condition required by his Objective card.
         * @param objectiveCard The player's own ObjectiveCard.
         */
        public int checkDiagonalPlacement(ObjectiveCard objectiveCard) {
            int[][] indexes = new int[70][70];
            //initialization of the matrix
            for (int i = 0; i < indexes.length; i++) {
                for (int j = 0; j < indexes[i].length; j++) {
                    indexes[i][j] = 0;
                }
            }
            int count = 0;
            ResourceType mainResource = objectiveCard.getMainResource();
            CornerType direction = objectiveCard.getDirection();

            int rows = bookMatrix.length;
            int columns = bookMatrix[0].length;

            if (direction == CornerType.BRCorner) {
                for (int i = 0; i < rows - 2; i++) {
                    for (int j = 0; j < columns - 2; j++) {
                        if (!skipIndexes(indexes,i,j) && bookMatrix[i][j].getCard() != null &&
                                bookMatrix[i + 1][j + 1].getCard() != null &&
                                bookMatrix[i + 2][j + 2].getCard() != null &&
                                bookMatrix[i][j].getCard().getMainResource() == mainResource &&
                                bookMatrix[i + 1][j + 1].getCard().getMainResource() == mainResource &&
                                bookMatrix[i + 2][j + 2].getCard().getMainResource() == mainResource) {
                            count++;
                            indexes[i][j] = 1;
                            indexes[i+1][j+1] = 1;
                            indexes[i+2][j+2] = 1;
                        }
                    }
                }
            } else if (direction == CornerType.BLCorner) {
                for (int i = 0; i < rows - 2; i++) {
                    for (int j = 2; j < columns; j++) {
                        if (!skipIndexes(indexes,i,j) && bookMatrix[i][j].getCard() != null &&
                                bookMatrix[i + 1][j - 1].getCard() != null &&
                                bookMatrix[i + 2][j - 2].getCard() != null &&
                                bookMatrix[i][j].getCard().getMainResource() == mainResource &&
                                bookMatrix[i + 1][j - 1].getCard().getMainResource() == mainResource &&
                                bookMatrix[i + 2][j - 2].getCard().getMainResource() == mainResource) {
                            count++;
                            indexes[i][j] = 1;
                            indexes[i+1][j-1] = 1;
                            indexes[i+2][j-2] = 1;
                        }
                    }
                }
            }
            return count * 2;
        }

    /**
     * Determines whether to skip the specified indexes based on their value.
     * <p>
     * This method checks the value at the specified indexes in the given 2D array.
     * If the value is 1, the method returns {@code true}, indicating that the
     * indexes should be skipped. Otherwise, it returns {@code false}.
     * This function is used in other methods of the class to avoid going out of
     * the bounds of the {@code bookMatrix}.
     * </p>
     *
     * @param indexes the 2D array of indexes to check
     * @param i the row index to check
     * @param j the column index to check
     * @return {@code true} if the value at the specified indexes is 1, otherwise {@code false}
     */
        public boolean skipIndexes(int[][] indexes,int i, int j){
            boolean skip;
            if(indexes[i][j] == 1){
                skip = true;
            }else{
                skip=false;
            }
            return skip;
        }




        /**
         * @return Victory Points obtained by the player reaching the LPlacement condition required by his Objective card.
         * @param objectiveCard The player's own ObjectiveCard.
         */
        public int checkLPlacement(ObjectiveCard objectiveCard) {
            int[][] indexes = new int[70][70];
            for (int i = 0; i < indexes.length; i++) {
                for (int j = 0; j < indexes[i].length; j++) {
                    indexes[i][j] = 0;
                }
            }
            int count = 0;
            ResourceType mainResource = objectiveCard.getMainResource();
            ResourceType secondResource = objectiveCard.getSecondResource();

            switch (objectiveCard.getDirection()) {
                case TLCorner:
                    for (int i = 2; i < bookMatrix.length; i++) {
                        for (int j = 1; j < bookMatrix[i].length - 1; j++) {
                            if (!skipIndexes(indexes, i, j) && bookMatrix[i][j].getCard() != null && bookMatrix[i][j].getCard().getMainResource() == secondResource) {
                                System.out.println("entrato fase 0 TLCorner");
                                if (bookMatrix[i - 1][j - 1].getCard() != null && bookMatrix[i - 3][j - 1].getCard() != null &&
                                        bookMatrix[i - 1][j - 1].getCard().getMainResource() == mainResource &&
                                        bookMatrix[i - 3][j - 1].getCard().getMainResource() == mainResource) {
                                    count++;
                                    indexes[i][j] = 1;
                                    indexes[i-1][j-1] = 1;
                                    indexes[i-3][j-1] = 1;
                                    System.out.println("entratoTLCorner");
                                }
                            }
                        }
                    }
                    break;
                case TRCorner:
                    for (int i = 3; i < bookMatrix.length; i++) {
                        for (int j = 0; j < bookMatrix[i].length - 2; j++) {
                            if (!skipIndexes(indexes, i, j) && bookMatrix[i][j].getCard() != null && bookMatrix[i][j].getCard().getMainResource() == secondResource) {
                                if (bookMatrix[i - 1][j + 1].getCard() != null && bookMatrix[i - 3][j + 1].getCard() != null &&
                                        bookMatrix[i - 1][j + 1].getCard().getMainResource() == mainResource &&
                                        bookMatrix[i - 3][j + 1].getCard().getMainResource() == mainResource) {
                                    count++;
                                    indexes[i][j] = 1;
                                    indexes[i-1][j+1] = 1;
                                    indexes[i-3][j+1] = 1;
                                }
                            }
                        }
                    }
                    break;
                case BLCorner:
                    for (int i = 0; i < bookMatrix.length - 2; i++) {
                        for (int j = 1; j < bookMatrix[i].length - 1; j++) {
                            if (!skipIndexes(indexes, i, j) && bookMatrix[i][j].getCard() != null && bookMatrix[i][j].getCard().getMainResource() == secondResource) {
                                if (bookMatrix[i + 1][j - 1].getCard() != null && bookMatrix[i + 3][j - 1].getCard() != null &&
                                        bookMatrix[i + 1][j - 1].getCard().getMainResource() == mainResource &&
                                        bookMatrix[i + 3][j - 1].getCard().getMainResource() == mainResource) {
                                    count++;
                                    indexes[i][j] = 1;
                                    indexes[i+1][j-1] = 1;
                                    indexes[i+3][j-1] = 1;
                                }
                            }
                        }
                    }
                    break;
                case BRCorner:
                    for (int i = 0; i < bookMatrix.length - 2; i++) {
                        for (int j = 0; j < bookMatrix[i].length - 2; j++) {
                            if (!skipIndexes(indexes, i, j) && bookMatrix[i][j].getCard() != null && bookMatrix[i][j].getCard().getMainResource() == secondResource) {
                                if (bookMatrix[i + 1][j + 1].getCard() != null && bookMatrix[i + 3][j + 1].getCard() != null &&
                                        bookMatrix[i + 1][j + 1].getCard().getMainResource() == mainResource &&
                                        bookMatrix[i + 3][j + 1].getCard().getMainResource() == mainResource) {
                                    count++;
                                    indexes[i][j] = 1;
                                    indexes[i+1][j+1] = 1;
                                    indexes[i+3][j+1] = 1;
                                }
                            }
                        }
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid direction");
            }

            return count*3;
        }

    public Cell[][] getBookMatrix() {
        return bookMatrix;
    }

    /**
     * Displays the current state of the resource and symbol maps.
     * <p>
     * This method iterates through the {@code resourceMap} and {@code symbolMap},
     * building a string representation of each map. The resulting string shows
     * the names and quantities of each resource and symbol present in the game.
     * </p>
     *
     * @return a string representation of the resource and symbol maps
     */
    public String showMaps(){
        StringBuilder result = new StringBuilder();
        result.append("*******RESOURCEMAP*******: \n");
        for (Map.Entry<ResourceType, Integer> entry : resourceMap.entrySet()) {
            ResourceType resource = entry.getKey();
            int num = entry.getValue();
            result.append(resource.toString()).append(": ").append(num).append("\n");
        }

        result.append("*******SYMBOLMAP*******: \n");
        for (Map.Entry<SymbolType, Integer> entry : symbolMap.entrySet()) {
            SymbolType symbol = entry.getKey();
            int num = entry.getValue();
            result.append(symbol.toString()).append(": ").append(num).append("\n");
        }
        return result.toString();
    }

    /**
     * Generates a string representation of the book matrix.
     * <p>
     * This method constructs a detailed string representation of the book matrix, including
     * row and column indices. It highlights available cells in green and includes the contents
     * of each cell, whether it contains a card or is empty. The method also appends the
     * current state of the resource and symbol maps at the end.
     * </p>
     * <p>
     * The matrix display includes:
     * <ul>
     * <li>Row and column indices</li>
     * <li>Cell contents or placeholders for empty cells</li>
     * <li>Green highlighting for available cells</li>
     * <li>Resource and symbol maps at the bottom</li>
     * </ul>
     * </p>
     *
     * @return a string representation of the book matrix and the resource/symbol maps
     */
  @Override
  public String toString() {
      final String GREEN = "\033[0;32m";
      final String RESET = "\033[m";

      StringBuilder result = new StringBuilder();
      int[] limits = findSubMatrix();

      int minI = limits[0];
      int minJ = limits[1];
      int maxI = limits[2];
      int maxJ = limits[3];

      result.append("********************BOOK********************\n");

      int maxWidth = DefaultValue.printLenght + 2;

      result.append("     ");
      for (int j = minJ; j <= maxJ; j++) {
          result.append(String.format("%-" + maxWidth + "d", j));
      }
      result.append("\n");

      ArrayList<ArrayList<String>> rows = new ArrayList<>();

      for (int i = minI; i <= maxI; i++) {
          ArrayList<StringBuilder> rowBuilders = new ArrayList<>();
          for (int k = 0; k < DefaultValue.printHeight + 2; k++) {
              rowBuilders.add(new StringBuilder());
          }

          for (int j = minJ; j <= maxJ; j++) {
              String[] lines;
              if (bookMatrix[i][j].getCard() != null) {
                  lines = bookMatrix[i][j].getCard().toString().split("\n");
              } else if (bookMatrix[i][j].isAvailable()) {
                  lines = nullCardPrint().split("\n");
              } else {
                  lines = new String[DefaultValue.printHeight + 2];
                  Arrays.fill(lines, " ".repeat(maxWidth));
              }


              boolean isAvailable = bookMatrix[i][j].isAvailable();
              String colorCode = isAvailable ? GREEN : RESET;

              for (int k = 0; k < lines.length; k++) {
                  if (k < rowBuilders.size()) {
                      int spacesToAdd = Math.max(0, maxWidth - lines[k].length());
                      rowBuilders.get(k).append(colorCode).append(lines[k]).append(" ".repeat(spacesToAdd)).append(RESET);
                  }
              }
          }

          ArrayList<String> rowStrings = new ArrayList<>();
          for (StringBuilder sb : rowBuilders) {
              rowStrings.add(sb.toString());
          }
          rows.add(rowStrings);
      }

      for (int i = 0; i < rows.size(); i++) {
          for (int k = 0; k < rows.get(i).size(); k++) {
              if (k == 0) {
                  result.append(String.format("%-4d", minI + i)).append(rows.get(i).get(k)).append("\n");
              } else {
                  result.append("    ").append(rows.get(i).get(k)).append("\n");
              }
          }
      }

      result.append("\n");
      result.append(showMaps());
      result.append("\n");

      return result.toString();
  }

    /**
     * Generates a string representation of an empty card.
     * <p>
     * This method constructs a string that represents an empty card with a specified
     * width and height, enclosed within a border. The interior of the card is filled
     * with spaces.
     * </p>
     *
     * @return a string representation of an empty card
     */
    public String nullCardPrint() {
        StringBuilder result = new StringBuilder();
        int maxWidth = DefaultValue.printLenght;

        String borderLine = "+" + "-".repeat(maxWidth) + "+";
        result.append(borderLine).append("\n");

        for (int i = 0; i < DefaultValue.printHeight; i++) {
            result.append("| ");
            result.append(" ".repeat(maxWidth - 2));
            result.append(" |\n");
        }
        result.append(borderLine).append("\n");
        return result.toString();
    }

    /**
     * Determines the smallest submatrix that contains all the cards in the book matrix.
     * <p>
     * This method searches through the {@code bookMatrix} to find the smallest rectangular
     * submatrix that includes all cells containing cards. It returns the limits of this
     * submatrix, including a border of one cell around the cards if possible.
     * </p>
     *
     * @return an array of four integers representing the limits of the submatrix:
     *         - limits[0]: minimum row index (inclusive)
     *         - limits[1]: minimum column index (inclusive)
     *         - limits[2]: maximum row index (inclusive)
     *         - limits[3]: maximum column index (inclusive)
     */
    public int[] findSubMatrix() {
        int[] limits = new int[4];
        int minI = bookMatrix.length;
        int minJ = bookMatrix[0].length;
        int maxI = -1;
        int maxJ = -1;

        for (int i = 0; i < bookMatrix.length; i++) {
            for (int j = 0; j < bookMatrix[i].length; j++) {
                if (bookMatrix[i][j].getCard() != null) {
                    if (i < minI) minI = i;
                    if (j < minJ) minJ = j;
                    if (i > maxI) maxI = i;
                    if (j > maxJ) maxJ = j;
                }
            }
        }

        if (minI > 0) minI--;
        if (minJ > 0) minJ--;
        if (maxI < bookMatrix.length - 1) maxI++;
        if (maxJ < bookMatrix[0].length - 1) maxJ++;

        limits[0] = minI;
        limits[1] = minJ;
        limits[2] = maxI;
        limits[3] = maxJ;

        return limits;
    }
}
