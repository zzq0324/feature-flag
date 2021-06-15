# Feature Flag

## 为什么需要Feature Flag

## Feature Flag技术实现

### 灰度算法

详见[Feature Flag灰度算法](docs/algorithm.md)

## 使用说明

对于接入方，需要完成Apollo、代码接入2个步骤才能最终用上Feature Flag。具体如下：

### Apollo配置

一个完整的Feature Flag配置如下：
```properties
# 灰度开关描述
flags.enable-open-search.desc=控制是否开启开放搜索
# 灰度开始时间，留空默认不限制开始时间
flags.enable-open-search.startTime=2021-06-12 01:02:03
# 灰度结束时间，留空默认不限制开始时间
flags.enable-open-search.endTime=
# 灰度白名单，允许为空
flags.enable-open-search.whiteList=u1,u2,u3
# 灰度黑名单，允许为空
flags.enable-open-search.blackList=
# 灰度比例
flags.enable-open-search.launchPercent=95
```

### 代码调用

* **场景一：同个业务id严格控制每次执行结果一致**
    ```java
    FeatureFlagHelper.isFeatureOn("$flagName", "$bizId");
    ```

* **场景二：控制流量整体趋势符合灰度比例，同个业务id执行结果不要求强一致**
    ```java
    FeatureFlagHelper.isFeatureOn("$flagName");
    ```

## 命名规范和管理流程

### 命名规范

flag名称尽量以`enable-`开头，例如`enable-open-search`。

### 管理流程

## 参考资料

* [为什么Java String哈希乘数为3？](https://mp.weixin.qq.com/s/sCWQGU_OWiQkDUuSPXvw-w)
* [FNV哈希算法](https://blog.csdn.net/hustfoxy/article/details/23687239)
* [JavaFNV](https://github.com/Killeroid/JavaFNV)
* [Fowler–Noll–Vo hash function](https://en.wikipedia.org/wiki/Fowler%E2%80%93Noll%E2%80%93Vo_hash_function)