package com.pauldavdesign.mineauz.minigames.signs;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Sign;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.SignChangeEvent;

import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.MinigameSave;
import com.pauldavdesign.mineauz.minigames.MinigameUtils;
import com.pauldavdesign.mineauz.minigames.Minigames;

public class RewardSign implements MinigameSign {
	
	private static Minigames plugin = Minigames.plugin;

	@Override
	public String getName() {
		return "Reward";
	}

	@Override
	public String getCreatePermission() {
		return "minigame.sign.create.reward";
	}

	@Override
	public String getCreatePermissionMessage() {
		return MinigameUtils.getLang("sign.reward.createPermission");
	}

	@Override
	public String getUsePermission() {
		return "minigame.sign.use.reward";
	}

	@Override
	public String getUsePermissionMessage() {
		return MinigameUtils.getLang("sign.reward.usePermission");
	}

	@Override
	public boolean signCreate(SignChangeEvent event) {
		event.setLine(1, ChatColor.GREEN + "Reward");
		if(event.getLine(2).isEmpty()){
			event.getPlayer().sendMessage(ChatColor.RED + MinigameUtils.getLang("sign.reward.noName"));
			return false;
		}
		String[] split = event.getLine(3).split(" ");
		if(!event.getLine(3).isEmpty()){
			if(!split[0].matches("[0-9]+(:[0-9]+)?(:[0-9]+(,[0-9]+)?)+?")){
				if(Material.getMaterial(split[0].toUpperCase()) != null){
					return true;
				}
				event.getPlayer().sendMessage(ChatColor.RED + MinigameUtils.formStr("sign.reward.invalidItem", split[0]));
			}
			else{
				if(Material.getMaterial(Integer.parseInt(split[0].split(":")[0])) != null)
					return true;
				else
					event.getPlayer().sendMessage(ChatColor.RED + MinigameUtils.formStr("sign.reward.noId", split[0]));
			}
		}
		else{
			event.getPlayer().sendMessage(ChatColor.BLUE + "[Minigames] " + ChatColor.WHITE + MinigameUtils.getLang("sign.reward.clickSign"));
			final Location loc = event.getBlock().getLocation();
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					if(loc.getBlock().getState() instanceof Sign){
						Sign sign = (Sign) loc.getBlock().getState();
						if(sign.getLine(3).isEmpty()){
							loc.getBlock().breakNaturally();
						}
					}
				}
			}, 200);
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean signUse(Sign sign, MinigamePlayer player) {
		if(!sign.getLine(3).isEmpty()){
			ItemStack item = null;
			
			Configuration completion = plugin.mdata.getConfigurationFile("completion");
			boolean hascompleted = completion.getStringList(sign.getLine(2)).contains(player.getName());
			
			if(!hascompleted){
				List<String> completionlist = completion.getStringList(sign.getLine(2));
				completionlist.add(player.getName());
				completion.set(sign.getLine(2), completionlist);
				MinigameSave completionsave = new MinigameSave("completion");
				completionsave.getConfig().set(sign.getLine(2), completionlist);
				completionsave.saveConfig();
				
				String[] split = sign.getLine(3).split(" ");
				if(!split[0].matches("[0-9]+(:[0-9]+)?(:[0-9]+(,[0-9]+)?)+?")){
					if(Material.getMaterial(split[0].toUpperCase()) != null){
						int amount = 1;
						if(split.length == 2 && split[1].matches("[0-9]+")){
							amount = Integer.parseInt(split[1]);
						}
						item = new ItemStack(Material.getMaterial(split[0].toUpperCase()), amount);
					}
				}
				else{
					if(Material.getMaterial(Integer.parseInt(split[0].split(":")[0])) != null){
						String[] split2 = split[0].split(":");
						short damage = 0;
						if(split2.length >= 2 && split2[1].matches("[0-9]+")){
							damage = Short.parseShort(split2[1]);
						}
	
						int amount = 1;
						if(split.length == 2 && split[1].matches("[0-9]+")){
							amount = Integer.parseInt(split[1]);
						}
						
						item = new ItemStack(Integer.parseInt(split2[0]), amount);
						
						item.setDurability(damage);

						if(split2.length > 2){
							for(int i = 2; i < split2.length; i++){
								String[] enchants = split2[i].split(",");
								if(enchants[0].matches("[0-9]+")){
									int level = 1;
									if(enchants.length == 2 && enchants[1].matches("[0-9]+")){
										level = Integer.parseInt(enchants[1]);
									}
									item.addEnchantment(Enchantment.getById(Integer.parseInt(enchants[0])), level);
								}
							}
						}
					}
				}
				
				if(item != null){
					player.getPlayer().getInventory().addItem(item);
					player.sendMessage(MinigameUtils.formStr("sign.reward.rewarded", item.getAmount(), MinigameUtils.getItemStackName(item)));
					player.getPlayer().updateInventory();
					return true;
				}
			}
		}
		else{
			if(player.getPlayer().getItemInHand().getType() != Material.AIR){
				String id = "";
				ItemStack item = player.getPlayer().getItemInHand();
				id += item.getTypeId();
				if(item.getDurability() != 0 || !item.getEnchantments().isEmpty()){
					id += ":" + item.getDurability();
				}
				if(!item.getEnchantments().isEmpty()){
					for(Enchantment en : item.getEnchantments().keySet()){
						id += ":" + en.getId();
						if(item.getEnchantments().get(en) != 1){
							id += "," + item.getEnchantments().get(en);
						}
					}
				}
				
				if(item.getAmount() > 1){
					id += " " + item.getAmount();
				}
				sign.setLine(3, id);
				sign.update();
				return true;
			}
		}
		return false;
	}

}
