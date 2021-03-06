package com.pauldavdesign.mineauz.minigames.commands.set;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pauldavdesign.mineauz.minigames.MinigameUtils;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;
import com.pauldavdesign.mineauz.minigames.minigame.Minigame;

public class SetLobbyCommand implements ICommand{

	@Override
	public String getName() {
		return "lobby";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public boolean canBeConsole() {
		return false;
	}

	@Override
	public String getDescription() {
		return "Sets the lobby position of a Minigame to where you are standing. You can also modify lobby settings using this command.";
	}

	@Override
	public String[] getParameters() {
		return null;
	}

	@Override
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> lobby", 
				"/minigame set <Minigame> lobby <canMove/canInteract/teleport> <playerWait/startWait> <true/false>"};
	}

	@Override
	public String getPermissionMessage() {
		return "You do not have permission to set the Minigames Lobby Position!";
	}

	@Override
	public String getPermission() {
		return "minigame.set.lobby";
	}

	@Override
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args == null){
			minigame.setLobbyPosition(((Player)sender).getLocation());
			sender.sendMessage(ChatColor.GRAY + "Lobby position has been set for " + minigame);
			return true;
		}
		else{
			if(args.length == 3){
				if(args[0].equalsIgnoreCase("canmove")){
					boolean v = true;
					v = Boolean.getBoolean(args[2]);
					if(args[1].equalsIgnoreCase("playerwait")){
						minigame.setCanMovePlayerWait(v);
						if(v)
							sender.sendMessage(ChatColor.GRAY + "Allowed players to move in lobby on player wait.");
						else
							sender.sendMessage(ChatColor.GRAY + "Disallowed players to move in lobby on player wait.");
					}
					else if(args[1].equalsIgnoreCase("startwait")){
						minigame.setCanMoveStartWait(v);
						if(v)
							sender.sendMessage(ChatColor.GRAY + "Allowed players to move in lobby on start wait.");
						else
							sender.sendMessage(ChatColor.GRAY + "Disallowed players to move in lobby on start wait.");
					}
					else{
						sender.sendMessage(ChatColor.RED + "Invalid syntax!");
						return false;
					}
				}
				else if(args[0].equalsIgnoreCase("caninteract")){
					boolean v = true;
					v = Boolean.getBoolean(args[2]);
					if(args[1].equalsIgnoreCase("playerwait")){
						minigame.setCanInteractPlayerWait(v);
						if(v)
							sender.sendMessage(ChatColor.GRAY + "Allowed players to interact in lobby on player wait.");
						else
							sender.sendMessage(ChatColor.GRAY + "Disallowed players to interact in lobby on player wait.");
					}
					else if(args[1].equalsIgnoreCase("startwait")){
						minigame.setCanInteractStartWait(v);
						if(v)
							sender.sendMessage(ChatColor.GRAY + "Allowed players to interact in lobby on start wait.");
						else
							sender.sendMessage(ChatColor.GRAY + "Disallowed players to interact in lobby on start wait.");
					}
					else{
						sender.sendMessage(ChatColor.RED + "Invalid syntax!");
						return false;
					}
				}
				else if(args[0].equalsIgnoreCase("teleport")){
					boolean v = Boolean.parseBoolean(args[2]);
					if(args[1].equalsIgnoreCase("playerwait")){
						minigame.setTeleportOnPlayerWait(v);
						if(v)
							sender.sendMessage(ChatColor.GRAY + "Allowed players to teleport out of lobby on player wait.");
						else
							sender.sendMessage(ChatColor.GRAY + "Disallowed players to teleport out of lobby on player wait.");
					}
					else if(args[1].equalsIgnoreCase("startwait")){
						minigame.setTeleportOnStart(v);
						if(v)
							sender.sendMessage(ChatColor.GRAY + "Allowed players to teleport out of lobby on start.");
						else
							sender.sendMessage(ChatColor.GRAY + "Disallowed players to teleport out of lobby on start.");
					}
					else{
						sender.sendMessage(ChatColor.RED + "Invalid syntax!");
						return false;
					}
				}
				else{
					sender.sendMessage(ChatColor.RED + "Invalid syntax!");
					return false;
				}
			}
			return false;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Minigame minigame,
			String alias, String[] args) {
		if(args != null){
			if(args.length == 1){
				return MinigameUtils.tabCompleteMatch(MinigameUtils.stringToList("canmove;caninteract;teleport"), args[args.length - 1]);
			}
			else if(args.length == 2){
				return MinigameUtils.tabCompleteMatch(MinigameUtils.stringToList("playerwait;startwait"), args[args.length - 1]);
			}
			else{
				return MinigameUtils.tabCompleteMatch(MinigameUtils.stringToList("true;false"), args[args.length - 1]);
			}
		}
		return null;
	}
}
