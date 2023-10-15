<template>
  <va-navbar
    style="height: 50px;"
    class="flex flex-col md12"
  >
      <va-navbar-item class="navbar-item-slot">
        Amber project (KV) ©{{new Date().getFullYear() }}
      </va-navbar-item>
  </va-navbar>
</template>

<script>

export default {
  name: 'NavBar',
  components: {

  },
  props: {
    msg: String
  },
  data(){
    return {
    }
  },
  methods: {

  }
}
</script>

<style scoped>

</style>
