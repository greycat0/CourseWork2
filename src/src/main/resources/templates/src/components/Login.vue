<template>
    <div class="login" v-show="show">
        <div class="form">
            <a-input v-model="login" placeholder="Логин">
                <a-icon slot="prefix" type="user"></a-icon>
            </a-input>
            <a-input-password v-model="password" placeholder="Пароль">
                <a-icon slot="prefix" type="key"></a-icon>
            </a-input-password>
            <a-button :loading="$store.state.loading" @click="logIn" type="primary">Войти</a-button>
        <p class="offer">Еще не создали аккаунт?
            <a href="#" @click="$router.push('/register')">Зарегистрируйтесь</a>
            сейчас!</p>
        </div>
    </div>
</template>

<script>
    import Vue from "vue";

    import { Input, Button, Icon, message } from 'ant-design-vue'
    Vue.use(Input)
    Vue.use(Button)
    Vue.use(Icon)

    import 'ant-design-vue/lib/input/style/index.css'
    import 'ant-design-vue/lib/button/style/index.css'
    import 'ant-design-vue/lib/icon/style/index.css'
    import 'ant-design-vue/lib/message/style/css'

    export default {
        name: "Login",
        data() {
            return {
                login: '',
                password: '',
                show: false
            }
        },
        async beforeMount() {
            message.config({maxCount:1})
            //Проверка на авторизацию
            await this.$store.dispatch('checkLogin')
            //Если авторизован - редирект на страницу заметок
            if (this.$store.state.isLogin) {
                this.$router.replace({ path: '/' })
            } else {this.show = true}
        },
        methods: {
            //Отправка запроса на авторизацию
            async logIn() {
                //Запрос
                await this.$store.dispatch('login', {login: this.login, password: this.password})
                //Если успешно авторизовался - редирект на страницу заметок
                if (this.$store.state.isLogin) {
                    this.$router.replace({ path: '/' })
                } else {    //Если нет - выводим сообщение об ошибке
                    message.error('Неверный логин или пароль!')
                    this.password = ''
                }
            }
        }
    }
</script>

<style scoped>
    .login {
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
    }
    .form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        background-color: rgba(255, 255, 255, 0.75);
        padding: 20px;
        border-radius: 5px;
    }
    .ant-input-affix-wrapper {
        margin: 10px;
        width: 300px;
        height: 32px;
    }
    .ant-btn {
        margin: 10px;
        width: 100px;
    }
    .offer {
        color: darkslategrey;
        font-size: 16px;
    }
    .offer a {
        color: darkcyan;
    }
</style>
