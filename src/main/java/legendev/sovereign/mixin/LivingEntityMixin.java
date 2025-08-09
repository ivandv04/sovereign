package legendev.sovereign.mixin;

import legendev.sovereign.callback.LivingEntityDeathCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @SuppressWarnings("UnreachableCode")
    @Inject(at = @At("HEAD"), method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V", cancellable = true)
    private void onDeath(@NotNull DamageSource damageSource, CallbackInfo info) {
        LivingEntity source = (LivingEntity) (Object) this;
        if (source != null) {
            ActionResult result = LivingEntityDeathCallback.EVENT.invoker()
                    .onEntityDeath(source, damageSource.getAttacker(), source.getWorld());
            // Cancel the death if FAIL result given
            if (result == ActionResult.FAIL) info.cancel();
        }
    }

}