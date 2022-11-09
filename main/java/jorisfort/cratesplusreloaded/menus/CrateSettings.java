package jorisfort.cratesplusreloaded.menus;

import jorisfort.cratesplusreloaded.Main;
import jorisfort.cratesplusreloaded.frameworks.DataManager;
import jorisfort.cratesplusreloaded.frameworks.Menu;
import jorisfort.cratesplusreloaded.frameworks.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class CrateSettings extends Menu {
    private final Main plugin;
    private final String crateName;
    private final DataManager data;
    public CrateSettings(PlayerMenuUtility playerMenuUtility, Main plugin, String crateName) {
        super(playerMenuUtility);

        this.plugin = plugin;
        this.crateName = crateName;
        data = new DataManager(plugin, "crates");
    }

    @Override
    public String getMenuName() {
        return "Edit " + crateName + " Crate";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public boolean isChangeable() {
        return false;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        switch (e.getCurrentItem().getType()) {
            case DIAMOND -> new CrateWinnings(Main.getPlayerMenuUtility(player), plugin, crateName).open();
            case CYAN_WOOL -> new CrateColor(Main.getPlayerMenuUtility(player), plugin, crateName).open();
            case GOLD_INGOT -> new CrateChances(Main.getPlayerMenuUtility(player), plugin, crateName).open();
        }
    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {
        // make items
        inventory.setItem(1, makeItem(Material.DIAMOND, "Edit Crate Winnings"));
        inventory.setItem(4, makeItem(Material.CYAN_WOOL, "Edit Crate Color"));
        inventory.setItem(7, makeItem(Material.GOLD_INGOT, "Edit Item Percentages"));
    }
}
