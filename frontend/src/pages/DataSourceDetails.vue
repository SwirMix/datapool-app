<template>
   <div class="row align-content-start ma-2" v-if="this.get_current_project().role !== 'READER'">
       <div class="flex flex-col md12 ma-3">
          <va-breadcrumbs>
             <va-breadcrumbs-item label="Datasource" />
             <va-breadcrumbs-item :label="$route.params.id" />
          </va-breadcrumbs>
       </div>
       <div class="flex flex-col xs5" v-if="this.check_current_datasource_exist()" >
          <va-card
            outlined
            style="height: 100%;"
          >
            <va-card-title>
                <va-icon name="description"/>
                <h3 class="ml-3">{{this.get_current_datasource().name}}</h3>
                <va-spacer class="spacer"/>
            </va-card-title>
            <va-card-content>
                <va-list>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <b>ID: </b> {{ this.get_current_datasource().id }}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <b>Name: </b> {{ this.get_current_datasource().name }}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <b>project: </b> {{ this.get_current_datasource().projectId }}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <b>type: </b> {{ this.get_current_datasource().properties.type }}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <b>url: </b> {{ this.get_current_datasource().properties.properties.url }}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <b>schema: </b> {{ this.get_current_datasource().properties.properties.schema }}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-divider></va-divider>
                </va-list>
            </va-card-content>
          </va-card>
        </div>
        <div class="flex flex-col xs7">
                <va-card
                  square
                  outlined
                  class="mr-2"
                >
                  <va-card-content>
                    <va-card-title>
                        <va-icon name="table_chart"/>
                        <va-spacer></va-spacer>
                        <va-button size="small" class="ml-3" @click="refresh_tables()">refresh</va-button>
                    </va-card-title>
                    <va-card-content style="height: 100%;">
                      <va-data-table
                        :items="this.get_current_datasource_table()"
                        :columns="columns"
                        :wrapper-size="700"
                        :item-size="46"
                        virtual-scroller
                        sticky-header
                      >
                        <template #cell(columns) = "{rowData}">
                              <va-tree-view
                                :nodes="calcColumnsTreeView(rowData.columns)"
                              />
                        </template>
                        <template #cell(actions) = "{rowData}">
                            <router-link :to="{ name: 'DataSourceDetails', params: { id: rowData.id }}">
                               <va-button
                                size="small"
                                icon="rocket_launch"
                                outlined
                                @click="selectTableAction(rowData)"
                               > Create cache </va-button>
                            </router-link>
                        </template>
                      </va-data-table>
                    </va-card-content>
                  </va-card-content>
                </va-card>
       </div>
       <div class="flex flex-col xs12 mt-3">

       </div>
   </div>
   <va-sidebar
      v-model="createCacheSidebar"
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
          Create cache
          <va-spacer></va-spacer>
          <va-icon
            name="close"
            size="large"
            @click="this.createCacheSidebar = !this.createCacheSidebar"
          />
          </va-card-title>
          <va-card-content>
            <div class="row" v-if="this.createCacheSidebar">
                <va-input
                  v-model="this.body.entity"
                  class="flex-col xs12 ma-2"
                  label="Entity"
                  readonly
                  placeholder="input query or tableName"
                  v-if="type==='DEFAULT'"
                />
                <va-input
                  v-model="body.key.cacheName"
                  class="flex-col xs12 ma-2"
                  label="cacheName"
                  placeholder="input cacheName"
                />
                <va-list class="ma-2">
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <b>Datasource id: </b> {{this.get_current_datasource().id}}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <b>Project: </b> {{this.get_current_datasource().projectId}}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                </va-list>
                <va-button
                  class="ma-2 mt-3 flex flex-col"
                  icon="add"
                  @click="create_jdbc_cache_from_table()"
                >
                  Load full table
                </va-button>
            </div>
            <va-divider></va-divider>
            <div class="row ma-2">
                <va-input
                  v-model="customQuery"
                  class="flex-col xs12"
                  label="custom query"
                  placeholder="input cacheName"
                />
                <va-button
                  class="mt-3 flex flex-col xs12"
                  icon="add"
                  @click="create_jdbc_cache_from_table_query()"
                >
                  Load queryResult
                </va-button>
            </div>
          </va-card-content>
        </va-card>
   </va-sidebar>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'


export default {
  name: 'DataSourceDetails',
  components: {

  },
  props: {
    msg: String
  },
  computed: {
  },
  methods: {
    ...mapActions(
        ['get_datasource_details', 'get_datasource_tables', 'create_cache_from_table', 'get_caches']
    ),
    ...mapGetters(
        ['get_current_datasource', 'check_current_datasource_exist', 'get_current_datasource_table', 'get_current_project']
    ),
    selectTableAction(tableData){
        this.selected = tableData;
        this.createCacheSidebar = true;
        this.body.entity = tableData.tableName
    },
    refresh_tables(){
        this.get_datasource_tables(this.get_current_datasource().id)
    },

    create_jdbc_cache_from_table(){
        this.body.type = 'DEFAULT'
        this.body.datasourceId = this.get_current_datasource().id
        this.body.key.project = this.get_current_datasource().projectId
        console.log(this.body)
        this.create_cache_from_table(this.body)
        this.createCacheSidebar = false
        this.get_caches(this.get_current_datasource().projectId)
    },

    create_jdbc_cache_from_table_query(){
        this.body.type = 'QUERY'
        this.body.entity = this.customQuery
        this.body.datasourceId = this.get_current_datasource().id
        this.body.key.project = this.get_current_datasource().projectId
        this.create_cache_from_table(this.body)
        this.createCacheSidebar = false
        this.get_caches(this.get_current_datasource().projectId)
    },

    calcColumnsTreeView(columns){
        let id = 0;
        let nodes = [];
        let root = {
            'id': 1,
            'label': 'columns ' + columns.length,
            'icon': 'table_chart',
            'children': []
        }
        for (let column in columns){
            id =  Number(column) + 2;
            root.children.push({
                'id': id,
                'label': columns[column].label,
                'icon': 'view_column'
            })
        }
        nodes.push(root);
        console.log(nodes);
        return nodes
    }
  },
  mounted(){
    this.type = this.types[0]
    if (this.get_current_project().role == 'READER'){
        this.$router.push('/403')
    }
  },
  created() {
    this.type = this.types[0]
    this.get_datasource_details(this.$route.params.id)
    if (this.get_current_project().role == 'READER'){
        this.$router.push('/403')
    }
  },
  data(){
    return {
        customQuery: '',
        body: {
            "datasourceId": "string",
            "entity": "string",
            "type": "DEFAULT",
            "key": {
                "cacheName": "",
                "project": "string",
                "publicCacheName": "string"
            }
        },
        type: {},
        types: [
            "DEFAULT",
            "QUERY"
        ],
        createCacheSidebar: false,
        selected: {},
        createCacheParams: {
            "entity": ""
        },
        columns: [
          { key: "tableName", sortable: true},
          { key: "columns", sortable: false},
          { key: "actions", sortable: true},
        ],
        nodes: []
    }
  }
}
</script>

<style scoped></style>