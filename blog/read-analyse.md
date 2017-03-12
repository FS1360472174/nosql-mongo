# 分布式读 #
**读冲突**
分布式中数据库有多份数据，各份数据可能存在不一致性。
mongo 只会写到primary节点上，理论上来说不会有文档冲突，也就是说数据库中的数据都以primary节点为标准。

但是有种情况，一个主节点的数据还没有全部replicate 到secondary 节点，它down 了，这部分数据就有不一致性了，当它重新上线后变成了一个secondary 节点，就会有冲突了。需要将之前的这部分更新应用到cluster中。mongo 提供了rollback的功能来实现[https://docs.mongodb.com/manual/core/replica-set-rollbacks/#rollbacks-during-replica-set-failover](https://docs.mongodb.com/manual/core/replica-set-rollbacks/#rollbacks-during-replica-set-failover)

**并发控制**
多版本并发控制(MVCC)，不同的用户可以基于相同的document操作，支持并发读写，而不用锁，极大的提高了性能,MySQL,Oracle 等关系型数据库也有对此的支持。一般的做法就是在document级别多维护个version的字段。NoSQL cloudant 也支持MVCC(https://docs.cloudant.com/mvcc.html)，但是mongo目前并不支持，有个基于java实现的mongo 并发控制[mongo MVCC](https://github.com/igd-geo/mongomvcc)


# Read Concern #
mongo3.2 才引入read concern,需要在启动mongod时启动，--enableMajorityReadConcern，或者在配置文件中配置。

 - local,默认值。直接读取当前的MongoDB实例，但是可能会读到副本集中不一致的数据，甚至可能回滚。
 - majority策略读取那些已经被副本集大多数成员所认可的数据，因此数据不可能被回滚。

目前majority只被WiredTiger存储引擎所支持。

读发生回滚，这个地方可能有不理解，为什么读操作会有回滚呢。其实在上面已经提到过了，如果设置成local，不能保证读到的数据都已经被写入到replicate set的各个节点，有可能还只是在primary node上。primary node down重新上线后，就会发生roll back.

# Read Preference #
默认情况下，mongo从primary node读取数据，但是mongo secondary node不仅可以做数据的备份。同样也可以拿来读取，这样可以极大的提高读性能。可以在每个connection 层面配置读路由的节点：

```
Mongo.setReadPref(mode, tagSet)
```
- mode	string类型	有五种选择primary(默认值), primaryPreferred, secondary, secondaryPreferred, or nearest.

	primary:只从primary node读取
	primaryPreferred:先从primary node读取，但是如果secondary节点不可达，则查询primaryPreferred
	secondary:只从secondary读取
	secondaryPreferred: 先从secondary node上读取，如果不可达，则从primary node读取。
	nearest: 从最近的节点读取，在多个datacenter 可能会比较有用

- tagSet	array类型	.指定打了某tag的节点 不能用于primary 模式(不是指primary 节点)

```
db.getMongo().setReadPref('secondaryPreferred',
                          [ { "dc": "east", "use": "production" }]
```

如果选用了不是primary节点，必须要接受数据的不一致性，primary 节点的数据是异步复制到secondary 节点上，所以secondary 节点上的数据有可能不是最新的。

**总结**
mongo 读配置和写配置其实有点类似，都是需要在低延迟和高一致性做权衡，只是mongo中分primary和secondary两种节点，所以配置起来没有cassandra那么的灵活

 1. 低延迟， 应用能够忍受可能的过时数据。使用secondaryPreferred,local concern
 2. 一致性要求高 使用primary,majority
多数时候可能需要读写一起考虑，关于写可以考虑这篇文章[mongo 写分析](http://blog.csdn.net/fs1360472174/article/details/53365445)

