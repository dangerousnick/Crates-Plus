package jorisfort.cratesplusreloaded.Handlers;

import jorisfort.cratesplusreloaded.Main;
import jorisfort.cratesplusreloaded.frameworks.DataManager;
import jorisfort.cratesplusreloaded.menus.CrateOpen;
import jorisfort.cratesplusreloaded.menus.CratePreview;
import org.bukkit.*;
import org.bukkit.block.TileState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerHandler implements Listener {
    private final Main plugin;
    private final DataManager data;
    public PlayerHandler(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);

        this.plugin = plugin;
        data = new DataManager(plugin, "crates");
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        ItemStack itemStack = event.getItemInHand();
        if (!isCrate(itemStack)) return;

        // Get crate name
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        String crateName = itemMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, "name"), PersistentDataType.STRING);
        assert crateName != null;


        TileState state = (TileState) event.getBlock().getState();
        PersistentDataContainer container = state.getPersistentDataContainer();

        container.set(new NamespacedKey(plugin, "name"), PersistentDataType.STRING, crateName);
        state.update();

        // Place holograms
        data.reloadConfig();
        String color = data.getConfig().getString(crateName + ".color");
        Location crateLoc = event.getBlock().getLocation().add(0.5, -0.5, 0.5);

        ArmorStand name = (ArmorStand) event.getBlock().getWorld().spawnEntity(crateLoc, EntityType.ARMOR_STAND);
        name.setVisible(false);
        name.setInvulnerable(true);
        name.setCollidable(false);
        name.setAI(false);
        name.setGravity(false);
        name.setCustomName(color + crateName);
        name.setCustomNameVisible(true);

        Location crateLoc1 = event.getBlock().getLocation().add(0.5, -0.75, 0.5);

        ArmorStand name1 = (ArmorStand) event.getBlock().getWorld().spawnEntity(crateLoc1, EntityType.ARMOR_STAND);
        name1.setVisible(false);
        name1.setInvulnerable(true);
        name1.setCollidable(false);
        name1.setAI(false);
        name1.setGravity(false);
        name1.setCustomName("Right-Click to Open!");
        name1.setCustomNameVisible(true);

        Location crateLoc2 = event.getBlock().getLocation().add(0.5, -1, 0.5);

        ArmorStand name2 = (ArmorStand) event.getBlock().getWorld().spawnEntity(crateLoc2, EntityType.ARMOR_STAND);
        name2.setVisible(false);
        name2.setInvulnerable(true);
        name2.setCollidable(false);
        name2.setAI(false);
        name2.setGravity(false);
        name2.setCustomName("Left-Click to Preview!");
        name2.setCustomNameVisible(true);
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasBlock()) return;
        if (event.getClickedBlock().getType() != Material.CHEST) return;

        if (event.getClickedBlock().getState() instanceof TileState state) {
            PersistentDataContainer container = state.getPersistentDataContainer();

            // If chest is a crate
            if (container.has(new NamespacedKey(plugin, "name"), PersistentDataType.STRING)) {
                String crateName = container.get(new NamespacedKey(plugin, "name"), PersistentDataType.STRING);
                Player player = event.getPlayer();

                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    if (player.hasPermission("cratesPlusReloaded.admin") && player.isSneaking()) {
                        event.setCancelled(false);
                    } else {
                        event.setCancelled(true);
                        new CratePreview(Main.getPlayerMenuUtility(event.getPlayer()), plugin, crateName).open();
                    }
                } else {
                    event.setCancelled(true);
                    ItemStack itemStack = event.getItem();
                    data.reloadConfig();
                    String color = data.getConfig().getString(crateName + ".color");

                    if (itemStack != null) {
                        if (isCorrectKey(itemStack, crateName) && itemStack.getType() == Material.TRIPWIRE_HOOK) {
                            // Remove key from inv
                            itemStack.setAmount(itemStack.getAmount() - 1);

                            new CrateOpen(Main.getPlayerMenuUtility(player), plugin, crateName, player).open();
                        } else {
                            player.sendMessage(Main.chatPrefix + ChatColor.RED + "You must be holding a " + color + crateName + ChatColor.RED + " key to open this crate");
                        }
                    } else {
                        player.sendMessage(Main.chatPrefix + ChatColor.RED + "You must be holding a " + color + crateName + ChatColor.RED + " key to open this crate");
                    }
                }
            }
        }
    }

    private boolean isCrate(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        return container.has(new NamespacedKey(plugin, "name"), PersistentDataType.STRING);
    }

    private boolean isCorrectKey(ItemStack itemStack, String crateName) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        // If not a key return
        if (!container.has(new NamespacedKey(plugin, "name"), PersistentDataType.STRING)) return false;

        String keyName = container.get(new NamespacedKey(plugin, "name"), PersistentDataType.STRING);

        return crateName.equals(keyName);
    }
}