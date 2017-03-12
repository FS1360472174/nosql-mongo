#摘要#
在[mongo初识](http://blog.csdn.net/fs1360472174/article/details/52901780)文中介绍了mongo与cassandra的主要区别，以及mongo物理部署架构图。本文接着上一篇的mongo 架构图，来继续讲分片集群。

#分片介绍#
##**shard key**##
mongo 默认配置是不分片的，需要自行enable.mongo 根据shard key来对数据进行分片。有三种分片方式

- range 
根据范围来分片，比如1-10到shardA,11-20 shardB，以此类推

- hash 

  根据shard key的hash 值来分片

- tag
代表的shard key 的一段范围值,比如下图，Tag A代表的就是[1,10)

![这里写图片描述](https://docs.mongodb.com/manual/_images/sharded-cluster-tag-aware.png)

**三种分片的方式区别**

range分片会让相近的数据分配在同一个shard上，这样如果进行shard key范围查找的时候效率更高，因为不需要跨shard，或者跨更少的shard.

hash 会让数据分布的更加均匀，降低了某一部分连续的数据都存储在同一shard上，导致数据在集群中分布不均匀。

tag通常是用来隔离数据的，多用于多数据中心架构中。确保相近的数据能够落在物理上相近的shard上。

##**chunk的概念**##
看了上面的tag hash，有分布式基础的应该会有疑问，A,B两个shard都有tag A,如果一条数据的shard key值落在了tag A范围时，那他会落在哪个shard 上呢。Shard A or Shard B,this is a question.

这里必须得引入chunk的概念了。shard 并不是mongo document 存储的最小单位，插入/读取的路由也不是基于shard的。最小单位是chunk.
chunk 是一组shard key value 范围的数据集合[minKeyValue,maxKeyValue)，是shard的细分。

在Nosql的分布式存储模式中，要求数据要尽可能的在集群中均匀分布，必然会涉及到数据的移动进行balance.cassandra 通过一致性hash以及虚拟节点来实现这点。如果mongo只是通过shard 这种粗粒度的进行数据分片，可以想象随着数据的增长，必然会出现数据的分布不均匀，不同shard 的规模不一样。引入chunk后，细分了数据，由于shard 是一组物理及其的复制集，不可移动。引入chunk后，可以通过move chunk来balance 数据在各个shard直接的分布。

**chunk的移动**
chunk 是一组shard key value的集合。他有大小限制，默认的chunk size 是64MB.你可以去更改这个值。当chunk超过这个值，插入或者更新时就会触发chunk 分裂，chunk代表更小范围的shard key value 集合。所以chunk最小范围是一个shard key,以后再也无法分裂。当chunk 在各个shard 上分布不均匀的时候，mongo 的balancer就会move chunk,确保在chunk在各个shard中均匀分布。moveChunk是需要代价的，所以mongo有阈值来控制什么时候去move chunk.

| chunk中数量 | 阈值|
| ------------- |-------------|
| <20 | 2 |
| 20-79 | 4 | 
| '>=80 | 8 |

当一个chunk document 数目超过250,000 或者大小超过1.3*(chunk_size/obj_avg_size)的时候就无法move了。obj_avg_size是一个document的平均大小。就会演变成一个超级大的chunk，就会导致数据的分布不均匀，导致请求的热点产生，极大的影响性能。这是我们不想看到的。所以通常来说单一字段作为shard key，都会造成问题，需要联合字段来做shard key.

**跨数据中心的chunk move代价岂不是很高**
数据的移动在物理分布不同的数据中心代价肯定很高，所以要避免这样的情况发生，可以利用三种分片方式中的tag 分片来做。因为tag是在chunk move 之上，当chunk move时会去检查这个tag 设置的。

# 如何选择shard key#

如何选择shard key，是件充满艺术的事情！mongo 不支持修改shard key，所以要提前定义好shard key。shard key 需要遵循以下原则

-  数据要在集群中分布均匀
nosql database 都要考虑这个问题，数据均匀分布意味着很多，shard key 的取值范围肯定不能太小

-  查询
需要考虑你的query case，要让查询尽可能的跨越少的shard

- mongo 的限制
比如上面说的，chunk 的documents 数和大小限制，要避免大的，不可移动的chunk 产生。


#什么时候去分片#
当你准备好的时候，就去分片。

程序的架构是不断演进的，数据的规模也是不断在增长的。如果你的mongo是为大数据存储服务的，那么你应该尽可能早的去定义shard key.如果你的应用可预知的数据规模是很小的，百万级别以下的，那你可以推迟这件事。
