<template>
    <div class="register" v-show="show">
        <a-form method="POST" action="/signup" class="form" :form="form" @submit="handleSubmit">
            <a-form-item class="field">
                <a-input
                        v-decorator="['login', { rules: [{ required: true, message: 'Заполните это поле!' }] }]"
                        placeholder="Логин"
                >
                    <a-icon slot="prefix" type="user"></a-icon>
                </a-input>
            </a-form-item>
            <a-form-item class="field">
                <a-input-password
                        v-decorator="['password', { rules: [
            { required: true, message: 'Заполните это поле!' },
            { validator: validateToNextPassword}
            ] }]"
                        placeholder="Пароль"
                >
                    <a-icon slot="prefix" type="key"></a-icon>
                </a-input-password>
            </a-form-item>
            <a-form-item class="field">
                <a-input-password
                        v-decorator="['confirm', { rules: [
            { required: true, message: 'Заполните это поле!' },
            { validator: compareToFirstPassword, message: 'Пароли не совпадают!'}
            ], validateFirst: true}]"
                        placeholder="Подтвердите пароль"
                >
                    <a-icon slot="prefix" type="check"></a-icon>
                </a-input-password>
            </a-form-item>
            <a-form-item class="ok">
                <a-button html-type="submit" type="primary">Зарегистрироваться</a-button>
            </a-form-item>
        </a-form>
    </div>
</template>

<script>
    import Vue from 'vue'

    import {Button, Input, Form, Icon, message} from "ant-design-vue";
    import 'ant-design-vue/lib/button/style/index.css'
    import 'ant-design-vue/lib/input/style/index.css'
    import 'ant-design-vue/lib/form/style/css'
    import 'ant-design-vue/lib/message/style/css'
    import 'ant-design-vue/lib/icon/style/index.css'

    Vue.use(Button)
    Vue.use(Input)
    Vue.use(Form)
    Vue.use(Icon)

    export default {
        name: "Register",
        data() {
            return {
                form: this.$form.createForm(this, { name: 'register' }),
                show: false
            };
        },
        async beforeMount() {
            message.config({maxCount:1})
            await this.$store.dispatch('checkLogin')
            if (this.$store.state.isLogin) {
                this.$router.replace({ path: '/' })
            } else {this.show = true}
        },
        methods: {
            handleSubmit(e) {
                this.form.validateFields(async (err, values) => {
                    e.preventDefault();
                    if (!err) {
                        console.log('Received values of form: ', values);
                        let res = await this.$store.dispatch('register',{
                            login: this.form.getFieldValue('login'),
                            password: this.form.getFieldValue('password')
                        })
                        console.log(res)
                        if (res.status === 1) {
                            this.$router.replace({ path: '/' })
                        } else if (res.status === -2) {
                            message.error('Введенный логин занят. Попробуйте другой')
                        }
                    }
                });
            },
            compareToFirstPassword(rule, value, callback) {
                if (this.form.getFieldValue('password') !== value) {
                    callback('passwords is not matching');
                    return
                }
                callback()
            },
            validateToNextPassword(rule, value, callback) {
                if (this.form.getFieldValue('confirm')) {
                    this.form.validateFields(['confirm'], {force: true});
                }
                callback()
            },
        },
    }
</script>

<style scoped>
    .register {
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;

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
    .field {
        margin: 5px;
    }
    .ant-input-affix-wrapper {
        width: 300px;
        height: 32px;
    }
    .ok {
        margin-top: 10px;
    }
</style>
