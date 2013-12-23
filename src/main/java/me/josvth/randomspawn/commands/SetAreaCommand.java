package me.josvth.randomspawn.commands;

import java.util.List;

import me.josvth.randomspawn.RandomSpawn;

import me.josvth.randomspawn.handlers.WorldConfig;
import me.josvth.randomspawn.handlers.WorldConfigNode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetAreaCommand extends AbstractCommand{

	public SetAreaCommand(RandomSpawn instance){
		super(instance,"setarea");
	}

	public boolean onCommand(CommandSender sender, List<String> args){
		Player player = (Player) sender;

		double xmin = 0;
		double xmax = 0;
		double zmin = 0;
		double zmax = 0;

        WorldConfig cfg = plugin.getWorldConfig();

		if (args.size() == 1) {
			Location reference = player.getLocation();

			try{
				xmin = reference.getX() - Double.parseDouble(args.get(0));
				xmax = reference.getX() + Double.parseDouble(args.get(0));
				zmin = reference.getZ() - Double.parseDouble(args.get(0));
				zmax = reference.getZ() + Double.parseDouble(args.get(0));
			} catch(NumberFormatException e){
				sender.sendMessage("Invalid number.");
				return false;
			}

			String worldname = reference.getWorld().getName();

            cfg.set(worldname, WorldConfigNode.RDM_X_MIN, xmin);
            cfg.set(worldname, WorldConfigNode.RDM_X_MAX, xmax);
            cfg.set(worldname, WorldConfigNode.RDM_Z_MIN, zmin);
            cfg.set(worldname, WorldConfigNode.RDM_Z_MAX, zmax);
            cfg.set(worldname, WorldConfigNode.RDM_RESPAWN, true);

            cfg.save();
            cfg.reload();

			plugin.playerInfo(player,  "Spawn area set!");

			return true;
		}

		if (args.size() == 2 && (args.get(0).equalsIgnoreCase("circle") || args.get(0).equalsIgnoreCase("square"))) {

			Location reference = player.getLocation();

			try{

				xmin = reference.getX() - Double.parseDouble(args.get(1));
				xmax = reference.getX() + Double.parseDouble(args.get(1));
				zmin = reference.getZ() - Double.parseDouble(args.get(1));
				zmax = reference.getZ() + Double.parseDouble(args.get(1));

			} catch(NumberFormatException e){
				sender.sendMessage("Invalid number.");
				return false;
			}

			String worldname = reference.getWorld().getName();

            cfg.set(worldname, WorldConfigNode.RDM_X_MIN, xmin);
            cfg.set(worldname, WorldConfigNode.RDM_X_MAX, xmax);
            cfg.set(worldname, WorldConfigNode.RDM_Z_MIN, zmin);
            cfg.set(worldname, WorldConfigNode.RDM_Z_MAX, zmax);
            cfg.set(worldname, WorldConfigNode.RDM_RESPAWN, true);

			if (args.get(0).matches("circle")) {
                cfg.set(worldname, WorldConfigNode.RDM_SEARCHTYPE, "circle");
			}else{
                cfg.set(worldname, WorldConfigNode.RDM_SEARCHTYPE, "square");
			}

			plugin.playerInfo(player, "Spawn area set!");

			cfg.save();
            cfg.reload();

			return true;
		}

		if (args.size() == 2){
			
			Location reference = player.getLocation();

			try{
				xmin = reference.getX() - (Double.parseDouble(args.get(0)) / 2);
				xmax = reference.getX() + (Double.parseDouble(args.get(0)) / 2);
				zmin = reference.getZ() - (Double.parseDouble(args.get(1)) / 2);
				zmax = reference.getZ() + (Double.parseDouble(args.get(1)) / 2);
			}catch(NumberFormatException e){
				sender.sendMessage("Invalid number.");
				return false;
			}


			String worldname = reference.getWorld().getName();

            cfg.set(worldname, WorldConfigNode.RDM_X_MIN, xmin);
            cfg.set(worldname, WorldConfigNode.RDM_X_MAX, xmax);
            cfg.set(worldname, WorldConfigNode.RDM_Z_MIN, zmin);
            cfg.set(worldname, WorldConfigNode.RDM_Z_MAX, zmax);
            cfg.set(worldname, WorldConfigNode.RDM_RESPAWN, true);
			
			cfg.save();
            cfg.reload();

			plugin.playerInfo(player,  "Spawn area set!");

			return true;
		}

		if (args.size() == 4){
			Location reference = player.getLocation();

			try{

				xmin = Double.parseDouble(args.get(0));
				xmax = Double.parseDouble(args.get(1));
				zmin = Double.parseDouble(args.get(2));
				zmax = Double.parseDouble(args.get(3));

			} catch(NumberFormatException e){
				sender.sendMessage("Invalid number.");
				return false;
			}

			String worldname = reference.getWorld().getName();

            cfg.set(worldname, WorldConfigNode.RDM_X_MIN, xmin);
            cfg.set(worldname, WorldConfigNode.RDM_X_MAX, xmax);
            cfg.set(worldname, WorldConfigNode.RDM_Z_MIN, zmin);
            cfg.set(worldname, WorldConfigNode.RDM_Z_MAX, zmax);
            cfg.set(worldname, WorldConfigNode.RDM_RESPAWN, true);

            cfg.save();
            cfg.reload();

			plugin.playerInfo(player,  "Spawn area set!");

			return true;
		}

		return false;

	}

}
