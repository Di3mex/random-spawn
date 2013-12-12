package me.josvth.randomspawn.commands;

import java.util.List;

import me.josvth.randomspawn.RandomSpawn;

import me.josvth.randomspawn.handlers.WorldConfig;
import me.josvth.randomspawn.handlers.WorldConfigNode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnsetFirstSpawnCommand extends AbstractCommand{
	
	public UnsetFirstSpawnCommand(RandomSpawn instance){
		super(instance,"unsetfirstspawn");
	}
	
	public boolean onCommand(CommandSender sender, List<String> args){
		Player player = (Player)sender;
		String worldname = player.getWorld().getName();
        WorldConfig cfg = plugin.getWorldConfig();

		if (cfg.getBoolean(WorldConfigNode.WORLDSPAWN_OVERRIDE, worldname)){

            cfg.set(worldname, WorldConfigNode.WORLDSPAWN_OVERRIDE, false);

            cfg.save();
            cfg.reload();

			plugin.playerInfo(player,  "The first spawn location of this world is removed!");
			plugin.playerInfo(player,  "Now refering to world spawn.");
		}else{
			plugin.playerInfo(player,  "There's no first spawnpoint set in this world!");
		}
		return true;
	}
}
