<template>
    <div v-if="!check_auth()">
        <div class="row justify-space-around" style="padding-top: 20%;">
            <v-container fill-height>
              <v-layout row wrap align-center>
                  <div class="flex flex-col xs" style="height: 100%;">
                    <div class="item">
                      <LoginPage/>
                    </div>
                  </div>
              </v-layout>
            </v-container>
        </div>
    </div>
    <div v-else>
        <PageAppBar/>
        <div class="page-frame">
          <div style="height: 100%;">
          <SideBarNavigation id="events"/>
          </div>
          <router-view class="main ma-3">
          </router-view>
        </div>
    </div>
</template>

<script>
import PageAppBar from '@/components/PageAppBar.vue'
import SideBarNavigation from '@/components/SideBarNavigation.vue'
import LoginPage from '@/components/login/LoginPage.vue'
import {mapGetters, mapMutations} from 'vuex'

export default {
  name: 'App',
  components: {
    PageAppBar,
    SideBarNavigation,
    LoginPage
  },
  computed: {

  },
  data(){
    return {

    }
  },
  methods: {
    ...mapGetters(
        ['check_auth', 'get_toast', 'get_global_admin']
    ),
    ...mapMutations(
        ['set_toast']
    )
  }
}
</script>

<style scoped>
.page-frame
{
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  width: 100%;
  font-size: 12.3px;
}
font-size: 20%;
#menu {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
}

#content {min-height: calc(100%); color: ffdb46;}
</style>