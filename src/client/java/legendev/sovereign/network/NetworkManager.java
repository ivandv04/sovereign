package legendev.sovereign.network;

import legendev.sovereign.network.c2s.CodexEditPacket;
import legendev.sovereign.network.c2s.CitizenAssignPacket;
import legendev.sovereign.network.c2s.CoupPacket;
import legendev.sovereign.network.s2c.CitizenPapersOpenPacket;
import legendev.sovereign.network.s2c.CodexOpenPacket;
import legendev.sovereign.network.s2c.IdeologyBookOpenPacket;
import legendev.sovereign.payload.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class NetworkManager {

    private NetworkManager() {
    }

    /*
     * Server PlayChannelHandler receiver methods should be of the form:
     * (Remember to include the server thread executor!)
     *
     * void receive(T payload, ServerPlayNetworking.Context context) {
     *      context.player().getServer().execute(() -> { ... });
     *      ...
     */

    public static void registerC2S() {
        // Codex Edit Packet
        PayloadTypeRegistry.playC2S().register(CodexEditPayload.ID, CodexEditPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(CodexEditPayload.ID, CodexEditPacket::receive);
        // Resident Assignment Packet
        PayloadTypeRegistry.playC2S().register(CitizenAssignPayload.ID, CitizenAssignPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(CitizenAssignPayload.ID, CitizenAssignPacket::receive);
        // Coup Packet
        PayloadTypeRegistry.playC2S().register(CoupPayload.ID, CoupPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(CoupPayload.ID, CoupPacket::receive);
    }

    /*
     * Client PlayChannelHandler receiver methods should be of the form:
     * (Remember to include the client thread executor!)
     *
     * void receive(T payload, ClientPlayNetworking.Context context) {
     *      context.client().execute(() -> { ... });
     *      ...
     */

    public static void registerS2C() {
        // Codex Open Packet
        PayloadTypeRegistry.playS2C().register(CodexOpenPayload.ID, CodexOpenPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(CodexOpenPayload.ID, CodexOpenPacket::receive);
        // Resident Papers Open Packet
        PayloadTypeRegistry.playS2C().register(CitizenPapersOpenPayload.ID, CitizenPapersOpenPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(CitizenPapersOpenPayload.ID, CitizenPapersOpenPacket::receive);
        // Ideology Book Open Packet
        PayloadTypeRegistry.playS2C().register(IdeologyBookOpenPayload.ID, IdeologyBookOpenPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(IdeologyBookOpenPayload.ID, IdeologyBookOpenPacket::receive);
    }

}
