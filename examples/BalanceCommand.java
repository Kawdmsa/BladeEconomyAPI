package examples;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import usa.liez.BladeEconomy;
import usa.liez.EconomyAPI;

/**
 * Пример плагина, использующего BladeEconomy API
 * Демонстрирует как создать команду /balance
 */
public class BalanceCommand extends PluginBase {
    
    @Override
    public void onEnable() {
        // Регистрируем команду
        getServer().getCommandMap().register("balance", new Command("balance", "Проверить баланс", "/balance [игрок]") {
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
                    // Показать свой баланс
                    double coins = api.getCoins(player);
                    double amethysts = api.getAmethysts(player);
                    
                    sender.sendMessage(TextFormat.GOLD + "=== Ваш баланс ===");
                    sender.sendMessage(TextFormat.YELLOW + "Монеты: " + TextFormat.WHITE + coins);
                    sender.sendMessage(TextFormat.LIGHT_PURPLE + "Аметисты: " + TextFormat.WHITE + amethysts);
                    return true;
                }
                
                // Показать баланс другого игрока
                String targetName = args[0];
                Player target = getServer().getPlayer(targetName);
                
                if (target == null) {
                    sender.sendMessage(TextFormat.RED + "Игрок " + targetName + " не найден!");
                    return true;
                }
                
                double coins = api.getCoins(target);
                double amethysts = api.getAmethysts(target);
                
                sender.sendMessage(TextFormat.GOLD + "=== Баланс " + target.getName() + " ===");
                sender.sendMessage(TextFormat.YELLOW + "Монеты: " + TextFormat.WHITE + coins);
                sender.sendMessage(TextFormat.LIGHT_PURPLE + "Аметисты: " + TextFormat.WHITE + amethysts);
                return true;
            }
        });
        
        getLogger().info(TextFormat.GREEN + "BalanceCommand успешно загружен!");
    }
} 