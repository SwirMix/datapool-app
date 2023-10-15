<template>
  <div class="va-table-responsive">
      <va-card>
        <va-card-content>
              <va-data-table
                :items="this.get_storage()"
                :columns="columns"
                :wrapper-size="550"
                :item-size="46"
                virtual-scroller
                sticky-header
              >
                <template #cell(createDate) = "{rowData}">
                  {{ new Date(rowData.createDate) }}
                </template>
                <template #cell(actions) = "{rowData}">
                  <va-button class="ml-2" color="primary" size="small" icon="info" @click="select_file(rowData)">
                    actions
                  </va-button>
                </template>
              </va-data-table>
        </va-card-content>
      </va-card>
  </div>
  <va-sidebar
      v-model="fileActionSidebar"
      :minimized="minimized"
      position="right"
      style="position: absolute;"
      width="35rem"
 >
        <va-card
          square
          outlined
          style="height: 100%;"
        >
          <va-card-title>
          ID: {{selected.fileName}}
          <va-spacer></va-spacer>
          <va-icon
            name="close"
            size="large"
            @click="this.fileActionSidebar = !this.fileActionSidebar"
          />
          </va-card-title>
          <va-card-content>
            <va-list fit>
                <va-list-item
                  class="list__item mt-1"
                  v-if="get_current_project().role !== 'READER'"
                >
                    <va-input
                      v-model="cacheRq.cacheName"
                      class="mb-12"
                      label="CacheName"
                      style="width: 30rem;"
                    >
                    </va-input>
                </va-list-item>
                <va-list-item
                  class="list__item mt-3"
                >
                    <b class="mr-2">Created: </b> {{new Date(selected.createDate)}}
                </va-list-item>
                <va-list-item
                  class="list__item mt-3"
                >
                    <b class="mr-2">Rows: </b> {{selected.rows}}
                </va-list-item>
                <va-list-item
                  class="list__item mt-3"
                >
                    <b class="mr-2">Size: </b> {{selected.size/1024}} Kb
                </va-list-item>
            </va-list>
          </va-card-content>
          <va-card-actions>
            <va-button size="small" icon="download" @click="downloadFile(selected)">
                download
            </va-button>
            <va-button size="small" icon="rocket_launch" @click="csvToCacheAction()" v-if="get_current_project().role !== 'READER'">
                create cache
            </va-button>
            <va-spacer></va-spacer>
            <va-button size="small" icon="delete" color="danger" @click="deleteFile()" v-if="get_current_project().role !== 'READER'">
                delete
            </va-button>
          </va-card-actions>
        </va-card>
   </va-sidebar>
</template>

<script>
import { mapGetters, mapActions} from 'vuex'
import axios from 'axios';

export default {
  name: 'StorageComponent',
  components: {

  },
  props: {
    msg: String
  },
  computed: {

  },
  data(){
    return {
        fileActionSidebar: false,
        columns: [
          { key: "id", sortable: false},
          { key: "fileName", sortable: false},
          { key: "rows", sortable: false},
          { key: "createDate", sortable: false},
          { key: "size", sortable: false},
          { key: "actions", sortable: false},
        ],
        selected: {},
        cacheRq: {
            cacheName: '',
            project: '',
            fileId: ''
        }
    }
  },
  methods: {
    ...mapGetters(
        ['get_storage','get_current_project']
    ),
    ...mapActions(
        ['get_project_storage']
    ),
    select_file(rowData){
        this.selected = rowData
        this.actionMenu()
    },
    deleteFile(){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.delete(this.$store.state.backend + '/api/v1/storage/' + this.get_current_project().id + '/delete/' + this.selected.id , config).then(function(response){
            let responseBody = response.data

            if (responseBody.success === true){
                console.log(response.data)

            } else {
                status.success = responseBody.success
            }
        }).catch(function (error) {
            console.log(error)
        })
        this.get_project_storage(this.get_current_project().id)
        this.fileActionSidebar = !this.fileActionSidebar

    },
    csvToCacheAction(){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        let toast = this.$vaToast
        let responseBody = {}
        this.cacheRq.fileId = this.selected.id
        this.cacheRq.project = this.get_current_project().id
        axios.post(this.$store.state.backend + '/api/v1/storage/export', this.cacheRq, config).then(function(response){
            responseBody = response.data

            if (responseBody.success === true){
                console.log(response.data)
                toast.init({ message: response.data.message, color: 'success' })
            } else {
                status.success = responseBody.success
                status.msg = 'Internal error. Check body.'
                toast.init({ message: response.data.message, color: 'danger' })
            }
        }).catch(function (error) {
            console.log(error)
            toast.init({ message: error.response.data.message, color: 'danger' })
        })
        this.fileActionSidebar = !this.fileActionSidebar
    },
    actionMenu(){
        this.fileActionSidebar = !this.fileActionSidebar
    },
    downloadFile(rowData){
          console.log(rowData)
          axios({
                url: this.$store.state.backend + '/' + rowData.url, // Download File URL Goes Here
                method: 'GET',
                responseType: 'blob',
                headers: {
                    'Access-Control-Allow-Origin': '*',
                    'Access-Control-Allow-Methods': ' GET, PUT, POST, DELETE, OPTIONS',
                    'Access-Control-Allow-Headers': 'Origin, Content-Type, X-Auth-Token',
                    'Access-Control-Allow-Credentials': 'false',
                    'token': localStorage.datapool_token
                },
            }).then((res) => {
                var FILE = window.URL.createObjectURL(new Blob([res.data]));
                var docUrl = document.createElement('a');
                docUrl.href = FILE;
                docUrl.setAttribute('download', rowData.fileName);
                document.body.appendChild(docUrl);
                docUrl.click();
            });
    }
  },
  mounted() {
    this.get_project_storage(this.get_current_project().id)
  },
  created() {
    this.get_project_storage(this.get_current_project().id)
  }
}
</script>

<style scoped>

</style>
