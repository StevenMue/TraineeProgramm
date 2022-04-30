package de.materna.candygame;

import de.materna.candygame.enums.Candy;
import de.materna.candygame.enums.City;
import de.materna.candygame.enums.PlayerStates;
import de.materna.candygame.events.Event;
import de.materna.candygame.events.Gift;
import de.materna.candygame.events.RandomNegativeTravelAmount;
import de.materna.candygame.events.Thief;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Controller {
    public static final String YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN = "You don't entered a number. Try again";
    private final int amountOfPlayers;
    private int currentDay = 0;
    ArrayList<Player> players = new ArrayList<>();
    private Bank bank;
    private UIConsoleGenerator ui = new UIConsoleGenerator();
    PlayerStates state;
    Player currentPlayer;

    Random r;
    int[][] candys=new int [City.values().length][Candy.values().length];
    StringBuilder sb = new StringBuilder();

    /**
     * Constructor for initialise the game and its classes
     * @param amountOfPlayers the number of players in the game
     */
    Controller(int amountOfPlayers) {
        for (int i = 0; i < amountOfPlayers; i++) {
            players.add(new Player(i+1,2000, City.CENTRAL));
        }
        this.state = PlayerStates.IDLE;
        bank = new Bank(players, City.BRONX);
        r = new Random();
        this.amountOfPlayers=amountOfPlayers;
    }

    /**
     * Get the user input.
     * @return user input as a string
     */
    private String getUserInput() {
        return new Scanner(System.in).nextLine();
    }

    /**
     * Check the user input if its one of the options and activate wanted action.
     * @param input string user input
     */
    private void handler(String input) {
        switch (input.toLowerCase(Locale.ROOT)) {
            case "t" -> travel();
            case "b" -> bankOrBuy();
            case "s" -> sell();
            case "o" -> wantToStoreItems();
            case "r" -> goBack();
            case "l" -> leave();
            case "m" -> marketOrBank();
            case "k" -> wantToTakeMoney();
            case "a" -> wantToTakeItems();
            case "c" -> wantToTakeCredit();
            case "d" -> wantToRepayDebt();
            default -> {
                ui.errorMessage("Pls enter a valid key.");
                getUserInput();
            }
        }
    }

    /**
     * Check the position and input of the player to decide which action to choose.
     * proceed to the market if the player is in the player window.
     * proceed with the store money action when the player is in the bank.
     */
    private void marketOrBank() {
        if (state == PlayerStates.IDLE) {
            ui.printMarketWindow(candys[currentPlayer.getCurrentCityENUM().getID()], currentPlayer);
            state = PlayerStates.MARKET;
        } else if (state == PlayerStates.BANK) {
            storeMoney();
        } else {
            ui.message("Pls enter a valid key.");
            handler(getUserInput());
        }
    }

    /**
     * Check the position and input of the player to decide which action to choose.
     * Proceed to the bank if the player is in the player window.
     * Proceed with the buy action when the player is in the market.
     */
    private void bankOrBuy() {
        if (state == PlayerStates.IDLE && currentPlayer.getCurrentCityENUM() == bank.homecity) {
            ui.printBankWindow(bank, currentPlayer);
            state = PlayerStates.BANK;
        } else if (state == PlayerStates.MARKET) {
            buy();
        } else {
            ui.message("Pls enter a valid key.");
        }
    }

    /**
     * Check input of the player to decide which action to choose.
     * Proceed with the store items action when the player is in the bank.
     */
    private void wantToStoreItems() {
        if (state == PlayerStates.BANK) {
            ui.printBankWindow(bank, currentPlayer);
            storeItems();
        } else {
            ui.message("Pls enter a valid key.");
        }
    }

    /**
     * Check input of the player to decide which action to choose.
     * Proceed with the take money action when the player is in the bank.
     */
    private void wantToTakeMoney() {
        if (state == PlayerStates.BANK) {
            ui.printBankWindow(bank, currentPlayer);
            takeMoney();
        } else {
            ui.message("Pls enter a valid key.");
        }
    }

    /**
     * Check input of the player to decide which action to choose.
     * Proceed with the take credit action when the player is in the bank.
     */
    private void wantToTakeCredit() {
        if (state == PlayerStates.BANK) {
            ui.printBankWindow(bank, currentPlayer);
            takeCredit();
        } else {
            ui.message("Pls enter a valid key.");
        }
    }

    /**
     * Check input of the player to decide which action to choose.
     * Proceed with the take items action when the player is in the bank.
     */
    private void wantToTakeItems() {
        if (state == PlayerStates.BANK) {
            ui.printBankWindow(bank, currentPlayer);
            takeItems();
        } else {
            ui.message("Pls enter a valid key.");
        }
    }

    /**
     * Check input of the player to decide which action to choose.
     * Proceed with the repay debt action when the player is in the bank.
     */
    private void wantToRepayDebt() {
        if (state == PlayerStates.BANK) {
            ui.printBankWindow(bank, currentPlayer);
            repayDebt();
        } else {
            ui.message("Pls enter a valid key.");
        }
    }

    /**
     * Method to buy items in the market.
     * User need to enter the ID of the candy and the amount.
     */
    private void buy() {
        ui.message("What do you want to buy? : ");

        try {
            int itemID = Integer.parseInt(getUserInput());
            ui.message("How much do you wish to buy? You can buy " + currentPlayer.spaceLeft() + " : ");
            int amount = Integer.parseInt(getUserInput());
            if (currentPlayer.spaceLeft() < amount) {
                ui.errorMessage("You backpack cant hold so much! You have only " + currentPlayer.spaceLeft() + " space left!");
                getUserInput();
                return;
            }
            TaskCompletionState task = currentPlayer.reduceMoney(amount * candys[currentPlayer.getCurrentCityENUM().getID()][itemID]);

            if (task.isSuccess) {
                task = currentPlayer.addItem(Candy.getCandy(itemID), amount);
                if (!task.isSuccess) {
                    ui.errorMessage(task.msg);
                    getUserInput();
                }
            } else {
                ui.errorMessage(task.msg);
                getUserInput();
            }
        } catch (NumberFormatException e) {
            ui.errorMessage(YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN);
            getUserInput();
        } catch (ArrayIndexOutOfBoundsException a) {
            ui.errorMessage("A item with that ID doesn't exits");
            getUserInput();
        }

    }

    /**
     * Method to sell items in the market.
     * User need to enter the ID of the candy and the amount.
     */
    private void sell() {
        ui.message("What do you want to sell? : ");
        try {
            int itemID = Integer.parseInt(getUserInput());
            ui.message("How much do you wish to sell? You have " + currentPlayer.getItem(itemID) + " : ");
            int amount = Integer.parseInt(getUserInput());
            TaskCompletionState task = currentPlayer.reduceItem(Candy.getCandy(itemID), amount);
            if (task.isSuccess) {
                currentPlayer.addMoney(amount * candys[currentPlayer.getCurrentCityENUM().getID()][itemID]);
            } else {
                ui.errorMessage(task.msg);
                getUserInput();
            }
        } catch (NumberFormatException e) {
            ui.errorMessage(YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN);
            getUserInput();
        } catch (ArrayIndexOutOfBoundsException a) {
            ui.errorMessage("A item with that ID doesn't exits");
            getUserInput();
        }
    }

    /**
     * Method to store items in the account.
     * User need to enter the ID of the candy and the amount.
     */
    private void storeItems() {
        ui.message("What do you want to store?");
        try {
            int itemID = Integer.parseInt(getUserInput());
            ui.message("How much do you wish to store? You have " + currentPlayer.getItem(itemID));
            int amount = Integer.parseInt(getUserInput());
            TaskCompletionState task = bank.storeItems(currentPlayer, Candy.getCandy(itemID), amount);
            if (!task.isSuccess) {
                ui.errorMessage(task.msg);
                getUserInput();
            }
        } catch (NumberFormatException e) {
            ui.errorMessage(YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN);
            getUserInput();
        } catch (ArrayIndexOutOfBoundsException a) {
            ui.errorMessage("A item with that ID doesn't exits");
            getUserInput();
        }
    }

    /**
     * Method to take items from the account.
     * User need to enter the ID of the candy and the amount.
     */
    private void takeItems() {
        ui.message("What do you want to take?");
        TaskCompletionState task;

        try {
            int itemID = Integer.parseInt(getUserInput());
            ui.message("How much do you wish to take?");
            int amount = Integer.parseInt(getUserInput());
            if (currentPlayer.spaceLeft() >= amount) {
                ui.message("You dont have enough left space!");
                task = bank.takeItems(currentPlayer, Candy.getCandy(itemID), amount);
                if (!task.isSuccess) {
                    ui.errorMessage(task.msg);
                    getUserInput();
                }
            }
        } catch (NumberFormatException e) {
            ui.errorMessage(YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN);
        } catch (IllegalArgumentException a) {
            ui.errorMessage("A candy with that ID doesn't exits");
            getUserInput();
        }
    }

    /**
     * Method to store money in the account.
     * User need to enter the amount.
     */
    private void storeMoney() {
        try {
            ui.message("How much do you wish to store?");
            int amount = Integer.parseInt(getUserInput());
            TaskCompletionState task = bank.storeMoney(currentPlayer, amount);
            if (!task.isSuccess) {
                ui.errorMessage(task.msg);
                getUserInput();
            }
        } catch (NumberFormatException e) {
            ui.errorMessage(YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN);
            getUserInput();
        }
    }

    /**
     * Method to take money from the account.
     * User need to enter  the amount.
     */
    private void takeMoney() {
        try {
            ui.message("How much do you wish to take?");
            int amount = Integer.parseInt(getUserInput());
            TaskCompletionState task = bank.takeMoney(currentPlayer, amount);
            if (!task.isSuccess) {
                ui.errorMessage(task.msg);
                getUserInput();
            }
        } catch (NumberFormatException e) {
            ui.errorMessage(YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN);
            getUserInput();
        }
    }

    /**
     * Method to return to the player window.
     */
    private void goBack() {
        state = PlayerStates.IDLE;
    }

    /**
     * Method to end the game and show the scoreboard.
     */
    private void leave() {
        currentDay = 31;
    }

    /**
     * Method to travel to another city.
     * Check if an event will occur.
     * User need to enter the ID of the city.
     */
    private void travel() {
        int[] costs = new int[City.values().length];

        for (City city : City.values()) {
            costs[city.getID()] = calculateTravelCosts(city);
        }
        TaskCompletionState task=checkIfTravelIsPossible(costs);
        if(!task.isSuccess){
            ui.message(task.msg);
            currentPlayer.setBroke(true);
            return;
        }
        ui.printTravelTargets(costs, currentPlayer, currentDay);
        try {
            String input;
                input = getUserInput();
                if (input.equals("r")) {
                    return;
                }
            if(Integer.parseInt(input)>(City.values().length-1)||Integer.parseInt(input)<0){
                throw new IllegalArgumentException();
            }
           task= currentPlayer.travel(City.getCity(Integer.parseInt(input)), costs[Integer.parseInt(input)]);
            if(!task.isSuccess){
                ui.errorMessage(task.msg);
                getUserInput();
                return;
            }
            currentPlayer.setPlayerReady(true);
            if (Math.random() <= 0.1) {
                Event event = createEvent();
                task = event.process(currentPlayer);
                if (task.isSuccess) {
                    currentDay += event.getDuration();
                    ui.errorMessage(event.getMessage());
                } else {
                    ui.errorMessage(task.msg);
                }
                getUserInput();
            }
        } catch (NumberFormatException e) {
            ui.errorMessage(YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN);
            getUserInput();
        } catch (IllegalArgumentException a){
            ui.errorMessage("Pls enter a valid number.");
            getUserInput();
        }

    }

    /**
     * Method to take a credit from the bank.
     * User need to enter the amount.
     */
    private void takeCredit() {
        ui.message("How much credit do you want to take?");
        try {
            int input = Integer.parseInt(getUserInput());
            bank.giveCredit(currentPlayer, input);
        } catch (NumberFormatException e) {
            ui.errorMessage(YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN);
            getUserInput();
        }
    }

    /**
     * Method to repay the debt to the bank.
     * User need to enter the amount.
     */
    private void repayDebt() {
        ui.message("How much debt do you want to repay?");
        try {
            int input = Integer.parseInt(getUserInput());
            bank.reduceDebt(currentPlayer, input);
        } catch (NumberFormatException e) {
            ui.errorMessage(YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN);
            getUserInput();
        }
    }

    private int calculateTravelCosts(City city) {
        int costs = (int) Math.round(city.getDistance(currentPlayer.getCurrentCityENUM()) * 10);
        return costs;
    }

    /**
     * Check if the players traveled to end the current day
     * on day end the bank interest charges will be added and
     * the new candy costs will be calculated.
     */
    private void nextDay() {
        boolean allReady=true;
        for (Player player: players) {
            if(!player.isPlayerReady()){
                allReady=false;
            }
        }
        if (allReady) {
            bank.nextDay();
            currentDay++;
            this.randomCityCosts();
            for (Player player: players) {
                player.setPlayerReady(false);
            }
        }
    }

    /**
     * If more than 30 days passed the game will end and the scoreboard will be shown for each player
     */
    private void endGame() {
        int[] scores=new int[amountOfPlayers];
            for (Player player:players) {
               scores[player.getNumber()-1] =(player.getBalance()+bank.getPlayerAccount(player).getPlayerAccountBalance()) -
                        bank.getPlayerAccount(player).getPlayerDebt();
            }
            ui.printScoreBoard(amountOfPlayers,scores);
            ui.message("Press enter");
            getUserInput();
    }

    /**
     * Create a event based on occurs chance.
     * @return Event will return the occurred event
     * @see Event
     */
    private Event createEvent() {
        int chance = r.nextInt(0, 100);
        if (chance < 30) {
            return new Thief();
        } else if (chance < 60) {
            return new RandomNegativeTravelAmount();
        } else {
            return new Gift();
        }
    }

    /**
     * Method to calculate the candy cost of a city
     */
    private void randomCityCosts() {
        for (City city: City.values()) {
            for (Candy candy : Candy.values()) {
                this.candys[city.getID()][candy.getID()] = r.nextInt(candy.minPrice(), candy.maxPrice());
            }
        }
    }

    /**
     * Test if the player is broke.
     * Check if he has enough money or candy.
     */
    private TaskCompletionState checkIfTravelIsPossible(int[]costs){
        for(int i=0;i<costs.length;i++){
            if(currentPlayer.getBalance()>=costs[i]){
                return new TaskCompletionState("",true) ;
            }
        }
        for (Candy candy:Candy.values()) {
            if(currentPlayer.getItem(candy.getID())!=0){
                return new TaskCompletionState("",true) ;
            }
        }
        if (currentPlayer.getCurrentCityENUM().equals(bank.homecity)){
            Account acc = bank.getPlayerAccount(currentPlayer);
            if(0<acc.getPlayerAccountBalance()){
                return new TaskCompletionState("",true) ;
            }
            for (Candy candy: Candy.values()) {
                if (acc.getItemAmount(candy) > 0){
                    return new TaskCompletionState("",true) ;
                }
            }
        }
        return new TaskCompletionState("Player is broke and cannot travel.",false);
    }
    /**
     * Function for running the game
     */
    public void run() {
        this.randomCityCosts();
        boolean allBroke=false;
        while (this.currentDay < 30&&!allBroke) {
            for (int i=0;i<amountOfPlayers;) {
                currentPlayer=players.get(i);
                if(currentPlayer.isBroke()||currentPlayer.isPlayerReady()){
                    allBroke=true;
                    i++;
                    continue;
                }
                allBroke=false;
            switch (state.name()) {
                case "BANK" -> ui.printBankWindow(bank, currentPlayer);
                case "MARKET" -> ui.printMarketWindow(candys[currentPlayer.getCurrentCityENUM().getID()], currentPlayer);
                default -> ui.printPlayerWindow(currentPlayer,
                        currentPlayer.getCurrentCityENUM() == bank.homecity);
            }
            this.handler(this.getUserInput());
            if(currentPlayer.isPlayerReady()){
                i++;
            }
            }
            this.nextDay();
        }
        this.endGame();
    }

    public static void main(String[] args) {
        Controller controller = new Controller(5);
        controller.run();
    }
}
