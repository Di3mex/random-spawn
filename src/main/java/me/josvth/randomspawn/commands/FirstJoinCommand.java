package me.josvth.randomspawn.commands;

import java.util.List;

import me.josvth.randomspawn.handlers.WorldConfig;
import me.josvth.randomspawn.handlers.WorldConfigNode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.josvth.randomspawn.RandomSpawn;

public class FirstJoinCommand extends AbstractCommand{

	public FirstJoinCommand(RandomSpawn instance){
		super(instance, "firstjoin");
	}

	public boolean onCommand(CommandSender sender, List<String> args){

		Player player = (Player) sender;
		World world = player.getWorld();

        WorldConfig cfg = plugin.getWorldConfig();

		if (args.size() == 0){
			if (cfg.getBoolean(WorldConfigNode.RDM_FIRSTJOIN, world)){
				cfg.set(world, WorldConfigNode.RDM_FIRSTJOIN, false);
				plugin.playerInfo(player, "Random Spawn will not spawn new players.");
				cfg.save();
                cfg.reload();
				return true;
			}else{
                cfg.set(world, WorldConfigNode.RDM_FIRSTJOIN, true);
				plugin.playerInfo(player, "Random Spawn will random spawn new players.");
                cfg.save();
                cfg.reload();
				return true;
			}
		}
		if (args.size() == 1){
			if (args.get(0).matches("true")){
                cfg.set(world, WorldConfigNode.RDM_FIRSTJOIN, false);
                plugin.playerInfo(player, "Random Spawn will not spawn new players.");
                cfg.save();
                cfg.reload();
				return true;
			}
			if (args.get(0).matches("false")){
                cfg.set(world, WorldConfigNode.RDM_FIRSTJOIN, true);
                plugin.playerInfo(player, "Random Spawn will random spawn new players.");
                cfg.save();
                cfg.reload();
				return true;
			}
		}
		return false;
	}
}
