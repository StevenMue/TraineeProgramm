package de.materna.candygame;

public class TaskCompletionState {
    public final boolean isSuccess;
    public final String msg;

    private TaskCompletionState(){
        this.msg="";
        this.isSuccess=false;
    }

    public TaskCompletionState(String msg, boolean isSuccess){
        this.msg=msg;
        this.isSuccess=isSuccess;
    }
}
