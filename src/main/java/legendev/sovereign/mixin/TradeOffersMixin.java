package legendev.sovereign.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapDecorationTypes;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.util.Util;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(TradeOffers.class)
public class TradeOffersMixin {

    @Shadow
    public static final Map<VillagerProfession, Int2ObjectMap<TradeOffers.Factory[]>>
            PROFESSION_TO_LEVELED_TRADE = Util.make(Maps.newHashMap(),
            (map) -> {
                // Peasants lose all trading options
                // Instead, they will be given AI overrides to preform certain tasks
            });

    /*
     * WANDERING TRADER OVERRIDE
     * - Removed fish bucket trades
     */
    @Shadow
    public static final Int2ObjectMap<TradeOffers.Factory[]> WANDERING_TRADER_TRADES =
            new Int2ObjectOpenHashMap<>(ImmutableMap.of(
                    1, new TradeOffers.Factory[]{
                            new TradeOffers.SellItemFactory(Items.SEA_PICKLE, 2, 1, 5, 1),
                            new TradeOffers.SellItemFactory(Items.SLIME_BALL, 4, 1, 5, 1),
                            new TradeOffers.SellItemFactory(Items.GLOWSTONE, 2, 1, 5, 1),
                            new TradeOffers.SellItemFactory(Items.NAUTILUS_SHELL, 5, 1, 5, 1),
                            new TradeOffers.SellItemFactory(Items.FERN, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.SUGAR_CANE, 1, 1, 8, 1),
                            new TradeOffers.SellItemFactory(Items.PUMPKIN, 1, 1, 4, 1),
                            new TradeOffers.SellItemFactory(Items.KELP, 3, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.CACTUS, 3, 1, 8, 1),
                            new TradeOffers.SellItemFactory(Items.DANDELION, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.POPPY, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.BLUE_ORCHID, 1, 1, 8, 1),
                            new TradeOffers.SellItemFactory(Items.ALLIUM, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.AZURE_BLUET, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.RED_TULIP, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.ORANGE_TULIP, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.WHITE_TULIP, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.PINK_TULIP, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.OXEYE_DAISY, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.CORNFLOWER, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.LILY_OF_THE_VALLEY, 1, 1, 7, 1),
                            new TradeOffers.SellItemFactory(Items.WHEAT_SEEDS, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.BEETROOT_SEEDS, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.PUMPKIN_SEEDS, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.MELON_SEEDS, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.ACACIA_SAPLING, 5, 1, 8, 1),
                            new TradeOffers.SellItemFactory(Items.BIRCH_SAPLING, 5, 1, 8, 1),
                            new TradeOffers.SellItemFactory(Items.DARK_OAK_SAPLING, 5, 1, 8, 1),
                            new TradeOffers.SellItemFactory(Items.JUNGLE_SAPLING, 5, 1, 8, 1),
                            new TradeOffers.SellItemFactory(Items.OAK_SAPLING, 5, 1, 8, 1),
                            new TradeOffers.SellItemFactory(Items.SPRUCE_SAPLING, 5, 1, 8, 1),
                            new TradeOffers.SellItemFactory(Items.CHERRY_SAPLING, 5, 1, 8, 1),
                            new TradeOffers.SellItemFactory(Items.MANGROVE_PROPAGULE, 5, 1, 8, 1),
                            new TradeOffers.SellItemFactory(Items.RED_DYE, 1, 3, 12, 1),
                            new TradeOffers.SellItemFactory(Items.WHITE_DYE, 1, 3, 12, 1),
                            new TradeOffers.SellItemFactory(Items.BLUE_DYE, 1, 3, 12, 1),
                            new TradeOffers.SellItemFactory(Items.PINK_DYE, 1, 3, 12, 1),
                            new TradeOffers.SellItemFactory(Items.BLACK_DYE, 1, 3, 12, 1),
                            new TradeOffers.SellItemFactory(Items.GREEN_DYE, 1, 3, 12, 1),
                            new TradeOffers.SellItemFactory(Items.LIGHT_GRAY_DYE, 1, 3, 12, 1),
                            new TradeOffers.SellItemFactory(Items.MAGENTA_DYE, 1, 3, 12, 1),
                            new TradeOffers.SellItemFactory(Items.YELLOW_DYE, 1, 3, 12, 1),
                            new TradeOffers.SellItemFactory(Items.GRAY_DYE, 1, 3, 12, 1),
                            new TradeOffers.SellItemFactory(Items.PURPLE_DYE, 1, 3, 12, 1),
                            new TradeOffers.SellItemFactory(Items.LIGHT_BLUE_DYE, 1, 3, 12, 1),
                            new TradeOffers.SellItemFactory(Items.LIME_DYE, 1, 3, 12, 1),
                            new TradeOffers.SellItemFactory(Items.ORANGE_DYE, 1, 3, 12, 1),
                            new TradeOffers.SellItemFactory(Items.BROWN_DYE, 1, 3, 12, 1),
                            new TradeOffers.SellItemFactory(Items.CYAN_DYE, 1, 3, 12, 1),
                            new TradeOffers.SellItemFactory(Items.BRAIN_CORAL_BLOCK, 3, 1, 8, 1),
                            new TradeOffers.SellItemFactory(Items.BUBBLE_CORAL_BLOCK, 3, 1, 8, 1),
                            new TradeOffers.SellItemFactory(Items.FIRE_CORAL_BLOCK, 3, 1, 8, 1),
                            new TradeOffers.SellItemFactory(Items.HORN_CORAL_BLOCK, 3, 1, 8, 1),
                            new TradeOffers.SellItemFactory(Items.TUBE_CORAL_BLOCK, 3, 1, 8, 1),
                            new TradeOffers.SellItemFactory(Items.VINE, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.BROWN_MUSHROOM, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.RED_MUSHROOM, 1, 1, 12, 1),
                            new TradeOffers.SellItemFactory(Items.LILY_PAD, 1, 2, 5, 1),
                            new TradeOffers.SellItemFactory(Items.SMALL_DRIPLEAF, 1, 2, 5, 1),
                            new TradeOffers.SellItemFactory(Items.SAND, 1, 8, 8, 1),
                            new TradeOffers.SellItemFactory(Items.RED_SAND, 1, 4, 6, 1),
                            new TradeOffers.SellItemFactory(Items.POINTED_DRIPSTONE, 1, 2, 5, 1),
                            new TradeOffers.SellItemFactory(Items.ROOTED_DIRT, 1, 2, 5, 1),
                            new TradeOffers.SellItemFactory(Items.MOSS_BLOCK, 1, 2, 5, 1)},
                    2, new TradeOffers.Factory[]{
                            new TradeOffers.SellItemFactory(Items.PACKED_ICE, 3, 1, 6, 1),
                            new TradeOffers.SellItemFactory(Items.BLUE_ICE, 6, 1, 6, 1),
                            new TradeOffers.SellItemFactory(Items.GUNPOWDER, 1, 1, 8, 1),
                            new TradeOffers.SellItemFactory(Items.PODZOL, 3, 3, 6, 1)}
            ));

}