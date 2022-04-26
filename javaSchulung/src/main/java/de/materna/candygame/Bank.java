package de.materna.candygame;

import de.materna.candygame.enums.Candy;
import de.materna.candygame.enums.CityENUM;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    public final CityENUM homecity;
    private ArrayList<Account> accounts = new ArrayList<Account>();

    /**
     * Create for each given player a account
     * @param players a list of the players in game
     * @param homeCity the city of the bank
     */
    public Bank(List<Player> players, CityENUM homeCity) {
        this.homecity = homeCity;
        for (Player player :
                players) {
            createAccount(player);
        }
    }

    private Bank() {
        this.homecity = CityENUM.BRONX;
    }

    /**
     * after a day increase the debt and account balance of each player
     * debt increase 10%, account balance increase 5%
     */
    public void nextDay() {
        debtInterestChanging();
        accountInterestCharges();
    }

    /**
     * Reduce the amount of candy in the account and give it the player.
     * Success: when player have enough space, account have enough items, player have a account, input is positive digit
     * @throws IllegalStateException if player has no account
     * @param player who want to take items
     * @param item which kind of candy to take
     * @param amount positive the amount of candy to take
     * @return TaskCompletionState
     * @see TaskCompletionState
     */
    public TaskCompletionState takeItems(Player player, Candy item, int amount) throws IllegalStateException {
        if(amount<0){
            return new TaskCompletionState("You entered a negative number!", false);
        }
        TaskCompletionState task;
        for (Account account :
                accounts) {
            if (account.getPlayer() == player ) {
                if(player.spaceLeft()>=amount) {
                    task = account.removeItem(item, amount);
                }else {
                    task=new TaskCompletionState("You dont have enough backpack space!", false);
                }
                if(!task.isSuccess){
                   return task;
                }
                task=player.addItem(item, amount);
                return task;
            }
        }
        throw new IllegalStateException("Player has no bank account.");
    }

    /**
     * Increase the amount of candy in the account and reduce it on the players side.
     * Success: when player have enough items, player have a account, input is positive digit
     * @throws IllegalStateException if player has no account
     * @param player who want to store items
     * @param item which kind of candy to store
     * @param amount positive the amount of candy to store
     * @return TaskCompletionState
     * @see TaskCompletionState
     */
    public TaskCompletionState storeItems(Player player, Candy item, int amount) throws IllegalStateException {
        if(amount<0){
            return new TaskCompletionState("You entered a negative number!", false);
        }
            for (Account account :
                    accounts) {
                if (account.getPlayer() == player) {
                    TaskCompletionState task = player.reduceItem(item, amount);
                    if (task.isSuccess) {
                        account.addItem(item, amount);
                        return new TaskCompletionState("", true);
                    }else{
                        return task;
                    }
                }
            }
            throw new IllegalStateException("Player has no bank account.");
    }

    /**
     * Increase the amount of money in the account and reduce on the player side.
     * Success: when player have enough money, player have a account, input is positive digit
     * @throws IllegalStateException if player has no account
     * @param player who want to store money
     * @param amount positive the amount of money to store
     * @return TaskCompletionState
     * @see TaskCompletionState
     */
    public TaskCompletionState storeMoney(Player player, int amount) throws IllegalStateException{
        if(amount<0){
            return new TaskCompletionState("You entered a negative number!", false);
        }
        for (Account account : accounts) {
            if (account.getPlayer() == player) {
                TaskCompletionState task = player.reduceMoney(amount);
                if (task.isSuccess) {
                    account.increaseAccountBalance(amount);
                    return new TaskCompletionState("", true);
                } else {
                    return task;
                }
            }
        }
        throw new IllegalStateException("Player has no bank account.");
    }

    /**
     * Reduce the amount of money in the account and increase on the player side.
     * Success: when account have enough money, player have a account, input is positive digit
     * @throws IllegalStateException if player has no account
     * @param player who want to take money
     * @param amount positive the amount of money to take
     * @return TaskCompletionState
     * @see TaskCompletionState
     */
    public TaskCompletionState takeMoney(Player player, int amount) throws IllegalStateException{
        if(amount<0){
            return new TaskCompletionState("You entered a negative number!", false);
        }
        for (Account account : accounts) {
            if (account.getPlayer() == player) {
                TaskCompletionState task = account.reduceAccountBalance(amount);
                if (task.isSuccess) {
                    player.addMoney(amount);
                }
                return task;
            }
        }
        throw new IllegalStateException("Player has no bank account.");
    }

    /**
     * Increase the amount of debt in the account and increase on the player side.
     * Success: when account have no debt, player have a account, input is positive digit
     * @throws IllegalStateException if player has no account
     * @param player who want to take a credit
     * @param askAmount positive the amount of money
     * @return TaskCompletionState
     * @see TaskCompletionState
     */
    public TaskCompletionState giveCredit(Player player, int askAmount) {
        if(askAmount<0){
            return new TaskCompletionState("You entered a negative number!", false);
        }
        for (Account account :
                accounts) {
            if (account.getPlayer() == player) {
                if (account.getPlayerDebt() == 0) {
                    player.addMoney(askAmount);
                    account.increaseDebt(askAmount);
                    return new TaskCompletionState("",true);
                } else {
                    return new TaskCompletionState("You have already a debt, first pay the "
                            +account.getPlayerDebt()+" debt!", false);
                }
            }
        }
        throw new IllegalStateException("Player has no bank account.");
    }

    /**
     * Increase the amount of debt in the account and increase on the player side.
     * Success: when player has enough money, player have a account, input is positive digit
     * @throws IllegalStateException if player has no account
     * @param player who want to repay a debt
     * @param amount positive the amount of money
     * @return TaskCompletionState
     * @see TaskCompletionState
     */
    public TaskCompletionState reduceDebt(Player player, int amount) {
        if(amount<0){
            return new TaskCompletionState("You entered a negative number!", false);
        }
        TaskCompletionState task;
        for (Account account : accounts) {
            if (account.getPlayer() == player) {
                if (amount >= account.getPlayerDebt()) {
                    task=player.reduceMoney(account.getPlayerDebt());
                    if (task.isSuccess) {
                        account.reduceDebt(account.getPlayerDebt());
                    }
                } else {
                    task =player.reduceMoney(amount);
                    if (task.isSuccess) {
                        account.reduceDebt(amount);
                    }
                }
                return task;
            }
        }
        throw new IllegalStateException("Player has no bank account.");
    }

    /**
     * give each player a account
     * @param player who need a account
     */
    public void createAccount(Player player) {
        for (Account account : accounts) {
            if (account.getPlayer() == player) {
                return;
            }
        }
        accounts.add(new Account(player));
    }

    /**
     * increase the debt by 10%
     */
    private void debtInterestChanging() {
        for (Account account : accounts) {
            account.increaseDebt((int) Math.round(account.getPlayerDebt() * 0.10));
        }
    }

    /**
     * increase account balance by 5%
     */
    private void accountInterestCharges() {
        for (Account account : accounts) {
            account.increaseAccountBalance((int) Math.round(account.getPlayerAccountBalance() *0.05));
        }
    }

    /**
     * NOT FULLY IMPLEMENTED
     * return the object as a string
     * @param player the player of the account
     * @return the object as a string
     */
    public String toStringPlayer(Player player) {
        StringBuilder sb = new StringBuilder();
        Account ac = null;
        for (Account account : accounts) {
            if (account.getPlayer() == player) {
                ac = account;
            }
        }
        if (ac == null) {
            throw new IllegalStateException("Player has no bank account.");
        }

        sb.append("[").append("playerMoney:").append(ac.getPlayerAccountBalance())
                .append(", playerDebt:").append(ac.getPlayerDebt()).append(", playerItems: ");

        for (Candy candy : Candy.values()) {
            sb.append(candy.name()).append(": ").append(ac.getItemAmount(candy.getID()));
            if (candy.getID() + 1 != Candy.values().length) {
                sb.append(", ");
            }
        }
        sb.append(", HOMECITY:").append(homecity);


        return sb.toString();
    }

    /**
     * Return the account which is connected to the given player
     * @throws IllegalStateException if player has no account
     * @param player a specific player
     * @return connected to the given player
     */
    Account getPlayerAccount(Player player) throws IllegalStateException{
        for (Account account: accounts
             ) {
            if(account.getPlayer()==player){
                return account;
            }
        }
        throw new IllegalStateException("Player has no bank account.");
    }
}
