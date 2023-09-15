# ToolBoxPlugin

## 介绍
idea插件开发，工具箱
1. 图书模块：搜索图书、阅读图书、图书收藏 v1.0 
2. 音乐功能：[暂停]
    1. 扫码登录，未登录可以看到首页的推荐模块
## 技术
java、Gradle、Swing UI、

## 图书模块



## Music模块[关闭]
1. 登录逻辑--- 仅支持扫码登录
* 进入music模块 如果用户已经登则直接加载登录页面信息，如果用户未登录可以看见发现推荐模块，用户头像展示为登录二维码
* 登录后可以查收藏列表和喜欢的歌曲，最近播放过的歌曲

> https://binaryify.github.io/NeteaseCloudMusicApi/
> http://cloud-music.pl-fe.cn/top/playlist?limit=10&order=hot
> **由于对网易云接口返回字段理解，和swing布局困难，音乐模块暂停**
