#O objetivo do algoritmo é prever se uma luz sera ou não acendida baseado em atributos previsores que serão: Dia da semana, Horário e cômodo acendido anteriormente
import tensorflow as tf
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
import csv
from datetime import datetime
import random
import pandas as pd

# MONTANDO O CSV ================================
def hour_to_minute(hour, min):
    return hour * 60 + min

with open('rotina.csv', 'w', newline = '') as csvfile:
    c = csv.writer(csvfile)
    c.writerow(['hora', 'comodo_origem', 'dia_semana', 'acendeu'])

    for i in range(20000):
        min = hour_to_minute(datetime.now().hour, datetime.now().minute)
        comodo = random.randint(0, 1)
        dia_semana = random.randint(0, 6)
        dia = datetime.now().day
        acendeu = 1

        if dia_semana == 0:
            acendeu = 0
        elif dia_semana != 0 and comodo == 0: #and min > 990 and min < 1050:
            acendeu = 1

        c.writerow([min, comodo, dia_semana, acendeu])

# ======================================================

base = pd.read_csv('rotina.csv')

#a variável x vai receber todas as colunas do .csv com exceção da coluna 'acendeu'
x = base.drop('acendeu', axis = 1)

#variável y vai receber a coluna 'acendeu'
y = base['acendeu']

nome_colunas = ['hora', 'comodo_origem', 'dia_semana']
colunas = [tf.feature_column.numeric_column(key = c) for c in nome_colunas]

#fazendo treinamento
x_treinamento, x_teste, y_treinamento, y_teste = train_test_split(x, y, test_size = 0.3)
funcao_treinamento = tf.estimator.inputs.pandas_input_fn(x = x_treinamento, y = y_treinamento, batch_size = 32, num_epochs = None, shuffle = True)
classificador = tf.estimator.LinearClassifier(feature_columns = colunas)
#fazendo efetivamente o treinamento ... stepes = 10000 significa que ele vai rodar um loop 10000 vezes treinando a rede
classificador.train(input_fn = funcao_treinamento, steps = 10000)

#fazendo previsões
funcao_previsao = tf.estimator.inputs.pandas_input_fn(x = x_teste, batch_size = 32, shuffle = False)
teste = [960, 0, 2]
previsoes = classificador.predict(x_teste)

#print(list(previsoes))

#previsoes_final = []

#for p in classificador.predict(input_fn = funcao_previsao):
#    previsoes_final.append(p['class_ids'])

#print(previsoes_final)

#verificando a taxa de acerto
#taxa_acerto = accuracy_score(y_teste, previsoes_final)
#print('\n')
#print('taxa de acerto: {}%'.format(taxa_acerto * 100))
#print('\n')
