<template>
    <div class="note-list" v-show="show">
        <div class="menu">
        <a-popconfirm
                      title="Вы уверены, что хотите выйти?"
                      @confirm="logout"
                      okText="Уверен"
                      cancelText="Закрыть"
        >
            <a-button :icon="$store.state.loading ? 'loading' : 'check'" type="primary" ghost href="#">{{$store.state.userLogin}}</a-button>
        </a-popconfirm>
        </div>
        <div class="list">
            <note :access="true" ref="notes" v-for="note in $store.state.notes" :key="note.id" @input="inputHandler($event, note.id)" :initText="note.text"></note>
            <note @input="create"></note>
        </div>
    </div>
</template>

<script>
    import Vue from 'vue'

    import { Popconfirm, Button, Icon } from 'ant-design-vue'
    import 'ant-design-vue/lib/icon/style/css'
    import 'ant-design-vue/lib/popconfirm/style/css'
    import 'ant-design-vue/lib/button/style/css'
    Vue.use(Popconfirm)
    Vue.use(Button)
    Vue.use(Icon)

    import Note from './Note'

    let timeout = null
    export default {
        name: "NoteList",
        components: {
            Note
        },
        data() {
            return {
                show: false,
            }
        },
        async beforeMount() {
            await this.$store.dispatch('checkLogin')
            if (this.$store.state.isLogin) {
                await this.$store.dispatch('loadNotes')
                this.show = true
            } else {
                this.$router.replace({ path: 'login' })
            }
        },
        methods: {
            async inputHandler(e, id) {
                if (id < 0) return
                if (timeout != null) {
                    clearTimeout(timeout)
                    timeout = null
                }
                timeout = setTimeout(() => {
                    this.$store.dispatch('updateNote', {id, text: e.target.value})
                }, 500)
            },
            //  Добавить новую заметку и отправить запрос на бэк
            async create(e) {
                //  Добавить заметку с id = -1 на страницу
                await this.$store.dispatch('addNoteToPage', {note: {id: -1, text: e.key}})
                this.$refs.notes[this.$store.state.notes.length - 1].$el.focus()

                //  Отправить запрос на бэк, получить id новой заметки и переназначить id у заметки с id = -1 полученным id
                let res = await this.$store.dispatch('addNote', {text: e.key})
                await this.$store.dispatch('updateNoteInPage', {id: this.$store.state.notes.length - 1,
                    note: {id: +res.message, text: this.$refs.notes[this.$store.state.notes.length - 1].$el.value}})
                this.$refs.notes[this.$store.state.notes.length - 1].$el.focus()

                //Если во время запроса заметка изменилась, отправить новый запрос на обновление заметки
                if (this.$refs.notes[this.$store.state.notes.length - 1].$el.value !== e.key) {
                    await this.$store.dispatch('updateNote', {id: +res.message,
                        text: this.$refs.notes[this.$store.state.notes.length - 1].$el.value
                    })
                }
            },
            //  Отправить запрос бэку на разлогин
            async logout() {
                await this.$store.dispatch('logout')
                this.$router.replace({ path: 'login' })
            }
        }
    }
</script>

<style scoped>
    .note-list {
        min-height: 100vh;
        display: flex;
        flex-direction: column;
    }
    .list {
        display: grid;
        grid-template-rows: repeat(auto-fill, 100px);
        grid-template-columns: repeat(auto-fill, 300px);
        grid-gap: 10px;
        justify-content: center;
        width: 100%;
        margin-top: 20px;
    }
    .menu {
        display: flex;
        width: 100%;
        align-self: flex-end;
        margin: 15px 15px 0 0;
    }
    .menu a {
        margin-left: auto;
        margin-right: 15px;
    }
    .menu button {
        margin-left: 30px;
    }
</style>
