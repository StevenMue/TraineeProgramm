package de.materna.candygame.events;

import de.materna.candygame.Player;
import de.materna.candygame.TaskCompletionState;

public abstract class Event {
    private String msg;
    private int duration;

    public abstract TaskCompletionState process(Player player );

    void setMessage(String msg){
        this.msg=msg;
    }
    void setDuration(int duration){
        this.duration=duration;
    }
    public String getMessage(){
        return this.msg;
    }
    public int getDuration(){
        return this.duration;
    }


}
