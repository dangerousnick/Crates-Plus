package jorisfort.cratesplusreloaded.menus;

import jorisfort.cratesplusreloaded.Main;
import jorisfort.cratesplusreloaded.frameworks.DataManager;
import jorisfort.cratesplusreloaded.frameworks.Menu;
import jorisfort.cratesplusreloaded.frameworks.PlayerMenuUtility;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CratePreview extends Menu {
    private final DataManager data;
    private final String crateName;
    public CratePreview(PlayerMenuUtility playerMenuUtility, Main plugin, String crateName) {
        super(playerMenuUtility);

        data = new DataManager(plugin, "crates");
        this.crateName = crateName;
    }

    @Override
    public String getMenuName() {
        data.reloadConfig();
        String color = data.getConfig().getString(crateName + ".color");
        return color + crateName + " Possible Wins:";
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
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {
        // Load the crate items
        data.reloadConfig();
        List<ItemStack> items = (List<ItemStack>) data.getConfig().getList(crateName + ".items");
        List<Double> chances = (List<Double>) data.getConfig().getList(crateName + ".chances");
        assert items != null;
        assert chances != null;

        for (int i = 0; i < chances.size(); i++) {
            ItemStack itemStack = items.get(i);
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            List<String> lore = new ArrayList<>();
            lore.add(" ");
            lore.add("Chance: " + chances.get(i) + "%");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            inventory.addItem(itemStack);
        }
    }
}
