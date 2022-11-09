package jorisfort.cratesplusreloaded;

import jorisfort.cratesplusreloaded.Handlers.MenuHandler;
import jorisfort.cratesplusreloaded.Handlers.PlayerHandler;
import jorisfort.cratesplusreloaded.commands.Crate;
import jorisfort.cratesplusreloaded.frameworks.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin {
    public static String chatPrefix = ChatColor.GRAY + "[" + ChatColor.AQUA + "CratesPlusReloaded" + ChatColor.GRAY + "] " + ChatColor.RESET;
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    @Override
    public void onEnable() {
        // Plugin startup logic
        new PlayerHandler(this);
        new MenuHandler(this);
        new Crate(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) { //See if the player has a player menu utility "saved" for them

            //This player doesn't. Make one for them add it to the hashmap
            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p); //Return the object by using the provided player
        }
    }
}