package legendev.sovereign.geomodel;

import legendev.sovereign.Sovereign;
import legendev.sovereign.entity.VillagerInfantryEntity;
import legendev.sovereign.util.GeoUtil;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class VillagerInfantryModel extends GeoModel<VillagerInfantryEntity> {

    private static final Identifier MODEL = GeoUtil.getGeoModel(VillagerInfantryEntity.ID);
    private static final Identifier ANIM = GeoUtil.getAnimation(VillagerInfantryEntity.ID);

    @Override
    public Identifier getModelResource(VillagerInfantryEntity animatable) {
        return MODEL;
    }

    @Override
    public Identifier getTextureResource(@NotNull VillagerInfantryEntity animatable) {
        return new Identifier(Sovereign.ID, "textures/entity/villager_infantry/"
                + animatable.idFull() + ".png");
    }

    @Override
    public Identifier getAnimationResource(VillagerInfantryEntity animatable) {
        return ANIM;
    }

    @Override
    public void setCustomAnimations(VillagerInfantryEntity animatable, long instanceId,
                                    @NotNull AnimationState<VillagerInfantryEntity> animationState) {
        GeoBone head = this.getBone("head").orElseThrow();
        EntityModelData data = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        head.setRotX((float) Math.toRadians(data.headPitch()));
        head.setRotY((float) Math.toRadians(data.netHeadYaw()));
    }

}