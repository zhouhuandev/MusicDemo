# Android音乐播放器的开发实例

## 介绍

该项目旨在引导喜爱 `Android` 开发爱好者入门教程实例，可以一步一步的跟着来完成属于自己的项目开发过程。

此项目为基于 `Java` 语言开发，使用 `RecyclerView` 多样式布局组件，`Rxjava2` 权限请求管理，与一些其他基础组件开发完成

实现上一曲、下一曲、开始/暂停、停止以及拖动进度条可以试试快进退正在播放的歌曲内容

博客地址：[点击我哦~~~ https://blog.csdn.net/youxun1312/article/details/80356060](https://blog.csdn.net/youxun1312/article/details/80356060)


## 基于 Java 语言版本
### 更新内容 v2.1.0 2020-11-20
- 1.整体架构进行重写重构。封装基础页面类，基础适配器等
- 2.使用最新的 `RecyclerView` 流式布局 + `RecyclerViewAdapter` 更灵活的进行控制渲染图层
- 3.对手机目录文件不再单一指向 Music 文件夹，全盘扫描手机路径含有 music 文件夹。例如，`music/qqmusic/kgmusci/cloudmusic`
- 4.支持播放音乐格式 `AAC`、`AMR`、`FLAC`、`MP3`、`MIDI`、`OGG`、`PCM`
- 5.音乐媒体播放封装 `MusicPlayerHelper` 帮助 `Android` 学习爱好者直接调用
- 6.增加扫描无数据时候展示无数据页面
- 7.增加可以刷新列表页面
- 8.增加可以点击列表进行播放当前歌曲功能
- 9.优化界面布局

## 版本说明
### 音乐播放器 v2.0.0 Java版本（老版本）
[音乐播放器 v2.0.0 Java版本 https://gitee.com/shandong_zhaotai_network_sd_zhaotai/MusicDemo/releases/v2.0.0](https://gitee.com/shandong_zhaotai_network_sd_zhaotai/MusicDemo/releases/v2.0.0)
### 音乐播放器 v2.1.0 Java版本 (新版本）
[音乐播放器 v2.1.0 Java版本 https://gitee.com/shandong_zhaotai_network_sd_zhaotai/MusicDemo/releases/v2.1.0](https://gitee.com/shandong_zhaotai_network_sd_zhaotai/MusicDemo/releases/v2.1.0)
### 音乐播放器 v1.0.0 Kotlin版本 （新版本）
[音乐播放器 v1.0.0 Kotlin版本 （新版本）https://gitee.com/shandong_zhaotai_network_sd_zhaotai/MusicDemo/releases/v1.0.0](https://gitee.com/shandong_zhaotai_network_sd_zhaotai/MusicDemo/releases/v1.0.0)
### 备注
热烈欢迎感兴趣的同学加入学习 `Android` 的队列当中

扫描进群方式

![](https://img-blog.csdnimg.cn/20190529211707548.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3lvdXh1bjEzMTI=,size_16,color_FFFFFF,t_70)

