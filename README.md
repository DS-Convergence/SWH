# Squirrel Warehouse(다람쥐 창고)

### 팀원 구성👩‍💻 : 
  - 황세원 (PM, Firebase 관리, 로그인와 유저 관련 기능 개발 담당, 이미지 인식 기능 개선)
  - 김은배 (채팅 서비스 개발 담당)
  - 엄예솔 (메인 페이지, 추천 서비스 개발 담당)
  - 이예은 (상품 상세 페이지, 이미지 인식 기능 담당, 추천 서비스 기능 개선)

### 서비스 소개✨ : 
```
지역 기반 취미 물품 공유 플랫폼
취미 물품을 공유(대여)할 수 있는 플랫폼으로 사용자간의 취미 물품 대여 거래를 할 수 있는 P2P서비스
```

### 프로젝트 기간⏰ : 
```
21.02.20 ~ 21.11.30
```

### 핵심 기능🔧 : 
  1. **AI 물품 추천 서비스**   
  사용자의 찜 목록과 물건을 본 시간 데이터를 기반으로 사용자가 관심을 가질만한 물건을 추천하는 사용자 기반의 협업 필터링을 이용한다.

  2. **물건 인식 & 자동 카테고리 선정**   
   Tensorflow Lite Model Maker를 통해 80여 개의 물건을 학습시킨 모델을 이용하여 물건을 인식하고, 물건의 카테고리를 자동으로 선정한다.

  3. **QR코드를 이용한 물건 거래 & 보증금, 대여료 제도**   
   QR코드를 이용해 물건을 대여하고 반납할 수 있는 서비스이다. 물건 공유에 필요한 정보를 저장할 수 있는 QR코드를 제공하고, 반납이 끝난 후 사용자 평가를 수행한다.

  4. **채팅**   
   사용자는 채팅을 통해 정보를 공유할 수 있다. 사진을 찍고 업로드하는 기능을 통해 물건에 대한 자세한 정보를 제공할 수 있고 알림을 통해 사용자에게 온 채팅을 확인할 수 있다.

  5. **위치 기반 서비스 (GPS 이용)**   
   GPS를 이용해 어플리케이션 사용자 주변의 물품을 지도상에서 확인할 수 있다.

  6. **홍보 서비스, ‘파워람쥐’**   
   ‘파워람쥐’는 사용자들이 전문적으로 대여 서비스를 제공할 수 있도록 하는 광고 서비스이다. 사용자가 돈을 내고 서비스에 가입하면 사용자의 프로필이 메인페이지 상단에 노출되어 다른 사용자들의 접근을 높인다.

### 시스템 구성도 📊   
<img src="https://user-images.githubusercontent.com/55613591/160283610-6da02753-4b8e-454e-a92b-00655ada9ff7.png" width="500">
<img src="https://user-images.githubusercontent.com/55613591/160283744-210ffc51-f7bd-4f65-94f8-1e26667a1690.png" width="500">

### 시연 영상 🎞   
[시연 영상 바로가기(youtube)](https://www.youtube.com/watch?v=c2l7jufDAfg)

<br><br>

### 실행화면 🖥
![image](https://user-images.githubusercontent.com/55613591/160284171-6cdd292f-013c-4b7f-b296-df335e2bb1d4.png)   
![image](https://user-images.githubusercontent.com/55613591/160284196-d79b5b66-c765-43a5-b4b0-d2c4ba1bf4a7.png)   
![image](https://user-images.githubusercontent.com/55613591/160284217-e8827022-f4f2-49f7-87c3-fc4af407d479.png)   
![image](https://user-images.githubusercontent.com/55613591/160284227-fa5de05f-37cb-4dd3-a07d-237d3e5cc0cc.png)   
![image](https://user-images.githubusercontent.com/55613591/160284244-d85c55f4-8123-410f-b35f-5cd94a63ccea.png)   


