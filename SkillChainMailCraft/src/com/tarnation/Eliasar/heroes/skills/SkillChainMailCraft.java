package com.tarnation.Eliasar.heroes.skills;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.effects.EffectType;
import com.herocraftonline.heroes.characters.skill.PassiveSkill;
import com.herocraftonline.heroes.characters.skill.SkillConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class SkillChainMailCraft extends PassiveSkill {

    public SkillChainMailCraft(Heroes plugin) {
        super(plugin, "ChainmailCraft");
        ShapedRecipe chainHelm = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_HELMET, 1));
        chainHelm.shape("III", "L L", "   ")
                .setIngredient('I', Material.IRON_INGOT)
                .setIngredient('L', Material.LEATHER);
        ShapedRecipe chainChest = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1));
        chainChest.shape("L L", "III", "III")
                .setIngredient('I', Material.IRON_INGOT)
                .setIngredient('L', Material.LEATHER);
        ShapedRecipe chainLegs = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1));
        chainLegs.shape("LIL", "I I", "I I")
                .setIngredient('I', Material.IRON_INGOT)
                .setIngredient('L', Material.LEATHER);
        ShapedRecipe chainBoots = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_BOOTS, 1));
        chainBoots.shape("   ", "L L", "I I")
                .setIngredient('I', Material.IRON_INGOT)
                .setIngredient('L', Material.LEATHER);
        Bukkit.addRecipe(chainHelm);
        Bukkit.addRecipe(chainChest);
        Bukkit.addRecipe(chainLegs);
        Bukkit.addRecipe(chainBoots);
        setDescription("Grants ability to forge chainmail armor.");
        setEffectTypes(EffectType.BENEFICIAL);
        Bukkit.getServer().getPluginManager().registerEvents(new SkillChainmailCraftListener(), plugin);
    }

    @Override
    public String getDescription(Hero hero) {
        return getDescription();
    }

    @Override
    public ConfigurationSection getDefaultConfig() {
        ConfigurationSection node = super.getDefaultConfig();
        node.set("boot-exp", 60);
        node.set("chest-exp", 100);
        node.set("helmet-exp", 50);
        node.set("leg-exp", 80);
        return super.getDefaultConfig();
    }

    public class SkillChainmailCraftListener implements Listener {

        @EventHandler(priority=EventPriority.LOWEST)
        public void onPlayerCraft(CraftItemEvent event) {
            if (event.isCancelled()) {
                return;
            }

            if (event.getInventory() != null
                    && event.getSlotType().equals(InventoryType.SlotType.RESULT)) {

                if (event.getCurrentItem() == null) {
                    return;
                }

                Hero hero = plugin.getCharacterManager().getHero((Player)event.getWhoClicked());
                double experienceGranted = SkillConfigManager.getUseSetting(hero, SkillChainMailCraft.this, "helmet-exp", 50, false);

                if (!hero.hasEffect("ChainMailCraft")) {
                    event.setCancelled(true);
                    hero.getPlayer().sendMessage(ChatColor.GRAY + "You lack the blacksmithing expertise required to craft chainmail!");
                    return;
                }

                if (event.getCurrentItem().getType() == Material.CHAINMAIL_BOOTS) {
                    experienceGranted = SkillConfigManager.getUseSetting(hero, SkillChainMailCraft.this, "boot-exp", 60, false);
                } else if (event.getCurrentItem().getType() == Material.CHAINMAIL_CHESTPLATE) {
                    experienceGranted = SkillConfigManager.getUseSetting(hero, SkillChainMailCraft.this, "chest-exp", 100, false);
                } else if (event.getCurrentItem().getType() == Material.CHAINMAIL_HELMET) {
                    experienceGranted = SkillConfigManager.getUseSetting(hero, SkillChainMailCraft.this, "helmet-exp", 50, false);
                } else if (event.getCurrentItem().getType() == Material.CHAINMAIL_LEGGINGS) {
                    experienceGranted = SkillConfigManager.getUseSetting(hero, SkillChainMailCraft.this, "leg-exp", 80, false);
                }

                hero.addExp(experienceGranted, hero.getSecondClass(), hero.getPlayer().getLocation());
            }
        }
    }
}