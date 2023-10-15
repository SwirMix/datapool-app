<template>
  <div class="va-table-responsive mt-1 mr-2">
      <va-card>
        <va-card-title>
            <va-spacer></va-spacer>
            <va-button
               icon="table_chart"
               color="primary"
               class="mr-2"
               size="small"
               @click="this.get_data_page()"
            >
               load
            </va-button>
        </va-card-title>
        <va-card-content>
              <div class="row justify-space-around" style="padding-top: 2%;" v-if="(this.data.length === 0) && (processed)" >
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
                :item-size="20"
                virtual-scroller
                sticky-header
              >
                <template #cell(actions)>
                    <div style="text-align: end;">
                      <va-button
                        icon="table_chart"
                        color="primary"
                        class="mr-2"
                        size="small"
                      >
                        details
                      </va-button>
                    </div>
                </template>
              </va-data-table>
              <va-pagination
                  size="small"
                  v-model="page"
                  :pages="pages"
                  :visible-pages="10"
              />
        </va-card-content>
      </va-card>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import axios from 'axios'

export default {
  name: 'CacheDataTable',
  components: {

  },
  props: {
    msg: String
  },
  watch: {
     page: {
        handler(){
            this.get_data_page()
        }
     }
  },
  computed: {
  },
  data(){
    return {
        pages: 1,
        columns: [],
        page: 1,
        data: [],
        processed: false,
        value: [45, 65],
    }
  },
  methods: {
    ...mapGetters([
        'get_current_project',
        'get_current_cache_info'
    ]),
    processTrackLabel(value, order) {
      return order === 0 ? `min ${value}$` : `max ${value}$`;
    },
    get_data_page(){
        this.processed = true
        try {
            let config = {
                headers: {
                    token: localStorage.datapool_token,
                }
            }
            let cacheKey = this.get_current_cache_info().metadata.cacheMetadata.cache
            axios.get(this.$store.state.backend + '/api/v1/cache/datapool/data/' + cacheKey.project + '/' + cacheKey.cacheName + '?page=' + this.page , config).then((response) => {
                this.columns = response.data.result.columns
                this.pages = response.data.result.totalPage
                this.data.length = 0
                let finaly = this.data
                response.data.result.data.forEach(function(entry) {
                    let item = {}
                    for (let key in entry){
                        console.log('IS ' + (entry[key] instanceof Object))
                        if (entry[key] instanceof Object){
                            item[key] = JSON.stringify(entry[key])
                        } else {
                            item[key] = entry[key]
                        }
                        console.log(item)
                    }
                    finaly.push(item)
                });
            }).catch(e => {
                console.log(e)
            });
        } catch (err) {
            console.log(err)
        }
    },
    isObject(val) {
        return val instanceof Object;
    }
  },
  mounted() {
    try {
        this.data.length = 0
        this.data.columns = 0
        //this.get_data_page()
    } catch (err) {
        console.log(err)
    }
  },
  created() {
    try {
        this.data.length = 0
        this.data.columns = 0
        //this.get_data_page()
    } catch (err) {
        console.log(err)
    }
  }
}
</script>

<style scoped>

</style>
