package com.arkmod.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
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
            if (distance > 8.0D) {
                // Teleport (çok uzaksa)
                this.setPosition(owner.posX, owner.posY, owner.posZ);
            } else if (distance > 3.0D) {
                // Yürü
                this.getNavigator().tryMoveToEntityLiving(owner, 1.0D);
            }
        }
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (!this.world.isRemote) {
            if (owner == null) {
                owner = player;
                player.sendMessage(new TextComponentString("§d[ArkMod] Artık sevgilin oldum!"));
            } else if (owner == player) {
                // Öpüşme
                affection += 5;
                player.sendMessage(new TextComponentString("§d[ArkMod] Öpüldün ♥ Sevgi: " + affection));

                // Kalp particle
                for (int i = 0; i < 7; i++) {
                    this.world.spawnParticle(EnumParticleTypes.HEART,
                            this.posX + (rand.nextDouble() - 0.5D),
                            this.posY + 1.5D + rand.nextDouble(),
                            this.posZ + (rand.nextDouble() - 0.5D),
                            0, 0.1, 0);
                }

                // Ses
                this.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 0.6F, 1.4F);
            } else {
                player.sendMessage(new TextComponentString("§cBen başkasının sevgilisiyim."));
            }
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
