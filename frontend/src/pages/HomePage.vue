<template>
   <div class="row ma-3" v-if="!this.get_toast().show" >
       <div class="flex flex-col md12 ma-3">
              <va-breadcrumbs>
                <va-breadcrumbs-item label="Cashes" />
              </va-breadcrumbs>
       </div>
       <div class="ma-3 row">
           <div class="flex flex-col md7" style="height: 100%;" v-if="true">
              <va-card
                stripe
                outlined
                style="height: 100%;"
              >
                <va-card-title class="ma-2">
                    <va-icon name="description"/><div class="ml-3">Project description</div>
                    <va-spacer class="spacer" />
                    <va-badge
                        placement="right-start"
                        :text="this.get_current_team().length"
                        color="success"
                        offset="-2px"
                        size="small"

                    >
                        <va-button
                            preset="primary"
                            size="small"
                            class="mr-6 mb-2"
                            icon="groups"
                            @click="this.teamSideBar = !this.teamSideBar"
                        >
                            Team
                        </va-button>
                    </va-badge>
                </va-card-title>
                <va-card-content>
                  {{this.get_current_project().description}}
                </va-card-content>
              </va-card>
           </div>

       </div>
       <div class="flex flex-col xs12 ml-3 mr-3">
        <va-card outlined>
            <va-card-content>
              <va-tabs v-model="tab" class="">
                <template #tabs>
                  <va-tab>
                    <va-icon
                      name="cached"
                      class="mr-3"
                    />
                    Caches
                  </va-tab>
                  <va-tab>
                    <va-icon
                      name="group"
                      class="mr-3"
                    />
                    Consumers
                  </va-tab>
                  <va-tab>
                    <va-icon
                      name="folder"
                      class="mr-3"
                    />
                    CSV storage
                  </va-tab>
                </template>
              </va-tabs>
            </va-card-content>
        </va-card>
       </div>
       <div class="flex flex-col xs12 ma-3">
        <div v-if="tab === 0">
         <CacheTable/>
        </div>
        <div v-if="tab === 1">
         <ConsumerFullRateTable/>
        </div>
        <div v-if="tab === 2">
         <StorageComponent/>
        </div>
       </div>
   </div>
   <div v-else class="row justify-space-around" style="padding-top: 20%;">
          <v-layout row wrap align-center>
              <div class="flex flex-col xs" style="height: 100%;">
                <div class="item row items-center">
                    {{this.get_toast().message}}
                </div>
              </div>
          </v-layout>
   </div>
   <va-sidebar
      v-model="teamSideBar"
      :minimized="minimized"
      position="right"
      style="position: absolute;"
      width="40rem"
   >
        <va-card
          square
          outlined
          style="height: 100%;"
        >
          <va-card-title>
          Project team
          <va-spacer></va-spacer>
          <va-icon
            name="close"
            size="large"
            @click="this.teamSideBar = !this.teamSideBar"
          />
          </va-card-title>
          <va-card-content>
            <va-data-table
              :items="get_current_team()"
              :columns="team_columns"
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
            </va-data-table>
          </va-card-content>
        </va-card>
   </va-sidebar>
</template>

<script>
import CacheTable from '@/components/tables/CacheTable.vue'
import { mapGetters , mapMutations} from 'vuex'
import ConsumerFullRateTable from '@/components/tables/ConsumerFullRateTable.vue'
import StorageComponent from '@/components/tables/StorageFolderComponent.vue'
import axios from 'axios'

export default {
  name: 'HomePage',
  components: {
    CacheTable,
    ConsumerFullRateTable,
    StorageComponent
  },
  props: {
    msg: String
  },
  data(){
    return {
        tab: 0,
        csvFile: [],
        teamSideBar: false,
        minimized: false,
        team_columns: [
          { key: "email", sortable: false},
          { key: "role", sortable: true}
        ],
    }
  },
  mounted(){
    this.team = this.get_current_team()
  },
  methods: {
    ...mapGetters([
        'get_current_team',
        'get_current_project',
        'get_toast',
        'get_current_project'
    ]),
    ...mapMutations([
        'set_current_project'
    ]),
    upload_file(){
        var formData = new FormData();
        formData.append("file", this.csvFile[0]);
        axios.post(
            this.$store.state.backend + '/api/v1/storage/' + this.get_current_project().id + '/upload',
            formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
              'token': localStorage.datapool_token
            }
        })
    }
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
  background-color: var(--va-background-primary);
  text-align: center;
}
</style>