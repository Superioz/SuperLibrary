package de.superioz.library.minecraft.server.lab.disguise;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.darkblade12.particleeffect.ReflectionUtils;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.util.BukkitUtilities;
import de.superioz.library.minecraft.server.util.CraftBukkitUtil;
import de.superioz.library.minecraft.server.util.ChatUtil;
import de.superioz.library.minecraft.server.util.ProtocolUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.UUID;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class PlayerDisguise {

    private String customName;
    private DisguiseType type;
    private UUID disguised;
    private Object entity;


    public PlayerDisguise(UUID p, DisguiseType type, String name) {
        this.disguised = p;
        this.type = type;
        this.customName = ChatUtil.colored(name);

        try{
            this.initEntity();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // ======================================= OTHER METHODS ===================================

    public void send(Player... players){
        try{
            this.initEntity();
            sendDisguise(players);
            SuperLibrary.pluginManager().callEvent(new PlayerDisguiseEvent(getDisguised(), this));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void send(){
        send(BukkitUtilities.onlinePlayers());
    }

    public void undisguise(Player... players){
        try{
            SuperLibrary.pluginManager().callEvent(new PlayerUndisguiseEvent(getDisguised()));
            sendUndisguise(players);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void undisguise(){
        undisguise(BukkitUtilities.onlinePlayers());
    }


    // ======================================== PROTOCOL ==========================================

    private void initEntity() throws Exception {
        // Variables
        Player p = Bukkit.getPlayer(this.disguised);
        Location loc = p.getLocation();

        Object world = CraftBukkitUtil.getHandle(p.getWorld());
        Class<?> entityClass = Class.forName(type.getClassName());

        // Init entity class
        if(this.entity == null){
            this.entity = ReflectionUtils.instantiateObject(entityClass, world);
        }

        assert entity != null;
        ReflectionUtils.invokeMethod(entity, "setPosition", loc.getX(), loc.getY(), loc.getZ());
        ReflectionUtils.getMethod(entityClass, "d", int.class).invoke(entity, p.getEntityId());
        this.handleType(type, entity);

        // Name
        if(customName != null){
            ReflectionUtils.getMethod(entityClass, "setCustomName", String.class).invoke(entity, customName);
            ReflectionUtils.getMethod(entityClass, "setCustomNameVisible", boolean.class).invoke(entity, true);
        }
    }

    private void sendDisguise(Player... players) throws Exception {
        // Destroy packet
        Player p = Bukkit.getPlayer(this.disguised);
        PacketContainer destroyPacket = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        destroyPacket.getIntegerArrays().write(0, new int[]{p.getEntityId()});

        // Spawn packet
        Object spawnPacket;
        if(type.getClassName().equals(DisguiseType.ARMORSTAND.getClassName())){
            spawnPacket = ReflectionUtils.instantiateObject("PacketPlayOutSpawnEntity",
                    ReflectionUtils.PackageType.MINECRAFT_SERVER, entity, type.getId());
        }
        else{
            spawnPacket = ReflectionUtils.instantiateObject("PacketPlayOutSpawnEntityLiving",
                    ReflectionUtils.PackageType.MINECRAFT_SERVER, entity);
        }

        ProtocolUtil.sendServerPacket(destroyPacket, Collections.singletonList(p), players);
        CraftBukkitUtil.sendPacket(spawnPacket, Collections.singletonList(p), players);
    }

    private void sendUndisguise(Player... players) throws Exception {
        Player p = Bukkit.getPlayer(this.disguised);
        PacketContainer destroyPacket = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        destroyPacket.getIntegerArrays().write(0, new int[]{p.getEntityId()});

        // Spawn packet
        Object spawnPacket = ReflectionUtils.instantiateObject("PacketPlayOutNamedEntitySpawn",
                ReflectionUtils.PackageType.MINECRAFT_SERVER, CraftBukkitUtil.getHandle(p));

        ProtocolUtil.sendServerPacket(destroyPacket, Collections.singletonList(p), players);
        CraftBukkitUtil.sendPacket(spawnPacket, Collections.singletonList(p), players);
    }

    // ======================================== GETTER & SETTER ==========================================

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public DisguiseType getType() {
        return type;
    }

    public void setType(DisguiseType type) {
        this.type = type;
    }

    public Player getDisguised() {
        return Bukkit.getPlayer(disguised);
    }

    public Object getEntity(){
        return entity;
    }

    // ========================================= TYPES ======================================================

    private Object handleType(DisguiseType type, Object entity)
            throws Exception {
        Player p = Bukkit.getPlayer(this.disguised);
        Location loc = p.getLocation();

        switch (type) {
            case WITHER_SKELETON:
                ReflectionUtils.invokeMethod(entity, "setSkeletonType", 1);
                break;
            case BAT:
                ReflectionUtils.invokeMethod(entity, "setAsleep", false);
                break;
        }
        return entity;
    }

    public void setSlimeSize(int size) {
        if(type == DisguiseType.SLIME
                || type == DisguiseType.MAGMA_CUBE){
            try{
                Method m = ReflectionUtils.getMethod(entity.getClass(), "setSize", int.class);
                m.setAccessible(true);
                m.invoke(entity, size);
            }catch(IllegalAccessException |
                    InvocationTargetException |
                    NoSuchMethodException e){
                e.printStackTrace();
            }
        }
    }

    public void setEndermanCarried(Block block){
        if(type == DisguiseType.ENDERMAN){
            try{
                ReflectionUtils.invokeMethod(entity, "setCarried", block);
            }catch(IllegalAccessException |
                    InvocationTargetException |
                    NoSuchMethodException e){
                e.printStackTrace();
            }
        }
    }

    public void setSheepSheared(boolean flag){
        if(type == DisguiseType.SHEEP){
            try{
                ReflectionUtils.invokeMethod(entity, "setSheared", flag);
            }catch(IllegalAccessException |
                    InvocationTargetException |
                    NoSuchMethodException e){
                e.printStackTrace();
            }
        }
    }

    /*public void setSheepColor(EnumColor color){
        if(type == DisguiseType.SHEEP){
            try{
                Method m = ReflectionUtils.getMethod(entity.getClass(), "setColor", EnumColor.class);
                m.invoke(entity, color);
            }catch(IllegalAccessException |
                    InvocationTargetException |
                    NoSuchMethodException e){
                e.printStackTrace();
            }
        }
    }*/

    public void setWolfTamed(boolean flag){
        if(type == DisguiseType.WOLF){
            try{
                ReflectionUtils.invokeMethod(entity, "setTamed", flag);
            }catch(IllegalAccessException |
                    InvocationTargetException |
                    NoSuchMethodException e){
                e.printStackTrace();
            }
        }
    }

    /*public void setWolfCollarColor(EnumColor color){
        if(type == DisguiseType.WOLF){
            try{
                ReflectionUtils.invokeMethod(entity, "setCollarColor", color);
            }catch(IllegalAccessException |
                    InvocationTargetException |
                    NoSuchMethodException e){
                e.printStackTrace();
            }
        }
    }*/

    public void setVillagerProfession(int value){
        if(type == DisguiseType.VILLAGER){
            try{
                ReflectionUtils.invokeMethod(entity, "setProfession", value);
            }catch(IllegalAccessException |
                    InvocationTargetException |
                    NoSuchMethodException e){
                e.printStackTrace();
            }
        }
    }

    public void setGuardianElder(boolean flag){
        if(type == DisguiseType.GUARDIAN){
            try{
                ReflectionUtils.invokeMethod(entity, "setElder", flag);
            }catch(IllegalAccessException |
                    InvocationTargetException |
                    NoSuchMethodException e){
                e.printStackTrace();
            }
        }
    }

}

