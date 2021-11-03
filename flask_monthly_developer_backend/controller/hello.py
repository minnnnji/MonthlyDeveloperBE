import json

from flask import request
from flask_restx import Api, Resource, fields, Namespace

NewPost = Namespace("New Post", description="new post")

introduce = NewPost.model('Introduce', {
    'recruit_title': fields.String(description='recruit title', required=True),
    'recruit_author': fields.String(description='recruit author', required=True),
    'recruit_contents': fields.String(description='recruit contents', required=True),
    'recruit_state': fields.String(description='recruit state', required=True),
})


@NewPost.route('/recruit', methods=['POST'])
class RecruitPost(Resource):
    @NewPost.expect(introduce)
    def post(self):
        board = []

        recruit_title = request.json.get("recruit_title")
        recruit_author = request.json.get("recruit_author")
        recruit_contents = request.json.get('recruit_contents')
        recruit_state = request.json.get('recruit_state')

        board.append([recruit_title, recruit_author, recruit_contents, recruit_state])
        print(board)
        return json.dumps({"status": 200,
                           "result": {"id": len(board)}})
