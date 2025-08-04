package usa.liez;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import me.hteppl.data.database.SQLiteDatabase;
import org.sql2o.Connection;

/**
 * Класс для работы с базой данных экономики используя DataManager
 */
public class EconomyDatabase extends SQLiteDatabase {
    
    private final BladeEconomy plugin;
    
    public EconomyDatabase(BladeEconomy plugin) {
        super("economy");
        this.plugin = plugin;
        
        // Создаем таблицу при инициализации
        createTables();
    }
    
    private void createTables() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS player_economy (
                uuid TEXT PRIMARY KEY,
                player_name TEXT NOT NULL,
                coins REAL DEFAULT 0.0,
                amethysts REAL DEFAULT 0.0,
                kills INTEGER DEFAULT 0,
                deaths INTEGER DEFAULT 0,
                playtime_seconds INTEGER DEFAULT 0,
                first_join TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                last_join TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        this.executeScheme(createTableSQL);
    }
    
    public void createPlayerIfNotExists(Player player) {
        String sql = "INSERT OR IGNORE INTO player_economy (uuid, player_name) VALUES (:uuid, :name)";
        
        try (Connection connection = this.openConnection()) {
            connection.createQuery(sql)
                .addParameter("uuid", player.getUniqueId().toString())
                .addParameter("name", player.getName())
                .executeUpdate();
        } catch (Exception e) {
            plugin.getLogger().error(TextFormat.RED + "Ошибка при создании игрока в базе данных: " + e.getMessage());
        }
    }
    
    public double getCoins(Player player) {
        String sql = "SELECT coins FROM player_economy WHERE uuid = :uuid";
        
        try (Connection connection = this.openConnection()) {
            return connection.createQuery(sql)
                .addParameter("uuid", player.getUniqueId().toString())
                .executeScalar(Double.class);
        } catch (Exception e) {
            plugin.getLogger().error(TextFormat.RED + "Ошибка при получении монет: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    public double getAmethysts(Player player) {
        String sql = "SELECT amethysts FROM player_economy WHERE uuid = :uuid";
        
        try (Connection connection = this.openConnection()) {
            return connection.createQuery(sql)
                .addParameter("uuid", player.getUniqueId().toString())
                .executeScalar(Double.class);
        } catch (Exception e) {
            plugin.getLogger().error(TextFormat.RED + "Ошибка при получении аметистов: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    public void setCoins(Player player, double amount) {
        String sql = "UPDATE player_economy SET coins = :amount WHERE uuid = :uuid";
        
        try (Connection connection = this.openConnection()) {
            connection.createQuery(sql)
                .addParameter("amount", amount)
                .addParameter("uuid", player.getUniqueId().toString())
                .executeUpdate();
        } catch (Exception e) {
            plugin.getLogger().error(TextFormat.RED + "Ошибка при установке монет: " + e.getMessage());
        }
    }
    
    public void setAmethysts(Player player, double amount) {
        String sql = "UPDATE player_economy SET amethysts = :amount WHERE uuid = :uuid";
        
        try (Connection connection = this.openConnection()) {
            connection.createQuery(sql)
                .addParameter("amount", amount)
                .addParameter("uuid", player.getUniqueId().toString())
                .executeUpdate();
        } catch (Exception e) {
            plugin.getLogger().error(TextFormat.RED + "Ошибка при установке аметистов: " + e.getMessage());
        }
    }
    
    public void addCoins(Player player, double amount) {
        double currentCoins = getCoins(player);
        setCoins(player, currentCoins + amount);
    }
    
    public void addAmethysts(Player player, double amount) {
        double currentAmethysts = getAmethysts(player);
        setAmethysts(player, currentAmethysts + amount);
    }
    
    public void removeCoins(Player player, double amount) {
        double currentCoins = getCoins(player);
        if (currentCoins >= amount) {
            setCoins(player, currentCoins - amount);
        }
    }
    
    public void removeAmethysts(Player player, double amount) {
        double currentAmethysts = getAmethysts(player);
        if (currentAmethysts >= amount) {
            setAmethysts(player, currentAmethysts - amount);
        }
    }
    
    public boolean hasCoins(Player player, double amount) {
        return getCoins(player) >= amount;
    }
    
    public boolean hasAmethysts(Player player, double amount) {
        return getAmethysts(player) >= amount;
    }
    
    public void updateLastJoin(Player player) {
        String sql = "UPDATE player_economy SET last_join = CURRENT_TIMESTAMP WHERE uuid = :uuid";
        
        try (Connection connection = this.openConnection()) {
            connection.createQuery(sql)
                .addParameter("uuid", player.getUniqueId().toString())
                .executeUpdate();
        } catch (Exception e) {
            plugin.getLogger().error(TextFormat.RED + "Ошибка при обновлении времени последнего входа: " + e.getMessage());
        }
    }
    
    // Методы для работы со статистикой
    
    public int getKills(Player player) {
        String sql = "SELECT kills FROM player_economy WHERE uuid = :uuid";
        
        try (Connection connection = this.openConnection()) {
            return connection.createQuery(sql)
                .addParameter("uuid", player.getUniqueId().toString())
                .executeScalar(Integer.class);
        } catch (Exception e) {
            plugin.getLogger().error(TextFormat.RED + "Ошибка при получении убийств: " + e.getMessage());
        }
        
        return 0;
    }
    
    public int getDeaths(Player player) {
        String sql = "SELECT deaths FROM player_economy WHERE uuid = :uuid";
        
        try (Connection connection = this.openConnection()) {
            return connection.createQuery(sql)
                .addParameter("uuid", player.getUniqueId().toString())
                .executeScalar(Integer.class);
        } catch (Exception e) {
            plugin.getLogger().error(TextFormat.RED + "Ошибка при получении смертей: " + e.getMessage());
        }
        
        return 0;
    }
    
    public int getPlaytimeSeconds(Player player) {
        String sql = "SELECT playtime_seconds FROM player_economy WHERE uuid = :uuid";
        
        try (Connection connection = this.openConnection()) {
            return connection.createQuery(sql)
                .addParameter("uuid", player.getUniqueId().toString())
                .executeScalar(Integer.class);
        } catch (Exception e) {
            plugin.getLogger().error(TextFormat.RED + "Ошибка при получении времени игры: " + e.getMessage());
        }
        
        return 0;
    }
    
    public void addKill(Player player) {
        String sql = "UPDATE player_economy SET kills = kills + 1 WHERE uuid = :uuid";
        
        try (Connection connection = this.openConnection()) {
            connection.createQuery(sql)
                .addParameter("uuid", player.getUniqueId().toString())
                .executeUpdate();
        } catch (Exception e) {
            plugin.getLogger().error(TextFormat.RED + "Ошибка при добавлении убийства: " + e.getMessage());
        }
    }
    
    public void addDeath(Player player) {
        String sql = "UPDATE player_economy SET deaths = deaths + 1 WHERE uuid = :uuid";
        
        try (Connection connection = this.openConnection()) {
            connection.createQuery(sql)
                .addParameter("uuid", player.getUniqueId().toString())
                .executeUpdate();
        } catch (Exception e) {
            plugin.getLogger().error(TextFormat.RED + "Ошибка при добавлении смерти: " + e.getMessage());
        }
    }
    
    public void addPlaytime(Player player, int seconds) {
        String sql = "UPDATE player_economy SET playtime_seconds = playtime_seconds + :seconds WHERE uuid = :uuid";
        
        try (Connection connection = this.openConnection()) {
            connection.createQuery(sql)
                .addParameter("seconds", seconds)
                .addParameter("uuid", player.getUniqueId().toString())
                .executeUpdate();
        } catch (Exception e) {
            plugin.getLogger().error(TextFormat.RED + "Ошибка при обновлении времени игры: " + e.getMessage());
        }
    }
    
    public void setKills(Player player, int kills) {
        String sql = "UPDATE player_economy SET kills = :kills WHERE uuid = :uuid";
        
        try (Connection connection = this.openConnection()) {
            connection.createQuery(sql)
                .addParameter("kills", kills)
                .addParameter("uuid", player.getUniqueId().toString())
                .executeUpdate();
        } catch (Exception e) {
            plugin.getLogger().error(TextFormat.RED + "Ошибка при установке убийств: " + e.getMessage());
        }
    }
    
    public void setDeaths(Player player, int deaths) {
        String sql = "UPDATE player_economy SET deaths = :deaths WHERE uuid = :uuid";
        
        try (Connection connection = this.openConnection()) {
            connection.createQuery(sql)
                .addParameter("deaths", deaths)
                .addParameter("uuid", player.getUniqueId().toString())
                .executeUpdate();
        } catch (Exception e) {
            plugin.getLogger().error(TextFormat.RED + "Ошибка при установке смертей: " + e.getMessage());
        }
    }
    
    public void setPlaytime(Player player, int seconds) {
        String sql = "UPDATE player_economy SET playtime_seconds = :seconds WHERE uuid = :uuid";
        
        try (Connection connection = this.openConnection()) {
            connection.createQuery(sql)
                .addParameter("seconds", seconds)
                .addParameter("uuid", player.getUniqueId().toString())
                .executeUpdate();
        } catch (Exception e) {
            plugin.getLogger().error(TextFormat.RED + "Ошибка при установке времени игры: " + e.getMessage());
        }
    }
} 