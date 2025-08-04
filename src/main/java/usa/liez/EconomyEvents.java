package usa.liez;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.event.player.PlayerDeathEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Класс для обработки событий экономики
 */
public class EconomyEvents implements Listener {
    
    private final BladeEconomy plugin;
    private final EconomyAPI economyAPI;
    private final Map<UUID, Long> joinTimes = new HashMap<>();
    
    public EconomyEvents(BladeEconomy plugin) {
        this.plugin = plugin;
        this.economyAPI = plugin.getEconomyAPI();
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Создаем игрока в базе данных если его нет
        economyAPI.createPlayerIfNotExists(player);
        
        // Обновляем время последнего входа
        plugin.getDatabase().updateLastJoin(player);
        
        // Записываем время входа для подсчета времени игры
        joinTimes.put(player.getUniqueId(), System.currentTimeMillis());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        // Подсчитываем время игры
        Long joinTime = joinTimes.remove(player.getUniqueId());
        if (joinTime != null) {
            long playTimeSeconds = (System.currentTimeMillis() - joinTime) / 1000;
            economyAPI.addPlaytime(player, (int) playTimeSeconds);
        }
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        
        // Добавляем смерть игроку
        economyAPI.addDeath(player);
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            
            // Проверяем, был ли игрок убит другим игроком
            if (event.getEntity().getLastDamageCause() != null && 
                event.getEntity().getLastDamageCause().getEntity() instanceof Player) {
                
                Player killer = (Player) event.getEntity().getLastDamageCause().getEntity();
                
                // Добавляем убийство киллеру
                economyAPI.addKill(killer);
            }
        }
    }
} 