
#摘要#
在[MongoDB 初识](http://blog.csdn.net/fs1360472174/article/details/52901780)篇中谈到过Mongo 与 Cassandra的区别，这边再谈谈Mongo与Cassandra的存储引擎差别

#概括#

存储引擎：

| 类型 | 功能 | 应用 |
| ------------- |-------------| -----|
| hash | 增删改、随机读、~~顺序扫描~~ | Key-Value存储系统 |
| B-Tree | 增删改、随机读、顺序扫描 | 关系型数据库 |
| LSM | 增删改、随机读、顺序扫描 | 分布式存储系统，如cassandra、google LevelDB |


###B-Tree###

**缓存管理**

缓存管理的核心在于置换算法，置换算法常见的有FIFO（First In First Out），LRU(Least Recently Used)。关系型数据库在LRU的基础上，进行了改进，主要使用LIRS(Low Inter-reference Recency Set)
将缓存分为两级，第一次采用LRU，最近被使用到的数据会进第一级，如果数据在较短时间内被访问了两次或以上，则成为热点数据，进入第二级。避免了进行全表扫描的时候，可能会将缓存中的大量热点数据替换掉。



### LSM ###

Log-Structured Merge Tree:结构化合并树，核心思想就是不将数据立即从内存中写入到磁盘，而是先保存在内存中，积累了一定量后再刷到磁盘中

### LSM VS B-Tree ###

LSM在B-Tree的基础上为了获取更好的写性能而牺牲了部分的读性能，同时利用其它的实现来弥补读性能，比如boom-filter.

1.写

B树的写入，是首先找到对应的块位置，然后将新数据插入。随着写入越来越多，为了维护B树结构，节点得分裂。这样插入数据的随机写概率就会增大，性能会减弱。

LSM 则是在内存中形成小的排好序的树，然后flush到磁盘的时候不断的做merge.因为写入都是内存写，不写磁盘，所以写会很高效。

2.读

B树从根节点开始二分查询直到叶子节点，每次读取一个节点，如果对应的页面不在内存中，则读取磁盘，缓存数据。

LSM树整个结构不是有序的，所以不知道数据在什么地方，需要从每个小的有序结构中做二分查询，找到了就返回，找不到就继续找下一个有序结构。所以说LSM牺牲了读性能。但是LSM之所以能够作为大规模数据存储系统在于读性能可以通过其他方式来提高，比如读取性能更多的依赖于内存/缓存命中率而不是磁盘读取。


#Cassandra#

Cassandra是一个写性能优于读性能的NoSql数据库，写性能好一个原因在于选择了LSM存储引擎。

#Mongo#

### MMAPv1 ###

Mongo 3.2以前默认使用MMAPv1存储引擎，是基于B-Tree类型的。

**边界(padding)**

MMAPv1 存储引擎使用一个叫做"记录分配"的过程来为document存储分配磁盘空间。MongoDB与Cassandra不同的是，需要去更新原有的document。如果原有的document空间不足，则需要将这个document移动到新的位置，更新对应的index。这样就会导致一些不必要的更新，和数据碎片。

为了避免出现上述情况，就有了边界的概念，就是为document预分配空间。但是这样就有可能造成资源的浪费。

**锁**
MMAPv1使用collection级别的锁，即一个collecion增，删，改一次只能有一个。在并发操作时，就会造成等待。


### WiredTiger ###

3.2及其以后的默认存储引擎,同样是基于B-Tree的。
锁级别为document。并且引入了compression，减少了磁盘占用。


#参考#
https://docs.mongodb.com/manual/core/mmapv1/
http://weibo.com/ttarticle/p/show?id=2309403985460371265557



