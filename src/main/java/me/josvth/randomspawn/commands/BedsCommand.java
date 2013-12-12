package me.josvth.randomspawn.commands;

import java.util.List;

import me.josvth.randomspawn.RandomSpawn;

import me.josvth.randomspawn.handlers.WorldConfig;
import me.josvth.randomspawn.handlers.WorldConfigNode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BedsCommand extends AbstractCommand{
	
	public BedsCommand(RandomSpawn instance){
		super(instance,"usebeds");
	}
		
	public boolean onCommand(CommandSender sender, List<String> args){
		
		Player player = (Player) sender;
		World world = player.getWorld();

        WorldConfig cfg = plugin.getWorldConfig();

        final boolean cfgBedRespawn = cfg.getBoolean(WorldConfigNode.RDM_BEDRESPAWN, world);

		if (args.size() == 0){
			if (cfgBedRespawn){
                cfg.set(world, WorldConfigNode.RDM_BEDRESPAWN, false);
				plugin.playerInfo((Player)sender, "Beds will now work like normal.");
				cfg.save();
                cfg.reload();
				return true;
			}else{
                cfg.set(world, WorldConfigNode.RDM_BEDRESPAWN, true);
				plugin.playerInfo((Player)sender, "Beds are now disabled.");
                cfg.save();
                cfg.reload();
				return true;
			}
		}
		
		if (args.size() == 1){
			if (args.get(0).matches("true")){
                cfg.set(world, WorldConfigNode.RDM_BEDRESPAWN, true);
                plugin.playerInfo((Player) sender, "Beds are now disabled.");
                cfg.save();
                cfg.reload();
				return true;
			}
			else if (args.get(0).matches("false")){
                cfg.set(world, WorldConfigNode.RDM_BEDRESPAWN, false);
                plugin.playerInfo((Player) sender, "Beds will now work like normal.");
                cfg.save();
                cfg.reload();
				return true;
			}
			
		}
		return false;
	}
}
