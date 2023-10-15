<template>
    <va-sidebar
      hoverable
      minimized-width="64px"

      class="colored-sidebar"
      gradient
      style="--va-sidebar-min-height: 100%; --va-sidebar-height: 100%;"
    >
      <va-sidebar-item>
        <va-sidebar-item-content>
          <va-icon name="face"/>
          <va-sidebar-item-title style="white-space: normal;" class="justify-center">
            <b>{{info.email}}</b>
            <p>{{info.role}}</p>
          </va-sidebar-item-title>
        </va-sidebar-item-content>
      </va-sidebar-item>
      <va-divider/>
      <va-sidebar-item
        v-for="item in navigation"
        :key="item.title"
        :active="item.active"
        @click='pushRoute(item.path)'
      >
        <va-sidebar-item-content>
          <va-icon :name="item.icon"/>
          <va-sidebar-item-title >
              {{ item.title }}
          </va-sidebar-item-title>
        </va-sidebar-item-content>
      </va-sidebar-item>
      <va-spacer></va-spacer>
      <LogoutDialog/>
    </va-sidebar>
</template>

<script>
import LogoutDialog from '@/components/login/LogoutDialog.vue'
import {mapGetters} from 'vuex'

export default {
  name: 'SideBarNavigation',
  components: {
    LogoutDialog
  },
  props: {
    msg: String
  },
  computed: {
    info: function(){
        return this.get_user_info()
    }
  },
  data(){
    return {
        navigation: [
            {
                title: 'Caches',
                path: '/',
                icon: 'dashboard',
                active: true
            },
            {
                title: 'Tokens',
                path: '/tokens',
                icon: 'key',
                active: false
            },
            {
                title: 'journal',
                path: '/log',
                icon: 'auto_stories',
                active: false
            },
            {
                title: 'Datasources',
                path: '/datasources',
                icon: 'lan',
                active: false
            }
        ]
    }
  },
  methods: {
    ...mapGetters(
        ['get_user_info']
    ),
    pushRoute(path){
        this.$router.push(path)
        let data = [];
        for (let index in this.navigation){
            let menuItem = this.navigation[index];
            if (menuItem.path === path){
                menuItem.active = true;
            } else {
                menuItem.active = false;
            }
            data.push(menuItem)
        }
        this.navigation = data;
    }
  }
}
</script>

<style scoped>
</style>
