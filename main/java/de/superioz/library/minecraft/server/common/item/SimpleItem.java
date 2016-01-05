package de.superioz.library.minecraft.server.common.item;

import de.superioz.library.minecraft.server.util.ChatUtil;
import de.superioz.library.minecraft.server.util.ItemUtil;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class SimpleItem {

    protected ItemStack wrapped;

    public SimpleItem(Material material, int amount, short damage){
        this.wrapped = new ItemStack(material, amount, damage);
    }

    public SimpleItem(Material material, int amount){
        this(material, amount, (short) 0);
    }

    public SimpleItem(Material material){
        this(material, 1, (short) 0);
    }

    public SimpleItem(ItemStack item){
        this.wrapped = item;
    }

    // ===========================================================================

    public SimpleItem setType(Material mat){
        getWrappedStack().setType(mat);
        return this;
    }

    public SimpleItem setName(String name){
        ItemMeta m = getItemMeta();
        m.setDisplayName(ChatUtil.colored(name));
        getWrappedStack().setItemMeta(m);
        return this;
    }

    public SimpleItem setAmount(int amount){
        getWrappedStack().setAmount(amount);
        return this;
    }

    public SimpleItem setLore(String... lines){
        ItemMeta m = getItemMeta();
        m.setLore(Arrays.asList(ChatUtil.colored(lines)));
        getWrappedStack().setItemMeta(m);
        return this;
    }

    public SimpleItem addLore(String... lines){
        ItemMeta m = getItemMeta();
        List<String> lore = new ArrayList<>();

        if(m.hasLore())
            lore = m.getLore();
        Collections.addAll(lore, ChatUtil.colored(lines));

        m.setLore(lore);
        getWrappedStack().setItemMeta(m);
        return this;
    }

    public SimpleItem setUnbreakable(boolean flag){
        ItemMeta m = getItemMeta();
        m.spigot().setUnbreakable(flag);
        getWrappedStack().setItemMeta(m);
        return this;
    }

    public SimpleItem setFlags(boolean val, ItemFlag... flags){
        ItemMeta m = getItemMeta();
        if(val) m.addItemFlags(flags);
        else m.removeItemFlags(flags);

        getWrappedStack().setItemMeta(m);
        return this;
    }

    public SimpleItem setColor(Color color){
        if(compareType(Material.LEATHER_BOOTS)
                || compareType(Material.LEATHER_LEGGINGS)
                || compareType(Material.LEATHER_CHESTPLATE)
                || compareType(Material.LEATHER_HELMET)){
            LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
            meta.setColor(color);
            getWrappedStack().setItemMeta(meta);
        }
        return this;
    }

    public SimpleItem setColor(DyeColor color){
        if(compareType(Material.LEATHER_BOOTS)
                || compareType(Material.LEATHER_LEGGINGS)
                || compareType(Material.LEATHER_CHESTPLATE)
                || compareType(Material.LEATHER_HELMET)){
            LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
            meta.setColor(color.getColor());
            getWrappedStack().setItemMeta(meta);
        }
        else if(compareType(Material.STAINED_CLAY)
                || compareType(Material.STAINED_GLASS)
                || compareType(Material.STAINED_GLASS_PANE)
                || compareType(Material.WOOL)){
            setDurability(color.getData());
        }
        return this;
    }

    public SimpleItem setDurability(short durability){
        getWrappedStack().setDurability(durability);
        return this;
    }

    public SimpleItem enchant(SimpleItemSpell... enchantments){
        for(SimpleItemSpell e : enchantments){
            getWrappedStack().addUnsafeEnchantment(e.getEnchantment(), e.getLevel());
        }
        return this;
    }

    // ===========================================================================

    public SimpleItem clearType(){
        getWrappedStack().setType(Material.STONE);
        return this;
    }

    public SimpleItem clearName(){
        ItemMeta m = getItemMeta();
        m.setDisplayName("");
        getWrappedStack().setItemMeta(m);
        return this;
    }

    public SimpleItem clearAmount(){
        getWrappedStack().setAmount(1);
        return this;
    }

    public SimpleItem clearLore(){
        ItemMeta m = getItemMeta();
        m.setLore(Collections.singletonList(""));
        getWrappedStack().setItemMeta(m);
        return this;
    }

    public SimpleItem clearFlags(){
        setUnbreakable(false);
        setFlags(false, ItemFlag.values());
        return this;
    }

    public SimpleItem clearDurability(){
        getWrappedStack().setDurability((short) 0);
        return this;
    }

    public SimpleItem clearEnchantments(){
        for(Enchantment e : Enchantment.values()){ getWrappedStack().removeEnchantment(e); }
        return this;
    }

    // ===========================================================================

    public ItemMeta getItemMeta(){
        return getWrappedStack().getItemMeta();
    }

    public ItemStack getWrappedStack(){
        return wrapped;
    }

    public String getName(){
        return getItemMeta().getDisplayName();
    }

    public int getAmount(){
        return getWrappedStack().getAmount();
    }

    public short getDurability(){
        return getWrappedStack().getDurability();
    }

    public Material getType(){
        return getWrappedStack().getType();
    }

    public boolean compareType(Material mat){
        return getType() == mat;
    }

    public boolean compareWith(ItemStack item){
        return ItemUtil.compare(getWrappedStack(), item);
    }

}
