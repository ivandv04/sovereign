package legendev.sovereign.screen;

import legendev.sovereign.factiondata.FactionIdeology;
import legendev.sovereign.payload.CodexEditPayload;
import legendev.sovereign.payload.CoupPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class IdeologyBookScreen extends InteractiveBookScreen {

    private static final String PREF_PATH = "ideology_book_";

    private final FactionIdeology type;

    private final String getName;
    private final String getCode;

    public IdeologyBookScreen(@NotNull FactionIdeology type, String getName, String getCode) {
        super("World Codex", PREF_PATH + type.adj.toLowerCase(), 196, 180);
        this.type = type;
        this.getName = getName;
        this.getCode = getCode;
    }

    private TextFieldWidget nameInput;
    private TextFieldWidget codeInput;

    @Override
    protected void init() {
        super.init();

        nameInput = new TextFieldWidget(textRenderer,
                orgX() + 17, orgY() + 119, 102, 20, Text.literal("Name"));
        codeInput = new TextFieldWidget(textRenderer,
                orgX() + 17, orgY() + 139, 102, 20, Text.literal("Passcode"));

        nameInput.setTooltip(Tooltip.of(Text.literal("Reformed name")));
        codeInput.setTooltip(Tooltip.of(Text.literal("Reformed passcode")));

        ButtonWidget autoNameButton = ButtonWidget.builder(Text.literal("???"), b -> autoName())
                .dimensions(orgX() + 122, orgY() + 120, 55, 18)
                .tooltip(Tooltip.of(Text.literal(
                        "Generates a possible name and code for your reformed faction.")))
                .build();

        ButtonWidget coupButton = ButtonWidget.builder(Text.literal("Coup!"), b -> startCoup())
                .dimensions(orgX() + 122, orgY() + 140, 55, 18)
                .tooltip(Tooltip.of(Text.literal(
                        "Creates a new faction with the given name and code. " +
                                "Transfers over you and your capital plus all infantry deployed there.")))
                .build();

        addDrawableChild(nameInput);
        addDrawableChild(codeInput);

        addDrawableChild(autoNameButton);
        addDrawableChild(coupButton);
    }

    private void autoName() {
        nameInput.setText(type.adj + " " + getName);
        codeInput.setText(getCode + randI() + randI() + randI());
    }

    private void startCoup() {
        String name = nameInput.getText().strip();
        String code = codeInput.getText().strip();
        if (name.isBlank()) return;
        ClientPlayNetworking.send(new CoupPayload(type, name, code));
        this.close();
    }

    private static int randI() {
        return (int) (Math.random() * 10);
    }

}