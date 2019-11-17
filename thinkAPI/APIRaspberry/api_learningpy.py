from flask import Flask
from home_controller import *

#API para receber comando das IA
app = Flask(__name__)

@app.route('/acende_casa/<comodo>', methods=["PUT"])
def acende(comodo):
    string = 'IA acendeu o comodo %s' %(comodo)
    print(string)
    acende_apaga(comodo, 1, False)
    
@app.route('/apaga_casa/<comodo>', methods=["PUT"])
def apaga(comodo):
    string = 'IA apagou o comodo %s' %(comodo)
    print(string)
    acende_apaga(comodo, 0, False)
    
if __name__ == '__main__':
    app.run(debug = False, host = '0.0.0.0')