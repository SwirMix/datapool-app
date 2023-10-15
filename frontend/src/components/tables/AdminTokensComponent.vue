<template>
  <div class="va-table-responsive">
        <va-card-title>
            <va-input
              v-model="filter"
              placeholder="Filter..."
              class="w-full"
            />
            <va-spacer></va-spacer>
            <va-button
                class="mt-3 ml-3 flex flex-col"
                icon="add"
                size="small"
                @click = "createToken = !createToken"
            >
                new token
            </va-button>
        </va-card-title>
        <va-card-content>
              <va-data-table
                :items="get_tokens()"
                :columns="columns"
                :wrapper-size="400"
                :item-size="46"
                @filtered="filtered = $event.items"
                :filter="filter"
                virtual-scroller
                sticky-header
              >
                <template #cell(name) = "{rowData}">
                    <va-icon class="mr-3" name="key"/>
                    {{rowData.name}}
                </template>
                <template #cell(creator) = "{rowData}">
                    {{rowData.creator}}
                </template>
                <template #cell(token) = "{rowData}">
                    <va-popover message="copy to clipboard" @click="copyId(rowData.token)">

                      <va-chip square flat>
                        **************
                      </va-chip>
                      <va-icon name="content_copy"/>
                    </va-popover>
                </template>
                <template #cell(actions) = "{rowData}">
                    <va-button
                        preset="plain"
                        icon="edit"
                        @click="modalEdit(rowData)"
                    />
                </template>
              </va-data-table>
        </va-card-content>
  </div>
  <va-sidebar
      v-model="sidebar"
      :minimized="minimized"
      position="right"
      style="position: absolute;"
      width="30rem"
  >
    <va-card style="height: 100%;">
        <va-card-title>
              Edit Token
            <va-spacer></va-spacer>
            <va-icon
                name="close"
                size="large"
                @click="this.sidebar = !this.sidebar"
            />
         </va-card-title>
         <va-card-content>
            <div class="row">
                <va-input
                    v-model="selected.token"
                    class="mt-3 flex flex-col md12"
                    label="token"
                />
            </div>
         </va-card-content>
         <va-card-actions>
           <va-button
              class="mt-3 ml-3 flex flex-col md2"
              icon="delete"
              size="small"
              @click="delete_token()"
           >
              delete
           </va-button>
         </va-card-actions>
     </va-card>
  </va-sidebar>
  <va-modal
    v-model="createToken"
  >
    <template #content >
        <va-card-title>
            new token
        </va-card-title>
        <va-card-content style="width: 40rem;">
            <div class="row">
               <va-input
                  v-model="new_token_name"
                  class="mt-3 md12 flex flex-col"
                  label="Name"
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
              @click="create_token()"
           >
              create
           </va-button>
        </va-card-actions>
    </template>
  </va-modal>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  name: 'AdminTokenComponent',
  components: {

  },
  props: {
    msg: String
  },
  computed: {
    filtered(){
        let data = []
        console.log(this.filter)
        if (this.filter === ''){
            data = this.tokens
        } else {
            for (let token in this.tokens){
                let item = this.tokens[token]
                if (item.token.includes(this.filter)){
                    data.push(item)
                }
            }
        }

        return data
    }
  },
  data(){
    return {
        new_token_name: '',
        createToken: false,
        sidebar: false,
        selected: '',
        filter: '',
        columns: [
          { key: "name", sortable: true},
          { key: "token", sortable: true},
          { key: "creator", sortable: true},
          { key: "actions", sortable: false}
        ]
    }
  },
  methods: {
    ...mapGetters(
        ['get_tokens','get_current_project']
    ),
    ...mapActions(
        ['create_remote_token','get_remote_tokens','delete_remote_token']
    ),
    delete_token(){
        this.delete_remote_token(this.selected.name)
        this.sidebar = false
    },
    create_token(){
        let body = {
            name: this.new_token_name,
            projectId: this.get_current_project().id
        }
        this.create_remote_token(body)
        this.createToken = false
    },
    copyId(id){
        navigator.clipboard.writeText(id);
        this.$vaToast.init({ message: 'Copy token (Ctrl + V)', color: 'primary' })
    },
    modalEdit(rowData){
        this.selected = rowData
        this.sidebar = true
    }
  },
  mounted() {

  }
}
</script>

<style scoped>

</style>
