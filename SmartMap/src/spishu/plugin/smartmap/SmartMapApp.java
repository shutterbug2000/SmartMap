package spishu.plugin.smartmap;

import org.bukkit.map.MapRenderer;

public abstract class SmartMapApp extends MapRenderer {
	
	double cost;
	SmartMapPlugin host;
	
	void handleTouchPress(){}
	
	void handleTouchRelease(){}
	
	void handleTouchHold(){}

	public SmartMapApp(double cost, SmartMapPlugin host) {
		this.cost = cost;
		this.host = host;
	}
	
}