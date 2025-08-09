package legendev.sovereign;

import legendev.sovereign.network.NetworkManager;
import legendev.sovereign.registry.types.SovereignEntities;
import legendev.sovereign.renderer.VillagerInfantryRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class SovereignClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Initialize network packets
        NetworkManager.registerC2S();
        NetworkManager.registerS2C();
        // Initialize entity renderers
        EntityRendererRegistry.register(SovereignEntities.ANARCHIST_INFANTRY, VillagerInfantryRenderer::new);
        EntityRendererRegistry.register(SovereignEntities.COMMUNALIST_INFANTRY, VillagerInfantryRenderer::new);
        EntityRendererRegistry.register(SovereignEntities.FEUDAL_INFANTRY, VillagerInfantryRenderer::new);
        EntityRendererRegistry.register(SovereignEntities.OLIGARCHIC_INFANTRY, VillagerInfantryRenderer::new);
        EntityRendererRegistry.register(SovereignEntities.TOTALITARIAN_INFANTRY, VillagerInfantryRenderer::new);
    }
}