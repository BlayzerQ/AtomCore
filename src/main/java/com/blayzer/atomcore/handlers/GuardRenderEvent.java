package com.blayzer.atomcore.handlers;

import java.util.Iterator;

import com.blayzer.atomcore.AtomCore;
import com.blayzer.atomcore.proxy.ClientProxy;
import com.blayzer.atomcore.util.CuboidRegion;
import com.blayzer.atomcore.util.RegionData;
import com.blayzer.atomcore.util.RegionData.Area;
import com.blayzer.atomcore.util.RegionData.Area.Vec3;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import org.lwjgl.opengl.GL11;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraft.client.Minecraft;

public class GuardRenderEvent
{
    public Minecraft mc;
    
    public GuardRenderEvent() {
        this.mc = Minecraft.getMinecraft();
    }
    
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ClientProxy.keyActivatedRender.isPressed()) {
        	
//        	mc.player.sendChatMessage("/rggetlist");
            AtomCore.isRenderer = !AtomCore.isRenderer;
            return;
        }
    }
    
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (AtomCore.isRenderer) {
            Minecraft.getMinecraft().mcProfiler.startSection("RenderRegions");
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            GL11.glDepthMask(false);
            GL11.glDisable(3008);
            GL11.glPolygonOffset(-3.0f, -3.0f);
            GL11.glEnable(32823);
            GL11.glEnable(3008);
            GL11.glDisable(3553);
            try {
                GL11.glLineWidth(3.0f);
                this.preRender(event.getPartialTicks());
            }
            catch (Throwable var3) {
                System.out.println("Not render! Kapyt maincraft'y");
            }
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glDisable(3008);
            GL11.glPolygonOffset(0.0f, 0.0f);
            GL11.glDisable(32823);
            GL11.glEnable(3008);
            GL11.glDepthMask(true);
            GL11.glPopMatrix();
            Minecraft.getMinecraft().mcProfiler.endSection();
        }
    }
    
    public void preRender(final float ticks) {
        try {
            CuboidRegion thr = null;
            for (RegionData rg : AtomCore.regions_client) {
                thr = new CuboidRegion();
                thr.setPoint(0, rg.area.min.x, rg.area.min.y, rg.area.min.z);
                thr.setPoint(1, rg.area.max.x, rg.area.max.y, rg.area.max.z);
                thr.render(ticks);
            }
        }
        catch (Throwable var4) {
            var4.printStackTrace();
        }
    }
}
