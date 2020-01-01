# ICat

Recording to Tomcat-9.0.27...

This is one wheel I made. The name of a Container is the path of the uri, which can route to one ```Wrapper```.
 
Recording to ```ServerConfigurationUtil``` and ```CatProperties```, uri *http://www.logan.org:8007/app1/welcome*, *http://www.logan.org:8007/app1/index* represent welcome/index named Wrapper, app1 named Context, and www.logan.org named Host. 

Before you start ICat and visit these two uri, add

> 127.0.0.1 www.logan.org (using Windows, modify C:\Windows\System32\drivers\etc\hosts)

> 127.0.0.1 www.logan.org (using Linux, modify /etc/hosts)

Cause something are thought as 'physical work' by me, there're following differences.

- There's no *XML* and *WAR* parser, one class named ```ServerConfigurationUtil``` will mock a runnable ```Server```;
- ```servletContent@Wrapper``` instead of ```service@Servlet``` will provide html;
- ```deployApps@HostConfigListener``` is an empty method.

There're notes when I reading Tomcat-9.0.27 source code written in Chinese [here](https://www.cnblogs.com/Logan12138/).
 
