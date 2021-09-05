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

English | [简体中文](README.zh-CN.md)

Sonar Intellij Plugin is an Intellij IDEA plugin, you can use Sonar Intellij Plugin in Intellij IDEA to perform Sonar code analysis on Java projects, view code analysis reports, and prompt problem codes. 


## Install

Sonar Intellij Plugin has been released to the JetBrains plugin marketplace, the latest version is [0.1.0](). 

## Usage

Sonar Intellij Plugin uses [SonarScanner](https://docs.sonarqube.org/latest/analysis/scan/sonarscanner/) to perform code analysis. SonarScanner needs to connect to the SonarQube server. Therefore, you need to make some necessary settings before using Sonar Intellij Plugin, and then perform code analysis on the project.

### Settings

The plugin can be set at the IDE level and the Project level. Go to `Settings` / `Tools` / `SonarAnalyzer` (under macOS is `Preferences` / `Tools` / `SonarAnalyzer`) to set the plugin.

IDE level settings include:

* Plugin language switch (currently supports Chinese and English) 
* Manage SonarQube connections (add, update, delete) 
* Manage global SonarScanner properties (add, update, delete) 

Project level settings include:

* Bind SonarQube connection for Project
* Manage the SonarScanner properties used by the current Project (add, update, delete, and control whether to inherit the global SonarScanner properties) 

#### SonarQube Connection

All SonarQube connections can be managed at the IDE level, and SonarQube connections can be added, deleted and updated. 

Each SonarQube connection must provide a name, URL, and Token. Add SonarQube connection: 

![sonar-intellij-plugin-english-add-connection](https://note.yujunyang.com/static/2021/8/2e1e77802c8925e250e9d0a7c7406589.png)

In the Project level settings, you can specify the SonarQube connection to be bound for the Project: 

![sonar-intellij-plugin-english-project-settings](https://note.yujunyang.com/static/2021/8/e3e02251866b115afe7596341e246e36.png)

If the Project does not specify a SonarQube connection, the first SonarQube connection that has been added is used by default. 


#### SonarScanner Property

When using SonarScanner inside the plugin, reasonable and complete properties have been used as much as possible, but you may still need to set some properties independently. Currently, you can set two properties: `sonar.exclusions` and `sonar.cpd.exclusions`. 

Add property:

![sonar-intellij-plugin-english-add-property](https://note.yujunyang.com/static/2021/8/b591ba5833163f289cd8be4ac2b4bc4a.png)

You can manage properties at the IDE level and the Project level, add the properties required by all projects at the IDE level, and then you can set whether to inherit the properties added at the IDE level in the Project settings, and manage the special properties of the current Project: 

![sonar-intellij-plugin-english-project-settings](https://note.yujunyang.com/static/2021/8/e3e02251866b115afe7596341e246e36.png)

### Analysis

#### Start Anslysis

After setting the plugin, you can trigger the project code analysis in the following places: 

* The `SonarAnalyzer` menu in the context menu of the content in the Project Tool Window
* The `SonarAnalyzer` menu in the context menu of the code editor 
* The start action button in the SonarAnalyzer Tool Window

After starting the code analysis, the plugin will start the code compilation, and then use SonarScanner to complete the code analysis after the compilation is successful. The entire operation log will be output in the `Log` of the SonarAnalyzer Tool Window: 

![sonar-intellij-plugin-english-log](https://note.yujunyang.com/static/2021/8/3d0146c5de34ea58cb498211ddc6aace.png)

#### Analysis Result

When the code analysis is completed, the complete analysis report will be displayed in the `Report` of the SonarAnalyzer Tool Window, and the problem code line will also be prompted in the editor: 

![sonar-intellij-plugin-english-report](https://note.yujunyang.com/static/2021/8/10bfa152ae16d038d8cdeefbcba57df6.png)


## Contributing

Feel free to dive in! Open an [issue](https://github.com/lowkeyfish/sonar-intellij-plugin/issues/new) or submit PRs.

## License

GPL-3.0 &copy; Yu Junyang











