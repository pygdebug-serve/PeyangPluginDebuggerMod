package tokyo.peya.mod.peyangplugindebuggermod.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketInformationRequest;
import tokyo.peya.mod.peyangplugindebuggermod.PeyangPluginDebuggerMod;

import java.util.UUID;

public class ServerEventHandler
{
    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent event)
    {
        if (!(event.getEntity() instanceof PlayerEntity))
            return;

        ClientPlayerEntity player = Minecraft.getInstance().player;

        if (player == null)
            return;

        UUID self = player.getUniqueID();

        if (event.getEntity().getUniqueID() != self)
            return;

        PeyangPluginDebuggerMod.INSTANCE.mainChannel.sendPacket(
                new PacketInformationRequest(PacketInformationRequest.Action.PLATFORM));
        PeyangPluginDebuggerMod.INSTANCE.mainChannel.sendPacket(
                new PacketInformationRequest(PacketInformationRequest.Action.SERVER_STATUS));

    }
}
