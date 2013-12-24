package me.josvth.randomspawn.handlers;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import me.josvth.randomspawn.RandomSpawn;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

/**
 * @author Diemex
 */
public class LocationHandler
{
    RandomSpawn plugin;

    File configFile;

    //WorldName, PlayerName, Location(x,z)
    Table<String, String, LiteLocation> locations = HashBasedTable.create();

    public FileConfiguration fileConfiguration;

    public LocationHandler(RandomSpawn instance, File configFile) {
        plugin = instance;
        this.configFile = configFile;
        if (!(this.configFile.exists())){try{
            this.configFile.createNewFile();} catch (IOException e){e.printStackTrace();}}
        loadLocations();
        saveLocations();
    }

    private void loadLocations(){
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
        for (String spawn : fileConfiguration.getKeys(true))
        {
            if (spawn.contains("."))
            {
                String [] splitted = spawn.split("[.]"); //split uses a regex not string
                if (splitted.length > 1)
                {
                    String world = splitted[0];
                    LiteLocation loc = LiteLocation.fromString(fileConfiguration.getString(spawn));
                    loc.playerName = splitted[1];

                    if (loc != null && loc.playerName != null) //parsing could fail
                    {
                        locations.put(world, loc.playerName, loc);
                    }
                }
            }
        }
    }

    private void saveLocations() {
        try {
            //spawnLocations = new YamlConfiguration();
            for (Table.Cell<String, String, LiteLocation> locationCell : locations.cellSet())
                fileConfiguration.set(locationCell.getRowKey() + "." + locationCell.getColumnKey(), locationCell.getValue().toString());
            fileConfiguration.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeLocation (Player player, World world)
    {
        locations.remove(world.getName(), player.getName());
        fileConfiguration.set(world.getName() + "." + player.getName(), null);
        saveLocations();
    }

    public void addLocation(Player player, Location loc)
    {
        locations.put(loc.getWorld().getName(), player.getName(), new LiteLocation(loc.getBlockX(), loc.getBlockZ()));
        saveLocations();
    }


    public Location getLocation(Player player, World world)
    {
        LiteLocation previous = locations.get(world.getName(), player.getName());
        return previous != null ? new Location(world, previous.x, world.getHighestBlockYAt(previous.x, previous.z), previous.z) : null;
    }
}
