<template>
  <va-card-content class="max-w-xs">
    <va-spacer></va-spacer>
    <va-button
        class="mt-3"
        icon="add"
        size="small"
        @click = " createPgSourceDialog = !createPgSourceDialog"
    >
        postgresql datasource
    </va-button>
  </va-card-content>
  <va-card-content class="max-w-xs" style="height: 70vh;">
        <va-card-content>
              <va-data-table
                :items="get_datasources()"
                :columns="columns"
                :wrapper-size="400"
                :item-size="46"
                virtual-scroller
                sticky-header
              >
                <template #cell(type) = "{rowData}">
                  {{rowData.properties.type}}
                </template>
                <template #cell(actions) = "{rowData}">
                    <va-button
                        preset="plain"
                        icon="edit"
                        @click="modalDataSourceEdit(rowData)"
                    />
                </template>
              </va-data-table>
        </va-card-content>
  </va-card-content>
  <va-sidebar
      v-model="sidebar"
      :minimized="minimized"
      position="right"
      style="position: absolute;"
      width="30rem"
  >
    <va-card-title>
          Edit Datasource
        <va-spacer></va-spacer>
        <va-icon
            name="close"
            size="large"
            @click="this.sidebar = !this.sidebar"
        />
     </va-card-title>
     <va-card
        square
        outlined
        style="height: 100%;"
     >
          <va-card-content>
            <div class="row">
                <va-input
                    v-model="selected.id"
                    class="mt-3 flex flex-col md12"
                    label="this.modify_source"
                    readonly
                />
                <va-input
                    v-model="selected.name"
                    class="mt-3 flex flex-col md12"
                    label="Name"
                />
                <va-input
                    v-model="selected.projectId"
                    class="mt-3 flex flex-col md12"
                    label="projectId"
                    readonly
                />
                <va-input
                    v-model="selected.properties.type"
                    class="mt-3 flex flex-col md12"
                    label="type"
                    readonly
                />
                <va-input
                  v-model="this.source_props_stringify"
                  class="mb-12 mt-3"
                  type="textarea"
                  label="properties"
                  :min-rows="20"
                  :max-rows="40"
                />
            </div>
          </va-card-content>
          <va-card-actions>
           <va-button
              class="mt-3 flex flex-col md2"
              icon="save"
              size="small"
              @click="update_datasource()"
           >
              save
           </va-button>
           <va-button
              class="mt-3 ml-3 flex flex-col md2"
              icon="delete"
              size="small"
              @click="delete_source()"
           >
              delete
           </va-button>
          </va-card-actions>
     </va-card>
  </va-sidebar>
  <va-modal
    v-model="createPgSourceDialog"
  >
    <template #content >
        <va-card-title>
            Postgresql source
        </va-card-title>
        <va-card-content style="width: 40rem;">
            <div class="row">
               <va-input
                  v-model="body.dataSourceName"
                  class="mt-3 md12 flex flex-col"
                  label="Source name"
               />
               <va-input
                  v-model="body.properties.properties.url"
                  class="mt-3 md12 flex flex-col"
                  label="Source url"
               />
               <va-input
                  v-model="body.properties.properties.schema"
                  class="mt-3 md12 flex flex-col"
                  label="Source schema"
               />
               <va-input
                  v-model="body.properties.properties.username"
                  class="mt-3 md12 flex flex-col"
                  label="Source login"
               />
               <va-input
                  v-model="body.properties.properties.password"
                  class="mt-3 md12 flex flex-col"
                  label="Source password"
               />
            </div>
        </va-card-content>
        <va-card-actions>
           <va-spacer>
           </va-spacer>
           <va-button
              class="ml-3 mt-3 flex flex-col"
              icon="add"
              size="small"
              @click="create_pg_source()"
           >
              create source
           </va-button>
        </va-card-actions>
    </template>
  </va-modal>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'

export default {
  name: 'AdminDataSourceComponent',
  components: {

  },
  props: {
    msg: String
  },

  data(){
    return {
        createPgSourceDialog: false,
        selected: {
            "id": "",
            "projectId": "",
            "name": "",
            "properties": {
                "type": "",
                "properties": {
                    "schema": "",
                    "password": "",
                    "url": "",
                    "username": ""
                }
            }
        },
        source_props_stringify: '',
        sidebar: false,
        columns: [
          { key: "type", sortable: true},
          { key: "id", sortable: false},
          { key: "name", sortable: true},
          { key: "actions", sortable: false}
        ],
        datasources: [
        ],
        body: {
            "projectId": "",
            "dataSourceName": "",
            "properties": {
                "type": "POSTGRESQL",
                "properties": {
                    "schema": "profile",
                    "password": "perfcona",
                    "url": "jdbc:postgresql://localhost:5432/perfcona?currentSchema=datapool",
                    "username": "perfcona"
                  }
            }
        }
    }
  },
  created(){
  },
  mounted() {

  },
  methods: {
    ...mapGetters(
        ['get_datasources', 'get_current_project']
    ),
    ...mapActions(
        ['create_jdbc_datasource', 'delete_datasource', 'update_datasource_request']
    ),
    delete_source(){
        this.delete_datasource(this.selected.id)
        this.sidebar = false
    },
    create_pg_source(){
        this.body.projectId = this.get_current_project().id
        this.create_jdbc_datasource(this.body)
        this.createPgSourceDialog = false
    },
    update_datasource(){
        this.selected.properties.properties = JSON.parse(this.source_props_stringify)
        this.update_datasource_request(this.selected)
        this.sidebar = false
    },
    modalDataSourceEdit(rowData){
        this.selected = rowData
        this.source_props_stringify = JSON.stringify(this.selected.properties.properties)
        this.sidebar = true
    },

  }
}
</script>

<style lang="scss" scoped>
</style>