package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.core.item.IItemEnchantmentCondition;
import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResult;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

/**
 * The multitool - one item that mines everything a shovel, pickaxe, axe or hoe would.
 * <p>
 * It cannot extend {@code ConfigurableToolItem} any more. Durability is a data component fixed at
 * construction, and the multitool's durability is the tier's scaled by the configured
 * {@code multiToolModifier}, which used to be an overridden {@code getMaxDamage(stack)}. Since
 * {@code Properties#tool} writes the material's own durability, the scaling has to happen in the
 * material handed to it, so this builds its properties itself.
 */
public class MultiToolItem extends Item implements ITiered, IItemEnchantmentCondition {

    private static final float ATTACK_SPEED = -2.8F;

    private final ItemTierConfig tierHolder;

    public MultiToolItem(ItemTierConfig tier, Item.Properties builderIn) {
        super(builderIn.tool(scaledMaterial(tier), ToolsTags.Blocks.MINEABLE_MULTITOOL, attackDamage(tier), ATTACK_SPEED, 0.0F)
                // Properties#tool leaves a Weapon(2); the multitool has always cost one durability
                // per swing rather than two, which used to be an overridden hurtEnemy.
                .component(DataComponents.WEAPON, new Weapon(1)));
        this.tierHolder = tier;
    }

    /**
     * The tier's material with the multitool durability modifier already applied.
     */
    private static ToolMaterial scaledMaterial(ItemTierConfig tier) {
        ToolMaterial material = tier.material();
        return new ToolMaterial(material.incorrectBlocksForDrops(), Math.max(1, (int) (material.durability() * tier.getMultiToolModifier())), material.speed(), material.attackDamageBonus(), material.enchantmentValue(), material.repairItems());
    }

    private static float attackDamage(ItemTierConfig tier) {
        return tier.getAxeDamage() > tier.getDamage() ? tier.getAxeDamage() : tier.getDamage() + tier.getDamage();
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return this.tierHolder;
    }

    @Override
    public Optional<Boolean> assortedlib_canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return Optional.of(enchantment.canEnchant(new ItemStack(Items.IRON_SWORD)) ||
                enchantment.canEnchant(new ItemStack(Items.IRON_SHOVEL)) ||
                enchantment.canEnchant(new ItemStack(Items.IRON_PICKAXE)) ||
                enchantment.canEnchant(new ItemStack(Items.IRON_HOE)) ||
                enchantment.canEnchant(new ItemStack(Items.IRON_AXE)));
    }

    /**
     * {@code canAttackBlock} is gone. This is the same rule expressed against its replacement: a
     * creative player cannot break blocks with a multitool.
     */
    @Override
    public boolean canDestroyBlock(ItemStack stack, BlockState state, Level level, BlockPos pos, LivingEntity user) {
        return !(user instanceof Player player && player.getAbilities().instabuild);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(Blocks.COBWEB)) {
            return 15.0F;
        }

        boolean validBlock = state.is(BlockTags.MINEABLE_WITH_SHOVEL) ||
                state.is(BlockTags.MINEABLE_WITH_PICKAXE) ||
                state.is(BlockTags.MINEABLE_WITH_AXE) ||
                state.is(BlockTags.MINEABLE_WITH_HOE);

        return validBlock ? this.getTierHolder().getEfficiency() : state.is(BlockTags.SWORD_EFFICIENT) ? 1.5F : super.getDestroySpeed(stack, state);
    }

    /**
     * Stripping, scraping, waxing off, path making, dousing a campfire and tilling - whichever of
     * them the clicked block accepts, in the order a player would expect.
     * <p>
     * This used to be two whole reimplementations of vanilla's behaviour, one per loader: a Fabric
     * mixin that read {@code STRIPPABLES} / {@code FLATTENABLES} / {@code TILLABLES} out of vanilla
     * through accessor mixins, and a NeoForge mixin built on {@code ToolActions}, which no longer
     * exists. Both are deleted.
     * <p>
     * Vanilla's own {@code useOn} implementations read nothing from the item they are called on -
     * they act on the context and damage {@code context.getItemInHand()}, which here is the
     * multitool. So the tools can simply be asked in turn, and the behaviour stays correct as
     * vanilla changes rather than drifting from a copy.
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        for (Item tool : List.of(Items.IRON_AXE, Items.IRON_SHOVEL, Items.IRON_HOE)) {
            InteractionResult result = tool.useOn(context);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }

        return super.useOn(context);
    }
}
