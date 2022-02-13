<template>
  <div class="app">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'app',
}
</script>

<style>
  @font-face {
    font-family: Montserrat;
    src: url(../assets/Montserrat-Light.ttf);
  }
  .app {
    min-height: 100vh;
    font-family: Montserrat, sans-serif;
    background: linear-gradient(rgba(255, 255,255, 0.75), rgba(0, 0,0, 0.75)), url("../assets/logo.webp") center no-repeat;
  }
</style>
