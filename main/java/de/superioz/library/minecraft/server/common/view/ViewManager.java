package de.superioz.library.minecraft.server.common.view;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import de.superioz.library.java.util.TimeUtils;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.util.BukkitUtil;
import de.superioz.library.minecraft.server.util.ChatUtil;
import de.superioz.library.minecraft.server.util.ProtocolUtil;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Class created on April in 2015
 */
public class ViewManager {

    private static Class packetClass = BukkitUtil.getNMSClassExact("PacketPlayOutChat");
    private static Class chatComponentClass = BukkitUtil.getNMSClassExact("IChatBaseComponent");

    public static void sendHotbarMessage(String text, Player... players){
        for(Player p : players)
            sendHotbarMessagePacket(text, p);
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn,
                                 int stay, int fadeOut, boolean timeUnitSeconds){
        new CraftTitle(title, subtitle, new int[]{fadeIn, stay, fadeOut}).send(timeUnitSeconds, player);
    }

    public static void sendTitle(Player player, String title, String subtitle){
        sendTitle(player, title, subtitle, 1, 1, 1, true);
    }

    public static void sendTitle(Player player, String title){
        sendTitle(player, title, "");
    }

    public static void setSight(Player camera, Player spectator){
        ProtocolUtil.sendServerPacket(getPacket(camera.getEntityId()), spectator);
    }

    private static PacketContainer getPacket(int cameraID){
        PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.CAMERA);
        packet.getIntegers().write(0, cameraID);

        return packet;
    }

    //============================================ PRIVATE ============================================

    @SuppressWarnings("unchecked")
    private static void sendHotbarMessagePacket(String text, Player player){
        byte target = (byte) 2;

        // Get packet
        try{
            Object chatBaseComp = BukkitUtil.getChatBaseComp("{'text':'" + ChatUtil.colored(text) + "'}");
            Object packet;

            if(packetClass != null && chatBaseComp != null){
                Constructor constructor = packetClass.getDeclaredConstructor(chatComponentClass, byte.class);
                packet = constructor.newInstance(chatBaseComp, target);

                BukkitUtil.sendPacket(packet, player);
            }
        }catch(NoSuchMethodException
                | InvocationTargetException
                | InstantiationException
                | IllegalAccessException e){
            e.printStackTrace();
        }

    }

    /**
     * Class created on April in 2015
     */
    public static class CraftTitle {

        protected String title;
        protected String subTitle;
        protected int[] timings;

        public CraftTitle(String title, String subTitle, int[] timings){
            this.title = title;
            this.subTitle = subTitle;
            this.setTimings(timings);
        }

        //============================================ GETTER ============================================

        public int getFadeIn(){
            return timings[0];
        }

        public int getFadeOut(){
            return timings[2];
        }

        public int getStay(){
            return timings[1];
        }

        public int[] getTimings(){
            return timings;
        }

        public String getTitle(){
            return title;
        }

        public String getSubTitle(){
            return subTitle;
        }

        //============================================ SETTER ============================================

        public void setFadeIn(int fadeIn){
            this.timings[0] = fadeIn;
        }

        public void setFadeOut(int fadeOut){
            this.timings[2] = fadeOut;
        }

        public void setTitle(String title){
            this.title = title;
        }

        public void setSubTitle(String subTitle){
            this.subTitle = subTitle;
        }

        public void setStay(int stay){
            this.timings[1] = stay;
        }

        public void setTimings(int[] timings){
            int[] timeValues = timings;

            if(timings != null && timings.length != 3){
                timeValues = new int[]{1,1,1};
            }

            this.timings = timeValues;
        }

        //============================================ PROTOCOL ============================================

        public PacketContainer getPacket(){
            return SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.TITLE);
        }

        public void send(boolean ticks, Player... players){
            // Timings
            if(timings != null && timings.length == 3){
                PacketContainer timingsPacket = getPacket();

                // Title
                timingsPacket.getTitleActions().write(0, EnumWrappers.TitleAction.TIMES);
                timingsPacket.getIntegers().write(0, TimeUtils.convertTime(this.getFadeIn(), ticks));
                timingsPacket.getIntegers().write(1, TimeUtils.convertTime(this.getFadeIn(), ticks));
                timingsPacket.getIntegers().write(2, TimeUtils.convertTime(this.getFadeIn(), ticks));


                // Send
                ProtocolUtil.sendServerPacket(timingsPacket, players);
            }

            // Title
            if(title != null && !title.isEmpty()){
                PacketContainer titlePacket = getPacket();

                // Data
                titlePacket.getTitleActions().write(0, EnumWrappers.TitleAction.TITLE);
                titlePacket.getChatComponents().write(0, WrappedChatComponent.fromText(ChatUtil.colored(title)));

                // Send
                ProtocolUtil.sendServerPacket(titlePacket, players);
            }

            // Subtitle
            if(subTitle != null && !subTitle.isEmpty()){
                PacketContainer subtitlePacket = getPacket();

                // Title
                subtitlePacket.getTitleActions().write(0, EnumWrappers.TitleAction.SUBTITLE);
                subtitlePacket.getChatComponents().write(0, WrappedChatComponent.fromText(ChatUtil.colored(subTitle)));

                // Send
                ProtocolUtil.sendServerPacket(subtitlePacket, players);
            }
        }

        public void send(Player... players){
            this.send(false, players);
        }

    }
}
