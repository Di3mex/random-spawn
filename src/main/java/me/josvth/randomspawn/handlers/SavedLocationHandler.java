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

public class SavedLocationHandler
{

	RandomSpawn plugin;

    File spawnLocationsFile;

    //WorldName, PlayerName, Location(x,z)
    Table<String, String, LiteLocation> previousSpawnLocations = HashBasedTable.create();

    public FileConfiguration spawnLocations;
	
	public SavedLocationHandler(RandomSpawn instance) {
		plugin = instance;
        spawnLocationsFile = new File(this.plugin.getDataFolder(), "spawnLocations.yml");
        if (!(spawnLocationsFile.exists())){try{spawnLocationsFile.createNewFile();} catch (IOException e){e.printStackTrace();}}
        loadSpawnLocations();
        saveSpawnLocations();
	}

    public void loadSpawnLocations(){
        spawnLocations = YamlConfiguration.loadConfiguration(spawnLocationsFile);
        for (String spawn : spawnLocations.getKeys(true))
        {
            if (spawn.contains("."))
            {
                String [] splitted = spawn.split("[.]"); //split uses a regex not string
                String world = splitted[0];
                LiteLocation loc = LiteLocation.fromString(spawnLocations.getString(spawn));

                if (loc != null) //parsing could fail
                {
                    previousSpawnLocations.put(world, loc.playerName, loc);
                }
            }
        }
    }
    
    public void saveSpawnLocations() {
        try {
            //spawnLocations = new YamlConfiguration();
            for (Table.Cell<String, String, LiteLocation> locationCell : previousSpawnLocations.cellSet())
                spawnLocations.set(locationCell.getRowKey() + "." + locationCell.getColumnKey(), locationCell.getValue().toString());
            spawnLocations.save(spawnLocationsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addSpawnLocation(Player player, Location loc)
    {
        previousSpawnLocations.put(loc.getWorld().getName(), player.getName(), new LiteLocation(loc.getBlockX(), loc.getBlockZ()));
        saveSpawnLocations();
    }


    public Location getSpawnLocation(Player player, World world)
    {
        LiteLocation previous = previousSpawnLocations.get(world.getName(), player.getName());
        return previous != null ? new Location(world, previous.x, world.getHighestBlockYAt(previous.x, previous.z), previous.z) : null;
    }
}
