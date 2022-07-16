package tokyo.peya.mod.peyangplugindebuggermod;

import tokyo.peya.lib.pygdebug.common.Packet;
import tokyo.peya.lib.pygdebug.common.PacketBase;

public class Utils
{
    public static byte getPacketId(PacketBase packet)
    {
        return getPacketId(packet.getClass());
    }

    public static byte getPacketId(Class<? extends PacketBase> packetClass)
    {
        return packetClass.getAnnotation(Packet.class).value();
    }
}
