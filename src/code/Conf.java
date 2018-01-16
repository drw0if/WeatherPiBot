package code;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Conf {

	static File conf;
	
	public static String[] getConf() {
		
		//Controlla che il file conf esista, altrimenti lo crea
		if(!conf.exists()) {
			createConf();
			System.exit(1);
		}
		
		//Leggo l'intero file e lo metto in confText
		String confText = readConf();
		
		//Splitto l'intero file in base all'escape, ho ogni rigo in una casella diversa del vettore
		String confRow[] = confText.split("\n");
		
		if(confRow.length < 2) {
			createConf();
			System.exit(1);
		}
		
		ArrayList<String> argsRaw = new ArrayList<String>();
		
		//Leggo username e token del bot, in quanto secondo elemento dopo lo split per "="
		for(int i = 0; i<2; i++) {
			String app[] = confRow[i].split("=");
			try {
				argsRaw.add(app[1]);
			}
			catch (Exception e){
				if(i==0)
					System.out.println("The right form is: Username=YourUsername");
				else
					System.out.println("The right form is: Token=YourToken");
			}
		}
		
		//Aggiunta della whitelist all'array degli argomenti
		if(confRow.length > 3)
			for(int i = 3; i < confRow.length; i++)
				argsRaw.add(confRow[i]);
		
		return method.listToArray(argsRaw) ;
	}
	
	//Crea il file di configurazione qualora dovesse essere necessario
	private static void createConf() {
		System.out.println("Add username, token and whitelist to the WeatherPi.conf file and re-open the bot.");
		try {
			FileWriter writer = new FileWriter(conf);
			writer.append("Username=usernameBot\n");
			writer.append("Token=tokenBot\n");
			writer.append("Whitelist:(one username for row)\n");
			writer.close();
		} 
		catch (IOException e) {
			System.out.println("It's not possible to create WeatherPi.conf file!");
		}
	}
		
	//Legge tutto il contenuto del file conf
	private static String readConf() {
		String confText = "";
		try {
			FileReader reader = new FileReader(conf);
			
			while(reader.ready())
				confText += (char)reader.read();
			
			reader.close();
		} 
		catch (IOException e) {
			System.out.println("It's not possible to read the conf file!");
		}
		
		return confText;
	}
	
	

}