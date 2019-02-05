package com.blayzer.atomcore.handlers;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;

import com.blayzer.atomcore.util.RedProtectAPI;

import blusunrize.immersiveengineering.common.util.IESounds;
import br.net.fabiozumbi12.RedProtect.Sponge.Region;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityHandler {

	@SubscribeEvent
	public void LivingUpdateEvent(LivingUpdateEvent e) {
		EntityLivingBase entity = e.getEntityLiving();
		
		if(entity instanceof EntityPlayer) {
			World world = entity.world;
			Location<org.spongepowered.api.world.World> loc = new Location<org.spongepowered.api.world.World>((org.spongepowered.api.world.World) world, entity.posX, entity.posY, entity.posZ);
	    	Region region = RedProtectAPI.getRegion(loc);
	    	if(region != null && !region.flagExists("invincible")) {
	    		Player p = (Player) ((EntityPlayer) entity);
	    		if(!region.canBuild(p)) {
	    			entity.attackEntityFrom(DamageSource.GENERIC, 0.5F);
	    			world.playSound(null, entity.getPosition(), IESounds.spark, SoundCategory.NEUTRAL, 0.040F, 1);
	    			//entity.playSound(IESounds.spark, 0.5F, 1);
	    		}
	    	}
		}
	}
	
}
