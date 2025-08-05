plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij.platform") version "2.3.0"
}

group = "org.wei"
version = "1.9"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        create("IC", "2024.2.5")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
        // 添加 okhttp 依赖
        implementation("com.squareup.okhttp3:okhttp:4.11.0")
        // 添加 gson 依赖
        implementation("com.google.code.gson:gson:2.10.1")
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "242"
            untilBuild = ""
        }
        changeNotes = """
        <h2>1.8</h2>
        <ul>
            <li>优化表格展示</li>
        </ul>
        <h2>1.7</h2>
        <ul>
            <li>设置中添加了查询结果展示为表格结构</li>
        </ul>
        <h2>1.6</h2>
        <ul>
            <li>优化登录后无法logout 问题</li>
        </ul>
        <h2>1.5</h2>
        <ul>
            <li>修改配置项保存为项目级别</li>
        </ul>
        <h2>1.4</h2>
        <ul>
            <li>查询历史可通过tab 页手动关闭.新的查询会放到第一个tab</li>
        </ul>
        <h2>1.3</h2>
        <ul>
            <li>修复查询报错问题</li>
        </ul>
        <h2>1.2</h2>
        <ul>
            <li>主动检测查询SQL中是否有limit ,没有limit则主动添加 limit 100</li>
        </ul>
        <h2>1.1</h2>
        <ul>
            <li>添加异步查询</li>
        </ul>
        <h2>1.0</h2>
        <ul>
            <li>添加核心查询功能.登陆功能</li>
        </ul>
    """.trimIndent()
        description = """
    metabase 的idea 本地代理查询插件.<br>
    可以通过本地登陆metabase 后直接查询数据.<br>
<br>
    📦 Metabase Proxy 插件 <br>
      使用方法: <br>
      1. 在setting中填写Metabase地址和用户和地址 <br>
      2. 选中要执行的SQL.右键执行选择 Run Metabase Query 执行选中的SQL <br>
      3. 选中要执行的SQL.使用快捷键执行. <br>
      4. 插件会自动保存参数.下次执行会自动使用上次的参数. <br>
<br>
      ⚠️ 注意: 插件查询是同步执行的.所以请不要在SQL中执行耗时操作.!!! <br>
    """.trimIndent()
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "21"
    }
}
