import tweepy
import time
import requests
from scrap import Scrap

# Substitua pelos seus respectivos valores de chaves e tokens do Twitter
CONSUMER_KEY = 'yaJaOuz35RolqEDZSp0v1z6yr'
CONSUMER_SECRET = 'BKqpzbAMrCxeNdu4LyBNgU5qDT4tHKJqxd4XWR0dPDBTp2Yw8h'
ACCESS_TOKEN = '1713241241637257216-T8Re4ZJhipBQYI1TW8ijGJbR6CuTqf'
ACCESS_TOKEN_SECRET = 'VNV3ch3leH968PE8VCBvFw57enZwAmHe4BkF9EAXuOhJZ'

# Autenticação no Twitter
auth = tweepy.OAuthHandler(CONSUMER_KEY, CONSUMER_SECRET)
auth.set_access_token(ACCESS_TOKEN, ACCESS_TOKEN_SECRET)
api = tweepy.API(auth)


def obter_preco_atual(url_api):
    try:
        s = Scrap()
        return s.get_finance(name="BTC-BRL")["value"]
    except Exception as e:
        print(f"Erro ao acessar a API: {e}")
        return None


def analisar_tendencia(precos):
    tendencias = []
    for i in range(1, len(precos)):
        if precos[i] > precos[i - 1]:
            tendencias.append('subida')
        else:
            tendencias.append('queda')
    return tendencias


def decidir_compra_venda(tendencias):
    if tendencias[-3:] == ['subida', 'subida', 'subida']:
        return 'Vender'
    elif tendencias[-3:] == ['queda', 'queda', 'queda']:
        return 'Comprar'
    return 'Aguardar'


# URL da API - substitua pela URL real da API que você está usando
url_api = 'https://finance-api-n9wk.onrender.com/finance/'

precos_bitcoin = []

while True:
    preco = obter_preco_atual(url_api)
    if preco:
        precos_bitcoin.append(preco)
        if len(precos_bitcoin) > 3:
            precos_bitcoin.pop(0)  # Mantém somente os últimos 4 preços para análise

            tendencias = analisar_tendencia(precos_bitcoin)
            decisao = decidir_compra_venda(tendencias)

            if decisao in ['Comprar', 'Vender']:
                api.update_status(f"Recomendação: {decisao} Bitcoin!")
                print(f"Tweet enviado: Recomendação: {decisao} Bitcoin!")

    time.sleep(300)  # Espera 5 minutos antes de executar novamente
