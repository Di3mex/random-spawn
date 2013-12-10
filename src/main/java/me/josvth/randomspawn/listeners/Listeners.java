package me.josvth.randomspawn.listeners;


import me.josvth.randomspawn.RandomSpawn;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
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

/**
 * @author Diemex
 */
public class Listeners implements Listener
{
    RandomSpawn plugin;

    public Listeners (RandomSpawn plugin)
    {
        this.plugin = plugin;
    }

    //TODO What does this do
    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player && event.getEntity().hasMetadata("lasttimerandomspawned") && !event.getCause().equals(EntityDamageEvent.DamageCause.SUICIDE)){
            if((event.getEntity().getMetadata("lasttimerandomspawned").get(0).asLong() + (plugin.yamlHandler.config.getInt("nodamagetime",5)*1000)) > System.currentTimeMillis()){
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

        if(player.hasPlayedBefore()) return;

        List<String> randomSpawnFlags = plugin.yamlHandler.worlds.getStringList(worldName + ".randomspawnon");

        if (!randomSpawnFlags.contains("firstjoin")){
            player.teleport(plugin.yamlHandler.getFirstSpawn(world));
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

        if (plugin.yamlHandler.worlds.getBoolean(worldName + ".keeprandomspawns",false)){
            player.setBedSpawnLocation(spawnLocation);
        }

        if (plugin.yamlHandler.config.getString("messages.randomspawned") != null){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.yamlHandler.config.getString("messages.randomspawned")));
        }
    }


    /**
     * This does exactly what???
     *
     * @param event event that occurred
     */
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event){
        if(event.getPlayer().hasMetadata("lasttimerandomspawned")){
            if((event.getPlayer().getMetadata("lasttimerandomspawned").get(0).asLong() + (plugin.yamlHandler.config.getInt("nodamagetime",5)*1000)) > System.currentTimeMillis()){
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
        String worldName = world.getName();

        List<String> randomSpawnFlags = plugin.yamlHandler.worlds.getStringList(worldName + ".randomspawnon");

        if (event.isBedSpawn() && !randomSpawnFlags.contains("bedrespawn")){  		// checks if player should be spawned at his bed
            plugin.logDebug(playerName + " is spawned at his bed!");
            return;
        }

        if (plugin.yamlHandler.worlds.getBoolean(worldName + ".keeprandomspawns", false) && player.getBedSpawnLocation() != null ){
            event.setRespawnLocation(player.getBedSpawnLocation());
            plugin.logDebug(playerName + " is spawned at his saved spawn.");
            return;
        }

        if (randomSpawnFlags.contains("respawn")){

            Location spawnLocation = plugin.chooseSpawn(world);

            //player.sendMessage("You should be random spawned at: " + spawnLocation.getX() + "," + spawnLocation.getY() + "," + spawnLocation.getZ());

            plugin.sendGround(player, spawnLocation);

            event.setRespawnLocation(spawnLocation);

            player.setMetadata("lasttimerandomspawned", new FixedMetadataValue(plugin, System.currentTimeMillis()));

            if (plugin.yamlHandler.worlds.getBoolean(worldName + ".keeprandomspawns",false)){
                player.setBedSpawnLocation(spawnLocation);
            }

            if (plugin.yamlHandler.config.getString("messages.randomspawned") != null){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.yamlHandler.config.getString("messages.randomspawned")));
            }
        }
    }

    @EventHandler
    public void onPlayerSignInteract(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block block = event.getClickedBlock();
            if (block.getTypeId() == 68 || block.getTypeId() == 63){
                Sign sign = (Sign)block.getState();
                final Player player = event.getPlayer();
                if (sign.getLine(0).equalsIgnoreCase(plugin.yamlHandler.config.getString("rs-sign-text","[RandomSpawn]") ) ){

                    if (player.hasPermission("RandomSpawn.usesign")){

                        World world = null;

                        String worldName = sign.getLine(1);

                        if ( worldName != null )
                            world = Bukkit.getWorld(worldName);

                        if ( world == null )
                            world = player.getWorld();

                        final Location spawnLocation = plugin.chooseSpawn(world);

                        plugin.sendGround(player, spawnLocation);

                        player.teleport(spawnLocation.add(0, 5, 0));

                        player.setMetadata("lasttimerandomspawned", new FixedMetadataValue(plugin, System.currentTimeMillis()));

                        if (plugin.yamlHandler.worlds.getBoolean(world.getName() + ".keeprandomspawns",false)){
                            player.setBedSpawnLocation(spawnLocation);
                        }

                        if (plugin.yamlHandler.config.getString("messages.randomspawned") != null){
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.yamlHandler.config.getString("messages.randomspawned")));
                        }

                    }else{
                        plugin.playerInfo(player, "You don't have the permission to use this Random Spawn Sign!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerSignPlace(SignChangeEvent event){
        if (event.getLine(0).equalsIgnoreCase( plugin.yamlHandler.config.getString("rs-sign-text","[RandomSpawn]") ) ){
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

        List<String> randomSpawnFlags = plugin.yamlHandler.worlds.getStringList(to.getName() + ".randomspawnon");

        if(randomSpawnFlags.contains("teleport-from-" + from.getName())){

            Location spawnLocation = plugin.chooseSpawn(to);

            plugin.sendGround(player, spawnLocation);

            player.teleport(spawnLocation.add(0, 5, 0));

            player.setMetadata("lasttimerandomspawned", new FixedMetadataValue(plugin, System.currentTimeMillis()));

            if (plugin.yamlHandler.worlds.getBoolean(to.getName() + ".keeprandomspawns",false)){
                player.setBedSpawnLocation(spawnLocation);
            }

            if (plugin.yamlHandler.config.getString("messages.randomspawned") != null){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.yamlHandler.config.getString("messages.randomspawned")));
            }
        }
    }
}
