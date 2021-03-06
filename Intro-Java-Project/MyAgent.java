import java.util.Random;

public class MyAgent extends Agent
{
    Random r;
    int totalColumns = myGame.getColumnCount(); //gets the number of columns in the game
    int totalRows = myGame.getRowCount(); //gets the number rows in the game

    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     * 
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public MyAgent(Connect4Game game, boolean iAmRed)
    {
        super(game, iAmRed);
        r = new Random();
    }

    /**
     * The move method is run every time it is this agent's turn in the game. You may assume that
     * when move() is called, the game has at least one open slot for a token, and the game has not
     * already been won.
     * 
     * By the end of the move method, the agent should have placed one token into the game at some
     * point.
     * 
     * After the move() method is called, the game engine will check to make sure the move was
     * valid. A move might be invalid if:
     * - No token was place into the game.
     * - More than one token was placed into the game.
     * - A previous token was removed from the game.
     * - The color of a previous token was changed.
     * - There are empty spaces below where the token was placed.
     * 
     * If an invalid move is made, the game engine will announce it and the game will be ended.
     * 
     */
    
    public void move()
    {
        //System.out.println(toString() + "Moving..."); //Used for debugging
       int move = canWin(iAmRed);//if I can win then plays winning move. checks this first to make sure winning is priority
        // System.out.println(toString() + ": I can win! Moving at " + move + "."); //Used for debuging
        if (move == -1){//if they can win plays blocking move. checks this next to ensure that blocking is performed.
            move = canWin(!iAmRed);
            //System.out.println(toString() + ": They can win! Moving at " + move + "."); //Used for debuging
            if(move == -1){//if I can make a strategic move and place a third token to attept winning.
                move = canMakeStratMove(!iAmRed);
                //System.out.println(toString() + ": Move in a strategic way to win! Moving at " + move + "."); //Used for debuging
                if(move == -1){// if neither then plays random move
                    move = randomMove();
                    //System.out.println(toString() + ": Move randomly! Moving at " + move + ".");  //Used for debuging
                }
            }
        }
        moveOnColumn(move); //executes command
    }
    
    /**Determines which column and slot should be ether played to win or blocked to prevent them from winning 
     * depending on whether the token is red or yellow.
     * 
     * @param red checks if token is red or not
     * @return the slot that should have a token placed in
     */ 
    public int canWin(boolean red) {
        //System.out.println("  Checking can win..."); //Used for debuging
        
        for(int i = 0; i < totalColumns; i++) {
            //System.out.println("Checking column " + i + "..."); //Used for debuging
            int tei = getTopEmptyIndex(myGame.getColumn(i));
            if(tei > -1) {
                //System.out.println("Column " + i + " has an empty slot at " + tei + "."); //Used for debuging
                if(tei < totalRows - 3) { // if more than 3 tokens might be below this slot
                    //System.out.println("Checking if there's three tokens below..."); //Used for debuging
                    if(myGame.getColumn(i).getSlot(tei+1).getIsRed()==red && myGame.getColumn(i).getSlot(tei+2).getIsRed()==red && myGame.getColumn(i).getSlot(tei+3).getIsRed()==red) {// if a column win is available here
                        return i;
                    }
                }
                if(i < totalColumns - 3 && tei < totalRows - 3) { // if three tokens might be diagnal down to the right
                    //System.out.println("Checking if three tokens might be diagnal down to the right..."); //Used for debuging
                    if(checkIfEqual(red, myGame.getColumn(i+1).getSlot(tei+1), myGame.getColumn(i+3).getSlot(tei+3), myGame.getColumn(i+2).getSlot(tei+2))) {
                        return i;
                    }
                }
                if(i < totalColumns && i > 2 && tei < totalRows && tei > 2) { // if three tokens might be diagnal up to the left
                    //System.out.println("Checking if three tokens might be diagnal up to the left..."); //Used for debuging
                    if(checkIfEqual(red, myGame.getColumn(i-1).getSlot(tei-1), myGame.getColumn(i-2).getSlot(tei-2), myGame.getColumn(i-3).getSlot(tei-3))) {
                        return i;
                    }
                }
                if(i > 2 && i < totalColumns && tei < totalRows - 3 && tei >= 0) { // if three tokens might be diagnal down to the left
                    //System.out.println("Checking if three tokens might be diagnal down to the left..."); //Used for debuging
                    if(checkIfEqual(red, myGame.getColumn(i-1).getSlot(tei+1), myGame.getColumn(i-2).getSlot(tei+2), myGame.getColumn(i-3).getSlot(tei+3))) {
                        return i;
                    }
                }
                if(i >= 0 && i < totalColumns - 3 && tei < totalRows && tei > 2) { // if three tokens might be diagnal up the right
                    //System.out.println("Checking if three tokens might be diagnal up the right..."); //Used for debuging
                    if(checkIfEqual(red, myGame.getColumn(i+3).getSlot(tei-3), myGame.getColumn(i+2).getSlot(tei-2), myGame.getColumn(i+1).getSlot(tei-1))) {
                        return i;
                    }
                }
                if(i < totalColumns - 3) { // if three tokens might be to the right
                    //System.out.println("Checking if there's three tokens to the right..."); //Used for debuging
                    if(checkIfEqual(red, myGame.getColumn(i+1).getSlot(tei), myGame.getColumn(i+2).getSlot(tei), myGame.getColumn(i+3).getSlot(tei))) {
                        return i;
                    }
                }
                if(i > 2) { // if three tokens might be to the left
                    //System.out.println("Checking if there's three tokens to the left..."); //Used for debuging
                    if(checkIfEqual(red, myGame.getColumn(i-1).getSlot(tei), myGame.getColumn(i-3).getSlot(tei), myGame.getColumn(i-2).getSlot(tei))) {
                        return i;
                    }
                }
                if(i < totalColumns - 2 && i > 0) { // if two tokens might be to the right and one to the left
                    //System.out.println("Checking if there's two tokens to the right and one to the left..."); //Used for debuging
                    if(checkIfEqual(red, myGame.getColumn(i-1).getSlot(tei), myGame.getColumn(i+1).getSlot(tei), myGame.getColumn(i+2).getSlot(tei))) {
                        return i;
                    }
                }
                if(i < totalColumns - 1 && i > 1) { // if one token might be to the right and two to the left
                    //System.out.println("Checking if there's one token to the right and two to the left..."); //Used for debuging
                    if(checkIfEqual(red, myGame.getColumn(i-1).getSlot(tei), myGame.getColumn(i+1).getSlot(tei), myGame.getColumn(i-2).getSlot(tei))) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    /**
     * Determines if there are two tokens available to attempt to make a strategic move and win the game
     * 
     * @param red checks if token is red or not
     * @return the slot that should have a token placed in
     */ 
    public int canMakeStratMove(boolean red){
        for(int i = 0; i < totalColumns; i++) {
            //System.out.println("Checking column " + i + " for a strat move"); //Used for debuging
            int tei = getTopEmptyIndex(myGame.getColumn(i));
            if(tei > -1) {
                //System.out.println("Column " + i + " has an empty slot at " + tei + "."); //Used for debuging
                if(tei < totalRows - 2) { // if more than 3 tokens might be below this slot
                    //System.out.println("Checking if there's two tokens below..."); //Used for debuging
                    if(myGame.getColumn(i).getSlot(tei+1).getIsRed()==red && myGame.getColumn(i).getSlot(tei+2).getIsRed()==red) {// checks if two yellow tokens are below
                        return i;
                    }
                }
                if(i < totalColumns - 2 && tei < totalRows - 2) { // if two tokens might be diagnal down to the right
                    //System.out.println("Checking if two tokens might be diagnal down to the right..."); //Used for debuging
                    if(checkIfEqualTwoTokens(red, myGame.getColumn(i+1).getSlot(tei+1), myGame.getColumn(i+2).getSlot(tei+2))) {
                        return i;
                    }
                }
                if(i < totalColumns && i > 1 && tei < totalRows && tei > 1) { // if two tokens might be diagnal up to the left
                    //System.out.println("Checking if two tokens might be diagnal up to the left..."); //Used for debuging
                    if(checkIfEqualTwoTokens(red, myGame.getColumn(i-1).getSlot(tei-1), myGame.getColumn(i-2).getSlot(tei-2))) {
                        return i;
                    }
                }
                if(i > 1 && i < totalColumns && tei < totalRows - 2 && tei >= 0) { // if two tokens might be diagnal down to the left
                    //System.out.println("Checking if two tokens might be diagnal down to the left..."); //Used for debuging
                    if(checkIfEqualTwoTokens(red, myGame.getColumn(i-1).getSlot(tei+1), myGame.getColumn(i-2).getSlot(tei+2))) {
                        return i;
                    }
                }
                if(i >= 0 && i < totalColumns - 2 && tei < totalRows && tei > 1) { // if two tokens might be diagnal up the right
                    //System.out.println("Checking if two tokens might be diagnal up the right..."); //Used for debuging
                    if(checkIfEqualTwoTokens(red, myGame.getColumn(i+2).getSlot(tei-2), myGame.getColumn(i+1).getSlot(tei-1))) {
                        return i;
                    }
                }
                if(i < totalColumns - 2) { // if two tokens might be to the right
                    //System.out.println("Checking if there's two tokens to the right..."); //Used for debuging
                    if(checkIfEqualTwoTokens(red, myGame.getColumn(i+1).getSlot(tei), myGame.getColumn(i+2).getSlot(tei))) {
                        return i;
                    }
                }
                if(i > 2) { // if two tokens might be to the left
                    //System.out.println("Checking if there's two tokens to the left..."); //Used for debuging
                    if(checkIfEqualTwoTokens(red, myGame.getColumn(i-1).getSlot(tei), myGame.getColumn(i-2).getSlot(tei))) {
                        return i;
                    }
                }
              }
            }
        return -1;
    
    }
    /**
     * Determines the top slot that is free of any tokens
     * 
     * @param column the column number that needs to be checked for tokens.
     * @return the row that the empty slot is in. 
     */
    public int getTopEmptyIndex(Connect4Column column) {
        int topEmptySlot=-1;
        for(int i = 0; i<column.getRowCount(); i++) { 
            if(!column.getSlot(i).getIsFilled()) {
                topEmptySlot=i;
            }
        }
        return topEmptySlot;
    }
    /**
     * Checks if the slot has a red token in it.
     *
     * @param isRed determines if the token is red or not
     * @param slot1 location of the first slot to check for tokens
     * @param slot2 location of the second slot to check for tokens
     * @param slot3 location of the third slot to check for tokens
     * @return determines if the token is red or not. 
     */
    public boolean checkIfEqual(boolean isRed, Connect4Slot slot1, Connect4Slot slot2, Connect4Slot slot3) {
        if(slot1.getIsFilled() && slot2.getIsFilled() && slot3.getIsFilled()) { //determines if slot is filled
            if(slot1.getIsRed()==isRed && slot2.getIsRed()==isRed && slot3.getIsRed()==isRed) {// determines if the slot is red or not
                return true;
            }
        }
        return false;
    }
    /**
     * Checks if the slot has a red token in it. Needed only to check for two tokens instead of three.
     *
     * @param isRed determines if the token is red or not
     * @param slot1 location of the first slot to check for tokens
     * @param slot2 location of the second slot to check for tokens
     * @return determines if the token is red or not. 
     */
    public boolean checkIfEqualTwoTokens(boolean isRed, Connect4Slot slot1, Connect4Slot slot2){
        if(slot1.getIsFilled() && slot2.getIsFilled()) { //determines if slot is filled
            if(slot1.getIsRed()==isRed && slot2.getIsRed()==isRed) {// determines if the slot is red or not
                return true;
            }
        }
        return false;
    }


    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     * 
     * @param columnNumber The column into which to drop the token.
     * 
     */
    public void moveOnColumn(int columnNumber)
    {
        int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));   // Find the top empty slot in the column
                                                                                                  // If the column is full, lowestEmptySlot will be -1
        if (lowestEmptySlotIndex > -1)  // if the column is not full
        {
            Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);  // get the slot in this column at this index
            if (iAmRed) // If the current agent is the Red player...
            {
                lowestEmptySlot.addRed(); // Place a red token into the empty slot
            }
            else // If the current agent is the Yellow player (not the Red player)...
            {
                lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
            }
        }
    }

    /**
     * Returns the index of the top empty slot in a particular column.
     * 
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column; -1 if the column is already full.
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++)
        {
            if (!column.getSlot(i).getIsFilled())
            {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }

    /**
     * Returns a random valid move. If your agent doesn't know what to do, making a random move
     * can allow the game to go on anyway.
     * 
     * @return a random valid move.
     */
    public int randomMove()
    {
        int i = r.nextInt(myGame.getColumnCount());
        while (getLowestEmptyIndex(myGame.getColumn(i)) == -1)
        {
            i = r.nextInt(myGame.getColumnCount());
        }
        return i;
    }


    /**
     * Returns the name of this agent.
     *
     * @return the agent's name
     */
    public String getName()
    {
        return "My Agent";
    }
}
