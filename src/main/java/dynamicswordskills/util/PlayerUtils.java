/**
    Copyright (C) <2017> <coolAlias>

    This file is part of coolAlias' Dynamic Sword Skills Minecraft Mod; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package dynamicswordskills.util;


import dynamicswordskills.DynamicSwordSkills;
import dynamicswordskills.api.ISkillProvider;
import dynamicswordskills.api.IWeapon;
import dynamicswordskills.api.WeaponRegistry;
import dynamicswordskills.network.PacketDispatcher;
import dynamicswordskills.network.bidirectional.PlaySoundPacket;
import dynamicswordskills.skills.SkillBase;
import dynamicswordskills.skills.Skills;
import mods.battlegear2.api.core.IBattlePlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

/**
 *
 * A collection of utility methods related to the player
 *
 */
public class PlayerUtils
{
	/**
	 * Returns whether the player is blocking, accounting for the possibility of Battlegear2 shield item use
	 */
	public static boolean isBlocking(EntityPlayer player) {
		if (player.isBlocking()) {
			return true;
		} else if (DynamicSwordSkills.isBG2Enabled) {
			return ((IBattlePlayer) player).isBattlemode() && ((IBattlePlayer) player).isBlockingWithShield();
		}
		return false;
	}

	/**
	 * Returns true if the item is a sword: i.e. if it is an {@link ItemSword},
	 * an {@link IWeapon} (returns {@link IWeapon#isSword(ItemStack)}),
	 * or registered to the {@link WeaponRegistry} as a sword
	 */
	public static boolean isSword(ItemStack stack) {
		if (stack == null) {
			return false;
		} else if (stack.getItem() instanceof IWeapon) {
			return ((IWeapon) stack.getItem()).isSword(stack);
		}
		return WeaponRegistry.INSTANCE.isSword(stack.getItem());
	}

	/**
	 * Returns true if the item is any kind of weapon: a {@link #isSword(ItemStack) sword},
	 * an {@link IWeapon}, or registered to the {@link WeaponRegistry} as a weapon
	 */
	public static boolean isWeapon(ItemStack stack) {
		if (stack == null) {
			return false;
		} else if (stack.getItem() instanceof IWeapon) {
			return ((IWeapon) stack.getItem()).isWeapon(stack);
		}
		return (isSword(stack) || WeaponRegistry.INSTANCE.isWeapon(stack.getItem()));
	}

	/** Returns true if the stack is either a {@link #isSword(ItemStack) sword} or {@link #isProvider(ItemStack, SkillBase) provider} of this skill */
	public static boolean isSwordOrProvider(ItemStack stack, SkillBase skill) {
		return (isSword(stack) || isProvider(stack, skill));
	}

	/**
	 * Returns whether the stack is a {@link ISkillProvider} for the target skill
	 */
	public static boolean isProvider(ItemStack stack, SkillBase skill) {
		if (stack == null || !(stack.getItem() instanceof ISkillProvider)) {
			return false;
		} else if (((ISkillProvider) stack.getItem()).getSkillId(stack) == skill.getId()) {
			return true;
		}
		return Skills.swordBasic.is(skill) && ((ISkillProvider) stack.getItem()).grantsBasicSwordSkill(stack);
	}

	/** Returns the difference between player's max and current health */
	public static float getHealthMissing(EntityPlayer player) {
		return player.capabilities.isCreativeMode ? 0.0F : (player.getMaxHealth() - player.getHealth());
	}

	/** Sends a translated chat message with optional arguments to the player */
	public static void sendTranslatedChat(EntityPlayer player, String message, Object... args) {
		player.addChatMessage(new ChatComponentTranslation(message, args));
	}

	/**
	 * Sends a packet to the client to play a sound on the client side only, or
	 * sends a packet to the server to play a sound on the server for all to hear.
	 * To avoid playing a sound twice, only call the method from one side or the other, not both.
	 */
	public static void playSound(EntityPlayer player, String sound, float volume, float pitch) {
		if (player.worldObj.isRemote) {
			PacketDispatcher.sendToServer(new PlaySoundPacket(sound, volume, pitch, player));
		} else {
			PacketDispatcher.sendTo(new PlaySoundPacket(sound, volume, pitch), (EntityPlayerMP) player);
		}
	}

	/**
	 * Plays a sound with randomized volume and pitch.
	 * Sends a packet to the client to play a sound on the client side only, or
	 * sends a packet to the server to play a sound on the server for all to hear.
	 * To avoid playing a sound twice, only call the method from one side or the other, not both.
	 * @param f		Volume: nextFloat() * f + add
	 * @param add	Pitch: 1.0F / (nextFloat() * f + add)
	 */
	public static void playRandomizedSound(EntityPlayer player, String sound, float f, float add) {
		float volume = player.worldObj.rand.nextFloat() * f + add;
		float pitch = 1.0F / (player.worldObj.rand.nextFloat() * f + add);
		playSound(player, sound, volume, pitch);
	}

	/**
	 * Plays a sound on the server with randomized volume and pitch; no effect if called on a client
	 * @param f		Volume: nextFloat() * f + add
	 * @param add	Pitch: 1.0F / (nextFloat() * f + add)
	 */
	public static void playSoundAtEntity(World world, Entity entity, String sound, float f, float add) {
		float volume = world.rand.nextFloat() * f + add;
		float pitch = 1.0F / (world.rand.nextFloat() * f + add);
		world.playSoundAtEntity(entity, sound, volume, pitch);
	}

}
