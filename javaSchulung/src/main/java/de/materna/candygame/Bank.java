package de.materna.candygame;

import de.materna.candygame.enums.Candy;
import de.materna.candygame.enums.CityENUM;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    public final CityENUM homecity;
    private ArrayList<Account> accounts = new ArrayList<Account>();

    public Bank(List<Player> players, CityENUM homeCity) {
        this.homecity = homeCity;
        for (Player player :
                players) {
            createKonto(player);
        }
    }

    private Bank() {
        this.homecity = CityENUM.BRONX;
    }

    public void nextDay() {
        debtInterestChanging();
        accountInterestCharges();
    }

    public TaskCompletionState takeItems(Player player, Candy item, int amount) {
        TaskCompletionState task;
        for (Account account :
                accounts) {
            if (account.getPlayer() == player ) {
                task = account.removeItem(item, amount);
                if(!task.isSuccess){
                   return task;
                }
                task=player.addItem(item, amount);
                return task;
            }
        }
        throw new IllegalStateException("Player has no bank account.");
    }

    public TaskCompletionState storeItems(Player player, Candy item, int amount) {
        TaskCompletionState task = player.reduceItem(item, amount);
        if (task.isSuccess) {
            for (Account account :
                    accounts) {
                if (account.getPlayer() == player) {
                    account.addItem(item, amount);
                    return new TaskCompletionState("", true);
                }
            }
        }
        return task;
    }

    public TaskCompletionState storeMoney(Player player, int amount) {
        for (Account account : accounts) {
            if (account.getPlayer() == player) {
                TaskCompletionState task = player.reduceMoney(amount);
                if (task.isSuccess) {
                    account.increaseAccountBalance(amount);
                    return new TaskCompletionState("", true);
                } else {
                    return task;
                }
            } else {
                throw new IllegalStateException("Player has no bank account.");
            }
        }
        throw new IllegalStateException("takeMoney didn't return in the right place.");
    }

    public TaskCompletionState takeMoney(Player player, int amount) {
        for (Account account : accounts) {
            if (account.getPlayer() == player) {
                TaskCompletionState task = account.reduceAccountBalance(amount);
                if (task.isSuccess) {
                    player.addMoney(amount);
                }
                return task;
            } else {
                throw new IllegalStateException("Player has no bank account.");
            }
        }
        throw new IllegalStateException("takeMoney didn't return in the right place.");
    }

    public TaskCompletionState giveCredit(Player player, int askAmount) {
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
            } else {
                throw new IllegalStateException("Player has no bank account.");
            }
        }
        throw new IllegalStateException("The Game shouldn't be in that state");
    }

    public TaskCompletionState reduceDebt(Player player, int amount) {
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

    public void createKonto(Player player) {
        for (Account account : accounts) {
            if (account.getPlayer() == player) {
                return;
            }
        }
        accounts.add(new Account(player));
    }

    private void debtInterestChanging() {
        for (Account account : accounts) {
            account.increaseDebt((int) Math.round(account.getPlayerDebt() * 0.10));
        }
    }

    private void accountInterestCharges() {
        for (Account account : accounts) {
            account.increaseAccountBalance((int) Math.round(account.getPlayerAccountBalance() *0.05));
        }
    }

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
            sb.append(candy.name()).append(": ").append(ac.getItemAmount(candy.getNr()));
            if (candy.getNr() + 1 != Candy.values().length) {
                sb.append(", ");
            }
        }
        sb.append(", HOMECITY:").append(homecity);


        return sb.toString();
    }

    Account getPlayerAccount(Player player){
        for (Account account: accounts
             ) {
            if(account.getPlayer()==player){
                return account;
            }
        }
        throw new IllegalStateException("Player has no bank account.");
    }
}
