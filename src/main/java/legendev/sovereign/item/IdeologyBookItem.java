package legendev.sovereign.item;

import legendev.sovereign.factiondata.Faction;
import legendev.sovereign.factiondata.FactionCornerstone;
import legendev.sovereign.factiondata.FactionIdeology;
import legendev.sovereign.payload.IdeologyBookOpenPayload;
import legendev.sovereign.persistent.FactionCodexState;
import legendev.sovereign.util.ChatUtil;
import legendev.sovereign.util.FormatStrings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.item.TooltipType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IdeologyBookItem extends Item {

    private final FactionIdeology type;

    public IdeologyBookItem(FactionIdeology type) {
        super(new Settings().maxCount(1).rarity(Rarity.RARE));
        this.type = type;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context,
                              @NotNull List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal(FormatStrings.GRAY + "'The Path To "
                + this.type.col + this.type.name + FormatStrings.GRAY + "'"));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(@NotNull World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && world.getServer() != null) {
            FactionCodexState state = FactionCodexState.getServerState(world.getServer());
            Faction faction = state.tryToFindPlayer(user.getUuid());
            if (faction == null) {
                ChatUtil.sendErrorOverlay(user, "You must be in a faction to read this");
                return super.use(world, user, hand);
            }
            ServerPlayNetworking.send((ServerPlayerEntity) user,
                    new IdeologyBookOpenPayload(type, faction.name, faction.passcode));
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }
}
