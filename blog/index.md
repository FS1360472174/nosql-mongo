#摘要#
mongo 的索引非常强大，和关系型数据库索引没什么区别。这里主要介绍本人在mongo索引上的犯的错。

#索引种类#
1.单字段索引
2.复合索引
多个字段索引
如{name:1,address:1}，包含的是两个查询

```
db.book.find({name:"xxx"})
db.book.find({name:"xxx",address:"xxx"})
```


3.多键索引
如array索引
4.唯一索引
db.book.createIndex({"name":1},{"unique":true})
mongo 默认创建的不是唯一索引，需要显示指定。唯一索引会对数据进行校验，不允许重复数据。

#array index#
mongo 可以对array建立index,注意是将index中的每个元素都作为index key,进行索引。所以对array建立index一定要十分小心，很容易导致index size 很大。另外mongo支持指定array某一列进行查询。


```
test.book
{
	_id:1,
	name:english,
	address:[addr1,addr2]
}
```
db.book.find({"address.0":"addr1"})
**当对address创建index,这样的查询是用不到index的。只有基于array的查询，index才能有效。**
mongo并没有那么神奇的在创建index的同时还保留列数。


#shard key index#

 - 表中有数据
    表中有数据再创建shard key,需要首先创建对应的index,才能去创建shard key
 - 表中无数据
    表中无数据，创建shard key的同时,mongo会自动创建一个对应字段的index

  

```
sh.shardCollection("test.book",{name:1,address:1})
```
会自动创建index

```
{name:1,address:1}
```

#mongo index VS cassandra secondary index #
**1.query 过程**
cassandra query,首先根据partitioner key去找对应**partition**,partition中的数据是按照clustering key排序的。注意是按照clustering key排序的，clustering key这个字段 不是index。

mongo(sharding cluster) query,首先根据给定的shard key去找在哪个节点上，然后将请求发送到此节点。进行查找。
如果你的query case是

```
db.book.find({name:"xxx",address:"xxx"})
```
而shard key是name。此外再单独为address建立一个index。这时候你的query其实是命中的address 的单字段index。而不是预想的已经将name数据过滤了。这点和cassandra有很大的不同

**2.范围**
cassandra secondary index 是local的,在每个节点上。
mongo 的index是全局的。