# Member

### Create

1. 사용자 추가하기
2. 사용자의 역할 추가하기

### Read

1. 사용자 리스트 불러오기
2. 특정 역할의 사용자 리스트 불러오기
3. 특정 사용자 불러오기

### Update

1. 사용자 업데이트
    - 사용자 기본정보 업데이트
    - 사용자 역할 업데이트

### Delete

1. 사용자 삭제하기

---

### CREATE : 사용자 추가하기

***Method / URL*** : **`POST`**  */members/*

***RequestBody***

- ***content-type*** : *application/json*

```json
{
  "name" : "VARCHAR(255) 사용자 이름",
  "studentId" : "VARCHAR(255) 사용자 학번"
}
```

***Request Body Example***

```json
{
  "name" : "jaemin",
  "studentId" : "201911190"
}
```



***Response***

`200 OK` : 사용자 생성 성공

```json
{
  "id" : 1,
  "name" : "jaemin",
  "studentId" : "201911190",
  "attendance" : " ",
  "roles" : []
}
```

`409 Conflict` : 이미 존재하는 학번으로 사용자 추가를 시도했을 경우

```json
{
  "message" : "중복 회원입니다"
}
```



### CREATE : 사용자의 역할 추가하기

***Method / URL*** : **`PUT`**  */members/*

***RequestBody***

- ***content-type*** : *application/json*

```json
{
  "id" : "BIGINT 사용자 고유 Id",
  "name" : "VARCHAR(255) 사용자 이름",
  "studentId" : "VARCHAR(255) 학번",
  "attendance" : "VARCHAR(255) 출석",
  "roles" : 
  	[
      {
        "groupName" : "VARCHAR(255) 소속 그룹",
        "role" : "VARCHAR(255) 역할",
        "status" : "1 ( 0 : 기존 역할, 1 : 추가되는 역할, 2 : 제거할 역할 )"
      }
    ]
}
```

***Request Body Example***

```json
{
  "id" : 1,
  "name" : "jaemin",
  "studentId" : "201911190",
  "attendance" : "",
  "roles" : 
	  [
      {
        "groupName" : "비빔밥 웹 프로젝트",
        "role" : "백엔드",
        "status" : "1"
      },
      {
        "groupName" : "오픈스택 스터디",
        "role" : "스터디원",
        "status" : "1"
      }
    ]
}
```





***Response***

`200 OK` : 역할 추가 성공

```json
{
  "id" : 1,
  "name" : "jaemin",
  "studentId" : "201911190",
  "attendance" : "",
  "roles" : 
	  [
      {
        "id" : 1,
        "groupName" : "비빔밥 웹 프로젝트",
        "role" : "백엔드",
      },
      {
        "id" : 2,
        "groupName" : "오픈스택 스터디",
        "role" : "스터디원",
      }
    ]
}
```

`404 Not Found` : 존재하지 않는 사용자( *id* )에 역할 추가를 시도할 경우

```json
{
  "message" : "존재하지 않는 회원입니다"
}
```



### READ : 사용자 리스트 불러오기

***Method / URL*** : **`GET`**  */members/*

***QueryParameter***

| Name |  Type  | Required |        Description         |
| :--: | :----: | :------: | :------------------------: |
| page | Number |   True   |        페이지 번호         |
| size | Number |   True   | 한 페이지에 불러올 멤버 수 |



***Response***

`200 OK` : 멤버 조회 성공

```json
[
    {
        "id": 1,
        "name": "memberA",
        "studentId": "1111",
        "attendance": "",
        "roles": []
    },
    {
        "id": 2,
        "name": "memberB",
        "studentId": "2222",
        "attendance": "",
        "roles": []
    }
]
```

`400 Bad Request` : 범위를 벗어난 페이지에 대해 요청할 경우

```json
{
    "message": "유효하지 않은 페이지입니다"
}
```



### READ : 특정 역할의 사용자 리스트 불러오기

***Method / URL*** : **`GET`**  */members/*

***QueryParameter***

|   Name    |  Type  | Required |   Description    |
| :-------: | :----: | :------: | :--------------: |
| groupName | String |   True   | 해당 역할의 소속 |
|   Role    | String |   True   |    해당 역할     |



***Response***

`200 OK` : 멤버 조회 성공 ( */members/?groupName=비빔밥&role=admin* )

```json
[
  	  {
        "id": 1,
        "name": "memberA",
        "studentId": "1111",
        "attendance": "",
        "roles": 
      	[
            {
                "id": 1,
                "groupName": "비빔밥",
                "role": "admin"
            }
        ]
	    },
      {
        "id": 1,
        "name": "memberB",
        "studentId": "2222",
        "attendance": "",
        "roles": [
            {
                "id": 2,
                "groupName": "비빔밥",
                "role": "admin"
            },
            {
                "id": 3,
                "groupName": "자바스터디",
                "role": "튜터"
            }
        ]
    }
]
```

`400 Bad Request` : 존재하지 않는 역할( *groupName, role* ) 에 대해 요청한 경우

```json
{
    "message": "존재하지 않는 역할입니다"
}
```



### READ : 특정 사용자 불러오기

***Method / URL*** : **`GET`**  */members/<id>*

***URLParameter***

| Name |  Type  | Required | Description |
| :--: | :----: | :------: | :---------: |
|  Id  | Number |   True   |  사용자 id  |



***Response***

`200 OK` : 멤버 조회 성공

```json
{
    "id": 1,
    "name": "test",
    "studentId": "1",
    "attendance": "",
    "roles": []
}
```

`400 Bad Request` : 존재하지 않는 회원 id에 대해 요청한 경우

```json
{
    "message": "존재하지 않는 회원입니다"
}
```



### UPDATE : 사용자 기본정보 업데이트

***Method / URL*** : **`PUT`**  */members/*

***RequestBody***

- ***content-type*** : *application/json*

```json
{
  "id" : "BIGINT 사용자 고유 Id",
  "name" : "VARCHAR(255) 사용자 이름",
  "studentId" : "VARCHAR(255) 학번",
  "attendance" : "VARCHAR(255) 출석",
  "roles" : 
  	[
      {
        "groupName" : "VARCHAR(255) 소속 그룹",
        "role" : "VARCHAR(255) 역할",
        "status" : "1 ( 0 : 기존 역할, 1 : 추가,수정되는 역할, 2 : 제거할 역할 )"
      }
    ]
}
```

***Request Body Example***

```json
{
  "id" : 1,
  "name" : "jaemin jaemin",
  "studentId" : "201911190",
  "attendance" : "",
  "roles" : 
	  [
      {
        "groupName" : "비빔밥 웹 프로젝트",
        "role" : "백엔드",
        "status" : "0"
      },
      {
        "groupName" : "오픈스택 스터디",
        "role" : "스터디원",
        "status" : "0"
      }
    ]
}
```



***Response***

`200 OK` : 멤버 수정 성공

```json
{
    "id": 1,
    "name": "jaemin jaemin",
    "studentId": "201911190",
    "attendance": "",
    "roles": [
        {
            "id": 1,
            "groupName": "비빔밥",
            "role": "admin"
        },
        {
            "id": 2,
            "groupName": "자바스터디",
            "role": "튜터"
        }
    ]
}
```

`404 Not Found` : 존재하지 않는 사용자( *id* )에 수정을 시도할 경우

```json
{
  "message" : "존재하지 않는 회원입니다"
}
```



### UPDATE : 사용자 역할정보 업데이트

***Method / URL*** : **`PUT`**  */members/*

***RequestBody***

- ***content-type*** : *application/json*

```json
{
  "id" : "BIGINT 사용자 고유 Id",
  "name" : "VARCHAR(255) 사용자 이름",
  "studentId" : "VARCHAR(255) 학번",
  "attendance" : "VARCHAR(255) 출석",
  "roles" : 
  	[
      {
        "id" : "NUMBER 역할id - 기존 역할 수정시에 필요"
        "groupName" : "VARCHAR(255) 소속 그룹",
        "role" : "VARCHAR(255) 역할",
        "status" : "1 ( 0 : 기존 역할, 1 : 추가,수정되는 역할, 2 : 제거할 역할 )"
      }
    ]
}
```

***Request Body Example***

```json
{
  "id" : 1,
  "name" : "jaemin jaemin",
  "studentId" : "201911190",
  "attendance" : "",
  "roles" : 
	  [
      {
        "id" : 1,
        "groupName" : "비빔밥 웹 프로젝트 - 동아리 웹사이트 만들기",
        "role" : "백엔드 - 스프링",
        "status" : "1"
      },
      {
        "groupName" : "오픈스택 스터디",
        "role" : "스터디원",
        "status" : "0"
      }
    ]
}
```



***Response***

`200 OK` : 멤버 수정 성공

```json
{
    "id": 1,
    "name": "jaemin jaemin",
    "studentId": "201911190",
    "attendance": "",
    "roles": [
	      {
            "id": 1,
            "groupName": "비빔밥 웹 프로젝트 - 동아리 웹사이트 만들기",
            "role": "백엔드 - 스프링"
        },
        {
            "id": 2,
            "groupName": "자바스터디",
            "role": "튜터"
        }
    ]
}
```

`404 Not Found` : 존재하지 않는 사용자( *id* )에 수정을 시도할 경우

```json
{
  "message" : "존재하지 않는 회원입니다"
}
```



### DELETE : 특정 사용자 제거하기

***Method / URL*** : **`GET`**  */members/<id>*

***URLParameter***

| Name |  Type  | Required | Description |
| :--: | :----: | :------: | :---------: |
|  Id  | Number |   True   |  사용자 id  |



***Response***

`204 No Content` : 멤버 삭제 성공

`400 Bad Request` : 존재하지 않는 회원 id에 대해 요청한 경우

```json
{
  "message": "존재하지 않는 회원입니다"
}
```

