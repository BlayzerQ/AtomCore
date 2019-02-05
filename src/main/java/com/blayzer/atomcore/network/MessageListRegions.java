package com.blayzer.atomcore.network;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.util.LinkedList;

import com.blayzer.atomcore.AtomCore;
import com.blayzer.atomcore.util.RegionData;

import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageListRegions implements IMessage
{
    int index;
    byte[] bytes;
    int length;
    
    public MessageListRegions() {
    }
    
    public MessageListRegions(final int index, final byte[] bytes) {
        this.index = index;
        this.bytes = bytes;
        this.length = bytes.length;
    }
    
    public void toBytes(final ByteBuf buffer) {
        buffer.writeInt(this.length);
        for (int i = 0; i < this.length; ++i) {
            buffer.writeByte((int)this.bytes[i]);
        }
        buffer.writeInt(this.index);
    }
    
    public void fromBytes(final ByteBuf buffer) {
        this.length = buffer.readInt();
        this.bytes = new byte[this.length];
        for (int i = 0; i < this.length; ++i) {
            this.bytes[i] = buffer.readByte();
        }
        this.index = buffer.readInt();
    }
    
    public static class Handler implements IMessageHandler<MessageListRegions, IMessage>
    {
        public IMessage onMessage(final MessageListRegions packet, final MessageContext message) {
            try {
                final Object o = this.getObject(packet.bytes);
                switch (packet.index) {
                    case 0: {
//                        System.out.println("Object RGVisual: " + o);
                        AtomCore.regions_client = (LinkedList<RegionData>) o;
                        break;
                    }
//                    case 1: {
//                        System.out.println("Object Display OstiumGuard: " + o);
//                        String k = ClientProxy.regions_client = (String)o;
//                        if (k.isEmpty()) {
//                            k = "§aСвободная территория";
//                        }
//                        else {
//                            k = "§4[" + ClientProxy.regions_client + "§4]";
//                        }
//                        RenderEvent.setTextRegion(k, 5);
//                        break;
//                    }
                }
            }
            catch (Exception ex) {
                System.out.println("Synchronizing error - RGVisual (" + packet.index + ")");
                ex.printStackTrace();
            }
            return null;
        }
        
        public Object getObject(final byte[] bytes) throws Exception {
            final ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInput in = null;
            Object o;
            try {
                in = new ObjectInputStream(bis);
                o = in.readObject();
            }
            finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                }
                catch (IOException ex) {
                    System.out.println("Synchronizing error");
                }
            }
            return o;
        }
    }
}
