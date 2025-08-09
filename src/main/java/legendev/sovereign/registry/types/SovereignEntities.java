package legendev.sovereign.registry.types;

import legendev.sovereign.Sovereign;
import legendev.sovereign.entity.VillagerInfantryEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class SovereignEntities {

    private SovereignEntities() {
    }

    // Basic Entities
    // ... nothing here

    // Villager Infantry Units
    public static EntityType<VillagerInfantryEntity.Anarchist> ANARCHIST_INFANTRY
            = createInfantryType("anarchist_infantry", VillagerInfantryEntity.Anarchist::new);
    public static EntityType<VillagerInfantryEntity.Communalist> COMMUNALIST_INFANTRY
            = createInfantryType("communalist_infantry", VillagerInfantryEntity.Communalist::new);
    public static EntityType<VillagerInfantryEntity.Feudal> FEUDAL_INFANTRY
            = createInfantryType("feudal_infantry", VillagerInfantryEntity.Feudal::new);
    public static EntityType<VillagerInfantryEntity.Oligarchic> OLIGARCHIC_INFANTRY
            = createInfantryType("oligarchic_infantry", VillagerInfantryEntity.Oligarchic::new);
    public static EntityType<VillagerInfantryEntity.Totalitarian> TOTALITARIAN_INFANTRY
            = createInfantryType("totalitarian_infantry", VillagerInfantryEntity.Totalitarian::new);

    // the EntityFactory should be a constructor for the Entity class
    private static <T extends Entity> EntityType<T> createEntityType(String path, SpawnGroup spawnGroup,
                                                                     EntityType.EntityFactory<T> factory,
                                                                     float hitboxWidth, float hitboxHeight) {
        return Registry.register(
                Registries.ENTITY_TYPE,
                new Identifier(Sovereign.ID, path),
                EntityType.Builder.create(factory, spawnGroup).dimensions(hitboxWidth, hitboxHeight).build());
    }

    private static <T extends VillagerInfantryEntity> EntityType<T> createInfantryType(
            String path, EntityType.EntityFactory<T> factory) {
        return createEntityType(path, SpawnGroup.MISC, factory, 0.6f, 1.95f);
    }

}
