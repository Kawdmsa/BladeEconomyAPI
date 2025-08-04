package examples;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import usa.liez.BladeEconomy;
import usa.liez.EconomyAPI;

/**
 * Пример плагина с командой /stats
 * Демонстрирует просмотр статистики игрока
 */
public class StatsCommand extends PluginBase {
    
    @Override
    public void onEnable() {
        // Регистрируем команду /stats
        getServer().getCommandMap().register("stats", new Command("stats", "Просмотр статистики", "/stats [игрок]") {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(TextFormat.RED + "Эта команда только для игроков!");
                    return true;
                }
                
                Player player = (Player) sender;
                
                // Получаем API
                BladeEconomy bladeEconomy = (BladeEconomy) getServer().getPluginManager().getPlugin("BladeEconomy");
                if (bladeEconomy == null) {
                    sender.sendMessage(TextFormat.RED + "BladeEconomy не найден!");
                    return true;
                }
                
                EconomyAPI api = bladeEconomy.getEconomyAPI();
                
                if (args.length == 0) {
                    // Показать свою статистику
                    sender.sendMessage(api.getFormattedStats(player));
                    return true;
                }
                
                // Показать статистику другого игрока
                String targetName = args[0];
                Player target = getServer().getPlayer(targetName);
                
                if (target == null) {
                    sender.sendMessage(TextFormat.RED + "Игрок " + targetName + " не найден!");
                    return true;
                }
                
                sender.sendMessage(api.getFormattedStats(target));
                return true;
            }
        });
        
        getLogger().info(TextFormat.GREEN + "StatsCommand успешно загружен!");
    }
} 