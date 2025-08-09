package legendev.sovereign.registry.types;

import legendev.sovereign.block.CornerstoneBlock;
import net.minecraft.block.AbstractBlock;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class SovereignBlocks {

    private SovereignBlocks() {
    }

    // Blocks
    public static final CornerstoneBlock CORNERSTONE_BLOCK = new CornerstoneBlock(blockInit());

    @Contract(value = " -> new", pure = true)
    private static AbstractBlock.@NotNull Settings blockInit() {
        return AbstractBlock.Settings.create();
    }
}
