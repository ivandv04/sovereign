package legendev.sovereign.registry.types;

import legendev.sovereign.factiondata.FactionIdeology;
import legendev.sovereign.factiondata.TerritoryType;
import legendev.sovereign.item.*;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class SovereignItems {

    private SovereignItems() {
    }

    // Generic Items
    public static final Item PIG_IRON_INGOT_ITEM = new Item(itemInit(64));
    public static final Item EMPTY_NOTEBOOK_ITEM = new Item(itemInit(64));
    public static final Item CORNERSTONE_SHARD_ITEM = new Item(itemInit(64));
    public static final Item SILK_BALL_ITEM = new Item(itemInit(64));
    public static final Item RESIDENT_PAPERS_ITEM = new Item(itemInit(16));
    public static final Item CONSCRIPT_PAPERS_ITEM = new Item(itemInit(16));
    public static final Item PAPERS_PLEASE_ITEM = new Item(itemInit(16));
    public static final Item EMPTY_CAN = new Item(itemInit(16));

    // Food Items
    public static final Item ROASTED_CORPSE = new Item(foodItem(64, 1, 0.0f));
    public static final Item CORPSE_STARCH = new Item(foodItem(16, 2, 0.4f));

    // Advanced Items
    public static final WorldCodexItem WORLD_CODEX_ITEM = new WorldCodexItem();
    public static final UnravelledShardItem UNRAVELLED_SHARD_ITEM = new UnravelledShardItem();

    // Abstracted Items
    public static final BuildTerritoryItem BUILD_CAPITAL_ITEM = terrItem(TerritoryType.CAPITAL);
    public static final BuildTerritoryItem BUILD_FARM_ITEM = terrItem(TerritoryType.FARM);
    public static final BuildTerritoryItem BUILD_MINE_ITEM = terrItem(TerritoryType.MINE);
    public static final BuildTerritoryItem BUILD_RESIDENCE_ITEM = terrItem(TerritoryType.RESIDENCE);
    public static final BuildTerritoryItem BUILD_TEMPLE_ITEM = terrItem(TerritoryType.TEMPLE);
    public static final BuildTerritoryItem BUILD_BARRACKS_ITEM = terrItem(TerritoryType.BARRACKS);
    public static final BuildTerritoryItem BUILD_CAMP_ITEM = terrItem(TerritoryType.CAMP);
    public static final IdeologyBookItem ANARCHISM_BOOK = ideoItem(FactionIdeology.ANARCHISM);
    public static final IdeologyBookItem COMMUNALISM_BOOK = ideoItem(FactionIdeology.COMMUNALISM);
    public static final IdeologyBookItem FEUDALISM_BOOK = ideoItem(FactionIdeology.FEUDALISM);
    public static final IdeologyBookItem OLIGARCHISM_BOOK = ideoItem(FactionIdeology.OLIGARCHISM);
    public static final IdeologyBookItem TOTALITARIANISM_BOOK = ideoItem(FactionIdeology.TOTALITARIANISM);

    private static Item.Settings itemInit(int stackSize) {
        return new Item.Settings().maxCount(stackSize);
    }

    private static Item.Settings foodItem(int stackSize, int hunger, float saturation) {
        return itemInit(stackSize).food(new FoodComponent.Builder()
                .nutrition(hunger).saturationModifier(saturation).build());
    }

    private static @NotNull BuildTerritoryItem terrItem(TerritoryType type) {
        return new BuildTerritoryItem(type);
    }

    private static @NotNull IdeologyBookItem ideoItem(FactionIdeology type) {
        return new IdeologyBookItem(type);
    }
}