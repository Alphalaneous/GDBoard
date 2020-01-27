package Main;
import com.registry.RegDWORDValue;
import com.registry.RegistryKey;

public class GetTheme extends Thread {

	public void run() {
		
		RegistryKey personalizeStart = new RegistryKey(
				"Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize");

		int prevTheme = ((RegDWORDValue) personalizeStart.getValue("AppsUseLightTheme")).getIntValue();
		
		if(prevTheme == 0) {
			Defaults.setDark();
		}
		else if (prevTheme == 1) {
			Defaults.setLight();
		}

		while (true) {

			RegistryKey personalize = new RegistryKey(
					"Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize");

			int theme = ((RegDWORDValue) personalize.getValue("AppsUseLightTheme")).getIntValue();
			if(theme == 0 && prevTheme == 1) {
				Defaults.setDark();
				prevTheme = 0;
			}
			else if (theme == 1 && prevTheme == 0) {
				Defaults.setLight();
				prevTheme = 1;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
