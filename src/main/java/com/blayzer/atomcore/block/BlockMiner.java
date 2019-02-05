package com.blayzer.atomcore.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;

import com.blayzer.atomcore.AtomCore;
import com.blayzer.atomcore.config.ConfigHandler;
import com.blayzer.atomcore.handlers.GuiHandler;
import com.blayzer.atomcore.handlers.RegionHandler;
import com.blayzer.atomcore.items.Items;
import com.blayzer.atomcore.tileentities.TileEntityMinerBlock;
import com.blayzer.atomcore.util.DBUtils;
import com.blayzer.atomcore.util.ModUtil;
import com.blayzer.atomcore.util.RedProtectAPI;
import com.blayzer.atomcore.util.TextHelper;

import br.net.fabiozumbi12.RedProtect.Sponge.RPUtil;
import br.net.fabiozumbi12.RedProtect.Sponge.RedProtect;
import br.net.fabiozumbi12.RedProtect.Sponge.Region;
import br.net.fabiozumbi12.RedProtect.Sponge.RegionBuilder;
import br.net.fabiozumbi12.RedProtect.Sponge.actions.DefineRegionBuilder;
import br.net.fabiozumbi12.RedProtect.Sponge.config.RPLang;
import br.net.fabiozumbi12.RedProtect.Sponge.events.DeleteRegionEvent;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BlockMiner extends BlockBase implements ITileEntityProvider{

    public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public BlockMiner() {
		super(Material.IRON, ModUtil.BLOCK_CONTROLLER);
		this.setHardness(1.5F)
        .setResistance(10f)
        .setCreativeTab(CreativeTabs.MISC);
		this.setSoundType(SoundType.METAL);
		
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te != null && te instanceof TileEntityMinerBlock){
            try {
                TileEntityMinerBlock rainTE = (TileEntityMinerBlock)te;
                rainTE.activated(playerIn, playerIn.isSneaking());
            } catch (Exception e)
            {
            	
            }
        }
        playerIn.openGui(AtomCore.instance, GuiHandler.GUI_CONTROLLERBLOCK, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
    
	// TODO Blayzer code start
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack)
	{
		world.setBlockState(pos, state.withProperty(FACING, EnumFacing.NORTH), 2);
		if(world.isRemote) return; //Избавляет от краша, но не уверен что оно тут нужно
		super.onBlockPlacedBy(world, pos, state, entity, stack);
		
		//Если делать по логике как тут, забрать права на создание и удаление привата у игрока
		if(RedProtectAPI.isRegionSelected((Player) entity) && entity instanceof EntityPlayer) {
			String name = RPUtil.nameGen(((Player) entity).getName(), ((Player) entity).getWorld().getName());
            String leader = ((Player) entity).getUniqueId().toString();
            
			RegionBuilder rb2 = new DefineRegionBuilder((Player) entity, RedProtect.get().firstLocationSelections.get((Player) entity), RedProtect.get().secondLocationSelections.get((Player) entity), name, leader, new LinkedList<>(), false);
            if (rb2.ready()) {
            	Region newregion = rb2.build();
                RPLang.sendMessage((Player) entity, RPLang.get("cmdmanager.region.created") + " " + newregion.getName() + ".");
                RedProtect.get().rm.add(newregion, ((Player) entity).getWorld());

                RedProtect.get().firstLocationSelections.remove((Player) entity);
                RedProtect.get().secondLocationSelections.remove((Player) entity);

                RedProtect.get().logger.addLog("(World "+newregion.getWorld()+") Player "+((Player) entity).getName()+" CLAIMED region "+newregion.getName());
            }
		} else {
			TextHelper.chatMessageServer((EntityPlayer) entity, I18n.translateToLocalFormatted("message.rgNotSelected"));
			world.destroyBlock(pos, true);
			world.removeTileEntity(pos);
			return;
		}
		//КОНЕЦ СОЗДАНИЯ РЕГИОНА
		
		TileEntity tile = world.getTileEntity(pos);
		
		Location<org.spongepowered.api.world.World> loc = new Location<org.spongepowered.api.world.World>((org.spongepowered.api.world.World) world, pos.getX(), pos.getY(), pos.getZ());
    	Region region = RedProtectAPI.getRegion(loc);
    	
    	//Нужна ли данная проверка тут?
		if(region == null) {
			TextHelper.chatMessageServer((EntityPlayer) entity, I18n.translateToLocalFormatted("message.rgNotFound"));
			world.destroyBlock(pos, true);
			world.removeTileEntity(pos);
			
			//Удаление региона
			Region r = RedProtect.get().rm.getTopRegion(((Player) entity).getLocation(), this.getClass().getName());
	        if (RedProtect.get().ph.hasRegionPermLeader(((Player) entity), "delete", r)) {
	            if (r == null) {
	                return;
	            }

				int claims = RedProtect.get().cfgs.root().region_settings.can_delete_first_home_after_claims;
				if (!r.canDelete() && (claims == -1 || RedProtect.get().rm.getPlayerRegions(((Player) entity).getName(), ((Player) entity).getWorld()) < claims) && !((Player) entity).hasPermission("redprotect.bypass")){
					if (claims != -1){
						RPLang.sendMessage(((Player) entity), RPLang.get("cmdmanager.region.cantdeletefirst-claims").replace("{claims}", ""+claims));
					} else {
						RPLang.sendMessage(((Player) entity), RPLang.get("cmdmanager.region.cantdeletefirst"));
					}
					return;
				}

	            DeleteRegionEvent event = new DeleteRegionEvent(r, ((Player) entity));
				if (Sponge.getEventManager().post(event)){
					return;
				}

	            String rname = r.getName();
	            String w = r.getWorld();
	            RedProtect.get().rm.remove(r, RedProtect.get().serv.getWorld(w).get());
	            RPLang.sendMessage(((Player) entity),RPLang.get("cmdmanager.region.deleted") +" "+ rname);
	            RedProtect.get().logger.addLog("(World "+w+") Player "+((Player) entity).getName()+" REMOVED region "+rname);
	        }
			return;
		}
    	
    	String key = region.getName() + "_" + region.getWorld(); //Назваие_Мир
		
		if (tile instanceof TileEntityMinerBlock && entity instanceof EntityPlayer) {
			((TileEntityMinerBlock) tile).owner = ((EntityPlayer) entity).getUniqueID();
			((TileEntityMinerBlock) tile).rgkey = key;
		}

		try {
			if(!DBUtils.hasDBValue(key)) {
				DBUtils.writeToDB(key, pos);
			} else {
				TextHelper.chatMessageServer((EntityPlayer) entity, I18n.translateToLocalFormatted("message.rgAlredyExits"));
				world.destroyBlock(pos, true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Minecraft.getMinecraft().player.sendChatMessage("//sel");
		int radius = ConfigHandler.minerBlock.radiusOnPrivate;
		AxisAlignedBB aabb = new AxisAlignedBB(pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius, pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius);
		List<EntityLivingBase> playersList = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
		for(EntityLivingBase player : playersList) {
			if(player instanceof EntityPlayer) {
				RegionHandler.sendRegionsToPlayer((EntityPlayer) player);
			}
		}
		
	}
	// TODO Blayzer code end

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.blockRain_item;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        TileEntityMinerBlock te = world.getTileEntity(pos) instanceof TileEntityMinerBlock ? (TileEntityMinerBlock) world.getTileEntity(pos) : null;
        if (te != null) {
            int energy = te.getEnergyStored();
            ItemStack stack = new ItemStack(Items.blockRain_item);
            if (energy > 0) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setInteger("energy", energy);
                stack.setTagCompound(nbt);
            }
            drops.add(stack);
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        //If it will harvest, delay deletion of the block until after getDrops
    	if(world.isRemote) return false;
    	
		Location<org.spongepowered.api.world.World> loc = new Location<org.spongepowered.api.world.World>((org.spongepowered.api.world.World) world, pos.getX(), pos.getY(), pos.getZ());
    	Region region = RedProtectAPI.getRegion(loc);
    	
		if(region != null) {
			//УДАЛЕНИЕ РЕГИОНА ПРИ ПОЛОМКЕ БЛОКА
			int claims = RedProtect.get().cfgs.root().region_settings.can_delete_first_home_after_claims;
			if (!region.canDelete() && (claims == -1 || RedProtect.get().rm.getPlayerRegions(((Player) player).getName(), ((Player) player).getWorld()) < claims) && !((Player) player).hasPermission("redprotect.bypass")){
				if (claims != -1){
					RPLang.sendMessage(((Player) player), RPLang.get("cmdmanager.region.cantdeletefirst-claims").replace("{claims}", ""+claims));
				} else {
					RPLang.sendMessage(((Player) player), RPLang.get("cmdmanager.region.cantdeletefirst"));
				}
				//return;
			}

            DeleteRegionEvent event = new DeleteRegionEvent(region, ((Player) player));
			if (Sponge.getEventManager().post(event)){
				//return;
			}

            String rname = region.getName();
            String w = region.getWorld();
            RedProtect.get().rm.remove(region, RedProtect.get().serv.getWorld(w).get());
            RPLang.sendMessage(((Player) player),RPLang.get("cmdmanager.region.deleted") +" "+ rname);
            RedProtect.get().logger.addLog("(World "+w+") Player "+((Player) player).getName()+" REMOVED region "+rname);
			//УДАЛЕНИЕ РЕГИОНА ПРИ ПОЛОМКЕ БЛОКА
            
			world.setBlockToAir(pos);
			world.removeTileEntity(pos);
			
	    	String key = region.getName() + "_" + region.getWorld(); //Назваие_Мир
	    	
			try {
				if(DBUtils.hasDBValue(key))
					DBUtils.deleteFromDB(key);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
        return willHarvest || super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
     */
    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool)
    {
        super.harvestBlock(world, player, pos, state, te, tool);
        world.setBlockToAir(pos);

    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return true;
    }
    
	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		if(side == EnumFacing.DOWN) {
			TileEntityMinerBlock te = (TileEntityMinerBlock) world.getTileEntity(pos);
			if (!te.isProtectionActive())
				return 3;
			else return 0;
		}
		return 0;
	}

	@Override
	public int getStrongPower(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return 0;
	}
	
	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMinerBlock();
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

}
