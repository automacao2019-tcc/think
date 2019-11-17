from flask import Flask
from thinkAPIService import *

app  = Flask(__name__)

#função chamada sempre quando alguma luz é acesa
@app.route('/acende/<comodo>', methods=["PUT"])
def acende(comodo):
    acende_apaga(comodo, 1)

@app.route('/apaga/<comodo>', methods=["PUT"])
def apaga(comodo):
    acende_apaga(comodo, 0)

if __name__ == '__main__':
    app.run(debug = False, host = '0.0.0.0')

