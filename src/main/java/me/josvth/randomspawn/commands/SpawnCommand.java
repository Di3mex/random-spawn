package me.josvth.randomspawn.commands;

import java.util.List;

import me.josvth.randomspawn.RandomSpawn;

import me.josvth.randomspawn.handlers.GlobalConfig;
import me.josvth.randomspawn.handlers.GlobalConfigNode;
import me.josvth.randomspawn.handlers.WorldConfig;
import me.josvth.randomspawn.handlers.WorldConfigNode;
import me.josvth.randomspawn.listeners.Listeners;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class SpawnCommand extends AbstractCommand {

	public SpawnCommand(RandomSpawn instance) {
		super(instance, "spawn");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, List<String> args) {
		
		Player target = null;
		World world = null;

        WorldConfig cfg = plugin.getWorldConfig();
        GlobalConfig globalCfg = plugin.getGlobalConfig();

		if (args.size() == 0) {
			target = (Player) sender;
			world = target.getWorld();
		}
		
		if (args.size() == 2) {
			
			List<Player> players = plugin.getServer().matchPlayer(args.get(0));
			
			if ( players.isEmpty() ){
				sender.sendMessage("No player named: "+ args.get(0) +" found.");
				return true;
			}
			
			target = players.get(0);
			
			world = plugin.getServer().getWorld(args.get(1));
			
			if ( world == null){
				sender.sendMessage("No world named: "+ args.get(1) +" found.");
				return true;
			}
			
		}
		
		if (world == null || target == null) return false;
		
		Location spawn = plugin.chooseSpawn(world);
		
		target.teleport(spawn);
		
		target.setMetadata("lasttimerandomspawned", new FixedMetadataValue(plugin, System.currentTimeMillis()));
		
		if (cfg.getBoolean(WorldConfigNode.SAVE_SPAWN_AS_BED, world))
			target.setBedSpawnLocation(spawn);

		if (globalCfg.getBoolean(GlobalConfigNode.SHOW_RDM_SPAWN_MSG))
            Listeners.showRdmRespawnMsg(target, globalCfg);
				
		if(target != sender)
			sender.sendMessage("Player: "+ target.getName() + " was random teleported!");
		
		return true;
		
	}

}
