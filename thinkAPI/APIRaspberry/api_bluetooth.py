#instalacao das bibliotecas bluetooths
#sudo apt install python-pip python-dev ipython
#sudo apt install bluetooth libbletooth-dev
#sudo pip install pybluez
import bluetooth
#import de home_controller.py
from home_controller import *

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

while True:
    data = client.recv(1024)
    
    if data is not None:
        print(data)
        
        #corredor
        if data == b'apaga_corredor':
            acende_apaga('corredor', 0)
        elif data == b'acende_corredor':
            acende_apaga('corredor', 1)
            
        if data == b'fechar_portao':
            acende_apaga('corredor', 0)
        elif data == b'abrir_portao':
            acende_apaga('corredor', 1)
           
        #SEGUNDO LED
        if data == b'apaga_banheiro':
            acende_apaga('banheiro_1', 0)
        elif data == b'acende_banheiro':
            acende_apaga('banheiro_1', 1)
         
        #TERCEIRO LED 
        if data == b'apaga_banheiro_2':
            acende_apaga('banheiro_2', 0)
        
        elif data == b'acende_banheiro_2':
            acende_apaga('banheiro_2', 1)
            
        #QUARTO LED 
        if data == b'apaga_quarto':
            acende_apaga('quarto_1', 0)
        
        elif data == b'acende_quarto':
            acende_apaga('quarto_1', 1)
            
        #SEXTO LED 
        if data == b'apaga_cozinha':
            acende_apaga('cozinha', 0)
        
        elif data == b'acende_cozinha':
            acende_apaga('cozinha', 1)
            
        #SETIMO LED 
        if data == b'apaga_quarto_2':
            acende_apaga('quarto_2', 0)
        
        elif data == b'acende_quarto_2':
            acende_apaga('quarto_2', 1)

        #OITAVO LED 
        if data == b'apaga_sala':
            acende_apaga('sala', 0)
        
        elif data == b'acende_sala':
            acende_apaga('sala', 1)











