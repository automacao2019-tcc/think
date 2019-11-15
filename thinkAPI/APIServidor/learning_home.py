import tensorflow as tf
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
import csv
from datetime import datetime
import random
import pandas as pd
#pip install requests
import requests

ip_rasp = '192.168.1.150'
comodos = ['quarto_1', 'quarto_2', 'cozinha', 'sala', 'banheiro_1', 'banheiro_2', 'portao', 'corredor']

def trainning(ultimo_comodo, horas, dia_semana):
    #lendo o arquivo csv com as rotinas
    base = pd.read_csv('rotina.csv')

    #a variavel x vai contar todas as colunas do csv com exceção da coluna 'resultado' 
    x = base.drop('resultado', axis = 1)

    #a variavel y vai conter a coluna 'resultado'
    y = base['resultado']

    nome_colunas =  ['de', 'para',	'hora',	'diaSemana']
    colunas = [tf.feature_column.numeric_column(key = c) for c in nome_colunas]

    #fazendo o treinamento
    x_treinamento, x_teste, y_treinamento, y_teste = train_test_split(x, y, test_size = 0.3)
    funcao_treinamento = tf.estimator.inputs.pandas_input_fn(x = x_treinamento, y = y_treinamento, batch_size = 32, num_epochs = None, shuffle = True)
    classificador = tf.estimator.LinearClassifier(feature_columns = colunas)
    #fazendo efetivamente o treinamento ... steps = 10000 significa que ele vai rodar um loop 10000 vezes treinando a rede
    classificador.train(input_fn = funcao_treinamento, steps = 10000)

    monta_csv_previsoes(ultimo_comodo, horas, dia_semana)

    base = pd.read_csv('previsao.csv')
    x_for_predicts = base

    #fazendo previsoes
    funcao_previsao = tf.estimator.inputs.pandas_input_fn(x = x_for_predicts, batch_size = 32, shuffle = True)

    previsoes_final = []
    for p in classificador.predict(input_fn = funcao_previsao):
        previsoes_final.append(p['class_ids'])

    
    print(previsoes_final)

    i = 0
    for p in previsoes_final:
        print('send: %s' %(p))
        send_command(i, p)
        i += 1    


def monta_csv_previsoes(ultimo_comodo, horas, dia_semana):
    #gerando csv de previsao
    nome = 'previsao.csv'
    csv_file = open(nome, 'w')
    newCsv = csv.writer(csv_file)

    #colunas
    row = ['de', 'para',	'hora',	'diaSemana']
    newCsv.writerow(row)

    for i in range(0, 7):
        row = [ultimo_comodo, i, horas, dia_semana]
        newCsv.writerow(row)


def send_command(comodo_number, acendeu):
    acende_apaga = 'acende'
    if acendeu == 0:
        acende_apaga = 'apaga'

    comodo = comodos[comodo_number]

    url = 'http://%s:5000/%s_casa/%s' %(ip_rasp, acende_apaga, comodo)
    requests.put(url)