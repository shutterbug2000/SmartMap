package spishu.plugin.smartmap;

import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;
import org.bukkit.plugin.java.JavaPlugin;

public class SmartMap extends JavaPlugin implements Listener {
    
    static MapRenderer MAP_RENDERER;
    static ShapedRecipe MAP_RECIPE;
    static ItemStack MAP_ITEM;
    
    static {
    	
    	MAP_RENDERER = new MapRenderer(){
    		public void render(MapView view, MapCanvas canvas, Player player) {
    			canvas.drawText(0, 0, MinecraftFont.Font, "Welcome to SmartMap OS!\n\n V0.0.1 Beta!");
    		}
    	};
    	
    	MAP_ITEM = new ItemStack(Material.MAP, 1);
		ItemMeta meta = MAP_ITEM.getItemMeta();
		meta.setLore(Arrays.asList("SmartMap", "0.0.1b"));
		MAP_ITEM.setItemMeta(meta);
		
		//Add custom recipe.
        MAP_RECIPE = new ShapedRecipe(MAP_ITEM);
        MAP_RECIPE.shape(
                        "GCG",
                        "RMR",
                        "GDG");
        MAP_RECIPE.setIngredient('M', Material.EMPTY_MAP);
        MAP_RECIPE.setIngredient('R', Material.DIODE);
        MAP_RECIPE.setIngredient('C', Material.REDSTONE_COMPARATOR);
        MAP_RECIPE.setIngredient('D', Material.DIAMOND);
        MAP_RECIPE.setIngredient('G', Material.GOLD_INGOT);
        
    }

	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().addRecipe(MAP_RECIPE); //Set up the recipe after onEnable so we know the server is entirely loaded.
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onItemCraft(CraftItemEvent event) {
		getLogger().log(Level.INFO, "Detected craftitemevent for %1s", event.getWhoClicked().getName());
		if(event.getCurrentItem().equals(MAP_ITEM)) {
			getLogger().log(Level.INFO, "%1s crafted a map.", event.getWhoClicked().getName());
			ItemStack mapItem = event.getCurrentItem();
			MapView mapView = Bukkit.getServer().createMap(event.getWhoClicked().getWorld());
			mapView.getRenderers().clear();
			mapView.addRenderer(MAP_RENDERER);
			mapItem.setDurability(mapView.getId());
		}
	}
	
}