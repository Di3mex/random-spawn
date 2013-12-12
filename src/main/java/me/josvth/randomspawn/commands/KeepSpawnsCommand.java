package me.josvth.randomspawn.commands;

import java.util.List;

import me.josvth.randomspawn.RandomSpawn;

import me.josvth.randomspawn.handlers.WorldConfig;
import me.josvth.randomspawn.handlers.WorldConfigNode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KeepSpawnsCommand extends AbstractCommand {

	public KeepSpawnsCommand(RandomSpawn instance){
		super(instance, "keepfirstspawns");
	}

	public boolean onCommand(CommandSender sender, List<String> args){

		Player player = (Player) sender;		
		String worldname = player.getWorld().getName();

        WorldConfig cfg = plugin.getWorldConfig();

		if (args.size() == 0){
			if (cfg.getBoolean(WorldConfigNode.SAVE_SPAWN_AS_BED, worldname)){
                cfg.set(worldname, WorldConfigNode.SAVE_SPAWN_AS_BED, false);
                cfg.save();
                cfg.reload();
				plugin.playerInfo(player, "Keep random spawns is now disabled.");
				return true;
			}else
			{
                cfg.set(worldname, WorldConfigNode.SAVE_SPAWN_AS_BED, true);
                cfg.save();
                cfg.reload();
				plugin.playerInfo(player, "Random Spawn will now save the spawn locations.");
				return true;
			}
		}
		if (args.size() == 1){
			if (args.get(0).matches("true")){
                cfg.set(worldname, WorldConfigNode.SAVE_SPAWN_AS_BED, true);
                cfg.save();
                cfg.reload();
                plugin.playerInfo(player, "Random Spawn will now save the spawn locations.");
				return true;
			}
			if (args.get(0).matches("false")){
                cfg.set(worldname, WorldConfigNode.SAVE_SPAWN_AS_BED, false);
                cfg.save();
                cfg.reload();
                plugin.playerInfo(player, "Keep random spawns is now disabled.");
				return true;
			}
		}

		return false;
	}
}
