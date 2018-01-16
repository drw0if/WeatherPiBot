package code;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class method {
	static String List = "\"List\": list of the possible operations\n"
						+"\"Hi\": ping from the weather station\n"
						+"\"Ip\": global ip of the machine\n"
						+"\"Temperature/Temp\": CPU and case temps\n"
						+ "\"Clear\": clean all the backup and data files\n"
						+ "\"Uptime\": reads the uptime of the system";
	
	static String Start = "Welcome in the Telegram Control Bot of the WeatherPi "
						+ "coded by DrWolf_ (wolf.432118@gmail.com).\n";
	
	static final String DEFAULT_CONF = "WeatherPiBot.conf";
	static final String temperatureSensorPath = "/sys/bus/w1/devices/";
	
//***Metodi per il bot***
	
	//Legge l'ip remoto della macchina
	public static String getRemoteIp() {
		URL whatismyip;
		String ip;
		try {
			whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			ip = in.readLine();
		} 
		catch (MalformedURLException e) {
			ip = "Error";
		} 
		catch (IOException e) {
			ip = "Error";
		}
		
		return ip;
	}
	
	//Restituisce le due temperature
	public static String getTemperatures() {
		
		return 	"CPU: " + CPUTemp() + "\n"
				+ "Case: " + caseTemp();
	}
	
	//Pulisce i backup
	public static void clearBackup() {
		//Cancello i file nella cartella data
		deleteInDirectory(new File("/home/pi/CumulusMX/data/"));
		//Cancello i file nella cartella backup
		deleteInDirectory(new File("/home/pi/CumulusMX/backup/"));	
	}
	
	//Legge l'uptime del sistema
	public static String upTime() {
		return execProcess("uptime", "It's not possible to read the uptime");
	}
	
//***Metodi generali***
	
	//Restituisce true se la stringa passata e' contenuta nell'array passato
	public static boolean contains(String value, String...array) {
		
		for(int i = 0; i < array.length; i++)
			if(array[i].equals(value))
				return true;
		return false;
	}

	//Calcola la whitelist dagli argomenti passati
	public static String[] getWhiteList(String args[]) {
		
		if(args.length == 2)
			return new String[0];
		
		String[] whiteList = new String[args.length-2];
		for(int i = 2; i < args.length; i++ )
			whiteList[i-2] = args[i];
		
		return whiteList;
	}

	//Converte un arrayList di stringhe in un array
	public static String[] listToArray(ArrayList<String> argsRaw) {
				
		String args[] = new String[argsRaw.size()];
			
		for(int i = 0; i<args.length; i++)
			args[i] = argsRaw.get(i);
		
		 return args;
	}
	
	//Elimina i file in una directory passata preservando la directory
	private static void deleteInDirectory(File directory) {
		//Prendo la lista dei file nella cartella
		String dataList[] = directory.list();
		
		//Cancello i file nella cartella
		for(String listElement : dataList)
			deleteDirectory(new File(directory.getAbsolutePath() + "/" + listElement));
	}
	
	//Cancella i file presenti in directory che contengono contained
	private static void deleteDirectory(File directory){
		
		//Mi assicuro che il file in questo caso sia una directory
		if(directory.isDirectory()) {
			//Leggo la lista dei file presenti nella directory
			String dirList[] = directory.list();
			
			//Per ogni file nella directory chiamo questa funzione
			for(String dirName : dirList)
				deleteDirectory(new File(directory.getAbsolutePath() + "/" + dirName));	
		}
		//Se non � una directory o se � stata svuotata, cancella questa directory
		directory.delete();
	}
	
	//Esegue un comando passato come primo parametro e restituisce il risultato di questo, altrimenti restituisce la stringa di errore
	private static String execProcess(String command, String error) {	 
		try {
			//Eseguo il process
			Process uptime = Runtime.getRuntime().exec(command);
			//Dichiaro una variabile di appoggio
			int app = 0;
			//Dichiaro una variabile in cui mettere quanto letto dal process
			String readed = "";
			//Apro il flusso di input dal processo
			InputStream lettore = uptime.getInputStream();
			//Leggo ed immagazzino in read
	        while((app = lettore.read()) != -1)
	        	readed += (char) app;
	        
	        //Ritorno la stringa letta
	        return readed;
		} 
		catch (IOException e) {
			return error;
		}

	}
	
	//Ottiene la temperatura della CPU in forma di stringa
	private static String CPUTemp() {
		String readed = execProcess("vcgencmd measure_temp", "Error while getting the CPU temperature!");
			
		//Creo una stringa in cui mettere i cast dei byte
		String temperature = "";
		//costruisco la stringa castando il buffer di byte
		for(int i = 5; i < 9; i++)
			temperature += (char) readed.charAt(i);
			
		//Ritorno la stringa
		return temperature;
	}
	
	//Ottiene la temperatura dal sensore di temperatura
	private static String caseTemp() {
		
		String[] fileList = new File(temperatureSensorPath).list();
		String sensorDir = "";
		
		for(int i = 0; i < fileList.length; i++)
			if(fileList[i].contains("28-"))
				sensorDir = temperatureSensorPath + fileList[i];
		
		String temperatureFile = execProcess("cat " + sensorDir + "/w1_slave", "Error while getting the case temperature!");
		
		String[] temperatureRaw = temperatureFile.split("\n");
		
		if(temperatureRaw.length == 2) {
			String[] temperatureRaw2 = temperatureRaw[1].split(" ");
			String[] temperatureRaw3 = temperatureRaw2[temperatureRaw2.length - 1].split("=");
			float temp = Float.parseFloat(temperatureRaw3[1]);
			return temp/1000 + "";
		}
		
		return "null";
	}
}