package com.blayzer.atomcore.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

public class CustomButton extends GuiButton {
	 
    public CustomButton(int i, int j, int k, String s)
    {
        this(i, j, k, 120, 20, s);
    }
 
    public CustomButton(int i, int j, int k, int l, int i1, String s)
    {
        super(i, j, k, l, i1, s);
    }
 
    @Override
    public int getHoverState(boolean flag)
    {
        byte byte0 = 1;
        if (!enabled)
        {
                byte0 = 0;
                }
        else if (flag)
        {
                byte0 = 2;
        }
        return byte0;
    }
 
    @Override
    public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
    {
        FontRenderer fontrenderer = mc.fontRenderer;
        boolean flag = mx >= x && my >= y && mx < x + width && my < y + height;  //Flag, tells if your mouse is hovering the button
        if (flag)
        { // Hover Action
                drawBorderedRect(x, y, x + width, y + height, 1, 0xBFBFBF, 0xFFB22222);
                drawCenteredString(fontrenderer, displayString, x + width / 2, y + (height - 8) / 2, 0xFFFFFFFF);
        }
        else { // Normal
                drawBorderedRect(x, y, x + width, y + height, 1, 0xBFBFBF, 0xCC000000);
                drawCenteredString(fontrenderer, displayString, x + width / 2, y + (height - 8) / 2, 0xFFCCCCCC);
        }
    }
    
    public void drawBorderedRect(int x, int y, int x1, int y1, int size, int borderC, int insideC)
    {
        drawRect(x + size, y + size, x1 - size, y1 - size, insideC);
        drawRect(x + size, y + size, x1, y, borderC);
        drawRect(x, y, x + size, y1, borderC);
        drawRect(x1, y1, x1 - size, y + size, borderC);
        drawRect(x, y1 - size, x1, y1, borderC);
    }
 
}
