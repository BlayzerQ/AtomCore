package com.blayzer.atomcore.gui;

import com.blayzer.atomcore.tileentities.TileEntityMinerBlock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class ContainerController extends Container {

    private final TileEntityMinerBlock te;

    public ContainerController(IInventory playerInventory, TileEntityMinerBlock te) {
        this.te = te;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
    	return true;
        //return te.canInteractWith(playerIn);
    }
}
