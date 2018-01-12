package code;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class RemoteBot extends TelegramLongPollingBot {

	String usernameBOT, token, whiteList[];
	boolean log;
	
	@Override
	public void onUpdateReceived(Update update) {

		//Se l'update contiene un messaggio che contiene testo
		if(update.hasMessage() && update.getMessage().hasText()) {

			//Se chi ha inviato il messaggio si trova nella whitelist esegue lo switch
			if(whiteList.length == 0 || method.contains(update.getMessage().getChat().getUserName(), whiteList)) {
				
				//Se i log sono attivi scrive sul log il mittente ed il messaggio ricevuto
				if(log)
					Logger.log(update.getMessage().getChat().getUserName() + ": " + update.getMessage().getText());
				
				//Comparazione del messaggio con la lista comandi
				switch(update.getMessage().getText()) {
					case "/start": sendMessage(update, 
							method.Start+"\nPlease type \"List\" to start"); break;
							
					case "List": sendMessage(update, 
							method.List); break;
							
					case "Hi": sendMessage(update,
							"Hello to you " + update.getMessage().getChat().getFirstName()); break;
							
					case "Ip" : sendMessage(update, 
							method.getRemoteIp()); break;
							
					case "Temp" : 
						case "Temperature" : sendMessage(update, 
							method.getTemperatures()); break;
		
					case "Clear" : method.clearBackup(); sendMessage(update, 
							"Deletetion completed!"); break;
						
					case "Uptime" : sendMessage(update, 
							method.upTime()); break;
					//Se non e' stato ricevuto nessun messaggio ignora il testo ricevuto
					default: break;
				}
			}
			
			//Se il mittente non e' il whitelist gli risponde di non avere i requisiti
			else {
				sendMessage(update, "I'm so sorry " + update.getMessage().getChat().getFirstName() + " but you haven't the access to these resources!");
			}
		}
			
	}

	//Invia un messaggio passato come stringa
	private void sendMessage(Update update, String text) {
		SendMessage message = new SendMessage()
				.setChatId(update.getMessage().getChatId()).setText(text);
		try {
			execute(message);
			if(log)
				Logger.log("Bot: " + text);
		}
		catch(TelegramApiException e) {
			if(log)
				Logger.log("Error on sending the message!");
		}
	}
	
	//Costruttori e metodi obbligatori
	RemoteBot(String username, String token, boolean log, String...whiteList) {
		this.usernameBOT = username;
		this.token = token;
		this.log = log;
		this.whiteList = whiteList;
	}
	
	@Override
	public String getBotUsername() {
		return usernameBOT;
	}

	@Override
	public String getBotToken() {
		return token;
	}

}
