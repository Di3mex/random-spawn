package me.josvth.randomspawn.handlers;


import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Configuration for normal global plugin settings
 */
public class GlobalConfig extends ModularConfig
{
    private final String key = "rootcfg";
    FileConfiguration config;
    File configFile;
    Plugin plugin;

    public GlobalConfig(Plugin plugin)
    {
        this.plugin = plugin;
        configFile = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    @Override
    public void load()
    {
        OPTIONS.clear();
        config = YamlConfiguration.loadConfiguration(configFile);
        loadDefaults(config);
        loadSettings(config);
    }


    @Override
    public void save()
    {
        try
        {
            config = new YamlConfiguration();
            for (ConfigNode node : GlobalConfigNode.values())
                config.set(node.getPath(), OPTIONS.get(key, node));
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


    @Override
    public void loadDefaults(ConfigurationSection config)
    {
        for(ConfigNode node : GlobalConfigNode.values()) {
            if(!config.contains(node.getPath())) {
                config.set(node.getPath(), node.getDefaultValue());
            }
        }
    }


    @Override
    public void loadSettings(ConfigurationSection config)
    {
        for(final ConfigNode node : GlobalConfigNode.values()) {
            updateOption(key, node, config);
        }
    }


    public int getInt (ConfigNode node)
    {
        return getInt(node, key);
    }

    public String getString(ConfigNode node)
    {
        return getString(node, key);
    }

    public List<String> getStringList (ConfigNode node)
    {
        return getStringList(node, key);
    }

    public double getDouble (ConfigNode node)
    {
        return getDouble(node, key);
    }

    public boolean getBoolean (ConfigNode node)
    {
        return getBoolean(node, key);
    }
}
