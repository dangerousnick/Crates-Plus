package jorisfort.cratesplusreloaded.menus;

import jorisfort.cratesplusreloaded.Main;
import jorisfort.cratesplusreloaded.frameworks.DataManager;
import jorisfort.cratesplusreloaded.frameworks.Menu;
import jorisfort.cratesplusreloaded.frameworks.PlayerMenuUtility;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CrateChances extends Menu {
    private final Main plugin;
    private final DataManager data;
    private final String crateName;
    public CrateChances(PlayerMenuUtility playerMenuUtility, Main plugin, String crateName) {
        super(playerMenuUtility);

        this.plugin = plugin;
        this.crateName = crateName;
        data = new DataManager(plugin, "crates");
    }

    @Override
    public String getMenuName() {
        return "Edit " + crateName + " Crate Chances";
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
        data.reloadConfig();
        List<Double> chances = (List<Double>) data.getConfig().getList(crateName + ".chances");
        assert chances != null;
        int slot = event.getSlot();
        String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
        Double chance = chances.get(slot);

        data.reloadConfig();

        new AnvilGUI.Builder()
                .onComplete((player, text) -> {
                    // OLD CODE
                    /**
                    // Get total percentage
                    Double counter = 0.0;
                    for (int i = 0; i < chances.size(); i++) {
                        if (i != slot) {
                            counter += chances.get(i);
                        }
                    }

                    Double percentage = Double.parseDouble(text);

                    if (counter + percentage == 100.0) {
                        chances.set(slot, percentage);
                        data.getConfig().set(crateName + ".chances", chances);
                        data.saveConfig();

                        new CrateChances(Main.getPlayerMenuUtility(player), plugin, crateName).open();
                        return AnvilGUI.Response.close();
                    } else {
                        player.sendMessage(Main.chatPrefix + ChatColor.RED + "The chances need to add up to 100%");
                    }

                    return AnvilGUI.Response.text("Incorrect value"); **/

                    Double percentage = Double.parseDouble(text);
                    chances.set(slot, percentage);
                    data.getConfig().set(crateName + ".chances", chances);
                    data.saveConfig();

                    new CrateChances(Main.getPlayerMenuUtility(player), plugin, crateName).open();
                    return AnvilGUI.Response.close();
                })
                .title(itemName + " Chance")
                .text(chance.toString())
                .plugin(plugin)
                .itemLeft(new ItemStack(Material.IRON_AXE))
                .open((Player) event.getWhoClicked());
    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {
        // Load the crate items]
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
