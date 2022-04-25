package de.materna.candygame.events;

import de.materna.candygame.Player;
import de.materna.candygame.TaskCompletionState;
import de.materna.candygame.enums.Candy;

public class Gift extends Event {

    public Gift(){
        this.setDuration(0);
    }
    @Override
    public TaskCompletionState process(Player player) {
        int max = 100;
        int min = 1;
        Candy candyType=Candy.getCandy((int)(Math.random()*(Candy.values().length-1)+1));
        int candyAmount = (int)(Math.random()*(max-min)+min);
        TaskCompletionState task = player.addItem(candyType, candyAmount);
        setMessage("Congratulations they came across a bunch["+candyAmount+"] of ["+candyType+"]!");
        return task;
    }
}
