<template>
  <div>
    <va-card outlined style="height: 60rem;" v-if="!content_edit">
        <va-card-title>
            {{this.content.title}} <va-spacer></va-spacer>   <va-icon class="ml-4" name="edit" @click="edit_mode(true)"/>
        </va-card-title>
        <va-card-content>
          <va-alert
            color="info"
            outline
            icon="info"
            class="mb-6"
          >
              <template #title>
                <h4>Summary</h4>
              </template>
              <va-divider/>
            {{content.summary}}
          </va-alert>
        </va-card-content>
        <va-card-content>
          <va-alert
            color="info"
            outline
            class="mb-6"
          >
              <template #title>
                <h4>Special conditions</h4>
              </template>
              <va-divider/>
            {{content.conditions}}
          </va-alert>
        </va-card-content>
        <va-card-content>
          <va-alert
            color="info"
            outline
            class="mb-6"
          >
              <template #title>
                <h4>Table columns description</h4>
              </template>
              <va-divider/>
            <va-data-table
                :items="content.columns"
                :wrapper-size="550"
                :item-size="46"
                virtual-scroller
            />
          </va-alert>
        </va-card-content>
    </va-card>
    <va-card outlined style="height: 60rem;" v-else>
        <va-card-title>
            <va-input
              v-model="edit_content.title"
              :max-length="128"
              counter
              class="mb-12"
              style="width: 80%;"
            />
            <va-button
                icon="save"
                class="ml-3 mb-3"
                @click="save_action()"
            >
               Save
            </va-button>
            <va-button
                icon="close"
                class="ml-3 mb-3"
                @click="edit_mode(false)"
            >
               cancel
            </va-button>
        </va-card-title>
        <va-card-content>
          <va-alert
            color="info"
            outline
            icon="info"
            class="mb-6"
          >
              <template #title>
                <h4>Summary</h4>
              </template>
              <va-divider/>
              <va-input
                  v-model="edit_content.summary"
                  :max-length="600"
                  counter
                  type="textarea"
                  :min-rows="3"
                  :max-rows="3"
                  class="mb-12"
                  style="width: 100%;"
              />
          </va-alert>
        </va-card-content>
        <va-card-content>
          <va-alert
            color="info"
            outline
            class="mb-6"
          >
              <template #title>
                <h4>Special conditions</h4>
              </template>
              <va-divider/>
              <va-input
                  v-model="edit_content.conditions"
                  :max-length="400"
                  counter
                  type="textarea"
                  :min-rows="3"
                  :max-rows="3"
                  class="mb-12"
                  style="width: 100%;"
              />
          </va-alert>
        </va-card-content>
        <va-card-content>
          <va-alert
            color="info"
            outline
            class="mb-6"
          >
              <template #title>
                <h4>Table columns description</h4>
              </template>
              <va-divider/>
            <va-data-table
                :items="edit_content.columns"
                :wrapper-size="400"
                virtual-scroller
            >
                <template #cell(desc) = "{rowData}">
                    <va-input
                      v-model="rowData.desc"
                      :max-length="128"
                      counter
                      class="mb-12"
                      style="width: 100%;"
                    />
                </template>
            </va-data-table>
          </va-alert>
        </va-card-content>
    </va-card>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'
import axios from 'axios';

export default {
  name: 'CacheNotesComponent',
  components: {

  },
  props: {
    msg: String
  },
  data(){
      return {
        content_edit: false,
        edit_content: {},
        currentPage: 0,
        content: {
           title: 'Кэш выгрузки полетов внутренних междугородних рейсов',
           summary: 'Общий пул зарегистрированных и уже осуществленных полетов.',
           conditions: 'Международный аэропорт Пулково (ИАТА: LED, ИКАО: ULLI) — международный аэропорт, имеющий статус федерального значения[2], располагающийся в 15 км от центра Санкт-Петербурга в Московском районе. Единственный аэропорт Санкт-Петербурга, обслуживающий официальные рейсы. Пулково является одним из наиболее загруженных аэропортов России. Имущество аэропорта с 2010 года по государственно-частному партнёрству находится в управлении ООО «Воздушные ворота Северной столицы»[3][4]. ',
           columns: [
            {
                name: 'amount',
                type: 'numeric',
                desc: 'Стоимость билета'
            },
            {
                name: 'ticket_no',
                type: 'bpchar',
                desc: 'Номер билета'
            },
            {
                name: 'flight_id',
                type: 'int4',
                desc: 'Идентификатор полета'
            },
            {
                name: 'fare_conditions',
                type: 'varchar',
                desc: 'Тип места в самолете. Эконом, Бизнес.'
            }
           ]
        }
      }

  },
  methods: {
    ...mapActions(
        ['update_tokens_in_cache']
    ),
    ...mapGetters(
        ['get_current_project', 'get_current_cache_info']
    ),
    edit_mode(status){
        this.content_edit = status
        this.edit_content = JSON.parse(JSON.stringify(this.get_current_cache_info()));
    },
    save_action(){
        this.content_edit = false
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        let request = {
            key: {
                cacheName: this.edit_content.metadata.cacheMetadata.cache.cacheName,
                project: this.edit_content.metadata.cacheMetadata.cache.project,
            },
            cacheInfo: {
                title: this.edit_content.title,
                summary: this.edit_content.summary,
                conditions: this.edit_content.conditions,
                columns: this.edit_content.columns
            }
        }
        let toast = this.$vaToast
        let responseBody = {}
        axios.post(this.$store.state.backend + '/api/v1/cache/info', request, config).then(function(response){
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
        this.content_edit = false
    }
  },
  created(){
    this.content = this.get_current_cache_info()
  },
  mounted(){
    this.content = this.get_current_cache_info()
  }
}
</script>

<style lang="scss" scoped>
@import "vuestic-ui/styles/grid";

.row {
  min-height: 6rem;

  & + .row {
    border-top: 1px solid var(--va-background-border);
  }
}

.item {
  border: 1px solid var(--va-background-border);
  background-color: var(--va-background-primary);
  text-align: center;
}
</style>