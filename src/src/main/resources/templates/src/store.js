import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)
console.log
export default new Vuex.Store({
    state: {
        api: `http://${location.host}/api`,
        isLogin: false,
        userLogin: '',
        notes: [],
        loading: false
    },
    mutations: {
        setIsLogin(state, value) {
            state.isLogin = value
        },
        setUserLogin(state, value) {
            state.userLogin = value
        },
        setNotes(state, notes) {
            state.notes = notes
        },
        setLoading(state, value) {
            state.loading = value
        }
    },
    actions:  {
        async loadNotes({state, commit}) {
            commit('setLoading', true)
            let res = await axios.get(`${state.api}/notes`)
            commit('setLoading', false)

            if (res.data.status === 1) {
                commit('setNotes', JSON.parse(res.data.message).sort((a, b) => a.id - b.id))
            }
        },
        async addNote({state, commit}, {text}) {
            commit('setLoading', true)
            let res = await axios.get(`${state.api}/addnote?text=${text}`)
            commit('setLoading', false)
            return res.data
        },
        async addNoteToPage({state, commit}, {note}) {
            let notes = state.notes
            notes.push(note)
            commit('setNotes', notes)
        },
        async updateNoteInPage({state, commit},{id, note}) {
            // let notes = state.notes
            // notes[id] = note
            // commit('setNotes', notes)
            Vue.delete(state.notes, id)
            Vue.set(state.notes, id, note)
        },
        async updateNote({state, commit}, {id, text}) {
            commit('setLoading', true)
            let res = await axios.get(`${state.api}/updatenote/${id}?text=${text}`)
            commit('setLoading', false)
        },
        async login({state, commit}, {login, password}) {
            commit('setLoading', true)
            let res = await axios.get(`${state.api}/login?login=${login}&password=${password}`)
            commit('setLoading', false)

            if (res.data.status === 1) {
                commit('setIsLogin', true)
            } else {
                commit('setIsLogin', false)
            }

        },

        async logout({state, commit}) {
            commit('setLoading', true)
            await axios.get(`${state.api}/logout`)
            commit('setLoading', false)
        },

        async checkLogin({state, commit}) {
            commit('setLoading', true)
            let res = await axios.get(`${state.api}/loginstatus`)
            commit('setLoading', false)

            if (res.data.status === 1) {
                commit('setIsLogin', true)
                commit('setUserLogin', res.data.message)
            } else {
                commit('setIsLogin', false)
            }
        },

        async register({state, commit}, {login, password}) {
            commit('setLoading', true)
            let res = await axios.get(`${state.api}/register?login=${login}&password=${password}`)
            commit('setLoading', false)

            return res.data
        }
    }
})
