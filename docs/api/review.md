## API

### Review

**description**

프로젝트, 스터디의 리뷰

**base_url** : `/review`

**table**

```json
{
    "id" : "Number",
    "author_name" : "Number(년도)",
    "comment" : "String(팀 이름)",
    "lecture" : "String(스터디,프로젝트 구분/id ex)STUDY/3 )",
}
```

##### getList

**url** : `/`

**METHOD** : `GET`

**URL PARAMS**

| name       | type   | required | description |
| ---------- | ------ | -------- | ----------- |
| pageNumber | Number | True     | 페이지네이션 오프셋 |
| pageSize   | Number | True     | 페이지 사이즈     |

**RESPONSE**

```json
// 200 OK
[
    {
    "id" : "Number",
    "author_name" : "Number(년도)",
    "comment" : "String(팀 이름)",
    "lecture" : "String(스터디,프로젝트 구분/id ex)STUDY/3 )",
    },
    {
    "id" : "Number",
    "author_name" : "Number(년도)",
    "comment" : "String(팀 이름)",
    "lecture" : "String(스터디,프로젝트 구분/id ex)STUDY/3 )",
    }
]
```

##### getReviewById

**url** : `/<id>`

**METHOD** : `GET`

**URL PARAMS**

| name | type   | required | description |
| ---- | ------ | -------- | ----------- |
| id   | Number | True     | 멤버 고유 id    |

**RESPONSE**

```json
// 200 OK
{
    "id" : "Number",
    "author_name" : "Number(년도)",
    "comment" : "String(팀 이름)",
    "lecture" : "String(스터디,프로젝트 구분/id ex)STUDY/3 )",
}
```

##### createReview

**url** : `/`

**METHOD** : `POST`

**Request Body**

```json
// content-type : json
{
    "author_name" : "Number(년도)",
    "comment" : "String(팀 이름)",
    "lecture" : "String(스터디,프로젝트 구분/id ex)STUDY/3 )",
}
```

**RESPONSE**

```json
// 200 OK
{
    "id" : "Number",
    "author_name" : "Number(년도)",
    "comment" : "String(팀 이름)",
    "lecture" : "String(스터디,프로젝트 구분/id ex)STUDY/3 )",
}
```

##### updateReview

**url** : `/`

**METHOD** : `PUT`

**Request Body**

```json
// content-type : json
{
    "id" : "Number",
    "author_name" : "Number(년도)",
    "comment" : "String(팀 이름)",
    "lecture" : "String(스터디,프로젝트 구분/id ex)STUDY/3 )",
}
```

**RESPONSE**

```json
// 200 OK
{
    "id" : "Number",
    "author_name" : "Number(년도)",
    "comment" : "String(팀 이름)",
    "lecture" : "String(스터디,프로젝트 구분/id ex)STUDY/3 )",
}
```

##### deleteReview

**url** : `/<id>`

**METHOD** : `DELETE`

**URL PARAMS**

| name | type   | required | description |
| ---- | ------ | -------- | ----------- |
| id   | Number | True     | 멤버 고유 id    |

**RESPONSE**

```json
// 200 OK
{
}
```
