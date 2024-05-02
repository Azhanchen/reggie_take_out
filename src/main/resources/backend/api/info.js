// 查询列表接口
const getInfo = (params) => {
    return $axios({
        url: '/info/page',
        method: 'get',
        params
    })
}

// 新增数据接口
const addInfo = (params) => {
    return $axios({
        url: '/info',
        method: 'post',
        data: {...params}
    })
}

// 修改数据接口
const editInfo = (params) => {
    return $axios({
        url: '/info',
        method: 'put',
        data: {...params}
    })
}

// 上架下架通知
const InfoStatusByStatus = (params) => {
    return $axios({
        url: `/info/status/${params.status}`,
        method: 'post',
        params: {ids: params.ids}
    })
}
// 查询详情接口
const queryInfoById = (id) => {
    return $axios({
        url: `/info/${id}`,
        method: 'get'
    })
}
// 删除当前列的接口
const deleteInfo = (ids) => {
    return $axios({
        url: '/info',
        method: 'delete',
        params: {ids}
    })
}
