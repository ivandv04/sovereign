package legendev.sovereign.screen;

import legendev.sovereign.payload.CodexEditPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class WorldCodexScreen extends InteractiveBookScreen {

    public WorldCodexScreen() {
        super("World Codex", "world_codex", 146, 180);
    }

    private TextFieldWidget nameInput;
    private TextFieldWidget codeInput;

    @Override
    protected void init() {
        super.init();

        nameInput = new TextFieldWidget(textRenderer,
                orgX() + 17, orgY() + 66, 110, 20, Text.literal("Name"));
        codeInput = new TextFieldWidget(textRenderer,
                orgX() + 17, orgY() + 86, 110, 20, Text.literal("Passcode"));

        nameInput.setTooltip(Tooltip.of(Text.literal("Faction name")));
        codeInput.setTooltip(Tooltip.of(Text.literal("Faction passcode")));

        ButtonWidget createButton = ButtonWidget.builder(Text.literal("Create"), b -> createFaction())
                .dimensions(orgX() + 17, orgY() + 106, 42, 18)
                .tooltip(Tooltip.of(Text.literal(
                        "Creates a new faction with the given name and passcode.")))
                .build();

        ButtonWidget joinButton = ButtonWidget.builder(Text.literal("Join"), b -> joinFaction())
                .dimensions(orgX() + 59, orgY() + 106, 33, 18)
                .tooltip(Tooltip.of(Text.literal(
                        "Joins the faction of the given name if the given passcode is correct.")))
                .build();

        ButtonWidget editButton = ButtonWidget.builder(Text.literal("Edit"), b -> editFaction())
                .dimensions(orgX() + 92, orgY() + 106, 35, 18)
                .tooltip(Tooltip.of(Text.literal(
                        "Changes the name and passcode of your current faction to the ones given above.")))
                .build();

        ButtonWidget destroyButton = ButtonWidget.builder(Text.literal("Destroy"), b -> destroyFaction())
                .dimensions(orgX() + 17, orgY() + 124, 55, 18)
                .tooltip(Tooltip.of(Text.literal(
                        "Destroys your current faction if it has no cornerstones.")))
                .build();

        ButtonWidget leaveButton = ButtonWidget.builder(Text.literal("Leave"), b -> leaveFaction())
                .dimensions(orgX() + 72, orgY() + 124, 55, 18)
                .tooltip(Tooltip.of(Text.literal(
                        "Leaves your current faction.")))
                .build();

        ButtonWidget listButton = ButtonWidget.builder(Text.literal("List All Factions"), b -> listFactions())
                .dimensions(orgX() + 17, orgY() + 142, 110, 18)
                .tooltip(Tooltip.of(Text.literal(
                        "Lists all active world factions and their stats, if any exist.")))
                .build();

        addDrawableChild(nameInput);
        addDrawableChild(codeInput);

        addDrawableChild(createButton);
        addDrawableChild(joinButton);
        addDrawableChild(editButton);
        addDrawableChild(destroyButton);
        addDrawableChild(leaveButton);
        addDrawableChild(listButton);
    }

    private void createFaction() {
        abstractAlteration(CodexEditPayload.Task.CREATE);
    }

    private void joinFaction() {
        abstractAlteration(CodexEditPayload.Task.JOIN);
    }

    private void editFaction() {
        abstractAlteration(CodexEditPayload.Task.EDIT);
    }

    private void destroyFaction() {
        abstractAlteration(CodexEditPayload.Task.DESTROY);
    }

    private void leaveFaction() {
        abstractAlteration(CodexEditPayload.Task.LEAVE);
    }

    private void listFactions() {
        abstractAlteration(CodexEditPayload.Task.LIST);
    }

    private void abstractAlteration(CodexEditPayload.Task task) {
        String name = nameInput.getText().strip();
        String code = codeInput.getText().strip();
        if (name.isBlank() && !task.permitsBlank()) return;
        ClientPlayNetworking.send(new CodexEditPayload(
                name, code, task));
        this.close();
    }

}