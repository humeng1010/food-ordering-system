function loginApi(data) {
    return $axios({
      'url': '/user/login',
      'method': 'post',
      data
    })
  }
const sendMsg = (data)=> $axios({
    'url': '/user/sendMsg',
    'method': 'post',
    data
})
function loginoutApi() {
  return $axios({
    'url': '/user/loginout',
    'method': 'post',
  })
}

  