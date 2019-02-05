package com.blayzer.atomcore.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class IRegion
{
    public abstract void render(final float p0);
    
    public abstract void setPoint(final int p0, final int p1, final int p2, final int p3);
}
