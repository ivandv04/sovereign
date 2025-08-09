package legendev.sovereign.util;

import legendev.sovereign.factiondata.Faction;
import legendev.sovereign.persistent.FactionCodexState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ChatUtil {

    private ChatUtil() {
    }

    public static void send(@NotNull PlayerEntity player, String msg, boolean overlay) {
        player.sendMessage(Text.literal(msg), overlay);
    }

    public static void sendChat(PlayerEntity player, String msg) {
        send(player, msg, false);
    }

    public static void sendFactionMessage(@NotNull ServerWorld world, @NotNull Faction faction, String msg) {
        for (ServerPlayerEntity player : world.getPlayers())
            if (faction.hasMember(player.getUuid()))
                sendChat(player, bind(faction.baseNameColoured(), msg));
    }

    public static void sendOverlay(PlayerEntity player, String msg) {
        send(player, msg, true);
    }

    public static void sendErrorOverlay(PlayerEntity player, String msg) {
        sendOverlay(player, FormatStrings.RED + msg);
    }

    public static void codexBroadcast(@NotNull MinecraftServer server, String msg) {
        server.getPlayerManager().broadcast(
                Text.literal(bind(FactionCodexState.CHAT_REFER, msg)), false);
    }

    @Contract(pure = true)
    private static @NotNull String bind(String name, String msg) {
        return "[" + name + "] " + msg;
    }

}