package sexy.criss.game.prison.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.WeatherChangeEvent;
import sexy.criss.game.prison.listener.manager.SexyListener;

public class WorldHandler extends SexyListener {

    @EventHandler
    public void onWeather(WeatherChangeEvent e) {
        e.getWorld().setTime(1000);
        e.setCancelled(true);
    }

    @Override
    public String getName() {
        return "World Listener";
    }

    @Override
    public String getType() {
        return "world";
    }
}
