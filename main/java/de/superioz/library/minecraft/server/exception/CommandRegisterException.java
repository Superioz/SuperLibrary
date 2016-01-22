package de.superioz.library.minecraft.server.exception;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class CommandRegisterException extends Exception {

    private Reason reason;
    private Class<?> clazz;

    public CommandRegisterException(Reason reason, Class<?> clazz){
        this.reason = reason;
        this.clazz = clazz;
    }

    // -- Intern methods

    public String getReason(){
        return reason.getReason() + " ["+clazz.getSimpleName()+"]";
    }

    public Class<?> getClazz(){
        return clazz;
    }

    public enum Reason {

        CLASS_NOT_COMMAND_CASE("This class is not a command case!"),
        COMMAND_ALREADY_EXISTS("There's a command which already exists!");

        private String string;

        Reason(String string){
            this.string = string;
        }

        public String getReason(){
            return this.string;
        }

    }

}
