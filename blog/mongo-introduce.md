#前言#

2016年伊始，开始研究NoSql.看了couchdb,cloudant,cassandra,redis.却一直没有看过排行榜第一的mongo，实属不该。近期会花时间研究下mongo。本文是初识mongo的体验。有错误之处，还望指正。


#Mongo VS cassandra#
目前NoSql 排行版前三是Mongo,Cassandra,Redis.Redis 特点明确，key-value 形式，数据不持久化主要用来做缓存。所以这里主要比较Mongo,Cassandra

1. license 
mongo 是AGPLV3,mongo driver 才是apache.AGPLV3不是完全开源的，
cassandra 是apache,无压力

2. 性能
现在这世道，是个数据库都说自己是高性能。个人使用情况来看。cassandra写入性能要好很多，原因在于他只写commit log 和memtable.线性扩展Cassandra也好很多。读操作没有特别区别。
但是不基于业务来谈性能没什么多大意义。因为cassandra为了高效存储数据，对query的支持不是很友好，所以通常为了满足query，你可能需要建立index,视图，或者新表。这些开销会影响到写性能。

3. 数据模型
mongo 支持复杂的数据模型，数据类型更加的丰富，还可以嵌套文档。
cassandra 相对要弱点。

4. 集群架构
mongo 的sharded 集群需要三种角色，query router,config server,data store replicat set.当有primary节点down掉，需要重新选举primary node.所以会有down time.另外集群角色多，部署起来相对麻烦，但个人觉得这不是什么多大问题，毕竟没有人需要经常去部署。
cassandra所有节点都一样，replication_factor 为3的，write_consistency 为quorum时，可支持1个node down。对于整个cluster来说，是zero down time.没有任何影响。

5. multi active-active 数据中心
mongo 有主从节点，只有主节点能够写。所以没法做到active-active.如果用户从一个数据中心到另外一个数据中心了，没法写到新的数据中心，必须要写到最初的数据中心。
cassandra 没问题，server端可以配置DC-Aware 模式，就可以根据server来路由到新的数据中心
6. 与关系型数据库对比
mongo 更像关系型数据库，虽然mongo是基于document，没有了SQL语句，而cassandra却有CQL.以及mongo 没有Schema，而cassandra却有。但是从设计schema，和使用情况来说。mongo不需要太多的思想转变，数据既可以是规范化也可以有反规范化。但是cassandra则完全反规范化。


# Mongo 物理架构#

## 复制集##
![这里写图片描述](https://docs.mongodb.com/manual/_images/replica-set-primary-with-two-secondaries.png)

一个主节点，两个secondary 节点。主节点写，secondary 节点可以分发读。三个节点的数据完全一样。

##sharded cluster ##
复制集这种不分片的mongo 架构满足不了数据量大的情况。

![这里写图片描述](https://docs.mongodb.com/manual/_images/sharded-cluster-production-architecture.png)

三种角色

 - query router
 查询路由，server 发请求到router，然后分发给shard
 - config servers
 存储集群的元数据和配置信息，不存储数据
 - shard
 存储分片数据

注意query router 是mongos instance,而config server 和 shard中节点都是mongod instance.两者不一样。mongos 启动的时候需要连接config server,并配置各个shard信息。

