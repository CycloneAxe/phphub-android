
## PHPHub related projects

You can checkout the others open source projects of PHPHub in the following list.

* [PHPHub-iOS](https://github.com/Aufree/phphub-ios) by [@Aufree](https://github.com/Aufree)
* [PHPHub-Server](https://github.com/NauxLiu/phphub-server) by [@NauxLiu](https://github.com/NauxLiu)
* [PHPHub-Android](https://github.com/CycloneAxe/phphub-android) by [@Kelvin](https://github.com/CycloneAxe) and [@Xiaoxiaoyu](https://github.com/xiaoxiaoyu)
* [PHPHub-UI](https://github.com/phphub/phphub-ui) by [@Summer](https://github.com/phphub/phphub-ui) and [@Aufree](https://github.com/aufree)
* [PHPHub-Web](https://github.com/summerblue/phphub) by [@Summer](https://github.com/phphub/phphub-ui)

## 项目介绍

此项目为 [PHPHub](https://phphub.org/) Android 客户端

项目架构采用 MVP 模式

[PHPHub Android APK 下载](http://fir.im/phphub)

![](https://cloud.githubusercontent.com/assets/324764/10683969/448b0cda-797a-11e5-8f93-3e51ad7144df.png)

## 运行环境
Min SDK verison 4.0+

Android Studio version 1.3.2

Gradle version 2.4

## 安装方式
1、在指定的目录下执行

> git clone https://github.com/CycloneAxe/phphub-android.git

2、复制 gradle.properties.example 为 gradle.properties 并编辑里面的相关信息

3、将项目导入 Android Studio 运行即可

## 功能简述

* 渠道包构建
* 使用 [nucleus](https://github.com/konmik/nucleus) 简化 MVP 架构
* 使用 [RxJava](https://github.com/ReactiveX/RxJava) 处理 API 请求返回数据
* 使用 [SmartTabLayout](https://github.com/ogaclejapan/SmartTabLayout) 实现带 Icon 的选项卡切换
* 消息提醒
* 使用 [Account Manager](http://developer.android.com/reference/android/accounts/AccountManager.html) 存储登陆用户信息
* WebView 图片点击事件注入
* 二维码扫描登陆
* Deep Link 实现

## 项目依赖

项目名称 | 项目信息
------- | -------
[android.support.*](https://developer.android.com/tools/support-library/index.html) | Android Support Library
[Dagger 2](https://github.com/google/dagger) | Android 依赖注入组件
[retrofit](https://github.com/square/retrofit) | 网络请求组件
[retroauth](https://github.com/Unic8/retroauth) | 基于 Retrofit, 用于简化处理账户认证请求
[butterknife](https://github.com/JakeWharton/butterknife) | View注入框架
[icepick](https://github.com/frankiesardo/icepick) | 一个通过注解来方便我们保存和恢复 Android Instance 状态的 Library
[fresco](https://github.com/facebook/fresco) | Facebook 推出的 Android 图片加载库
[SmartTabLayout](https://github.com/ogaclejapan/SmartTabLayout) | 带有滑动反馈动效的 Tab 指示器
[smart-adapters](https://github.com/mrmans0n/smart-adapters) | 简化 ListView / RecyclerView Adapter 的库
[MultiStateView](https://github.com/Kennyc1012/MultiStateView) | 根据状态显示不同 content 的 Android 视图
[BGABadgeView-Android](https://github.com/bingoogolapple/BGABadgeView-Android) | Android 徽章控件
[RxJava](https://github.com/ReactiveX/RxJava) | RxJava 是由 Netflix 开发的响应式扩展（Reactive Extensions）的Java实现
[RxAndroid](https://github.com/ReactiveX/RxAndroid) | RxAndroid 是 RxJava 的一个针对 Android 平台的扩展
[logger](https://github.com/orhanobut/logger) | 一个简单、漂亮、功能强大的 Android 日志程序
[nucleus](https://github.com/konmik/nucleus) | 简化 MVP 的库
[Android-MaterialRefreshLayout](https://github.com/android-cjj/Android-MaterialRefreshLayout) | 下拉刷新控件
[LeakCanary](https://github.com/square/leakcanary) | Android 内存泄漏检测工具
[android-percent-support-extend](https://github.com/hongyangAndroid/android-percent-support-extend) | Google 百分比布局库的扩展
[Barcode Scanner](https://github.com/dm77/barcodescanner) | 二维码扫描
[prettytime](https://github.com/ocpsoft/prettytime) | 时间格式化
[prefser](https://github.com/pwittchen/prefser) | Shared Preferences Helper
[Android-TopScrollHelper](https://github.com/kmshack/Android-TopScrollHelper) | 点击系统栏自动滚动到顶部
[sweet-alert-dialog](https://github.com/pedant/sweet-alert-dialog) | A beautiful and clever alert dialog
[android-saripaar](https://github.com/ragunathjawahar/android-saripaar) | 表单验证
[DeepLinkDispatch](https://github.com/airbnb/DeepLinkDispatch) | 一个简单的、基于注解的 Deep Link 处理库
[otto](https://github.com/square/otto) | Event Bus, 用来简化应用组件之间的通信
[android-iconify](https://github.com/JoanZapata/android-iconify) | 一个将 Aweome ICON 和 Android 结合起来的项目
[Localify](https://github.com/polok/localify) | 从文件中加载内容的Android类库
[PhotoDraweeView](https://github.com/ongakuer/PhotoDraweeView) | 基于 Fresco 的图片缩放控件

## 参与贡献
[晓晓鱼](https://github.com/xiaoxiaoyu)

[刘相轩](https://github.com/NauxLiu)

###License
<pre>
Copyright 2015 EST Group

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>
