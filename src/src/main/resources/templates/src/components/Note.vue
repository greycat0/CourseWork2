<template>
    <div class="wrap">
        <a-textarea v-model="text" @input="inputHandler" class="note"></a-textarea>
        <a-button class="important-button" @click="switchImportant" type="link" icon="exclamation"></a-button>
    </div>
</template>

<script>
    import Vue from "vue";
    import { Input, Button } from 'ant-design-vue'
    import 'ant-design-vue/lib/input/style/index.css'
    import 'ant-design-vue/lib/button/style/index.css'

    Vue.use(Input)
    Vue.use(Button)

    export default {
        name: "Note",
        data() {
            return {
                text: this.initText
            }
        },
        props: [
            'id',
            'initText',
            'enterable',
            'access'
        ],
        methods: {
            inputHandler(e) {
                this.$emit('input', e)
                if (!this.access) {
                    e.preventDefault()
                }
            },
            switchImportant() {
                alert('ok')
            }
        }
    }
</script>

<style scoped>
    .note {
        position: relative;
        height: 100px;
        background-color: rgba(255, 255, 255, 0.75);
        resize: none;
    }
    .delete {
        position: absolute;
        bottom: 10px;
        right: 10px;
    }
    .important-button {
        position: absolute;
        bottom: 10px;
        right: 10px;
    }
    .wrap {
        position: relative;
    }
</style>
