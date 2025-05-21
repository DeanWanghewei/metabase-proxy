
# 这是什么
这是一个IntelliJ IDEA插件，可以在IntelliJ IDEA中查询 metabase 库
# 如何构建
1. 手动点击 gradle -> Tasks -> Intellij platform -> buildPlugin
# 在哪里找插件包
build/distributions/

# 如何使用
1. 先在setting中 Other settings -> Metabase Proxy Settings 填写 user,password,url
2. 点击登陆.
3. 选中要查询的SQL.右键点击 Run Metabase Query.
4. 选择要查询的库,执行查询

⚠️  注意: 插件可以记录上次的查询库,下次查询时,会自动填充上次查询的库.
插件可以记录上次配置的参数,下次查询时,会自动填充上次配置的参数.
