package de.materna.candygame;

import de.materna.candygame.enums.Candy;

import java.rmi.ServerError;

public class Account {
    private int playerDebt;
    private int playerAccountBalance;
    private int[] items = new int[Candy.values().length];
    private final Player player;

    private Account() {
        this.player = null;
    }

    public Account(Player player) {
        this.player = player;
        this.playerDebt = 0;
        this.playerAccountBalance = 0;
    }

    public Account(Player player, int playerDebt, int playerAccountBalance) {
        this(player);
        this.playerDebt = playerDebt;
        this.playerAccountBalance = playerAccountBalance;
    }

    public int getPlayerDebt() {
        return playerDebt;
    }

    public void increaseDebt(int amount) {
        this.playerDebt = this.playerDebt + amount;
    }

    public void reduceDebt(int amount) {
        if (amount > this.playerDebt) {
            this.playerDebt = 0;
        } else {
            this.playerDebt = this.playerDebt - amount;
        }
    }

    public int getPlayerAccountBalance() {
        return playerAccountBalance;
    }

    public void increaseAccountBalance(int amount) {
        this.playerAccountBalance = this.playerAccountBalance + amount;
    }

    public TaskCompletionState reduceAccountBalance(int amount) {
        if (amount > this.playerAccountBalance) {
            return new TaskCompletionState("You don't have so much money in your account!", false);
        } else {
            this.playerAccountBalance = this.playerAccountBalance - amount;
            return new TaskCompletionState("", true);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void addItem(Candy candy, int amount) {
        items[candy.getNr()] = items[candy.getNr()] + amount;
        System.out.printf("You stored %d of %s in you account.%n", amount, candy.name());
    }

    public TaskCompletionState removeItem(Candy candy, int amount) {

        if (amount > items[candy.getNr()]) {
            return new TaskCompletionState("You dont have so much "+candy.name()+" stored!", false);
        } else {
            items[candy.getNr()] = items[candy.getNr()] - amount;
            return new TaskCompletionState("", true);
        }
    }

    int getItemAmount(int itemID) {
        return items[itemID];
    }
}
