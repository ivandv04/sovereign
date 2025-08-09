package legendev.sovereign;

import legendev.sovereign.registry.SovereignRegistry;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sovereign implements ModInitializer {

    public static final String ID = "sovereign";

    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    @Override
    public void onInitialize() {
        SovereignRegistry.register();
        LOGGER.info("Finished Fabric mod setup");
    }

}