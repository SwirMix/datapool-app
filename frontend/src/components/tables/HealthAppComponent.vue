<template>
  <div class="va-table-responsive">
        <va-card-title>
        </va-card-title>
        <va-card-content>
              <div class="row justify-space-around" style="padding-top: 2%;" v-if="(this.data.length === 0)" >
                    <v-container fill-height>
                      <v-layout row wrap align-center>
                          <va-progress-circle indeterminate></va-progress-circle>
                      </v-layout>
                    </v-container>
              </div>
              <va-data-table
                :items="data"
                :columns="columns"
                :wrapper-size="400"
                :item-size="46"
                virtual-scroller
                sticky-header
              >
                <template #cell(name) = "{rowData}">
                    <a
                      :href="rowData.path"
                    >
                    {{rowData.name}}
                    </a>
                </template>
              </va-data-table>
        </va-card-content>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'HealthAppComponent',
  components: {

  },
  props: {
    msg: String
  },
  computed: {

  },
  data(){
    return {
        columns: [
          { key: "name", sortable: true},
          { key: "status", sortable: true},
          { key: "path", sortable: true},
        ],
        data: []
    }
  },
  methods: {
    get_data(){
        try {
            let config = {
                headers: {
                    token: localStorage.datapool_token,
                }
            }
            axios.get(this.$store.state.backend + '/api/v1/health/' , config).then((response) => {
                response.data
                this.data = response.data.result
            })
        } catch (err) {
            console.log(err)
        }
    }

  },
  mounted() {
    this.get_data()
  },
  created() {
    this.get_data()
  }
}
</script>

<style scoped>

</style>
