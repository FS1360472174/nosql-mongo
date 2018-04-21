#核心组件 #

1. driver

2. async driver

3. BSON core

# 主要类 #

MongoClient: Mongo 数据库连接池

# 编解码器codec#

MongoCollection 默认为 Document,BasicDBObject,BsonDocument配置了解码器
CodecRegistry 