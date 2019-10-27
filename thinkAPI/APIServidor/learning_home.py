import tensorflow as tf
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
import csv
from datetime import datetime
import random
import pandas as pd
    
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

#fazendo previsoes
funcao_previsao = tf.estimator.inputs.pandas_input_fn(x = x, batch_size = 32, shuffle = False)
previsoes = classificador.predict(x)

previsoes_final = []
for p in classificador.predict(input_fn = funcao_previsao):
    previsoes_final.append(p['class_ids'])

print(previsoes_final)

#verificando a taxa de acerto
taxa_acerto = accuracy_score(y_teste, previsoes_final)
print('\n')
print('taxa acerto: {}%'.format(taxa_acerto * 100))
print('\n')

