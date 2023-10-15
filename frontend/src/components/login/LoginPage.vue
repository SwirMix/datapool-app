<template>
    <va-card>
        <va-card-title>
            <va-avatar
                size="small"
                class="mr-4"
                :src='require("@/assets/amber.png")'
            >
            </va-avatar>
            Amber project (KV) - Webpool ADMINUI
            <va-divider></va-divider>
        </va-card-title>
        <va-card-content>
          <div class="row">
            <va-input
                v-model="email"
                class="mt-3 flex flex-col xs12"
                label="email"
            />
            <va-input
                v-model="pass"
                class="mt-3 flex flex-col xs12"
                label="password"
                type="password"
            />
            <va-divider class="mt-3 flex flex-col xs12"></va-divider>
            <va-select
                  v-if="this.get_success_login_request()"
                  class="mt-3 flex flex-col xs12"
                  v-model="selected_project"
                  label="projects"
                  :options="get_projects_for_options_field()"
            />
          </div>
        </va-card-content>
        <va-card-actions>
           <va-button
              v-if="!(this.get_success_login_request())"
              class="flex flex-col md2"
              icon="cached"
              size="small"
              @click="login()"
           >
              <div class="ma-2">login</div>
           </va-button>
           <va-button
              v-else
              icon="login"
              size="small"
              @click="set_current_project(selected_project.id)"
           >
              <div class="ma-2">enter the project</div>
           </va-button>
           <va-alert
            color="warning"
            class="mb-6"
            v-if="(get_projects_for_options_field().length === 0) & (this.get_user_info().email !== '')"
           >
            Your account is not connected to any of the projects. Contact the administrator.
           </va-alert>
        </va-card-actions>
    </va-card>
</template>

<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'

export default {
  name: 'LoginPage',
  components: {

  },
  data(){
    return {
        login_status: false,
        email: '',
        pass: '',
        selected_project: {
            id: "",
            name: "",
            description: "",
            active: true,
            userId: "",
            role: "",
            created: ""
        },
    }
  },
  methods: {
    ...mapActions(
        ['auth']
    ),
    ...mapMutations(
        ['set_current_project']
    ),
    ...mapGetters(
        ['get_projects_for_options_field', 'get_user_info', 'get_success_login_request', 'get_global_admin']
    ),
    login(){
        let body = {
            email: this.email,
            password: this.pass
        }
        let status = this.auth(body)
        console.log(status)
    }
  }
}
</script>

<style scoped>
</style>