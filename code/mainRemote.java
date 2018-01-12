package code;

import java.io.File;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

public class mainRemote {
	
	public static void main(String[] args) {
		boolean log = false;
		
		if(args.length > 0 && method.contains("--log", args))
			log = true;
		
		for(int i = 0; i < args.length; i++) {
			File app;
			if((app = new File(args[i])).exists())
				Conf.conf = app;
		}
		
		if(Conf.conf == null)
			Conf.conf = new File(method.DEFAULT_CONF);
				
		String[] data = Conf.getConf();
			
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
			
		try {
			telegramBotsApi.registerBot(new RemoteBot(data[0], data[1], log, method.getWhiteList(data)));
		} 
		catch (TelegramApiRequestException e) {
			System.out.println(
					"It's not possible to run the bot, please check the conf file and if it is already running ");
		}
		
		if(log)
			Logger.log("Bot opening");
		System.out.println(method.Start);
		
	}

}
