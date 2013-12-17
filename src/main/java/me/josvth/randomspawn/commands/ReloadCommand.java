package me.josvth.randomspawn.commands;

import java.util.List;

import me.josvth.randomspawn.RandomSpawn;

import org.bukkit.command.CommandSender;

public class ReloadCommand extends AbstractCommand {
	
	public ReloadCommand(RandomSpawn instance){
		super(instance, "reload");
	}
	
	public boolean onCommand(CommandSender sender, List<String> args){

		if (args.size() == 0) {
            plugin.getGlobalConfig().reload();
            plugin.getWorldConfig().reload();
			sender.sendMessage( "Random Spawn configurations reloaded!" );
			return true;
		}

		if (args.get(0).matches("config")) {
			plugin.getGlobalConfig().reload();
			sender.sendMessage( "Random Spawn config file is reloaded!");
			return true;
		}

		if (args.get(0).matches("worlds")) {
            plugin.getWorldConfig().reload();
			sender.sendMessage( "Random Spawn worlds file reloaded!");
			return true;
		}
		
		return false;
	}
}
