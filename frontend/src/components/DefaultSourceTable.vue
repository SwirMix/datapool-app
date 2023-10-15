<template>
  <div class="va-table-responsive">
      <va-card>
        <va-card-title>
        </va-card-title>
        <va-card-content>
              <va-data-table
                :items="tables"
                :columns="columns"
                :wrapper-size="400"
                :item-size="46"
                virtual-scroller
                sticky-header
              >
                <template #cell(actions)>
                  <va-button
                    preset="plain"
                    icon="input"
                    class="ml-3"
                  />
                </template>
              </va-data-table>
        </va-card-content>
      </va-card>
  </div>
</template>

<script>

export default {
  name: 'NavBar',
  components: {

  },
  props: {
    msg: String
  },
  data(){
    return {
        columns: [
          { key: "table", sortable: true},
          { key: "size", sortable: true},
          { key: "columns", sortable: true},
          { key: "actions", sortable: false},
        ],
        tables: [
            {
                table: 'profiles',
                size: 95000000,
                columns: {
                    id: 'text',
                    login: 'text',
                    password: 'text'
                },
                status: 'READY',
                popularity: 4,
                consumers: 5
            },
            {
                table: 'users_accounts',
                size: 95000000,
                columns: {
                    id: 'text',
                    login: 'text',
                    password: 'text'
                },
                status: 'READY',
                popularity: 4,
                consumers: 5
            },
        ]
    }
  },
  methods: {

  }
}
</script>

<style scoped>

</style>
