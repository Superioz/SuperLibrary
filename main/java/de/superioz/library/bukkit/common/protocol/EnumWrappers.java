package de.superioz.library.bukkit.common.protocol;

/**
 * Created on 02.04.2016.
 */
public class EnumWrappers {

	public enum PlayerInfoAction {

		ADD_PLAYER,
		UPDATE_GAME_MODE,
		UPDATE_LATENCY,
		UPDATE_DISPLAY_NAME,
		REMOVE_PLAYER

	}

	public enum NativeGameMode {

		NOT_SET,
		SURVIVAL,
		CREATIVE,
		ADVENTURE,
		SPECTATOR,

	}

	public enum ItemSlot {

		MAINHAND,
		OFFHAND,
		FEET,
		LEGS,
		CHEST,
		HEAD

	}

	public enum TitleAction {

		TITLE,
		SUBTITLE,
		TIMES,
		CLEAR,
		RESET

	}

	public enum EntityUseAction {

		INTERACT,
		ATTACK,
		INTERACT_AT

	}

	public enum WorldBorderAction {

		SET_SIZE,
		LERP_SIZE,
		SET_CENTER,
		INITIALIZE,
		SET_WARNING_TIME,
		SET_WARNING_BLOCKS

	}

	public enum BossbarColor {

		PINK,
		BLUE,
		RED,
		GREEN,
		YELLOW,
		PURPLE,
		WHITE;

	}

	public enum BossbarStyle {

		PROGRESS,
		NOTCHED_6,
		NOTCHED_10,
		NOTCHED_12,
		NOTCHED_20;

	}

	public enum BossbarAction {

		ADD,
		REMOVE,
		UPDATE_PCT,
		UPDATE_NAME,
		UPDATE_STYLE,
		UPDATE_PROPERTIES;

	}

}
