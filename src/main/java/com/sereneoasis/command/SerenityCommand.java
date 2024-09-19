package com.sereneoasis.command;

import com.sereneoasis.mobs.SereneMonster;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class SerenityCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {

            if (strings.length == 0) {
                player.sendMessage("Specify an entity");
            } else {
                SereneMonster mob = null;
                Location location = player.getLocation();
                try {
                    new SereneMonster((EntityType<? extends PathfinderMob>) EntityType.byString(strings[0]).get(), location);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
//                switch (strings[0]) {
//                    case "skeleton" -> {
//                        new SereneMonster(EntityType.SKELETON, location);
//                    }
//                    case "zombie" -> {
//                        new SereneMonster(EntityType.ZOMBIE, location);
//                    }
//                    case "creeper" -> {
//                        new SereneMonster(EntityType.CREEPER, location);
//
//                    }
//                    case "spider" -> {
//                        new SereneMonster(EntityType.SPIDER, location);
//                    }
//                    case "giant" -> {
//                        new SereneMonster(EntityType.GIANT, location);
//                    }
//                }

            }
        }
        return true;
    }
}