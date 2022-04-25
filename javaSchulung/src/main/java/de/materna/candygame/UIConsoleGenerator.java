package de.materna.candygame;

import de.materna.candygame.enums.Candy;
import de.materna.candygame.enums.CityENUM;

public class UIConsoleGenerator {

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
        System.out.printf("$%17s%s%18s$%n","","PLAYER","");
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
        System.out.printf("%-14s | %-14s | %-14s%n","[t]ravel","[b]uy","[s]ell");
        System.out.printf("%-14s | %-14s %n","[r]eturn","[l]eave");
    }
    //TODO change that playercity is not shown
    void printTravelTargets(int[] travelCost, Player player, int currentDay){
        clearScreen();
        System.out.println("+"+"-".repeat(45)+"+");
        System.out.printf("|%5s%s%2s<%2s%-11s:%4d%2s>%2s|%n","","Travel Targets","","","Days Left",currentDay,"","");
        System.out.println("+"+",".repeat(45)+"+");
        if(CityENUM.values().length%2==0) {
            for (int i = 0; i < CityENUM.values().length; i += 2) {
                System.out.printf("$ [%d]%-12s: %2d€ $ [%d]%-12s: %2d€ $%n",
                        CityENUM.values()[i].getID(),CityENUM.values()[i].name(), travelCost[i],
                        CityENUM.values()[i+1].getID(),CityENUM.values()[i + 1].name(), travelCost[i + 1]);
            }
        }else{
            for (int i = 0; i < Candy.values().length; i += 2) {
                System.out.printf("$ [%d]%-12s: %2d€ $ [%d]%-12s: %2d€ $%n",
                        CityENUM.values()[i].getID(),CityENUM.values()[i].name(), travelCost[i],
                        CityENUM.values()[i+1].getID(),CityENUM.values()[i + 1].name(), travelCost[i + 1]);
            }
            System.out.printf("$ [%d]%-12s: %2d€ $    %-12s  %2s  $%n",
                    CityENUM.values()[CityENUM.values().length-1].getID(),CityENUM.values()[CityENUM.values().length-1].name(),
                    travelCost[CityENUM.values().length-1], "", "");
        }
        System.out.println("+"+"-".repeat(45)+"+");
        System.out.printf("%-14s %n","[r]eturn");
        System.out.print("Pls enter the number of the City you want to travel:");
    }
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
        System.out.printf("$%18s%s%17s$%n","","PLAYER","");
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

    void printPlayerWindow(Player player, boolean bankCity){
        clearScreen();

        System.out.println("+"+"-".repeat(41)+"+");
        //TODO number length break UI
        System.out.printf("$%17s%s%18s$%n","","PLAYER","");
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

    void printScoreBoard(int score){
        clearScreen();
        System.out.println("+"+"/\\".repeat(20)+"/+");
        //TODO number length break UI
        System.out.printf("$%15s%s%16s$%n","","SCOREBOARD","");
        System.out.println("+"+"^^".repeat(20)+"^+");
        System.out.printf("|%9s%-8s: %10d€%10s |%n",
                "","SCORE",score,""
        );
        System.out.println("+"+"/\\".repeat(20)+"/+");
    }
    public static void clearScreen() {
        System.out.printf("%n".repeat(10));
    }
    public void message (String msg){
        System.out.println(msg);
    }
    public void errorMessage (String msg){
        System.out.println(msg);
        System.out.println("Press a enter...");
    }
}
