<template>
   <div class="row">
       <div class="flex flex-col md12 ma-3">
        <va-card stripe outlined>
            <va-card-title>
                <va-icon name="admin_panel_settings">
                </va-icon>
            </va-card-title>
            <va-card-content>
                <va-button
                  preset="primary"
                  size="small"
                  icon="description"
                  @click="sidebar_content_selector(0)"
                >
                  Create project
                </va-button>
                <va-button
                  preset="primary"
                  size="small"
                  icon="description"
                  class="ml-3"
                  @click="sidebar_content_selector(1)"
                >
                  Create user
                </va-button>
                <va-button
                  preset="primary"
                  size="small"
                  icon="settings"
                  class="ml-3"
                  @click="sidebar_content_selector(2)"
                >
                  Settings
                </va-button>
            </va-card-content>
        </va-card>
       </div>
       <div class="flex flex-col md12 ma-3">
        <va-card outlined>
          <va-card-content>
             <div>
              <va-tabs v-model="value" class="mt-3">
                <template #tabs>
                  <va-tab>
                    <va-icon
                      name="group"
                      class="mr-3"
                    />
                    Users
                  </va-tab>
                  <va-tab>
                    <va-icon
                      name="descriptions"
                      class="mr-3"
                    />
                    Projects
                  </va-tab>
                  <va-tab>
                    <va-icon
                      name="dns"
                      class="mr-3"
                    />
                    Applications
                  </va-tab>
                </template>
              </va-tabs>
             </div>
          </va-card-content>
          <va-card-content>
           <div v-if="value === 2">
            <HealthAppComponent/>
           </div>
           <div v-if="value === 0">
            <div class="row justify-space-around" style="padding-top: 2%;" v-if="(this.get_users().length === 0)" >
                  <v-container fill-height>
                    <v-layout row wrap align-center>
                        <va-progress-circle indeterminate></va-progress-circle>
                    </v-layout>
                  </v-container>
            </div>
            <va-data-table
              :items="get_users()"
              :columns="team_table_headers"
              style="height: 100%"
            >
                <template #cell(email) = "{rowData}">
                    <va-icon
                        name="face"
                        size="small"
                        class="ml-2 mr-4"
                    />
                    {{rowData.email}}
                </template>
                <template #cell(actions) = "{rowData}">
                    <va-button
                        preset="plain"
                        icon="edit"
                        @click="drop_user_action(rowData)"
                    />
                </template>
            </va-data-table>
           </div>
           <div v-if="value === 1">
            <div class="row justify-space-around" style="padding-top: 2%;" v-if="(this.get_projects_admin().length === 0)" >
                  <v-container fill-height>
                    <v-layout row wrap align-center>
                        <va-progress-circle indeterminate></va-progress-circle>
                    </v-layout>
                  </v-container>
            </div>
            <va-data-table
              :items="get_projects_admin()"
              :columns="project_table_headers"
              style="height: 100%"
            >
                <template #cell(actions) = "{rowData}">
                    <va-button
                        preset="plain"
                        icon="edit"
                        @click="drop_project_action(rowData)"
                    />
                </template>
            </va-data-table>
           </div>
          </va-card-content>
        </va-card>
       </div>
   </div>
   <va-sidebar
      v-model="sideBar"
      :minimized="minimized"
      position="right"
      style="position: absolute;"
      width="30rem"
   >
        <va-card
          square
          outlined
          style="height: 100%;"
        >
          <va-card-title>
          Action
          <va-spacer></va-spacer>
          <va-icon
            name="close"
            size="large"
            @click="this.sideBar = !this.sideBar"
          />
          </va-card-title>
          <va-card-content v-if="sidebar_selector === 0">
            <va-list fit>
                <va-list-item
                  class="list__item"
                >
                    <va-input
                      v-model="new_project.name"
                      class="mb-6"
                      label="Name"
                      placeholder="name"
                    />
                </va-list-item>
                <va-list-item
                  class="list__item"
                >
                    <va-input
                      v-model="new_project.description"
                      class="mt-3"
                      style="width: 25rem;"
                      label="description"
                      placeholder="description"
                    />
                </va-list-item>
            </va-list>
          </va-card-content>
          <va-card-content v-if="sidebar_selector === 1">
            <va-list fit>
                <va-list-item
                  class="list__item"
                >
                    <va-input
                      v-model="new_user.login"
                      class="mb-6"
                      label="Login"
                      placeholder="login"
                    />
                </va-list-item>
                <va-list-item
                  class="list__item"
                >
                    <va-input
                      v-model="new_user.email"
                      class="mt-3"
                      style="width: 25rem;"
                      label="Email"
                      placeholder="email"
                    />
                </va-list-item>
                <va-list-item
                  class="list__item"
                >
                    <va-input
                      v-model="new_user.password"
                      class="mt-3"
                      style="width: 25rem;"
                      label="Password"
                      placeholder="password"
                    />
                </va-list-item>
            </va-list>
          </va-card-content>
          <va-card-content v-if="sidebar_selector === 2">
            <va-icon
                :name="isPasswordVisible ? 'visibility_off' : 'visibility'"
                size="small"
                color="--va-primary"
                @click="isPasswordVisible = !isPasswordVisible"
                class="ma-3"
            />
            <b>Global settings</b>
            <va-alert
                color="warning"
                class="mb-6"
            >
                Need reboot after change parameters.
            </va-alert>
            <va-list fit>
                <va-list-item
                  class="list__item mt-3"
                  v-for="item in this.get_admin_settings()" :key="item.name"
                >
                    <va-input
                      v-model="item.value"
                      class="mb-12"
                      :label="item.name"
                      style="width: 25rem;"
                      :type="isPasswordVisible ? 'text' : 'password'"
                    >
                    </va-input>
                </va-list-item>
            </va-list>

          </va-card-content>
          <va-card-actions>
           <va-button
              class="mt-3 flex flex-col md2"
              icon="save"
              size="small"
              @click="sidebar_action_selector()"
           >
              save
           </va-button>
          </va-card-actions>
        </va-card>
  </va-sidebar>
  <va-sidebar
      v-model="delete_project_dialog"
      :minimized="minimized"
      position="right"
      style="position: absolute;"
      width="30rem"
  >
        <va-card
          square
          outlined
          style="height: 100%;"
        >
          <va-card-title>
          Project info
          <va-spacer></va-spacer>
          <va-icon
            name="close"
            size="large"
            @click="this.delete_project_dialog = !this.delete_project_dialog"
          />
          </va-card-title>
          <va-card-content>
            <va-list fit>
                <va-list-item
                  class="list__item"
                >
                    <va-input
                      v-model="selected_project.id"
                      class="mb-12"
                      style="width: 25rem;"
                      label="id"
                      readonly
                      placeholder="id"
                    />
                </va-list-item>
                <va-list-item
                  class="list__item"
                >
                    <va-input
                      v-model="selected_project.name"
                      class="mb-12 mt-3"
                      style="width: 25rem;"
                      label="name"
                      placeholder="name"
                    />
                </va-list-item>
                <va-list-item
                  class="list__item"
                >
                    <va-input
                      v-model="selected_project.description"
                      class="mb-12 mt-3"
                      style="width: 25rem;"
                      label="description"
                      placeholder="description"
                    />
                </va-list-item>
                <va-list-item class="mt-3">
                     <va-switch
                        size="small"
                        v-model="selected_project.active"
                        true-label="Active"
                        false-label="Disable"
                     />
                </va-list-item>
            </va-list>
          </va-card-content>
          <va-card-actions>
            <va-button icon="save" size="small" @click="update_project_data()">
                Save
            </va-button>
          </va-card-actions>
        </va-card>
  </va-sidebar>
  <va-sidebar
      v-model="delete_user_dialog"
      :minimized="minimized"
      position="right"
      style="position: absolute;"
      width="30rem"
  >
        <va-card
          square
          outlined
          style="height: 100%;"
        >
          <va-card-title>
          User info
          <va-spacer></va-spacer>
          <va-icon
            name="close"
            size="large"
            @click="this.delete_user_dialog = !this.delete_user_dialog"
          />
          </va-card-title>
          <va-card-content>
            <va-list fit>
                <va-list-item
                  class="list__item"
                >
                    <va-input
                      v-model="selected_user.id"
                      class="mb-12"
                      style="width: 25rem;"
                      label="id"
                      readonly
                      placeholder="id"
                    />
                </va-list-item>
                <va-list-item
                  class="list__item"
                >
                    <va-input
                      v-model="selected_user.login"
                      class="mb-12 mt-3"
                      style="width: 25rem;"
                      label="login"
                      placeholder="login"
                    />
                </va-list-item>
                <va-list-item
                  class="list__item"
                >
                    <va-input
                      v-model="selected_user.email"
                      class="mb-12 mt-3"
                      style="width: 25rem;"
                      label="email"
                      placeholder="email"
                    />
                </va-list-item>
                <va-list-item
                  class="list__item"
                >
                    <va-input
                      v-model="selected_user.password"
                      class="mb-12 mt-3"
                      style="width: 25rem;"
                      label="password"
                      placeholder="password"
                    />
                </va-list-item>
            </va-list>
          </va-card-content>
          <va-card-actions>
            <va-button icon="save" size="small" @click="update_user_data()">
                Save
            </va-button>
            <va-button class="ml-3" icon="delete" size="small" @click="delete_user_data(this.selected_user)">
                delete
            </va-button>
          </va-card-actions>
          <va-divider class="mt-2"></va-divider>
          <va-card-actions>
          <va-switch
            size="small"
            v-model="selected_user.admin"
            true-label="Global admin enabled"
            false-label="Global admin disabled"
          />
          </va-card-actions>
        </va-card>
  </va-sidebar>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import HealthAppComponent from '@/components/tables/HealthAppComponent.vue'
export default {
  name: 'TokenPage',
  components: {
    HealthAppComponent
  },
  props: {
    msg: String
  },
  data(){
    return {
        isPasswordVisible: false,
        delete_project_dialog: false,
        delete_user_dialog: false,
        selected_user: {},
        selected_project: {},
        new_project: {
            name: '',
            description: ''
        },
        new_user: {
            login: '',
            password: '',
            email: ''
        },
        sidebar_selector: 0,
        sideBar: false,
        value: 0,
        team_table_headers: [
            { key: "id", sortable: false},
            { key: "email", sortable: true},
            { key: "role", sortable: true},
            { key: "actions", sortable: true}
        ],
        project_table_headers: [
            { key: "id", sortable: false},
            { key: "name", sortable: true},
            { key: "description", sortable: true},
            { key: "created", sortable: true},
            { key: "active", sortable: true},
            { key: "actions", sortable: true}
        ]
    }
  },
  methods: {
      ...mapGetters(
          ['get_users', 'get_current_project', 'get_projects_admin', 'get_admin_settings']
      ),
      ...mapActions(
          [
            'users_request',
            'projects_request',
            'create_user',
            'create_project',
            'update_project',
            'update_user',
            'delete_user_data',
            'download_service_settings',
            'post_service_settings'
          ]
      ),
      sidebar_content_selector(id){
        this.sidebar_selector = id
        this.sideBar = !this.sideBar
      },
      sidebar_action_selector(){
        if (this.sidebar_selector === 0) {
            this.create_project(this.new_project)
        } else if (this.sidebar_selector === 1) {
            this.create_user(this.new_user)
        } else if (this.sidebar_selector === 2) {
            this.post_service_settings(this.get_admin_settings())
        }
        this.sideBar = !this.sideBar
        this.users_request()
        this.projects_request()
      },
      drop_user_action(data){
        this.delete_user_dialog = true
        this.selected_user = data
      },
      drop_project_action(data){
        this.delete_project_dialog = true
        this.selected_project = data
      },
      update_user_data(){
        this.update_user(this.selected_user)
        this.delete_user_dialog = false
      },
      update_project_data(){
        this.update_project(this.selected_project)
        this.delete_project_dialog = false
      }
  },
  created(){
    this.users_request()
    this.projects_request()
    this.download_service_settings()
  },
  mounted(){
    this.users_request()
    this.projects_request()
    this.download_service_settings()
  }
}
</script>

<style lang="scss" scoped>
@import "vuestic-ui/styles/grid";

.row {
  min-height: 6rem;

  & + .row {
    border-top: 1px solid var(--va-background-border);
  }
}

.item {
  border: 1px solid var(--va-background-border);
  background-color: var(--va-background-primary);
  text-align: center;
}
</style>