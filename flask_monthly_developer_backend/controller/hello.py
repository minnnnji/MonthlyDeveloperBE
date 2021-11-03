import json

from flask import request
from flask_restx import Api, Resource, fields, Namespace

NewPost = Namespace("New Post", description="new post")

introduce = NewPost.model('Introduce', {
    'recruit_title': fields.String(description='recruit title', required=True),
    'recruit_author': fields.String(description='recruit author', required=True),
    'recruit_contents': fields.String(description='recruit contents', required=True),
    'recruit_tags': fields.List(fields.String, description='recruit tags', required=False),
    'recruit_state': fields.String(description='recruit state', required=True),
})


@NewPost.route('/recruit', methods=['POST'])
class RecruitPost(Resource):
    @NewPost.expect(introduce)
    def post(self):
        # 새 글 생성
        recruit_title = request.json.get("recruit_title") # 제목
        recruit_author = request.json.get("recruit_author") # 글쓴이
        recruit_contents = request.json.get('recruit_contents') # 내용
        recruit_tags = request.json.get('recruit_tags') # tags
        recruit_state = request.json.get('recruit_state') # 상태

        newpost_recruit = {'recruit_title': recruit_title, 'recruit_author': recruit_author,
                           'recruit_contents': recruit_contents, 'recruit_tags': recruit_tags, 'recruit_state': recruit_state}

        return json.dumps(newpost_recruit, ensure_ascii=False)
