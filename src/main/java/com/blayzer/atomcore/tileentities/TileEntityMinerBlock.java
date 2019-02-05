package com.blayzer.atomcore.tileentities;

import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;

import com.blayzer.atomcore.AtomCore;
import com.blayzer.atomcore.config.ConfigHandler;
import com.blayzer.atomcore.handlers.RegionHandler;
import com.blayzer.atomcore.network.MessageUpdateInfo;
import com.blayzer.atomcore.tileentities.modules.ModuleTypes;
import com.blayzer.atomcore.util.ChunkLoadUtils;
import com.blayzer.atomcore.util.DBUtils;
import com.blayzer.atomcore.util.Economy;
import com.blayzer.atomcore.util.RedProtectAPI;
import com.blayzer.atomcore.util.TextHelper;

import br.net.fabiozumbi12.RedProtect.Sponge.RPUtil;
import br.net.fabiozumbi12.RedProtect.Sponge.Region;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class TileEntityMinerBlock extends TileEntityBase implements Property.Energy, Energy.Consumer {

    //private boolean redstone = false;
    private int prevEnergy = 0;
    private int energy = 0;
    public UUID owner = UUID.randomUUID();
    public String rgkey = "";
    
    public int maxTimeLeft = 0;
    public int timeLeft = 0;
    
    private static final Capability[] capabilities = new Capability[]{
            CapabilityEnergy.ENERGY,
            TeslaCapabilities.CAPABILITY_HOLDER,
            TeslaCapabilities.CAPABILITY_CONSUMER
    };

    public TileEntityMinerBlock() {
        super(new ModuleTypes[]{}, new Object[][]{});
    }
    
    public boolean isProtectionActive() {
    	Region curreg = getRegion();
    	return !RedProtectAPI.getBoolFlag(curreg, "build");
    }
    
    public Region getRegion() {
    	World world2 = getWorld();
    	Location<org.spongepowered.api.world.World> loc = new Location<org.spongepowered.api.world.World>((org.spongepowered.api.world.World) world2, this.pos.getX(), this.pos.getY(), this.pos.getZ());
    	Region region = RedProtectAPI.getRegion(loc);
    	return region;
    }

    public void activated(@Nullable EntityPlayer player, boolean shiftKeyDown){
    	markDirty(true);
        if (!world.isRemote){
            if (player != null){
            	try {
                	//Возможно нужно кешировать
                	Region region = getRegion();
                    if(region != null) {
                    	boolean isActiveProtect = false;
                    	if(isProtectionActive())
                    		isActiveProtect = true;
                    	
                    	AtomCore.network.sendTo(new MessageUpdateInfo(isActiveProtect, RegionHandler.getRegionFee(region)), (EntityPlayerMP) player);

                    } else {
                    	TextHelper.chatMessageServer(player, I18n.translateToLocalFormatted("message.rgNotFound"));
                    	world.destroyBlock(pos, true);
                    	world.removeTileEntity(pos);
                    	
                    	if(DBUtils.hasDBValue(rgkey)) {
                    		DBUtils.deleteFromDB(rgkey);
                    	}
                    }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        }
    }

    @Override
    public void update() {
        //server
        if (!world.isRemote){

//            boolean prevRedstone = redstone;
//            redstone = getWorld().isBlockPowered(this.pos);
//            if (!prevRedstone && redstone)
//                activated(null, false);
        	
            if (atTick(4)) {
                if (prevEnergy != energy){
                    sync();
                    prevEnergy = energy;
                    world.notifyNeighborsOfStateChange(pos, blockType, true);
                }
                
            }
        }
    }

    @Override
    public void setEnergy(int energy, boolean sync) {
        this.energy = energy;
        markDirty(sync);
    }

    @Override
    public boolean changeEnergy(int a, boolean sync) {
        int prevEnergy = energy;
        energy += a;
        if (energy < 0)
            energy = 0;
        if (energy > getMaxEnergyStored())
            energy = getMaxEnergyStored();
        if (prevEnergy != energy) {
            markDirty(sync);
            return true;
        }
        return false;
    }

    // Forge Energy

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return ConfigHandler.minerBlock.FE_capacity;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        energy = compound.getInteger("energy");
        owner = compound.getUniqueId("owner");
        rgkey = compound.getString("rgkey");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("energy", energy);
        compound.setUniqueId("owner", owner);
        compound.setString("rgkey", rgkey);
        return super.writeToNBT(compound);
    }
    
    public boolean canInteractWith(EntityPlayer playerIn) {
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int received = Math.min(getMaxInput(), Math.min(getMaxEnergyStored() - energy, maxReceive));
        if (!simulate) {
            changeEnergy(received, true);
        }
        return received;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return Arrays.asList(capabilities).contains(capability) || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (Arrays.asList(capabilities).contains(capability)) {
            return capability.cast((T) this);
        }
        return null;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = getTileData();
        compound.setInteger("energy", energy);
        compound.setUniqueId("owner", owner);
        compound.setString("rgkey", rgkey);
        return compound;
    }
    
    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        energy = tag.getInteger("energy");
        owner = tag.getUniqueId("owner");
        rgkey = tag.getString("rgkey");
        super.handleUpdateTag(tag);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound compound = pkt.getNbtCompound();
        energy = compound.getInteger("energy");
        owner = compound.getUniqueId("owner");
        rgkey = compound.getString("rgkey");
        sync();
    }

    @Override
    public long givePower(long power, boolean simulated) {
        long received = Math.min(getMaxInput(), Math.min(getMaxEnergyStored() - energy, power));
        if (!simulated) {
            changeEnergy((int) received, true);
        }
        return received;
    }

    @Override
    public long getStoredPower() {
        return energy;
    }

    @Override
    public long getCapacity() {
        return getMaxEnergyStored();
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        int received = Math.min(getMaxInput(), Math.min(getMaxEnergyStored() - energy, maxReceive));
        if (!simulate) {
            changeEnergy(received, true);
        }
        return received;
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        return energy;
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return true;
    }

    public int getMaxInput() {
        return ConfigHandler.minerBlock.FE_maxInput;
    }

    public int getActivation() {
    	return (RegionHandler.getRegionFee(getRegion())*ConfigHandler.minerBlock.DMPerBlock)/2;
    }
}
