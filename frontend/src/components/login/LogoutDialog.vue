<template>
  <va-sidebar-item>
    <va-sidebar-item-content @click="logout = !logout">
        <va-icon name="logout"/>
        <va-sidebar-item-title >
              logout
        </va-sidebar-item-title>
    </va-sidebar-item-content>
  </va-sidebar-item>
  <va-modal v-model="logout" hideDefaultActions="true" blur>
        <va-card-title>
            Logout
        </va-card-title>
        <va-card-content  style="width: 20rem;">
        <p>
          confirm exit?
        </p>
        </va-card-content>
        <va-card-actions>
            <va-button @click="set_auth(false)">
                logout
            </va-button>
        </va-card-actions>
  </va-modal>
</template>

<script>
import { mapMutations } from 'vuex'
export default {
  name: 'LogoutDialog',
  components: {

  },
  data(){
    return {
        logout: false
    }
  },
  methods: {
    ...mapMutations(['set_auth'])
  }
}
</script>

<style scoped>
</style>