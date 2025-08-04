# BladeEconomy API

A powerful and simple economy API plugin for PowerNukkitX with dual currency system (coins & amethysts) and comprehensive player statistics tracking.

[English](#english) | [Русский](#russian)

---

## English

### Description

BladeEconomy is a simple and powerful API plugin for economy and player statistics in PowerNukkitX. It provides two currencies (coins and amethysts) and comprehensive player statistics tracking.

### Features

- ✅ Two currencies: coins and amethysts
- ✅ SQLite database using DataManager
- ✅ Player statistics (kills, deaths, playtime)
- ✅ K/D ratio calculation
- ✅ Automatic playtime tracking
- ✅ Easy API for integration with other plugins
- ✅ Scoreboard support
- ✅ Automatic player creation in database

### Installation

1. Download the JAR file
2. Place it in the `plugins` folder
3. Restart the server
4. The plugin will automatically create the database and tables

### Configuration

The `config.yml` file contains only essential settings:

```yaml
# Starting balance for new players
starting_coins: 100.0
starting_amethysts: 10.0
```

### API Usage

#### Getting the API

```java
BladeEconomy plugin = (BladeEconomy) getServer().getPluginManager().getPlugin("BladeEconomy");
EconomyAPI api = plugin.getEconomyAPI();
```

#### Economy Methods

**Get Balance:**
```java
// Get coins amount
double coins = api.getCoins(player);

// Get amethysts amount
double amethysts = api.getAmethysts(player);

// Get for scoreboard (strings)
String coinsStr = api.getCoinsForScoreboard(player);
String amethystsStr = api.getAmethystsForScoreboard(player);
```

**Modify Balance:**
```java
// Add currency
api.addCoins(player, 100.0);
api.addAmethysts(player, 10.0);

// Remove currency
boolean success = api.removeCoins(player, 50.0);
boolean success = api.removeAmethysts(player, 5.0);

// Set balance
api.setCoins(player, 1000.0);
api.setAmethysts(player, 100.0);
```

**Check Balance:**
```java
// Check if player has enough funds
boolean hasCoins = api.hasCoins(player, 100.0);
boolean hasAmethysts = api.hasAmethysts(player, 10.0);
```

**Transfer Between Players:**
```java
// Transfer coins
boolean success = api.transferCoins(fromPlayer, toPlayer, 100.0);

// Transfer amethysts
boolean success = api.transferAmethysts(fromPlayer, toPlayer, 10.0);
```

#### Statistics Methods

**Get Statistics:**
```java
// Get player statistics
int kills = api.getKills(player);
int deaths = api.getDeaths(player);
int playtimeSeconds = api.getPlaytimeSeconds(player);
double playtimeHours = api.getPlaytimeHours(player);
double kdRatio = api.getKDRatio(player);

// Get for scoreboard
String killsStr = api.getKillsForScoreboard(player);
String deathsStr = api.getDeathsForScoreboard(player);
String playtimeStr = api.getPlaytimeForScoreboard(player);
String kdStr = api.getKDRatioForScoreboard(player);
```

**Add Statistics:**
```java
// Add statistics
api.addKill(player);
api.addDeath(player);
api.addPlaytime(player, 3600); // 1 hour

// Set statistics
api.setKills(player, 100);
api.setDeaths(player, 50);
api.setPlaytime(player, 7200); // 2 hours
```

#### Utility Methods

```java
// Create player in database if not exists
api.createPlayerIfNotExists(player);

// Get formatted balance
String balance = api.getFormattedBalance(player);

// Get formatted statistics
String stats = api.getFormattedStats(player);

// Check if player exists in database
boolean exists = api.playerExists(player);
```

### Examples

#### Balance Command

```java
@Command(name = "balance", description = "Check balance")
public class BalanceCommand extends Command {
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is for players only!");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Get API
        BladeEconomy plugin = (BladeEconomy) getServer().getPluginManager().getPlugin("BladeEconomy");
        EconomyAPI api = plugin.getEconomyAPI();
        
        if (args.length == 0) {
            // Show own balance
            sender.sendMessage(api.getFormattedBalance(player));
            return true;
        }
        
        // Show other player's balance
        String targetName = args[0];
        Player target = getServer().getPlayer(targetName);
        
        if (target == null) {
            sender.sendMessage("Player not found!");
            return true;
        }
        
        sender.sendMessage(api.getFormattedBalance(target));
        return true;
    }
}
```

#### Pay Command

```java
@Command(name = "pay", description = "Transfer currency to another player")
public class PayCommand extends Command {
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is for players only!");
            return true;
        }
        
        if (args.length != 3) {
            sender.sendMessage("Usage: /pay <player> <type> <amount>");
            return true;
        }
        
        Player from = (Player) sender;
        String targetName = args[0];
        String currencyType = args[1].toLowerCase();
        double amount;
        
        try {
            amount = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid amount!");
            return true;
        }
        
        Player to = getServer().getPlayer(targetName);
        if (to == null) {
            sender.sendMessage("Player not found!");
            return true;
        }
        
        BladeEconomy plugin = (BladeEconomy) getServer().getPluginManager().getPlugin("BladeEconomy");
        EconomyAPI api = plugin.getEconomyAPI();
        
        boolean success = false;
        switch (currencyType) {
            case "coins":
                success = api.transferCoins(from, to, amount);
                break;
            case "amethysts":
                success = api.transferAmethysts(from, to, amount);
                break;
            default:
                sender.sendMessage("Invalid currency type! Use: coins, amethysts");
                return true;
        }
        
        if (success) {
            sender.sendMessage("Transferred " + amount + " " + currencyType + " to " + to.getName());
            to.sendMessage("You received " + amount + " " + currencyType + " from " + from.getName());
        } else {
            sender.sendMessage("Insufficient funds!");
        }
        
        return true;
    }
}
```

#### Scoreboard Integration

```java
public class ScoreboardManager {
    
    public void updateScoreboard(Player player) {
        BladeEconomy plugin = (BladeEconomy) getServer().getPluginManager().getPlugin("BladeEconomy");
        EconomyAPI api = plugin.getEconomyAPI();
        
        // Add lines to scoreboard
        scoreboard.setLine("coins", "Coins: " + api.getCoinsForScoreboard(player));
        scoreboard.setLine("amethysts", "Amethysts: " + api.getAmethystsForScoreboard(player));
        scoreboard.setLine("kills", "Kills: " + api.getKillsForScoreboard(player));
        scoreboard.setLine("deaths", "Deaths: " + api.getDeathsForScoreboard(player));
        scoreboard.setLine("kd", "K/D: " + api.getKDRatioForScoreboard(player));
        scoreboard.setLine("playtime", "Time: " + api.getPlaytimeForScoreboard(player) + "h");
    }
}
```

### Automatic Features

The plugin automatically:
- Creates players in the database on first join
- Tracks playtime on join/quit
- Records kills and deaths
- Updates last join time

### Requirements

- DataManager plugin 

---

## Русский

### Описание

BladeEconomy - это простой и мощный API плагин для экономики и статистики игроков в PowerNukkitX. Предоставляет две валюты (монеты и аметисты) и комплексное отслеживание статистики игроков.

### Возможности

- ✅ Две валюты: монеты и аметисты
- ✅ SQLite база данных с использованием DataManager
- ✅ Статистика игроков (убийства, смерти, время игры)
- ✅ K/D соотношение
- ✅ Автоматический подсчет времени игры
- ✅ Удобное API для интеграции с другими плагинами
- ✅ Поддержка скорборда
- ✅ Автоматическое создание игроков в базе данных

### Установка

1. Скачайте JAR файл плагина
2. Поместите в папку `plugins`
3. Перезапустите сервер
4. Плагин автоматически создаст базу данных и таблицы

### Конфигурация

Файл `config.yml` содержит только необходимые настройки:

```yaml
# Начальный баланс для новых игроков
starting_coins: 100.0
starting_amethysts: 10.0
```

### Использование API

#### Получение API

```java
BladeEconomy plugin = (BladeEconomy) getServer().getPluginManager().getPlugin("BladeEconomy");
EconomyAPI api = plugin.getEconomyAPI();
```

#### Методы экономики

**Получение баланса:**
```java
// Получить количество монет
double coins = api.getCoins(player);

// Получить количество аметистов
double amethysts = api.getAmethysts(player);

// Получить для скорборда (строки)
String coinsStr = api.getCoinsForScoreboard(player);
String amethystsStr = api.getAmethystsForScoreboard(player);
```

**Изменение баланса:**
```java
// Добавить валюту
api.addCoins(player, 100.0);
api.addAmethysts(player, 10.0);

// Убрать валюту
boolean success = api.removeCoins(player, 50.0);
boolean success = api.removeAmethysts(player, 5.0);

// Установить баланс
api.setCoins(player, 1000.0);
api.setAmethysts(player, 100.0);
```

**Проверка баланса:**
```java
// Проверить наличие средств
boolean hasCoins = api.hasCoins(player, 100.0);
boolean hasAmethysts = api.hasAmethysts(player, 10.0);
```

**Переводы между игроками:**
```java
// Перевести монеты
boolean success = api.transferCoins(fromPlayer, toPlayer, 100.0);

// Перевести аметисты
boolean success = api.transferAmethysts(fromPlayer, toPlayer, 10.0);
```

#### Методы статистики

**Получение статистики:**
```java
// Получить статистику игрока
int kills = api.getKills(player);
int deaths = api.getDeaths(player);
int playtimeSeconds = api.getPlaytimeSeconds(player);
double playtimeHours = api.getPlaytimeHours(player);
double kdRatio = api.getKDRatio(player);

// Получить для скорборда
String killsStr = api.getKillsForScoreboard(player);
String deathsStr = api.getDeathsForScoreboard(player);
String playtimeStr = api.getPlaytimeForScoreboard(player);
String kdStr = api.getKDRatioForScoreboard(player);
```

**Добавление статистики:**
```java
// Добавить статистику
api.addKill(player);
api.addDeath(player);
api.addPlaytime(player, 3600); // 1 час

// Установить статистику
api.setKills(player, 100);
api.setDeaths(player, 50);
api.setPlaytime(player, 7200); // 2 часа
```

#### Утилиты

```java
// Создать игрока в базе если его нет
api.createPlayerIfNotExists(player);

// Получить форматированный баланс
String balance = api.getFormattedBalance(player);

// Получить форматированную статистику
String stats = api.getFormattedStats(player);

// Проверить существование игрока в базе
boolean exists = api.playerExists(player);
```

### Примеры

#### Команда /balance

```java
@Command(name = "balance", description = "Проверить баланс")
public class BalanceCommand extends Command {
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эта команда только для игроков!");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Получаем API
        BladeEconomy plugin = (BladeEconomy) getServer().getPluginManager().getPlugin("BladeEconomy");
        EconomyAPI api = plugin.getEconomyAPI();
        
        if (args.length == 0) {
            // Показать свой баланс
            sender.sendMessage(api.getFormattedBalance(player));
            return true;
        }
        
        // Показать баланс другого игрока
        String targetName = args[0];
        Player target = getServer().getPlayer(targetName);
        
        if (target == null) {
            sender.sendMessage("Игрок не найден!");
            return true;
        }
        
        sender.sendMessage(api.getFormattedBalance(target));
        return true;
    }
}
```

#### Команда /pay

```java
@Command(name = "pay", description = "Перевести валюту другому игроку")
public class PayCommand extends Command {
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эта команда только для игроков!");
            return true;
        }
        
        if (args.length != 3) {
            sender.sendMessage("Использование: /pay <игрок> <тип> <количество>");
            return true;
        }
        
        Player from = (Player) sender;
        String targetName = args[0];
        String currencyType = args[1].toLowerCase();
        double amount;
        
        try {
            amount = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Неверное количество!");
            return true;
        }
        
        Player to = getServer().getPlayer(targetName);
        if (to == null) {
            sender.sendMessage("Игрок не найден!");
            return true;
        }
        
        BladeEconomy plugin = (BladeEconomy) getServer().getPluginManager().getPlugin("BladeEconomy");
        EconomyAPI api = plugin.getEconomyAPI();
        
        boolean success = false;
        switch (currencyType) {
            case "coins":
                success = api.transferCoins(from, to, amount);
                break;
            case "amethysts":
                success = api.transferAmethysts(from, to, amount);
                break;
            default:
                sender.sendMessage("Неверный тип валюты! Используйте: coins, amethysts");
                return true;
        }
        
        if (success) {
            sender.sendMessage("Переведено " + amount + " " + currencyType + " игроку " + to.getName());
            to.sendMessage("Вам переведено " + amount + " " + currencyType + " от " + from.getName());
        } else {
            sender.sendMessage("Недостаточно средств!");
        }
        
        return true;
    }
}
```

#### Интеграция со скорбордом

```java
public class ScoreboardManager {
    
    public void updateScoreboard(Player player) {
        BladeEconomy plugin = (BladeEconomy) getServer().getPluginManager().getPlugin("BladeEconomy");
        EconomyAPI api = plugin.getEconomyAPI();
        
        // Добавить строки в скорборд
        scoreboard.setLine("coins", "Монеты: " + api.getCoinsForScoreboard(player));
        scoreboard.setLine("amethysts", "Аметисты: " + api.getAmethystsForScoreboard(player));
        scoreboard.setLine("kills", "Убийства: " + api.getKillsForScoreboard(player));
        scoreboard.setLine("deaths", "Смерти: " + api.getDeathsForScoreboard(player));
        scoreboard.setLine("kd", "K/D: " + api.getKDRatioForScoreboard(player));
        scoreboard.setLine("playtime", "Время: " + api.getPlaytimeForScoreboard(player) + "ч");
    }
}
```

### Автоматические функции

Плагин автоматически:
- Создает игроков в базе данных при первом входе
- Подсчитывает время игры при входе/выходе
- Отслеживает убийства и смерти
- Обновляет время последнего входа

### Требования

- DataManager плагин

### Лицензия


MIT License 
