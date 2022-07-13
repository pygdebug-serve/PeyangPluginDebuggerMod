package tokyo.peya.mod.peyangplugindebuggermod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import tokyo.peya.plugin.peyangplugindebugger.networking.OutgoingMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PacketIO
{
    private static final ObjectMapper MAPPER;

    private final SimpleChannel channel;
    private final PeyangPluginDebuggerMod mod;
    private final String name;

    private final List<PacketHandler> handlers;

    static {
        MAPPER = new ObjectMapper(new MessagePackFactory());
    }

    public PacketIO(PeyangPluginDebuggerMod mod, String name)
    {
        this.mod = mod;
        this.name = name;

        this.channel = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(PeyangPluginDebuggerMod.NAMESPACE_ROOT, name))
                .clientAcceptedVersions(version -> true)
                .serverAcceptedVersions(version -> true)
                .networkProtocolVersion(() -> "1")
                .simpleChannel();

        this.handlers = new ArrayList<>();
    }

    private void encode(OutgoingMessage message, PacketBuffer buffer)
    {
        try
        {
            buffer.writeBytes(MAPPER.writeValueAsBytes(message));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private <T extends OutgoingMessage> T decode(PacketBuffer buffer)
    {
        try
        {
            byte[] data = new byte[buffer.readableBytes() - 1];
            System.arraycopy(buffer.array(), 0, data, 0, data.length - 1);

            return MAPPER.readValue(data, new TypeReference<T>(){});
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private <T extends OutgoingMessage> void handle(T message, Supplier<? extends NetworkEvent.Context> ctx)
    {
        ctx.get().setPacketHandled(true);

        this.handlers.forEach(handler -> {
            try
            {
                handler.handlePacket(message.getId(), message);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T extends OutgoingMessage> void registerPacket(T message)
    {
        this.channel.registerMessage(
                message.getId(),
                (Class<T> ) new TypeReference<T>(){}.getType(),
                this::encode,
                this::decode,
                this::handle
                );
    }

    public void registerHandler(PacketHandler handler)
    {
        this.handlers.add(handler);
    }
}
