# 1. 베이스 이미지 선택 (Python 3.9 사용)
FROM python:3.9-slim

# 2. 작업 디렉터리 생성
WORKDIR /app

# 3. 현재 디렉터리의 모든 파일을 컨테이너 내 /app 폴더로 복사
COPY . /app

# 4. 필요한 패키지 설치 (예: 소켓을 사용하므로 기본적으로 패키지 설치는 필요 없음)
# 만약 추가 패키지가 있다면 requirements.txt로 설치
# RUN pip install -r requirements.txt

# 5. Python 파일 실행
CMD ["python", "python-server.py"]
