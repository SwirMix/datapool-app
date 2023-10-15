<template>
   <div class="row">
       <div class="flex flex-col md12 ma-3">
          <va-breadcrumbs>
             <va-breadcrumbs-item label="Tokens" />
          </va-breadcrumbs>
       </div>
       <div class="flex flex-col md12 ma-3">
        <va-card outlined>
            <va-card-content style="text-align: end;">
                <va-button icon="cached" @click="update_tokens_in_cache(get_current_project().id)">
                    update tokens
                </va-button>
            </va-card-content>
        </va-card>
       </div>
       <div class="flex flex-col md12 ma-3">
          <TokenTable/>
       </div>
   </div>
</template>

<script>
import TokenTable from '@/components/tables/TokenTable.vue'
import { mapActions, mapGetters } from 'vuex'

export default {
  name: 'TokenPage',
  components: {
    TokenTable
  },
  props: {
    msg: String
  },
  data(){
  },
  methods: {
    ...mapActions(
        ['update_tokens_in_cache']
    ),
    ...mapGetters(
        ['get_current_project']
    )
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