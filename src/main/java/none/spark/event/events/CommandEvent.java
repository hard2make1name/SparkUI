package none.spark.event.events;

import none.spark.event.Event;

public class CommandEvent extends Event {
    public String getName(){
        return "cmd";
    }
    public final String commandName;
    public final String[] args;
    public CommandEvent(String commandName, String[] args){
        this.commandName = commandName;
        this.args = args;
    }
}
