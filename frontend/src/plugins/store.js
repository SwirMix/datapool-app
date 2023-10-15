import Vuex from 'vuex'
import backend from '@/plugins/BackendMock.js'
import axios from 'axios'
import router from '@/plugins/router.js'
import backends from '@/static/server_map.json'

const store = new Vuex.Store({
  state: {
    backend: backends.api,
    storageApp: backends.storage,
    datapool_check_data: {},
    auth: false,
    success_login_request: false,
    processing: true,
    toast: {
        show: false,
        message: 'debug message'
    },
    user: {
        token: localStorage.datapool_token,
        email: '',
        global_admin: false
    },
    projects: [],
    current_project: {
        id: "",
        name: "",
        description: "",
        active: true,
        userId: "",
        role: "",
        created: ""
    },
    caches: [],
    team: [],
    datasources: [],
    current_datasource: {
        id: "45464a11-3cd0-4a1e-97da-a62bce6af9d8",
        projectId: "711bff9d-a873-4497-873f-fbfceb220071",
        name: "",
        properties: {
            type: "POSTGRESQL",
            properties: {
                schema: "profile",
                password: "perfcona",
                url: "jdbc:postgresql://localhost:5432/perfcona?currentSchema=datapool",
                username: "perfcona"
            }
        }
    },
    current_cache_info: {
        metadata: {},
        datasource: {},
        ready: false
    },
    tokens: [],
    events: {
        events: [],
        totalPages: 1
    },
    createCacheDialog: false,
    runtime_cache_status: {
        success: true,
        msg: 'Internal error. Check body.'
    },
    users: [],
    projects_admin: [],
    consumers_list: [],
    admin_settings: [],
    storage: []
  },
  getters: {
    get_storage: state => {
        let storage = []
        state.storage.forEach(function(entry) {
            let file = entry.fileInfo
            file['url'] = entry.url
            storage.push(file)
        });
        return storage
    },
    get_datapool_check_data: state => {
        return state.datapool_check_data
    },
    get_processing_status: state => {
        return state.processing
    },
    get_admin_settings: state => {
        return state.admin_settings
    },
    get_consumers_list: state => {
        return state.consumers_list
    },
    get_consumers_success_rate: state => {
        let backgroundColor = ['#41B883', '#E46651']
        let labels = []
        let data = []
        let total_success = 0
        let total_fails = 0
        state.consumers_list.forEach(function(entry) {
            total_success += entry.count
            total_fails += entry.errors
        });
        data.push(((total_success-total_fails)/total_success)*100)
        labels.push('success: ' + (((total_success-total_fails)/total_success)*100) + '%')
        data.push((total_fails/total_success)*100)
        labels.push('failed: ' + ((total_fails/total_success)*100) + '%')
        let chart = {
            labels: labels,
            datasets: [
               {
                backgroundColor: backgroundColor,
                data: data
               }
            ]
        }
        return chart
    },
    get_consumers_chart: state => {
        let backgroundColor = ['#41B883', '#E46651', '#3be733', '#33c6e7', '#3349e7', '#c133e7']
        let colors = []
        let labels = []
        let data = []
        state.consumers_list.forEach(function(entry) {
            labels.push(entry.consumer + '[' + entry.cache + ' ]')
            data.push(entry.count)
            colors.push(backgroundColor[Math.floor(Math.random()*backgroundColor.length)])
        });
        let chart = {
            labels: labels,
            datasets: [
               {
                backgroundColor: colors,
                data: data
               }
            ]
        }
        return chart
    },
    get_success_login_request: state => {
        return state.success_login_request
    },
    get_users: state => {
        return state.users
    },
    get_projects_admin: state => {
        return state.projects_admin
    },
    get_runtime_cache_status: state => {
        return state.runtime_cache_status
    },
    get_create_cache_dialog: state => {
        return state.createCacheDialog
    },
    get_current_cache_info: state => {
        return state.current_cache_info
    },
    get_current_datasource_table: state => {
        let result_tables = []
        let tables = state.current_datasource.tables
        if (tables){
                tables.forEach(function(entry) {
                    console.log(entry['tableName'])
                    let table = {
                        tableName: entry['tableName'],
                        columns: []
                    }
                    for (var key in entry.columns) {
                      table.columns.push(
                        { label: key + ' type: ' + entry.columns[key]}
                      )
                    }
                    result_tables.push(table)
                });
        }
        return result_tables
    },
    check_current_datasource_exist: state => {
        if (state.current_datasource.success){
            return true
        } else {
            return false
        }
    },
    get_current_datasource: state => {
        return state.current_datasource.result
    },
    get_events: state => {
        return state.events
    },
    get_toast: state => {
        return state.toast
    },
    get_tokens: state => {
        return state.tokens
    },
    get_datasources: state => {
        return state.datasources
    },
    get_current_team: state => {
        return state.team
    },
    get_current_caches: state => {
        return state.caches
    },
    get_user_info: state => {
        let info = {
            email: state.user.email,
            role: state.current_project.role
        }
        return info
    },
    check_auth: state => {
        return state.auth
    },
    get_projects: state => {
        return state.projects
    },
    get_current_project: state => {
        return state.current_project
    },
    get_current_project_for_option_field: state => {
          let project = {
            'text': state.current_project.name + ' [ ' + state.current_project.id + ' ]',
            'value': state.current_project,
            'id': state.current_project.id
          }
          return project
    },
    get_projects_for_options_field: state => {
        let data = []
        for (let i = 0; i < state.projects.length; i += 1) {
          // Этот код выполняется для каждого элемента
          let item = state.projects[i]
          let text = item.name + ' [ ' + item.id + ' ]'
          let value = item
          let id = item.id

          let project = {
            'text': text,
            'value': value,
            'id': id
          }
          data.push(project)
        }
        return data
    },
    get_global_admin: state => {
        return state.user.global_admin
    },
    check_project_admin: state => {
        if (state.current_project.role === 'ADMIN'){
            return true
        } else {
            return false
        }
    }
  },
  mutations: {
    set_storage(state, data){
        state.storage = data
    },
    set_datapool_check_data(state, data){
        state.datapool_check_data = data
    },
    set_processing_status(state, status){
        state.processing = status
    },
    set_admin_settings(state, settings){
        state.admin_settings = settings
    },
    set_consumers_list(state, consumers){
        state.consumers_list = consumers
    },
    set_success_login_request(state, success_login_request){
        state.success_login_request = success_login_request
    },
    set_projects_admin(state, projects){
        state.projects_admin = projects
    },
    set_auth(state, auth){
        state.auth = auth
        state.success_login_request = auth
        state.user.global_admin = auth
    },
    set_users(state, users){
        state.users = users
    },
    set_runtime_cache_status(state, status){
        state.runtime_cache_status = status
    },
    set_create_cache_dialog(state, status){
        state.runtime_cache_status.success = true
        state.createCacheDialog = status
    },
    set_current_cache_info(state, cache_info){
        state.current_cache_info.datasource = cache_info.datasource
        state.current_cache_info.metadata = cache_info.metadata
        state.current_cache_info.ready = true
        state.current_cache_info.summary = cache_info.summary
        state.current_cache_info.title = cache_info.title
        state.current_cache_info.columns = cache_info.columns
        state.current_cache_info.conditions = cache_info.conditions
    },
    set_datasource_tables(state, datasource_table){
        state.current_datasource.tables = datasource_table
    },
    set_current_datasource(state, datasource){
        state.current_datasource = datasource
    },
    set_events(state, events){
        state.events = events
    },
    set_toast(state, toast){
        state.toast = toast
    },
    set_tokens(state, tokens){
        state.tokens = tokens
    },
    set_datasources(state, datasources){
        state.datasources = datasources
    },

    set_team(state, team){
        state.team = team
    },

    set_caches(state, caches){
        state.caches = caches
    },

    set_auth_info(state, data){
        state.user.token = data.jwt
        localStorage.datapool_token = data.jwt
        state.user.email = data.email
        state.user.global_admin = data.globalAdmin
    },

    set_projects(state, projects){
        state.projects = projects
    },

    set_current_project(state, projectId){
        router.push('/')
        state.team = []
        state.caches = []
        state.datasources = []
        state.tokens = []
        for (let i = 0; i < state.projects.length; i += 1) {
                if (projectId === state.projects[i].id){
                    state.current_project = state.projects[i]
                    //Обновляем информацию по кэшам в соответствии с проектом
                    let config = {
                        headers: {
                          token: localStorage.datapool_token,
                        }
                    }
                    axios.get(backend.backend + '/api/v1/cache/' + projectId, config).then(function (response) {
                       if (response.data.success){
                            console.log(response.data)
                            state.caches = response.data.result
                            state.toast = {
                                show: false,
                                message: response.data.result.message,
                            }
                       } else {
                            state.toast = {
                                show: true,
                                message: response.data.result.message,
                            }
                       }
                    })
                    //Обновляем информацию по команду
                    axios.get(backend.backend + '/api/v1/projects/team/' + projectId, config).then(function (response) {
                       if (response.data.success){
                          state.team = response.data.result
                       }
                    })
                    //обновляем информацию о датасорсах
                    axios.get(backend.backend + '/api/v1/datasource/' + projectId, config).then(function (response) {
                       if (response.data.success){
                          console.log(response.data)
                          state.datasources = response.data.result
                       }
                    })
                    //Обновляем информацию о токенах проекта
                    axios.get(backend.backend + '/api/v1/projects/tokens/' + projectId, config).then(function (response) {
                       if (response.data.success){
                          console.log(response.data)
                          state.tokens = response.data.result
                       }
                    })
                    //Обновляем информация о событиях кэшей.
                    axios.get(backend.backend + '/api/v1/log/' + projectId, config).then(function (response) {
                       if (response.data.success){
                          console.log(response.data.result.content)
                          state.events.events = response.data.result.content
                          state.events.totalPages = response.data.result.totalPages
                       }
                    })
                }
            }
            if (state.user.token !== '' && state.current_project.id !== ''){
                state.auth = true
            }
    }
  },
  actions: {
    update_tokens_in_cache(context, project){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.post(backend.backend + '/api/v1/projects/tokens/' + project, null, config).then(function(response){
            let responseBody = response.data
            if (responseBody.success === true){
                console.log(response.data)
            } else {
                status.success = responseBody.success
                status.msg = 'Internal error. Check body.'
            }
        })
    },
    get_project_storage(context, project){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.get(backend.backend + '/api/v1/storage/' + project , config).then(function (response) {
            if (response.data.success){
                console.log(response.data)
                context.commit('set_storage', response.data.result)
            }
        }).catch(function (error) {
           console.log(error)
        })
    },
    update_cache_cursor(context, data){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.post(backend.backend + '/api/v1/cache/update/cursor/' + data.projectId + '/' + data.cacheName + "?cursor=" + data.cursor, null, config).then(function(response){
                    let responseBody = response.data
                    if (responseBody.success === true){
                        console.log(response.data)
                    } else {
                        status.success = responseBody.success
                        status.msg = 'Internal error. Check body.'
                    }
                })
    },
    reload_cache(context, data){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.post(backend.backend + '/api/v1/cache/reload/' + data.projectId + '/' + data.cacheName, null, config).then(function(response){
            let responseBody = response.data
            if (responseBody.success === true){
                console.log(response.data)
            } else {
                status.success = responseBody.success
                status.msg = 'Internal error. Check body.'
            }
        })
    },
    check_datapool_data(context, data){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.get(backend.backend + '/api/v1/cache/datapool?' + 'project=' + data.project + '&cacheName=' + data.cacheName + '&strategy=' + data.strategy + '&key=' + data.key , config).then(function (response) {
            if (response.data.success){
                console.log(response.data)
                context.commit('set_datapool_check_data', response.data.result)
            }
        }).catch(function (error) {
           console.log(error)
        })
    },
    download_service_settings(context){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.get(backend.backend + '/api/v1/admin/settings' , config).then(function (response) {
            if (response.data.success){
                console.log(response.data)
                context.commit('set_admin_settings', response.data.result)
            }
        })
    },
    post_service_settings(context, settings){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.post(backend.backend + '/api/v1/admin/settings', settings, config).then(function(response){
            let responseBody = response.data
            if (responseBody.success === true){
                console.log(response.data)
            } else {
                status.success = responseBody.success
                status.msg = 'Internal error. Check body.'
            }
        })
    },
    request_consumers_list(context, body){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.get(backend.backend + '/api/v1/log/consumers/' + body.project + '/' + body.cache + '?start=' + body.start + '&end=' + body.end , config).then(function (response) {
            if (response.data.success){
                console.log(response.data)
                context.commit('set_consumers_list', response.data.result)
            }
        })
    },
    request_consumers_list_all(context, body){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.get(backend.backend + '/api/v1/log/consumers/' + body.project + '?start=' + body.start + '&end=' + body.end , config).then(function (response) {
            if (response.data.success){
                console.log(response.data)
                context.commit('set_consumers_list', response.data.result)
            }
        })
    },

    create_user(context, body){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.post(backend.backend + '/api/v1/account/create', body, config).then(function(response){
            let responseBody = response.data
            if (responseBody.success === true){
                console.log(response.data)
            } else {
                status.success = responseBody.success
                status.msg = 'Internal error. Check body.'
            }
        })
    },
    create_project(context, body){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.post(backend.backend + '/api/v1/projects/create', body, config).then(function(response){
            let responseBody = response.data
            if (responseBody.success === true){
                console.log(response.data)
            } else {
                status.success = responseBody.success
                status.msg = 'Internal error. Check body.'
            }
        })
    },
    delete_user_data(context, user){
      let config = {
        headers: {
            token: localStorage.datapool_token,
        }
      }
      axios.delete(backend.backend + '/api/v1/admin/users/' + user.id, config).then(function(response){
            if (response.data.success){
                console.log(response.data)
            }
      }).catch(function (error) {
            console.log(error)
      })
    },
    update_project(context, body){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.post(backend.backend + '/api/v1/admin/projects', body, config).then(function(response){
            let responseBody = response.data
            if (responseBody.success === true){
                console.log(response.data)
            } else {
                status.success = responseBody.success
                status.msg = 'Internal error. Check body.'
            }
        })
    },
    update_user(context, body){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.post(backend.backend + '/api/v1/admin/users', body, config).then(function(response){
            let responseBody = response.data
            if (responseBody.success === true){
                console.log(response.data)
            } else {
                status.success = responseBody.success
                status.msg = 'Internal error. Check body.'
            }
        })
    },
    delete_user(context, user){
      let config = {
        headers: {
            token: localStorage.datapool_token,
        }
      }
          axios.delete(backend.backend + '/api/v1/projects/member/', {headers: config.headers, data: user}).then(function(response){
            if (response.data.success){
                console.log(response.data)
            }
          }).catch(function (error) {
            console.log(error)
          })
    },
    add_user_to_team(context, user){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.post(backend.backend + '/api/v1/projects/member', user, config).then(function(response){
            let responseBody = response.data
            if (responseBody.success === true){
                console.log(response.data)
            }
        })
    },
    users_request(context){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.get(backend.backend + '/api/v1/account/users', config).then(function (response) {
            if (response.data.success){
                console.log(response.data)
                context.commit('set_users', response.data.result)
            }
        })
    },
    projects_request(context){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.get(backend.backend + '/api/v1/admin/projects', config).then(function (response) {
            if (response.data.success){
                console.log(response.data)
                context.commit('set_projects_admin', response.data.result)
            }
        })
    },
    get_cache_data_page(page){
        console.log(page)
    },
    update_datasource_request(context, body){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.post(backend.backend + '/api/v1/datasource/update', body, config).then(function(response){
            let responseBody = response.data
            if (responseBody.success === true){
                console.log(response.data)
            }
        })
        axios.get(backend.backend + '/api/v1/datasource/' + body.projectId, config).then(function (response) {
            if (response.data.success){
                console.log(response.data)
                context.commit('set_datasources', response.data.result)
            }
        })
    },
    delete_datasource(context, datasourceId){
      let config = {
        headers: {
            token: localStorage.datapool_token,
        }
      }
      try {
          axios.delete(backend.backend + '/api/v1/datasource/delete/' + datasourceId, config).then(function(response){
            if (response.data.success){
                console.log(response.data)
                context.commit('set_datasources', response.data.result)
            }
          })
      } catch (err) {
          console.log(err)
      }
    },
    create_jdbc_datasource(context, body){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.post(backend.backend + '/api/v1/datasource/create', body, config).then(function(response){
            let responseBody = response.data
            if (responseBody.success === true){
                console.log(response.data)
            }
        })
    },
    get_remote_tokens(context, projectId){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.get(backend.backend + '/api/v1/projects/tokens/' + projectId, config).then(function (response) {
            if (response.data.success){
               console.log(response.data)
               context.commit('set_tokens', response.data.result)
            }
        })
    },
    delete_remote_token(context, name){
      let config = {
        headers: {
            token: localStorage.datapool_token,
        }
      }
      try {
          axios.delete(backend.backend + '/api/v1/projects/tokens/' + name, config).then(function(response){
            if (response.data.success){
                console.log(response.data)
                context.commit('set_tokens', response.data.result)
            }
          })
      } catch (err) {
          console.log(err)
      }
    },
    create_remote_token(context, body){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.post(backend.backend + '/api/v1/projects/tokens', body, config).then(function(response){
            let responseBody = response.data
            if (responseBody.success === true){
                console.log(response.data)
                context.commit('set_tokens', response.data.result)
            }
        })
    },
    change_cache_status(context, body){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.post(backend.backend + '/api/v1/cache/status', body, config).then(function(response){
            let responseBody = response.data
            if (responseBody.success === true){
                console.log(response.data)
                context.commit('set_current_cache_info', response.data.result)
            } else {
                status.success = responseBody.success
                status.msg = 'Internal error. Check body.'
            }
        })
    },
    create_runtime_cache(context, body){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        let status = {
            success: true,
            msg: 'Cache was created'
        }
        axios.post(backend.backend + '/api/v1/cache/create/runtime', body, config).then(function(response){
            let responseBody = response.data
            if (responseBody.success === true){
                status.success = responseBody.success
                context.commit('set_create_cache_dialog', false)
                console.log(response.data)
                context.commit('set_caches', response.data.result)
            } else {
                status.success = responseBody.success
                status.msg = 'Internal error. Check body.'
                context.commit('set_runtime_cache_status', status)
            }
        })
    },
    get_cache_info(context, body){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.get(backend.backend + '/api/v1/cache/' + body.projectId + '/' + body.cacheName, config).then(function (response) {
            if (response.data.success){
               console.log(response.data)
               context.commit('set_current_cache_info', response.data.result)
            }
        }).catch(e => {
            if (e.response.status === 404) {
               console.log('404 datasource')
               router.push('/404')
            }
        })
    },
    delete_cache_action(context, body){
      let config = {
        headers: {
            token: localStorage.datapool_token,
        }
      }
      try {
          axios.delete(backend.backend + '/api/v1/cache/' + body.project + '/' + body.cacheName, config).then(function(response){
            if (response.data.success){
                console.log(response.data)
                context.commit('set_caches', response.data.result)
            }
          })
      } catch (err) {
          console.log(err)
      }
    },
    create_cache_from_table(context, body){
      console.log(body)
      let config = {
        headers: {
            token: localStorage.datapool_token,
        }
      }
      try {
          axios.post(backend.backend + '/api/v1/cache/create/jdbc', body, config).then(function (response) {
                if (response.data.success){
                    console.log(response.data)
                }
                router.push({ name: 'CacheDetails', params: { id: body.key.project + '_' +  body.key.cacheName}})
          })
      } catch (err) {
          console.log(err)
      }

    },
    get_datasource_tables(context, datasourceId){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.get(backend.backend + '/api/v1/datasource/browse/' + datasourceId, config).then(function (response) {
            if (response.data.success){
               console.log(response.data)
               context.commit('set_datasource_tables', response.data.result)
            }
        })
    },
    get_datasource_details(context, datasourceId){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.get(backend.backend + '/api/v1/datasource/details/' + datasourceId, config).then(function (response) {
            if (response.data.success){
               console.log(response.data)
               context.commit('set_current_datasource', response.data)
            }
        }).catch(e => {
           if (e.response.status === 404) {
              console.log('404 datasource')
              router.push('/404')
           }
        });
    },
    get_events_by_page(context, body){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.get(backend.backend + '/api/v1/log/' + body.projectId + '?page=' + body.page, config).then(function (response) {
            if (response.data.success){
               console.log(response.data.result.content)
               let events = {
                    events: response.data.result.content,
                    totalPages: response.data.result.totalPages
               }
               context.commit('set_events', events)
            }
        })
    },
    auth(context, body) {
      axios.post(backend.backend + '/api/v1/account/auth', {
            email: body.email,
            password: body.password
          }).then(function (response) {
            if (response.data.success){
              context.commit('set_auth_info', response.data.result)
              context.commit('set_projects', response.data.result.projects)
              context.commit('set_success_login_request', true)
            }
      }).catch(function (error) {
         console.log(error.toJSON());
      });
      return true
    },
    get_caches(context, projectId){
      let config = {
        headers: {
          token: localStorage.datapool_token,
        }
      }
      context.commit('set_processing_status', true)
      axios.get(backend.backend + '/api/v1/cache/' + projectId, config).then(function (response) {
            if (response.data.success){
                console.log(response.data)
                context.commit('set_caches', response.data.result)
                context.commit('set_processing_status', false)
            }
      })
    },
    get_team(context, projectId){
      let config = {
        headers: {
          token: localStorage.datapool_token,
        }
      }
      axios.get(backend.backend + '/api/v1/projects/team/' + projectId, config).then(function (response) {
            if (response.data.success){
                console.log(response.data)
                context.commit('set_team', response.data.result)
            }
      })
    },
    get_datasources(context, projectId){
      let response = backend.get_datasources(projectId)
      if (response.success){
        context.commit('set_datasources', response.result)
        return true
      } else return false
    }
  }
})

export default store