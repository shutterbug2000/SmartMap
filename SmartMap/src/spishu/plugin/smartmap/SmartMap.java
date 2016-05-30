package spishu.plugin.smartmap;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;
import org.bukkit.plugin.java.JavaPlugin;

public class SmartMap extends JavaPlugin implements Listener {
    
    static MapRenderer renderer;
    static ShapedRecipe recipe;
    
    static {
    	
    	renderer = new MapRenderer(){
    		public void render(MapView view, MapCanvas canvas, Player player) {
    			canvas.drawText(66, 66, MinecraftFont.Font, "Welcome to SmartMap OS!\n\n V0.0.1 Beta!");
    		}
    	};
    	
    	ItemStack map = new ItemStack(Material.MAP, 1);
		ItemMeta meta = map.getItemMeta();
		meta.setLore(Arrays.asList("SmartMap", "0.0.1b"));
		map.setItemMeta(meta);
		
		//Add custom recipe.
        recipe = new ShapedRecipe(map);
        recipe.shape(
                        "GCG",
                        "RMR",
                        "GDG");
        recipe.setIngredient('M', Material.EMPTY_MAP);
        recipe.setIngredient('R', Material.DIODE);
        recipe.setIngredient('C', Material.REDSTONE_COMPARATOR);
        recipe.setIngredient('D', Material.DIAMOND);
        recipe.setIngredient('G', Material.GOLD_INGOT);
        
    }

	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().addRecipe(recipe); //Set up the recipe after onEnable so we know the server is entirely loaded.
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