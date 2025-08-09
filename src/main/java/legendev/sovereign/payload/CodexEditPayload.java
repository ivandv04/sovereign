package legendev.sovereign.payload;

import legendev.sovereign.util.PayloadUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record CodexEditPayload(String name, String passcode, Task task) implements CustomPayload {

    public static final Id<CodexEditPayload> ID
            = PayloadUtil.createId("codex_edit_payload");

    public static final PacketCodec<PacketByteBuf, CodexEditPayload> CODEC
            = PacketCodec.of(CodexEditPayload::deconstruct, CodexEditPayload::construct);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    /*
     * The PacketByteBuf will contain a concatenated string of a sample faction
     * name and passcode, as well as an int representing the index of the starting character
     * of the passcode in the concatented string. If there is NO passocde, then send -1 instead.
     */

    private static void deconstruct(@NotNull CodexEditPayload payload, @NotNull PacketByteBuf buf) {
        String cat = payload.name + payload.passcode;
        int br = payload.passcode.isEmpty() ? -1 : payload.name.length();
        buf.writeString(cat).writeInt(br).writeEnumConstant(payload.task);
    }

    @Contract("_ -> new")
    private static @NotNull CodexEditPayload construct(@NotNull PacketByteBuf buf) {
        String cat = buf.readString();
        int br = buf.readInt();
        String name, passcode;
        if (br < 0) {
            name = cat;
            passcode = "";
        } else {
            name = cat.substring(0, br);
            passcode = cat.substring(br);
        }
        return new CodexEditPayload(name, passcode, buf.readEnumConstant(Task.class));
    }

    /**
     * Used to indicate the desired codex task to be completed.
     */
    public enum Task {
        /**
         * Creates a new faction. Data buffer requires name/code.
         * </p> Required states (will post error in chat if not met):
         * <ul>
         *     <li>Given faction does not exist</li>
         * </ul>
         */
        CREATE,
        /**
         * Joins a pre-existing faction. Data buffer requires name/code.
         * </p> Required states (will post error in chat if not met):
         * <ul>
         *     <li>Given faction exists</li>
         *     <li>Given faction is not current one</li>
         *     <li>Given passcode is correct</li>
         * </ul>
         */
        JOIN,
        /**
         * Assigns current faction a new name and/or code. Data buffer requires name/code.
         * </p> Required states (will post error in chat if not met):
         * <ul>
         *     <li>Currently in an faction</li>
         *     <li>New name is not the name of another faction</li>
         *     <li>New name and/or code is different than current</li>
         * </ul>
         */
        EDIT,
        /**
         * Deletes the current faction.
         * </p> Required states (will post error in chat if not met):
         * <ul>
         *     <li>Currently in an faction</li>
         *     <li>Faction has no cornerstones or infantry</li>
         * </ul>
         */
        DESTROY,
        /**
         * Leaves the current faction.
         * </p> Required states (will post error in chat if not met):
         * <ul>
         *     <li>Currently in an faction</li>
         * </ul>
         */
        LEAVE,
        /**
         * Lists all active factions to chat if any exist.
         */
        LIST;

        // destroy, leave, list: do not require inputs
        public boolean permitsBlank() {
            return this == DESTROY || this == LEAVE || this == LIST;
        }

    }

}
