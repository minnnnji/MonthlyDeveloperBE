from flask import request
from flask_restx import Api, Resource, fields, Namespace

Hello = Namespace("Hello!", description = "Hello everyone")

introduce = Hello.model('Introduce', {
    'name': fields.String(description='Your name', required=True),
    'age': fields.Integer(description='Your age', required=False)
})

@Hello.route('')
class HelloClass(Resource):
    @Hello.expect(introduce)
    def post(self):
        return f"name is {request.json.get('name')}, age is {request.json.get('age')}"