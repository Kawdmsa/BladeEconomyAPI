package usa.liez;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BladeEconomy extends PluginBase implements Listener {
    
    private static BladeEconomy instance;
    private EconomyAPI economyAPI;
    private EconomyDatabase database;
    private Config config;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Создаем папки
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        
        // Загружаем конфигурацию
        saveDefaultConfig();
        config = getConfig();
        
        // Инициализируем базу данных
        database = new EconomyDatabase(this);
        
        // Инициализируем API
        economyAPI = new EconomyAPI(this);
        
        // Регистрируем события
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new EconomyEvents(this), this);
        
        // Регистрируем команды
        registerCommands();
        
        getLogger().info(TextFormat.GREEN + "BladeEconomy успешно загружен!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info(TextFormat.RED + "BladeEconomy выгружен!");
    }
    
    private void registerCommands() {
        // Команды убраны - используйте API для создания собственных команд
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        database.createPlayerIfNotExists(player);
    }
    
    public static BladeEconomy getInstance() {
        return instance;
    }
    
    public EconomyAPI getEconomyAPI() {
        return economyAPI;
    }
    
    public EconomyDatabase getDatabase() {
        return database;
    }
    
    public Config getPluginConfig() {
        return config;
    }
}