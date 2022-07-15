package tokyo.peya.mod.peyangplugindebuggermod;

import tokyo.peya.lib.pygdebug.common.PacketBase;

public interface PacketHandler
{
    void registerPackets(PacketIO channel);

    void handlePacket(byte id, PacketBase packet);
}
