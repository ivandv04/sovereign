package legendev.sovereign.aitask;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import legendev.sovereign.Sovereign;
import legendev.sovereign.registry.BrainObjectRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestTypes;

import java.util.Optional;

public class FindTempleTask extends MultiTickTask<VillagerEntity> {


    public FindTempleTask() {
        super(ImmutableMap.of(BrainObjectRegistry.MOSQUE_POINT, MemoryModuleState.VALUE_ABSENT));
    }

    @Override
    protected boolean shouldRun(ServerWorld world, VillagerEntity entity) {
        return world.getPointOfInterestStorage().getNearestPosition(
                        p -> p.matchesKey(BrainObjectRegistry.HOLY_BLOCK),
                        entity.getBlockPos(), 48, PointOfInterestStorage.OccupationStatus.ANY).isPresent();
    }

    @Override
    protected void run(ServerWorld world, VillagerEntity entity, long time) {
        world.getPointOfInterestStorage().getPositions(p ->
                p.matchesKey(BrainObjectRegistry.HOLY_BLOCK), blockPos -> {
            Path path = entity.getNavigation().findPathTo(blockPos,
                    BrainObjectRegistry.getPoi(BrainObjectRegistry.HOLY_BLOCK).searchDistance());
            return path != null && path.reachesTarget();
        }, entity.getBlockPos(), 48, PointOfInterestStorage.OccupationStatus.ANY).findAny().ifPresent(
                blockPos -> entity.getBrain().remember(
                        BrainObjectRegistry.MOSQUE_POINT, GlobalPos.create(world.getRegistryKey(), blockPos)));

    }


    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>>
    createCoreTasks(VillagerProfession profession, float speed) {

        return ImmutableList.of(
                Pair.of(0, new StayAboveWaterTask(0.8f)),
                Pair.of(0, OpenDoorsTask.create()),
                Pair.of(0, new LookAroundTask(45, 90)),
                Pair.of(0, new PanicTask()),
                Pair.of(0, WakeUpTask.create()),
                Pair.of(0, HideWhenBellRingsTask.create()),
                Pair.of(0, StartRaidTask.create()),
                Pair.of(0, ForgetCompletedPointOfInterestTask.create(
                        profession.heldWorkstation(), MemoryModuleType.JOB_SITE)),
                Pair.of(0, ForgetCompletedPointOfInterestTask.create(
                        profession.acquirableWorkstation(), MemoryModuleType.POTENTIAL_JOB_SITE)),
                Pair.of(1, new WanderAroundTask()), Pair.of(2, WorkStationCompetitionTask.create()),
                Pair.of(3, new FollowCustomerTask(speed)),
                new Pair[]{
                        Pair.of(5, WalkToNearestVisibleWantedItemTask.create(speed, false, 4)),
                        Pair.of(6, FindPointOfInterestTask.create(
                                profession.acquirableWorkstation(),
                                MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE,
                                true, Optional.empty())),
                        Pair.of(7, new WalkTowardJobSiteTask(speed)),
                        Pair.of(8, TakeJobSiteTask.create(speed)),
                        Pair.of(10, FindPointOfInterestTask.create(
                                poiType -> poiType.matchesKey(PointOfInterestTypes.HOME),
                                MemoryModuleType.HOME, false, Optional.of((byte) 14))),
                        Pair.of(10, FindPointOfInterestTask.create(
                                poiType -> poiType.matchesKey(PointOfInterestTypes.MEETING),
                                MemoryModuleType.MEETING_POINT, true, Optional.of((byte) 14))),
                        Pair.of(10, GoToWorkTask.create()),
                        Pair.of(10, LoseJobOnSiteLossTask.create())
                });
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>>
    createWorkTasks(VillagerProfession profession, float speed) {
        VillagerWorkTask villagerWorkTask =
                profession == VillagerProfession.FARMER ? new FarmerWorkTask() : new VillagerWorkTask();
        return ImmutableList.of(
                //VillagerTaskListProvider.createBusyFollowTask(),
                Pair.of(5, new RandomTask(ImmutableList.of(
                        Pair.of(villagerWorkTask, 7),
                        Pair.of(GoToIfNearbyTask.create(MemoryModuleType.JOB_SITE, 0.4f, 4), 2),
                        Pair.of(GoToNearbyPositionTask.create(MemoryModuleType.JOB_SITE, 0.4f, 1, 10), 5),
                        Pair.of(GoToSecondaryPositionTask.create(MemoryModuleType.SECONDARY_JOB_SITE, speed, 1, 6, MemoryModuleType.JOB_SITE), 5),
                        Pair.of(new FarmerVillagerTask(), profession == VillagerProfession.FARMER ? 2 : 5),
                        Pair.of(new BoneMealTask(), profession == VillagerProfession.FARMER ? 4 : 7)))),
                Pair.of(10, new HoldTradeOffersTask(400, 1600)),
                Pair.of(10, FindInteractionTargetTask.create(EntityType.PLAYER, 4)),
                Pair.of(2, VillagerWalkTowardsTask.create(MemoryModuleType.JOB_SITE, speed, 9, 100, 1200)),
                Pair.of(3, new GiveGiftsToHeroTask(100)),
                Pair.of(99, ScheduleActivityTask.create()));
    }

}
