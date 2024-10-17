package tfar.keepbedinventory;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class KeepBedInventory {

    public static final String MOD_ID = "keepbedinventory";
    public static final String MOD_NAME = "KeepBedInventory";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final String BED_BOUND = "bedbound";

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {

    }

    public static void onSetSpawn(ServerPlayer player, BlockPos newSpawn) {
        if (newSpawn != null) {
            Inventory inventory = player.getInventory();
            final List<NonNullList<ItemStack>> compartments = ImmutableList.of(inventory.items, inventory.armor, inventory.offhand);
            long time = player.level().getGameTime();
            ((ServerPlayerDuck)player).setLastValidTimestamp(time);
            for (List<ItemStack> list : compartments) {
                for (int i = 0; i < list.size(); i++) {
                    ItemStack itemstack = list.get(i);
                    if (!itemstack.isEmpty()) {
                        CustomData customData = itemstack.get(DataComponents.CUSTOM_DATA);
                        CompoundTag tag;
                        if (customData == null) {
                            tag = new CompoundTag();
                        } else {
                            tag = customData.getUnsafe();
                        }
                        tag.putLong(BED_BOUND, time);
                        itemstack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                    }
                }
            }
        }
    }


    public static void saveBedItems(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.getRespawnPosition() != null) {
                Inventory inventory = player.getInventory();
                SavedInventory savedInventory = ((ServerPlayerDuck) player).getSavedInventory();
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    ItemStack stack = inventory.getItem(i);
                    if (!stack.isEmpty()) {
                        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
                        if (customData != null) {
                            CompoundTag tag = customData.getUnsafe();
                            if (tag.contains(BED_BOUND)) {
                                long itemTimeStamp = tag.getLong(BED_BOUND);
                                if (itemTimeStamp == ((ServerPlayerDuck) player).getLastValidTimestamp()) {
                                    savedInventory.setItem(i, stack);
                                    inventory.setItem(i, ItemStack.EMPTY);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void clone(ServerPlayer oldPlayer,ServerPlayer newPlayer,boolean wasDeath) {
        Inventory newInventory = newPlayer.getInventory();
        SavedInventory oldSavedInventory = ((ServerPlayerDuck)oldPlayer).getSavedInventory();
        for (int i = 0; i < newInventory.getContainerSize(); i++) {
            ItemStack stack = newInventory.getItem(i);
            if (stack.isEmpty()) {
                ItemStack savedStack = oldSavedInventory.getItem(i);
                if (!savedStack.isEmpty()) {
                    newInventory.setItem(i,savedStack);
                }
            }
        }
        oldSavedInventory.clearContent();
        ((ServerPlayerDuck)newPlayer).setLastValidTimestamp(((ServerPlayerDuck)oldPlayer).getLastValidTimestamp());
    }
}