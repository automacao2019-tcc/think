import tensorflow as tf
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
import csv
from datetime import datetime
import random
import pandas as pd
#pip install requests
import requests

ip_rasp = '192.168.1.196'
comodos = ['quarto_1', 'quarto_2', 'cozinha', 'sala', 'banheiro_1', 'banheiro_2', 'portao', 'corredor']

def trainning(ultimo_comodo, horas, dia_semana):
        #lendo o arquivo csv com as rotinas
        base = pd.read_csv('rotina.csv')

        #a variavel x vai contar todas as colunas do csv com exceção da coluna 'resultado' 
        x = base.drop('resultado', axis = 1)

        #a variavel y vai conter a coluna 'resultado'
        y = base['resultado']

        #categorizando as horas
        hora = tf.feature_column.numeric_column('hora')
        hora_categorica = [tf.feature_column.bucketized_column(hora, boundaries=[
        0,
        60,
        120,
        180,
        240,
        300,
        360,
        420,
        480,
        540,
        600,
        660,
        720,
        780,
        840,
        900,
        960,
        1020,
        1080,
        1140,
        1200,
        1260,
        1320,
        1380,])]

        nome_colunas =  ['de', 'para',	'diaSemana']
        colunas_categoricas = [tf.feature_column.categorical_column_with_vocabulary_list(key = c, vocabulary_list = x[c].unique()) for c in nome_colunas]

        colunas = colunas_categoricas + hora_categorica

        #fazendo o treinamento
        x_treinamento, x_teste, y_treinamento, y_teste = train_test_split(x, y, test_size = 0.3)
        funcao_treinamento = tf.estimator.inputs.pandas_input_fn(x = x_treinamento, y = y_treinamento, batch_size = 32, num_epochs = None, shuffle = True)
        classificador = tf.estimator.LinearClassifier(feature_columns = colunas)
        #fazendo efetivamente o treinamento ... steps = 5000 significa que ele vai rodar um loop 5000 vezes treinando a rede
        classificador.train(input_fn = funcao_treinamento, steps = 5000)

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
        while i < len(previsoes_final):
                send_command(i, previsoes_final[i][0])
                i = i + 1    


def monta_csv_previsoes(ultimo_comodo, horas, dia_semana):
        #gerando csv de previsao
        nome = 'previsao.csv'
        csv_file = open(nome, 'w')
        newCsv = csv.writer(csv_file)

        #colunas
        row = ['de', 'para', 'hora', 'diaSemana']
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