let subscribeEventList = [];




// 获取订阅消息模板
function getSubscribeTemplate(data) {
    subscribeEventList = data;
}

// 订阅消息
function subscribeMessage(event, callback= undefined) {
  debugger
  let tmplIds = [];
  if (typeof event === 'string') {
    const temp = subscribeEventList.find(item => item.title.includes(event));
    if (temp) {
      tmplIds.push(temp.priTmplId);
    }
  }
  if (typeof event === 'object') {
    event.forEach((e) => {
      const temp = subscribeEventList.find(item => item.title.includes(e));
      if (temp) {
        tmplIds.push(temp.priTmplId);
      }
    });
  }
  if (tmplIds.length === 0) return;

  uni.requestSubscribeMessage({
    tmplIds,
		success: ()=>{
      // 不管是拒绝还是同意都触发
      callback && callback()
		},
    fail: (err) => {
      console.log(err);
    },
  });
}

module.exports = {
  subscribeMessage,
  getSubscribeTemplate
}
