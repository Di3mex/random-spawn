package me.josvth.randomspawn.commands;


import me.josvth.randomspawn.RandomSpawn;
import me.josvth.randomspawn.handlers.GlobalConfig;
import me.josvth.randomspawn.handlers.GlobalConfigNode;
import me.josvth.randomspawn.handlers.WorldConfig;
import me.josvth.randomspawn.handlers.WorldConfigNode;
import me.josvth.randomspawn.listeners.Listeners;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

/**
 * @author Diemex
 */
public class RandomTeleportCommand extends AbstractCommand
{
    public RandomTeleportCommand(RandomSpawn instance)
    {
        super(instance, "rdmtp");
    }


    @Override
    public boolean onCommand(CommandSender sender, List<String> args)
    {
        Player target = sender instanceof Player ? (Player) sender : null;
        World world = target != null ? target.getWorld() : null;

        WorldConfig cfg = plugin.getWorldConfig();
        GlobalConfig globalCfg = plugin.getGlobalConfig();

        Location spawn;
        if (target != null)
        {
            if (args.size() == 2 /*&& args.get(0).equals("-here")*/)
            {
                spawn = target.getLocation();
                try
                {
                    Integer radius = Integer.parseInt(args.get(1));
                    spawn = plugin.getRandomSpawn(spawn, cfg.getIntegerList(WorldConfigNode.BLACKLISTED_BLOCKS, spawn.getWorld()), radius);
                }
                catch (NumberFormatException e)
                {
                    sender.sendMessage("Invalid argument 3, expected a number but got " + args.get(1));
                    return false;
                }
            }
            else
            {
                spawn = plugin.chooseSpawn(world);
            }

            if (/*target == null ||*/ world == null) return false;

            target.teleport(spawn);

            target.setMetadata("lasttimerandomspawned", new FixedMetadataValue(plugin, System.currentTimeMillis()));

            if (cfg.getBoolean(WorldConfigNode.SAVE_SPAWN_AS_BED, world))
                target.setBedSpawnLocation(spawn);

            if (globalCfg.getBoolean(GlobalConfigNode.SHOW_RDM_SPAWN_MSG))
                Listeners.showRdmRespawnMsg(target, globalCfg);

            //if(target != sender)
            //    sender.sendMessage("Player: "+ target.getName() + " was random teleported!");
        }

        return true;
    }
}
