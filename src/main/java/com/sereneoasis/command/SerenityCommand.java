package com.sereneoasis.command;

import com.sereneoasis.mobs.SereneMonster;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SerenityCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {

            if (strings.length == 0) {
                player.sendMessage("Specify an entity");
            } else {
                SereneMonster mob = null;
                Location location = player.getLocation();
                switch (strings[0]) {
                    case "skeleton" -> {
                        new SereneMonster(EntityType.SKELETON, location);
                    }
                    case "zombie" -> {
                        new SereneMonster(EntityType.ZOMBIE, location);
                    }
                    case "creeper" -> {
                        new SereneMonster(EntityType.CREEPER, location);

                    }
                    case "spider" -> {
                        new SereneMonster(EntityType.SPIDER, location);
                    }
                    case "giant" -> {
                        new SereneMonster(EntityType.GIANT, location);
                    }
                }

            }
        }
        return true;
    }
}