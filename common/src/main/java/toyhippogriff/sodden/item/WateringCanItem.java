package toyhippogriff.sodden.item;

import dev.architectury.hooks.level.entity.PlayerHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.features.NetherFeatures;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.GrowingPlantBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import toyhippogriff.sodden.Sodden;
import toyhippogriff.sodden.tags.SoddenTags;

import java.util.List;

public class WateringCanItem extends Item
{
    private final WateringCanTier tier;

    public WateringCanItem(WateringCanTier tier)
    {
        super(new Item.Properties().tab(SoddenItems.CREATIVE_TAB).durability(tier.getCapacity()));

        this.tier = tier;
    }

    public WateringCanTier getTier()
    {
        return tier;
    }

    @Override
    public ItemStack getDefaultInstance()
    {
        ItemStack itemStack = new ItemStack(this);

        itemStack.setDamageValue(itemStack.getMaxDamage());

        return itemStack;
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items)
    {
        if(!this.allowdedIn(tab))
        {
            return;
        }

        items.add(this.getDefaultInstance());
        items.add(new ItemStack(this));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag)
    {
        tooltip.add(new TranslatableComponent("tooltip.sodden.fill", itemStack.getMaxDamage() - itemStack.getDamageValue(), itemStack.getMaxDamage()));
    }

    @Override
    public int getBarColor(ItemStack itemStack)
    {
        return 0x4466FF;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack)
    {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack itemStack)
    {
        return 200;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack itemStack = player.getItemInHand(hand);

        if(!tryFill(level, player, itemStack))
        {
            return InteractionResultHolder.pass(itemStack);
        }

        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Player player = context.getPlayer();

        if(player == null)
        {
            return InteractionResult.FAIL;
        }

        InteractionHand hand = context.getHand();
        ItemStack itemStack = context.getItemInHand();
        BlockPos blockPos = context.getClickedPos();
        Direction direction = context.getClickedFace();

        if(!player.mayUseItemAt(blockPos.relative(direction), direction, itemStack))
        {
            return InteractionResult.FAIL;
        }

        if(itemStack.getDamageValue() + tier.getUsage() >= itemStack.getMaxDamage())
        {
            return InteractionResult.PASS;
        }

        player.startUsingItem(hand);

        return InteractionResult.PASS;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingTicks)
    {
        if(remainingTicks < 0)
        {
            entity.releaseUsingItem();
        }

        if(entity instanceof Player player)
        {
            BlockHitResult hitResult = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);

            if(hitResult.getType() != HitResult.Type.BLOCK)
            {
                player.releaseUsingItem();

                return;
            }

            BlockPos blockPos = hitResult.getBlockPos();
            Direction direction = hitResult.getDirection();

            if(this.tryWater(level, player, itemStack, blockPos, direction))
            {
                itemStack.setDamageValue(itemStack.getDamageValue() + tier.getUsage());
            }
        }
        else
        {
            entity.releaseUsingItem();
        }
    }

    public boolean tryFill(Level level, Player player, ItemStack itemStack)
    {
        if(!itemStack.isDamaged())
        {
            return false;
        }

        BlockHitResult hitResult = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if(hitResult.getType() != HitResult.Type.BLOCK)
        {
            return false;
        }

        BlockPos blockPos = hitResult.getBlockPos();
        Direction direction = hitResult.getDirection();

        if(!level.mayInteract(player, blockPos) || !player.mayUseItemAt(blockPos.relative(direction), direction, itemStack))
        {
            return false;
        }

        FluidState fluidState = level.getFluidState(blockPos);

        if(!fluidState.is(FluidTags.WATER) || !fluidState.isSource())
        {
            return false;
        }

        BlockState blockState = level.getBlockState(blockPos);

        if(blockState.getBlock() instanceof BucketPickup bucketPickup)
        {
            itemStack.setDamageValue(itemStack.getDamageValue() - tier.getFill());

            player.playSound(bucketPickup.getPickupSound().orElse(SoundEvents.BUCKET_FILL), 1.0F, 1.0F);

            if(Sodden.CONFIG.consumesWater)
            {
                bucketPickup.pickupBlock(level, blockPos, blockState);
            }

            return true;
        }

        return false;
    }

    public boolean tryWater(Level level, Player player, ItemStack itemStack, BlockPos centerBlockPos, Direction direction)
    {
        if(player == null)
        {
            return false;
        }

        if(!player.mayUseItemAt(centerBlockPos.relative(direction), direction, itemStack))
        {
            return false;
        }

        if(itemStack.getDamageValue() + tier.getUsage() > itemStack.getMaxDamage())
        {
            return false;
        }

        if(!Sodden.CONFIG.fakePlayersAllowed && PlayerHooks.isFake(player))
        {
            return false;
        }

        if(!level.isClientSide)
        {
            ItemCooldowns cooldowns = player.getCooldowns();
            Item item = itemStack.getItem();

            if(cooldowns.isOnCooldown(item))
            {
                return false;
            }

            cooldowns.addCooldown(item, Sodden.CONFIG.cooldown);

            ServerLevel serverLevel = (ServerLevel) level;

            BlockPos.betweenClosed(centerBlockPos.offset(-tier.getRadius(), -1, -tier.getRadius()), centerBlockPos.offset(tier.getRadius(), 1, tier.getRadius()))
                .forEach(blockPos ->
                {
                    BlockState blockState = level.getBlockState(blockPos);
                    Block block = blockState.getBlock();

                    this.moisturizeFarmland(serverLevel, blockPos, blockState);
                    this.applyGrowthTick(serverLevel, blockPos, blockState, block);
                    this.applyBonemeal(serverLevel, blockPos, blockState, block);
                    this.applyFlower(serverLevel, blockPos, blockState);
                    this.applyFungus(serverLevel, blockPos, blockState);
                    this.applySpreadTick(serverLevel, blockPos, blockState, block);
                });
        }

        BlockPos.betweenClosed(centerBlockPos.offset(-tier.getRadius(), 0, -tier.getRadius()), centerBlockPos.offset(tier.getRadius(), 0, tier.getRadius()))
            .forEach(blockPos ->
            {
                BlockState blockState = level.getBlockState(blockPos);
                Block block = blockState.getBlock();
                double x = blockPos.getX() + level.random.nextFloat();
                double y = blockPos.getY() + 1.0D + (blockState.canOcclude() || block instanceof FarmBlock ? 0.3D : 0.0D);
                double z = blockPos.getZ() + level.random.nextFloat();

                level.addParticle(ParticleTypes.RAIN, x, y, z, 0.0D, 0.0D, 0.0D);
            });

        return true;
    }

    public void moisturizeFarmland(ServerLevel level, BlockPos blockPos, BlockState blockState)
    {
        if(!blockState.hasProperty(FarmBlock.MOISTURE))
        {
            return;
        }

        if(blockState.getValue(FarmBlock.MOISTURE) == FarmBlock.MAX_MOISTURE)
        {
            return;
        }

        level.setBlock(blockPos, blockState.setValue(FarmBlock.MOISTURE, 7), 3);
    }

    public void applyGrowthTick(ServerLevel level, BlockPos blockPos, BlockState blockState, Block block)
    {
        if(level.random.nextFloat() > tier.getGrowthTickChance())
        {
            return;
        }

        if(!blockState.is(SoddenTags.WATERING_CAN_GROWABLE) && !(Sodden.CONFIG.useGrowthTickCompatibilityCheck && !(block instanceof BonemealableBlock || block instanceof GrowingPlantBlock)))
        {
            return;
        }

        blockState.randomTick(level, blockPos, level.random);
    }

    public void applyBonemeal(ServerLevel level, BlockPos blockPos, BlockState blockState, Block block)
    {
        if(level.random.nextFloat() > tier.getBonemealChance())
        {
            return;
        }

        if(block instanceof BonemealableBlock bonemealableBlock)
        {
            if(!blockState.is(SoddenTags.WATERING_CAN_BONEMEALABLE) && !Sodden.CONFIG.useBonemealCompatibilityCheck)
            {
                return;
            }

            if(block instanceof SpreadingSnowyDirtBlock || blockState.is(Blocks.GRASS) || blockState.is(Blocks.FERN) || blockState.is(BlockTags.TALL_FLOWERS))
            {
                return;
            }

            if(!bonemealableBlock.isValidBonemealTarget(level, blockPos, blockState, level.isClientSide))
            {
                return;
            }

            bonemealableBlock.performBonemeal(level, level.random, blockPos, blockState);
        }
    }

    public void applyFlower(ServerLevel level, BlockPos blockPos, BlockState blockState)
    {
        if(level.random.nextFloat() > tier.getFlowerChance())
        {
            return;
        }

        if(!blockState.is(Blocks.GRASS_BLOCK) || !level.getBlockState(blockPos.above()).isAir())
        {
            return;
        }

        List<ConfiguredFeature<?, ?>> features = level.getBiome(blockPos).value().getGenerationSettings().getFlowerFeatures();

        if(features.isEmpty())
        {
            return;
        }

        RandomPatchConfiguration config = (RandomPatchConfiguration) features.get(0).config();

        config.feature().value().place(level, level.getChunkSource().getGenerator(), level.random, blockPos.above());
    }

    public void applyFungus(ServerLevel level, BlockPos blockPos, BlockState blockState)
    {
        if(level.random.nextFloat() > tier.getFlowerChance())
        {
            return;
        }

        if(!level.getBlockState(blockPos.above()).isAir())
        {
            return;
        }

        if(blockState.is(Blocks.CRIMSON_NYLIUM))
        {
            NetherFeatures.CRIMSON_FOREST_VEGETATION_BONEMEAL.value().place(level, level.getChunkSource().getGenerator(), level.random, blockPos);
        }
        else if(blockState.is(Blocks.WARPED_NYLIUM))
        {
            NetherFeatures.WARPED_FOREST_VEGETATION_BONEMEAL.value().place(level, level.getChunkSource().getGenerator(), level.random, blockPos);
        }
    }

    public void applySpreadTick(ServerLevel level, BlockPos blockPos, BlockState blockState, Block block)
    {
        if(level.random.nextFloat() > tier.getSpreadTickChance())
        {
            return;
        }

        if(!blockState.is(SoddenTags.WATERING_CAN_SPREADABLE) && !(Sodden.CONFIG.useSpreadTickCompatibilityCheck && block instanceof SpreadingSnowyDirtBlock))
        {
            return;
        }

        blockState.randomTick(level, blockPos, level.random);
    }
}