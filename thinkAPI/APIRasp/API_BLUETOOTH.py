#instalacao das bibliotecas bluetooths
#sudo apt install python-pip python-dev ipython
#sudo apt install bluetooth libbletooth-dev
#sudo pip install pybluez
import RPi.GPIO as GPIO
import bluetooth
#necessario instalar o requests:
#$pip install requests
import requests

GPIO.setmode  (GPIO.BCM)
GPIO.setup(1,  GPIO.OUT)
GPIO.setup(6,  GPIO.OUT)
GPIO.setup(7,  GPIO.OUT)
GPIO.setup(8,  GPIO.OUT)
GPIO.setup(13, GPIO.OUT)
GPIO.setup(25, GPIO.OUT)
GPIO.setup(26, GPIO.OUT)

host = ""
port = 1
server = bluetooth.BluetoothSocket(bluetooth.RFCOMM)

ip_servidor = '192.168.1.112'

try:
    server.bind((host, port))
except:
    print("ERRO")
    
server.listen(1)
client, address = server.accept()

print("Conectado a:", address)

GPIO.output(1,  GPIO.HIGH)
GPIO.output(6,  GPIO.HIGH)
GPIO.output(7,  GPIO.HIGH)
GPIO.output(8,  GPIO.HIGH)
GPIO.output(13, GPIO.HIGH)
GPIO.output(25, GPIO.HIGH)
GPIO.output(26, GPIO.HIGH)
GPIO.setwarnings(False)

def send_command(comodo, acendeu):
    command = 'acende'
    if acendeu == 0:
        command = 'apaga'
        
    url = 'http://%s:5000/%s/%s'  %(ip_servidor, command, comodo)
    response = requests.get(url)
    print(response.status_code)

while True:
    data = client.recv(1024)
    
    if data is not None:
        print(data)
        
        #PRIMEIRO LED
        if data == b'apaga_corredor':
            print("CORREDOR - OFF")
            GPIO.output(26, GPIO.HIGH)
        elif data == b'acende_corredor':
            print("CORREDOR - ON")
            GPIO.output(26, GPIO.LOW)
           
        #SEGUNDO LED
        if data == b'apaga_banheiro':
            print("BANHEIRO - OFF")
            GPIO.output(6, GPIO.HIGH)
            send_command('banheiro_1', 0)
        elif data == b'acende_banheiro':
            print("BANHEIRO - ON")
            GPIO.output(6, GPIO.LOW)
            send_command('banheiro_1', 1)
         
        #TERCEIRO LED 
        if data == b'apaga_banheiro_2':
            print("SUITE - OFF")
            GPIO.output(13, GPIO.HIGH)
            send_command('banheiro_2', 0)
        
        elif data == b'acende_banheiro_2':
            print("SUITE - ON")
            GPIO.output(13, GPIO.LOW)
            send_command('banheiro_2', 1)
            
        #QUARTO LED 
        if data == b'apaga_quarto':
            print("QUARTO CASAL - OFF")
            GPIO.output(19, GPIO.HIGH)
            send_command('quarto_1', 0)
        
        elif data == b'acende_quarto':
            print("QUARTO CASAL - ON")
            GPIO.output(19, GPIO.LOW)
            send_command('quarto_1', 1)
            
        #SEXTO LED 
        if data == b'apaga_cozinha':
            print("COZINHA - OFF")
            GPIO.output(1, GPIO.HIGH)
            send_command('cozinha', 0)
        
        elif data == b'acende_cozinha':
            print("COZINHA - ON")
            GPIO.output(1, GPIO.LOW)
            send_command('cozinha', 1)
            
        #SETIMO LED 
        if data == b'apaga_quarto_2':
            print("QUARTO - OFF")
            GPIO.output(7, GPIO.HIGH)
            send_command('quarto_2', 0)
        
        elif data == b'acende_quarto_2':
            print("QUARTO - ON")
            GPIO.output(7, GPIO.LOW)
            send_command('quarto_2', 1)

        #OITAVO LED 
        if data == b'apaga_sala':
            print("SALA - OFF")
            GPIO.output(8, GPIO.HIGH)
            send_command('sala', 0)
        
        elif data == b'acende_sala':
            print("SALA - ON")
            GPIO.output(8, GPIO.LOW)
            send_command('sala', 1)

