# git

## 혹시 모를 강제 복구

1. git reset —hard [원하는곳] —해당 원하는 곳까지 리셋 ( 커밋에 안남고 그간 기록 까지 지움)

## 가져오는 방법

1. git clone -url

## 일반적인 추가 방법

1. git add .

2. git commit -m "커밋명"

3. git push origin main

## 브랜치 추가

1. git branch 이름 // 브랜치 생성

2. git branch -d 이름 // 브랜치 삭제

3. git checkout 원하는 브랜치 // 해당 브랜치 접속

## 합치는 방법

만든 브랜치에서 

1. git add .

2. git commit -m "커밋명"

3. git checkout main

4. git pull // 가져오기

5. git merge 합치려는 브랜치 이름

## 브랜치 제거

1. git branch -d 이름 // 브랜치 삭제

3. git push origin --delete 삭제할 브랜치 이름 // 브랜치 삭제 후 원격 저장소에 적용시키기
