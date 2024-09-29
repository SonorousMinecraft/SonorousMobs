package com.sereneoasis.command;

import com.sereneoasis.ability.Archetype;
import com.sereneoasis.mobs.SereneMonster;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;

import java.lang.reflect.InvocationTargetException;

public class SerenityCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Entity entity) {

            if (strings.length == 0) {
                entity.sendMessage("Specify an entity");
            } else {
                SereneMonster mob = null;
                Location location = entity.getLocation();
                try {
                    if (strings.length == 1){
                        entity.sendMessage("Specify an archetype");
                    }
                    else {
                        Archetype archetype = Archetype.valueOf(strings[1]);
                        new SereneMonster((EntityType<? extends PathfinderMob>) EntityType.byString(strings[0]).get(), location, archetype);
                    }
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                         InvocationTargetException e) {
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