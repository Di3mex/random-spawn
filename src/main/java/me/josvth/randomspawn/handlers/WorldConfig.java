package me.josvth.randomspawn.handlers;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Multiworld implementation of {@link me.josvth.randomspawn.handlers.ModularConfig},
 * stores values on a per world basis
 */
public class WorldConfig extends ModularConfig
{
    FileConfiguration config;
    File configFile;
    Plugin plugin;

    public WorldConfig (Plugin plugin)
    {
        this.plugin = plugin;
        configFile = new File (plugin.getDataFolder(), "worlds.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
    }


    @Override
    public void load()
    {
        loadDefaults(config);
        loadSettings(config);
    }


    @Override
    public void save()
    {
        try
        {
            config.save(configFile);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void reload()
    {
        load();
        save();
    }

    private String[] getAvailableWorlds (ConfigurationSection config)
    {
        Set<String> worlds = new HashSet<String>();
        for (String key : config.getKeys(false))
        {
            String [] sections = key.split(".");
            if (sections.length > 0 && sections[0].length() > 0)
                worlds.add(sections[0]);
        }
        return worlds.toArray(new String[worlds.size()]);
    }

    @Override
    public void loadDefaults(ConfigurationSection config)
    {
        //Iterate trough all available worlds in the config and afterwards iterate through all the confignodes
        for(String world : getAvailableWorlds(config))
        {
            for(ConfigNode node : WorldConfigNode.values()) {
                if(!config.contains(world + "." + node.getPath())) {
                    config.set(world + "." + node.getPath(), node.getDefaultValue());
                }
            }
        }
    }


    @Override
    public void loadSettings(ConfigurationSection config)
    {
        for(String world : getAvailableWorlds(config))
        {
            for(final WorldConfigNode node : WorldConfigNode.values()) {
                updateOption(world, node, config);
            }
        }
    }


    private Map<String, Location> bufferedSpawns = new HashMap<String, Location>();

    /**
     * Buffered method for getting an overriden spawn location
     *
     * @param world world to get the spawn for
     *
     * @return worldspawn or overriden worldspawn
     */
    public Location getFirstSpawn(World world) {
        if (getBoolean(WorldConfigNode.WORLDSPAWN_OVERRIDE, world)){

            if (bufferedSpawns.containsKey(world.getName()))
                return bufferedSpawns.get(world.getName());
            else
            {
                double x = getDouble(WorldConfigNode.WORLDSPAWN_X, world);
                double y = getDouble(WorldConfigNode.WORLDSPAWN_Y, world);
                double z = getDouble(WorldConfigNode.WORLDSPAWN_Z, world);

                double dyaw = getDouble(WorldConfigNode.WORLDSPAWN_YAW, world);
                double dpitch = getDouble(WorldConfigNode.WORLDSPAWN_PITCH, world);

                float yaw = (float) dyaw;
                float pitch = (float) dpitch;

                Location loc = new Location(world, x, y, z, yaw, pitch);

                bufferedSpawns.put(world.getName(), loc);

                return loc;
            }
        }
        return world.getSpawnLocation();
    }
}
