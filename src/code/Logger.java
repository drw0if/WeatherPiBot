package code;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
	private static final String logName = "BOT.log";
	private static final File file = new File(logName);
	private static final Calendar cal = Calendar.getInstance();
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public static void log(String text) {
		//Check per l'esistenza del file di log
		if(!file.exists())
			createLog();
		
		try {
			FileWriter writer = new FileWriter(file,true);
			writer.append(sdf.format(cal.getTime()) + " " + text + "\n");
			writer.close();
		}
		
		//Se non è stato possibile scrivere sul file lo notifico e chiudo il programma
		catch (IOException e) {
			System.out.println("It's not possible to write on the log file!");
			System.exit(1);
		}
	}
	
	//Se non è possibile creare un file per il log lo notifico e chiudo il bot
	private static void createLog() {
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.append("Created on: " + sdf.format(cal.getTime()) + "\n");
			writer.close();
		} 
		catch (IOException e) {
			System.out.println("It's not possible to create a log file!");
			System.exit(1);
		}
	}
	
}