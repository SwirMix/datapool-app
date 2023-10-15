<template>
  <div class="va-table-responsive">
      <va-card>
        <va-card-title>
        </va-card-title>
        <va-card-content>
              <va-data-table
                :items="datasources"
                :columns="columns"
                :wrapper-size="700"
                :item-size="46"
                @filtered="filtered = $data.items"
                :filter="filter"
                virtual-scroller
                sticky-header
              >
                <template #cell(type) = "{rowData}">
                  <va-avatar
                        v-if="rowData.properties.type === 'POSTGRESQL'"
                        size="small"
                        class="mr-4"
                        :src='require("@/assets/pg.png")'
                  >
                  </va-avatar>
                </template>
                <template #cell(actions) = "{rowData}">
                    <router-link :to="{ name: 'DataSourceDetails', params: { id: rowData.id }}">
                       <va-button size="small" icon="info"> Details </va-button>
                    </router-link>
                </template>
              </va-data-table>
        </va-card-content>
      </va-card>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'DataSourceTable',
  components: {

  },
  props: {
    msg: String
  },
  computed: {
    datasources: function(){
        return this.get_datasources()
    },
    filtered(){
        let data = []
        let events = this.datasources
        console.log(this.filter)
        if (this.filter === ''){
            data = events
        } else {
            for (let event in events){
                let item = events[event]
                if (item.message.includes(this.filter)){
                    data.push(item)
                }
            }
        }
        return data
    }
  },
  data(){
    return {
        filter: '',
        columns: [
          { key: "type", sortable: true},
          { key: "id", sortable: false},
          { key: "name", sortable: true},
          { key: "actions", sortable: false}
        ]
    }
  },
  methods: {
    ...mapGetters(
        ['get_datasources']
    ),
    copyId(id){
        navigator.clipboard.writeText(id);
        this.$vaToast.init({ message: 'Copy token (Ctrl + V)', color: 'primary' })
    }
  },
  mounted() {
    this.datasources = this.get_datasources()
  }
}
</script>

<style scoped>

</style>
