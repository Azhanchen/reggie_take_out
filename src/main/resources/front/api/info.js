//分页查询订单
function infoPagingApi(data) {
    return $axios({
        'url': '/info/userPage',
        'method': 'get',
        params: {...data}
    })
}
//确认已知
function updateIsReadApi(data) {
    return $axios({
        'url': '/info/read',
        'method': 'post',
        data
    })
}
