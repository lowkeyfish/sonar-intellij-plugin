<p style="text-align:center;">
	<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512" style="width:100px;height:100px;">
    <path stroke="null" id="svg_1" d="m403.52687,177.1871l-9.88005,-8.93755c-1.64938,-2.16939 -3.83502,-3.25002 -6.58941,-3.25002s-4.94003,1.08063 -6.58941,3.25002l-231.40931,227.50117l-81.53479,-80.43791c-1.64938,-2.16939 -3.83502,-3.25002 -6.58941,-3.25002s-4.94003,1.08063 -6.58941,3.25002l-9.88005,8.93755c-1.64938,2.16939 -2.47001,4.6069 -2.47001,7.31254s0.82063,4.87503 2.47001,6.50003l98.00425,96.688c2.18564,2.16939 4.52565,3.25002 7.00379,3.25002s4.81002,-1.08063 7.00379,-3.25002l247.05814,-243.75125c1.64938,-1.62501 2.47001,-3.79439 2.47001,-6.50003s-0.82875,-5.14315 -2.47814,-7.31254zm-258.2057,101.5874c2.15314,2.15314 4.45252,3.22564 6.88191,3.22564s4.72877,-1.07251 6.88191,-3.22564l179.48217,-178.93779c1.61688,-1.62501 2.42939,-3.77814 2.42939,-6.46753s-0.8125,-5.11065 -2.42939,-7.26379l-9.71755,-8.88067c-1.61688,-2.15314 -3.77002,-3.22564 -6.47566,-3.22564s-4.85877,1.07251 -6.47566,3.22564l-164.10147,162.80146l-58.70343,-58.52468c-1.61688,-2.15314 -3.77002,-3.22564 -6.47566,-3.22564s-4.85877,1.07251 -6.47566,3.22564l-9.71755,8.88067c-1.61688,2.15314 -2.42939,4.5744 -2.42939,7.26379s0.8125,4.84252 2.42939,6.45941l74.89663,74.66913z" fill="green"/>
</svg>
</p>

<h1 style="text-align:center;">Sonar Intellij Plugin</h1>

<p style="text-align:center;">
<img src="https://img.shields.io/jetbrains/plugin/v/1234"/>
<img src="https://img.shields.io/github/license/lowkeyfish/sonar-intellij-plugin"/>
</p>

[English](README.md) | 简体中文

Sonar Intellij Plugin是一个Intellij IDEA插件，使用Sonar Intellij Plugin你可以在Intellij IDEA中对Java项目执行Sonar代码分析、查看代码分析报告以及对问题代码进行提示。



## 安装

Sonar Intellij Plugin已经发布到JetBrains插件市场，最新版本是[0.1.0]()。

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

![sonar-intellij-plugin-中文-添加连接](https://note.yujunyang.com/static/2021/8/737a08525f1aa64ac24633da9e4ea2a6.png)

在Project级别设置中可以为Project指定要绑定的SonarQube连接：

![sonar-intellij-plugin-中文-project绑定连接](https://note.yujunyang.com/static/2021/8/b7ebf0765e7b7ac96260329327895021.png)

如果Project未绑定SonarQube连接，默认使用已添加的第一个SonarQube连接。


#### SonarScanner属性

插件内部在使用SonarScanner时已经尽量使用了合理完善的属性，但是你可能仍需要独立设置一些属性，目前可以设置两个属性：`sonar.exclusions`和`sonar.cpd.exclusions`。

添加属性：

![sonar-intellij-plugin-中文-添加属性](https://note.yujunyang.com/static/2021/8/ffee76b3d64701e96dbc2ec52811c439.png)

你可以在IDE级别和Project级别管理属性，在IDE级别添加所有Project都需要的属性，然后可以在Project设置中控制是否继承IDE级别添加的属性，以及管理当前Project的专用属性：

![sonar-intellij-plugin-中文-project绑定连接](https://note.yujunyang.com/static/2021/8/b7ebf0765e7b7ac96260329327895021.png)

### 分析

#### 启动分析

在对插件完成设置后，你可以通过以下位置触发项目代码分析：

* Project Tool Window中内容右键菜单中的`SonarAnalyzer`菜单
* 代码编辑器右键菜单中的`SonarAnalyzer`菜单
* SonarAnalyzer Tool Window中的开始按钮

开始代码分析后，插件将启动代码编译，编译成功后再使用SonarScanner完成代码分析。在SonarAnalyzer Tool Window的`日志`中会输出整个操作日志：

![sonar-intellij-plugin-中文-log](https://note.yujunyang.com/static/2021/8/e5c3b801d1b894359357e79768293006.png)


#### 分析结果

当代码分析完成后会在SonarAnalyzer Tool Window的`报告`中展示完整的分析报告，同时也会在编辑器中对问题代码行进行提示：

![sonar-intellij-plugin-中文-report](https://note.yujunyang.com/static/2021/8/06c4023eef64a4f9a0fabc4005715f94.png)



## 如何贡献

非常欢迎你的加入！提一个 [Issue](https://github.com/lowkeyfish/sonar-intellij-plugin/issues/new) 或者提交一个 Pull Request。

## 使用许可

GPL-3.0 &copy; Yu Junyang









