package jorisfort.cratesplusreloaded.menus;

import jorisfort.cratesplusreloaded.Main;
import jorisfort.cratesplusreloaded.frameworks.DataManager;
import jorisfort.cratesplusreloaded.frameworks.Menu;
import jorisfort.cratesplusreloaded.frameworks.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CrateWinnings extends Menu {
    private final DataManager data;
    private final String crateName;
    public CrateWinnings(PlayerMenuUtility playerMenuUtility, Main plugin, String crateName) {
        super(playerMenuUtility);

        this.crateName = crateName;
        data = new DataManager(plugin, "crates");
    }

    @Override
    public String getMenuName() {
        return "Edit " + crateName + " Crate Winnings";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public boolean isChangeable() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) != null) {
                ItemStack itemStack = inventory.getItem(i);
                items.add(itemStack);
            }
        }
        data.getConfig().set(crateName + ".items", items);

        // Set Chances
        List<Double> chances = new ArrayList<>();
        Double chance = 100.0 / items.size();
        for (int i = 0; i < items.size(); i++) {
            chances.add(chance);
        }
        data.getConfig().set(crateName + ".chances", chances);
        data.saveConfig();

        event.getPlayer().sendMessage(Main.chatPrefix + ChatColor.GREEN + "Successfully changed crate winnings");
    }

    @Override
    public void setMenuItems() {
        // Load the crate items
        data.reloadConfig();
        List<ItemStack> items = (List<ItemStack>) data.getConfig().getList(crateName + ".items");

        assert items != null;
        for (ItemStack itemStack : items) {
            inventory.addItem(itemStack);
        }
    }
}
