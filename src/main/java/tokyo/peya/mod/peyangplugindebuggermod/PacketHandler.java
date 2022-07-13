package tokyo.peya.mod.peyangplugindebuggermod;

import tokyo.peya.plugin.peyangplugindebugger.networking.OutgoingMessage;

public interface PacketHandler
{
    void handlePacket(byte id, OutgoingMessage packet);
}
