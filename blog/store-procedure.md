
#摘要#

本文主要介绍mongo存储过程，mongo 存储过程其实就是JS方法，然后通过eval 方法来执行，但是这个方法在3.0 depreate了，也就是在未来的版本，这个功能可能不提供了。从目前的jira的issue来看，mongo官方还没有打算提供了eval的替代。

#介绍#
mongo 存储过程其实就是在database 端存储js 方法

    db.system.js.save({
		_id:"add", 
        value:function(x, y){ 
           return x + y; 
         }
    });

然后通过db.system.js.find() 查看是否被成功加入

通过eval方法来执行存储过程

	db.eval('add(2,3)')

#问题#
[http://bbs.csdn.net/topics/392041499](http://bbs.csdn.net/topics/392041499)

上面这位网友提出来的问题是比较典型的，很容易遇到的

    WARNING: db.eval is deprecated
	Error: {
	"ok" : 0,
	"errmsg" : "ReferenceError: z_add is not defined :\n_funcs1@:1:24\n",
	"code" : 139
	} :

这个问题产生的原因是因为存入了没有带id的js方法，可以通过db.system.js.find()来查看。使用db.system.js.remove()掉不带id的方法即可。

#sharded collection#

eval()方法不支持在sharded collection，可能原因在于

1. eval()方法在sharded 架构之前出来
2. 在sharded cluster 架构中，将逻辑放在DB意义不大，同样需要在各个shard上进行查询

#利弊分析#
**利**

将应用逻辑放在DB端，可以集成query,update,减少application 到db 的连接请求

**弊**

1. 一致性，由于不支持sharded collection,如果开始在非shard上这么做，那么后面需要sharding了，没法向后兼容
2. 性能，eval方法会有全局lock,影响性能
3. 安全角度，eval 可能会导致sql 注入问题。同时需要admin用户

综上所述，不建议使用stored procedure.有case需求的可以关注下这个issue

[https://jira.mongodb.org/browse/SERVER-20510](https://jira.mongodb.org/browse/SERVER-20510)
#参考#

http://pointbeing.net/weblog/2010/08/getting-started-with-stored-procedures-in-mongodb.html

https://docs.mongodb.com/manual/tutorial/store-javascript-function-on-server/

http://stackoverflow.com/questions/2284513/mongodb-err-name-has-to-be-a-string-with-where