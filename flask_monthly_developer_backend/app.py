from flask import Flask
from flask_restx import Api
from controller import hello
from controller import connent_test

from config import db_connector


def create_env():
    app = Flask(__name__)
    app.config['MONGO_URI'] = "mongodb://localhost:27017/monthly_developer_db"
    db_connector.mongo.init_app(app)

    api = Api(app, version = 1.0, title = "flask_env", description = "flask_env_test")
    api.add_namespace(hello.Hello, '/hello')
    api.add_namespace(connent_test.db_ns, '/db_test')

    return app