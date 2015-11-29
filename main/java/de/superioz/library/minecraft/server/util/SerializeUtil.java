package de.superioz.library.minecraft.server.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class SerializeUtil {

    public static String toString(ItemStack... items){
        String serialization = items.length + ";";
        for(int i = 0; i < items.length; i++){
            ItemStack is = items[i];
            if(is != null){
                String serializedItemStack = "";

                String isType = String.valueOf(is.getType().getId());
                serializedItemStack += "t@" + isType;

                if(is.getDurability() != 0){
                    String isDurability = String.valueOf(is.getDurability());
                    serializedItemStack += ":d@" + isDurability;
                }

                if(is.getAmount() != 1){
                    String isAmount = String.valueOf(is.getAmount());
                    serializedItemStack += ":a@" + isAmount;
                }

                Map<Enchantment, Integer> isEnch = is.getEnchantments();
                if(isEnch.size() > 0){
                    for(Map.Entry<Enchantment, Integer> ench : isEnch.entrySet()){
                        serializedItemStack += ":e@" + ench.getKey().getId() + "@" + ench.getValue();
                    }
                }

                serialization += i + "#" + serializedItemStack + ";";
            }
        }
        return serialization;
    }

    public static ItemStack[] itemsFromString(String string){
        String[] serializedBlocks = string.split(";");
        String invInfo = serializedBlocks[0];
        ItemStack[] itemStacks = new ItemStack[Integer.parseInt(invInfo)];

        for(int i = 1; i < serializedBlocks.length; i++){
            String[] serializedBlock = serializedBlocks[i].split("#");
            int stackPosition = Integer.valueOf(serializedBlock[0]);

            if(stackPosition >= itemStacks.length){
                continue;
            }

            ItemStack is = null;
            Boolean createdItemStack = false;

            String[] serializedItemStack = serializedBlock[1].split(":");
            for(String itemInfo : serializedItemStack){
                String[] itemAttribute = itemInfo.split("@");
                if(itemAttribute[0].equals("t")){
                    is = new ItemStack(Material.getMaterial(Integer.valueOf(itemAttribute[1])));
                    createdItemStack = true;
                }else if(itemAttribute[0].equals("d") && createdItemStack){
                    is.setDurability(Short.valueOf(itemAttribute[1]));
                }else if(itemAttribute[0].equals("a") && createdItemStack){
                    is.setAmount(Integer.valueOf(itemAttribute[1]));
                }else if(itemAttribute[0].equals("e") && createdItemStack){
                    is.addEnchantment(Enchantment.getById(Integer.valueOf(itemAttribute[1])), Integer.valueOf(itemAttribute[2]));
                }
            }

            itemStacks[stackPosition] = is;
        }
        return itemStacks;
    }

    public static String toString(Location location){
        return location.getWorld().getName()
                + ";" + location.getX()
                + ";" + location.getY()
                + ";" + location.getZ()
                + ";" + location.getYaw()
                + ";" + location.getPitch();
    }

    public static Location locFromString(String str){
        if(str == null || str.isEmpty()){
            return null;
        }

        String[] strar = str.split(";");
        Location newLoc = new Location(Bukkit.getWorld(strar[0]), Double.valueOf(strar[1]),
                Double.valueOf(strar[2]),
                Double.valueOf(strar[3]),
                Float.valueOf(strar[4]),
                Float.valueOf(strar[5]));
        return newLoc.clone();
    }

}
