<template>
  <va-card-content class="max-w-xs" style="height: 70vh;">
            <AddTeamMember/>
            <va-data-table
              :items="get_current_team()"
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
                        @click="modalEditUserRole(rowData)"
                    />
                </template>
            </va-data-table>
  </va-card-content>
  <va-sidebar
      v-model="teamSideBar"
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
          Edit Teammate
          <va-spacer></va-spacer>
          <va-icon
            name="close"
            size="large"
            @click="this.teamSideBar = !this.teamSideBar"
          />
          </va-card-title>
          <va-card-content>
            <va-list fit>
                <va-list-item
                  class="list__item"
                >
                  <b> user id:  </b> <div class="ml-3">{{selected.id}}</div>
                </va-list-item>
                <va-list-item
                  class="list__item"
                >
                  <b> email:  </b> <div class="ml-3">{{selected.email}}</div>
                </va-list-item>
                <va-list-item
                  class="list__item mt-3"
                >
                  <va-select
                      v-model="new_role"
                      :options="roles"
                  />
                </va-list-item>
            </va-list>
          </va-card-content>
          <va-card-actions>
           <va-button
              class="mt-3 flex flex-col md2"
              icon="save"
              size="small"
              @click="update_user_permission()"
           >
              save
           </va-button>
           <va-button
              class="mt-3 ml-3 flex flex-col md2"
              icon="delete"
              size="small"
              @click="delete_team_member()"
           >
              delete
           </va-button>
          </va-card-actions>
        </va-card>
  </va-sidebar>
</template>

<script>
import AddTeamMember from '@/components/cards/AddTeamMember.vue'
import { mapGetters, mapActions } from 'vuex'

export default {
  name: 'AdminTeamComponent',
  components: {
    AddTeamMember
  },
  props: {
    msg: String
  },
  data(){
    return {
        team: [],
        selected: {},
        roles: [
            'ADMIN',
            'TEAMMATE',
            'READER'
        ],
        teamSideBar: false,
        new_role: '',
        team_table_headers: [
            { key: "id", sortable: false},
            { key: "email", sortable: true},
            { key: "role", sortable: true},
            { key: "actions", sortable: true}
        ]
    }
  },
  created(){
    this.get_team(this.get_current_project().id)
  },
  mounted(){
    this.get_team(this.get_current_project().id)
  },
  methods: {
     ...mapGetters(
        ['get_current_team', 'get_current_project']
     ),
     ...mapActions(
        ['delete_user', 'get_team', 'add_user_to_team']
     ),
     modalEditUserRole(user){
        let temp = user
        this.new_role = user.role;
        this.selected = temp;
        this.teamSideBar = true;
     },
     delete_team_member(){
        this.selected['projectId'] = this.get_current_project().id
        this.selected['userId'] = this.selected.id
        this.delete_user(this.selected)
        this.get_team(this.get_current_project().id)
        this.teamSideBar = false
     },
    update_user_permission(){
        this.selected['projectId'] = this.get_current_project().id
        this.selected['userId'] = this.selected.id
        this.selected.role = this.new_role
        this.add_user_to_team(this.selected)
        this.get_team(this.get_current_project().id)
        this.teamSideBar = false
    }
  }
}
</script>

<style lang="scss" scoped>
</style>