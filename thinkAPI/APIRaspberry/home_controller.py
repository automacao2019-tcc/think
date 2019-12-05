import RPi.GPIO as GPIO
#necessario instalar o requests:
#$pip install requests
import requests
import time

GPIO.setmode  (GPIO.BCM)
GPIO.setup(1,  GPIO.OUT)
GPIO.setup(6,  GPIO.OUT)
GPIO.setup(7,  GPIO.OUT)
GPIO.setup(8,  GPIO.OUT)
GPIO.setup(13, GPIO.OUT)
GPIO.setup(18, GPIO.OUT)
GPIO.setup(19, GPIO.OUT)
GPIO.setup(25, GPIO.OUT)
GPIO.setup(26, GPIO.OUT)

ip_servidor = '192.168.1.105'

GPIO.output(1,  GPIO.HIGH)
GPIO.output(6,  GPIO.HIGH)
GPIO.output(7,  GPIO.HIGH)
GPIO.output(8,  GPIO.HIGH)
GPIO.output(13, GPIO.HIGH)
GPIO.output(19, GPIO.HIGH)
GPIO.output(25, GPIO.HIGH)
GPIO.output(26, GPIO.HIGH)
GPIO.setwarnings(False)

p = GPIO.PWM(18,50)
p.start(7.5)

def send_command(comodo, acendeu):
    command = 'acende'
    if acendeu == 0:
        command = 'apaga'
        
    url = 'http://%s:5000/%s/%s'  %(ip_servidor, command, comodo)
    #print(url)
    response = requests.put(url)
    #print(response.status_code)

def acende_apaga(comodo, acendeu, send):
    if comodo == 'corredor' and acendeu == 0:
        print("CORREDOR - OFF")
        GPIO.output(1, GPIO.HIGH)
        if send:
            send_command('corredor', 0)
    elif comodo == 'corredor' and acendeu == 1:
        print("CORREDOR - ON")
        GPIO.output(1, GPIO.LOW)
        if send:
            send_command('corredor', 1)
    
    if comodo == 'portao' and acendeu == 0:
        print("PORTAO - OFF")
        p.ChangeDutyCycle(7.8)
        time.sleep(1)
        if send:
            send_command('portao', 0)
    elif comodo == 'portao' and acendeu == 1:
        print("PORTAO - ON")
        p.ChangeDutyCycle(0.5)
        time.sleep(1)
        if send:
            send_command('portao', 1)
    
    #SEGUNDO LED
    if comodo == 'banheiro_1' and acendeu == 0:
        print("BANHEIRO - OFF")
        GPIO.output(7, GPIO.HIGH)
        if send:
            send_command('banheiro_1', 0)
    elif comodo == 'banheiro_1' and acendeu == 1:
        print("BANHEIRO - ON")
        GPIO.output(7, GPIO.LOW)
        if send:
            send_command('banheiro_1', 1)
     
    #TERCEIRO LED 
    if comodo == 'banheiro_2' and acendeu == 0:
        print("SUITE - OFF")
        GPIO.output(25, GPIO.HIGH)
        if send:
            send_command('banheiro_2', 0)
    
    elif comodo == 'banheiro_2' and acendeu == 1:
        print("SUITE - ON")
        GPIO.output(25, GPIO.LOW)
        if send:
            send_command('banheiro_2', 1)
        
    #QUARTO LED 
    if comodo == 'quarto_1' and acendeu == 0:
        print("QUARTO CASAL - OFF")
        GPIO.output(8, GPIO.HIGH)
        if send:
            send_command('quarto_1', 0)
    
    elif comodo == 'quarto_1' and acendeu == 1:
        print("QUARTO CASAL - ON")
        GPIO.output(8, GPIO.LOW)
        if send:
            send_command('quarto_1', 1)
        
    #SEXTO LED 
    if comodo == 'cozinha' and acendeu == 0:
        print("COZINHA - OFF")
        GPIO.output(19, GPIO.HIGH)
        if send:
            send_command('cozinha', 0)
    
    elif comodo == 'cozinha' and acendeu == 1:
        print("COZINHA - ON")
        GPIO.output(19, GPIO.LOW)
        if send:
            send_command('cozinha', 1)
        
    #SETIMO LED 
    if comodo == 'quarto_2' and acendeu == 0:
        print("QUARTO - OFF")
        GPIO.output(6, GPIO.HIGH)
        if send:
            send_command('quarto_2', 0)
    
    elif comodo == 'quarto_2' and acendeu == 1:
        print("QUARTO - ON")
        GPIO.output(6, GPIO.LOW)
        if send:
            send_command('quarto_2', 1)

    #OITAVO LED 
    if comodo == 'sala' and acendeu == 0:
        print("SALA - OFF")
        GPIO.output(13, GPIO.HIGH)
        if send:
            send_command('sala', 0)
    
    elif comodo == 'sala' and acendeu == 1:
        print("SALA - ON")
        GPIO.output(13, GPIO.LOW)
        if send:
            send_command('sala', 1)