package jorisfort.cratesplusreloaded.menus;

import jorisfort.cratesplusreloaded.Main;
import jorisfort.cratesplusreloaded.frameworks.DataManager;
import jorisfort.cratesplusreloaded.frameworks.Menu;
import jorisfort.cratesplusreloaded.frameworks.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class Crates extends Menu {
    private final Main plugin;
    private final DataManager data;
    public Crates(PlayerMenuUtility playerMenuUtility, Main plugin) {
        super(playerMenuUtility);

        this.plugin = plugin;
        data = new DataManager(plugin, "crates");
    }

    @Override
    public String getMenuName() {
        return "CratesPlusReloaded Settings";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public boolean isChangeable() {
        return false;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        String crateName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
        new CrateSettings(Main.getPlayerMenuUtility(player), plugin, crateName).open();
    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {
        data.reloadConfig();
        data.getConfig().getKeys(false).forEach(crate -> {
            // Create crate item
            String color = data.getConfig().getString(crate + ".color");
            inventory.addItem(makeItem(Material.CHEST, color + crate));
        });
    }
}
