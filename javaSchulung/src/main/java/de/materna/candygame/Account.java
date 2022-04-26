package de.materna.candygame;

import de.materna.candygame.enums.Candy;

/**
 * A Account hold the items and money of each player
 * it provides the necessary functions for the bank to manipulate and get the values
 * Every player has only one account
 */
public class Account {
    private int playerDebt;
    private int playerAccountBalance;
    private int[] items = new int[Candy.values().length];
    private final Player player;

    private Account() {
        this.player = null;
    }

    Account(Player player) {
        this.player = player;
        this.playerDebt = 0;
        this.playerAccountBalance = 0;
    }

    Account(Player player, int playerDebt, int playerAccountBalance) {
        this(player);
        this.playerDebt = playerDebt;
        this.playerAccountBalance = playerAccountBalance;
    }

    /**
     * Return the amount of debt.
     * @return the amount of debt.
     */
    int getPlayerDebt() {
        return playerDebt;
    }

    /**
     * Increase the value of debt.
     * @param amount of debt to add.
     */
    void increaseDebt(int amount) {
        this.playerDebt = this.playerDebt + amount;
    }

    /**
     * Reduce the value of debt.
     * Can't go below 0.
     * @param amount of debt to reduce
     */
    void reduceDebt(int amount) {
        if (amount > this.playerDebt) {
            this.playerDebt = 0;
        } else {
            this.playerDebt = this.playerDebt - amount;
        }
    }

    /**
     *
     * @return the amount of money on the account.
     */
    int getPlayerAccountBalance() {
        return playerAccountBalance;
    }

    /**
     * Increase the account balance.
     * @param amount of money added to the account
     */
    void increaseAccountBalance(int amount) {
        this.playerAccountBalance = this.playerAccountBalance + amount;
    }

    /**
     * Reduces the amount of money on the account.
     * If the amount of asked money is higher than the account balance money will not be reduced.
     * @param amount the amount of money should be reduced
     * @return TaskCompletionState
     * @see TaskCompletionState
     */
    TaskCompletionState reduceAccountBalance(int amount) {
        if (amount > this.playerAccountBalance) {
            return new TaskCompletionState("You don't have so much money in your account!", false);
        } else {
            this.playerAccountBalance = this.playerAccountBalance - amount;
            return new TaskCompletionState("", true);
        }
    }

    /**
     *
     * @return the player of the account
     * @see Player
     */
    Player getPlayer() {
        return player;
    }

    /**
     * add a specific amount of a candy-typ to the account.
     *
     * @param candy Which kind of candy is added
     * @param amount how much candy should be added to the account
     * @see Candy
     */
    void addItem(Candy candy, int amount) {
        items[candy.getID()] = items[candy.getID()] + amount;
    }

    /**
     * Remove a specific amount of a candy-type form the account
     *
     * @param candy Which kind of candy is removed
     * @param amount how much candy should be removed from the account
     * @return TaskCompletionState
     * @see TaskCompletionState
     * @see Candy
     */
    TaskCompletionState removeItem(Candy candy, int amount) {

        if (amount > items[candy.getID()]) {
            return new TaskCompletionState("You dont have so much "+candy.name()+" stored!", false);
        } else {
            items[candy.getID()] = items[candy.getID()] - amount;
            return new TaskCompletionState("", true);
        }
    }

    /**
     * Return the amount of candy in the account with that ID.
     * @param itemID the ID of the kind of candy
     * @return the amount of that candy-type which is stored in the account.
     * @see Candy
     */
    int getItemAmount(int itemID) {
        return items[itemID];
    }
    /**
     * Return the amount of candy in the account.
     * @param candy Which kind of candy is asked.
     * @return the amount of that candy-type which is stored in the account.
     * @see Candy
     */
    int getItemAmount(Candy candy) {
        return items[candy.getID()];
    }
}
