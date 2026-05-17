# MapReduce 作业在服务器上的运行步骤

> 服务器：`root@218.194.47.8 -p 5222`
> Hadoop：`/opt/hadoop-3.4.1`（命令用全路径；`hadoop`/`hdfs` 不在 PATH）
> 前提：DataNode 已启动、HDFS 已退出安全模式（环境就绪后再做）

## 1. 本地打包

```powershell
cd d:\JavaLecture\Lab2\GengDirection
.\gradlew.bat :geng-mr:jar
# 产物：geng-mr\build\libs\geng-mr-1.0.jar
```

## 2. 上传 jar 到服务器

```powershell
scp -P 5222 geng-mr\build\libs\geng-mr-1.0.jar root@218.194.47.8:~/
```

## 3. 在服务器上运行（SSH 登录后）

```bash
HADOOP=/opt/hadoop-3.4.1/bin/hadoop
HDFS=/opt/hadoop-3.4.1/bin/hdfs

# 首次：建好原始日志目录（Spring Boot 会往这里写）
$HDFS dfs -mkdir -p /geng/behavior/raw

# 输出目录必须不存在，重跑前先删
$HDFS dfs -rm -r -f /geng/behavior/out/hotpost /geng/behavior/out/action-dist

# 作业一：热门帖子 Top-15
$HADOOP jar ~/geng-mr-1.0.jar com.nailong.mr.HotPostJob \
    /geng/behavior/raw /geng/behavior/out/hotpost 15

# 作业二：行为类型分布
$HADOOP jar ~/geng-mr-1.0.jar com.nailong.mr.ActionDistJob \
    /geng/behavior/raw /geng/behavior/out/action-dist

# 查看结果（答辩截图用）
$HDFS dfs -cat /geng/behavior/out/hotpost/part-r-00000
$HDFS dfs -cat /geng/behavior/out/action-dist/part-r-00000
```

## 说明（答辩讲解点）

- jar 里**只有我们自己的 3 个类**，Hadoop 类由 `hadoop jar` 命令注入，所以 jar 很小。
- 集群内存小（NodeManager 1GB，机器仅 2GB），`mapred-site.xml` 已把 map/reduce 调到
  256MB、AM 512MB；**作业代码里不要再调大内存**，否则申请不到 Container 会一直卡住。
- `HotPostJob` 用单 Reducer + TreeMap 在 `cleanup` 里取全局 Top-N；
  `ActionDistJob` 就是 WordCount 变体，并加了 Combiner 减少 shuffle。
- YARN 作业进度可在 `http://218.194.47.8:10034` 查看。
