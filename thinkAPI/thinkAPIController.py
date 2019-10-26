from flask import Flask
from thinkAPIService import *

app  = Flask(__name__)

#função chamada sempre quando alguma luz é acesa
@app.route('/acende/<comodo>')
def acende(comodo):
    string = 'acendeu o comodo %s' %(comodo)
    acende_apaga(comodo, 1)
    return string

@app.route('/apaga/<comodo>')
def apaga(comodo):
    string = 'apagou o comodo %s' %(comodo)
    acende_apaga(comodo, 0)
    return string

if __name__ == '__main__':
    app.run(debug = True, host = '0.0.0.0')

