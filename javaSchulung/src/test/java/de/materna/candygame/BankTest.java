package de.materna.candygame;

import de.materna.candygame.enums.Candy;
import de.materna.candygame.enums.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    Player player;
    Bank bank;
    ArrayList<Player> players;
    int[] candy = new int[Candy.values().length];
    Account account;

    @BeforeEach
    void setUp() {
        player = new Player(0,2000, City.CENTRAL);
        players = new ArrayList<>();
        players.add(player);
        bank = new Bank(players, City.BRONX);
        account = bank.getPlayerAccount(player);
    }

    @Test
    void nextDay() {
        bank.nextDay();
        assertEquals(0, bank.getPlayerAccount(player).getPlayerAccountBalance());
        assertEquals(0, bank.getPlayerAccount(player).getPlayerDebt());
        bank.storeMoney(player, 100);
        bank.giveCredit(player, 200);
        bank.nextDay();
        assertEquals(105, bank.getPlayerAccount(player).getPlayerAccountBalance());
        assertEquals(220, bank.getPlayerAccount(player).getPlayerDebt());
    }

    @Test
    void takeItems() {
        TaskCompletionState task = bank.takeItems(player, Candy.BONBON, 10);
        assertFalse(task.isSuccess);
        assertEquals(0, bank.getPlayerAccount(player).getItemAmount(Candy.BONBON.getID()));
        assertEquals(0, player.getItem((Candy.BONBON.getID())));

        player.addItem(Candy.BONBON, 10);
        bank.storeItems(player, Candy.BONBON, 10);
        task = bank.takeItems(player, Candy.BONBON, 10);
        assertTrue(task.isSuccess);
        assertEquals(0, bank.getPlayerAccount(player).getItemAmount(Candy.BONBON.getID()));
        assertEquals(10, player.getItem((Candy.BONBON.getID())));

        bank.storeItems(player, Candy.BONBON, 10);
        player.addItem(Candy.BONBON, 100);
        task = bank.takeItems(player, Candy.BONBON, 10);
        assertFalse(task.isSuccess);
        assertEquals(100, player.getItem(Candy.BONBON.getID()));
        assertEquals(10, bank.getPlayerAccount(player).getItemAmount(Candy.BONBON.getID()));


        task = bank.takeItems(player, Candy.BONBON, -10);
        assertFalse(task.isSuccess);
        assertEquals(100, player.getItem(Candy.BONBON.getID()));
        assertEquals(10, bank.getPlayerAccount(player).getItemAmount(Candy.BONBON.getID()));

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> bank.takeItems(new Player(0,10, City.BRONX), Candy.BONBON, 10),
                "Expected bank.takeItems to throw \"IllegalStateException\", but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Player has no bank account."));

    }

    @Test
    void storeItems() {
        player.addItem(Candy.BONBON, 10);
        TaskCompletionState task = bank.storeItems(player, Candy.BONBON, 10);
        assertTrue(task.isSuccess);
        assertEquals(10, bank.getPlayerAccount(player).getItemAmount(Candy.BONBON.getID()));
        assertEquals(0, player.getItem((Candy.BONBON.getID())));

        task = bank.storeItems(player, Candy.BONBON, 10);
        assertFalse(task.isSuccess);
        assertEquals(10, bank.getPlayerAccount(player).getItemAmount(Candy.BONBON.getID()));
        assertEquals(0, player.getItem((Candy.BONBON.getID())));

        task = bank.storeItems(player, Candy.BONBON, -10);
        assertFalse(task.isSuccess);
        assertEquals(10, bank.getPlayerAccount(player).getItemAmount(Candy.BONBON.getID()));
        assertEquals(0, player.getItem((Candy.BONBON.getID())));

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> bank.storeItems(new Player(0,10, City.BRONX), Candy.BONBON, 10),
                "Expected bank.storeItems to throw \"IllegalStateException\", but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Player has no bank account."));
    }

    @Test
    void storeMoney() {
        player = new Player(0,0, City.BRONX);
        players.remove(0);
        players.add(player);
        bank = new Bank(players, City.BRONX);
        TaskCompletionState task = bank.storeMoney(player, 100);
        assertFalse(task.isSuccess);
        assertEquals(0, player.getBalance());
        assertEquals(0, bank.getPlayerAccount(player).getPlayerAccountBalance());

        task = bank.storeMoney(player, -100);
        assertFalse(task.isSuccess);
        assertEquals(0, player.getBalance());
        assertEquals(0, bank.getPlayerAccount(player).getPlayerAccountBalance());

        player.addMoney(100);
        task = bank.storeMoney(player, 100);
        assertTrue(task.isSuccess);
        assertEquals(0, player.getBalance());
        assertEquals(100, bank.getPlayerAccount(player).getPlayerAccountBalance());

        player.addMoney(100);
        task = bank.storeMoney(player, 1000);
        assertFalse(task.isSuccess);
        assertEquals(100, player.getBalance());
        assertEquals(100, bank.getPlayerAccount(player).getPlayerAccountBalance());

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> bank.storeMoney(new Player(0,10, City.BRONX), 100),
                "Expected bank.storeMoney to throw \"IllegalStateException\", but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Player has no bank account."));
    }

    @Test
    void takeMoney() {
        TaskCompletionState task = bank.takeMoney(player, 100);
        assertFalse(task.isSuccess);
        assertEquals(2000, player.getBalance());
        assertEquals(0, bank.getPlayerAccount(player).getPlayerAccountBalance());

        task = bank.storeMoney(player, -100);
        assertFalse(task.isSuccess);
        assertEquals(2000, player.getBalance());
        assertEquals(0, bank.getPlayerAccount(player).getPlayerAccountBalance());

        bank.storeMoney(player, 500);
        task = bank.takeMoney(player, 300);
        assertTrue(task.isSuccess);
        assertEquals(1800, player.getBalance());
        assertEquals(200, bank.getPlayerAccount(player).getPlayerAccountBalance());

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> bank.takeMoney(new Player(0,10, City.BRONX), 100),
                "Expected bank.takeMoney to throw \"IllegalStateException\", but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Player has no bank account."));
    }

    @Test
    void giveCredit() {

        TaskCompletionState task = bank.giveCredit(player, 1000);
        assertTrue(task.isSuccess);
        assertEquals(3000, player.getBalance());
        assertEquals(1000, bank.getPlayerAccount(player).getPlayerDebt());
        task = bank.giveCredit(player, 1000);
        assertFalse(task.isSuccess);
        assertEquals(3000, player.getBalance());
        assertEquals(1000, bank.getPlayerAccount(player).getPlayerDebt());

        bank.reduceDebt(player, 1000);
        task = bank.giveCredit(player, -1000);
        assertFalse(task.isSuccess);
        assertEquals(2000, player.getBalance());
        assertEquals(0, bank.getPlayerAccount(player).getPlayerDebt());

        task = bank.giveCredit(player, 800);
        assertTrue(task.isSuccess);
        assertEquals(2800, player.getBalance());
        assertEquals(800, bank.getPlayerAccount(player).getPlayerDebt());

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> bank.giveCredit(new Player(0,10, City.BRONX), 100),
                "Expected bank.giveCredit to throw \"IllegalStateException\", but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Player has no bank account."));
    }

    @Test
    void reduceDebt() {

        TaskCompletionState task = bank.reduceDebt(player, 1000);
        assertTrue(task.isSuccess);
        assertEquals(2000, player.getBalance());
        assertEquals(0, bank.getPlayerAccount(player).getPlayerDebt());
        bank.giveCredit(player,1000);

        task = bank.reduceDebt(player, 1000);
        assertTrue(task.isSuccess);
        assertEquals(2000, player.getBalance());
        assertEquals(0, bank.getPlayerAccount(player).getPlayerDebt());

        task = bank.reduceDebt(player, -1000);
        assertFalse(task.isSuccess);
        assertEquals(2000, player.getBalance());
        assertEquals(0, bank.getPlayerAccount(player).getPlayerDebt());

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> bank.reduceDebt(new Player(0,10, City.BRONX), 100),
                "Expected bank.reduceDebt to throw \"IllegalStateException\", but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Player has no bank account."));
    }

    @Test
    void createKonto() {
        assertDoesNotThrow(
                () -> bank.createAccount(new Player(0,10, City.BRONX)),
                "Expected bank.reduceDebt to throw \"IllegalStateException\", but it didn't"
        );
    }

    @Test
    void getPlayerAccount() {
        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> bank.getPlayerAccount(new Player(0,10, City.BRONX)),
                "Expected bank.getPlayerAccount to throw \"IllegalStateException\", but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Player has no bank account."));
    }
}