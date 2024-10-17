package tfar.keepbedinventory;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;

@Mod(KeepBedInventory.MOD_ID)
public class KeepBedInventoryNeoForge {

    public KeepBedInventoryNeoForge(IEventBus eventBus) {
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.

        KeepBedInventory.init();
        NeoForge.EVENT_BUS.addListener(EventPriority.LOW, this::spawnSet);
        NeoForge.EVENT_BUS.addListener(this::playerClone);
    }


    void spawnSet(PlayerSetSpawnEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        BlockPos newSpawn = event.getNewSpawn();
        KeepBedInventory.onSetSpawn(player,newSpawn);
    }

    void playerClone(PlayerEvent.Clone event) {
        KeepBedInventory.clone((ServerPlayer) event.getOriginal(), (ServerPlayer) event.getEntity(),event.isWasDeath());
    }
}