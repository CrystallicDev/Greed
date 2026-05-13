package com.natsu.greed.server.brewing.blockentity;

import java.util.ArrayList;
import java.util.List;

import com.natsu.greed.common.registry.GreedBlockEntities;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.utils.PotionCreatorUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class GreedCauldronBlockEntity extends BlockEntity {

	private List<MobEffectInstance> potions = new ArrayList<>();
	private int level = 0;
	private static final int MAX_LEVEL = 3;
	
	public GreedCauldronBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
		super(GreedBlockEntities.CAULDRON.get(), p_155229_, p_155230_);
	}
	
	public List<MobEffectInstance> getPotion() { return potions; }
	public void setPotion(List<MobEffectInstance> potion) { this.potions = potion; }
	public void empty() { this.level = 0; this.potions.clear(); setChanged(); }
	public int getPotLevel() { return level; }
	public boolean isEmpty() { return level == 0 || this.potions.isEmpty(); }
	public boolean isFull() { return level >= MAX_LEVEL; }
	
	
	public boolean addPotion(Potion in) {
		if (isEmpty()) {
			this.potions = in.getEffects();
			this.level = 1;
			setChanged();
			return true;
		}
		if (!isFull()) {
			this.level++;
			setChanged();
			return true;
		}
		return false;
	}
	
	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt("level", level);
		ListTag effectsTag = new ListTag();
		potions.forEach(effect -> {
			CompoundTag nt = new CompoundTag();
			effect.save(nt);
			effectsTag.add(nt);
		});
		tag.put("effects", effectsTag);
	}
	
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.level = tag.getInt("level");
		potions.clear();
		tag.getList("effects", Tag.TAG_COMPOUND)
			.forEach(t -> {
				MobEffectInstance effect = MobEffectInstance.load((CompoundTag) t);
				if (potions != null) potions.add(effect);
			});
	}
	
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override
	public CompoundTag getUpdateTag() {
		return saveWithFullMetadata();
	}
	
	private static void updateCauldronAppearance(Level level, BlockPos pos, BlockState state, int potionLevel) {
		if (state.hasProperty(LayeredCauldronBlock.LEVEL)) {
			level.setBlock(pos, state.setValue(LayeredCauldronBlock.LEVEL, potionLevel), 3);
		}
	}
	
	public boolean onAddingPotion(Potion incoming) {
	    System.out.println("onAddingPotion");
	    if (isFull()) return false;

	    List<MobEffectInstance> finalEffects = new ArrayList<>();
	    boolean hasMadeUpdate = false;

	    if (isEmpty()) {
	        System.out.println("onAddingPotion - Cauldron is Empty");
	        finalEffects.addAll(incoming.getEffects());
	        hasMadeUpdate = true;
	    } else {
	        System.out.println("onAddingPotion - Cauldron is not Empty");

	        // 1. Copier les effets existants, en mergant ceux qui matchent avec incoming
	        for (MobEffectInstance existing : potions) {
	            boolean matchedByIncoming = false;
	            for (MobEffectInstance effect : incoming.getEffects()) {
	                if (!isEffectCompatible(existing, effect)) continue;
	                if (effect.getEffect().equals(existing.getEffect()) 
	                        && effect.getAmplifier() == existing.getAmplifier()) {
	                    // Merger la durée
	                    int mergedDuration = (int) Math.round(
	                        existing.getDuration() + (ServerConfig.CAULDRONS_POTION_DURATION_MERGE_FACTOR.get() 
	                        * effect.getDuration())
	                    );
	                    finalEffects.add(new MobEffectInstance(
	                        existing.getEffect(), mergedDuration, existing.getAmplifier()
	                    ));
	                    matchedByIncoming = true;
	                    hasMadeUpdate = true;
	                    break;
	                }
	            }
	            // Aucun effet de incoming ne correspond : on conserve l'effet existant tel quel
	            if (!matchedByIncoming) {
	                finalEffects.add(existing);
	            }
	        }

	        // 2. Ajouter les effets de incoming qui ne sont pas déjà dans potions
	        for (MobEffectInstance effect : incoming.getEffects()) {
	            boolean alreadyHandled = false;
	            for (MobEffectInstance existing : potions) {
	                if (!isEffectCompatible(existing, effect)) {
	                    alreadyHandled = true; // Incompatible, on ignore
	                    break;
	                }
	                if (effect.getEffect().equals(existing.getEffect()) 
	                        && effect.getAmplifier() == existing.getAmplifier()) {
	                    alreadyHandled = true; // Déjà mergé à l'étape 1
	                    break;
	                }
	            }
	            if (!alreadyHandled) {
	                finalEffects.add(effect);
	                hasMadeUpdate = true;
	            }
	        }
	    }

	    if (hasMadeUpdate) {
	        System.out.println("onAddingPotion - Cauldron is now updated !");
	        setPotion(finalEffects);
	        this.level++;
	        setChanged();
	        return true;
	    }
	    return false;
	}
	
	private boolean isEffectCompatible(MobEffectInstance effect, MobEffectInstance effect2) {
		if (effect.getEffect() == effect2.getEffect() && effect.getAmplifier() != effect2.getAmplifier()) return false;
		return true;
	}

	public boolean onTakingPotion(Player player) {
		System.out.println("onTakingPotion");
		List<MobEffectInstance> outgoing = getPotion();
		if (!outgoing.isEmpty()) {
			System.out.println("onTakingPotion - Cauldron has a potion");
			ItemStack potionBottle = PotionCreatorUtils.makeIntoPotion(Items.POTION, outgoing);
			empty();
			if (!player.getInventory().add(potionBottle)) {
	            player.drop(potionBottle, false);
	        }
			return true;
		}
		return false;
	}
	
	/**
	 * [22:36:05] [Render thread/INFO] [minecraft/ChatComponent]: [CHAT] Le bloc en 161, 81, 10 a été modifié
interact on Cauldron with Potion !
Cauldron is not Full ! pass
onAddingPotion
onAddingPotion - Cauldron is Empty
onAddingPotion - Cauldron is now updated !
Cauldron Adding : true
[22:36:11] [Server thread/INFO] [minecraft/MinecraftServer]: <Natsu91> t
[22:36:11] [Render thread/INFO] [minecraft/ChatComponent]: [CHAT] <Natsu91> t
interact on Cauldron with Potion !
Cauldron is not Full ! pass
onAddingPotion
onAddingPotion - Cauldron is not Empty
onAddingPotion - Cauldron is now updated !
Cauldron Adding : true
[22:36:15] [Server thread/INFO] [minecraft/MinecraftServer]: <Natsu91> ttttt
[22:36:15] [Render thread/INFO] [minecraft/ChatComponent]: [CHAT] <Natsu91> ttttt
Cauldron is not Empty ! pass
onTakingPotion
Cauldron Taking : false
[22:36:19] [Server thread/INFO] [minecraft/MinecraftServer]: <Natsu91> tttt
[22:36:19] [Render thread/INFO] [minecraft/ChatComponent]: [CHAT] <Natsu91> tttt

	 * */
	
}
