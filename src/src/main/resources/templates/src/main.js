import Vue from 'vue'

import store from './store'
import router from './router'
import App from './components/App'



new Vue({
  render: h => h(App),
  store,
  router
}).$mount('#app')
