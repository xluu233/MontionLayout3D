## MontionLayotu实现裸眼3D


### 1. 利用MotionLayout实现
> 最开始想到的是用motionlayout也可以同样实现，但是最后发现我错了，motionlayout设置的view路径是固定的，无法在xy轴上自由移动。

一个简单的效果：

![1.gif](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/35c78bece0a84c93809750ae3576eb8d~tplv-k3u1fbpfcp-watermark.image)

### 2. 封装View

> 利用自定义ViewGroup实现，直接在布局中引用就行了

最终效果：

![2.gif](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/a9491097460f41ac9359c704038413ee~tplv-k3u1fbpfcp-watermark.image)

![3.gif](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/4b9f9fdfe662418d965551a797b8b431~tplv-k3u1fbpfcp-watermark.image)


### 3. 问题

不知道为啥有时候会莫名抽搐，移动的不是很顺滑。。。。

实现效果不太好，小问题还很多，不建议使用😥😥

**求大佬指点**
