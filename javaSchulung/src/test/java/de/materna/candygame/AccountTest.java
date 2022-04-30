package de.materna.candygame;

import de.materna.candygame.enums.Candy;
import de.materna.candygame.enums.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    Player player;
    Bank bank;
    ArrayList<Player> players;
    int[] candy = new int [Candy.values().length];
    Account account;

    @BeforeEach
    void setUp() {
        player= new Player(0,2000, City.CENTRAL);
        players = new ArrayList<>();
        players.add(player);
        bank = new Bank(players, City.BRONX);
        account = bank.getPlayerAccount(player);
    }

    @Test
    void getPlayerDebt() {
       assertEquals(0, bank.getPlayerAccount(player).getPlayerDebt());
    }

    @Test
    void increaseDebt() {
        TaskCompletionState task = bank.giveCredit(player,1000);
        assertTrue(task.isSuccess);
    }

    @Test
    void reduceDebt() {
        bank.getPlayerAccount(player).increaseDebt(1100);
        TaskCompletionState task = bank.reduceDebt(player,1000);
        assertEquals(100,bank.getPlayerAccount(player).getPlayerDebt());
        task = bank.reduceDebt(player,1000);
        assertEquals(0 ,bank.getPlayerAccount(player).getPlayerDebt());
    }

    @Test
    void getPlayerAccountBalance() {
        bank.getPlayerAccount(player).getPlayerAccountBalance();
        assertEquals(0,bank.getPlayerAccount(player).getPlayerAccountBalance());
    }

    @Test
    void increaseAccountBalance() {
        bank.getPlayerAccount(player).increaseAccountBalance(1000);
        assertEquals(1000,bank.getPlayerAccount(player).getPlayerAccountBalance());
    }

    @Test
    void reduceAccountBalance() {
        bank.getPlayerAccount(player).increaseAccountBalance(1000);
        bank.getPlayerAccount(player).reduceAccountBalance(100);
        assertEquals(900,bank.getPlayerAccount(player).getPlayerAccountBalance());
        bank.getPlayerAccount(player).reduceAccountBalance(1000);
        assertEquals(900,bank.getPlayerAccount(player).getPlayerAccountBalance());
    }

    @Test
    void getPlayer() {
        assertEquals(bank.getPlayerAccount(player).getPlayer(), player);
        assertNotEquals(bank.getPlayerAccount(player).getPlayer(), new Player(0,1500, City.BRONX));
    }

    @Test
    void addItem() {

    }

    @Test
    void removeItem() {
    }

    @Test
    void getItemAmount() {
    }
}