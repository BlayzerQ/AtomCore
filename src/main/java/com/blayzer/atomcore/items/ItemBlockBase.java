package com.blayzer.atomcore.items;

import com.blayzer.atomcore.block.BaseBlock;
import com.blayzer.atomcore.util.ModUtil;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockBase extends ItemBlock {

    private final String id;

    public ItemBlockBase(Block block) {
        super(block);
        this.id = ((BaseBlock) block).getId();
        this.setUnlocalizedName(ModUtil.MOD_ID + "." + id);
        this.setRegistryName(id);
    }

    public String getModelName() {
        return ModUtil.MOD_ID + ":" + id;
    }

}
