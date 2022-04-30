package de.materna.candygame;

import de.materna.candygame.enums.Candy;
import de.materna.candygame.enums.City;

public class UIConsoleGenerator {
    private boolean isEnd=false;
    /**
     * Print the market window which contains the market candys and candy prices of that city and
     * print the player window with the amount of candy and the money
     * @param cityCandyCosts the candy costs of that city
     * @param player the player who is in that city market
     */
    void printMarketWindow(int[] cityCandyCosts, Player player){
        clearScreen();
        System.out.println("+"+"-".repeat(41)+"+");
        System.out.printf("|%17s%s%18s|%n","","MARKET","");
        System.out.println("+"+",".repeat(41)+"+");
        if(Candy.values().length%2==0) {
            for (int i = 0; i < Candy.values().length; i += 2) {
                System.out.printf("$ %-13s: %2d€ $ %-13s: %2d€ $%n",
                        Candy.values()[i].name(), cityCandyCosts[i],
                        Candy.values()[i + 1].name(), cityCandyCosts[i + 1]);
            }
        }else{
            for (int i = 0; i < Candy.values().length; i += 2) {
                System.out.printf("$ %-13s: %2d€  $ %-13s: %2d€ $%n",
                        Candy.values()[i].name(), cityCandyCosts[i],
                        Candy.values()[i + 1].name(), cityCandyCosts[i + 1]);
            }
            System.out.printf("$ %-13s: %2d€ $ %-13s: %2s€ $%n",
                    Candy.values()[Candy.values().length-1].name(),
                    cityCandyCosts[Candy.values().length-1], "", "");
        }
        System.out.println("+"+"-".repeat(41)+"+");
        //TODO number length break UI
        System.out.printf("$%14s%s%2d%s%17s$%n","","PLAYER[",player.getNumber(),"]","");
        System.out.println("+"+",".repeat(41)+"+");
        System.out.printf("$ %-10s: %5d€ $ %-10s: %5d  $%n",
                "BALANCE",player.getBalance(),"Backpack", player.spaceLeft()
        );
        if(Candy.values().length%2==0) {
            for (int i = 0; i < Candy.values().length; i += 2) {
                System.out.printf("$ %-10s: %5d  $ %-10s: %5d  $%n",
                        Candy.values()[i].name(), player.getItem(i),
                        Candy.values()[i+1].name(), player.getItem(+1)
                );
            }
        }else{
            for (int i = 0; i < Candy.values().length; i += 2) {
                System.out.printf("$ %-10s: %5d  $ %-10s: %5d  $%n",
                        Candy.values()[i].name(), player.getItem(i),
                        Candy.values()[i+1].name(), player.getItem(+1)
                );
            }
            System.out.printf("$ %-10s: %5d  $ %-10s: %5s  $%n",
                    Candy.values()[Candy.values().length-1].name(),
                    player.getItem(Candy.values().length-1),
                    "", "");
        }
        System.out.println("+"+"-".repeat(41)+"+");
        System.out.printf(" %-14s | %-14s%n","[b]uy","[s]ell");
        System.out.printf("%-14s | %-14s %n","[r]eturn","[l]eave");
    }

    /**
     * Print the travel window which contains the cities and the travel costs
     * @param travelCost the travel cost to get to the cities
     * @param player the player who wants to travel
     * @param currentDay the current in game day
     */
    //TODO change that playercity is not shown
    void printTravelTargets(int[] travelCost, Player player, int currentDay){
        clearScreen();
        System.out.println("+"+"-".repeat(45)+"+");
        System.out.printf("|%5s%s%2s<%2s%-11s:%4d%2s>%2s|%n","","Travel Targets","","","Days Left",currentDay,"","");
        System.out.println("+"+",".repeat(45)+"+");
        if(City.values().length%2==0) {
            for (int i = 0; i < City.values().length; i += 2) {
                System.out.printf("$ [%d]%-12s: %2d€ $ [%d]%-12s: %2d€ $%n",
                        City.values()[i].getID(), City.values()[i].name(), travelCost[i],
                        City.values()[i+1].getID(), City.values()[i + 1].name(), travelCost[i + 1]);
            }
        }else{
            for (int i = 0; i < Candy.values().length; i += 2) {
                System.out.printf("$ [%d]%-12s: %2d€ $ [%d]%-12s: %2d€ $%n",
                        City.values()[i].getID(), City.values()[i].name(), travelCost[i],
                        City.values()[i+1].getID(), City.values()[i + 1].name(), travelCost[i + 1]);
            }
            System.out.printf("$ [%d]%-12s: %2d€ $    %-12s  %2s  $%n",
                    City.values()[City.values().length-1].getID(), City.values()[City.values().length-1].name(),
                    travelCost[City.values().length-1], "", "");
        }
        System.out.println("+"+"-".repeat(45)+"+");
        System.out.printf("%-14s %n","[r]eturn");
        System.out.print("Pls enter the number of the City you want to travel:");
    }

    /**
     * Print the bank window which show the account of the player and the player window
     * @param bank the bank where the player is.
     * @param player the player who is in the bank
     */
    void printBankWindow(Bank bank, Player player){
        clearScreen();
        Account playerAccount = bank.getPlayerAccount(player);
        System.out.println("+"+"-".repeat(41)+"+");
        System.out.printf("|%18s%s%19s|%n","","BANK","");
        System.out.println("+"+",".repeat(41)+"+");
        System.out.printf("$ %-10s: %5d€ $ %-10s: %5d€ $%n",
                "DEBT",playerAccount.getPlayerDebt(),"BALANCE",playerAccount.getPlayerAccountBalance()
        );
        if(Candy.values().length%2==0) {
            for (int i = 0; i < Candy.values().length; i += 2) {
                System.out.printf("$ %-13s: %2d€ $ %-13s: %2d€ $%n",
                        Candy.values()[i].name(), playerAccount.getItemAmount(i),
                        Candy.values()[i + 1].name(), playerAccount.getItemAmount(i+1));
            }
        }else{
            for (int i = 0; i < Candy.values().length; i += 2) {
                System.out.printf("$ %-13s: %2d€  $ %-13s: %2d€ $%n",
                        Candy.values()[i].name(), playerAccount.getItemAmount(i),
                        Candy.values()[i + 1].name(), playerAccount.getItemAmount(i+1));
            }
            System.out.printf("$ %-13s: %2d€ $ %-13s: %2s€ $%n",
                    Candy.values()[Candy.values().length-1].name(),
                    playerAccount.getItemAmount(Candy.values().length-1), "", "");
        }
        System.out.println("+"+"-".repeat(41)+"+");
        //TODO number length break UI
        System.out.printf("$%15s%s%2d%s%16s$%n","","PLAYER[",player.getNumber(),"]","");
        System.out.println("+"+",".repeat(41)+"+");
        System.out.printf("$ %-10s: %5d€ $ %-10s: %5d  $%n",
                "BALANCE",player.getBalance(),"Backpack", player.spaceLeft()
        );
        if(Candy.values().length%2==0) {
            for (int i = 0; i < Candy.values().length; i += 2) {
                System.out.printf("$ %-10s: %5d  $ %-10s: %5d  $%n",
                        Candy.values()[i].name(), player.getItem(i),
                        Candy.values()[i+1].name(), player.getItem(+1)
                );
            }
        }else{
            for (int i = 0; i < Candy.values().length; i += 2) {
                System.out.printf("$ %-10s: %5d  $ %-10s: %5d  $%n",
                        Candy.values()[i].name(), player.getItem(i),
                        Candy.values()[i+1].name(), player.getItem(+1)
                );
            }
            System.out.printf("$ %-10s: %5d  $ %-10s: %5s $%n",
                    Candy.values()[Candy.values().length-1].name(),
                    player.getItem(Candy.values().length-1),
                    "", "");
        }
        System.out.println("+"+"-".repeat(41)+"+");
        System.out.printf("%-14s | %-14s %n%-14s | %-14s %n%-14s | %-14s %n",
                "st[o]re items","t[a]ke items",
                "store [m]oney","ta[k]e money","take [c]redit","repay [d]ebt");
        System.out.printf("%-14s | %-14s %n","[r]eturn","[l]eave");
    }

    /**
     * The main player window it contains the items and money of the player
     * @param player
     * @param bankCity
     */
    void printPlayerWindow(Player player, boolean bankCity){
        clearScreen();

        System.out.println("+"+"-".repeat(41)+"+");
        //TODO number length break UI
        System.out.printf("$%14s%s%2d%s%17s$%n","","PLAYER[",player.getNumber(),"]","");
        System.out.println("+"+",".repeat(41)+"+");
        System.out.printf("$ %-10s: %5d€ $ %-10s:  %4d  $%n",
                "BALANCE",player.getBalance(),"Backpack", player.spaceLeft()
        );
        if(Candy.values().length%2==0) {
            for (int i = 0; i < Candy.values().length; i += 2) {
                System.out.printf("$ %-10s: %5d  $ %-10s: %5d  $%n",
                        Candy.values()[i].name(), player.getItem(i),
                        Candy.values()[i+1].name(), player.getItem(i+1)
                );
            }
        }else{
            for (int i = 0; i < Candy.values().length; i += 2) {
                System.out.printf("$ %-10s: %5d  $ %-10s: %5d  $%n",
                        Candy.values()[i].name(), player.getItem(i),
                        Candy.values()[i+1].name(), player.getItem(i+1)
                );
            }
            System.out.printf("$ %-10s: %5d  $ %-10s: %5s  $%n",
                    Candy.values()[Candy.values().length-1].name(),
                    player.getItem(Candy.values().length-1),
                    "", "");
        }
        System.out.println("+"+"-".repeat(41)+"+");

        if(!bankCity){
            System.out.printf("%-14s | %-14s%n","[t]ravel","[m]arket");
        }else{
            System.out.printf("%-14s | %-14s | %-14s%n","[t]ravel","[m]arket","[b]ank");
        }
        System.out.printf("%-14s | %-14s %n","[r]eturn","[l]eave");
    }

    /**
     * print the scoreboard which show the difference between owned money and debt.
     * @param score the score of the player
     */
    void printScoreBoard(int playerAmount, int[] score){
        clearScreen();
        System.out.println("+"+"/\\".repeat(20)+"/+");
        //TODO number length break UI
        System.out.printf("$%15s%s%16s$%n","","SCOREBOARD","");
        System.out.println("+"+"/\\".repeat(20)+"/+");
        for(int i=0;i<playerAmount;i++){
            System.out.printf("$%8s%s%2d%2s%3s%s%5d%8s$%n","","Player[",i,"]","","SCORE:",score[i],"");
            System.out.println("+"+"^^".repeat(20)+"^+");
        }
        System.out.println("+"+"/\\".repeat(20)+"/+");
    }

    /**
     * print 10 empty lines to clear the window
     */
    public static void clearScreen() {
        System.out.printf("%n".repeat(10));
    }

    /**
     * Print out a single message
     * @param msg message to be printed
     */
    public void message (String msg){
        System.out.println(msg);
    }

    /**
     * Print the error message
     * needed a input from the user to acknowledge it
     * @param msg message to be printed
     */
    public void errorMessage (String msg){
        System.out.println(msg);
        System.out.println("Press a enter...");
    }
}
