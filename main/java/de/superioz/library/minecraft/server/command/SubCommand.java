package de.superioz.library.minecraft.server.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {

    String label();

    String[] aliases() default {""};

    String desc() default "";

    int min() default -1;

    int max() default -1;

    String permission() default "";

    String usage() default "";

    CommandWrapper.AllowedSender commandTarget() default CommandWrapper.AllowedSender.PLAYER_AND_CONSOLE;

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Nested {

        String parent();

    }

}
