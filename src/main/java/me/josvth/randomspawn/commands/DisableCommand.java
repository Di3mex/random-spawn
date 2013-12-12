package me.josvth.randomspawn.commands;

import java.util.List;

import me.josvth.randomspawn.RandomSpawn;

import me.josvth.randomspawn.handlers.WorldConfig;
import me.josvth.randomspawn.handlers.WorldConfigNode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisableCommand extends AbstractCommand{
	
	public DisableCommand(RandomSpawn instance){
		super(instance,"disable");
	}
	
	public boolean onCommand(CommandSender sender, List<String> args){
		Player player = (Player) sender;
		World world = player.getWorld();

        WorldConfig cfg = plugin.getWorldConfig();

        cfg.set(world, WorldConfigNode.RDM_RESPAWN, false);
        cfg.set(world, WorldConfigNode.RDM_BEDRESPAWN, false);
        cfg.set(world, WorldConfigNode.RDM_FIRSTJOIN, false);
        cfg.set(world, WorldConfigNode.RDM_TELEPORT_FROM, false);
        cfg.set(world, WorldConfigNode.RDM_TELEPORT_TO, false);

        cfg.save();
        cfg.reload();

		plugin.playerInfo(player, "Random Spawn is now disabled in this world!");
		return true;
	}
}
