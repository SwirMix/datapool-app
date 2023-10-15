<template>
  <div class="va-table-responsive">
      <va-card>
        <va-card-title>
            <va-input
              v-model="filter"
              placeholder="Filter..."
              class="w-full"
            />
        </va-card-title>
        <va-card-content>
              <va-data-table
                :items="tokens"
                :columns="columns"
                :wrapper-size="600"
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
                      {{rowData.token}}
                    </va-popover>
                </template>
              </va-data-table>
        </va-card-content>
      </va-card>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'TokenTable',
  components: {

  },
  props: {
    msg: String
  },
  computed: {
    tokens: function(){
       return this.get_tokens()
    },
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
        filter: '',
        columns: [
          { key: "name", sortable: true},
          { key: "token", sortable: true},
          { key: "creator", sortable: true}
        ]
    }
  },
  methods: {
    ...mapGetters(
        ['get_tokens']
    ),
    copyId(id){
        navigator.clipboard.writeText(id);
        this.$vaToast.init({ message: 'Copy token (Ctrl + V)', color: 'primary' })
    },
  },
  mounted() {
    this.tokens = this.get_tokens()
  }
}
</script>

<style scoped>

</style>
