package com.blayzer.atomcore.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import java.awt.Color;

public class CuboidRegion extends IRegion
{
    private Color color;
    private PointRegion first;
    private PointRegion second;
    
    public CuboidRegion() {
        this.color = new Color(250, 120, 200);
        this.first = new PointRegion();
        this.second = new PointRegion();
    }
    
    @Override
    public void render(final float partialTicks) {
        if (this.first.isValid() && this.second.isValid()) {
            this.renderGrid(partialTicks);
        }
    }
    
    private void renderGrid(final float partialTicks) {
        final float mx = this.first.coord.getX();
        final float my = this.first.coord.getY();
        final float mz = this.first.coord.getZ();
        final float SIZE_X = this.second.coord.getX() - this.first.coord.getX() - 0.1f + 1.0f;
        final float SIZE_Y = this.second.coord.getY() - this.first.coord.getY() - 0.1f + 1.0f;
        final float SIZE_Z = this.second.coord.getZ() - this.first.coord.getZ() - 0.1f + 1.0f;
        GL11.glPushMatrix();
        translateToEntity((Entity)Minecraft.getMinecraft().player, partialTicks);
        GL11.glDisable(2884);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(2.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.6f);
        GL11.glBegin(2);
        GL11.glVertex3f(mx + 0.1f, my + SIZE_Y, mz + 0.1f);
        GL11.glVertex3f(mx + SIZE_X, my + SIZE_Y, mz + 0.1f);
        GL11.glVertex3f(mx + SIZE_X, my + SIZE_Y, mz + SIZE_Z);
        GL11.glVertex3f(mx + 0.1f, my + SIZE_Y, mz + SIZE_Z);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3f(mx + 0.1f, my + 0.1f, mz + 0.1f);
        GL11.glVertex3f(mx + SIZE_X, my + 0.1f, mz + 0.1f);
        GL11.glVertex3f(mx + SIZE_X, my + 0.1f, mz + SIZE_Z);
        GL11.glVertex3f(mx + 0.1f, my + 0.1f, mz + SIZE_Z);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3f(mx + 0.1f, my + 0.1f, mz + 0.1f);
        GL11.glVertex3f(mx + SIZE_X, my + 0.1f, mz + 0.1f);
        GL11.glVertex3f(mx + SIZE_X, my + SIZE_Y, mz + 0.1f);
        GL11.glVertex3f(mx + 0.1f, my + SIZE_Y, mz + 0.1f);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3f(mx + 0.1f, my + 0.1f, mz + SIZE_Z);
        GL11.glVertex3f(mx + SIZE_X, my + 0.1f, mz + SIZE_Z);
        GL11.glVertex3f(mx + SIZE_X, my + SIZE_Y, mz + SIZE_Z);
        GL11.glVertex3f(mx + 0.1f, my + SIZE_Y, mz + SIZE_Z);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3f(mx + SIZE_X, my + 0.1f, mz + 0.1f);
        GL11.glVertex3f(mx + SIZE_X, my + 0.1f, mz + SIZE_Z);
        GL11.glVertex3f(mx + SIZE_X, my + SIZE_Y, mz + SIZE_Z);
        GL11.glVertex3f(mx + SIZE_X, my + SIZE_Y, mz + 0.1f);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3f(mx + 0.1f, my + 0.1f, mz + 0.1f);
        GL11.glVertex3f(mx + 0.1f, my + 0.1f, mz + SIZE_Z);
        GL11.glVertex3f(mx + 0.1f, my + SIZE_Y, mz + SIZE_Z);
        GL11.glVertex3f(mx + 0.1f, my + SIZE_Y, mz + 0.1f);
        GL11.glEnd();
        GL11.glColor4f(0.3f, 0.6f, 1.0f, 0.4f);
        GL11.glBegin(7);
        GL11.glVertex3f(mx + 0.1f, my + SIZE_Y, mz + 0.1f);
        GL11.glVertex3f(mx + SIZE_X, my + SIZE_Y, mz + 0.1f);
        GL11.glVertex3f(mx + SIZE_X, my + SIZE_Y, mz + SIZE_Z);
        GL11.glVertex3f(mx + 0.1f, my + SIZE_Y, mz + SIZE_Z);
        GL11.glVertex3f(mx + 0.1f, my + 0.1f, mz + 0.1f);
        GL11.glVertex3f(mx + SIZE_X, my + 0.1f, mz + 0.1f);
        GL11.glVertex3f(mx + SIZE_X, my + 0.1f, mz + SIZE_Z);
        GL11.glVertex3f(mx + 0.1f, my + 0.1f, mz + SIZE_Z);
        GL11.glVertex3f(mx + 0.1f, my + 0.1f, mz + 0.1f);
        GL11.glVertex3f(mx + SIZE_X, my + 0.1f, mz + 0.1f);
        GL11.glVertex3f(mx + SIZE_X, my + SIZE_Y, mz + 0.1f);
        GL11.glVertex3f(mx + 0.1f, my + SIZE_Y, mz + 0.1f);
        GL11.glVertex3f(mx + 0.1f, my + 0.1f, mz + SIZE_Z);
        GL11.glVertex3f(mx + SIZE_X, my + 0.1f, mz + SIZE_Z);
        GL11.glVertex3f(mx + SIZE_X, my + SIZE_Y, mz + SIZE_Z);
        GL11.glVertex3f(mx + 0.1f, my + SIZE_Y, mz + SIZE_Z);
        GL11.glVertex3f(mx + SIZE_X, my + 0.1f, mz + 0.1f);
        GL11.glVertex3f(mx + SIZE_X, my + 0.1f, mz + SIZE_Z);
        GL11.glVertex3f(mx + SIZE_X, my + SIZE_Y, mz + SIZE_Z);
        GL11.glVertex3f(mx + SIZE_X, my + SIZE_Y, mz + 0.1f);
        GL11.glVertex3f(mx + 0.1f, my + 0.1f, mz + 0.1f);
        GL11.glVertex3f(mx + 0.1f, my + 0.1f, mz + SIZE_Z);
        GL11.glVertex3f(mx + 0.1f, my + SIZE_Y, mz + SIZE_Z);
        GL11.glVertex3f(mx + 0.1f, my + SIZE_Y, mz + 0.1f);
        GL11.glEnd();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(2884);
        GL11.glPopMatrix();
    }
    
    @Override
    public void setPoint(final int id, final int x, final int y, final int z) {
        if (id == 0) {
            this.first.coord = new BlockPos(x, y, z);
        }
        else if (id == 1) {
            this.second.coord = new BlockPos(x, y, z);
        }
    }
    
    public static void translateToEntity(final Entity entity, final float ticks) {
        GL11.glTranslated(-entity.lastTickPosX - (-entity.lastTickPosX + entity.posX) * ticks, -entity.lastTickPosY - (-entity.lastTickPosY + entity.posY) * ticks, -entity.lastTickPosZ - (-entity.lastTickPosZ + entity.posZ) * ticks);
    }
}
