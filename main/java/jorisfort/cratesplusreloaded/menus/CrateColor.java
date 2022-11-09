package jorisfort.cratesplusreloaded.menus;

import jorisfort.cratesplusreloaded.Main;
import jorisfort.cratesplusreloaded.frameworks.DataManager;
import jorisfort.cratesplusreloaded.frameworks.Menu;
import jorisfort.cratesplusreloaded.frameworks.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class CrateColor extends Menu {
    private final DataManager data;
    private final String crateName;
    public CrateColor(PlayerMenuUtility playerMenuUtility, Main plugin, String crateName) {
        super(playerMenuUtility);

        this.crateName = crateName;
        data = new DataManager(plugin, "crates");
    }

    @Override
    public String getMenuName() {
        return "Edit Crate Color";
    }

    @Override
    public int getSlots() {
        return 18;
    }

    @Override
    public boolean isChangeable() {
        return false;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        switch (event.getCurrentItem().getType()) {
            case BLACK_WOOL -> data.getConfig().set(crateName + ".color", "§0");
            case BLUE_WOOL -> data.getConfig().set(crateName + ".color", "§1");
            case GREEN_CONCRETE -> data.getConfig().set(crateName + ".color", "§2");
            case CYAN_CONCRETE -> data.getConfig().set(crateName + ".color", "§3");
            case RED_CONCRETE -> data.getConfig().set(crateName + ".color", "§4");
            case PURPLE_WOOL -> data.getConfig().set(crateName + ".color", "§5");
            case ORANGE_WOOL -> data.getConfig().set(crateName + ".color", "§6");
            case LIGHT_GRAY_WOOL -> data.getConfig().set(crateName + ".color", "§7");
            case GRAY_WOOL -> data.getConfig().set(crateName + ".color", "§8");
            case LIGHT_BLUE_WOOL -> data.getConfig().set(crateName + ".color", "§9");
            case GREEN_WOOL -> data.getConfig().set(crateName + ".color", "§a");
            case CYAN_WOOL -> data.getConfig().set(crateName + ".color", "§b");
            case RED_WOOL -> data.getConfig().set(crateName + ".color", "§c");
            case PINK_WOOL -> data.getConfig().set(crateName + ".color", "§d");
            case YELLOW_WOOL -> data.getConfig().set(crateName + ".color", "§e");
            case WHITE_WOOL -> data.getConfig().set(crateName + ".color", "§f");
        }
        data.saveConfig();
    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {
        inventory.addItem(makeItem(Material.BLACK_WOOL, "§0Black"));
        inventory.addItem(makeItem(Material.BLUE_WOOL, "§1Dark Blue"));
        inventory.addItem(makeItem(Material.GREEN_CONCRETE, "§2Dark Green"));
        inventory.addItem(makeItem(Material.CYAN_CONCRETE, "§3Dark Aqua"));
        inventory.addItem(makeItem(Material.RED_CONCRETE, "§4Dark Red"));
        inventory.addItem(makeItem(Material.PURPLE_WOOL, "§5Dark Purple"));
        inventory.addItem(makeItem(Material.ORANGE_WOOL, "§6Orange"));
        inventory.addItem(makeItem(Material.LIGHT_GRAY_WOOL, "§7Gray"));
        inventory.addItem(makeItem(Material.GRAY_WOOL, "§8Dark Gray"));
        inventory.setItem(10, makeItem(Material.LIGHT_BLUE_WOOL, "§9Blue"));
        inventory.setItem(11, makeItem(Material.GREEN_WOOL, "§aGreen"));
        inventory.setItem(12, makeItem(Material.CYAN_WOOL, "§bAqua"));
        inventory.setItem(13, makeItem(Material.RED_WOOL, "§cRed"));
        inventory.setItem(14, makeItem(Material.PINK_WOOL, "§dPink"));
        inventory.setItem(15, makeItem(Material.YELLOW_WOOL, "§eYellow"));
        inventory.setItem(16, makeItem(Material.WHITE_WOOL, "§fWhite"));
    }
}
