package legendev.sovereign.screen;

import legendev.sovereign.Sovereign;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public abstract class InteractiveBookScreen extends Screen {

    private final Identifier backgroundPath;

    private final int bW, bH;

    private int orginX;
    private int orginY;

    private static final int RESOLUTION = 256;

    public InteractiveBookScreen(String title, String path, int width, int height) {
        super(Text.literal(title));
        backgroundPath = new Identifier(Sovereign.ID, path);
        bW = width;
        bH = height;
    }

    @Override
    protected void init() {
        orginX = this.width / 2 - bW / 2;
        orginY = this.height / 2 - bH / 2;
    }

    @Override
    public void renderBackground(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawGuiTexture(backgroundPath, orginX, orginY, RESOLUTION, RESOLUTION);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    protected int orgX() {
        return orginX;
    }

    protected int orgY() {
        return orginY;
    }
}
