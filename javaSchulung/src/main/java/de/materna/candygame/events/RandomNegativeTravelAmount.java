package de.materna.candygame.events;

import de.materna.candygame.Player;
import de.materna.candygame.TaskCompletionState;

public class RandomNegativeTravelAmount extends Event {

    @Override
    public TaskCompletionState process(Player player) {
        int max = 3;
        int min = 1;
        setDuration((int)(Math.random()*(max-min)+min));
        setMessage("Oh no! You hurt your leg and needed more time to get to your destination. You needed "+getDuration()+" days more");
        return new TaskCompletionState("", true);
    }
}
