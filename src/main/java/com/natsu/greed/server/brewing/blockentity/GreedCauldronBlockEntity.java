package com.natsu.greed.server.brewing.blockentity;

import java.util.ArrayList;
import java.util.List;

import com.natsu.greed.common.registry.GreedBlockEntities;
import com.natsu.greed.config.ServerConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// Contenu d'un chaudron Greed (le niveau est porté par le blockstate)
public class GreedCauldronBlockEntity extends BlockEntity {

	private List<MobEffectInstance> effects = new ArrayList<>();

	public GreedCauldronBlockEntity(BlockPos pos, BlockState state) {
		super(GreedBlockEntities.CAULDRON.get(), pos, state);
	}

	public List<MobEffectInstance> getEffects() {
		return effects;
	}

	public boolean hasEffects() {
		return !effects.isEmpty();
	}

	// Fusionne la potion au contenu ; sur effet commun, garde le plus haut ampli et
	// ajoute une fraction de la durée du plus faible. Ex : Speed II 1:30 + Speed I 8:00 = Speed II 5:30
	public boolean addPotion(Potion incoming) {
		if (incoming.getEffects().isEmpty()) {
			return false;
		}
		if (effects.isEmpty()) {
			effects = new ArrayList<>(incoming.getEffects());
			markUpdated();
			return true;
		}

		List<MobEffectInstance> merged = new ArrayList<>();
		boolean changed = false;

		for (MobEffectInstance existing : effects) {
			MobEffectInstance match = findByEffect(existing.getEffect(), incoming.getEffects());
			if (match != null) {
				merged.add(mergeInstances(existing, match));
				changed = true;
			} else {
				merged.add(existing);
			}
		}

		for (MobEffectInstance in : incoming.getEffects()) {
			if (findByEffect(in.getEffect(), effects) == null) {
				merged.add(in);
				changed = true;
			}
		}

		if (changed) {
			effects = merged;
			markUpdated();
		}
		return changed;
	}

	private MobEffectInstance mergeInstances(MobEffectInstance existing, MobEffectInstance incoming) {
		double factor = ServerConfig.CAULDRONS_POTION_DURATION_MERGE_FACTOR.get();
		MobEffectInstance strong = incoming.getAmplifier() > existing.getAmplifier() ? incoming : existing;
		MobEffectInstance weak = strong == existing ? incoming : existing;
		int duration = (int) Math.round(strong.getDuration() + (factor * weak.getDuration()));
		return new MobEffectInstance(strong.getEffect(), duration, strong.getAmplifier());
	}

	/** Vide le chaudron et retourne son contenu. */
	public List<MobEffectInstance> drain() {
		List<MobEffectInstance> out = effects;
		effects = new ArrayList<>();
		markUpdated();
		return out;
	}

	private static MobEffectInstance findByEffect(MobEffect effect, List<MobEffectInstance> list) {
		for (MobEffectInstance instance : list) {
			if (instance.getEffect() == effect) {
				return instance;
			}
		}
		return null;
	}

	private void markUpdated() {
		setChanged();
		if (level != null) {
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
		}
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		ListTag effectsTag = new ListTag();
		effects.forEach(effect -> effectsTag.add(effect.save(new CompoundTag())));
		tag.put("effects", effectsTag);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		effects.clear();
		tag.getList("effects", Tag.TAG_COMPOUND).forEach(t -> {
			MobEffectInstance effect = MobEffectInstance.load((CompoundTag) t);
			if (effect != null) {
				effects.add(effect);
			}
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

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		CompoundTag tag = pkt.getTag();
		if (tag != null) {
			load(tag);
		}
		// invalide le rendu pour rafraîchir la teinte dès le changement d'effets
		if (level != null && level.isClientSide()) {
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
		}
	}

}
