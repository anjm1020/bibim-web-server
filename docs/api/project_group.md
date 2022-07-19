## API

### ProjectGroup

**description**

프로젝트

**base_url** : `/project/group`

**table**

```json
{
    "id" : "Number",
    "period" : "Number(년도)",
    "teamName" : "String(팀 이름)",                
    "leaderName" : "String(팀장 이름)",
    "content" : "String(본문)"
    "members" : 
        [
            {
                "id": "Long",
                "name" : "String",
                "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
            },    
            {
                "id": "Long",
                "name" : "String",
                "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
            },
            {
                "id": "Long",
                "name" : "String",
                "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
            },
        ]
}
```

##### getList

**url** : `/`

**METHOD** : `GET`

**URL PARAMS**

| name       | type   | required | description |
| ---------- | ------ | -------- | ----------- |
| pageNumber | Number | True     | 페이지네이션 오프셋 |
| pageSize   | Number | True     | 페이지 사이즈     |

**RESPONSE**

```json
{
[
    {
    "id" : "Number",
    "period" : "Number(년도)",
    "teamName" : "String(팀 이름)",                
    "leaderName" : "String(팀장 이름)",
    "content" : "String(본문)",
    "members" : 
        [
            {
                "id": "Long",
                "name" : "String",
                "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
            },    
            {
                "id": "Long",
                "name" : "String",
                "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
            },
            {
                "id": "Long",
                "name" : "String",
                "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
            },
        ]
    },
    {
    "id" : "Number",
    "period" : "Number(년도)",
    "teamName" : "String(팀 이름)",                
    "leaderName" : "String(팀장 이름)",
    "content" : "String(본문)",
    "members" : 
        [
            {
                "id": "Long",
                "name" : "String",
                "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
            },    
            {
                "id": "Long",
                "name" : "String",
                "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
            },
            {
                "id": "Long",
                "name" : "String",
                "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
            },
        ]
    }
]
}
```

##### getProjectGroupById

**url** : `/<id>`

**METHOD** : `GET`

**URL PARAMS**

| name | type   | required | description |
| ---- | ------ | -------- | ----------- |
| id   | Number | True     | 멤버 고유 id    |

**RESPONSE**

```json
{
    "id" : "Number",
    "period" : "Number(년도)",
    "teamName" : "String(팀 이름)",                
    "leaderName" : "String(팀장 이름)",
    "content" : "String(본문)"
    "members" : 
        [
            {
                "id": "Long",
                "name" : "String",
                "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
            },    
            {
                "id": "Long",
                "name" : "String",
                "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
            },
            {
                "id": "Long",
                "name" : "String",
                "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
            },
        ]
}
```

##### createMember

**url** : `/`

**METHOD** : `POST`

**Request Body**

```json
{
    "period" : "Number",
    "teamName" : "String",
    "leaderName" : "String",
    "content" : "String(본문)",
    "members" : 
        [
            "Number(member 고유 id)",
        ]
}
```

**RESPONSE**

```json
{
    "id" : "Number",
    "period" : "Number(년도)",
    "teamName" : "String(팀 이름)",                
    "leaderName" : "String(팀장 이름)",
    "content" : "String(본문)",
    "members" : 
        [
            {
                "id": "Long",
                "name" : "String",
                "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
            },    
            {
                "id": "Long",
                "name" : "String",
                "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
            },
            {
                "id": "Long",
                "name" : "String",
                "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
            },
        ]
}
```

##### updateMember

**url** : `/`

**METHOD** : `PUT`

**Request Body**

```json
{
    "id" : "Number",
    "period" : "Number",
    "teamName" : "String",
    "leaderName" : "String",
    "content" : "String(본"
    "members" : 
        [
            "Number(member 고유 id)",
        ]
}
```

**RESPONSE**

```json
{
    "id": "Number",
    "name" : "String",
    "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
}
```

##### deleteMember

**url** : `/<id>`

**METHOD** : `DELETE`

**URL PARAMS**

| name | type   | required | description |
| ---- | ------ | -------- | ----------- |
| id   | Number | True     | 멤버 고유 id    |

**RESPONSE**

```json
{
}
```
