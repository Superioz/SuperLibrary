package de.superioz.library.bukkit.common.protocol;

import de.superioz.library.java.util.Converter;
import de.superioz.library.java.util.WrappedFieldArray;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created on 29.03.2016.
 */
@Getter
public class WrappedPacket {

	private Object handle;
	private PacketType type;

	/**
	 * Constructor of wrapper for a packet
	 *
	 * @param type The packet name
	 */
	public WrappedPacket(PacketType type){
		try{
			this.handle = ProtocolUtil.getNMSClassObject(type.getClassName());
			this.type = type;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public WrappedPacket(Object handle){
		this.handle = handle;

		String className = handle.getClass().getSimpleName();
		this.type = PacketType.lookup(className);
	}

	/**
	 * Get all integer fields from packet class
	 *
	 * @return The array of integer fields
	 */
	public WrappedFieldArray<Integer> getIntegers(){
		return new WrappedFieldArray<>(handle, int.class);
	}

	/**
	 * Get all itemstack fields from packet class
	 *
	 * @return The array of itemstack fields
	 */
	public WrappedFieldArray<Object> getItemModifier(){
		return new WrappedFieldArray<>(handle, ProtocolUtil.ITEMSTACK);
	}

	/**
	 * Get all double fields from packet class
	 *
	 * @return The array of double fields
	 */
	public WrappedFieldArray<Double> getDoubles(){
		return new WrappedFieldArray<>(handle, double.class);
	}

	/**
	 * Get all short fields from packet class
	 *
	 * @return The array of short fields
	 */
	public WrappedFieldArray<Short> getShorts(){
		return new WrappedFieldArray<>(handle, short.class);
	}

	/**
	 * Get all byte fields from packet class
	 *
	 * @return The array of byte fields
	 */
	public WrappedFieldArray<Byte> getBytes(){
		return new WrappedFieldArray<>(handle, byte.class);
	}

	/**
	 * Get all string fields from packet class
	 *
	 * @return The array of string fields
	 */
	public WrappedFieldArray<String> getStrings(){
		return new WrappedFieldArray<>(handle, String.class);
	}

	/**
	 * Get all float fields from packet class
	 *
	 * @return The array of float fields
	 */
	public WrappedFieldArray<Float> getFloats(){
		return new WrappedFieldArray<>(handle, float.class);
	}

	/**
	 * Get all long fields from packet class
	 *
	 * @return The array of long fields
	 */
	public WrappedFieldArray<Long> getLongs(){
		return new WrappedFieldArray<>(handle, long.class);
	}

	/**
	 * Get all boolean fields from packet class
	 *
	 * @return The array of boolean fields
	 */
	public WrappedFieldArray<Boolean> getBooleans(){
		return new WrappedFieldArray<>(handle, boolean.class);
	}

	/**
	 * Get all uuid fields from packet class
	 *
	 * @return The array of uuid fields
	 */
	public WrappedFieldArray<UUID> getUniqueIdentifier(){
		return new WrappedFieldArray<>(handle, UUID.class);
	}

	/**
	 * Get all BossbarAction fields from packet class
	 *
	 * @return The array of BossbarAction fields
	 */
	public WrappedFieldArray<EnumWrappers.BossbarAction> getBossbarActionModifier(){

		return new WrappedFieldArray<>(handle, ProtocolUtil.BOSSBAR_ACTION, new Converter<Object, EnumWrappers.BossbarAction>() {
			@Override
			public EnumWrappers.BossbarAction convert(Object val){
				return EnumWrappers.BossbarAction.valueOf(((Enum)val).name());
			}

			@Override
			public Object reverse(EnumWrappers.BossbarAction val){
				return ProtocolUtil.BOSSBAR_ACTION.getEnumConstants()[val.ordinal()];
			}
		});
	}

	/**
	 * Get all BossbarColor fields from packet class
	 *
	 * @return The array of BossbarColor fields
	 */
	public WrappedFieldArray<EnumWrappers.BossbarColor> getBossbarColorModifier(){

		return new WrappedFieldArray<>(handle, ProtocolUtil.BOSSBAR_COLOR, new Converter<Object, EnumWrappers.BossbarColor>() {
			@Override
			public EnumWrappers.BossbarColor convert(Object val){
				return EnumWrappers.BossbarColor.valueOf(((Enum)val).name());
			}

			@Override
			public Object reverse(EnumWrappers.BossbarColor val){
				return ProtocolUtil.BOSSBAR_COLOR.getEnumConstants()[val.ordinal()];
			}
		});
	}

	/**
	 * Get all BossbarStyle fields from packet class
	 *
	 * @return The array of BossbarStyle fields
	 */
	public WrappedFieldArray<EnumWrappers.BossbarStyle> getBossbarStyleModifier(){

		return new WrappedFieldArray<>(handle, ProtocolUtil.BOSSBAR_STYLE, new Converter<Object, EnumWrappers.BossbarStyle>() {
			@Override
			public EnumWrappers.BossbarStyle convert(Object val){
				return EnumWrappers.BossbarStyle.valueOf(((Enum)val).name());
			}

			@Override
			public Object reverse(EnumWrappers.BossbarStyle val){
				return ProtocolUtil.BOSSBAR_STYLE.getEnumConstants()[val.ordinal()];
			}
		});
	}

	/**
	 * Get all worldborderaction fields from packet class
	 *
	 * @return The array of worldborderaction fields
	 */
	public WrappedFieldArray<EnumWrappers.WorldBorderAction> getWorldBorderActions(){
		return new WrappedFieldArray<>(handle, ProtocolUtil.WORLD_BORDER_ACTION, new Converter<Object, EnumWrappers.WorldBorderAction>() {
			@Override
			public EnumWrappers.WorldBorderAction convert(Object val){
				return EnumWrappers.WorldBorderAction.valueOf(((Enum)val).name());
			}

			@Override
			public Object reverse(EnumWrappers.WorldBorderAction val){
				return ProtocolUtil.WORLD_BORDER_ACTION.getEnumConstants()[val.ordinal()];
			}
		});
	}

	/**
	 * Get all chatbasecomponent fields from packet class
	 *
	 * @return The array of chatbasecomponent fields
	 */
	public WrappedFieldArray<WrappedChatComponent> getChatComponents(){
		return new WrappedFieldArray<>(handle, ProtocolUtil.CHAT_BASE_COMPONENT, new Converter<Object, WrappedChatComponent>() {
			@Override
			public WrappedChatComponent convert(Object val){
				return new WrappedChatComponent(val);
			}

			@Override
			public Object reverse(WrappedChatComponent val){
				return val.getHandle();
			}
		});
	}

	/**
	 * Get all titleaction fields from packet class
	 *
	 * @return The array of titleaction fields
	 */
	public WrappedFieldArray<EnumWrappers.TitleAction> getTitleActions(){
		return new WrappedFieldArray<>(handle, ProtocolUtil.TITLE_ACTION, new Converter<Object, EnumWrappers.TitleAction>() {
			@Override
			public EnumWrappers.TitleAction convert(Object val){
				return EnumWrappers.TitleAction.valueOf(((Enum)val).name());
			}

			@Override
			public Object reverse(EnumWrappers.TitleAction val){
				return ProtocolUtil.TITLE_ACTION.getEnumConstants()[val.ordinal()];
			}
		});
	}

	/**
	 * Get all entityuseaction fields from packet class
	 *
	 * @return The array of entityuseaction fields
	 */
	public WrappedFieldArray<EnumWrappers.EntityUseAction> getEntityUseActions(){
		return new WrappedFieldArray<>(handle, ProtocolUtil.ENTITY_ACTION_USE, new Converter<Object, EnumWrappers.EntityUseAction>() {
			@Override
			public EnumWrappers.EntityUseAction convert(Object val){
				return EnumWrappers.EntityUseAction.valueOf(((Enum)val).name());
			}

			@Override
			public Object reverse(EnumWrappers.EntityUseAction val){
				return ProtocolUtil.ENTITY_ACTION_USE.getEnumConstants()[val.ordinal()];
			}
		});
	}

	/**
	 * Get all itemslot fields from packet class
	 *
	 * @return The array of itemslot fields
	 */
	public WrappedFieldArray<EnumWrappers.ItemSlot> getItemSlotModifier(){
		return new WrappedFieldArray<>(handle, ProtocolUtil.ENUM_ITEM_SLOT, new Converter<Object, EnumWrappers.ItemSlot>() {

			@Override
			public EnumWrappers.ItemSlot convert(Object val){
				return EnumWrappers.ItemSlot.valueOf(((Enum)val).name());
			}

			@Override
			public Object reverse(EnumWrappers.ItemSlot val){
				return ProtocolUtil.ENUM_ITEM_SLOT.getEnumConstants()[val.ordinal()];
			}
		});
	}

	/**
	 * Get all playerInfoData List fields from packet class
	 *
	 * @return The array of playerInfoData List fields
	 */
	public WrappedFieldArray<List<PlayerInfoData>> getPlayerInfoDataLists(){
		return new WrappedFieldArray<>(handle, List.class, new Converter<Object, List<PlayerInfoData>>() {
			@Override
			public List<PlayerInfoData> convert(Object val){
				List<PlayerInfoData> playerInfoDatas = new ArrayList<>();
				List<Object> list = (List<Object>) val;

				try{
					for(Object o : list){
						playerInfoDatas.add(PlayerInfoData.fromHandle(o));
					}
				}
				catch(Exception e){
					throw new RuntimeException("Couldn't get player info data fields.");
				}

				return playerInfoDatas;
			}

			@Override
			public Object reverse(List<PlayerInfoData> val){
				List<Object> list = new ArrayList<>();

				for(PlayerInfoData data : val){
					list.add(data.toHandle(handle));
				}

				return list;
			}
		});
	}

	/**
	 * Get all playerInfoAction fields from packet class
	 *
	 * @return The array of playerInfoAction fields
	 */
	public WrappedFieldArray<EnumWrappers.PlayerInfoAction> getPlayerInfoAction(){
		return new WrappedFieldArray<>(handle, ProtocolUtil.PLAYER_INFO_ACTION, new Converter<Object, EnumWrappers.PlayerInfoAction>() {
			@Override
			public EnumWrappers.PlayerInfoAction convert(Object val){
				return EnumWrappers.PlayerInfoAction.valueOf(((Enum) val).name());
			}

			@Override
			public Object reverse(EnumWrappers.PlayerInfoAction val){
				return ProtocolUtil.PLAYER_INFO_ACTION.getEnumConstants()[val.ordinal()];
			}
		});
	}

	/**
	 * Get all datawatcher fields from packet class
	 *
	 * @return The array of datawatcher fields
	 */
	public WrappedFieldArray<WrappedDataWatcher> getDataWatcherModifier(){
		return new WrappedFieldArray<>(handle, ProtocolUtil.DATA_WATCHER, new Converter<Object, WrappedDataWatcher>() {
			@Override
			public WrappedDataWatcher convert(Object val){
				WrappedDataWatcher w = new WrappedDataWatcher();
				w.inherit(val, ProtocolUtil.DATA_WATCHER);
				return w;
			}

			@Override
			public Object reverse(WrappedDataWatcher val){
				return val.getHandle();
			}
		});
	}

	/**
	 * Get all watchable object list fields from packet class
	 *
	 * @return The array of watchable object list fields
	 */
	public WrappedFieldArray<List<WrappedWatchableObject>> getWatchableObjectCollections(){
		return new WrappedFieldArray<>(handle, Collection.class, new Converter<Object, List<WrappedWatchableObject>>() {
			@Override
			public List<WrappedWatchableObject> convert(Object val){
				List<Object> list = (List<Object>) val;
				List<WrappedWatchableObject> newList = new ArrayList<>();

				for(Object ob : list){
					newList.add(new WrappedWatchableObject(ob));
				}

				return newList;
			}

			@Override
			public Object reverse(List<WrappedWatchableObject> val){
				List<Object> list = new ArrayList<>();

				for(WrappedWatchableObject ob : val){
					list.add(ob.getHandle());
				}

				return list;
			}

		});
	}

	/**
	 * Send this packet to given players
	 *
	 * @param players The players
	 */
	public void send(Player... players){
		ProtocolUtil.sendPacket(getHandle(), players);
	}

	/**
	 * Get all ? fields from packet class
	 *
	 * @return The array of ? fields
	 */
	public WrappedFieldArray<?> get(Class<?> type){
		return new WrappedFieldArray<>(handle, type);
	}

}
