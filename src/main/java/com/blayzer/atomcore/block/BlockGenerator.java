package com.blayzer.atomcore.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import com.blayzer.atomcore.AtomCore;
import com.blayzer.atomcore.handlers.GuiHandler;
import com.blayzer.atomcore.items.Items;
import com.blayzer.atomcore.tileentities.TileEntityBase;
import com.blayzer.atomcore.tileentities.TileEntityGeneratorBlock;
import com.blayzer.atomcore.util.ModUtil;

import java.util.Random;

public class BlockGenerator extends BlockBase implements ITileEntityProvider {

    public BlockGenerator() {
        super(Material.IRON, ModUtil.BLOCK_GENERATOR);
        this.setHardness(1.5F);
        setResistance(10f);
        setCreativeTab(CreativeTabs.MISC);
        this.setSoundType(SoundType.METAL);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        TileEntity te = worldIn.getTileEntity(pos);
        if (!(te instanceof TileEntityGeneratorBlock)) {
            return false;
        }
        playerIn.openGui(AtomCore.instance, GuiHandler.GUI_GENERATORBLOCK, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.blockgenerator_item;
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityGeneratorBlock) {
            ((TileEntityGeneratorBlock) tileentity).dropInventory(worldIn, pos, (TileEntityBase) tileentity);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityGeneratorBlock();
    }
}
