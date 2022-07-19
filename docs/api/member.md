## API

### Member

**description**

프로젝트, 스터디 에 속한 인원

**base_url** : `/member`

**table** 

```json
{
    "id": "Long",
    "name" : "String",
    "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
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
[
    {
    "id": "Number",
    "name" : "String",
    "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
    },

]
```

##### getMemberById

**url** : `/<id>`

**METHOD** : `GET`

**URL PARAMS** 

| name | type   | required | description |
| ---- | ------ | -------- | ----------- |
| id   | Number | True     | 멤버 고유 id    |

**RESPONSE**

```json
{
    "id": "Long",
    "name" : "String",
    "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
}
```

##### createMember

**url** : `/`

**METHOD** : `POST`

**Request Body** 

```json
{
    "name" : "String",
    "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
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

##### updateMember

**url** : `/`

**METHOD** : `PUT`

**Request Body**

```json
{
    "id" : "Number",
    "name" : "String",
    "attendance": "String(boolean 배열 01배열로 변환하여 저장)"
}
```

**RESPONSE**

```json
{
    "id" : "Number",
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
