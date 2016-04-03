package de.superioz.library.bukkit.common.item;

import org.bukkit.enchantments.Enchantment;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
public class SimpleItemSpell {

    private Enchantment enchantment;
    private int level;

    public SimpleItemSpell(Enchantment enchantment, int level){
        this.enchantment = enchantment;
        this.level = level;
    }

    // -- Intern methods

    public Enchantment getEnchantment(){
        return enchantment;
    }

    public int getLevel(){
        return level;
    }

}
