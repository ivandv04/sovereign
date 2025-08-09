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
public class ConscriptPapersScreen extends InteractiveBookScreen {

    private final UUID targEntity;
    private final long targFaction;

    public ConscriptPapersScreen(UUID targEntity, long targFaction) {
        super("Conscript Papers", "conscript_papers", 132, 98);
        this.targEntity = targEntity;
        this.targFaction = targFaction;
    }

    private TextFieldWidget nameInput;

    @Override
    protected void init() {
        super.init();

        nameInput = new TextFieldWidget(textRenderer,
                orgX() + 11, orgY() + 49, 110, 20, Text.literal("Name"));

        nameInput.setTooltip(Tooltip.of(Text.literal("A name for this infantry unit")));

        ButtonWidget randomButton = ButtonWidget.builder(Text.literal("?"), b -> randomName())
                .dimensions(orgX() + 11, orgY() + 69, 20, 18)
                .tooltip(Tooltip.of(Text.literal(
                        "Generates a random name.")))
                .build();

        ButtonWidget confirmButton = ButtonWidget.builder(Text.literal("Enlist Infantry"), b -> createInfantry())
                .dimensions(orgX() + 31, orgY() + 69, 90, 18)
                .tooltip(Tooltip.of(Text.literal(
                        "Conscripts this villager into your faction army if it is" +
                                " standing at a friendly Barracks or Camp cornerstone.")))
                .build();

        addDrawableChild(nameInput);
        addDrawableChild(confirmButton);
        addDrawableChild(randomButton);
    }

    private void createInfantry() {
        if (nameInput.getText().isBlank()) return;
        ClientPlayNetworking.send(new CitizenAssignPayload(targEntity, CitizenType.INFANTRY,
                nameInput.getText(), targFaction));
        this.close();
    }

    private void randomName() {
        nameInput.setText(RandomGen.randomTitledName());
    }

}