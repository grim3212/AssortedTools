package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Shears made of a configured material. {@code ShearsItem} supplies {@code useOn} and the wool,
 * leaves and vine mining rules, but not shearing mobs; see {@link #interactLivingEntity}.
 */
public class MaterialShears extends ShearsItem implements ITiered {

    private final ItemTierConfig tierHolder;

    public MaterialShears(Properties props, ItemTierConfig tierHolder) {
        // The 0.952 factor is the durability ratio vanilla iron shears have to iron tools, kept so
        // every material's shears stay in the same proportion to its other tools.
        super(props.durability((int) Math.rint(tierHolder.getMaxUses() * 0.952F))
                .enchantable(tierHolder.getEnchantability())
                .component(DataComponents.TOOL, ShearsItem.createToolProperties()));
        this.tierHolder = tierHolder;

        DispenserBlock.registerBehavior(this, new ShearsDispenseItemBehavior());
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return this.tierHolder;
    }

    /**
     * Shears a sheep, mooshroom, snow golem or bogged. Every shearable mob tests {@code
     * is(Items.SHEARS)} in {@code mobInteract}, which runs after this. NeoForge's patched {@code
     * super} routes through {@code IShearable} and also covers modded mobs, so it goes first; on
     * Fabric it passes and the vanilla {@link Shearable} fallback runs.
     */
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        InteractionResult result = super.interactLivingEntity(stack, player, target, hand);
        if (result != InteractionResult.PASS) {
            return result;
        }

        if (!(target instanceof Shearable shearable)) {
            return InteractionResult.PASS;
        }

        if (!(target.level() instanceof ServerLevel level)) {
            return shearable.readyForShearing() ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }

        if (!shearable.readyForShearing()) {
            return InteractionResult.CONSUME;
        }

        shearable.shear(level, SoundSource.PLAYERS, stack);
        target.gameEvent(GameEvent.SHEAR, player);
        stack.hurtAndBreak(1, player, hand.asEquipmentSlot());

        return InteractionResult.SUCCESS_SERVER;
    }
}
