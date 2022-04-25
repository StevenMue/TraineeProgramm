package de.materna.candygame;

import de.materna.candygame.enums.Candy;
import de.materna.candygame.enums.CityENUM;


public class Player {
    private int balance;
    private CityENUM currentCityENUM;

    public int getItem(int itemID) {
        if (itemID >= 0 && itemID < items.length) {
            return items[itemID];
        } else {
            throw new ArrayIndexOutOfBoundsException("itemID is out of bounds [0," + items.length + ") itemID: " + itemID);
        }
    }

    private int[] items = new int[Candy.values().length];
    private int leftSpace = 100;

    private Player() {
    }

    public Player(int balance, CityENUM currentCityENUM) {
        this.balance = balance;
        this.currentCityENUM = currentCityENUM;
    }

    public int getBalance() {
        return balance;
    }

    public CityENUM getCurrentCityENUM() {
        return currentCityENUM;
    }

    public void addMoney(int amount) {
        this.balance = this.balance + amount;

    }

    public TaskCompletionState reduceMoney(int amount) {
        if (this.balance >= amount) {
            this.balance = this.balance - amount;
            return new TaskCompletionState("",true);
        } else {
            return new TaskCompletionState("You dont have enough money.", false);
        }
    }

    public TaskCompletionState travel(CityENUM destination, int cost) {
        TaskCompletionState state=this.reduceMoney(cost);
        if (state.isSuccess) {
            this.currentCityENUM = destination;
        }
        return state;
    }

    public TaskCompletionState addItem(Candy candy, int amount) {
        if(leftSpace - amount>=0) {
            leftSpace = leftSpace - amount;
            items[candy.getNr()] = items[candy.getNr()] + amount;
            return new TaskCompletionState("",true);
        }else{
            return new TaskCompletionState("You dont have enough Space!", false);
        }
    }

    public TaskCompletionState reduceItem(Candy candy, int amount) {

        if (items[candy.getNr()] >= amount) {
            items[candy.getNr()] = items[candy.getNr()] - amount;
            leftSpace = leftSpace + amount;
            return new TaskCompletionState("", true);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("You dont have enough ").append(amount).append(" of ").append(candy.name()).append(" on you!");
            return new TaskCompletionState(sb.toString(), false);
        }
    }

    public int spaceLeft() {
        return this.leftSpace;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Candy candy : Candy.values()) {
            sb.append(candy.name()).append(": ").append(items[candy.getNr()]).append(", ");
        }
        return "[balance:" + balance + ", currentCity:" + getCurrentCityENUM() + ", leftSpace:" + leftSpace + ", items:" + sb.toString() + "]";
    }
}
