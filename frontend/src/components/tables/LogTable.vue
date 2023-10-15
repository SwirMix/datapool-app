<template>
  <div class="va-table-responsive">
      <va-card>
        <va-card-title>
        </va-card-title>
        <va-card-content>
              <va-data-table
                :items="get_events().events"
                :columns="columns"
                :wrapper-size="700"
                :item-size="46"
                virtual-scroller
                sticky-header
              >
                <template #cell(actions) = {rowData}>
                  <va-button
                    icon="info"
                    class="ml-3"
                    size="small"
                    @click = "show_details(rowData)"
                  >
                  details
                  </va-button>
                </template>
                <template #cell(cache) = "{rowData}">
                  {{ rowData.cacheName }}
                </template>
                <template #cell(name) = "{rowData}">
                    <va-icon class="mr-3" name="key"/>
                    {{rowData.name}}
                </template>
                <template #cell(timestamp) = "{rowData}">
                    {{rowData.date}}
                </template>
                <template #cell(user) = "{rowData}">
                    {{rowData.data.userEmail}}
                </template>
              </va-data-table>
              <va-pagination
                  size="small"
                  v-model="page"
                  :pages="pages"

              />
        </va-card-content>
      </va-card>
  </div>
  <va-sidebar
      v-model="sidebar"
      :minimized="minimized"
      position="right"
      style="position: absolute;"
      width="27rem"
  >
        <va-card
          square
          outlined
          style="height: 100%;"
        >
          <va-card-title>
          Log details
          <va-spacer></va-spacer>
          <va-icon
            name="close"
            size="large"
            @click="this.sidebar = !this.sidebar"
          />
          </va-card-title>
          <va-card-content v-if="selected">
            <va-list fit>
                <va-list-item
                  class="list__item"
                >
                  <va-list-item-section>
                    <va-list-item-label>
                      <b>ID: </b> {{selected.id}}
                    </va-list-item-label>
                  </va-list-item-section>
                </va-list-item>
                <va-list-item
                  class="list__item"
                >
                  <va-list-item-section>
                    <va-list-item-label>
                      <b>cacheName: </b> {{selected.cacheName}}
                    </va-list-item-label>
                  </va-list-item-section>
                </va-list-item>
                <va-list-item
                  class="list__item"
                >
                  <va-list-item-section>
                    <va-list-item-label>
                      <b>project: </b> {{selected.projectId}}
                    </va-list-item-label>
                  </va-list-item-section>
                </va-list-item>
                <va-list-item
                  class="list__item"
                >
                  <va-list-item-section>
                    <va-list-item-label>
                      <b>date: </b> {{selected.date}}
                    </va-list-item-label>
                  </va-list-item-section>
                </va-list-item>
                <va-list-item
                  class="list__item"
                >
                  <va-list-item-section>
                    <va-list-item-label>
                      <b>type: </b> {{selected.type}}
                    </va-list-item-label>
                  </va-list-item-section>
                </va-list-item>
                <va-list-item
                  class="list__item"
                >
                  <va-list-item-section>
                    <va-list-item-label v-if="selected.data">
                      <b>initiator: </b> {{selected.data.userEmail}}
                    </va-list-item-label>
                  </va-list-item-section>
                </va-list-item>
                <va-list-item
                  class="list__item"
                >
                  <va-list-item-section>
                    <va-list-item-label v-if="selected.data">
                      <b>datasource: </b> {{selected.data.props.datasourceId}}
                    </va-list-item-label>
                  </va-list-item-section>
                </va-list-item>
                <va-list-item
                  class="list__item"
                >
                  <va-list-item-section>
                    <va-list-item-label v-if="selected.data">
                      <b>entity: </b> {{selected.data.props.entity}}
                    </va-list-item-label>
                  </va-list-item-section>
                </va-list-item>
                <va-list-item
                  class="list__item"
                >
                  <va-list-item-section>
                    <va-list-item-label v-if="selected.data">
                      <b>condition: </b> {{selected.data.props.type}}
                    </va-list-item-label>
                  </va-list-item-section>
                </va-list-item>
            </va-list>
          </va-card-content>
          <va-card-content>
            <JsonViewer :value="selected.data" copyable boxed sort theme="light"  @onKeyClick="keyClick"/>
          </va-card-content>
        </va-card>
   </va-sidebar>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import {JsonViewer} from "vue3-json-viewer"
import "vue3-json-viewer/dist/index.css";

export default {
  name: 'TokenTable',
  components: {
    JsonViewer
  },
  props: {
    msg: String
  },
  watch: {
     page: {
        handler(){
            this.update_log_list(this.page)
        }
     }
  },
  computed: {
    pages: function(){
        return this.get_events().totalPages
    }
  },
  data(){
    return {
        filter: '',
        columns: [
          { key: "type", sortable: true},
          { key: "timestamp", sortable: false},
          { key: "cache", sortable: true},
          { key: "user", sortable: true},
          { key: "actions", sortable: false}
        ],
        events: [
        ],
        page: 1,
        sidebar: false,
        selected: {}
    }
  },
  methods: {
    show_details(data){
        this.selected = data
        this.sidebar = true
    },
    ...mapGetters(
        ['get_events', 'get_current_project']
    ),
    ...mapActions(
        ['get_events_by_page']
    ),
    copyId(id){
        navigator.clipboard.writeText(id);
        this.$vaToast.init({ message: 'Copy token (Ctrl + V)', color: 'primary' })
    },
    lastJobStatus(status){
        if (status){
            return 'done'
        } else {
            return 'error'
        }
    },
    update_log_list(page){
        let project = this.get_current_project().id
        let body = {
            projectId: project,
            page: page
        }
        this.get_events_by_page(body)
    }
  },
  mounted() {
    this.events = []
  }
}
</script>

<style scoped>

</style>
