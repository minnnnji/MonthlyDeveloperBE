from flask import request
from flask_restx import Api, Resource, fields, Namespace

import json
from bson import json_util

from config import db_connector

db_ns = Namespace("DB Connect Test", description = "DB 연결을 확인하기 위한 임시 컨트롤러입니다.")

@db_ns.route('')
class DB(Resource):
    def get(self):
        data = list(db_connector.mongo.db.recruit_post.find())
        return json.dumps(data, default=json_util.default)