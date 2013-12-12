package me.josvth.randomspawn.commands;

import java.util.List;

import me.josvth.randomspawn.RandomSpawn;

import me.josvth.randomspawn.handlers.WorldConfig;
import me.josvth.randomspawn.handlers.WorldConfigNode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpFirstSpawnCommand extends AbstractCommand{
	
	public TpFirstSpawnCommand(RandomSpawn instance){
		super(instance,"tpfirstspawn");
	}
		
	@Override
	public boolean onCommand(CommandSender sender, List<String> args){
		
		Player player = (Player)sender;
		String worldname = player.getWorld().getName();

        WorldConfig cfg = plugin.getWorldConfig();
		
		if (cfg.getBoolean(WorldConfigNode.WORLDSPAWN_OVERRIDE, worldname)){
			
			double x = cfg.getDouble(WorldConfigNode.WORLDSPAWN_X, worldname); //plugin.yamlHandler.worlds.getDouble(worldname+".firstspawn.x");
			double y = cfg.getDouble(WorldConfigNode.WORLDSPAWN_Y, worldname); //plugin.yamlHandler.worlds.getDouble(worldname+".firstspawn.y");
			double z = cfg.getDouble(WorldConfigNode.WORLDSPAWN_Z, worldname); //plugin.yamlHandler.worlds.getDouble(worldname+".firstspawn.z");
			
			double dyaw = cfg.getDouble(WorldConfigNode.WORLDSPAWN_YAW, worldname); //plugin.yamlHandler.worlds.getDouble(worldname+".firstspawn.yaw");
			double dpitch = cfg.getDouble(WorldConfigNode.WORLDSPAWN_PITCH, worldname); //plugin.yamlHandler.worlds.getDouble(worldname+".firstspawn.pitch");
			
			float yaw = (float)dyaw;
			float pitch = (float)dpitch;
			
			Location firstSpawn = new Location(player.getWorld(),x,y,z,yaw,pitch);
							
			player.teleport(firstSpawn);
			
			plugin.playerInfo(player, "You've been teleported to the first spawn location of this world!");
		}else{
			plugin.playerInfo(player,  "There's no first spawnpoint set in this world!");
		}
		return true;
	}
}
