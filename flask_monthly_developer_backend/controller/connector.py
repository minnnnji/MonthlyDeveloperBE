from flask import request, jsonify
from flask_restx import Api, Resource, fields, Namespace

import json

from config import db_connector

db_ns = Namespace("DB Connect Test", description = "DB 연결을 확인하기 위한 임시 컨트롤러입니다.")

@db_ns.route('')
class DB(Resource):
    def get(self):
        data = [doc for doc in db_connector.mongo.db.recruit_post.find({}, {"_id":0})]
        return jsonify(data)