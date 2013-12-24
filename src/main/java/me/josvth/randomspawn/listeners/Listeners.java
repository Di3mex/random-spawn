package me.josvth.randomspawn.listeners;


import me.josvth.randomspawn.RandomSpawn;
import me.josvth.randomspawn.handlers.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.Random;

/**
 * All the events that rs listens to
 */
public class Listeners implements Listener{

    private final WorldConfig worldConfig;
    private final GlobalConfig globalConfig;
    RandomSpawn plugin;

    public Listeners (RandomSpawn plugin){
        this.plugin = plugin;
        worldConfig = plugin.getWorldConfig();
        globalConfig = plugin.getGlobalConfig();
    }

    //TODO What does this do
    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player && event.getEntity().hasMetadata("lasttimerandomspawned") && !event.getCause().equals(EntityDamageEvent.DamageCause.SUICIDE)){
            if((event.getEntity().getMetadata("lasttimerandomspawned").get(0).asLong() + (globalConfig.getInt(GlobalConfigNode.NO_DMG_TIME)*1000)) > System.currentTimeMillis()){
                event.setCancelled(true);
            }
        }
    }

    /**
     * Makes sure that a player teleports to a safe location by sending the player the blocks he is standing on first to prevent him from glitching into the floor
     *
     * @param event event that occurred
     */
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event){
        plugin.sendGround(event.getPlayer(), event.getTo());
    }


    /**
     * Only handles spawns on first login
     *
     * @param event event that occurred
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();
        String playerName = player.getName();

        World world = player.getWorld();
        String worldName = world.getName();

        if(world.getEnvironment().equals(World.Environment.NETHER) || world.getEnvironment().equals(World.Environment.THE_END)) return;

        //Check if a player has a saved bed spawn and save it if
        if (player.getBedSpawnLocation() != null && plugin.getBedLocationHandler().getLocation(player, player.getWorld()) == null)
            plugin.getBedLocationHandler().addLocation(player, player.getBedSpawnLocation());

        if(player.hasPlayedBefore()) return;

        final boolean cfgRdmFirstJoin = worldConfig.getBoolean(WorldConfigNode.RDM_RESPAWN, world);
        final boolean cfgSaveBedRespawn  = worldConfig.getBoolean(WorldConfigNode.SAVE_SPAWN_AS_BED, world);

        if (!cfgRdmFirstJoin){
            player.teleport(worldConfig.getFirstSpawn(world));
            plugin.logDebug(playerName + " is teleported to the first spawn of " + worldName);
            return;
        }

        if (player.hasPermission("RandomSpawn.exclude")){ 																// checks if player should be excluded
            plugin.logDebug(playerName + " is excluded from Random Spawning.");
            return;
        }

        Location spawnLocation = plugin.chooseSpawn(world);

        plugin.sendGround(player, spawnLocation);

        player.teleport(spawnLocation.add(0, 3, 0));

        player.setMetadata("lasttimerandomspawned", new FixedMetadataValue(plugin, System.currentTimeMillis()));

        if (cfgSaveBedRespawn){
            player.setBedSpawnLocation(spawnLocation);
        }

        showRdmRespawnMsg(player, globalConfig);
    }


    @EventHandler
    public void onPlayerSleep (PlayerBedEnterEvent event)
    {
        plugin.getBedLocationHandler().addLocation(event.getPlayer(), event.getBed().getLocation());
    }


    /**
     * This does exactly what???
     *
     * @param event event that occurred
     */
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event){
        if(event.getPlayer().hasMetadata("lasttimerandomspawned")){
            if((event.getPlayer().getMetadata("lasttimerandomspawned").get(0).asLong() + (globalConfig.getInt(GlobalConfigNode.NO_DMG_TIME)*1000)) > System.currentTimeMillis()){
                event.setReason("");
                event.setLeaveMessage("");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){

        Player player = event.getPlayer();
        String playerName = player.getName();

        if (player.hasPermission("RandomSpawn.exclude")){ 																// checks if player should be excluded
            plugin.logDebug(playerName + " is excluded from Random Spawning.");
            return;
        }

        World world = event.getRespawnLocation().getWorld();

        final boolean cfgRdmRespawn = worldConfig.getBoolean(WorldConfigNode.RDM_RESPAWN, world);
        final boolean cfgRdmBedRespawn = worldConfig.getBoolean(WorldConfigNode.RDM_BEDRESPAWN, world);
        final boolean cfgSaveBedRespawn  = worldConfig.getBoolean(WorldConfigNode.SAVE_SPAWN_AS_BED, world);
        final boolean cfgSaveWorldRespawn = worldConfig.getBoolean(WorldConfigNode.SAVE_SPAWN_AS_WORLD, world);

        LocationHandler bedlocationHandler = plugin.getBedLocationHandler();
        final boolean bedObstructed = player.getBedSpawnLocation() == null;
        final Location bedRespawn = player.getBedSpawnLocation() != null ? player.getBedSpawnLocation() : bedlocationHandler.getLocation(player, player.getWorld());

        Location location = null;

        for (String order : worldConfig.getStringList(WorldConfigNode.SPAWN_PRIORITY, world))
        {
            switch (order)
            {
                case "bed-random":
                {
                    //Randomize BedRespawns
                    if (cfgRdmBedRespawn && event.isBedSpawn())
                    {
                        location = plugin.chooseSpawn(world);
                        event.setRespawnLocation(location);

                        plugin.sendGround(player, event.getRespawnLocation());

                        player.setMetadata("lasttimerandomspawned", new FixedMetadataValue(plugin, System.currentTimeMillis()));

                        if (cfgSaveBedRespawn && player.getBedSpawnLocation() == null)
                        {
                            player.setBedSpawnLocation(location);
                            bedlocationHandler.addLocation(player, location);
                        }
                        if (cfgSaveWorldRespawn && plugin.getSpawnLocationHandler().getLocation(player, world) == null)
                            plugin.getSpawnLocationHandler().addLocation(player, location);

                        showRdmRespawnMsg(player, globalConfig);
                        return;
                    }
                    break;
                }

                case "bed-normal":
                {
                    if (event.isBedSpawn() && !cfgRdmBedRespawn &&! bedObstructed){  		// checks if player should be spawned at his bed
                        plugin.logDebug(playerName + " is spawned at his bed!");
                        return;
                    }
                    break;
                }

                case "bed-obstructed":
                {
                    if (bedObstructed && worldConfig.getBoolean(WorldConfigNode.BED_OBSTRUCTED_RANDOM, world) && bedRespawn != null)
                    {
                        location = bedRespawn;

                        //bed block has been broken
                        if (location.getBlock().getType() != Material.BED_BLOCK)
                        {
                            bedlocationHandler.removeLocation(player, world);
                            break;
                        }

                        event.setRespawnLocation(plugin.getRandomSpawn(location,
                                worldConfig.getIntegerList(WorldConfigNode.BLACKLISTED_BLOCKS, world),
                                worldConfig.getInt(WorldConfigNode.BED_OBSTRUCTED_RADIUS, world)));

                        //So that when a player respawns it checks if the bed is still obstructed
                        player.setBedSpawnLocation(location);

                        if (cfgSaveBedRespawn && player.getBedSpawnLocation() == null)
                            bedlocationHandler.addLocation(player, location);

                        plugin.logDebug(playerName + " is spawned in a radius around his saved random spawn.");
                        return;
                    }
                    break;
                }

                case "saved-random-spawn":
                {
                    //Spawn players around a saved spawn location
                    if (cfgSaveWorldRespawn && plugin.getSpawnLocationHandler().getLocation(player, world) != null)
                    {
                        location = plugin.getSpawnLocationHandler().getLocation(player, world);

                        event.setRespawnLocation(plugin.getRandomSpawn(location,
                                worldConfig.getIntegerList(WorldConfigNode.BLACKLISTED_BLOCKS, world),
                                worldConfig.getInt(WorldConfigNode.RESPAWN_RADIUS, world)));

                        if (cfgSaveBedRespawn && player.getBedSpawnLocation() == null)
                        {
                            player.setBedSpawnLocation(location);
                            bedlocationHandler.addLocation(player, location);
                        }

                        plugin.logDebug(playerName + " is spawned in a radius around his saved random spawn.");
                        return;
                    }
                    break;
                }

                case "new-random-spawn":
                {
                    //Regular random spawn logic
                    if (cfgRdmRespawn){

                        location = plugin.chooseSpawn(world);
                        event.setRespawnLocation(location);

                        plugin.sendGround(player, event.getRespawnLocation());

                        player.setMetadata("lasttimerandomspawned", new FixedMetadataValue(plugin, System.currentTimeMillis()));

                        if (cfgSaveBedRespawn && player.getBedSpawnLocation() == null)
                        {
                            player.setBedSpawnLocation(location);
                            bedlocationHandler.addLocation(player, location);
                        }
                        if (cfgSaveWorldRespawn && plugin.getSpawnLocationHandler().getLocation(player, world) == null)
                            plugin.getSpawnLocationHandler().addLocation(player, location);

                        showRdmRespawnMsg(player, globalConfig);
                        return;
                    }
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerSignInteract(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block block = event.getClickedBlock();
            if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN){
                Sign sign = (Sign)block.getState();
                final Player player = event.getPlayer();
                if (sign.getLine(0).equalsIgnoreCase(globalConfig.getString(GlobalConfigNode.SIGN_TEXT) ) ){

                    if (player.hasPermission("RandomSpawn.usesign")){

                        World worldToTeleportTo = null;

                        String worldName = sign.getLine(1);

                        if ( worldName != null )
                            worldToTeleportTo = Bukkit.getWorld(worldName);

                        if ( worldToTeleportTo == null )
                            worldToTeleportTo = player.getWorld();

                        final boolean cfgSaveBedRespawn  = worldConfig.getBoolean(WorldConfigNode.SAVE_SPAWN_AS_BED, worldToTeleportTo);

                        final Location spawnLocation = plugin.chooseSpawn(worldToTeleportTo);

                        plugin.sendGround(player, spawnLocation);

                        player.teleport(spawnLocation.add(0, 5, 0));

                        player.setMetadata("lasttimerandomspawned", new FixedMetadataValue(plugin, System.currentTimeMillis()));

                        if (cfgSaveBedRespawn){
                            player.setBedSpawnLocation(spawnLocation);
                        }

                        showRdmRespawnMsg(player, globalConfig);


                    }else{
                        plugin.playerInfo(player, "You don't have the permission to use this Random Spawn Sign!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerSignPlace(SignChangeEvent event){
        if (event.getLine(0).equalsIgnoreCase( globalConfig.getString(GlobalConfigNode.SIGN_TEXT) ) ){
            Player player = event.getPlayer();
            if (player.hasPermission("RandomSpawn.placesign")){
                this.plugin.playerInfo(player, "Random Spawn Sign created!");
            }else{
                event.setLine(0, "");
                this.plugin.playerInfo(player, "You don't have the permission to place a Random Spawn Sign!");
            }
        }
    }

    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent event){

        final Player player = event.getPlayer();
        String playerName = player.getName();

        if (player.hasPermission("RandomSpawn.exclude")){ 																// checks if player should be excluded
            plugin.logDebug(playerName + " is excluded from Random Spawning.");
            return;
        }

        World from = event.getFrom();
        World to = player.getWorld();

        if(player.getBedSpawnLocation() != null && to.equals(player.getBedSpawnLocation().getWorld())) return;			// players bed is in this world

        final boolean cfgRdmSpawnOnTo = worldConfig.getBoolean(WorldConfigNode.RDM_TELEPORT_TO, to);
        final boolean cfgRdmSpawnOnFrom = worldConfig.getBoolean(WorldConfigNode.RDM_TELEPORT_TO, from);
        final boolean cfgKeepSpawn = worldConfig.getBoolean(WorldConfigNode.SAVE_SPAWN_AS_BED, to);

        if(cfgRdmSpawnOnTo || cfgRdmSpawnOnFrom){

            Location spawnLocation = plugin.chooseSpawn(to);

            plugin.sendGround(player, spawnLocation);

            player.teleport(spawnLocation.add(0, 5, 0));

            player.setMetadata("lasttimerandomspawned", new FixedMetadataValue(plugin, System.currentTimeMillis()));

            if (cfgKeepSpawn){
                player.setBedSpawnLocation(spawnLocation);
            }

            showRdmRespawnMsg(player, globalConfig);

        }
    }

    /**
     * Show one of the available messages, only prints if enabled in config
     */
    public static void showRdmRespawnMsg(Player player, GlobalConfig globalConfig)
    {
        if (globalConfig.getBoolean(GlobalConfigNode.SHOW_RDM_SPAWN_MSG)){
            //Choose a random message out of the available messages
            List<String> availableMsgs = globalConfig.getStringList(GlobalConfigNode.RDM_SPAWN_MSGS);
            String msg = "";
            if (availableMsgs.size() > 0)
            {
                msg = availableMsgs.get(new Random().nextInt(availableMsgs.size()));
            }
            if (msg != null &&! msg.isEmpty())
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }
}
