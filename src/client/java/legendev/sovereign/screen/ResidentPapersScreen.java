package legendev.sovereign.screen;

import legendev.sovereign.factiondata.CitizenType;
import legendev.sovereign.payload.CitizenAssignPayload;
import legendev.sovereign.util.RandomGen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class ResidentPapersScreen extends InteractiveBookScreen {

    private final UUID targEntity;
    private final long targFaction;

    public ResidentPapersScreen(UUID targEntity, long targFaction) {
        super("Resident Papers", "resident_papers", 132, 98);
        this.targEntity = targEntity;
        this.targFaction = targFaction;
    }

    private TextFieldWidget nameInput;

    @Override
    protected void init() {
        super.init();

        nameInput = new TextFieldWidget(textRenderer,
                orgX() + 11, orgY() + 49, 110, 20, Text.literal("Name"));

        nameInput.setTooltip(Tooltip.of(Text.literal("A name for this peasant")));

        ButtonWidget randomButton = ButtonWidget.builder(Text.literal("?"), b -> randomName())
                .dimensions(orgX() + 11, orgY() + 69, 20, 18)
                .tooltip(Tooltip.of(Text.literal(
                        "Generates a random name.")))
                .build();

        ButtonWidget confirmButton = ButtonWidget.builder(Text.literal("Make Peasant"), b -> createPeasant())
                .dimensions(orgX() + 31, orgY() + 69, 90, 18)
                .tooltip(Tooltip.of(Text.literal(
                        "Turns this villager into your peasant " +
                                "and assigns it to the faction cornerstone it is standing at.")))
                .build();

        addDrawableChild(nameInput);
        addDrawableChild(randomButton);
        addDrawableChild(confirmButton);
    }

    private void createPeasant() {
        if (nameInput.getText().isBlank()) return;
        ClientPlayNetworking.send(new CitizenAssignPayload(targEntity, CitizenType.PEASANT,
                nameInput.getText(), targFaction));
        this.close();
    }

    private void randomName() {
        nameInput.setText(RandomGen.randomNameShort());
    }

}