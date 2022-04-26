package de.materna.candygame;

/**
 * This class is used to show if a action was a success or
 * not and to hold the reason as a string why it was no success
 */
public class TaskCompletionState {
    /** false= the action failed true= the action was successful**/
    public final boolean isSuccess;
    /** hold the message of the failure or success **/
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
