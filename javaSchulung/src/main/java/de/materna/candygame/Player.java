package de.materna.candygame;

import de.materna.candygame.enums.Candy;
import de.materna.candygame.enums.City;

/**
 * Player Class contain the items and money of the player which are not stored.
 * Provide functions to manipulate, get the values and to travel
 */
public class Player {
    private int balance;
    private City currentCity;
    private boolean isBroke;
    private final int number;
    private boolean playerReady;
    /**
     * Return the amount of candys the player have on this person
     * @param itemID The ID of the Candy
     * @return  The amount of candy as int
     * @see Candy
     */
    public int getItem(int itemID) {
        if (itemID >= 0 && itemID < items.length) {
            return items[itemID];
        } else {
            throw new ArrayIndexOutOfBoundsException("itemID is out of bounds [0," + items.length + ") itemID: " + itemID);
        }
    }

    private int[] items = new int[Candy.values().length];
    private int leftSpace = 100;

    /**
     * Constructor of the Player
     * @param balance How much money the player starts with.
     * @param currentCity The starting city for the player
     * @see City
     */
    public Player(int number, int balance, City currentCity) {
        this.balance = balance;
        this.currentCity = currentCity;
        this.isBroke=false;
        this.number=number;
        this.playerReady =false;
    }

    /**
     * Returns the money balance of the player.
     * @return the balance of the player
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Returns the city in which the player currently is.
     * @return the current city
     * @see City
     */
    public City getCurrentCityENUM() {
        return currentCity;
    }

    /**
     * Adding money to the player
     * @param amount of money to add.
     */
    public void addMoney(int amount) {
        this.balance = this.balance + amount;

    }

    /**
     * Returns a TaskCompletionState
     *
     * @param amount of money to reduce
     * @return TaskCompletionState
     * @see TaskCompletionState
     */
    public TaskCompletionState reduceMoney(int amount) {
        if (this.balance >= amount) {
            this.balance = this.balance - amount;
            return new TaskCompletionState("",true);
        } else {
            return new TaskCompletionState("You dont have enough money.", false);
        }
    }

    /**
     * This method reduce the player money if he has enough on hand and change the current city of the player to the
     * destination target on success.
     * @param destination the city the player want to travel to
     * @param cost the travel costs
     * @return TaskCompletionState
     * @see TaskCompletionState
     */
    public TaskCompletionState travel(City destination, int cost) {
        if(currentCity.equals(destination)){
            return new TaskCompletionState("You are already in that city.",false);
        }
        TaskCompletionState state=this.reduceMoney(cost);
        if (state.isSuccess) {
            this.currentCity = destination;
        }
        return state;
    }

    /**
     * Add a specific amount of candy to the player.
     * Success if the player have enough space left
     * @param candy Kind of Candy to add
     * @param amount of candy to add
     * @return TaskCompletionState
     * @see TaskCompletionState
     */
    public TaskCompletionState addItem(Candy candy, int amount) {
        if(leftSpace - amount>=0) {
            leftSpace = leftSpace - amount;
            items[candy.getID()] = items[candy.getID()] + amount;
            return new TaskCompletionState("",true);
        }else{
            return new TaskCompletionState("You dont have enough Space!", false);
        }
    }

    /**
     * Reduce the mount of a specific candy
     * Success if the player has enough candy on hand
     * @param candy which kind of candy to reduce
     * @param amount of candy to reduce
     * @return TaskCompletionState
     * @see TaskCompletionState
     */
    public TaskCompletionState reduceItem(Candy candy, int amount) {

        if (items[candy.getID()] >= amount) {
            items[candy.getID()] = items[candy.getID()] - amount;
            leftSpace = leftSpace + amount;
            return new TaskCompletionState("", true);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("You dont have enough ").append(amount).append(" of ").append(candy.name()).append(" on you!");
            return new TaskCompletionState(sb.toString(), false);
        }
    }

    /**
     * @return the space which is left.
     */
    public int spaceLeft() {
        return this.leftSpace;
    }

    /**
     * NOT FULL IMPLEMENTED!
     * @return the object as String
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Candy candy : Candy.values()) {
            sb.append(candy.name()).append(": ").append(items[candy.getID()]).append(", ");
        }
        return "[balance:" + balance + ", currentCity:" + getCurrentCityENUM() + ", leftSpace:" + leftSpace + ", items:" + sb.toString() + "]";
    }

    public boolean isBroke() {
        return isBroke;
    }

    public void setBroke(boolean broke) {
        isBroke = broke;
    }

    public int getNumber() {
        return number;
    }
    public boolean isPlayerReady() {
        return playerReady;
    }

    public void setPlayerReady(boolean playerReady) {
        this.playerReady = playerReady;
    }
}
