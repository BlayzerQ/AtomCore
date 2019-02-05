package com.blayzer.atomcore.block;

import net.minecraft.block.Block;

public interface BaseBlock {
    String getModelName();
    String getTileEntityName();
    Block getBlock();
    String getId();
}
