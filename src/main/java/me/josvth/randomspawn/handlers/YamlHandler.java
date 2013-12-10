package me.josvth.randomspawn.handlers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import me.josvth.randomspawn.RandomSpawn;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class YamlHandler{

	RandomSpawn plugin;

	File configFile;
    File worldsFile;
    File spawnLocationsFile;

    //WorldName, PlayerName, Location(x,z)
    Table<String, String, LiteLocation> previousSpawnLocations = HashBasedTable.create();

    public FileConfiguration config;
    public FileConfiguration worlds;
    public FileConfiguration spawnLocations;
	
	public YamlHandler(RandomSpawn instance) {
		plugin = instance;
		setupYamls();
        loadYamls();
	}
	
	public void setupYamls(){
		configFile = new File(this.plugin.getDataFolder(), "config.yml");
        worldsFile = new File(this.plugin.getDataFolder(), "worlds.yml");
        spawnLocationsFile = new File(this.plugin.getDataFolder(), "spawnLocations.yml");
		
        if (!(configFile.exists())){this.plugin.saveResource("config.yml", false);}				// loads default config's on first run
        if (!(worldsFile.exists())){this.plugin.saveResource("worlds.yml", false);}
        if (!(spawnLocationsFile.exists())){try{spawnLocationsFile.createNewFile();} catch (IOException e){e.printStackTrace();}}
	
	}
	
	public void loadYamls() {

        config = YamlConfiguration.loadConfiguration(configFile);
        worlds = YamlConfiguration.loadConfiguration(worldsFile);
        loadSpawnLocations();

    }

    public void loadSpawnLocations(){
        spawnLocations = YamlConfiguration.loadConfiguration(spawnLocationsFile);
        for (String spawn : spawnLocations.getKeys(true))
        {
            if (spawn.contains("."))
            {
                String world = spawn.split(".")[0];
                LiteLocation loc = LiteLocation.fromString(spawnLocations.getString(spawn));

                if (loc != null) //parsing could fail
                {
                    previousSpawnLocations.put(world, loc.playerName, loc);
                }
            }
        }
    }
    
    public void saveYamls() {
        saveConfig();
        saveWorlds();
        saveSpawnLocations();
    }
    
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveWorlds() {
        try {
            worlds.save(worldsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveSpawnLocations() {
        try {
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
    }


    public Location getSpawnLocation(Player player, World world)
    {
        LiteLocation previous = previousSpawnLocations.get(world.getName(), player.getName());
        return previous != null ? new Location(world, previous.x, world.getHighestBlockYAt(previous.x, previous.z), previous.z) : null;
    }


    public Location getFirstSpawn(World world) {
        String worldName = world.getName();

        if (plugin.yamlHandler.worlds.contains(worldName +".firstspawn")){

            double x = plugin.yamlHandler.worlds.getDouble(worldName+".firstspawn.x");
            double y = plugin.yamlHandler.worlds.getDouble(worldName+".firstspawn.y");
            double z = plugin.yamlHandler.worlds.getDouble(worldName+".firstspawn.z");

            double dyaw = plugin.yamlHandler.worlds.getDouble(worldName+".firstspawn.yaw");
            double dpitch = plugin.yamlHandler.worlds.getDouble(worldName+".firstspawn.pitch");

            float yaw = (float) dyaw;
            float pitch = (float) dpitch;

            return new Location(world, x, y, z, yaw, pitch);
        }
        return world.getSpawnLocation();
    }
}
