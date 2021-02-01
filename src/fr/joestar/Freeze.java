package fr.toma;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Freeze
  extends JavaPlugin
  implements Listener
{
  ArrayList<String> frozen = new ArrayList();
  String prefix = "§f[§boFreeze§f]§a ";
  ChatColor red = ChatColor.RED;
  
  @EventHandler
  public void onPlayerMove(PlayerMoveEvent e)
  {
    Player p = e.getPlayer();
    if (this.frozen.contains(p.getName()))
    {
      e.setTo(e.getFrom());
      p.sendMessage(this.prefix + this.red + "You're frozen, you can't move!");
    }
  }
  
  @EventHandler
  public void onBlockBreak(BlockBreakEvent e)
  {
    Player p = e.getPlayer();
    if (this.frozen.contains(p.getName()))
    {
      e.setCancelled(true);
      p.sendMessage(this.prefix + this.red + "You're frozen, you can't break any blocks!");
    }
  }
  
  @EventHandler
  public void onBlockPlace(BlockPlaceEvent e)
  {
    Player p = e.getPlayer();
    if (this.frozen.contains(p.getName()))
    {
      e.setCancelled(true);
      p.sendMessage(this.prefix + this.red + "You're frozen, you can't place any blocks!");
    }
  }
  
  public void onEnable()
  {
    Bukkit.getServer().getPluginManager().registerEvents(this, this);
  }
  
  public void onDisable() {}
  
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
  {
    if (cmd.getName().equalsIgnoreCase("freeze"))
    {
      if (args.length == 0)
      {
        sender.sendMessage(this.prefix + ChatColor.RED + "Specify a player.");
        return true;
      }
      if (!sender.hasPermission("freeze.use")) {
        return true;
      }
      if (args[0].equalsIgnoreCase("*"))
      {
        if (!sender.hasPermission("freeze.all")) {
          return true;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
          this.frozen.add(player.getName());
        }
      }
      Player target = Bukkit.getServer().getPlayer(args[0]);
      if (target == null)
      {
        sender.sendMessage(this.prefix + ChatColor.RED + "Player " + args[0] + "is not logged!");
        return true;
      }
      if (this.frozen.contains(target.getName()))
      {
        this.frozen.remove(target.getName());
        sender.sendMessage(this.prefix + target.getName() + " has been unfrozen!");
        target.sendMessage(this.prefix + "You have been unfrozen!");
        target.setAllowFlight(false);
        target.setFlying(false);
        return true;
      }
      this.frozen.add(target.getName());
      target.setAllowFlight(true);
      target.setFlying(true);
      sender.sendMessage(this.prefix + target.getName() + " has been frozen!");
      target.sendMessage(this.prefix + "You have been frozen!");
    }
    if (cmd.getName().equalsIgnoreCase("frozen"))
    {
      if (args.length == 0)
      {
        sender.sendMessage(this.prefix + ChatColor.RED + "Specify a player");
        return true;
      }
      if (!sender.hasPermission("frozen.use")) {
        return true;
      }
      Player target = Bukkit.getServer().getPlayer(args[0]);
      if (target == null)
      {
        sender.sendMessage(this.prefix + ChatColor.RED + "Player " + args[0] + "is not logged!");
        return true;
      }
      if (this.frozen.contains(target.getName()))
      {
        sender.sendMessage(this.prefix + target.getName() + " is currently frozen");
        return true;
      }
      if (!this.frozen.contains(target.getName()))
      {
        sender.sendMessage(this.prefix + target.getName() + " is not currrently frozen");
        return true;
      }
    }
    return true;
  }
}
