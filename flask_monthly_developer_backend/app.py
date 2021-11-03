from flask import Flask
from flask_restx import Api
from controller import hello
from controller import connector

from config import config
from config import db_connector


def create_env():
    app = Flask(__name__)
    
    # MongoDB 연결을 설정합니다.
    app.config['MONGO_URI'] = config.MONGO_URI
    db_connector.mongo.init_app(app)

    api = Api(app, version = 1.0, title = "flask_env", description = "flask_env_test")

    # namespace를 추가합니다.
    api.add_namespace(hello.Hello, '/hello')
    api.add_namespace(connector.db_ns, '/db_test')

    return app