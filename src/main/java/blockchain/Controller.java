package blockchain;

/**
 * @author Mack_TB
 * @version 1.0.7
 * @since 03/19/2020
 */

public class Controller {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        command.execute();
    }
}
