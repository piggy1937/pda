"# pda"
{
    "errCode": 1,
    "errMsg": "success",
    "data": {
        "list": [],
        "pagination": {
            "total": 100,
            "pageIndex": 1,
            "pageSize": 10
        }
    }
}

1XX     100-101     消息提示

2XX     200-206     成功     200(OK)

3XX     300-305    重定向   302（found）一个链接到宁外一链接，304（NotModified）资源没有改变还是使用上次的缓存，

4XX     400-415    客户端错误  401（unauthorized）需要授权认证，403（forbidden）禁止访问，404(）没有找到资源，405（Method not allowed）方法不可用（get/post）

                               413（Request Entity Too  Large）请求的数据过大

5XX     500-505    服务端错误  500（Internal ServerError）服务列表错误，502（bad gateway）网关发生了错误，503（service Unavailable）服务器不可以  ，504(Gate TimeOut)放回超时
