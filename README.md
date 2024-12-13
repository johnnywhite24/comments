# comments
windows系统文件夹备注脚本

通过修改`desktop.ini`实现windows系统资源管理器（我的电脑）文件夹备注功能。

介绍：
--

直接使用请前往 [发布页面](https://github.com/johnnywhite24/comments/releases/tag/1.0.1) 下载并查看教程。

<br>

因为在项目开发过程中，会产生大量的文件夹目录来存储项目相关文件或代码，尤其对于项目小而多的场景下，再加上个人小项目或demo，
并且因为大部分项目都是用简短且通用的英文命名，所以如果不对他们进行精心整理的话，一时之间很难分辨各个目录的作用。

于是想到如果可以为文件夹或文件做备注或说明，那就非常清楚了。

最先想到的方案是：通过windows提供的接口实现，但是本人不善windows端开发，又涉及注册表等，成本太高。

继而通过观察.mp3格式文件，自带作者等属性，又在资源管理器扩展列中找到备注等属性，于是想到修改文件夹meta属性来实现该功能。

但是通过调研发现没法向windows文件夹设置备注属性。

最终由文件夹图标找到desktop.ini方案。


但是过程并不顺利。

desktop.ini 需要是HSA属性的，即（hidden，system）。为了支持中文需要ANSI编码，换行符需要使用 CR LF。

目标文件夹需要设置 system 属性。

使用java代码设置 HSA 不会使资源管理器刷新，还需使用 attrib 命令设置 HSA。

目前达到的效果及仍存在的问题
--

目前可以通过 comment 命令实现对目标文件夹的备注，通过鼠标悬浮在文件夹上可以看到备注内容，或者增加资源管理器备注列看到。

但是通过comment设置备注后，没法立刻看到效果，需要等待或刷新多次才能看到。

目前对于资源管理器重新加载desktop.ini配置的时机仍不确定。但大部分时候，刷新3-5次即可看到备注效果。

另一个问题是，二次修改备注时，因为desktop.ini已被设置为 HS （hidden,system），所以读写时会拒绝访问，
于是需要先将 desktop.ini 文件删除 HS 属性，但因为是命令行执行，所以是异步的，这里暂时还没做异步处理，
只偷懒使用了 Thread.sleep 。

使用
--

如果只简单使用的话，是够用的。可以通过 [发布页面](https://github.com/johnnywhite24/comments/releases) 页面直接下载编译后的脚本。

java程序接收两个参数：[目标目录，备注文本]

bat脚本接收一个参数：[备注文本字符串]

执行过程：bat脚本，接收[字符串]后，在将[当前目录]和[字符串]传给java处理。

```
$ C:\targetDir> comment "这是备注内容"
```

最后
--
希望大家有更好的解决方案或优化内容提出，欢迎您的加入与使用。