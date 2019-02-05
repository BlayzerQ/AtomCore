package com.blayzer.atomcore.network;

import com.blayzer.atomcore.gui.ControllerContainerGui;

import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageUpdateInfo implements IMessage
{
    boolean regionStatus;
    int regionPrice;
    
    public MessageUpdateInfo() {
    }
    
    public MessageUpdateInfo(boolean regionStatus, int regionPrice) {
        this.regionStatus = regionStatus;
        this.regionPrice = regionPrice;
    }
    
    public void toBytes(final ByteBuf buffer) {
    	buffer.writeBoolean(regionStatus);
    	buffer.writeInt(regionPrice);
    }
    
    public void fromBytes(final ByteBuf buffer) {
    	this.regionStatus = buffer.readBoolean();
    	this.regionPrice = buffer.readInt();
    }
    
    public static class Handler implements IMessageHandler<MessageUpdateInfo, IMessage>
    {
        public IMessage onMessage(final MessageUpdateInfo packet, final MessageContext message) {
        	ControllerContainerGui.regionStatus = packet.regionStatus;
        	ControllerContainerGui.regionPrice = packet.regionPrice;
            return null;
        }
    }
}
