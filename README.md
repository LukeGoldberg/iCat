# ICat

Recording to Tomcat-9.0.27(current is 9.0.34)...

> This is a servlet container, support api deployment as one embedded way but do not support *WAR* deployment method(as it needs lots of parse work).

> I obey Tomcat's naming method, main component classes can be found in this repository. And the main procedure is similar with Tomcat(with some processing branch discarded).

> Custom servlet needed to be add to iCat by hand(like one DispatcherServlet), usage described following.
 
I use one class named ```ServerConfigurationUtil``` to supply an runnable ```Server```, uri *http://www.logan.org:8007/app1/welcome*, *http://www.logan.org:8007/app1/index* can represent welcome/index named Wrapper, app1 named Context, and www.logan.org named Host. 

Before start iCat(run CatApplication.class) and visit these two uri, add

> 127.0.0.1 www.logan.org (using Windows, modify C:\Windows\System32\drivers\etc\hosts)

> 127.0.0.1 www.logan.org (using Linux, modify /etc/hosts)

and there're notes when I reading Tomcat-9.0.27 source code written in Chinese [here](https://www.cnblogs.com/Logan12138/).
 
