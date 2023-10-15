<template>
    <div class="row">
       <va-spacer></va-spacer>
       <va-button
          type="submit"
          class="ml-3 mt-3 flex flex-col md1"
          icon="add"
          size="small"
          @click="$refs.modal.show()"
       >
          Add user
       </va-button>
    </div>
    <va-modal v-model="doShowModal" ref="modal">
        <h4 class="va-h4">users</h4>
        <p>
            <va-input
              v-model="filter"
              placeholder="Filter..."
              class="w-full"
            />
            <va-data-table
                :items="this.get_users()"
                :columns="columns"
                :wrapper-size="350"
                :item-size="46"
                @filtered="filtered = $event.items"
                :filter="filter"
                virtual-scroller
                sticky-header
                class="mt-3"
            >
                <template #cell(actions) = "{rowData}">
                  <va-button
                    preset="primary"
                    class="mr-6 mb-2"
                    size="small"
                    @click="add_team_member(rowData)"
                  >
                    add member
                  </va-button>
                </template>
            </va-data-table>
        </p>
    </va-modal>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  name: 'modalEditUserRole',
  components: {

  },
  props: {
    msg: String
  },
  computed: {

  },
  data(){
    return {
        roles: [
            'ADMIN',
            'TEAMMATE'
        ],
        user_email: '',
        user_role: 'USER',
        doShowModal: false,
        filter: '',
        columns: [
          { key: "email", sortable: true},
          { key: "login", sortable: true},
          { key: "role", sortable: false},
          { key: "actions", sortable: false}
        ]
    }
  },
  methods: {
    ...mapGetters(
        ['get_users', 'get_current_project']
    ),
    ...mapActions(
        ['users_request', 'get_team', 'add_user_to_team']
    ),
    add_team_member(user){
        user['projectId'] = this.get_current_project().id
        user['userId'] = user.id
        this.add_user_to_team(user)
        this.get_team(this.get_current_project().id)
        console.log(user)
        this.doShowModal = false
    }
  },
  created(){
    this.users_request()
  },
  mounted(){
    this.users_request()
  }
}
</script>

<style scoped>

</style>
