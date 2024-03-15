from flask import Flask, Response
from flask_cors import CORS
import json
from scrap import Scrap

api = Flask(__name__)
CORS(api)


@api.route('/', methods=['GET', 'POST'])
def home():
    return response(status=True, data="Welcome!")


@api.route('/finance/<name>', methods=['GET'])
def news(name="BTC-BRL"):
    try:
        service = Scrap()
        return response(status=True, data=service.get_finance(name))
    except Exception as e:
        return response(status=False, data=str(e))


def response(status: bool, data: any):
    resp = {'success': True, 'data': data}
    if not status:
        resp = {'success': False, 'error': data}
    return Response(response=json.dumps(resp, sort_keys=False), mimetype='application/json')


if __name__ == '__main__':
    api.run(debug=False)
