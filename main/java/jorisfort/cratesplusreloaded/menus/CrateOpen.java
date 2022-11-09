package jorisfort.cratesplusreloaded.menus;

import jorisfort.cratesplusreloaded.Main;
import jorisfort.cratesplusreloaded.frameworks.DataManager;
import jorisfort.cratesplusreloaded.frameworks.Menu;
import jorisfort.cratesplusreloaded.frameworks.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CrateOpen extends Menu {
    private final Main plugin;
    private final Player player;
    private final DataManager data;
    private final String crateName;
    public CrateOpen(PlayerMenuUtility playerMenuUtility, Main plugin, String crateName, Player player) {
        super(playerMenuUtility);

        this.crateName = crateName;
        this.plugin = plugin;
        this.player = player;
        data = new DataManager(plugin, "crates");
    }

    @Override
    public String getMenuName() {
        data.reloadConfig();
        String color = data.getConfig().getString(crateName + ".color");
        return color + crateName + " Win";
    }

    @Override
    public int getSlots() {
        return 27;
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
        // set lines
        inventory.setItem(4, makeItem(Material.REDSTONE_TORCH, " "));
        inventory.setItem(22, makeItem(Material.REDSTONE_TORCH, " "));

        data.reloadConfig();
        List<ItemStack> items = (List<ItemStack>) data.getConfig().getList(crateName + ".items");
        List<Double> chances = (List<Double>) data.getConfig().getList(crateName + ".chances");
        assert chances != null;

        List<ItemStack> rowOfItems = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            rowOfItems.add(randomItemStack(items, chances, chances.size()));
        }

        // Set row of items


        for (int i = 0; i < 50; i++) {
            int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {
                    inventory.setItem(10, rowOfItems.get(finalI));
                    inventory.setItem(11, rowOfItems.get(1 + finalI));
                    inventory.setItem(12, rowOfItems.get(2 + finalI));
                    inventory.setItem(13, rowOfItems.get(3 + finalI));
                    inventory.setItem(14, rowOfItems.get(4 + finalI));
                    inventory.setItem(15, rowOfItems.get(5 + finalI));
                    inventory.setItem(16, rowOfItems.get(6 + finalI));
                }
            }.runTaskLater(plugin, 2 * i);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                player.getInventory().addItem(rowOfItems.get(52));
            }
        }.runTaskLater(plugin, 100);
    }

    // Utility function to find ceiling of r in arr[l..h]
    private int findCeil(double[] arr, double r, int l, int h)
    {
        int mid;
        while (l < h)
        {
            mid = l + ((h - l) >> 1); // Same as mid = (l+h)/2
            if(r > arr[mid])
                l = mid + 1;
            else
                h = mid;
        }
        return (arr[l] >= r) ? l : -1;
    }

    private ItemStack randomItemStack(List<ItemStack> items, List<Double> chance, int n)
    {
        // Create and fill prefix array
        double[] prefix = new double[n];
        int i;
        prefix[0] = chance.get(0);
        for (i = 1; i < n; ++i)
            prefix[i] = prefix[i - 1] + chance.get(i);

        // prefix[n-1] is sum of all frequencies.
        // Generate a random number with
        // value from 1 to this sum
        double r = (((int)(Math.random()*(323567)) % prefix[n - 1]) + 1);

        // Find index of ceiling of r in prefix array
        int indexc = findCeil(prefix, r, 0, n - 1);
        return items.get(indexc);
    }
}
