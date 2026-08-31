import { createStore } from 'vuex'
import app from './modules/app'
import dict from './modules/dict'
import user from './modules/user'
import tagsView from './modules/tagsView'
import permission from './modules/permission'
import settings from './modules/settings'
import getters from './getters'

const store = createStore({
  modules: {
    app,
    dict,
    user,
    tagsView,
    permission,
    settings
  },
  getters
})

export default store
