package com.arkmod.entity;

import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class EntityGirlfriend extends EntityCreature {

    private EntityPlayer owner;
    private int affection = 0;

    public EntityGirlfriend(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(3, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        // Sahibinin peşinden gelsin
        if (owner != null && !this.world.isRemote) {
            double distance = this.getDistance(owner);
            if (distance > 10.0D) {
                this.setPosition(owner.posX, owner.posY, owner.posZ);
            } else if (distance > 3.5D) {
                this.getNavigator().tryMoveToEntityLiving(owner, 1.0D);
            }
        }
    }

    // Yatak kontrolü
    private boolean isNearBed() {
        BlockPos pos = this.getPosition();
        for (int x = -2; x <= 2; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -2; z <= 2; z++) {
                    BlockPos checkPos = pos.add(x, y, z);
                    IBlockState state = this.world.getBlockState(checkPos);
                    if (state.getBlock() instanceof BlockBed || state.getBlock() == Blocks.BED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (!this.world.isRemote) {

            // 1. Henüz sevgili değilse
            if (owner == null) {
                owner = player;
                player.sendMessage(new TextComponentString("§d[ArkMod] Artık sevgilin oldum!"));
                return true;
            }

            // 2. Başkasının sevgilisiyse
            if (owner != player) {
                player.sendMessage(new TextComponentString("§cBen başkasının sevgilisiyim."));
                return true;
            }

            // 3. Shift + Sağ Tık (Domaltma)
            if (player.isSneaking()) {
                if (isNearBed()) {
                    // Domaltma başarılı
                    affection += 15;
                    player.sendMessage(new TextComponentString("§5[ArkMod] Domaltıldı... ♥ Sevgi: " + affection));

                    // Efektler
                    for (int i = 0; i < 12; i++) {
                        this.world.spawnParticle(EnumParticleTypes.HEART,
                                this.posX + (rand.nextDouble() - 0.5D) * 1.2D,
                                this.posY + 0.8D + rand.nextDouble(),
                                this.posZ + (rand.nextDouble() - 0.5D) * 1.2D,
                                0, 0.1, 0);
                    }
                    this.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 0.7F, 0.8F);
                } else {
                    player.sendMessage(new TextComponentString("§cYatağın yanında olmalısın!"));
                }
                return true;
            }

            // 4. Normal Sağ Tık (Öpüşme)
            affection += 5;
            player.sendMessage(new TextComponentString("§d[ArkMod] Öpüldün ♥ Sevgi: " + affection));

            for (int i = 0; i < 6; i++) {
                this.world.spawnParticle(EnumParticleTypes.HEART,
                        this.posX + (rand.nextDouble() - 0.5D),
                        this.posY + 1.4D + rand.nextDouble() * 0.4D,
                        this.posZ + (rand.nextDouble() - 0.5D),
                        0, 0.08, 0);
            }
            this.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 1.3F);
        }
        return true;
    }

    public EntityPlayer getOwner() {
        return owner;
    }

    public int getAffection() {
        return affection;
    }
}
