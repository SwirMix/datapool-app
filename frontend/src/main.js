import { createApp } from 'vue'
import App from './App.vue'
import router from '@/plugins/router.js'
import 'material-design-icons-iconfont/dist/material-design-icons.css'
import "@mdi/font/css/materialdesignicons.css";
import "@fortawesome/fontawesome-free/css/all.css";
import { createVuestic, createIconsConfig} from "vuestic-ui";
import 'vuestic-ui/styles/essential.css'
import "vuestic-ui/css";
import "material-design-icons-iconfont/dist/material-design-icons.min.css";
import store from '@/plugins/store.js'


const app = createApp(App)
app.use(router)
app.use(store)
app.use(createVuestic({
          config: {
            colors: {
              presets: {
                light: {
                  buttons: '#1687ff',
                }
              }
            },
            icons: createIconsConfig({
              aliases: [
                {
                  name: "bell",
                  color: "#FFD43A",
                  to: "fa4-bell",
                },
                {
                  name: "ru",
                  to: "flag-icon-ru small",
                },
              ],
              fonts: [
                {
                  name: "fa4-{iconName}",
                  resolve: ({ iconName }) => ({ class: `fa fa-${iconName}` }),
                },
                {
                  name: "flag-icon-{countryCode} {flagSize}",
                  resolve: ({ countryCode, flagSize }) => ({
                    class: `flag-icon flag-icon-${countryCode} flag-icon-${flagSize}`,
                  }),
                },
              ],
            }),

          },
        })
)
app.mount("#app");