<template>
   <div class="row" v-if="this.check_project_admin()">
       <div class="flex flex-col md12 ma-3">
          <va-breadcrumbs>
             <va-breadcrumbs-item label="Administration" />
          </va-breadcrumbs>
       </div>
       <div class="flex flex-col xs12 mt-4">
             <div>
              <va-tabs v-model="value" class="mt-3">
                <template #tabs>
                  <va-tab>
                    <va-icon
                      name="key"
                      class="mr-3"
                    />
                    Remote tokens
                  </va-tab>
                  <va-tab>
                    <va-icon
                      name="lan"
                      class="mr-3"
                    />
                    Datasources
                  </va-tab>
                  <va-tab>
                    <va-icon
                      name="groups"
                      class="mr-3"
                    />
                    Team
                  </va-tab>
                </template>
              </va-tabs>
             </div>
         <va-card-content class="max-w-xs">
           <div v-if="value === 2">
            <AdminTeamComponent/>
           </div>
           <div v-if="value === 1">
            <AdminDataSourceComponent/>
           </div>
           <div v-if="value === 0">
            <AdminTokensComponent/>
           </div>
         </va-card-content>
       </div>
   </div>
    <div v-else class="row justify-space-around" style="padding-top: 20%;">
        <v-container fill-height>
          <v-layout row wrap align-center>
              <div class="flex flex-col xs" style="height: 100%;">
                <div class="item row items-center">
                    <div class="item flex flex-col xs12 text-center" style="height: 100%;">
                       <va-icon
                         class="mr-2"
                         name="lock"
                         size="4rem"
                       />
                    </div>
                </div>
              </div>
          </v-layout>
        </v-container>
    </div>
</template>

<script>
import data from '@/static/team.json'
import AdminTeamComponent from '@/components/tables/AdminTeamComponent.vue'
import AdminDataSourceComponent from '@/components/tables/AdminDataSourceComponent.vue'
import AdminTokensComponent from '@/components/tables/AdminTokensComponent.vue'
import {mapGetters} from 'vuex'

export default {
  name: 'AdminPage',
  components: {
    AdminTeamComponent,
    AdminDataSourceComponent,
    AdminTokensComponent
  },
  props: {
    msg: String
  },
  data(){
    return {
        value: 0,
        team: [],
        roles: [],
        team_table_headers: [
            { key: "id", sortable: false},
            { key: "email", sortable: true},
            { key: "role", sortable: true},
            { key: "active", sortable: true},
            { key: "actions", sortable: true}
        ]
    }
  },
  methods: {
    ...mapGetters(
        ['check_project_admin', 'get_global_admin']
    )
  },
  created(){
    this.team = data.team
    this.roles = data.roles
    if (!this.check_project_admin()){
        this.$router.push('/403')
    }
  }
}
</script>

<style lang="scss" scoped>
</style>