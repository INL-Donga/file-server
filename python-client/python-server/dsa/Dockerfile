# 1. 베이스 이미지 선택 (Python 3.9 사용)
FROM python:3.9-slim

# 2. 필요한 라이브러리 설치를 위한 의존성 설치
RUN apt-get update && apt-get install -y \
    build-essential \
    libssl-dev \
    libffi-dev \
    python3-dev \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# 3. 작업 디렉터리 생성
WORKDIR /app

# 4. 현재 디렉터리의 모든 파일을 컨테이너 내 /app 폴더로 복사
COPY . /app

# 5. PyTorch 및 기타 필요한 라이브러리 설치
# requirements.txt가 없으므로, 필요한 패키지를 직접 명시
RUN pip install --upgrade pip && \
    pip install numpy pandas torch torchvision matplotlib Pillow

# 6. Python 파일 실행
CMD ["python", "python-server.py"]
