package me.josvth.randomspawn.commands;

import java.util.List;

import me.josvth.randomspawn.RandomSpawn;

import me.josvth.randomspawn.handlers.WorldConfig;
import me.josvth.randomspawn.handlers.WorldConfigNode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetFirstSpawnCommand extends AbstractCommand{
	
	public SetFirstSpawnCommand(RandomSpawn instance){
		super(instance,"setfirstspawn");
	}
		
	public boolean onCommand(CommandSender sender, List<String> args){
		Player player = (Player)sender;

		String worldname = player.getWorld().getName();

        WorldConfig cfg = plugin.getWorldConfig();

		double x = player.getLocation().getX();
		double y = player.getLocation().getY();
		double z = player.getLocation().getZ();

		double yaw = (double)player.getLocation().getYaw();
		double pitch = (double)player.getLocation().getPitch();

        cfg.set(worldname, WorldConfigNode.WORLDSPAWN_X, x);
        cfg.set(worldname, WorldConfigNode.WORLDSPAWN_Y, y);
        cfg.set(worldname, WorldConfigNode.WORLDSPAWN_Z, z);
        cfg.set(worldname, WorldConfigNode.WORLDSPAWN_YAW, yaw);
        cfg.set(worldname, WorldConfigNode.WORLDSPAWN_PITCH, pitch);
        cfg.set(worldname, WorldConfigNode.RDM_FIRSTJOIN, false);

        cfg.save();
        cfg.reload();
		
		plugin.playerInfo(player, "First spawn location set!");
		plugin.playerInfo(player, "Random spawning on first join is now disabled!");
		
		return true;
	}
}
