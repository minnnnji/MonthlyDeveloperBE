from app import create_env
from config.config import PORT
from config.config import HOST_IP

if __name__ == '__main__':
    create_env().run(HOST_IP, port=PORT, debug=True)