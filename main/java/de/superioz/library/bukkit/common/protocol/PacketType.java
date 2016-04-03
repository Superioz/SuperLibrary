package de.superioz.library.bukkit.common.protocol;

import de.superioz.library.java.util.WrappedFieldArray;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 31.03.2016.
 */
@Getter
public class PacketType implements Comparable<PacketType> {

	private String className;
	private Sender sender;
	private int id;

	private static List<PacketType> packetLookup = new ArrayList<>();

	static {
		registerPacketClass(new Play.Server());
		registerPacketClass(new Play.Client());
	}

	/**
	 * The constructor of the packet type
	 *
	 * @param sender    The packet sender
	 * @param className The packet class name
	 * @param id        The packet id
	 */
	public PacketType(Sender sender, String className, int id){
		this.sender = sender;
		this.id = id;

		String packetType = "PacketPlay" + ((sender == Sender.CLIENT) ? "In" : "Out");
		this.className = packetType + className;
	}

	/**
	 * Lookup a packet with id
	 * @param id The id
	 * @return The packet
	 */
	public static PacketType lookup(int id){
		for(PacketType type : packetLookup)
			if(type.getId() == id) return type;
		throw new IllegalArgumentException("Couldn't find packet with ID='"+id+"'.");
	}

	/**
	 * Lookup a packet with class name
	 * @param className The class name
	 * @return The packet
	 */
	public static PacketType lookup(String className){
		for(PacketType type : packetLookup)
			if(type.getClassName().equals(className)) return type;

		throw new IllegalArgumentException("Couldn't find packet with Name='"+className+"'.");
	}

	/**
	 * Registers given class instance into the packet lookup
	 *
	 * @param classInstance The instance
	 */
	private static void registerPacketClass(Object classInstance){
		WrappedFieldArray<PacketType> array = new WrappedFieldArray<>(classInstance, PacketType.class);

		try{
			for(Field f : array.getFields()){
				packetLookup.add((PacketType) f.get(null));
			}
		}
		catch(IllegalAccessException e){
			e.printStackTrace();
		}
	}

	@Override
	public int compareTo(PacketType o){
		return o.getClassName().compareTo(getClassName());
	}

	public static class Play {

		public static class Server {

			private static final PacketType.Sender SENDER;
			public static final PacketType SPAWN_ENTITY;
			public static final PacketType SPAWN_ENTITY_EXPERIENCE_ORB;
			public static final PacketType SPAWN_ENTITY_WEATHER;
			public static final PacketType SPAWN_ENTITY_LIVING;
			public static final PacketType SPAWN_ENTITY_PAINTING;
			public static final PacketType NAMED_ENTITY_SPAWN;
			public static final PacketType ANIMATION;
			public static final PacketType STATISTIC;
			public static final PacketType BLOCK_BREAK_ANIMATION;
			public static final PacketType TILE_ENTITY_DATA;
			public static final PacketType BLOCK_ACTION;
			public static final PacketType BLOCK_CHANGE;
			public static final PacketType BOSS;
			public static final PacketType SERVER_DIFFICULTY;
			public static final PacketType TAB_COMPLETE;
			public static final PacketType CHAT;
			public static final PacketType MULTI_BLOCK_CHANGE;
			public static final PacketType TRANSACTION;
			public static final PacketType CLOSE_WINDOW;
			public static final PacketType OPEN_WINDOW;
			public static final PacketType WINDOW_ITEMS;
			public static final PacketType WINDOW_DATA;
			public static final PacketType SET_SLOT;
			public static final PacketType SET_COOLDOWN;
			public static final PacketType CUSTOM_PAYLOAD;
			public static final PacketType CUSTOM_SOUND_EFFECT;
			public static final PacketType KICK_DISCONNECT;
			public static final PacketType ENTITY_STATUS;
			public static final PacketType EXPLOSION;
			public static final PacketType UNLOAD_CHUNK;
			public static final PacketType GAME_STATE_CHANGE;
			public static final PacketType KEEP_ALIVE;
			public static final PacketType MAP_CHUNK;
			public static final PacketType WORLD_EVENT;
			public static final PacketType WORLD_PARTICLES;
			public static final PacketType LOGIN;
			public static final PacketType MAP;
			public static final PacketType REL_ENTITY_MOVE;
			public static final PacketType REL_ENTITY_MOVE_LOOK;
			public static final PacketType ENTITY_LOOK;
			public static final PacketType ENTITY;
			public static final PacketType VEHICLE_MOVE;
			public static final PacketType OPEN_SIGN_EDITOR;
			public static final PacketType ABILITIES;
			public static final PacketType COMBAT_EVENT;
			public static final PacketType PLAYER_INFO;
			public static final PacketType POSITION;
			public static final PacketType BED;
			public static final PacketType ENTITY_DESTROY;
			public static final PacketType REMOVE_ENTITY_EFFECT;
			public static final PacketType RESOURCE_PACK_SEND;
			public static final PacketType RESPAWN;
			public static final PacketType ENTITY_HEAD_ROTATION;
			public static final PacketType WORLD_BORDER;
			public static final PacketType CAMERA;
			public static final PacketType HELD_ITEM_SLOT;
			public static final PacketType SCOREBOARD_DISPLAY_OBJECTIVE;
			public static final PacketType ENTITY_METADATA;
			public static final PacketType ATTACH_ENTITY;
			public static final PacketType ENTITY_VELOCITY;
			public static final PacketType ENTITY_EQUIPMENT;
			public static final PacketType EXPERIENCE;
			public static final PacketType UPDATE_HEALTH;
			public static final PacketType SCOREBOARD_OBJECTIVE;
			public static final PacketType MOUNT;
			public static final PacketType SCOREBOARD_TEAM;
			public static final PacketType SCOREBOARD_SCORE;
			public static final PacketType SPAWN_POSITION;
			public static final PacketType UPDATE_TIME;
			public static final PacketType TITLE;
			public static final PacketType UPDATE_SIGN;
			public static final PacketType NAMED_SOUND_EFFECT;
			public static final PacketType PLAYER_LIST_HEADER_FOOTER;
			public static final PacketType COLLECT;
			public static final PacketType ENTITY_TELEPORT;
			public static final PacketType UPDATE_ATTRIBUTES;
			public static final PacketType ENTITY_EFFECT;

			static{
				SENDER = Sender.SERVER;
				SPAWN_ENTITY = new PacketType(SENDER, "SpawnEntity", 0);
				SPAWN_ENTITY_EXPERIENCE_ORB = new PacketType(SENDER, "SpawnEntityExperienceOrb", 1);
				SPAWN_ENTITY_WEATHER = new PacketType(SENDER, "SpawnEntityWeather", 2);
				SPAWN_ENTITY_LIVING = new PacketType(SENDER, "SpawnEntityLiving", 3);
				SPAWN_ENTITY_PAINTING = new PacketType(SENDER, "SpawnEntityPainting", 4);
				NAMED_ENTITY_SPAWN = new PacketType(SENDER, "NamedEntitySpawn", 5);
				ANIMATION = new PacketType(SENDER, "Animation", 6);
				STATISTIC = new PacketType(SENDER, "Statistic", 7);
				BLOCK_BREAK_ANIMATION = new PacketType(SENDER, "BlockBreakAnimation", 8);
				TILE_ENTITY_DATA = new PacketType(SENDER, "TileEntityData", 9);
				BLOCK_ACTION = new PacketType(SENDER, "BlockAction", 10);
				BLOCK_CHANGE = new PacketType(SENDER, "BlockChange", 11);
				BOSS = new PacketType(SENDER, "Boss", 12);
				SERVER_DIFFICULTY = new PacketType(SENDER, "ServerDifficulty", 13);
				TAB_COMPLETE = new PacketType(SENDER, "TabComplete", 14);
				CHAT = new PacketType(SENDER, "Chat", 15);
				MULTI_BLOCK_CHANGE = new PacketType(SENDER, "MultiBlockChange", 16);
				TRANSACTION = new PacketType(SENDER, "Transaction", 17);
				CLOSE_WINDOW = new PacketType(SENDER, "CloseWindow", 18);
				OPEN_WINDOW = new PacketType(SENDER, "OpenWindow", 19);
				WINDOW_ITEMS = new PacketType(SENDER, "WindowItems", 20);
				WINDOW_DATA = new PacketType(SENDER, "WindowData", 21);
				SET_SLOT = new PacketType(SENDER, "SetSlot", 22);
				SET_COOLDOWN = new PacketType(SENDER, "SetCooldown", 23);
				CUSTOM_PAYLOAD = new PacketType(SENDER, "CustomPayload", 24);
				CUSTOM_SOUND_EFFECT = new PacketType(SENDER, "CustomSoundEffect", 25);
				KICK_DISCONNECT = new PacketType(SENDER, "KickDisconnect", 26);
				ENTITY_STATUS = new PacketType(SENDER, "EntityStatus", 27);
				EXPLOSION = new PacketType(SENDER, "Explosion", 28);
				UNLOAD_CHUNK = new PacketType(SENDER, "UnloadChunk", 29);
				GAME_STATE_CHANGE = new PacketType(SENDER, "GameStateChange", 30);
				KEEP_ALIVE = new PacketType(SENDER, "KeepAlive", 31);
				MAP_CHUNK = new PacketType(SENDER, "MapChunk", 32);
				WORLD_EVENT = new PacketType(SENDER, "WorldEvent", 33);
				WORLD_PARTICLES = new PacketType(SENDER, "WorldParticles", 34);
				LOGIN = new PacketType(SENDER, "Login", 35);
				MAP = new PacketType(SENDER, "Map", 36);
				REL_ENTITY_MOVE = new PacketType(SENDER, "RelEntityMove", 37);
				REL_ENTITY_MOVE_LOOK = new PacketType(SENDER, "RelEntityMoveLook", 38);
				ENTITY_LOOK = new PacketType(SENDER, "EntityLook", 39);
				ENTITY = new PacketType(SENDER, "Entity", 40);
				VEHICLE_MOVE = new PacketType(SENDER, "VehicleMove", 41);
				OPEN_SIGN_EDITOR = new PacketType(SENDER, "OpenSignEditor", 42);
				ABILITIES = new PacketType(SENDER, "Abilities", 43);
				COMBAT_EVENT = new PacketType(SENDER, "CombatEvent", 44);
				PLAYER_INFO = new PacketType(SENDER, "PlayerInfo", 45);
				POSITION = new PacketType(SENDER, "Position", 46);
				BED = new PacketType(SENDER, "Bed", 47);
				ENTITY_DESTROY = new PacketType(SENDER, "EntityDestroy", 48);
				REMOVE_ENTITY_EFFECT = new PacketType(SENDER, "RemoveEntityEffect", 49);
				RESOURCE_PACK_SEND = new PacketType(SENDER, "ResourcePackSend", 50);
				RESPAWN = new PacketType(SENDER, "Respawn", 51);
				ENTITY_HEAD_ROTATION = new PacketType(SENDER, "EntityHeadRotation", 52);
				WORLD_BORDER = new PacketType(SENDER, "WorldBorder", 53);
				CAMERA = new PacketType(SENDER, "Camera", 54);
				HELD_ITEM_SLOT = new PacketType(SENDER, "HeldItemSlot", 55);
				SCOREBOARD_DISPLAY_OBJECTIVE = new PacketType(SENDER, "ScoreboardDisplayObjective", 56);
				ENTITY_METADATA = new PacketType(SENDER, "EntityMetadata", 57);
				ATTACH_ENTITY = new PacketType(SENDER, "AttachEntity", 58);
				ENTITY_VELOCITY = new PacketType(SENDER, "EntityVelocity", 59);
				ENTITY_EQUIPMENT = new PacketType(SENDER, "EntityEquipment", 60);
				EXPERIENCE = new PacketType(SENDER, "Experience", 61);
				UPDATE_HEALTH = new PacketType(SENDER, "UpdateHealth", 62);
				SCOREBOARD_OBJECTIVE = new PacketType(SENDER, "ScoreboardObjective", 63);
				MOUNT = new PacketType(SENDER, "Mount", 64);
				SCOREBOARD_TEAM = new PacketType(SENDER, "ScoreboardTeam", 65);
				SCOREBOARD_SCORE = new PacketType(SENDER, "ScoreboardScore", 66);
				SPAWN_POSITION = new PacketType(SENDER, "SpawnPosition", 67);
				UPDATE_TIME = new PacketType(SENDER, "UpdateTime", 68);
				TITLE = new PacketType(SENDER, "Title", 69);
				UPDATE_SIGN = new PacketType(SENDER, "UpdateSign", 70);
				NAMED_SOUND_EFFECT = new PacketType(SENDER, "NamedSoundEffect", 71);
				PLAYER_LIST_HEADER_FOOTER = new PacketType(SENDER, "PlayerListHeaderFooter", 72);
				COLLECT = new PacketType(SENDER, "Collect", 73);
				ENTITY_TELEPORT = new PacketType(SENDER, "EntityTeleport", 74);
				UPDATE_ATTRIBUTES = new PacketType(SENDER, "UpdateAttributes", 75);
				ENTITY_EFFECT = new PacketType(SENDER, "EntityEffect", 76);
			}

		}

		public static class Client {

			private static final PacketType.Sender SENDER;
			public static final PacketType TELEPORT_ACCEPT;
			public static final PacketType TAB_COMPLETE;
			public static final PacketType CHAT;
			public static final PacketType CLIENT_COMMAND;
			public static final PacketType SETTINGS;
			public static final PacketType TRANSACTION;
			public static final PacketType ENCHANT_ITEM;
			public static final PacketType WINDOW_CLICK;
			public static final PacketType CLOSE_WINDOW;
			public static final PacketType CUSTOM_PAYLOAD;
			public static final PacketType USE_ENTITY;
			public static final PacketType KEEP_ALIVE;
			public static final PacketType POSITION;
			public static final PacketType POSITION_LOOK;
			public static final PacketType LOOK;
			public static final PacketType FLYING;
			public static final PacketType VEHICLE_MOVE;
			public static final PacketType BOAT_MOVE;
			public static final PacketType ABILITIES;
			public static final PacketType BLOCK_DIG;
			public static final PacketType ENTITY_ACTION;
			public static final PacketType STEER_VEHICLE;
			public static final PacketType RESOURCE_PACK_STATUS;
			public static final PacketType HELD_ITEM_SLOT;
			public static final PacketType SET_CREATIVE_SLOT;
			public static final PacketType UPDATE_SIGN;
			public static final PacketType ARM_ANIMATION;
			public static final PacketType SPECTATE;
			public static final PacketType USE_ITEM;
			public static final PacketType BLOCK_PLACE;

			static{
				SENDER = Sender.CLIENT;
				TELEPORT_ACCEPT = new PacketType(SENDER, "TeleportAccept", 0);
				TAB_COMPLETE = new PacketType(SENDER, "TabComplete", 1);
				CHAT = new PacketType(SENDER, "Chat", 2);
				CLIENT_COMMAND = new PacketType(SENDER, "ClientCommand", 3);
				SETTINGS = new PacketType(SENDER, "Settings", 4);
				TRANSACTION = new PacketType(SENDER, "Transaction", 5);
				ENCHANT_ITEM = new PacketType(SENDER, "EnchantItem", 6);
				WINDOW_CLICK = new PacketType(SENDER, "WindowClick", 7);
				CLOSE_WINDOW = new PacketType(SENDER, "CloseWindow", 8);
				CUSTOM_PAYLOAD = new PacketType(SENDER, "CustomPayload", 9);
				USE_ENTITY = new PacketType(SENDER, "UseEntity", 10);
				KEEP_ALIVE = new PacketType(SENDER, "KeepAlive", 11);
				POSITION = new PacketType(SENDER, "Position", 12);
				POSITION_LOOK = new PacketType(SENDER, "PositionLook", 13);
				LOOK = new PacketType(SENDER, "Look", 14);
				FLYING = new PacketType(SENDER, "Flying", 15);
				VEHICLE_MOVE = new PacketType(SENDER, "VehicleMove", 16);
				BOAT_MOVE = new PacketType(SENDER, "BoatMove", 17);
				ABILITIES = new PacketType(SENDER, "Abilities", 18);
				BLOCK_DIG = new PacketType(SENDER, "BlockDig", 19);
				ENTITY_ACTION = new PacketType(SENDER, "EntityAction", 20);
				STEER_VEHICLE = new PacketType(SENDER, "SteerVehicle", 21);
				RESOURCE_PACK_STATUS = new PacketType(SENDER, "ResourcePackStatus", 22);
				HELD_ITEM_SLOT = new PacketType(SENDER, "HeldItemSlot", 23);
				SET_CREATIVE_SLOT = new PacketType(SENDER, "SetCreativeSlot", 24);
				UPDATE_SIGN = new PacketType(SENDER, "UpdateSign", 25);
				ARM_ANIMATION = new PacketType(SENDER, "ArmAnimation", 26);
				SPECTATE = new PacketType(SENDER, "Spectate", 27);
				USE_ITEM = new PacketType(SENDER, "UseItem", 28);
				BLOCK_PLACE = new PacketType(SENDER, "BlockPlace", 29);
			}

		}


	}

	public enum Sender {

		SERVER,
		CLIENT

	}

}
