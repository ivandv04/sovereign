package legendev.sovereign.item;

import legendev.sovereign.util.FormatStrings;
import net.minecraft.client.item.TooltipType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UnravelledShardItem extends Item {

    public UnravelledShardItem() {
        super(new Settings().maxCount(64).rarity(Rarity.RARE));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context,
                              @NotNull List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal(FormatStrings.GRAY + "Unravelled"));
    }

}
