var JPushPlugin = function () {}

var PLUGIN_NAME = 'JPushPlugin'

var EventHandlers = {
  'receiveNotification': [],
  'receiveMessage': [],
  'openNotification': [],
  'clickNotification': [], // 点击了通知栏中的自定义按钮。
  'connectionChange': [],
  'receiveRegistrationId': []
}

var JPushPlugin = {
  init: function () {
    var success = function (result) {
      if (!EventHandlers.hasOwnProperty(result.eventName)) {
        return
      }

      for (var i in EventHandlers[result.eventName]) {
        EventHandlers[result.eventName][i].apply(undefined, [result.value])
      }
    }

    exec(success, null, PLUGIN_NAME, 'init', [])
  },
  /**
   * @param {object} params: {'enable': boolean}
   */
  setDebugMode: function (params) {
    exec(null, null, PLUGIN_NAME, 'setDebugMode', [])
  },
  /**
   * @param {function} success: (rId) => {} // 以参数形式返回 registration id。
   */
  getRegistrationId: function (success) {
    exec(null, null, PLUGIN_NAME, 'getRegistrationId', [])
  },
  /**
   * 停止推送服务。和 resumePush 需要成对调用（Android only）。
   */
  stopPush: function () {
    if (device.platform === 'Android')
      exec(null, null, PLUGIN_NAME, 'stopPush', [])
  },
  /**
   * 恢复推送服务。和 resumePush 需要成对调用（Android only）。
   */
  resumePush: function () {
    if (device.platform === 'Android')
      exec(null, null, PLUGIN_NAME, 'resumePush', [])
  },
  /**
   * @param {function} success: (isPushStopped) => {}
   */
  isPushStopped: function (success) {
    if (device.platform === 'Android')
      exec(success, null, PLUGIN_NAME, 'isPushStopped', [])
  }
}


module.exports = new JPushPlugin()