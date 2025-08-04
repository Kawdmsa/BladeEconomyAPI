package usa.liez;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;

/**
 * API для работы с экономикой BladeEconomy
 * Предоставляет удобные методы для работы с валютами
 */
public class EconomyAPI {
    
    private final BladeEconomy plugin;
    private final EconomyDatabase database;
    
    public EconomyAPI(BladeEconomy plugin) {
        this.plugin = plugin;
        this.database = plugin.getDatabase();
    }
    
    /**
     * Получить количество монет у игрока
     * @param player Игрок
     * @return Количество монет
     */
    public double getCoins(Player player) {
        if (player == null) return 0.0;
        return database.getCoins(player);
    }
    
    /**
     * Получить количество аметистов у игрока
     * @param player Игрок
     * @return Количество аметистов
     */
    public double getAmethysts(Player player) {
        if (player == null) return 0.0;
        return database.getAmethysts(player);
    }
    
    /**
     * Установить количество монет игроку
     * @param player Игрок
     * @param amount Количество монет
     */
    public void setCoins(Player player, double amount) {
        if (player == null || amount < 0) return;
        database.setCoins(player, amount);
    }
    
    /**
     * Установить количество аметистов игроку
     * @param player Игрок
     * @param amount Количество аметистов
     */
    public void setAmethysts(Player player, double amount) {
        if (player == null || amount < 0) return;
        database.setAmethysts(player, amount);
    }
    
    /**
     * Добавить монеты игроку
     * @param player Игрок
     * @param amount Количество монет для добавления
     */
    public void addCoins(Player player, double amount) {
        if (player == null || amount <= 0) return;
        database.addCoins(player, amount);
    }
    
    /**
     * Добавить аметисты игроку
     * @param player Игрок
     * @param amount Количество аметистов для добавления
     */
    public void addAmethysts(Player player, double amount) {
        if (player == null || amount <= 0) return;
        database.addAmethysts(player, amount);
    }
    
    /**
     * Убрать монеты у игрока
     * @param player Игрок
     * @param amount Количество монет для убирания
     * @return true если операция успешна, false если недостаточно монет
     */
    public boolean removeCoins(Player player, double amount) {
        if (player == null || amount <= 0) return false;
        
        if (hasCoins(player, amount)) {
            database.removeCoins(player, amount);
            return true;
        }
        return false;
    }
    
    /**
     * Убрать аметисты у игрока
     * @param player Игрок
     * @param amount Количество аметистов для убирания
     * @return true если операция успешна, false если недостаточно аметистов
     */
    public boolean removeAmethysts(Player player, double amount) {
        if (player == null || amount <= 0) return false;
        
        if (hasAmethysts(player, amount)) {
            database.removeAmethysts(player, amount);
            return true;
        }
        return false;
    }
    
    /**
     * Проверить, есть ли у игрока достаточно монет
     * @param player Игрок
     * @param amount Требуемое количество монет
     * @return true если у игрока достаточно монет
     */
    public boolean hasCoins(Player player, double amount) {
        if (player == null || amount <= 0) return false;
        return database.hasCoins(player, amount);
    }
    
    /**
     * Проверить, есть ли у игрока достаточно аметистов
     * @param player Игрок
     * @param amount Требуемое количество аметистов
     * @return true если у игрока достаточно аметистов
     */
    public boolean hasAmethysts(Player player, double amount) {
        if (player == null || amount <= 0) return false;
        return database.hasAmethysts(player, amount);
    }
    
    /**
     * Перевести монеты от одного игрока к другому
     * @param from Игрок, от которого переводятся монеты
     * @param to Игрок, которому переводятся монеты
     * @param amount Количество монет для перевода
     * @return true если перевод успешен
     */
    public boolean transferCoins(Player from, Player to, double amount) {
        if (from == null || to == null || amount <= 0) return false;
        
        if (hasCoins(from, amount)) {
            removeCoins(from, amount);
            addCoins(to, amount);
            return true;
        }
        return false;
    }
    
    /**
     * Перевести аметисты от одного игрока к другому
     * @param from Игрок, от которого переводятся аметисты
     * @param to Игрок, которому переводятся аметисты
     * @param amount Количество аметистов для перевода
     * @return true если перевод успешен
     */
    public boolean transferAmethysts(Player from, Player to, double amount) {
        if (from == null || to == null || amount <= 0) return false;
        
        if (hasAmethysts(from, amount)) {
            removeAmethysts(from, amount);
            addAmethysts(to, amount);
            return true;
        }
        return false;
    }
    
    /**
     * Получить форматированную строку баланса игрока
     * @param player Игрок
     * @return Форматированная строка с балансом
     */
    public String getFormattedBalance(Player player) {
        if (player == null) return "";
        
        double coins = getCoins(player);
        double amethysts = getAmethysts(player);
        
        return TextFormat.GOLD + "=== Баланс " + player.getName() + " ===\n" +
               TextFormat.YELLOW + "Монеты: " + TextFormat.WHITE + coins + "\n" +
               TextFormat.LIGHT_PURPLE + "Аметисты: " + TextFormat.WHITE + amethysts;
    }
    
    /**
     * Получить баланс монет для скорборда
     * @param player Игрок
     * @return Строка с балансом монет
     */
    public String getCoinsForScoreboard(Player player) {
        if (player == null) return "0";
        return String.valueOf((int) getCoins(player));
    }
    
    /**
     * Получить баланс аметистов для скорборда
     * @param player Игрок
     * @return Строка с балансом аметистов
     */
    public String getAmethystsForScoreboard(Player player) {
        if (player == null) return "0";
        return String.valueOf((int) getAmethysts(player));
    }
    
    /**
     * Получить общий баланс в монетах (для сравнения)
     * @param player Игрок
     * @return Общий баланс в монетах
     */
    public double getTotalCoins(Player player) {
        if (player == null) return 0.0;
        return getCoins(player);
    }
    
    /**
     * Получить общий баланс в аметистах (для сравнения)
     * @param player Игрок
     * @return Общий баланс в аметистах
     */
    public double getTotalAmethysts(Player player) {
        if (player == null) return 0.0;
        return getAmethysts(player);
    }
    
    /**
     * Проверить, существует ли игрок в базе данных
     * @param player Игрок
     * @return true если игрок существует в базе
     */
    public boolean playerExists(Player player) {
        if (player == null) return false;
        return database.getCoins(player) >= 0; // Простая проверка
    }
    
    /**
     * Создать игрока в базе данных если его нет
     * @param player Игрок
     */
    public void createPlayerIfNotExists(Player player) {
        if (player == null) return;
        database.createPlayerIfNotExists(player);
    }
    
    // ========== МЕТОДЫ ДЛЯ СТАТИСТИКИ ==========
    
    /**
     * Получить количество убийств игрока
     * @param player Игрок
     * @return Количество убийств
     */
    public int getKills(Player player) {
        if (player == null) return 0;
        return database.getKills(player);
    }
    
    /**
     * Получить количество смертей игрока
     * @param player Игрок
     * @return Количество смертей
     */
    public int getDeaths(Player player) {
        if (player == null) return 0;
        return database.getDeaths(player);
    }
    
    /**
     * Получить время игры игрока в секундах
     * @param player Игрок
     * @return Время игры в секундах
     */
    public int getPlaytimeSeconds(Player player) {
        if (player == null) return 0;
        return database.getPlaytimeSeconds(player);
    }
    
    /**
     * Получить время игры в часах
     * @param player Игрок
     * @return Время игры в часах
     */
    public double getPlaytimeHours(Player player) {
        if (player == null) return 0.0;
        return getPlaytimeSeconds(player) / 3600.0;
    }
    
    /**
     * Получить время игры в минутах
     * @param player Игрок
     * @return Время игры в минутах
     */
    public double getPlaytimeMinutes(Player player) {
        if (player == null) return 0.0;
        return getPlaytimeSeconds(player) / 60.0;
    }
    
    /**
     * Добавить убийство игроку
     * @param player Игрок
     */
    public void addKill(Player player) {
        if (player == null) return;
        database.addKill(player);
    }
    
    /**
     * Добавить смерть игроку
     * @param player Игрок
     */
    public void addDeath(Player player) {
        if (player == null) return;
        database.addDeath(player);
    }
    
    /**
     * Добавить время игры игроку
     * @param player Игрок
     * @param seconds Количество секунд
     */
    public void addPlaytime(Player player, int seconds) {
        if (player == null || seconds <= 0) return;
        database.addPlaytime(player, seconds);
    }
    
    /**
     * Установить количество убийств игроку
     * @param player Игрок
     * @param kills Количество убийств
     */
    public void setKills(Player player, int kills) {
        if (player == null || kills < 0) return;
        database.setKills(player, kills);
    }
    
    /**
     * Установить количество смертей игроку
     * @param player Игрок
     * @param deaths Количество смертей
     */
    public void setDeaths(Player player, int deaths) {
        if (player == null || deaths < 0) return;
        database.setDeaths(player, deaths);
    }
    
    /**
     * Установить время игры игроку
     * @param player Игрок
     * @param seconds Время в секундах
     */
    public void setPlaytime(Player player, int seconds) {
        if (player == null || seconds < 0) return;
        database.setPlaytime(player, seconds);
    }
    
    /**
     * Получить K/D соотношение игрока
     * @param player Игрок
     * @return K/D соотношение (убийства/смерти)
     */
    public double getKDRatio(Player player) {
        if (player == null) return 0.0;
        
        int kills = getKills(player);
        int deaths = getDeaths(player);
        
        if (deaths == 0) {
            return kills > 0 ? kills : 0.0;
        }
        
        return (double) kills / deaths;
    }
    
    /**
     * Получить статистику для скорборда
     * @param player Игрок
     * @return Строка с убийствами для скорборда
     */
    public String getKillsForScoreboard(Player player) {
        if (player == null) return "0";
        return String.valueOf(getKills(player));
    }
    
    /**
     * Получить статистику для скорборда
     * @param player Игрок
     * @return Строка со смертями для скорборда
     */
    public String getDeathsForScoreboard(Player player) {
        if (player == null) return "0";
        return String.valueOf(getDeaths(player));
    }
    
    /**
     * Получить время игры для скорборда
     * @param player Игрок
     * @return Строка с временем игры в часах
     */
    public String getPlaytimeForScoreboard(Player player) {
        if (player == null) return "0";
        return String.format("%.1f", getPlaytimeHours(player));
    }
    
    /**
     * Получить K/D для скорборда
     * @param player Игрок
     * @return Строка с K/D соотношением
     */
    public String getKDRatioForScoreboard(Player player) {
        if (player == null) return "0.0";
        return String.format("%.2f", getKDRatio(player));
    }
    
    /**
     * Получить форматированную статистику игрока
     * @param player Игрок
     * @return Форматированная строка со статистикой
     */
    public String getFormattedStats(Player player) {
        if (player == null) return "";
        
        int kills = getKills(player);
        int deaths = getDeaths(player);
        double kdRatio = getKDRatio(player);
        double playtimeHours = getPlaytimeHours(player);
        
        return TextFormat.GOLD + "=== Статистика " + player.getName() + " ===\n" +
               TextFormat.GREEN + "Убийства: " + TextFormat.WHITE + kills + "\n" +
               TextFormat.RED + "Смерти: " + TextFormat.WHITE + deaths + "\n" +
               TextFormat.BLUE + "K/D: " + TextFormat.WHITE + String.format("%.2f", kdRatio) + "\n" +
               TextFormat.YELLOW + "Время игры: " + TextFormat.WHITE + String.format("%.1f", playtimeHours) + " ч";
    }
} 