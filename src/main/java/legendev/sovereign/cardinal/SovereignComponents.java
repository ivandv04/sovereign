package legendev.sovereign.cardinal;


import legendev.sovereign.Sovereign;
import legendev.sovereign.cardinal.component.RandComponent;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class SovereignComponents implements EntityComponentInitializer {

    // TEMP
    public static final ComponentKey<RandComponent> RANDOM
            = ComponentRegistry.getOrCreate(new Identifier(Sovereign.ID, "rand"), RandComponent.class);

    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        registry.registerFor(SheepEntity.class, RANDOM, sheepEntity -> new RandComponent()); // TEMP
    }
}
