package fr.uhcraft.bonusrewards;

import fr.uhcraft.bonusrewards.core.SqlQueries;
import fr.uhcraft.bonusrewards.core.database.DatabaseManager;
import fr.uhcraft.bonusrewards.utils.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BonusRewards extends JavaPlugin implements CommandExecutor {
    private static final String PREFIX = "§9Bonus > §7";

    public static BonusRewards INSTANCE;

    public void onEnable() {
        INSTANCE = this;
        DatabaseManager.initAllDataBaseConnections();
        this.getCommand("bonusreward").setExecutor(this);
        this.log(this.getName() + " has been enabled.");
    }

    public void onDisable() {
        DatabaseManager.closeAllDataBaseConnections();
        this.log(this.getName() + " has been disabled.");
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            if (args.length != 0 && (args.length != 1 || sender.hasPermission("bonus.other")) && sender.hasPermission("bonus.give")) {
                final Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(PREFIX + "§a" + args[0] + "§7 est introuvable.");
                } else {
                    if (args.length > 1) {
                        double amount;
                        try {
                            amount = Math.abs(Double.parseDouble(args[1]));
                        } catch (NumberFormatException var7) {
                            sender.sendMessage(PREFIX + "Le montant n'est pas valide.");
                            return;
                        }

                        amount = this.applyBonus(target, amount);
                        target.sendMessage(PREFIX + "Vous avez reçu §a" + NumberUtils.format(amount) + "$§7.");
                        sender.sendMessage(PREFIX + "Vous avez donné §a" + NumberUtils.format(amount) + "$§7 à §a" + target.getName() + "§7.");
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + target.getName() + " " + amount);
                    } else {
                        this.showBonus(sender, target);
                    }

                }
            } else {
                this.showBonus(sender, (Player) sender);
            }
        });
        return true;
    }

    private void showBonus(CommandSender sender, Player target) {
        double amount = 100.0D;
        double bonus = SqlQueries.getBonus(target.getUniqueId());
        amount *= bonus;
        amount = NumberUtils.round(amount, 2);
        sender.sendMessage(PREFIX + (sender.getName().equals(target.getName()) ? "Tu as" : "§a" + target.getName() + "§7 a") + " un bonus de §a" + NumberUtils.format(amount - 100.0D) + "%§7.");
    }

    private double applyBonus(Player target, double amount) {
        double bonus = SqlQueries.getBonus(target.getUniqueId());
        amount *= bonus;
        return NumberUtils.round(amount, 2);
    }

    private void log(String msg) {
        Bukkit.getConsoleSender().sendMessage(msg);
    }
}
