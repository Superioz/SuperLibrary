package de.superioz.library.minecraft.server.command;

import de.superioz.library.java.util.list.ListUtil;
import de.superioz.library.minecraft.server.command.context.CommandContext;
import de.superioz.library.minecraft.server.event.CommandExecutionErrorEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class CommandWrapper {

    private String label, description, permission, usage, parentCommand;
    private List<String> aliases;
    private int min, max;
    private BukkitCommand command;
    private BukkitCommandExecutor commandExecutor;
    private Class<?> tabCompleterClass;
    private BukkitTabCompleter tabCompleter;
    private AllowedSender allowedSender;
    private List<CommandWrapper> subCommands;
    private boolean isChild, isNested = false;

    protected Class parentClass;
    protected Method parentMethod;

    public CommandWrapper(Method method){
        this.parentMethod = method;
        this.parentClass = method.getDeclaringClass();
        this.isChild = !parentMethod.getName().equals(CommandHandler.EXECUTE_METHOD_NAME);
        this.isNested = parentMethod.isAnnotationPresent(SubCommand.Nested.class);
        this.commandExecutor = new BukkitCommandExecutor(parentClass);

        if(this.isNested){
            SubCommand.Nested annotation = this.parentMethod.getAnnotation(SubCommand.Nested.class);
            this.parentCommand = annotation.parent();
        }

        if(this.isChild){
            SubCommand annotation = this.parentMethod.getAnnotation(SubCommand.class);
            this.label = annotation.label();
            this.description = annotation.desc();
            this.usage = annotation.usage();
            this.aliases = Arrays.asList(annotation.aliases());
            this.min = annotation.min();
            this.max = annotation.max();
            this.allowedSender = annotation.commandTarget();
            this.permission = annotation.permission();
        }else{
            Command annotation = (Command) this.parentClass.getAnnotation(Command.class);
            this.label = annotation.label();
            this.description = annotation.desc();
            this.usage = annotation.usage();
            this.aliases = Arrays.asList(annotation.aliases());
            this.min = annotation.min();
            this.max = annotation.max();
            this.allowedSender = annotation.commandTarget();
            this.tabCompleterClass = annotation.tabCompleter();
            this.permission = annotation.permission();
        }

        // Init command
        this.initCommand();
    }

    private void initCommand(){
        this.command = new BukkitCommand(this.label);
        this.command.setExecutor(this.commandExecutor);
        this.command.setTabCompleter(this.tabCompleterClass, this);

        if(!(this.aliases == null)){
            this.command.setAliases(this.aliases);
        }

        this.command.setUsage("/" + this.label);

        if(!this.permission.isEmpty())
            this.command.setPermission(this.permission);

        if(!this.description.isEmpty())
            this.command.setDescription(this.description);
    }

    public void initialize(List<CommandWrapper> subCommands){
        this.subCommands = subCommands;
    }

    public String getLabel(){
        return label;
    }

    public String getDescription(){
        return description;
    }

    public String getPermission(){
        return permission;
    }

    public String getUsage(){
        return usage;
    }

    public List<String> getAliases(){
        return aliases;
    }

    public int getMin(){
        return min;
    }

    public int getMax(){
        return max;
    }

    public BukkitCommandExecutor getCommandExecutor(){
        return commandExecutor;
    }

    public Class<?> getTabCompleterClass(){
        return tabCompleterClass;
    }

    public AllowedSender getAllowedSender(){
        return allowedSender;
    }

    public List<CommandWrapper> getSubCommands(){
        return subCommands;
    }

    public boolean isChild(){
        return isChild;
    }

    public boolean isNested(){
        return isNested;
    }

    public Class getParentClass(){
        return parentClass;
    }

    public Method getParentMethod(){
        return parentMethod;
    }

    public String getParentCommand(){
        return parentCommand;
    }

    public BukkitCommand getBukkitCommand(){
        return command;
    }

    public CommandWrapper getSubCommand(String label){
        if(getSubCommands() == null)
            return null;

        for(CommandWrapper cw : getSubCommands()){
            if(cw.getLabel().equalsIgnoreCase(label)
                    || ListUtil.listContains(cw.getAliases().toArray(), label))
                return cw;
        }
        return null;
    }

    public boolean hasSubCommand(String label){
        return getSubCommand(label) != null;
    }

    // =====================================================================================================

    public class BukkitCommand extends org.bukkit.command.Command {

        public BukkitCommandExecutor executor = null;
        public BukkitTabCompleter completer = null;

        protected BukkitCommand(String name){
            super(name);
        }

        @Override
        public boolean execute(CommandSender commandSender, String s, String[] strings){
            if(this.executor != null){
                executor.onCommand(commandSender, label, strings);
                return true;
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<String> tabComplete(CommandSender commandsender, String label, String[] args){
            if(completer == null)
                return new ArrayList<>();

            return completer.tabComplete(commandsender, label, args);
        }

        public void setExecutor(BukkitCommandExecutor executor){
            this.executor = executor;
        }

        public void setTabCompleter(Class<?> clazz, CommandWrapper wrapper){
            if(clazz == null || clazz == BukkitTabCompleter.class
                    || clazz.getSuperclass() != BukkitTabCompleter.class){
                return;
            }

            try{
                this.completer = (BukkitTabCompleter) clazz.getConstructor(CommandWrapper.class)
                        .newInstance(wrapper);
            }catch(InstantiationException
                    | IllegalAccessException
                    | InvocationTargetException
                    | NoSuchMethodException e){
                e.printStackTrace();
            }
        }

    }

    private class BukkitCommandExecutor {

        protected CommandWrapper command;
        protected Class executorClass;

        public BukkitCommandExecutor(Class clazz){
            this.executorClass = clazz;
        }

        public boolean onCommand(CommandSender commandSender, String label, String[] args){
            // Get command
            command = CommandHandler.getCommand(label);
            CommandContext context = new CommandContext(commandSender, command, args);

            // Check label
            if(label.equalsIgnoreCase(command.getLabel())
                    || ListUtil.listContains(command.getAliases().toArray(), command.getLabel())){

                if(checkCommandContext(context, command)){
                    executeCommand(context);
                    return true;
                }
            }
            return false;
        }

    }

    private boolean executeCommand(CommandContext context){
        try{
            int length = context.getArgumentsLength();
            CommandWrapper command = this;

            if(length >= 1){
                for(int i = 0; i < length; i++){
                    String arg = context.getArgument(i);

                    if(!command.hasSubCommand(arg)){
                        break;
                    }
                    command = command.getSubCommand(arg);

                    // Check command
                    if(!checkCommandContext(context, command)){
                        return false;
                    }
                }
            }

            Method m = command.isChild()
                    ? command.getParentMethod() : getExecuteCommand(getParentClass());
            assert m != null;

            // Check command
            if(!checkCommandContext(context, command)){
                return false;
            }

            if(Modifier.isStatic(m.getModifiers())){
                m.invoke(null, context);
            }else{
                m.invoke(getParentClass().newInstance(), context);
            }

            return true;
        }catch(IllegalAccessException
                | IllegalArgumentException | InvocationTargetException
                | InstantiationException e){
            e.printStackTrace();
            return false;
        }
    }

    private Method getExecuteCommand(Class<?> clazz){
        try{
            return clazz.getDeclaredMethod(CommandHandler.EXECUTE_METHOD_NAME, CommandContext.class);
        }catch(NoSuchMethodException e){
            e.printStackTrace();
        }
        return null;
    }

    private boolean checkCommandContext(CommandContext context, CommandWrapper command){
        CommandSender commandSender = context.getSender();

        // Check arguments length
        if((context.getArgumentsLength() > command.getMax()
                && !(command.getMax() <= 0))
                || context.getArgumentsLength() < command.getMin()){

            // Fire event
            de.superioz.library.main.SuperLibrary
                    .callEvent(new CommandExecutionErrorEvent(CommandExecutionErrorEvent
                            .Reason.INVALID_COMMAND_USAGE, context));
            return false;
        }

        // Check commandSender
        if(commandSender instanceof Player){

            // Does the context allows a player
            if(!context.allows(AllowedSender.PLAYER)){
                de.superioz.library.main.SuperLibrary.callEvent(new CommandExecutionErrorEvent(CommandExecutionErrorEvent
                        .Reason.NOT_ALLOWED_COMMANDSENDER, context));
                return false;
            }

            // Player
            Player player = (Player) context.getSender();
            String permission = command.getPermission();

            if(permission != null
                    && !permission.isEmpty()){
                if(!player.hasPermission(permission)){

                    // Fire event
                    de.superioz.library.main.SuperLibrary.callEvent(new CommandExecutionErrorEvent(CommandExecutionErrorEvent
                            .Reason.NO_PERMISSIONS, context));
                    return false;
                }
            }

            return true;
        }else{

            // Does the context allows the console
            if(!context.allows(AllowedSender.CONSOLE)){
                de.superioz.library.main.SuperLibrary.callEvent(new CommandExecutionErrorEvent(CommandExecutionErrorEvent
                        .Reason.NOT_ALLOWED_COMMANDSENDER, context));
                return false;
            }

            return true;
        }
    }

    public enum AllowedSender {

        PLAYER,
        CONSOLE,
        PLAYER_AND_CONSOLE

    }

}
