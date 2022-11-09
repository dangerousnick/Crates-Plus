package jorisfort.cratesplusreloaded.commands;

import jorisfort.cratesplusreloaded.frameworks.DataManager;
import jorisfort.cratesplusreloaded.Main;
import jorisfort.cratesplusreloaded.menus.Crates;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Crate implements CommandExecutor, TabCompleter {
    private final DataManager data;
    private final Main plugin;
    public Crate(Main plugin) {
        plugin.getCommand("crate").setExecutor(this);
        plugin.getCommand("crate").setTabCompleter(this);

        this.plugin = plugin;
        data = new DataManager(plugin, "crates");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("cratesPlusReloaded.admin")) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("create")) {
                        if (args.length > 1) {
                            String crateName = args[1];

                            data.reloadConfig();
                            if (data.getConfig().get(crateName + ".color") == null) {
                                List<ItemStack> items = new ArrayList<>();
                                items.add(new ItemStack(Material.IRON_SWORD));

                                List<Double> chances = new ArrayList<>();
                                chances.add(100.0);

                                data.getConfig().set(crateName + ".items", items);
                                data.getConfig().set(crateName + ".color", "§c");
                                data.getConfig().set(crateName + ".chances", chances);
                                data.saveConfig();

                                player.sendMessage(Main.chatPrefix + ChatColor.GREEN + "Successfully added the crate §c" + crateName);
                            } else {
                                player.sendMessage(Main.chatPrefix + ChatColor.RED + "Crate name already exists");
                            }
                        } else {
                            player.sendMessage(Main.chatPrefix + ChatColor.RED + "Argument required example:" + ChatColor.RESET + " /crate Create <name>");
                        }
                    } else if (args[0].equalsIgnoreCase("settings")) {
                        new Crates(Main.getPlayerMenuUtility(player), plugin).open();
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        data.reloadConfig();
                        player.sendMessage(Main.chatPrefix + ChatColor.GREEN + "Successfully reloaded config");
                    } else if (args[0].equalsIgnoreCase("rename")) {
                        if (args.length > 2) {
                            String oldName = args[1];
                            String newName = args[2];

                            data.reloadConfig();
                            if (data.getConfig().get(oldName + ".color") == null) {
                                player.sendMessage(Main.chatPrefix + ChatColor.RED + "Name doesn't exist");
                            } else {
                                // Get all the data
                                data.reloadConfig();
                                List<ItemStack> items = (List<ItemStack>) data.getConfig().getList(oldName + ".items");
                                List<Double> chances = (List<Double>) data.getConfig().getList(oldName + ".chances");
                                String color = data.getConfig().getString(oldName + ".color");

                                // Delete old crate
                                data.getConfig().set(oldName, null);

                                // Make new crate
                                data.getConfig().set(newName + ".items", items);
                                data.getConfig().set(newName + ".color", color);
                                data.getConfig().set(newName + ".chances", chances);
                                data.saveConfig();

                                player.sendMessage(Main.chatPrefix + ChatColor.GREEN + "Successfully renamed crate from " + oldName + " to " + newName);
                            }
                        } else {
                            player.sendMessage(Main.chatPrefix + ChatColor.RED + "Argument required example:" + ChatColor.RESET + " /crate Rename <old> <new>");
                        }
                    } else if (args[0].equalsIgnoreCase("delete")) {
                        if (args.length > 1) {
                            String crateName = args[1];

                            data.reloadConfig();
                            if (data.getConfig().get(crateName + ".color") != null) {
                                data.getConfig().set(crateName, null);
                                String color = data.getConfig().getString(crateName + ".color");
                                data.saveConfig();

                                player.sendMessage(Main.chatPrefix + ChatColor.GREEN + "Successfully removed crate " + color + crateName);
                            } else {
                                player.sendMessage(Main.chatPrefix + ChatColor.RED + "Crate doesn't exists");
                            }
                        } else {
                            player.sendMessage(Main.chatPrefix + ChatColor.RED + "Argument required example:" + ChatColor.RESET + " /crate Delete <name>");
                        }
                    } else if (args[0].equalsIgnoreCase("crate")) {
                        if (args.length > 2) {
                            String crateName = args[1];
                            Player target = Bukkit.getPlayer(args[2]);

                            data.reloadConfig();
                            if (data.getConfig().get(crateName + ".color") != null) {
                                if (target != null) {
                                    ItemStack crate = CreateCrate(crateName);
                                    String color = data.getConfig().getString(crateName + ".color");

                                    target.getInventory().addItem(crate);

                                    player.sendMessage(Main.chatPrefix + ChatColor.GREEN + "Given " + target.getDisplayName() + ChatColor.GREEN + " a crate");
                                    target.sendMessage(Main.chatPrefix + ChatColor.GREEN + "You have been given a " + color + crateName + ChatColor.GREEN + " crate");
                                } else {
                                    player.sendMessage(Main.chatPrefix + ChatColor.RED + "Invalid username");
                                }
                            } else {
                                player.sendMessage(Main.chatPrefix + ChatColor.RED + "Crate doesn't exists");
                            }
                        } else if (args.length > 1) {
                            String crateName = args[1];

                            data.reloadConfig();
                            if (data.getConfig().get(crateName + ".color") != null) {
                                ItemStack crate = CreateCrate(crateName);
                                String color = data.getConfig().getString(crateName + ".color");

                                player.getInventory().addItem(crate);
                                player.sendMessage(Main.chatPrefix + ChatColor.GREEN + "You have been given a " + color + crateName + ChatColor.GREEN + " crate");
                            } else {
                                player.sendMessage(Main.chatPrefix + ChatColor.RED + "Crate doesn't exists");
                            }
                        } else {
                            player.sendMessage(Main.chatPrefix + ChatColor.RED + "Argument required example:" + ChatColor.RESET + " /crate Crate <type> [player]");
                        }
                    } else if (args[0].equalsIgnoreCase("key")) {
                        if (args.length > 3) {
                            String crateName = args[1];

                            data.reloadConfig();
                            if (data.getConfig().get(crateName + ".color") != null) {
                                Player target = Bukkit.getPlayer(args[2]);
                                int amount = Integer.parseInt(args[3]);

                                if (target != null) {
                                    ItemStack key = CreateKey(crateName);
                                    String color = data.getConfig().getString(crateName + ".color");

                                    for (int i = 0; i < amount; i++) {
                                        target.getInventory().addItem(key);
                                    }

                                    player.sendMessage(Main.chatPrefix + ChatColor.GREEN + "Given " + target.getDisplayName() + ChatColor.GREEN + " a key");
                                    target.sendMessage(Main.chatPrefix + ChatColor.GREEN + "You have been given a " + color + crateName + ChatColor.GREEN + " key");
                                } else {
                                    player.sendMessage(Main.chatPrefix + ChatColor.RED + "Invalid username");
                                }
                            } else {
                                player.sendMessage(Main.chatPrefix + ChatColor.RED + "Crate doesn't exists");
                            }
                        } else if (args.length > 2) {
                            String crateName = args[1];

                            data.reloadConfig();
                            if (data.getConfig().get(crateName + ".color") != null) {
                                int amount = Integer.parseInt(args[2]);

                                ItemStack key = CreateKey(crateName);
                                String color = data.getConfig().getString(crateName + ".color");

                                for (int i = 0; i < amount; i++) {
                                    player.getInventory().addItem(key);
                                }
                                player.sendMessage(Main.chatPrefix + ChatColor.GREEN + "You have been given a " + color + crateName + ChatColor.GREEN + " key");
                            } else {
                                player.sendMessage(Main.chatPrefix + ChatColor.RED + "Crate doesn't exists");
                            }
                        } else if (args.length > 1) {
                            String crateName = args[1];

                            data.reloadConfig();
                            if (data.getConfig().get(crateName + ".color") != null) {
                                ItemStack key = CreateKey(crateName);
                                String color = data.getConfig().getString(crateName + ".color");

                                player.getInventory().addItem(key);
                                player.sendMessage(Main.chatPrefix + ChatColor.GREEN + "You have been given a " + color + crateName + ChatColor.GREEN + " key");
                            } else {
                                player.sendMessage(Main.chatPrefix + ChatColor.RED + "Crate doesn't exists");
                            }
                        } else {
                            player.sendMessage(Main.chatPrefix + ChatColor.RED + "Argument required example:" + ChatColor.RESET + " /crate Crate <type> [player] [amount]");
                        }
                    } else {
                        player.sendMessage(Main.chatPrefix + ChatColor.RED + "Invalid argument!");
                    }
                }
            } else {
                player.sendMessage(Main.chatPrefix + ChatColor.RED + "You need to give an argument!");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> options = new ArrayList<>();
        // Admin version
        Player player = (Player) sender;
        if (args.length == 1 && player.hasPermission("cratesPlusReloaded.admin")) {
            options.add("Create");
            options.add("Crate");
            options.add("Key");
            options.add("Settings");
            options.add("Reload");
            options.add("Rename");
            options.add("Delete");
        }
        return options;
    }

    private ItemStack CreateKey(String crateName) {
        ItemStack itemStack = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        data.reloadConfig();
        String color = data.getConfig().getString(crateName + ".color");
        itemMeta.setDisplayName(color + crateName + " Crate Key");

        // Inject name
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(new NamespacedKey(plugin, "name"), PersistentDataType.STRING, crateName);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }


    private ItemStack CreateCrate(String crateName) {
        ItemStack itemStack = new ItemStack(Material.CHEST);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;

        data.reloadConfig();
        String color = data.getConfig().getString(crateName + ".color");
        itemMeta.setDisplayName(color + crateName);

        // Inject name
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(new NamespacedKey(plugin, "name"), PersistentDataType.STRING, crateName);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
