package me.josvth.randomspawn.handlers;


import java.util.ArrayList;

/**
 * @author Diemex
 */
public enum GlobalConfigNode implements ConfigNode
{
    DEBUG("debug", VarType.BOOLEAN, false),
    SHOW_RDM_SPAWN_MSG("randomspaned-msg.enable", VarType.BOOLEAN, true),
    RDM_SPAWN_MSGS("messages.randomspawned", VarType.LIST, new DefaultRdmSpawnMsgs()),
    NO_DMG_TIME("nodamagetime", VarType.INTEGER, 5),
    SIGN_TEXT("rs-sign-text", VarType.STRING, "[RandomSpawn]")
    ;
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
    private GlobalConfigNode(String path, VarType type, Object def)
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

    public static class DefaultRdmSpawnMsgs extends ArrayList<String>
    {
        public DefaultRdmSpawnMsgs()
        {
            add("You wake up in an unfamiliar place.");
        }
    }
}
