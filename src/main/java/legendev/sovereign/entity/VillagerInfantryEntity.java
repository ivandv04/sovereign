package legendev.sovereign.entity;

import legendev.sovereign.factiondata.FactionIdeology;
import legendev.sovereign.registry.types.SovereignEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class VillagerInfantryEntity extends PathAwareEntity implements GeoEntity {

    public static final String ID = "villager_infantry";

    public static final String DEFAULT_HP = "1.00 ";

    private static final RawAnimation WALK_ANIM
            = RawAnimation.begin().thenLoop("animation.villager_infantry.walk_generic");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    protected FactionIdeology ideology;

    protected VillagerInfantryEntity(EntityType<? extends PathAwareEntity> entityType,
                                     World world, FactionIdeology ideology) {
        super(entityType, world);
        this.ideology = ideology;
    }

    public static @NotNull VillagerInfantryEntity createFromIdeology(@NotNull FactionIdeology ideology,
                                                                     World inWorld, String name) {
        VillagerInfantryEntity entity = switch (ideology) {
            case ANARCHISM -> new Anarchist(SovereignEntities.ANARCHIST_INFANTRY, inWorld);
            case COMMUNALISM -> new Communalist(SovereignEntities.COMMUNALIST_INFANTRY, inWorld);
            case FEUDALISM -> new Feudal(SovereignEntities.FEUDAL_INFANTRY, inWorld);
            case OLIGARCHISM -> new Communalist(SovereignEntities.OLIGARCHIC_INFANTRY, inWorld);
            case TOTALITARIANISM -> new Totalitarian(SovereignEntities.TOTALITARIAN_INFANTRY, inWorld);
        };
        entity.setCustomName(Text.literal(name));
        return entity;
    }

    protected static DefaultAttributeContainer.Builder createGenericAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 75d)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2d)
                .add(EntityAttributes.GENERIC_ARMOR, 10d)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5);
    }

    protected static DefaultAttributeContainer.Builder createStrongAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 125d)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2d)
                .add(EntityAttributes.GENERIC_ARMOR, 15d)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1d));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 4f));
        this.goalSelector.add(6, new LookAroundGoal(this));
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean ret = super.damage(source, amount);
        updateHealthBar();
        return ret;
    }

    @Override
    public void heal(float amount) {
        super.heal(amount);
        updateHealthBar();
    }

    private void updateHealthBar() {
        if (getCustomName() == null) return;
        String get = getCustomName().getString();
        float hp = getHealth() / getMaxHealth();
        setCustomName(Text.literal(String.format("%.2f", hp) + get.substring(4)));
    }

    public String getNameWithoutPrefix() {
        if (getCustomName() == null) return null;
        return getCustomName().getString().substring(7);
    }

    public String idFull() {
        return ID + "_" + ideology.adj.toLowerCase();
    }

    public boolean canBeStowed() {
        return getHealth() == getMaxHealth();
    }

    @Override
    public void registerControllers(AnimatableManager.@NotNull ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "walking", 5, (event) -> {
            if (event.isMoving()) return event.setAndContinue(WALK_ANIM);
            return PlayState.STOP;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public float getSoundPitch() {
        return super.getSoundPitch() * 0.75f;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    /**
     * Villager Infantry units for Anarchist Factions.
     */
    public static class Anarchist extends VillagerInfantryEntity {

        public Anarchist(EntityType<? extends PathAwareEntity> entityType, World world) {
            super(entityType, world, FactionIdeology.ANARCHISM);
        }

        public static DefaultAttributeContainer.Builder createAttributes() {
            return VillagerInfantryEntity.createStrongAttributes();
        }
    }

    /**
     * Villager Infantry units for Communalist Factions.
     */
    public static class Communalist extends VillagerInfantryEntity {

        public Communalist(EntityType<? extends PathAwareEntity> entityType, World world) {
            super(entityType, world, FactionIdeology.COMMUNALISM);
        }

        public static DefaultAttributeContainer.Builder createAttributes() {
            return VillagerInfantryEntity.createGenericAttributes();
        }
    }

    /**
     * Villager Infantry units for Feudal Factions.
     */
    public static class Feudal extends VillagerInfantryEntity {

        public Feudal(EntityType<? extends PathAwareEntity> entityType, World world) {
            super(entityType, world, FactionIdeology.FEUDALISM);
        }

        public static DefaultAttributeContainer.Builder createAttributes() {
            return VillagerInfantryEntity.createGenericAttributes();
        }
    }

    /**
     * Villager Infantry units for Oligarchic Factions.
     */
    public static class Oligarchic extends VillagerInfantryEntity {

        public Oligarchic(EntityType<? extends PathAwareEntity> entityType, World world) {
            super(entityType, world, FactionIdeology.OLIGARCHISM);
        }

        public static DefaultAttributeContainer.Builder createAttributes() {
            return VillagerInfantryEntity.createGenericAttributes();
        }
    }

    /**
     * Villager Infantry units for Totalitarian Factions.
     */
    public static class Totalitarian extends VillagerInfantryEntity {

        public Totalitarian(EntityType<? extends PathAwareEntity> entityType, World world) {
            super(entityType, world, FactionIdeology.TOTALITARIANISM);
        }

        public static DefaultAttributeContainer.Builder createAttributes() {
            return VillagerInfantryEntity.createStrongAttributes();
        }
    }

}
