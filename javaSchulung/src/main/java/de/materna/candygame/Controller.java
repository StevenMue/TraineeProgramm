package de.materna.candygame;

import de.materna.candygame.enums.Candy;
import de.materna.candygame.enums.CityENUM;
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
    public static final String YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN = "You dont entered a number. Try again";
    private int currentDay = 0;
    ArrayList<Player> players = new ArrayList<>();
    private Bank bank;
    boolean returnFlag = false;
    private UIConsoleGenerator ui = new UIConsoleGenerator();
    PlayerStates state;

    //TODO move to city
    Random r;
    int[] candys = new int[Candy.values().length];
    StringBuilder sb = new StringBuilder();

    private Controller() {
    }

    private Controller(int amountOfPlayer) {
        for (int i = 0; i < amountOfPlayer; i++) {
            players.add(new Player(2000, CityENUM.CENTRAL));
        }
        this.state = PlayerStates.IDLE;
        bank = new Bank(players, CityENUM.BRONX);
        r = new Random();
    }

    private String getUserInput() {
        return new Scanner(System.in).nextLine();
    }

    private void handler(String input) {

        //TODO complete switch, refector names "want..."
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

    private void marketOrBank() {
        if (state == PlayerStates.IDLE) {
            ui.printMarketWindow(candys, players.get(0));
            state = PlayerStates.MARKET;
        } else if (state == PlayerStates.BANK) {
            storeMoney();
        } else {
            ui.message("Pls enter a valid key.");
            handler(getUserInput());
        }
    }

    private void bankOrBuy() {
        if (state == PlayerStates.IDLE && players.get(0).getCurrentCityENUM() == bank.homecity) {
            ui.printBankWindow(bank, players.get(0));
            state = PlayerStates.BANK;
        } else if (state == PlayerStates.MARKET) {
            buy();
        } else {
            ui.message("Pls enter a valid key.");
        }
    }

    private void wantToStoreItems() {
        if (state == PlayerStates.BANK) {
            ui.printBankWindow(bank, players.get(0));
            storeItems();
        } else {
            ui.message("Pls enter a valid key.");
        }
    }

    private void wantToTakeMoney() {
        if (state == PlayerStates.BANK) {
            ui.printBankWindow(bank, players.get(0));
            takeMoney();
        } else {
            ui.message("Pls enter a valid key.");
        }
    }

    private void wantToTakeCredit() {
        if (state == PlayerStates.BANK) {
            ui.printBankWindow(bank, players.get(0));
            takeCredit();
        } else {
            ui.message("Pls enter a valid key.");
        }
    }

    private void wantToTakeItems() {
        if (state == PlayerStates.BANK) {
            ui.printBankWindow(bank, players.get(0));
            takeItems();
        } else {
            ui.message("Pls enter a valid key.");
        }
    }

    private void wantToRepayDebt() {
        if (state == PlayerStates.BANK) {
            ui.printBankWindow(bank, players.get(0));
            repayDebt();
        } else {
            ui.message("Pls enter a valid key.");
        }
    }

    //TODO make a new class market to handel that
    private void buy() {
        ui.message("What do you want to buy? : ");

        try {
            int itemID = Integer.parseInt(getUserInput());
            ui.message("How much do you wish to buy? You can buy " + players.get(0).spaceLeft() + " : ");
            int amount = Integer.parseInt(getUserInput());
            if (players.get(0).spaceLeft() < amount) {
                ui.errorMessage("You backpack cant hold so much! You have only " + players.get(0).spaceLeft() + " space left!");
                getUserInput();
                returnFlag = true;
                return;
            }
            TaskCompletionState task = players.get(0).reduceMoney(amount * candys[itemID]);

            if (task.isSuccess) {
                task = players.get(0).addItem(Candy.getCandy(itemID), amount);
                if (!task.isSuccess) {
                    ui.errorMessage(task.msg);
                    getUserInput();
                }
            } else {
                ui.errorMessage(task.msg);
                getUserInput();
            }
            returnFlag = true;
        } catch (NumberFormatException e) {
            ui.errorMessage(YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN);
            getUserInput();
            returnFlag = true;
        } catch (ArrayIndexOutOfBoundsException a) {
            ui.errorMessage("A item with that ID doesn't exits");
            getUserInput();
        }

    }

    private void sell() {
        ui.message("What do you want to sell? : ");
        try {
            int itemID = Integer.parseInt(getUserInput());
            ui.message("How much do you wish to sell? You have " + players.get(0).getItem(itemID) + " : ");
            int amount = Integer.parseInt(getUserInput());
            TaskCompletionState task = players.get(0).reduceItem(Candy.getCandy(itemID), amount);
            if (task.isSuccess) {
                players.get(0).addMoney(amount * candys[itemID]);
            } else {
                ui.errorMessage(task.msg);
                getUserInput();
            }
            returnFlag = true;
        } catch (NumberFormatException e) {
            ui.errorMessage(YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN);
            getUserInput();
            returnFlag = true;
        } catch (ArrayIndexOutOfBoundsException a) {
            ui.errorMessage("A item with that ID doesn't exits");
            getUserInput();
        }
    }

    private void storeItems() {
        ui.message("What do you want to store?");
        try {
            int itemID = Integer.parseInt(getUserInput());
            ui.message("How much do you wish to store? You have " + players.get(0).getItem(itemID));
            int amount = Integer.parseInt(getUserInput());
            TaskCompletionState task = bank.storeItems(players.get(0), Candy.getCandy(itemID), amount);
            if (!task.isSuccess) {
                ui.errorMessage(task.msg);
                getUserInput();
            }
            returnFlag = true;
        } catch (NumberFormatException e) {
            ui.errorMessage(YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN);
            getUserInput();
            returnFlag = true;
        } catch (ArrayIndexOutOfBoundsException a) {
            ui.errorMessage("A item with that ID doesn't exits");
            getUserInput();
        }
    }

    private void takeItems() {
        ui.message("What do you want to take?");
        TaskCompletionState task;

        try {
            int itemID = Integer.parseInt(getUserInput());
            ui.message("How much do you wish to take?");
            int amount = Integer.parseInt(getUserInput());
            if (players.get(0).spaceLeft() >= amount) {
                ui.message("You dont have enough left space!");
                task = bank.takeItems(players.get(0), Candy.getCandy(itemID), amount);
                if (!task.isSuccess) {
                    ui.errorMessage(task.msg);
                    getUserInput();
                }
            }
            returnFlag = true;
        } catch (NumberFormatException e) {
            ui.errorMessage(YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN);
            returnFlag = true;
        } catch (IllegalArgumentException a) {
            ui.errorMessage("A candy with that ID doesn't exits");
            getUserInput();
        }
    }

    private void storeMoney() {
        try {
            ui.message("How much do you wish to store?");
            int amount = Integer.parseInt(getUserInput());
            TaskCompletionState task = bank.storeMoney(players.get(0), amount);
            if (!task.isSuccess) {
                ui.errorMessage(task.msg);
                getUserInput();
            }
            returnFlag = true;
        } catch (NumberFormatException e) {
            ui.errorMessage(YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN);
            getUserInput();
            returnFlag = true;
        }
    }

    private void takeMoney() {
        try {
            ui.message("How much do you wish to take?");
            int amount = Integer.parseInt(getUserInput());
            TaskCompletionState task = bank.takeMoney(players.get(0), amount);
            if (!task.isSuccess) {
                ui.errorMessage(task.msg);
                getUserInput();
            }
            returnFlag = true;
        } catch (NumberFormatException e) {
            ui.errorMessage(YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN);
            getUserInput();
            returnFlag = true;
        }
    }

    private void goBack() {
        returnFlag = true;
        state = PlayerStates.IDLE;
    }

    private void leave() {
        currentDay = 31;
    }

    //TODO create event

    private void travel() {
        int[] costs = new int[CityENUM.values().length];

        for (CityENUM city : CityENUM.values()) {
            costs[city.getID()] = calculateTravelCosts(city);
        }
        ui.printTravelTargets(costs, players.get(0), currentDay);
        try {
            String input;
                input = getUserInput();
                if (input.equals("r")) {
                    returnFlag = true;
                    return;
                }
            //TODO optimize for more player
            Player player = players.get(0);
            if(Integer.parseInt(input)>(CityENUM.values().length-1)||Integer.parseInt(input)<0){
                throw new IllegalArgumentException();
            }
            player.travel(CityENUM.getCity(Integer.parseInt(input)), costs[Integer.parseInt(input)]);
            if (Math.random() <= 0.1) {
                Event event = createEvent();
                TaskCompletionState task = event.process(player);
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

    private void takeCredit() {
        ui.message("How much credit do you want to take?");
        try {
            int input = Integer.parseInt(getUserInput());
            bank.giveCredit(players.get(0), input);
            returnFlag = true;
        } catch (NumberFormatException e) {
            ui.errorMessage(YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN);
            getUserInput();
        }
    }

    private void repayDebt() {
        ui.message("How much debt do you want to repay?");
        try {
            int input = Integer.parseInt(getUserInput());
            bank.reduceDebt(players.get(0), input);
            returnFlag = true;
        } catch (NumberFormatException e) {
            ui.errorMessage(YOU_DONT_ENTERED_A_NUMBER_TRY_AGAIN);
            getUserInput();
        }
    }

    //TODO optimize for more player
    //TODO move to City
    private int calculateTravelCosts(CityENUM city) {
        Player player = players.get(0);
        int costs = (int) Math.round(city.getDistance(player.getCurrentCityENUM()) * 10);
        return costs;
    }

    private void nextDay() {
        if (!returnFlag) {
            bank.nextDay();
            currentDay++;
            this.randomCityCosts();
        }
        returnFlag = false;
    }

    private void endGame() {
        if (currentDay == 30) {
            ui.printScoreBoard(players.get(0).getBalance() - bank.getPlayerAccount(players.get(0)).getPlayerDebt());
            ui.message("Press enter");
            getUserInput();
        }
    }

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

    //TODO move to City
    private void randomCityCosts() {
        for (Candy candy : Candy.values()) {
            this.candys[candy.getNr()] = r.nextInt(candy.minPrice(), candy.maxPrice());
        }
    }

    public void run() {
        this.randomCityCosts();
        while (this.currentDay < 30) {
            switch (state.name()) {
                case "BANK" -> ui.printBankWindow(bank, players.get(0));
                case "MARKET" -> ui.printMarketWindow(candys, players.get(0));
                default -> ui.printPlayerWindow(players.get(0), players.get(0).getCurrentCityENUM() == bank.homecity);
            }
            this.handler(this.getUserInput());
            this.nextDay();
        }
        this.endGame();
    }

    public static void main(String[] args) {
        Controller controller = new Controller(1);
        controller.run();
    }
}
