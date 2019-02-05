package com.blayzer.atomcore.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.input.Mouse;

import com.blayzer.atomcore.AtomCore;
import com.blayzer.atomcore.network.MessageRebootButton;
import com.blayzer.atomcore.tileentities.TileEntityMinerBlock;
import com.blayzer.atomcore.util.ModUtil;
import com.blayzer.atomcore.util.TextHelper;

public class ControllerContainerGui extends GuiContainer {

	public static boolean regionStatus = false;
	public static int regionPrice = 0;
    private static final int WIDTH = 200; //180
    private static final int HEIGHT = 141;
    private int buttonID = 0;
    private int cooldown = 5;

    private static final ResourceLocation controller = new ResourceLocation(ModUtil.MOD_ID, "textures/gui/controller/controller.png");
    private final TileEntityMinerBlock te;

    public ControllerContainerGui(TileEntityMinerBlock tileEntity, ContainerController containerController) {
        super(containerController);
        
        xSize = WIDTH;
        ySize = HEIGHT;
        te = tileEntity;
    }

    private static int getScaled(int max, int a, int pixels) {
        if (max == 0) {
            return 0;
        } else {
            return Math.round((float) a / max * pixels);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        mc.getTextureManager().bindTexture(controller);
        drawTexturedModalRect(guiLeft + 12, guiTop, 0, 0, 176, 112);
        
        int l = getScaled(te.getMaxEnergyStored(), te.getEnergyStored(), 68);
        drawTexturedModalRect(guiLeft + 51 + 118, guiTop - 4 + (78 - l), 176, (87 - l), 10, l);
        
        //l = getScaled(te.maxTimeLeft, te.timeLeft, 142);
        //drawTexturedModalRect(guiLeft + 12 + 4, guiTop + 112 - 7, 0, 112, l, 6);
        
        drawString(mc.fontRenderer, I18n.translateToLocalFormatted("message.rgname", te.rgkey), guiLeft + 20, guiTop + 10, 0xFFFFFF);
        drawString(mc.fontRenderer, I18n.translateToLocalFormatted("message.protectprice", regionPrice), guiLeft + 20, guiTop + 25, 0xFFFFFF);
        if(regionStatus)
        	drawString(mc.fontRenderer, I18n.translateToLocalFormatted("message.statusOn"), guiLeft + 20, guiTop + 40, 0xFFFFFF);
        else
        	drawString(mc.fontRenderer, I18n.translateToLocalFormatted("message.statusOff"), guiLeft + 20, guiTop + 40, 0xFFFFFF);
        
        //drawString(mc.fontRenderer, I18n.translateToLocalFormatted("message.info"), guiLeft + 20, guiTop + 90, 0xFFFFFF);
        
        CustomButton button = new CustomButton(buttonID, guiLeft+53, guiTop+72, 90, 14, I18n.translateToLocalFormatted("button.reboot"));
        buttonList.add(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        //energy bar
        if (isPointInRegion(51 + 118, 8, 10, 63, mouseX, mouseY))
            drawHoveringText(new ArrayList<>(Arrays.asList(I18n.translateToLocalFormatted("message.energy", TextHelper.getEnergyText(te.getEnergyStored()), TextHelper.getEnergyText(te.getMaxEnergyStored())))), mouseX - guiLeft, mouseY - guiTop);

        //progress bar
        //if (isPointInRegion(12 + 4, 112 - 7, 142, 7, mouseX, mouseY))
        //    drawHoveringText(new ArrayList<>(Arrays.asList("Time to pay:", TextHelper.getTimeText(te.timeLeft) + "/" + TextHelper.getTimeText(te.maxTimeLeft))), mouseX - guiLeft, mouseY - guiTop);
    }
    
    @Override
	protected void actionPerformed(GuiButton button)
    {
    	if(button.id == buttonID && cooldown == 5) {
    		cooldown--;
            System.out.println("Button clicked!");
            AtomCore.network.sendToServer((new MessageRebootButton(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ())));
    	}
    }
}
