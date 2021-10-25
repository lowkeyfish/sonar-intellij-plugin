<p align="center">
<img width="100" src="https://note.yujunyang.com/static/2021/8/057b00d5f6f8f467f89293e3dfa246b6.png">
</p>

<h1 align="center">Sonar Intellij Plugin</h1>

<p>
<a href="https://plugins.jetbrains.com/plugin/17542-sonaranalyzer"><img src="https://img.shields.io/jetbrains/plugin/v/17542"/></a>
<a href="https://plugins.jetbrains.com/plugin/17542-sonaranalyzer"><img src="https://img.shields.io/jetbrains/plugin/d/17542"/></a>
<img src="https://img.shields.io/github/license/lowkeyfish/sonar-intellij-plugin"/>
</p>

[English](README.md) | 简体中文

Sonar Intellij Plugin是一个Intellij IDEA插件，使用Sonar Intellij Plugin你可以在Intellij IDEA中对Java项目执行Sonar代码分析、查看代码分析报告以及对问题代码进行提示。
你必须要做的配置只有绑定SonarQube，然后即可在不离开IDE的情况下使用所有功能。


## 安装

Sonar Intellij Plugin已经发布到JetBrains插件市场，最新版本是[0.1.5](https://plugins.jetbrains.com/plugin/17542-sonaranalyzer)。

打开`Settings` / `Plugins` / `Marketplace`（macOS下为`Preferences` / `Plugins` / `Marketplace`），使用关键词`SonarAnalyzer`搜索插件然后安装插件`SonarAnalyzer`：

![sonar-intellij-plugin-install](https://note.yujunyang.com/static/2021/8/6d863e1d7b882d586f695b149c83671c.png)

插件安装成功后重启IDE即可生效。

## 用法

Sonar Intellij Plugin底层使用[SonarScanner](https://docs.sonarqube.org/latest/analysis/scan/sonarscanner/)执行代码分析，SonarScanner需要连接SonarQube服务器，因此使用Sonar Intellij Plugin前需要做一些必要的设置，然后再对项目执行代码分析。

### 设置

设置分为IDE级别和Project级别，可以通过`Settings` / `Tools` / `SonarAnalyzer`（macOS下为`Preferences` / `Tools` / `SonarAnalyzer`）对插件进行设置。

IDE级别设置主要包含：

* 插件语言切换（目前支持中文、英文）
* 管理SonarQube连接（新增、更新、删除）
* 管理全局SonarScanner属性（新增、更新、删除）

Project级别设置主要包含：

* 为Project绑定SonarQube连接
* 管理Project使用的SonarScanner属性（新增、更新、删除，以及控制是否继承全局SonarScanner属性）

#### SonarQube连接

可以在IDE级别管理所有的SonarQube连接，可以新增、删除和更新SonarQube连接。

每个SonarQube连接必须提供名称、URL和Token。新增SonarQube连接：

![sonar-intellij-plugin-chinese-add-connection](https://note.yujunyang.com/static/2021/8/8c8571056a7f297f9a5d816f657e84f4.png)

在Project级别设置中可以为Project指定要绑定的SonarQube连接：

![sonar-intellij-plugin-chinese-project-settings](https://note.yujunyang.com/static/2021/8/08a91380e7f634041369872adf1c88e4.png?t=1)

如果Project未绑定SonarQube连接，默认使用已添加的第一个SonarQube连接。


#### SonarScanner属性

插件内部在使用SonarScanner时已经尽量使用了合理完善的属性，但是你可能仍需要独立设置一些属性，目前可以设置两个属性：`sonar.exclusions`和`sonar.cpd.exclusions`。

添加属性：

![sonar-intellij-plugin-chinese-add-property](https://note.yujunyang.com/static/2021/8/03b5135f1cd20e4d4711a8dbd7294e09.png)

你可以在IDE级别和Project级别管理属性，在IDE级别添加所有Project都需要的属性，然后可以在Project设置中控制是否继承IDE级别添加的属性，以及管理当前Project的专用属性：

![sonar-intellij-plugin-chinese-project-settings](https://note.yujunyang.com/static/2021/8/08a91380e7f634041369872adf1c88e4.png?t=1)

### 分析

#### 启动分析

在对插件完成设置后，你可以通过以下位置触发项目代码分析：

* Project Tool Window中内容右键菜单中的`SonarAnalyzer`菜单
* 代码编辑器右键菜单中的`SonarAnalyzer`菜单
* SonarAnalyzer Tool Window中的开始按钮

开始代码分析后，插件将启动代码编译，编译成功后再使用SonarScanner完成代码分析。在SonarAnalyzer Tool Window的`日志`中会输出整个操作日志：

![sonar-intellij-plugin-chinese-log](https://note.yujunyang.com/static/2021/8/3bf7ca60ceb9fdc6d707bd85ef65808e.png)


#### 分析结果

当代码分析完成后会在SonarAnalyzer Tool Window的`报告`中展示完整的分析报告，同时也会在编辑器中对问题代码行进行提示：

![sonar-intellij-plugin-chinese-report](https://note.yujunyang.com/static/2021/8/1d028ec39075b3abfd3d19faf707d8da.png)


## 如何贡献

非常欢迎你的加入！提一个 [Issue](https://github.com/lowkeyfish/sonar-intellij-plugin/issues/new) 或者提交一个 Pull Request。

## 使用许可

GPL-3.0 &copy; Yu Junyang









