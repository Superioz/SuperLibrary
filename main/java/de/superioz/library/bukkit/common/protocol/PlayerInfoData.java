package de.superioz.library.bukkit.common.protocol;

import com.mojang.authlib.GameProfile;
import de.superioz.library.java.util.ReflectionUtils;
import lombok.Getter;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Created on 02.04.2016.
 */
@Getter
public class PlayerInfoData {

	private EnumWrappers.NativeGameMode gameMode;
	private GameProfile profile;
	private WrappedChatComponent component;
	private int latence;

	public PlayerInfoData(EnumWrappers.NativeGameMode gameMode, GameProfile profile, int latence, WrappedChatComponent component){
		this.gameMode = gameMode;
		this.profile = profile;
		this.latence = latence;
		this.component = component;
	}

	/**
	 * Get playerinfodata from handle
	 *
	 * @param handle The handle
	 * @return The result
	 * @throws IllegalAccessException
	 */
	public static PlayerInfoData fromHandle(Object handle) throws IllegalAccessException{
		Class<?> clazz = handle.getClass();

		// Set fields
		int latency = ReflectionUtils.getField(clazz, "b").getInt(handle);
		EnumWrappers.NativeGameMode gameMode = EnumWrappers.NativeGameMode.
				valueOf(((Enum) ReflectionUtils.getField(clazz, "c").get(handle)).name());
		GameProfile profile = (GameProfile) ReflectionUtils.getField(clazz, "d").get(handle);
		WrappedChatComponent chatComponent = new WrappedChatComponent(ReflectionUtils.getField(clazz, "e").get(handle));

		return new PlayerInfoData(gameMode, profile, latency, chatComponent);
	}

	/**
	 * Get playerinfodata handle
	 *
	 * @return The handle
	 */
	public Object toHandle(Object packetInstance){
		Class<?> clazz = ProtocolUtil.PLAYER_INFO_DATA;
		Object gameMode = ProtocolUtil.ENUM_GAMEMODE.getEnumConstants()[getGameMode().ordinal()];
		Object chatBase = getComponent().getHandle();

		try{
			Constructor constructor = clazz.getConstructor(packetInstance.getClass(),
					GameProfile.class, int.class, ProtocolUtil.ENUM_GAMEMODE, ProtocolUtil.CHAT_BASE_COMPONENT);
			constructor.setAccessible(true);
			return constructor.newInstance(packetInstance, getProfile(), getLatence(), gameMode, chatBase);
		}
		catch(NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e){
			e.printStackTrace();
			throw new RuntimeException("Couldn't get handle of PlayerInfoData");
		}
	}

}
