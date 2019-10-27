import RPi.GPIO as GPIO
import bluetooth

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
        
        elif data == b'acende_banheiro':
            print("BANHEIRO - ON")
            GPIO.output(6, GPIO.LOW)
         
        #TERCEIRO LED 
        if data == b'apaga_banheiro_2':
            print("SUITE - OFF")
            GPIO.output(13, GPIO.HIGH)
        
        elif data == b'acende_banheiro_2':
            print("SUITE - ON")
            GPIO.output(13, GPIO.LOW)
            
        #QUARTO LED 
        if data == b'apaga_quarto':
            print("QUARTO CASAL - OFF")
            GPIO.output(19, GPIO.HIGH)
        
        elif data == b'acende_quarto':
            print("QUARTO CASAL - ON")
            GPIO.output(19, GPIO.LOW)
            
        #SEXTO LED 
        if data == b'apaga_cozinha':
            print("COZINHA - OFF")
            GPIO.output(1, GPIO.HIGH)
        
        elif data == b'acende_cozinha':
            print("COZINHA - ON")
            GPIO.output(1, GPIO.LOW)
            
        #SETIMO LED 
        if data == b'apaga_quarto_2':
            print("QUARTO - OFF")
            GPIO.output(7, GPIO.HIGH)
        
        elif data == b'acende_quarto_2':
            print("QUARTO - ON")
            GPIO.output(7, GPIO.LOW)

        #OITAVO LED 
        if data == b'apaga_sala':
            print("SALA - OFF")
            GPIO.output(8, GPIO.HIGH)
        
        elif data == b'acende_sala':
            print("SALA - ON")
            GPIO.output(8, GPIO.LOW)
