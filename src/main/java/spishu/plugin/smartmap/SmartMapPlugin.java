package spishu.plugin.smartmap;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;

public class SmartMapPlugin extends JavaPlugin implements Listener {
    
    static MapRenderer MAP_RENDERER;
    static ShapedRecipe MAP_RECIPE;
    static ItemStack MAP_ITEM;
    static Gson GSON;
    
    //Map<String,SmartMapApp> apps;
    Map<String,SmartMapApp> apps;
    List<Integer> maps;
    static BufferedImage chip8; //FOR TESTING ONLY
    static BufferedImage select;
    static String currentApp = "";
    
    {
    	
    	MAP_RENDERER = new MapRenderer(){
    		public void render(MapView view, MapCanvas canvas, Player player) {
    			canvas.drawText(10, 0, MinecraftFont.Font, "Welcome to SmartMap OS!\n\n          V0.0.1 Beta!");
    			canvas.drawText(20, 40, MinecraftFont.Font, "Applications:");
    			for(String name : apps.keySet()) { //TODO: Implement the app listing
    				canvas.drawImage(0,50,chip8);
    				currentApp = name;
    			}
    			canvas.drawImage(0,50,chip8);
    			canvas.drawImage(32, 82,select);
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
        
        GSON = new Gson();
        
    }

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onItemCraft(CraftItemEvent event) {
		if(event.getCurrentItem().equals(MAP_ITEM)) {
			ItemStack mapItem = event.getCurrentItem();
			MapView map = Bukkit.getServer().createMap(getServer().getWorlds().get(0)); //Arbitrary world
			clearRenderers(map);
			mapItem.setDurability(map.getId());
			map.addRenderer(MAP_RENDERER);
		}
	}
	
	@EventHandler 
	public void playerInteract(PlayerInteractEvent e){
	  //this will be called automatically by bukkit whenever a player interacts
	  if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)){
		  if(e.getPlayer().isSneaking()){
		  //if(args.length < 1) sender.sendMessage("Invalid number of arguments.");
			//else {
				//if(!(sender instanceof Player)) sender.sendMessage("Only players may use this command.");
				//else {
					if(!apps.containsKey(currentApp)) e.getPlayer().sendMessage("The app specified does not exist.");
					else {
						Player player = e.getPlayer();
						ItemStack mapItem = player.getInventory().getItemInMainHand();
						if(mapItem.getType() != Material.MAP) e.getPlayer().sendMessage("You must be holding a map.");
						else switchApp(mapItem, currentApp);
					//}
				//}
					}
			}
	  }
	}
	
	//TODO API for filesystem
	public void onEnable() {
		try {
			chip8 = ImageIO.read(new File(this.getDataFolder().getPath() + "/Chip8.png")); //FOR TESTING ONLY
			select = ImageIO.read(new File(this.getDataFolder().getPath() + "/select.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getServer().getPluginManager().registerEvents(this, this);
		getServer().addRecipe(MAP_RECIPE); //Set up the recipe after onEnable so we know the server is entirely loaded.
		apps = new HashMap<String,SmartMapApp>(); //Initialize app map
		
		/*TODO map cache
		File dir = getDataFolder(), mapCacheFile = new File(dir, "map-cache.json");
		dir.mkdir();
		if(!mapCacheFile.exists()) {
			try {
				java.nio.file.Files.copy(getClass().getResourceAsStream("res/map-cache.json"), mapCacheFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		*/

	}

	public void registerApp(String name, SmartMapApp app) {
		getLogger().log(Level.INFO, "Registered app %1s", name);
		apps.put(name, app);
	}
	
	@SuppressWarnings("deprecation")
	public void switchApp(ItemStack mapItem, SmartMapApp app) {
		MapView map = getServer().getMap(mapItem.getDurability());
		clearRenderers(map);
		map.addRenderer(app);
	}
	
	public void switchApp(ItemStack mapItem, String appName) {
		switchApp(mapItem, apps.get(appName));
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("switchapp")) {
			if(args.length < 1) sender.sendMessage("Invalid number of arguments.");
			else {
				if(!(sender instanceof Player)) sender.sendMessage("Only players may use this command.");
				else {
					if(!apps.containsKey(args[0])) sender.sendMessage("The app specified does not exist.");
					else {
						Player player = (Player) sender;
						ItemStack mapItem = player.getInventory().getItemInMainHand();
						if(mapItem.getType() != Material.MAP) sender.sendMessage("You must be holding a map.");
						else switchApp(mapItem, args[0]);
					}
				}
			}
		} else if(cmd.getName().equalsIgnoreCase("listapps")) {
			for(String name : apps.keySet()) {
				sender.sendMessage(name);
			}
		} else if(cmd.getName().equalsIgnoreCase("returntohome")) {
			ItemStack mapItem = ((Player) sender).getItemInHand();
			MapView map = Bukkit.getServer().createMap(getServer().getWorlds().get(0)); //Arbitrary world
			clearRenderers(map);
			mapItem.setDurability(map.getId());
			map.addRenderer(MAP_RENDERER);
		}
		return true; 
	}

	
	static void clearRenderers(MapView map) {
		for(MapRenderer renderer : map.getRenderers()) {
			map.removeRenderer(renderer);
		}
	}
	
}