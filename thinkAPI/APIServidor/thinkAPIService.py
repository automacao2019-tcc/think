import csv
from datetime import datetime
import pandas as pd
from learning_home import *

comodos = ['quarto_1', 'quarto_2', 'cozinha', 'sala', 'banheiro_1', 'banheiro_2', 'portao', 'corredor']

def hour_to_minute(horas, minutos):
    return horas * 60 + minutos

def retorna_indice_comodo(comodo):
    if comodo in comodos:
        i = 0
        while(i < len(comodos)):
            if comodos[i] == comodo:
                return i
            else:
                i += 1
    else:
        return -1

def acende_apaga(comodo, acendeu):
    minutos = hour_to_minute(datetime.now().hour, datetime.now().minute)
    comodo_nmr = retorna_indice_comodo(comodo)

    data = datetime.now()
    dia_semana = data.weekday()
    
    empilha_csv(comodo_nmr, dia_semana, minutos, acendeu)

def empilha_csv(comodo, dia_semana, horas, acendeu):
    rotina = open('rotina.csv', 'r')
    reader = csv.reader(rotina)

    dados = list(reader)

    for row in reader:
        dados.append(row)

    if len(dados) < 2:
        ultimo_comodo = 6
    else:
        ultimo_comodo = dados[len(dados) - 1][1]

    dados.append([ultimo_comodo, comodo, horas, dia_semana, acendeu])

    nome = 'rotina.csv'
    csv_file = open(nome, 'w')
    newCsv = csv.writer(csv_file)

    for row in dados:
        newCsv.writerow(row)
    
    rotina.close()
    csv_file.close()

    if len(dados) > 20:
        trainning(comodo, horas, dia_semana)