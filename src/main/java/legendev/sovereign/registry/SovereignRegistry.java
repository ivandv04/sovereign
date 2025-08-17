package legendev.sovereign.registry;

import legendev.sovereign.Sovereign;
import legendev.sovereign.entity.VillagerInfantryEntity;
import legendev.sovereign.event.CitizenKilledEvent;
import legendev.sovereign.event.CornerstoneRemoveEvent;
import legendev.sovereign.event.RenameEntityEvent;
import legendev.sovereign.event.CitizenAssignmentEvent;
import legendev.sovereign.registry.types.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;

public final class SovereignRegistry {

    private SovereignRegistry() {
    }

    // Item Groups
    public static final ItemGroup SOVEREIGN_GROUP = FabricItemGroup.builder().icon(
                    () -> new ItemStack(SovereignItems.WORLD_CODEX_ITEM)).displayName(
                    Text.translatable("itemGroup.sovereign.sovereign"))
            .entries((displayContext, entries) -> {
                entries.add(SovereignItems.EMPTY_NOTEBOOK_ITEM);
                entries.add(SovereignItems.WORLD_CODEX_ITEM);
                entries.add(SovereignItems.FEUDALISM_BOOK);
                entries.add(SovereignItems.COMMUNALISM_BOOK);
                entries.add(SovereignItems.OLIGARCHISM_BOOK);
                entries.add(SovereignItems.ANARCHISM_BOOK);
                entries.add(SovereignItems.TOTALITARIANISM_BOOK);
                entries.add(SovereignItems.BUILD_CAPITAL_ITEM);
                entries.add(SovereignItems.BUILD_FARM_ITEM);
                entries.add(SovereignItems.BUILD_MINE_ITEM);
                entries.add(SovereignItems.BUILD_RESIDENCE_ITEM);
                entries.add(SovereignItems.BUILD_TEMPLE_ITEM);
                entries.add(SovereignItems.BUILD_BARRACKS_ITEM);
                entries.add(SovereignItems.BUILD_CAMP_ITEM);
                entries.add(SovereignBlocks.CORNERSTONE_BLOCK);
                entries.add(SovereignItems.RESIDENT_PAPERS_ITEM);
                entries.add(SovereignItems.CONSCRIPT_PAPERS_ITEM);
                entries.add(SovereignItems.PAPERS_PLEASE_ITEM);
                entries.add(SovereignItems.CORNERSTONE_SHARD_ITEM);
                entries.add(SovereignItems.UNRAVELLED_SHARD_ITEM);
                entries.add(SovereignItems.SILK_BALL_ITEM);
                entries.add(SovereignItems.ROASTED_CORPSE);
                entries.add(SovereignItems.EMPTY_CAN);
                entries.add(SovereignItems.CORPSE_STARCH);
                // ... keep adding new items here
            })
            .build();

    // (should be called in mod init)
    public static void register() {
        // Items
        registerItem("empty_notebook", SovereignItems.EMPTY_NOTEBOOK_ITEM);
        registerItem("world_codex", SovereignItems.WORLD_CODEX_ITEM);
        registerItem("anarchism_book", SovereignItems.ANARCHISM_BOOK);
        registerItem("communalism_book", SovereignItems.COMMUNALISM_BOOK);
        registerItem("feudalism_book", SovereignItems.FEUDALISM_BOOK);
        registerItem("oligarchism_book", SovereignItems.OLIGARCHISM_BOOK);
        registerItem("totalitarianism_book", SovereignItems.TOTALITARIANISM_BOOK);
        registerItem("build_capital", SovereignItems.BUILD_CAPITAL_ITEM);
        registerItem("build_farm", SovereignItems.BUILD_FARM_ITEM);
        registerItem("build_mine", SovereignItems.BUILD_MINE_ITEM);
        registerItem("build_residence", SovereignItems.BUILD_RESIDENCE_ITEM);
        registerItem("build_temple", SovereignItems.BUILD_TEMPLE_ITEM);
        registerItem("build_barracks", SovereignItems.BUILD_BARRACKS_ITEM);
        registerItem("build_camp", SovereignItems.BUILD_CAMP_ITEM);
        registerItem("resident_papers", SovereignItems.RESIDENT_PAPERS_ITEM);
        registerItem("conscript_papers", SovereignItems.CONSCRIPT_PAPERS_ITEM);
        registerItem("papers_please", SovereignItems.PAPERS_PLEASE_ITEM);
        registerItem("cornerstone_shard", SovereignItems.CORNERSTONE_SHARD_ITEM);
        registerItem("silk_ball", SovereignItems.SILK_BALL_ITEM);
        registerItem("pig_iron_ingot", SovereignItems.PIG_IRON_INGOT_ITEM);
        registerItem("roasted_corpse", SovereignItems.ROASTED_CORPSE);
        registerItem("empty_can", SovereignItems.EMPTY_CAN);
        registerItem("corpse_starch", SovereignItems.CORPSE_STARCH);
        registerItem("unravelled_shard", SovereignItems.UNRAVELLED_SHARD_ITEM);
        // Blocks
        registerBlock("cornerstone", SovereignBlocks.CORNERSTONE_BLOCK);
        // Groups
        registerGroup("sovereign", SOVEREIGN_GROUP);
        // Events
        CornerstoneRemoveEvent.register();
        CitizenAssignmentEvent.register();
        CitizenKilledEvent.register();
        RenameEntityEvent.register();
        // Entities
        registerAttribute(SovereignEntities.ANARCHIST_INFANTRY,
                VillagerInfantryEntity.Anarchist.createAttributes());
        registerAttribute(SovereignEntities.COMMUNALIST_INFANTRY,
                VillagerInfantryEntity.Communalist.createAttributes());
        registerAttribute(SovereignEntities.FEUDAL_INFANTRY,
                VillagerInfantryEntity.Feudal.createAttributes());
        registerAttribute(SovereignEntities.OLIGARCHIC_INFANTRY,
                VillagerInfantryEntity.Oligarchic.createAttributes());
        registerAttribute(SovereignEntities.TOTALITARIAN_INFANTRY,
                VillagerInfantryEntity.Totalitarian.createAttributes());
        // Sounds
        registerSound(SovereignSounds.CITIZEN_ASSIGN);
    }

    private static void registerItem(String path, Item item) {
        Registry.register(Registries.ITEM, new Identifier(Sovereign.ID, path), item);
    }

    private static void registerBlock(String path, Block block) {
        Registry.register(Registries.BLOCK, new Identifier(Sovereign.ID, path), block);
        Registry.register(Registries.ITEM, new Identifier(Sovereign.ID, path),
                new BlockItem(block, new Item.Settings()));
    }

    private static void registerGroup(String path, ItemGroup group) {
        Registry.register(Registries.ITEM_GROUP, new Identifier(Sovereign.ID, path), group);
    }

    private static void registerSound(SoundEvent sound) {
        Registry.register(Registries.SOUND_EVENT, sound.getId(), sound);
    }

    private static <T extends LivingEntity> void registerAttribute(
            EntityType<T> type, DefaultAttributeContainer.Builder builder) {
        FabricDefaultAttributeRegistry.register(type, builder);
    }

}