
# 写操作 #

**复制集**

mongo所有的节点都是写入到primary节点，同时写入oplog,secondary 节点会持续的从primary节点上复制oplog的信息，然后根据oplog写数据。secondary 节点的复制oplog，写数据是一个异步的过程.

oplog 是一个capped collection（固定大小的集合，超过最大值后自动覆盖老的值），记录对数据库的更改操作。oplog大小默认是disk的5%，可以根据你的应用去调节

**sharding 环境**

sharding 集群环境，由mongos来分发，通过config server 保存的信息，将数据写到对应的shard上 

# write concern #

write concern 其实就是写操作的一致性级别，对于cassandra，每个节点都是一样的，所以一致性等级就是设置写多少个节点成功。而mongo就比较复杂一点

    {w： <value>,j: <boolean>,wtimeout: <number>}

- w 表示写应答
   
    w 可以为数字，代表的是收到写入成功的mongod 数目。w也可以是"majority",表示收到了大多数投票节点的应答。还可以是<tag>表示收到了被标记了某tag的复制集的某个节点应答。
   

- j是写操作是否被写入到journal

     3.2版本，j:true 要求w中设置的节点都写入到了journal.以前版本中，只要求复制集中的primary写入到了journal接可以，而不管w设置的值。


- wtimeout:timeout时间，避免写入时间过长，block住了其他操作

write concern具体设置成多少，由应用程序决定，应答节点设置的越多，写性能越差，数据的持久化越好。相反设置的越少，写性能越好，数据的安全性越差。

**journal**

为什么有journal这个，一般DB中写操作都是先写到内存中，然后flush到磁盘，这样效率比较高。但是这样有个风险，就是当内存中的数据还没有flush到磁盘中的时候，节点down掉或者数据库实例down，就会有造成这部分数据的丢失。对于cassandra，有commit log 来实现recover。对于mongo,就是journal.

以WiredTiger 存储引擎为例，WiredTiger 创建checkpoints，以60s间隔或者2G的journal data.如果在这期间节点down掉，内存数据丢失，就需要依靠journal file来实现数据的回复。journal数据包括一个写操作的所有影响，同样存储在内存中，每隔50ms flush到磁盘中。

journal记录没有到128KB，都会存储在缓冲区，journal 缓冲区每隔50ms flush到磁盘，也就是说数据还是有丢失的可能。当写操作concern加了true,可以确保数据被写入到journal file

**journal vs oplog**

oplog 已经记录了操作日志，为啥不可以作为数据恢复呢，cassandra中就是用commit log 写操作记录，和数据恢复。

在mongo中，oplog是比journal更高level的，文件大小要大，记录的对数据库的更改操作，用于primary,secondary数据之间的同步。
journal 文件最大100MB，主要用来数据恢复，记录的操作更加详细。比如update数据，不仅记录update什么数据，还记录对index的影响。

# 总结 #
说了这么多，对于具体的应用应该如何设置呢。
1. 不应该是为整个应用设置同一个write，需要给write 操作分别设置。
2. 应用中的操作基本可以分为两类
       -  低延迟， 写性能高，可以忍受脏读。w= 1
       -  数据一致性,不能忍受脏读,设置w = majority，牺牲部分性能

# 参考 #
http://stackoverflow.com/questions/8970739/how-do-the-mongodb-journal-file-and-oplog-differ

https://docs.mongodb.com/manual/reference/write-concern/
