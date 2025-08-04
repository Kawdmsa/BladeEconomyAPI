package examples;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import usa.liez.BladeEconomy;
import usa.liez.EconomyAPI;

/**
 * Пример плагина с командой /pay
 * Демонстрирует переводы валют между игроками
 */
public class PayCommand extends PluginBase {
    
    @Override
    public void onEnable() {
        // Регистрируем команду /pay
        getServer().getCommandMap().register("pay", new Command("pay", "Перевести валюту другому игроку", "/pay <игрок> <тип> <количество>") {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(TextFormat.RED + "Эта команда только для игроков!");
                    return true;
                }
                
                if (args.length != 3) {
                    sender.sendMessage(TextFormat.RED + "Использование: /pay <игрок> <тип> <количество>");
                    sender.sendMessage(TextFormat.YELLOW + "Типы валют: coins, amethysts");
                    return true;
                }
                
                Player from = (Player) sender;
                String targetName = args[0];
                String currencyType = args[1].toLowerCase();
                double amount;
                
                try {
                    amount = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(TextFormat.RED + "Неверное количество!");
                    return true;
                }
                
                if (amount <= 0) {
                    sender.sendMessage(TextFormat.RED + "Количество должно быть больше 0!");
                    return true;
                }
                
                Player to = getServer().getPlayer(targetName);
                if (to == null) {
                    sender.sendMessage(TextFormat.RED + "Игрок " + targetName + " не найден!");
                    return true;
                }
                
                if (from.equals(to)) {
                    sender.sendMessage(TextFormat.RED + "Вы не можете перевести валюту самому себе!");
                    return true;
                }
                
                // Получаем API
                BladeEconomy bladeEconomy = (BladeEconomy) getServer().getPluginManager().getPlugin("BladeEconomy");
                if (bladeEconomy == null) {
                    sender.sendMessage(TextFormat.RED + "BladeEconomy не найден!");
                    return true;
                }
                
                EconomyAPI api = bladeEconomy.getEconomyAPI();
                
                boolean success = false;
                switch (currencyType) {
                    case "coins":
                        success = api.transferCoins(from, to, amount);
                        break;
                    case "amethysts":
                        success = api.transferAmethysts(from, to, amount);
                        break;
                    default:
                        sender.sendMessage(TextFormat.RED + "Неверный тип валюты! Используйте: coins, amethysts");
                        return true;
                }
                
                if (success) {
                    sender.sendMessage(TextFormat.GREEN + "Переведено " + amount + " " + currencyType + " игроку " + to.getName());
                    to.sendMessage(TextFormat.GREEN + "Вам переведено " + amount + " " + currencyType + " от " + from.getName());
                } else {
                    sender.sendMessage(TextFormat.RED + "Недостаточно средств!");
                }
                
                return true;
            }
        });
        
        getLogger().info(TextFormat.GREEN + "PayCommand успешно загружен!");
    }
} 