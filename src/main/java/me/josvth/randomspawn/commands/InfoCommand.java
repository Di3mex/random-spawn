package me.josvth.randomspawn.commands;

import java.util.List;

import me.josvth.randomspawn.RandomSpawn;

import me.josvth.randomspawn.handlers.WorldConfig;
import me.josvth.randomspawn.handlers.WorldConfigNode;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCommand extends AbstractCommand {
	
	public InfoCommand(RandomSpawn instance){
		super(instance, "info");
	}
	
	public boolean onCommand(CommandSender sender, List<String> args){
		
		Player player = (Player)sender;
		String worldname = player.getWorld().getName();

        WorldConfig cfg = plugin.getWorldConfig();
		
		player.sendMessage(ChatColor.WHITE +" ---------------- " + ChatColor.AQUA + "Random Spawn Info" + ChatColor.WHITE + " ------------------ ");
		player.sendMessage(ChatColor.AQUA + "World: " + ChatColor.WHITE + worldname );
		if (!cfg.enabledFor(worldname)){
			player.sendMessage("Is not configured in Random Spawn ");
			player.sendMessage(ChatColor.WHITE +" --------------------------------------------------- ");
			return true;
		}
		
		String flags = "";
		
		/*for(String flag : plugin.yamlHandler.worlds.getStringList(worldname + ".randomspawnon")){
			flags += flag + ", ";
		}*/
		
		player.sendMessage(ChatColor.AQUA + "Random spawn on: " + ChatColor.WHITE + flags);

		//if(plugin.yamlHandler.worlds.contains(worldname + ".spawnarea")){
			player.sendMessage(ChatColor.AQUA + "Spawnarea " + cfg.getString(WorldConfigNode.RDM_SEARCHTYPE, worldname) + ":");
			
			double xmin = cfg.getDouble(WorldConfigNode.RDM_X_MIN, worldname);// plugin.yamlHandler.worlds.getDouble(worldname +".spawnarea.x-min");
			double xmax = cfg.getDouble(WorldConfigNode.RDM_X_MAX, worldname);// plugin.yamlHandler.worlds.getDouble(worldname +".spawnarea.x-max");
			double zmin = cfg.getDouble(WorldConfigNode.RDM_Z_MIN, worldname);// plugin.yamlHandler.worlds.getDouble(worldname +".spawnarea.z-min");
			double zmax = cfg.getDouble(WorldConfigNode.RDM_Z_MAX, worldname);// plugin.yamlHandler.worlds.getDouble(worldname +".spawnarea.z-max");
			
			player.sendMessage("x-min = "+ xmin);
			player.sendMessage("x-max = " + xmax);
			player.sendMessage("z-min = "+ zmin);
			player.sendMessage("z-max = " + zmax);
		/*}else
		{
			player.sendMessage("There is no spawn area set. Refering to defaults:");
			player.sendMessage("x-min = -100 | x-max = 100");
			player.sendMessage("z-min = -100 | z-max = 100");
		}*/
		
		player.sendMessage(ChatColor.AQUA + "keepspawns: " + ChatColor.WHITE + cfg.getBoolean(WorldConfigNode.SAVE_SPAWN_AS_BED, worldname));
			
		if(cfg.getBoolean(WorldConfigNode.WORLDSPAWN_OVERRIDE, worldname)){
			player.sendMessage(ChatColor.AQUA + "Firstspawn:");
			
			double x = cfg.getDouble(WorldConfigNode.WORLDSPAWN_X, worldname);
			double y = cfg.getDouble(WorldConfigNode.WORLDSPAWN_Y, worldname);
			double z = cfg.getDouble(WorldConfigNode.WORLDSPAWN_Z, worldname);
			double yaw = cfg.getDouble(WorldConfigNode.WORLDSPAWN_YAW, worldname);
			double pitch = cfg.getDouble(WorldConfigNode.WORLDSPAWN_PITCH, worldname);
			
			player.sendMessage("x = "+ x + "  |  y = " + y + "  |  z = " + z + "  |  yaw = " + yaw + "  |  pitch = " + pitch);
		}else
		{
			player.sendMessage("There is no first spawn point set. Refering to worldspawn.");
		}
		
		player.sendMessage(ChatColor.WHITE +" --------------------------------------------------- ");
					
		return true;
	}
}
