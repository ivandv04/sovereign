package legendev.sovereign.mixin;

import com.google.common.collect.ImmutableSet;
import legendev.sovereign.registry.BrainObjectRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registry;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(PointOfInterestTypes.class)
public class PointOfInterestTypesMixin {

    @Inject(at = @At("HEAD"),
            method= "registerAndGetDefault(Lnet/minecraft/registry/Registry;)" +
                    "Lnet/minecraft/world/poi/PointOfInterestType;")
    private static void registerAndGetDefault(
            Registry<PointOfInterestType> registry, CallbackInfoReturnable<PointOfInterestType> cir) {
        PointOfInterestTypes.register(registry, BrainObjectRegistry.HOLY_BLOCK,
                PointOfInterestTypes.getStatesOfBlock(Blocks.EMERALD_BLOCK), 32, 1);

    }

}
