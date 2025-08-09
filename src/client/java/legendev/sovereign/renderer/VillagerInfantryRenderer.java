package legendev.sovereign.renderer;

import legendev.sovereign.entity.VillagerInfantryEntity;
import legendev.sovereign.geomodel.VillagerInfantryModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VillagerInfantryRenderer extends GeoEntityRenderer<VillagerInfantryEntity> {

    public VillagerInfantryRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new VillagerInfantryModel());
    }
}
