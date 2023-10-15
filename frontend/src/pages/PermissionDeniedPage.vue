<template>
    <div class="row justify-space-around" style="padding-top: 20%;">
        <v-container fill-height>
          <v-layout row wrap align-center>
              <div class="flex flex-col xs" style="height: 100%; text-align: center;">
                <div class="item row">
                  <div class="flex flex-col xs12" >
                    <va-avatar
                        size="10rem"
                        class="mr-4"
                        :src='require("@/assets/mock.png")'
                    >
                    </va-avatar>
                  </div>
                  <div class="flex flex-col xs12">
                    <h1>403 PERMISSION DENIED</h1>
                  </div>
                </div>
              </div>
          </v-layout>
        </v-container>
    </div>
</template>

<script>

export default {
  name: 'AdminPage',
  components: {

  },
  props: {
    msg: String
  },
  data(){
    return {

    }
  },
  created(){

  }
}
</script>

<style lang="scss" scoped>
</style>