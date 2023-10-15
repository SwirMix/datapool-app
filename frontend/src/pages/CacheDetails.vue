<template>
   <div class="row align-content-start ma-2" v-if="this.get_current_cache_info().ready">
       <div class="flex flex-col md12 ma-3">
          <va-breadcrumbs>
             <va-breadcrumbs-item label="Cache" />
             <va-breadcrumbs-item :label="$route.params.id" />
          </va-breadcrumbs>
       </div>
       <div class="flex flex-col xs7">
        <va-card
         outlined
         style="height: 100%;"
         v-if="this.get_current_cache_info().metadata"
        >
            <va-card-title>
                <va-icon name="description"/>
                <h3 class="ml-3">{{this.get_current_cache_info().metadata.cacheMetadata.cache.cacheName}}</h3>
                <CacheToCsvDialog v-if="this.get_current_project().role !== 'READER'" />
                <va-spacer class="spacer"/>
                <va-button
                    icon="cached"
                    class="ml-4 mr-4"
                    preset="secondary"
                    hover-behavior="opacity"
                    :hover-opacity="0.4"
                    @click="reload_data()"
                >
                </va-button>
                <va-button size="small" @click="this.sidebar = !this.sidebar" v-if="this.get_current_project().role !== 'READER'">
                   cache actions
                </va-button>
            </va-card-title>
            <va-card-content>
                <va-list>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <b>ID: </b> {{ this.get_current_cache_info().metadata.cacheMetadata.cacheId }}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <b>name: </b> {{ this.get_current_cache_info().metadata.cacheMetadata.cache.cacheName }}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <b>project: </b> {{ this.get_current_cache_info().metadata.cacheMetadata.cache.project }}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <b>size: </b> {{ this.get_current_cache_info().metadata.cacheMetadata.size }}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <b>type: </b> {{ this.get_current_cache_info().metadata.cacheMetadata.type }}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <b>status: </b> {{ this.get_current_cache_info().metadata.cacheMetadata.status }}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <b>last update date: </b> {{ this.get_current_cache_info().metadata.cacheMetadata.lastUpdateDate }}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <b>hashKey: </b> {{ this.get_current_cache_info().metadata.cacheMetadata.hashKey }}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                            <b class="mr-2">cursor:</b> {{this.get_current_cache_info().metadata.cursor}}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item mt-4"
                    >
                      <va-list-item-section>
                            <va-list-item-label>
                              <b>message: </b>
                                  <va-alert
                                    :color="msgStatus"
                                    outline
                                    class="mb-6"
                                  >
                                    {{ this.get_current_cache_info().metadata.cacheMetadata.message.message }}
                                  </va-alert>
                            </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                </va-list>
                <va-divider></va-divider>
                <va-tree-view
                    :nodes="calcColumnsTreeView(set_columns(this.get_current_cache_info().metadata.cacheMetadata.columns))"
                />
            </va-card-content>
        </va-card>
       </div>
       <div class="flex flex-col xs5">
        <va-card
         outlined
         style="height: 100%;"
        >
            <va-card-title>
                Datasource
                <va-spacer></va-spacer>
            </va-card-title>
            <va-card-content style="max-height: 50vh;">
                <va-list>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-list-item-label v-if="this.get_current_cache_info().datasource.data">
                          <b>type: </b> {{ this.get_current_cache_info().datasource.data.props.type }}
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item"
                      v-if="this.get_current_cache_info().datasource.data"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <va-input
                              v-model="this.get_current_cache_info().datasource.data.props.entity"
                              style="width: 100%;"
                              class="mt-3 mb-3"
                              readonly
                              type="textarea"
                              :min-rows="4"
                              :max-rows="8"
                          />
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item"
                      v-if="datasourceExist()"
                    >
                      <va-list-item-section>
                        <va-list-item-label>
                          <b>datasourceId: </b> <router-link :to="{ name: 'DataSourceDetails', params: { id: this.get_current_cache_info().datasource.data.props.datasourceId}}">{{ this.get_current_cache_info().datasource.data.props.datasourceId }}</router-link>
                        </va-list-item-label>
                      </va-list-item-section>
                    </va-list-item>
                    <va-list-item
                      class="list__item"
                    >
                      <va-list-item-section>
                        <va-divider></va-divider>
                      </va-list-item-section>
                    </va-list-item>
                </va-list>
            </va-card-content>
        </va-card>
       </div>
       <div class="flex flex-col xs12">
        <va-card outlined>
            <va-card-content>
             <div class="row">
              <va-tabs v-model="tab" class="flex flex-col xs5 mt-3">
                <template #tabs>
                  <va-tab>
                    <va-icon
                      name="table_chart"
                      class="mr-3"
                    />
                    Data
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
                      name="bug_report"
                      class="mr-3"
                    />
                    Datapool check
                  </va-tab>
                  <va-tab>
                    <va-icon
                      name="notes"
                      class="mr-3"
                    />
                    Description
                  </va-tab>
                </template>
              </va-tabs>
             </div>
            </va-card-content>
        </va-card>
       </div>
       <div class="flex flex-col xs12">
        <div v-if="tab === 0">
         <CacheDataTable/>
        </div>
        <div v-if="tab === 1">
         <ConsumerRateTable/>
        </div>
        <div v-if="tab === 2">
         <DatapoolCheckTable/>
        </div>
        <div v-if="tab === 3">
         <CacheNotesComponent class="mt-1"/>
        </div>
       </div>
   </div>
   <div v-else class="row justify-space-around" style="padding-top: 20%;">
          <v-layout row wrap align-center>
              <div class="flex flex-col xs" style="height: 100%;">
                <div class="item row items-center">
                     <va-progress-circle indeterminate/>
                </div>
              </div>
          </v-layout>
   </div>
   <va-sidebar
      v-model="sidebar"
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
          Cache actions
          <va-spacer></va-spacer>
          <va-icon
            name="close"
            size="large"
            @click="this.sidebar = !this.sidebar"
          />
          </va-card-title>
          <va-card-content>
            <div class="row ma-2">
              <va-input
                     v-model="cursor"
                     pattern="\d{4,4}"
                     label="cache cursor"
                     class="flex flex-col xs6"
              />
              <div class="flex flex-col xs6" style="text-align: end;">
                    <va-button @click="update_cursor()">
                       update cursor
                    </va-button>
              </div>
            </div>
          </va-card-content>
          <va-card-content>
            <va-divider></va-divider>
            <div class="row ma-2">
                <div class="mt-3 flex flex-col xs12" v-if="datasourceExist()">
                  <va-alert
                    color="#fdeae7"
                    text-color="#940909"
                    icon="cached"
                  >
                    <div class="row ma-2">
                        <div class="flex flex-col xs10">
                             Reloading data from source.
                        </div>
                        <div class="flex flex-col xs2">
                            <va-button icon="cached" size="small" color="danger" @click="reload_request()">
                                 reload
                            </va-button>
                        </div>
                    </div>
                  </va-alert>
                </div>
                <div class="mt-3 flex flex-col xs12">
                  <va-alert
                    color="warning"
                    icon="warning"
                  >
                    <div class="row ma-2">
                        <div class="flex flex-col xs10">
                             The operation is irreversible. All data will be completely deleted.
                        </div>
                        <div class="flex flex-col xs2">
                            <va-button size="small" color="danger" @click="drop_cache()">
                                Drop cache
                            </va-button>
                        </div>
                    </div>
                  </va-alert>
                </div>
                <div class="mt-3 flex flex-col xs12">
                  <va-alert
                    color="#265c83"
                    icon="warning"
                    outline
                  >
                    <div class="row ma-2">
                        <div class="flex flex-col xs4 ml-3">
                             The operation to change the status of the cache.
                        </div>
                        <div class="flex flex-col xs4" style="text-color: dark;">
                            <va-select
                              v-model="new_status"
                              theme="light"
                              :options="['BUSY', 'READY']"
                            />
                        </div>
                        <va-spacer></va-spacer>
                        <div class="flex flex-col xs2 ml-3">
                            <va-button size="small" color="info" @click="change_status()">
                                Set status
                            </va-button>
                        </div>
                        <div class="flex flex-col xs12 ml-3" color="dark">
                            <va-divider></va-divider>
                        </div>
                        <div class="flex flex-col xs12 ml-3" color="dark">
                            <ul>
                                <li class="ma-2"><b>READY</b> - ready to consume data.</li>
                                <li class="ma-2"><b>BUSY</b> - cache locked for consumption.</li>
                            </ul>
                        </div>
                    </div>
                  </va-alert>
                </div>
            </div>
          </va-card-content>
          <va-divider></va-divider>
          <va-card-content>
            <div class="row">
               <div class="flex flex-col xs12 ml-3" color="dark">
                  <va-input
                    v-model="summary"
                    class="mb-6"
                    style="width: 100%;"
                    type="textarea"
                    label="Cache description"
                    :min-rows="6"
                    :max-rows="16"
                  />
               </div>
            </div>
                <div class="pt-0 px-4 pb-3" style="text-align: end;">
                    <va-button
                      icon="save"
                      class="ml-4 mt-2"
                      size="small"
                      hover-behavior="opacity"
                      :hover-opacity="0.4"
                      @click="update_cache_desc()"
                    >
                       update
                    </va-button>
                </div>
          </va-card-content>
        </va-card>
   </va-sidebar>
</template>

<script>
import { mapGetters, mapActions} from 'vuex'
import CacheDataTable from '@/components/tables/CacheDataTable.vue'
import ConsumerRateTable from '@/components/tables/ConsumerRateTable.vue'
import DatapoolCheckTable from '@/components/tables/DatapoolCheckTable.vue'
import CacheToCsvDialog from '@/components/cards/CacheToCsvDialog.vue'
import CacheNotesComponent from '@/components/CacheNotesComponent.vue'
import axios from 'axios';

export default {
  name: 'CacheDetails',
  components: {
    CacheDataTable,
    ConsumerRateTable,
    DatapoolCheckTable,
    CacheToCsvDialog,
    CacheNotesComponent
  },
  props: {
    msg: String
  },
  methods: {
    ...mapActions(
        ['get_cache_info','delete_cache_action', 'change_cache_status','reload_cache','update_cache_cursor']
    ),
    ...mapGetters(
        ['get_current_cache_info', 'get_current_project']
    ),
    update_cache_desc(){
        let toast = this.$vaToast
        let body = {
            project: this.get_current_project().id,
            cacheName: this.get_current_cache_info().metadata.cacheMetadata.cache.cacheName,
            summary: this.summary
        }
        axios.post(
            this.$store.state.backend + '/api/v1/cache/summary',
                body, {
                headers: {
                  'token': localStorage.datapool_token
                }
        }).catch(function (error) {
            console.log(error)
            toast.init({ message: 'Sorry. Error. :(\n ', color: 'warning' })
        })
        let path = this.$route.params.id.split('_')
        body = {
            projectId: path[0],
            cacheName: path[1]
        }
        this.get_cache_info(body)
        this.sidebar = false
    },
    reload_data(){
       let path = this.$route.params.id.split('_')
       let body = {
          projectId: path[0],
          cacheName: path[1]
       }
       this.get_cache_info(body)
    },
    update_cursor(){
        let request = {
            "cacheName": this.get_current_cache_info().metadata.cacheMetadata.cache.cacheName,
            "projectId": this.get_current_cache_info().metadata.cacheMetadata.cache.project,
            "cursor": this.cursor
        }
        this.update_cache_cursor(request)
        this.sidebar = false
    },

    reload_request(){
        let request = {
            "cacheName": this.get_current_cache_info().metadata.cacheMetadata.cache.cacheName,
            "projectId": this.get_current_cache_info().metadata.cacheMetadata.cache.project
        }
        this.reload_cache(request)
        this.$router.push('/')
    },
    change_status(){
        let request = {
            "key": {
                "cacheName": this.get_current_cache_info().metadata.cacheMetadata.cache.cacheName,
                "project": this.get_current_cache_info().metadata.cacheMetadata.cache.project
            },
            "status": this.new_status
        }
        this.change_cache_status(request)
        this.sidebar = false
    },

    drop_cache(){
        let body = {
            project: this.get_current_cache_info().metadata.cacheMetadata.cache.project,
            cacheName: this.get_current_cache_info().metadata.cacheMetadata.cache.cacheName
        }
        this.delete_cache_action(body)
        this.$router.push('/')
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
            let label = columns[column].label + " type: " + columns[column].type
            root.children.push({
                'id': id,
                'label': label,
                'icon': 'view_column'
            })
        }
        nodes.push(root);
        console.log(nodes);
        return nodes
    },
    set_columns(raw_columns){
        try {
            let columns = []
            for (var key in raw_columns) {
                columns.push(
                    {
                        label: key,
                        type:  raw_columns[key]
                    }
                )
            }
            return columns
        } catch (err){
            return []
        }
    },
    datasourceExist(){
        try {
            if (this.get_current_cache_info().datasource.data.props.type !== 'RUNTIME'){
                console.log(false)
                return true
            } else {
                return null
            }
        } catch {
            return null
        }
    }
  },
  computed: {
    metadata: function(){
        try {
            return this.get_current_cache_info().metadata;
        } catch(err){
            return {}
        }
    },
    msgStatus: function(){
        if (this.get_current_cache_info().metadata.cacheMetadata.message.lastSuccess === true){
            return 'success'
        } else {
            return 'warning'
        }
    }
  },
  created() {
    let path = this.$route.params.id.split('_')
    let body = {
        projectId: path[0],
        cacheName: path[1]
    }
    this.get_cache_info(body)
  },
  mounted(){
    let path = this.$route.params.id.split('_')
    let body = {
        projectId: path[0],
        cacheName: path[1]
    }
    this.get_cache_info(body)
  },
  data(){
    return {
        cursor: this.get_current_cache_info().metadata.cursor,
        datapoolCheck: false,
        tab: 3,
        subTab: 0,
        new_status: '',
        sidebar: false,
        consumers: [
            { key: "name", sortable: true},
            { key: "lastRequest", sortable: true},
        ],
        data_table: false,
        summary: ''
    }
  }
}
</script>

<style scoped></style>