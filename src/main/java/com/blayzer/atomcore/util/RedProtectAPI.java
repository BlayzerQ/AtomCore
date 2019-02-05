package com.blayzer.atomcore.util;

import org.spongepowered.api.event.cause.Cause;
import java.util.Map;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import java.util.Set;
import org.spongepowered.api.world.Location;
import br.net.fabiozumbi12.RedProtect.Sponge.RedProtect;
import br.net.fabiozumbi12.RedProtect.Sponge.Region;
import org.spongepowered.api.world.World;

public class RedProtectAPI
{
	public static boolean isRegionSelected(Player player) {
		return RedProtect.get().firstLocationSelections.containsKey(player) && RedProtect.get().secondLocationSelections.containsKey(player);
	}
	
    public static Region getRegion(String regionName, World world) {
        return RedProtect.get().rm.getRegion(regionName, world);
    }
    
    public static Region getRegion(Location<World> location) {
        return getHighPriorityRegion((World)location.getExtent(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
    
    public static Set<Region> getAllRegions() {
    	return RedProtect.get().rm.getAllRegions();
    }
    
    public static Set<Region> getRegionsNear(Player player, int radius) {
    	return RedProtect.get().rm.getRegionsNear(player, radius, player.getWorld());
    }
    
    public static Set<Region> getRegionsByWorld(World world) {
    	return RedProtect.get().rm.getRegionsByWorld(world);
    }
    
    public static Set<Region> getPlayerRegions(String uuid) {
        return RedProtect.get().rm.getRegions(uuid);
    }
    
    public static Set<Region> getPlayerRegions(String uuid, World world) {
        return RedProtect.get().rm.getRegions(uuid, world);
    }
    
    public static Set<Region> getPlayerRegions(Player player) {
        return RedProtect.get().rm.getRegions(Sponge.getServer().getOnlineMode() ? player.getName() : player.getUniqueId().toString(), player.getWorld());
    }
    
    public static Set<Region> getPlayerRegions(Player player, int x, int y, int z) {
        return RedProtect.get().rm.getRegions(player, x, y, z);
    }
    
    public static Region getHighPriorityRegion(World world, int x, int y, int z) {
        return RedProtect.get().rm.getTopRegion(world, x, y, z, RedProtectAPI.class.getName());
    }
    
    public static Region getLowPriorytyRegion(World world, int x, int y, int z) {
        return RedProtect.get().rm.getLowRegion(world, x, y, z);
    }
    
    public static Map<Integer, Region> getGroupRegions(World world, int x, int y, int z) {
        return RedProtect.get().rm.getGroupRegion(world, x, y, z);
    }
    
    public static void setRegionFlag(Region region, String flag, Object value) {
        setRegionFlag(null, region, flag, value);
    }
    
    public static void setRegionFlag(Cause cause, Region region, String flag, Object value) {
    	region.setFlag(cause, flag, value);
    }
    
    public static boolean getBoolFlag(Region region, String flag) {
        return region.getFlagBool(flag);
    }
    
    public static String getStringFlag(Region region, String flag) {
        return region.getFlagString(flag);
    }
    
    public static void addRegion(Region region, World world) {
        RedProtect.get().rm.add(region, world);
    }
    
    public static void removeRegion(Region region) {
        RedProtect.get().rm.remove(region, Sponge.getServer().getWorld(region.getWorld()).get());
    }
    
    public static void addAdminFlag(String flag) {
        RedProtect.get().cfgs.AdminFlags.add(flag);
    }
    
    public static void addPlayerFlag(String flag, Object defValue) {
        if (defValue instanceof Boolean) {
            RedProtect.get().cfgs.root().flags.put(flag, (Boolean)defValue);
        }
    }
    
    public static void renameRegion(Region region, String newName) {
        RedProtect.get().rm.renameRegion(newName, region);
    }
    
    public static boolean addFlag(String flag, boolean defaultValue, boolean isAdmin) {
        return RedProtect.get().cfgs.addFlag(flag, defaultValue, isAdmin);
    }
}
