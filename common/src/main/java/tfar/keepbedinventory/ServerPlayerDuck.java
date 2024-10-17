package tfar.keepbedinventory;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface ServerPlayerDuck {
    long getLastValidTimestamp();
    void setLastValidTimestamp(long timestamp);

    SavedInventory getSavedInventory();
}
