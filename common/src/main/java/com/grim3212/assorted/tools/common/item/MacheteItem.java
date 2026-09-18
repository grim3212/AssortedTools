package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableTools;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

/**
 * A sword that clears plants: it hits a little softer and swings a little faster than a sword of its
 * material, and cuts everything in {@code #assortedtools:mineable/machete} at the material's
 * speed. Otherwise the tool rules are a sword's, cobweb included.
 */
public class MacheteItem extends Item implements ITiered {

    public static final float ATTACK_DAMAGE = 2.2F;
    public static final float ATTACK_SPEED = -2.15F;

    private final ItemTierConfig tierHolder;

    public MacheteItem(ItemTierConfig tierHolder, Properties properties) {
        super(machete(tierHolder.material(), ConfigurableTools.tiered(tierHolder, properties)));
        this.tierHolder = tierHolder;
    }

    /** {@code ToolMaterial#applySwordProperties} with one more rule, for the plants. */
    private static Properties machete(ToolMaterial material, Properties properties) {
        HolderGetter<Block> blocks = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
        Tool tool = new Tool(List.of(
                Tool.Rule.minesAndDrops(HolderSet.direct(Blocks.COBWEB.builtInRegistryHolder()), 15.0F),
                Tool.Rule.overrideSpeed(blocks.getOrThrow(BlockTags.SWORD_INSTANTLY_MINES), Float.MAX_VALUE),
                Tool.Rule.overrideSpeed(blocks.getOrThrow(ToolsTags.Blocks.MINEABLE_MACHETE), Math.max(material.speed(), 1.5F))),
                1.0F, 1, false);

        ItemAttributeModifiers attributes = ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, ATTACK_DAMAGE + material.attackDamageBonus(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, ATTACK_SPEED, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();

        return properties.component(DataComponents.TOOL, tool).attributes(attributes).component(DataComponents.WEAPON, new Weapon(1));
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return this.tierHolder;
    }
}
