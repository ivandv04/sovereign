package legendev.sovereign.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import legendev.sovereign.Sovereign;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;

import java.util.Optional;
import java.util.Set;

public final class BrainObjectRegistry {

    private BrainObjectRegistry() {
    }

    // Activities
    public static final Activity PRAY = registerActivity("pray");

    // Mem Mod Types
    public static final MemoryModuleType<GlobalPos> MOSQUE_POINT
            = registerMemoryModType("mosque_point", GlobalPos.CODEC);

    // Poi Types
    // These need to be key references, not normal objects
    public static final RegistryKey<PointOfInterestType> HOLY_BLOCK = key("holy_block");


    public static void registerPointsOfInterest() {
    }

    private static RegistryKey<PointOfInterestType> key(String path) {
        return RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, new Identifier(path));
    }

    public static PointOfInterestType getPoi(RegistryKey<PointOfInterestType> key) {
        return Registries.POINT_OF_INTEREST_TYPE.get(key);
    }

    public static Set<BlockState> getStatesOfBlock(Block block) {
        return ImmutableSet.copyOf(block.getStateManager().getStates());
    }

    private static Activity registerActivity(String path) {
        return Registry.register(Registries.ACTIVITY, new Identifier(path), new Activity(path));
    }

    private static <T> MemoryModuleType<T> registerMemoryModType(String path, Codec<T> codec) {
        return Registry.register(Registries.MEMORY_MODULE_TYPE,
                new Identifier(path), new MemoryModuleType<>(Optional.of(codec)));
    }

    private static <T> MemoryModuleType<T> registerMemoryModType(String path) {
        return Registry.register(Registries.MEMORY_MODULE_TYPE,
                new Identifier(path), new MemoryModuleType<>(Optional.empty()));
    }

    private static void registerPoiType(RegistryKey<PointOfInterestType> path, Set<BlockState> states,
                                        int ticketCount, int searchDistance) {
        PointOfInterestType pointOfInterestType = new PointOfInterestType(states, ticketCount, searchDistance);
        Registry.register(Registries.POINT_OF_INTEREST_TYPE, path, pointOfInterestType);
    }

}
