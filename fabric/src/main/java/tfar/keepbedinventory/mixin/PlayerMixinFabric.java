package tfar.keepbedinventory.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.keepbedinventory.KeepBedInventory;

@Mixin(Player.class)
public class PlayerMixinFabric {

    @Inject(at = @At("RETURN"), method = "getBaseExperienceReward", cancellable = true)
    private void init(CallbackInfoReturnable<Integer> cir) {
        Player player = (Player) (Object)this;
        if (player instanceof ServerPlayer serverPlayer) {
            if (!serverPlayer.serverLevel().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) && KeepBedInventory.isRespawnValid(serverPlayer)) {
                cir.setReturnValue(KeepBedInventory.getExperienceDropped(player,cir.getReturnValue()));
            }
        }
    }
}