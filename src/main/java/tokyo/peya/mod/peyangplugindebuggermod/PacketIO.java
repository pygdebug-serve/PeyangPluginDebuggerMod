package tokyo.peya.mod.peyangplugindebuggermod;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import tokyo.peya.lib.pygdebug.common.PacketBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class PacketIO
{
    private static final ObjectMapper MAPPER;

    private static final ObjectWriter WRITER;

    private final SimpleChannel channel;
    private final PeyangPluginDebuggerMod mod;
    private final String name;

    private final List<PacketHandler> handlers;

    private final HashMap<Byte, Class<? extends PacketBase>> classCaches;

    static {
        MAPPER = new ObjectMapper(new MessagePackFactory());

        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        WRITER = MAPPER.writer().withoutAttribute("id");
    }

    public PacketIO(PeyangPluginDebuggerMod mod, String name)
    {
        this.mod = mod;
        this.name = name;

        this.channel = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(PeyangPluginDebuggerMod.NAMESPACE_ROOT, name),
                () -> "1",
                s -> true,
                s -> true
        );

        this.handlers = new ArrayList<>();

        this.classCaches = new HashMap<>();
    }

    private void encode(PacketBase message, PacketBuffer buffer)
    {
        try
        {
            buffer.writeBytes(WRITER.writeValueAsBytes(message));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends PacketBase> T decode(PacketBuffer buffer)
    {
        try
        {
            byte[] data = new byte[buffer.readableBytes()];
            buffer.getBytes(0, data);
            byte[] newData = new byte[data.length - 1];

            System.arraycopy(data, 1, newData, 0, data.length - 1);

            Class<?> clazz = this.classCaches.get(data[0]);

            return (T) MAPPER.readValue(newData, clazz);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private <T extends PacketBase> void handle(T message, Supplier<? extends NetworkEvent.Context> ctx)
    {
        ctx.get().setPacketHandled(true);

        this.handlers.forEach(handler -> {
            try
            {
                handler.handlePacket(Utils.getPacketId(message), message);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    public void registerPacket(byte id, Class<? extends PacketBase> packetType)
    {
        this.channel.registerMessage(
                id,
                packetType,
                this::encode,
                this::decode,
                this::handle
                );

        this.classCaches.put(id, packetType);
    }

    public void registerPacket(Class<? extends PacketBase> packet)
    {
        this.registerPacket(Utils.getPacketId(packet), packet);
    }

    public void registerHandler(PacketHandler handler)
    {
        this.handlers.add(handler);

        handler.registerPackets(this);
    }

    public void sendPacket(PacketBase message)
    {
        this.channel.sendToServer(message);
    }
}
