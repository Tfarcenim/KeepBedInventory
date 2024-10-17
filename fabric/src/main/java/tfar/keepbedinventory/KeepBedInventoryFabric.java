package tfar.keepbedinventory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.level.ServerPlayer;

public class KeepBedInventoryFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.
        EntitySleepEvents.ALLOW_SETTING_SPAWN.register((player, sleepingPos) -> {
            if (player instanceof ServerPlayer serverPlayer) {
                KeepBedInventory.onSetSpawn(serverPlayer, sleepingPos);
            }
            return true;
        });

        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> KeepBedInventory.clone(oldPlayer,newPlayer,!alive));
        // Use Fabric to bootstrap the Common mod.
        KeepBedInventory.init();
    }
}
