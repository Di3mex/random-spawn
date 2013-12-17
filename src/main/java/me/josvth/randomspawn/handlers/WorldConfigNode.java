package me.josvth.randomspawn.handlers;


import org.bukkit.Material;

import java.util.ArrayList;

/**
 * All the available config nodes
 */
public enum WorldConfigNode implements ConfigNode{

    RDM_RESPAWN("random-respawns", VarType.BOOLEAN, true),
    RDM_BEDRESPAWN("random-bedrespawn", VarType.BOOLEAN, true),
    RDM_FIRSTJOIN("random-spawn-on-firstjoin", VarType.BOOLEAN, true),
    RDM_TELEPORT_FROM("random-spawn-on-teleporting-from-this-world", VarType.BOOLEAN, false),
    RDM_TELEPORT_TO("random-spawn-on-teleporting-to-this-world", VarType.BOOLEAN, false),


    SAVE_SPAWN_AS_BED("save-random-spawn-as-bedrespawn", VarType.BOOLEAN, false),
    SAVE_SPAWN_AS_WORLD("save-random-spawn-as-worldspawn(per player)", VarType.BOOLEAN, true),

    RDM_SEARCHTYPE("spawnarea.type", VarType.STRING, "square"),
    RDM_X_MIN("spawnarea.x-min", VarType.DOUBLE, -100.0D),
    RDM_X_MAX("spawnarea.x-max", VarType.DOUBLE, 100.0D),
    RDM_Z_MIN("spawnarea.z-min", VarType.DOUBLE, -100.0D),
    RDM_Z_MAX("spawnarea.z-max", VarType.DOUBLE, 100.0D),
    RDM_THICKNESS("spawnarea.thickness", VarType.INTEGER, 0),

    RESPAWN_X_MIN("radius-around-saved-respawn-location.x-min", VarType.DOUBLE, 0.0D),
    RESPAWN_X_MAX("radius-around-saved-respawn-location.x-max", VarType.DOUBLE, 0.0D),
    RESPAWN_Z_MIN("radius-around-saved-respawn-location.z-min", VarType.DOUBLE, 0.0D),
    RESPAWN_Z_MAX("radius-around-saved-respawn-location.z-max", VarType.DOUBLE, 0.0D),

    WORLDSPAWN_OVERRIDE("fixed-first-spawn.enable", VarType.BOOLEAN, false),
    WORLDSPAWN_X("fixed-first-spawn.x", VarType.DOUBLE, 0.0D),
    WORLDSPAWN_Y("fixed-first-spawn.y", VarType.DOUBLE, 0.0D),
    WORLDSPAWN_Z("fixed-first-spawn.z", VarType.DOUBLE, 0.0D),
    WORLDSPAWN_PITCH("fixed-first-spawn.pitch", VarType.DOUBLE, 0.0D),
    WORLDSPAWN_YAW("fixed-first-spawn.yaw", VarType.DOUBLE, 0.0D),

    BLACKLISTED_BLOCKS("spawnblacklist", VarType.MATERIAL_LIST, new DefaultBlackList());

    /**
     * Path in the config
     */
    private final String path;

    /**
     * Variable type.
     */
    private final VarType type;

    /**
     * Default value.
     */
    private final Object defaultValue;


    /**
     * Constructor.
     *
     * @param path - Configuration path.
     * @param type - Variable type.
     * @param def  - Default value.
     */
    private WorldConfigNode(String path, VarType type, Object def)
    {
        this.path = path;
        this.type = type;
        this.defaultValue = def;
    }


    /**
     * Get the config path.
     *
     * @return Config path with a dot at the beginning
     */
    public String getPath(){
        return "." + path;
    }


    /**
     * Get the variable type.
     *
     * @return Variable type.
     */
    public VarType getVarType(){
        return type;
    }


    /**
     * Get the default value.
     *
     * @return Default value.
     */
    public Object getDefaultValue(){
        return defaultValue;
    }

    private static class DefaultBlackList extends ArrayList<String>{
        public DefaultBlackList()
        {
            super();
            add(Material.WATER.toString());
            add(Material.STATIONARY_WATER.toString());
            add(Material.LAVA.toString());
            add(Material.STATIONARY_LAVA.toString());
            add(Material.LEAVES.toString());
            add(Material.FIRE.toString());
            add(Material.CACTUS.toString());
        }
    }
}
