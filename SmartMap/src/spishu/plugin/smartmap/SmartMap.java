package spishu.plugin.smartmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

public class SmartMap extends JavaPlugin implements Listener {

    static ItemStack map;

	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
	    setUpMap(); //Set up the map after onEnable so we know the server is entirely loaded.
	}
	
	static void setUpMap() {
        
		MapView smartMapView = Bukkit.createMap(Bukkit.getServer().getWorlds().get(0));
		
		for(MapRenderer r : smartMapView.getRenderers()){
			smartMapView.removeRenderer(r);
			
		smartMapView.addRenderer(new MapRenderer(){
			public void render(MapView view, MapCanvas canvas, Player player) {
				canvas.drawText(66, 66, MinecraftFont.Font, "Welcome to SmartMap OS!\n\n V0.0.1 Beta!");
			}});
		}
		
		//Create actual itemstack.
		map = new ItemStack(Material.MAP, 1, smartMapView.getId());
		ItemMeta meta = map.getItemMeta();
		meta.setLore(Arrays.asList("SmartMap", "0.0.1b"));
		map.setItemMeta(meta);
		
		//Add custom recipe.
        ShapedRecipe mapRecipe = new ShapedRecipe(map);
        mapRecipe.shape(
                        "GCG",
                        "RMR",
                        "GDG");
        mapRecipe.setIngredient('M', Material.EMPTY_MAP);
        mapRecipe.setIngredient('R', Material.DIODE);
        mapRecipe.setIngredient('C', Material.REDSTONE_COMPARATOR);
        mapRecipe.setIngredient('D', Material.DIAMOND);
        mapRecipe.setIngredient('G', Material.GOLD_INGOT);
        Bukkit.getServer().addRecipe(mapRecipe);

	}
	
/*	@EventHandler
	public void onMapInitialize(MapInitializeEvent event){
		System.out.println("Map Init");
		for (final Player player : Bukkit.getOnlinePlayers())
		{
			
			Inventory inv = player.getInventory();
			
			for (ItemStack is : inv.getContents())
			{
				
				System.out.println(is.equals(map));
				if (is.equals(map))
				{
					MapView map = event.getMap();
					for(MapRenderer r : map.getRenderers()){
						map.removeRenderer(r);
						
					map.addRenderer(new MapRenderer(){
						public void render(MapView view, MapCanvas canvas, Player player) {
							canvas.drawText(66, 66, MinecraftFont.Font, "Welcome to SmartMap OS!\n\n V0.0.1 Beta!");
						}});
					}
				}
			}
		}
	}*/
}