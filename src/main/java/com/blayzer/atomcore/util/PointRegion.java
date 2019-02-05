package com.blayzer.atomcore.util;

import java.awt.Color;
import net.minecraft.util.math.BlockPos;

public class PointRegion
{
    public BlockPos coord;
    
    public void render(final Color color, final float opacity, final double offsetX, final double offsetY, final double offsetZ) {
        if (this.isValid()) {}
    }
    
    public boolean isValid() {
        return this.coord != null;
    }
}
