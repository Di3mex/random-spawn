package me.josvth.randomspawn;


import me.josvth.randomspawn.handlers.*;
import me.josvth.randomspawn.listeners.Listeners;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class RandomSpawn extends JavaPlugin{

	private LocationHandler spawnPersistence;
    private LocationHandler bedPersistence;
	CommandHandler commandHandler;
    private GlobalConfig globalConfig;
    private WorldConfig worldConfig;

	@Override
	public void onEnable() {
        //Has to be before listeners because listeners need the configs to setup
        globalConfig = new GlobalConfig(this);
        worldConfig = new WorldConfig(this);
        globalConfig.reload();
        worldConfig.reload();

		//setup handlers
		spawnPersistence = new LocationHandler(this, new File(getDataFolder(), "spawnLocations.yml"), false);
        bedPersistence = new LocationHandler(this, new File(getDataFolder(), "bedLocations.yml"), true);
		logDebug("Yamls loaded!");

		commandHandler = new CommandHandler(this);
		logDebug("Commands registered!");

		//setup listeners
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
	}

    public GlobalConfig getGlobalConfig()
    {
        return globalConfig;
    }

    public WorldConfig getWorldConfig()
    {
        return worldConfig;
    }

    public LocationHandler getSpawnLocationHandler()
    {
        return spawnPersistence;
    }

    public LocationHandler getBedLocationHandler()
    {
        return bedPersistence;
    }

	public void logInfo(String message){
		getLogger().info(message);
	}

	public void logDebug(String message){
		if (globalConfig.getBoolean(GlobalConfigNode.DEBUG)) { getLogger().info("(DEBUG) " + message); }
	}

	public void logWarning(String message){
		getLogger().warning(message);
	}

	public void playerInfo(Player player, String message){
		player.sendMessage(ChatColor.AQUA + "[RandomSpawn] " + ChatColor.RESET + message);
	}

	public Location chooseSpawn(World world){

		String worldName = world.getName();


		List<Integer> blacklist = worldConfig.getIntegerList(WorldConfigNode.BLACKLISTED_BLOCKS, worldName);

		double xmin = worldConfig.getDouble(WorldConfigNode.RDM_X_MIN, world);
		double xmax = worldConfig.getDouble(WorldConfigNode.RDM_X_MAX, world);
		double zmin = worldConfig.getDouble(WorldConfigNode.RDM_Z_MIN, world);
		double zmax = worldConfig.getDouble(WorldConfigNode.RDM_Z_MAX, world);
				
		// Spawn area thickness near border. If 0 spawns whole area
		int thickness = worldConfig.getInt(WorldConfigNode.RDM_THICKNESS, world);

		String type = worldConfig.getString(WorldConfigNode.RDM_SEARCHTYPE, worldName);
				
		return getRandomSpawn(world, blacklist, xmin, xmax, zmin, zmax, thickness, type);
	}


    /**
     * Choose a random spawn location around a center location
     *
     * The following method contains code made by NuclearW
     * based on his SpawnArea plugin:
     * http://forums.bukkit.org/threads/tp-spawnarea-v0-1-spawns-targetPlayers-in-a-set-area-randomly-1060.20408/
     *
     * @param blacklist list of blockids that are not acceptable for spawning a player
     * @param center location to center the search radius on
     * @param radius radius to search around center location
     *
     * @return the choosen location
     */
    public Location getRandomSpawn(Location center, List<Integer> blacklist, int radius)
    {
        return getRandomSpawn(center.getWorld(), blacklist, center.getX() - radius, center.getX() + radius, center.getZ() -radius, center.getZ() + radius, 0, "square");
    }


    /**
     * Choose a random spawn location
     *
     * The following method contains code made by NuclearW
     * based on his SpawnArea plugin:
     * http://forums.bukkit.org/threads/tp-spawnarea-v0-1-spawns-targetPlayers-in-a-set-area-randomly-1060.20408/
     *
     * @param world the world to look in
     * @param blacklist list of blockids that are not acceptable for spawning a player
     *
     * @param xmin minimum x
     * @param xmax maximum x
     * @param zmin minimum z
     * @param zmax maximum z
     *
     * @param thickness some value, maybe like a border? I don't know
     * @param type the area to look in can be circle or square
     *
     * @return the choosen location
     */
    //TODO why is it a double?
    //TODO what does thickness do?
    public Location getRandomSpawn(World world, List<Integer> blacklist, double xmin, double xmax, double zmin, double zmax, int thickness, String type)
    {
        double xrand = 0;
        double zrand = 0;
        double y = -1;

        if(type.equalsIgnoreCase("circle")){

            double xcenter = xmin + (xmax - xmin)/2;
            double zcenter = zmin + (zmax - zmin)/2;

            do {

                double r = Math.random() * (xmax - xcenter);
                double phi = Math.random() * 2 * Math.PI;

                xrand = xcenter + Math.cos(phi) * r;
                zrand = zcenter + Math.sin(phi) * r;

                y = getValidHighestY(world, xrand, zrand, blacklist);

            } while (y == -1);


        } else {

            if(thickness <= 0){

                do {

                    xrand = xmin + Math.random()*(xmax - xmin + 1);
                    zrand = zmin + Math.random()*(zmax - zmin + 1);

                    y = getValidHighestY(world, xrand, zrand, blacklist);

                } while (y == -1);

            }else {

                do {

                    int side = (int) (Math.random() * 4d);
                    double borderOffset = Math.random() * (double) thickness;
                    if (side == 0) {
                        xrand = xmin + borderOffset;
                        // Also balancing probability considering thickness
                        zrand = zmin + Math.random() * (zmax - zmin + 1 - 2*thickness) + thickness;
                    }
                    else if (side == 1) {
                        xrand = xmax - borderOffset;
                        zrand = zmin + Math.random() * (zmax - zmin + 1 - 2*thickness) + thickness;
                    }
                    else if (side == 2) {
                        xrand = xmin + Math.random() * (xmax - xmin + 1);
                        zrand = zmin + borderOffset;
                    }
                    else {
                        xrand = xmin + Math.random() * (xmax - xmin + 1);
                        zrand = zmax - borderOffset;
                    }

                    y = getValidHighestY(world, xrand, zrand, blacklist);

                } while (y == -1);

            }
        }
        return new Location(world, xrand, y, zrand);
    }


	private double getValidHighestY(World world, double x, double z, List<Integer> blacklist) {
		
		world.getChunkAt(new Location(world, x, 0, z)).load();

		double y = 0;
		int blockid = 0;

		if(world.getEnvironment().equals(Environment.NETHER)){
			int blockYid = world.getBlockTypeIdAt((int) x, (int) y, (int) z);
			int blockY2id = world.getBlockTypeIdAt((int) x, (int) (y+1), (int) z);
			while(y < 128 && !(blockYid == 0 && blockY2id == 0)){				
				y++;
				blockYid = blockY2id;
				blockY2id = world.getBlockTypeIdAt((int) x, (int) (y+1), (int) z);
			}
			if(y == 127) return -1;
		}else{
			y = 257;
			while(y >= 0 && blockid == 0){
				y--;
				blockid = world.getBlockTypeIdAt((int) x, (int) y, (int) z);
			}
			if(y == 0) return -1;
		}

		if (blacklist.contains(blockid)) return -1;
		if (blacklist.contains(81) && world.getBlockTypeIdAt((int) x, (int) (y+1), (int) z) == 81) return -1; // Check for cacti

		return y;
	}

	// Methods for a save landing :)

	public void sendGround(Player player, Location location){

		location.getChunk().load();

		World world = location.getWorld();

		for(int y = 0 ; y <= location.getBlockY() + 2; y++){
			Block block = world.getBlockAt(location.getBlockX(), y, location.getBlockZ());
			player.sendBlockChange(block.getLocation(), block.getType(), block.getData());
		}

	}
}
