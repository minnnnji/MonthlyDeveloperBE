import json
# from bson.objectid import ObjectId
from flask import request, redirect
from flask_restx import Api, Resource, fields, Namespace

Recruit = Namespace("Recruit post", description="for recruit")

introduce = Recruit.model('Introduce', {
    'recruit_title': fields.String(description='recruit title', required=True),
    'recruit_author': fields.String(description='recruit author', required=True),
    'recruit_contents': fields.String(description='recruit contents', required=True),
    'recruit_tags': fields.List(fields.String, description='recruit tags', required=False),
    'recruit_state': fields.String(description='recruit state', required=True),
})


@Recruit.route('/new_post', methods=['POST'])
class RecruitPostCreate(Resource):
    @Recruit.expect(introduce)
    def post(self):
        # 새 글 생성
        recruit_title = request.json.get("recruit_title")  # 제목
        recruit_author = request.json.get("recruit_author")  # 글쓴이
        recruit_contents = request.json.get('recruit_contents')  # 내용
        recruit_tags = request.json.get('recruit_tags')  # tags
        recruit_state = request.json.get('recruit_state')  # 상태

        newpost_recruit = {'recruit_title': recruit_title, 'recruit_author': recruit_author,
                           'recruit_contents': recruit_contents, 'recruit_tags': recruit_tags,
                           'recruit_state': recruit_state}
        # mongo DB에 추가
        # post = db.[colletion_name] # Collection에 접근 후
        # post.insert_one(newpost_recruit).inserted_id  #한 개 저장

        return json.dumps(newpost_recruit, ensure_ascii=False)


@Recruit.route('/new_post/<int:post_id>')
class RecruitPost(Resource):
    @Recruit.expect(introduce)
    def put(self, post_id):
        # 글 수정
        post_id = request.json.get("_id")
        recruit_title = request.json.get("recruit_title")  # 제목
        recruit_author = request.json.get("recruit_author")  # 글쓴이
        recruit_contents = request.json.get('recruit_contents')  # 내용
        recruit_tags = request.json.get('recruit_tags')  # tags
        recruit_state = request.json.get('recruit_state')  # 상태

        newpost_recruit = {'_id': post_id, 'recruit_title': recruit_title, 'recruit_author': recruit_author,
                           'recruit_contents': recruit_contents, 'recruit_tags': recruit_tags,
                           'recruit_state': recruit_state}

        return json.dumps(newpost_recruit, ensure_ascii=False)

    def delete(self, post_id):
        return None

    def get(self, post_id):
        return None
