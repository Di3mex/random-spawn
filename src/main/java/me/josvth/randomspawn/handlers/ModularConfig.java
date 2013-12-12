package me.josvth.randomspawn.handlers;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

/**
 * Modular configuration class that utilizes a ConfigNode enumeration as easy access and storage of configuration option values.
 *
 * @author Mitsugaru
 */
public abstract class ModularConfig
{
    /**
     * Cache of options for the config.
     */
    protected final Table<String, ConfigNode, Object> OPTIONS = HashBasedTable.create();


    public int getInt(final ConfigNode node, World world)
    {
        return getInt(node, world.getName());
    }


    /**
     * Get the integer value of the node.
     *
     * @param node - Node to use.
     *
     * @return Value of the node. Returns -1 if unknown.
     */
    public int getInt(final ConfigNode node, String worldName)
    {
        int i = -1;
        switch (node.getVarType())
        {
            case INTEGER:
            {
                try
                {
                    i = (Integer) OPTIONS.get(node, worldName);
                } catch (NullPointerException npe)
                {
                    if (enabledFor(worldName))
                        i = (Integer) node.getDefaultValue();
                    else
                        i = (Integer) disableValue(node);
                }
                break;
            }
            default:
            {
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + node.getVarType() + " as an integer.");
            }
        }
        return i;
    }


    public String getString(final ConfigNode node, World world)
    {
        return getString(node, world.getName());
    }


    /**
     * Get the string value of the node.
     *
     * @param node - Node to use.
     *
     * @return Value of the node. Returns and empty string if unknown.
     */
    public String getString(final ConfigNode node, String worldName)
    {
        String out = "";
        switch (node.getVarType())
        {
            case STRING:
            {
                try
                {
                    out = (String) OPTIONS.get(node, worldName);
                } catch (NullPointerException npe)
                {
                    if (enabledFor(worldName))
                        out = (String) node.getDefaultValue();
                    else
                        out = (String) disableValue(node);
                }
                break;
            }
            default:
            {
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + node.getVarType() + " as a string.");
            }
        }
        return out;
    }


    public List<String> getStringList(final ConfigNode node, World world)
    {
        return getStringList(node, world.getName());
    }


    /**
     * Get the list value of the node.
     *
     * @param node - Node to use.
     *
     * @return Value of the node. Returns an empty list if unknown.
     */
    @SuppressWarnings("unchecked")
    public List<String> getStringList(final ConfigNode node, String worldName)
    {
        //TODO
        List<String> list = new ArrayList<String>();
        switch (node.getVarType())
        {
            case LIST:
            {
                try
                {
                    list = (List) OPTIONS.get(node, worldName);
                } catch (NullPointerException npe)
                {
                    if (enabledFor(worldName))
                        list = (List) node.getDefaultValue();
                    else
                        list = (List) disableValue(node);
                }
                break;
            }
            default:
            {
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + node.getVarType() + " as a List<String>.");
            }
        }
        return list;
    }


    public double getDouble(final ConfigNode node, World world)
    {
        return getDouble(node, world.getName());
    }


    /**
     * Get the double value of the node.
     *
     * @param node - Node to use.
     *
     * @return Value of the node. Returns 0 if unknown.
     */
    public double getDouble(final ConfigNode node, String worldName)
    {
        double d = 0.0;
        switch (node.getVarType())
        {
            case DOUBLE:
            {
                try
                {
                    d = (Double) OPTIONS.get(node, worldName);
                } catch (NullPointerException npe)
                {
                    if (enabledFor(worldName))
                        d = (Double) node.getDefaultValue();
                    else
                        d = (Double) disableValue(node);
                }
                break;
            }
            default:
            {
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + node.getVarType() + " as a double.");
            }
        }
        return d;
    }


    public boolean getBoolean(final ConfigNode node, World world)
    {
        return getBoolean(node, world.getName());
    }


    /**
     * Get the boolean value of the node.
     *
     * @param node - Node to use.
     *
     * @return Value of the node. Returns false if unknown.
     */
    public boolean getBoolean(final ConfigNode node, String worldName)
    {
        boolean bool = false;
        switch (node.getVarType())
        {
            case BOOLEAN:
            {
                try
                {
                    bool = (Boolean) OPTIONS.get(node, worldName);
                } catch (NullPointerException npe)
                {
                    if (enabledFor(worldName))
                        bool = (Boolean) node.getDefaultValue();
                    else
                        bool = (Boolean) disableValue(node);
                }
                break;
            }
            default:
            {
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + node.getVarType() + " as a boolean.");
            }
        }
        return bool;
    }


    public List<Integer> getIntegerList(final ConfigNode node, World world)
    {
        return getIntegerList(node, world.getName());
    }


    /**
     * Gets a list of integers
     *
     * @param node      - Node to use.
     * @param worldName - name of world
     *
     * @return list of materials
     */
    @SuppressWarnings("unchecked")
    public List<Integer> getIntegerList(final ConfigNode node, String worldName)
    {
        List<Integer> list = new ArrayList<Integer>();
        switch (node.getVarType())
        {
            case MATERIAL_LIST:
            {
                try
                {
                    list = (List<Integer>) OPTIONS.get(node, worldName);
                } catch (NullPointerException npe)
                {
                    if (enabledFor(worldName))
                        list = (List<Integer>) node.getDefaultValue();
                    else
                        list = (List<Integer>) disableValue(node);
                }
                break;
            }
            default:
            {
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + node.getVarType() + " as a List<Integer>.");
            }
        }
        return list;
    }


    /**
     * This updates a configuration option from the file.
     *
     * @param node - ConfigNode to update.
     */
    @SuppressWarnings("unchecked")
    protected void updateOption(final String world, final ConfigNode node, final ConfigurationSection config)
    {
        switch (node.getVarType())
        {
            case LIST:
            {
                List<String> list = config.getStringList(node.getPath());
                if (list == null)
                {
                    list = (List<String>) node.getDefaultValue();
                }
                OPTIONS.put(world, node, list);
                break;
            }
            case MATERIAL_LIST:
            {
                List<String> list = config.getStringList(node.getPath());
                List<Integer> materials = new ArrayList<Integer>();
                if (list != null && list.size() > 0)
                    for (String material : list)
                    {
                        int id = MaterialParser.parseMaterial(material);
                        if (id > 0) //if smaller it's invalid
                            materials.add(id);
                    }
                OPTIONS.put(world, node, materials);
            }
            case DOUBLE:
            {
                OPTIONS.put(world, node, config.getDouble(node.getPath(), (Double) node.getDefaultValue()));
                break;
            }
            case STRING:
            {
                OPTIONS.put(world, node, config.getString(node.getPath(), (String) node.getDefaultValue()));
                break;
            }
            case INTEGER:
            {
                OPTIONS.put(world, node, config.getInt(node.getPath(), (Integer) node.getDefaultValue()));
                break;
            }
            case BOOLEAN:
            {
                OPTIONS.put(world, node, config.getBoolean(node.getPath(), (Boolean) node.getDefaultValue()));
                break;
            }
            default:
            {
                OPTIONS.put(world, node, config.get(node.getPath(), node.getDefaultValue()));
            }
        }
    }

    /**
     * Force set a given value
     *
     * @param world world instance
     * @param node  node to set
     * @param value value to set the node for the given world
     */
    public void set(final World world, final ConfigNode node, final Object value)
    {
        set(world.getName(), node, value);
    }

    /**
     * Force set a given value
     *
     * @param world worldname
     * @param node  node to set
     * @param value value to set the node for the given world
     */
    public void set(final String world, final ConfigNode node, final Object value)
    {
        try
        {
            switch (node.getVarType())
            {
                case LIST:
                {
                    OPTIONS.put(world, node, (List) value);
                    break;
                }
                case MATERIAL_LIST:
                {
                    OPTIONS.put(world, node, (List) value);
                }
                case DOUBLE:
                {
                    OPTIONS.put(world, node, (Double) value);
                    break;
                }
                case STRING:
                {
                    OPTIONS.put(world, node, (String) value);
                    break;
                }
                case INTEGER:
                {
                    OPTIONS.put(world, node, (Integer) value);
                    break;
                }
                case BOOLEAN:
                {
                    OPTIONS.put(world, node, (Boolean) value);
                    break;
                }
                default:
                {
                    throw new UnsupportedOperationException("Vartype " + node.getVarType() + " has no set() method defined");
                }
            }
        }
        //Inputted wrong type
        catch (ClassCastException e)
        {
            throw new IllegalArgumentException("Inputted object was of type " + value.getClass().getName() + " but we expected " + node.getVarType().name());
        }
    }

    public boolean enabledFor(String worldName)
    {
        return OPTIONS.containsRow(worldName);
    }

    public boolean enabledFor (World world)
    {
        return enabledFor(world.getName());
    }


    /**
     * Get the value for the given object that will disable the feature in code
     *
     * @param node node to check
     *
     * @return object that disables
     */
    private Object disableValue(ConfigNode node)
    {
        switch (node.getVarType())
        {
            case LIST:
            {
                return new ArrayList<String>();
            }
            case MATERIAL_LIST:
            {
                return new ArrayList<Integer>();
            }
            case BOOLEAN:
            {
                return false;
            }
            case INTEGER:
            {
                return 0;
            }
            case DOUBLE:
            {
                return 0.0D;
            }
            case STRING:
            {
                return "";
            }
            default:
                throw new UnsupportedOperationException("Node with type " + node.getVarType().name() + " has no value set which will disble it");
        }
    }

    /**
     * Perform loading and initializing
     */
    public abstract void load();

    /**
     * Saves the config.
     */
    public abstract void save();

    /**
     * Reloads info from yaml file(s).
     */
    public abstract void reload();

    /**
     * Update settings that can be changed on the fly.
     *
     * @param config - Main config to load from.
     */
    public abstract void loadSettings(final ConfigurationSection config);

    /**
     * Load defaults.
     *
     * @param config - Main config to load to.
     */
    public abstract void loadDefaults(final ConfigurationSection config);
}
