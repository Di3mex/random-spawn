package me.josvth.randomspawn.commands;

import java.util.List;

import me.josvth.randomspawn.RandomSpawn;

import me.josvth.randomspawn.handlers.WorldConfig;
import me.josvth.randomspawn.handlers.WorldConfigNode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnableCommand extends AbstractCommand{
	
	public EnableCommand(RandomSpawn instance){
		super(instance,"enable");
	}

	public boolean onCommand(CommandSender sender, List<String> args){
		Player player = (Player) sender;
		World world = player.getWorld();

        WorldConfig cfg = plugin.getWorldConfig();

        cfg.set(world, WorldConfigNode.RDM_RESPAWN, true);

		/*if (!(plugin.yamlHandler.worlds.contains(worldname +".spawnarea"))){
			plugin.yamlHandler.worlds.set(worldname + ".spawnarea.x-min", -100);
			plugin.yamlHandler.worlds.set(worldname + ".spawnarea.x-max", 100);
			plugin.yamlHandler.worlds.set(worldname + ".spawnarea.z-min", -100);
			plugin.yamlHandler.worlds.set(worldname + ".spawnarea.z-max", 100);
		} */
		
		cfg.save();
        cfg.reload();
		plugin.playerInfo(player, "Random Spawn is now enabled in this world!");
		
		return true;
	}
}
