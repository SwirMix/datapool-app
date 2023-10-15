<template>
    <div class="row justify-space-around" style="padding-top: 20%;">
        <v-container fill-height>
          <v-layout row wrap align-center>
              <div class="flex flex-col xs" style="height: 100%;">
                <div class="item row">
                  <div class="flex flex-col xs12" style="text-align: center;">
                    <va-avatar
                        size="10rem"
                        class="mr-4"
                        :src='require("@/assets/mock.png")'
                    >
                    </va-avatar>
                  </div>
                  <div class="flex flex-col xs12" style="text-align: center;">
                    <h1>404 NOT FOUND</h1>
                  </div>
                </div>
              </div>
          </v-layout>
        </v-container>
    </div>
</template>

<script>

export default {
  name: 'NotFoundPage',
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