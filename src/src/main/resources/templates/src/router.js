import Vue from "vue";
import VueRouter from 'vue-router'

Vue.use(VueRouter)


const routes = [
    { path: '/', component: () => import('./components/NoteList') },
    { path: '/login', component: () => import('./components/Login') },
    { path: '/register', component: () => import('./components/Register') },
]

export default new VueRouter({
    mode: 'history',
    routes
})
