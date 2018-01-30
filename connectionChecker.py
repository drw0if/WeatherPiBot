import os, time, string

HOST = "www.google.it"  #Host to ping to check connection
dir = "/tmp/connectionFlag.flag" #File to write flag for reboot
log = "/var/log/connectionLog.log" #File for logging activity

#Reading the connection state
if os.system("ping -c 1 " + HOST) != 0:
        #Logging activity
        os.system("echo \"$(date) | Not connected\" >> " + log)
        #Adding a flag
        os.system("echo 1 >> " + dir)
        #Reading the flag number
        f = open(dir,"r")
        flagString = f.read()
        f.close()
        flag = 0
        i = 0
        while i < len(flagString):
                if flagString[i] == '1':
                        flag+=1
                i+=1
        if flag == 2:
                os.system("echo \"$(date) | Rebooting\" >> " + log)
                os.system("sudo reboot")
else:
        os.system("echo \"$(date) | Connected\" >> " + log)
        os.system("sudo rm " + dir)