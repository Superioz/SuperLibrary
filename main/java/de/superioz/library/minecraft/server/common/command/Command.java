package de.superioz.library.minecraft.server.common.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String label();

    String[] aliases() default {""};

    String desc() default "";

    int min() default -1;

    int max() default -1;

    String permission() default "";

    String usage() default "";

    AllowedCommandSender commandTarget() default AllowedCommandSender.PLAYER_AND_CONSOLE;

    Class<?> tabCompleter() default BukkitTabCompleter.class;

}
