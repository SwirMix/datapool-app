<template>
  <div class="va-table-responsive">
      <va-card>
        <va-card-title>
            <va-input
              v-model="filter"
              placeholder="Filter..."
              class="w-full"
              bordered="2"
            />
        </va-card-title>
        <va-card-content>
              <div class="row justify-space-around" style="padding-top: 2%;" v-if="this.get_processing_status()" >
                    <v-container fill-height>
                      <v-layout row wrap align-center>
                          <va-progress-circle indeterminate></va-progress-circle>
                      </v-layout>
                    </v-container>
              </div>
              <va-data-table
                v-else
                :items="this.get_current_caches()"
                :columns="columns"
                :wrapper-size="550"
                :item-size="46"
                :per-page="perPage"
                :current-page="currentPage"
                :filter="filter"
                @filtered="filtered = $event.items"
                virtual-scroller
                sticky-header
              >
                <template #cell(actions) = "{rowData}">
                  <router-link :to="{ name: 'CacheDetails', params: { id: rowData.cacheId }}">
                      <va-button
                        icon="info"
                        size="small"
                      >
                        details
                      </va-button>
                  </router-link>
                </template>
                <template #cell(status) = "{rowData}">
                  <va-chip
                    :icon="statusIconCompute(rowData.status)"
                    class="mr-6 mb-2"
                    size="small"
                    square
                    style="width: 5rem;"
                  >
                    {{ rowData.status }}
                  </va-chip>
                </template>
                <template #cell(lastJobSuccess) = "{rowData}">
                  <va-icon :name="lastJobStatus(rowData.lastJobSuccess)"/>
                </template>
                <template #cell(columns) = "{rowData}">
                  {{ Object.keys(rowData.columns).length }}
                </template>
                <template #cell(cacheId) = "{rowData}">
                    <va-popover message="copy id to clipboard">
                      <va-chip size="small" square flat @click="copyId(rowData.cacheId)">
                        {{rowData.cacheId}}
                      </va-chip>
                    </va-popover>
                </template>
                <template #bodyAppend>
                  <tr>
                    <td colspan="12">
                      <div class="flex justify-center mt-4">
                        <va-pagination
                          v-model="currentPage"
                          :pages="pages"
                        />
                      </div>
                    </td>
                  </tr>
                </template>
              </va-data-table>
        </va-card-content>
      </va-card>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex'

export default {
  name: 'NavBar',
  components: {

  },
  props: {
    msg: String
  },
  computed: {
    processing: true,
    pages() {
      return this.perPage && this.perPage !== 0
        ? Math.ceil(this.filtered.length / this.perPage)
        : this.filtered.length;
    },
    filtered(){
        let data = []
        console.log(this.filter)
        if (this.filter === ''){
            data = this.get_current_caches()
        } else {
            for (let cache in this.get_current_caches()){
                let item = this.get_current_caches()[cache]
                if (item.cacheId.includes(this.filter) | item.name.includes(this.filter)){
                    data.push(item)
                }
            }
        }

        return data
    }
  },
  data(){
    return {
        deleteDialog: false,
        perPage: 7,
        currentPage: 1,
        filter: '',
        columns: [
          { key: "lastUpdateDate", sortable: true},
          { key: "cacheId", sortable: true},
          { key: "name", sortable: true},
          { key: "size", sortable: true},
          { key: "columns", sortable: true},
          { key: "cursor", sortable: true},
          { key: "status", sortable: true},
          { key: "actions", sortable: false},
        ],
        caches: []
    }
  },
  methods: {
    dropCacheAction(body){
        this.delete_cache_action(body)
        this.get_caches(body.project)
        this.deleteDialog = false
    },
    ...mapActions(
        ['get_caches', 'delete_cache_action', 'get_caches']
    ),
    ...mapGetters(
        ['get_current_caches', 'get_current_project', 'get_processing_status']
    ),
    copyId(id){
        navigator.clipboard.writeText(id);
        this.$vaToast.init({ message: 'Copy cacheId (Ctrl + V)', color: 'primary' })
    },
    statusIconCompute(status){
        if (status === 'READY'){
            return 'done'
        } else return 'lock_clock'
    },
    lastJobStatus(status){
        if (status){
            return 'done'
        } else {
            return 'error'
        }
    }
  },
  mounted() {
    this.processing = true
    this.get_caches(this.get_current_project().id)
    this.caches = this.get_current_caches()
    this.processing = false
  },
  created() {
    this.processing = true
    this.get_caches(this.get_current_project().id)
    this.caches = this.get_current_caches()
    this.processing = false
  }
}
</script>

<style scoped>

</style>
