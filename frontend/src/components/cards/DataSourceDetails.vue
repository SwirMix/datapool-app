<template>
   <va-button
      preset="primary"
      class="mr-6 mb-2"
      icon="groups"
      @click="this.detailsSidebar = !this.detailsSidebar"
   >
       Details
   </va-button>
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
              :items="team"
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

export default {
  name: 'DataSourceDetails',
  components: {

  },
  props: {
    datasource: {
       "id": String,
       "projectId": String,
       "name": String,
       "properties": {
          "type": String,
          "properties": {
             "schema": String,
             "password": String,
             "url": String,
              "username": String
          }
       }
    }
  },
  data(){
    return {
        detailsSidebar: false
    }
  },
  methods: {

  }
}
</script>
