from flask import Flask
from flask_restx import Api
from controller import hello

def create_env():
    app = Flask(__name__)
    
    api = Api(app, version = 1.0, title = "flask_env", description = "flask_env_test")
    api.add_namespace(hello.Hello, '/hello')
    
    return app