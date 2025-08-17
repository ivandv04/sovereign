package legendev.sovereign.mixin.accessor;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.function.BiPredicate;

@Mixin(VillagerEntity.class)
public interface VillagerEntityAccessor {

    @Mutable
    @Accessor("POINTS_OF_INTEREST")
    static void setPointsOfInterest(Map<MemoryModuleType<GlobalPos>,
            BiPredicate<VillagerEntity, RegistryEntry<PointOfInterestType>>> poi) {
        throw new AssertionError();
    }

}
