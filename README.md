# ToolBoxPlugin

## 介绍
idea插件开发，工具箱
1. 图书库模块：搜索图书、阅读图书、图书收藏 v1.0 
2. 音乐功能：
    1. 扫码登录，未登录可以看到首页的推荐模块
## 技术
java、Gradle、Swing UI、

## 涉及
> v1.0  阅读图书
1. idea plugin 创建方式
2. idea plugin 创建窗口的方式 工厂/自定义执行器
3. idea plugin swing ui 布局 展示 监听
3. idea plugin 存储设置信息
> v2.0  音乐
1. idea plugin 创建窗口的方式 工厂

## 注意
>build.gradle文件：org.jetbrains.intellij与JDK与gradle版本需要一一对应
>
## 主要业务逻辑
### Music模块
1. 登录逻辑--- 仅支持扫码登录
* 进入music模块 如果用户已经登则直接加载登录页面信息，如果用户未登录可以看见发现推荐模块，用户头像展示为登录二维码
* 登录后可以查收藏列表和喜欢的歌曲，最近播放过的歌曲


https://binaryify.github.io/NeteaseCloudMusicApi/

http://cloud-music.pl-fe.cn/top/playlist?limit=10&order=hot