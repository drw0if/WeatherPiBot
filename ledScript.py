#Importing the necessary libraries
import RPi.GPIO as GPIO
import time, glob, os, thread, subprocess, string

#Modify the variables above to change settings in the script
blinkingPin = 16        #pin for the blinking led
fanPin = 13             #pin for the fan
waitingTime = 5         #time between temperature reading
blinkTimeON = 1         #time ON for blinking
blinkTimeOFF = 1        #time OFF for blinking
maxCaseTemp = 32        #max temperature from the sensor
maxCPUTemp = 50         #max temperature for the CPU

#Setting warning to none
GPIO.setwarnings(False)
#Setting GPIO pin number to BOARD
GPIO.setmode(GPIO.BOARD)

#Setting up directory variables
temperatureFile = glob.glob('/sys/bus/w1/devices/28*')[0] + '/w1_slave'

#Setting pin mode
GPIO.setup(blinkingPin, GPIO.OUT, initial = 0)
GPIO.setup(fanPin, GPIO.OUT, initial = 0)

#Setting the temperature sensor
os.system('modprobe w1-gpio')
os.system('modprobe w1-therm')

#Function for the blinking of the green LED attached to pin 16
def blink():
        while True:
                GPIO.output(blinkingPin, 1)
                time.sleep(blinkTimeON)
                GPIO.output(blinkingPin, 0)
                time.sleep(blinkTimeOFF)

#Function for switching on and off the fan
def fan():
        while True:
                if getCaseTemperature() > maxCaseTemp or getCPUTemperatur$
                        GPIO.output(fanPin, 1)
                else:
                        GPIO.output(fanPin, 0)
                time.sleep(waitingTime)

#Function to get the temperature in degrees C
def getCaseTemperature():
        lines = readTemperatureFile()
        while lines[0].strip()[-3:] != 'YES':
                time.sleep(0.2)
                lines = readTemperatureFile()
        equals_pos = lines[1].find('t=')
        if equals_pos != -1:
                temp_string = lines[1][equals_pos+2:]
                return float(temp_string) / 1000.0

#Function to get the CPU temperature in degrees C
def getCPUTemperature():
        tempOutput = subprocess.check_output(['/opt/vc/bin/vcgencmd','mea$
        out1 = string.split(tempOutput, '=')
        out2 = string.split(out1[1], "'")
        return  float(out2[0])

#Function for reading the temp file
def readTemperatureFile():
        f = open(temperatureFile, 'r');
        temperatureFileRaw = f.readlines()
        f.close
        return temperatureFileRaw

try:
        thread.start_new_thread(blink, ())
        thread.start_new_thread(fan, ())
except:
        print "\nImpossible to initialize thread"
        GPIO.cleanup()

try:
        while 1:
                pass
except KeyboardInterrupt:
        print "\nQuitting the script"
	GPIO.cleanup()