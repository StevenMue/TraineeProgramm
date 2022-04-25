package de.materna.candygame.events;

import de.materna.candygame.Player;
import de.materna.candygame.TaskCompletionState;
import de.materna.candygame.enums.Candy;

public class Thief extends Event{

    public Thief(){
        this.setDuration(0);
    }
    @Override
    public TaskCompletionState process(Player player) {
        int max = 30;
        int min = 1;
        Candy candyType=Candy.getCandy((int)(Math.random()*(Candy.values().length-1)+1));
        int candyAmount = (int)(Math.random()*(max-min)+min);
        TaskCompletionState task = player.reduceItem(candyType, candyAmount);
        setMessage("You notice someone behind you. You look behind you but there is no one.\n"+
                "After a while you notice that some of your candy is missing!\n You lost "+candyAmount+" of "+candyType+"!");
        return task;
    }
}
