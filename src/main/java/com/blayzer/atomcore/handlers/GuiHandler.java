package com.blayzer.atomcore.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import com.blayzer.atomcore.gui.ContainerController;
import com.blayzer.atomcore.gui.ContainerGenerator;
import com.blayzer.atomcore.gui.ControllerContainerGui;
import com.blayzer.atomcore.gui.GeneratorContainerGui;
import com.blayzer.atomcore.tileentities.TileEntityGeneratorBlock;
import com.blayzer.atomcore.tileentities.TileEntityMinerBlock;


public class GuiHandler implements IGuiHandler{

	public GuiHandler() {
		
	}

    public static final int GUI_GENERATORBLOCK = 0;
    public static final int GUI_CONTROLLERBLOCK = 1;

    @Nullable
    @Override
    //@SideOnly(Side.SERVER)
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case GUI_GENERATORBLOCK:
                return new ContainerGenerator(player.inventory, (TileEntityGeneratorBlock) te);
            case GUI_CONTROLLERBLOCK:
                return new ContainerController(player.inventory, (TileEntityMinerBlock) te);
        }

        return null;
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case GUI_GENERATORBLOCK:
                TileEntityGeneratorBlock containerTE = (TileEntityGeneratorBlock) te;
                return new GeneratorContainerGui(containerTE, new ContainerGenerator(player.inventory, containerTE));
            case GUI_CONTROLLERBLOCK:
                TileEntityMinerBlock controllerTE = (TileEntityMinerBlock) te;
                return new ControllerContainerGui(controllerTE, new ContainerController(player.inventory, controllerTE));
        }

        return null;
    }

}
