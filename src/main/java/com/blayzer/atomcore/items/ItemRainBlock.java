package com.blayzer.atomcore.items;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import com.blayzer.atomcore.tileentities.TileEntityMinerBlock;
import com.blayzer.atomcore.util.TextHelper;

import java.util.List;

public class ItemRainBlock extends ItemBlockBase {

    public ItemRainBlock(Block block) {
        super(block);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (hasNbtTag(stack, "energy")) {
            int energy = stack.getTagCompound().getInteger("energy");
            tooltip.add("Energy: " + TextHelper.getEnergyText(energy) + " FE");
        } else {
            tooltip.add("Energy: 0 FE");
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);

        if (hasNbtTag(stack,"energy")){
            try{
                int energy = stack.getTagCompound().getInteger("energy");
                TileEntityMinerBlock te = (TileEntityMinerBlock) world.getTileEntity(pos);
                te.setEnergy(energy, true);
            }catch (Exception e){}
        }

        return true;
    }

    private static boolean hasNbtTag(ItemStack stack, String pNbtTag) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey(pNbtTag);
    }
}
