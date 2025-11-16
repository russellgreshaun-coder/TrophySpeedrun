package com.trophy;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class SpeedrunPlugin extends JavaPlugin {

    private Location trophyLocation = null;

    @Override
    public void onEnable() {
        getLogger().info("Trophy Speedrun Plugin Enabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return true;

        Player speedrunner = (Player) sender;

        if (args.length == 0 || !args[0].equalsIgnoreCase("start")) {
            speedrunner.sendMessage("§cUse: /speedrun start");
            return true;
        }

        World world = speedrunner.getWorld();

        int x = 5000;
        int z = 0;
        int y = world.getHighestBlockYAt(x, z) + 1;

        trophyLocation = new Location(world, x, y, z);

        world.getBlockAt(x, y, z).setType(Material.GOLD_BLOCK);
        world.getBlockAt(x, y - 1, z).setType(Material.STONE);

        ItemStack compass = new ItemStack(Material.COMPASS);
        CompassMeta meta = (CompassMeta) compass.getItemMeta();
        meta.setLodestone(trophyLocation);
        meta.setLodestoneTracked(false);
        compass.setItemMeta(meta);

        speedrunner.getInventory().addItem(compass);

        speedrunner.sendMessage("§aTrophy spawned at 5000 blocks! Mine the gold block to win!");

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p == speedrunner) continue;

            ItemStack hunterCompass = new ItemStack(Material.COMPASS);
            CompassMeta m2 = (CompassMeta) hunterCompass.getItemMeta();
            m2.setLodestone(speedrunner.getLocation());
            m2.setLodestoneTracked(false);
            hunterCompass.setItemMeta(m2);

            p.getInventory().addItem(hunterCompass);
            p.sendMessage("§cTrack the speedrunner!");
        }

        return true;
    }
}
