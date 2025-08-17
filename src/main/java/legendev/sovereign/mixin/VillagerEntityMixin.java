package legendev.sovereign.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import legendev.sovereign.aitask.FindTempleTask;
import legendev.sovereign.registry.BrainObjectRegistry;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {

    @Inject(at = @At("HEAD"), method = "initBrain(Lnet/minecraft/entity/ai/brain/Brain;)V")
    private void initBrain(Brain<VillagerEntity> brain, CallbackInfo info) {
        VillagerEntity v = (VillagerEntity) (Object) this;

        if (!v.isBaby()) {
            brain.setTaskList(
                    BrainObjectRegistry.PRAY,
                    createPrayerTasks()
            );
        }
    }

    @Unique
    private static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> createPrayerTasks() {
        return ImmutableList.of(

                Pair.of(1, FindPointOfInterestTask.create(
                        poiType -> poiType.matchesKey(BrainObjectRegistry.HOLY_BLOCK),
                        BrainObjectRegistry.MOSQUE_POINT, false, Optional.of((byte) 14))),

                Pair.of(2, VillagerWalkTowardsTask.create(
                        BrainObjectRegistry.MOSQUE_POINT, 0.5f, 1, 150, 1200)),
                Pair.of(3, ForgetCompletedPointOfInterestTask.create(
                        poiType -> poiType.matchesKey(BrainObjectRegistry.HOLY_BLOCK),
                        BrainObjectRegistry.MOSQUE_POINT)),
                /*

                //Pair.of(5, WalkHomeTask.create(0.5f)),


                Pair.of(2, VillagerWalkTowardsTask.create(
                        MemoryModuleType.HOME, 0.5f, 1, 150, 1200)),
                Pair.of(3, ForgetCompletedPointOfInterestTask.create(
                        poiType -> poiType.matchesKey(PointOfInterestTypes.HOME),
                        MemoryModuleType.HOME)),

                /*
                Pair.of(1, new FindTempleTask()),
                Pair.of(2, VillagerWalkTowardsTask.create(BrainObjectRegistry.MOSQUE_POINT,
                        0.75f, 1, 150, 1200)),
                Pair.of(3, ForgetCompletedPointOfInterestTask.create(
                        p -> p.matchesKey(BrainObjectRegistry.HOLY_BLOCK),
                        BrainObjectRegistry.MOSQUE_POINT)),

                 */
                Pair.of(99, ScheduleActivityTask.create())
        );
    }

    // Shadow the entire module list since accessor doesn't work
    @Shadow
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(
            // New Modules
            BrainObjectRegistry.MOSQUE_POINT,
            // Vanilla Modules (do NOT change)
            MemoryModuleType.HOME, MemoryModuleType.JOB_SITE,
            MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleType.MEETING_POINT,
            MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS,
            MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleType.NEAREST_PLAYERS,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS,
            MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.INTERACTION_TARGET,
            MemoryModuleType.BREED_TARGET, MemoryModuleType.PATH, MemoryModuleType.DOORS_TO_CLOSE,
            MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.SECONDARY_JOB_SITE,
            MemoryModuleType.HIDING_PLACE, MemoryModuleType.HEARD_BELL_TIME,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LAST_SLEPT,
            MemoryModuleType.LAST_WOKEN, MemoryModuleType.LAST_WORKED_AT_POI,
            MemoryModuleType.GOLEM_DETECTED_RECENTLY);

    @Shadow
    public static final Map<MemoryModuleType<GlobalPos>,
            BiPredicate<VillagerEntity, RegistryEntry<PointOfInterestType>>>
            POINTS_OF_INTEREST = ImmutableMap.of(
            // New Types
            BrainObjectRegistry.MOSQUE_POINT, (v,r)
            -> r.matchesKey(BrainObjectRegistry.HOLY_BLOCK),
            // Vanilla Types (do NOT change)
            MemoryModuleType.HOME, (v,r)
                    -> r.matchesKey(PointOfInterestTypes.HOME),
            MemoryModuleType.JOB_SITE, (v,r) 
                    -> v.getVillagerData().getProfession().heldWorkstation().test(r),
            MemoryModuleType.POTENTIAL_JOB_SITE, (v,r) 
                    -> VillagerProfession.IS_ACQUIRABLE_JOB_SITE.test(r),
            MemoryModuleType.MEETING_POINT, (v,r) 
                    -> r.matchesKey(PointOfInterestTypes.MEETING));

}