# devia

## 2018-01-12
支持完全的JSR-330注解 (过程中)

## 2018-01-13
代理类设置（参照Latke）
构造器注入点支持
Provider<T>的支持
Bean实例的完全创建

## 2018-01-14

对于@Qualify元注解，@Named是JSR330一个默认的@Qualify注解
构建对于@Named实现
当@Named注解在类上是，不指定value则设置其value的值为类名首字母小写
当@Named注解在字段或参数上是，不指定value则设置其value为变量名，当字段或参数不出现@Named注解时，不验证这个注解
对于其他自定义的被@Qualify定义的注解，仅验证出现与否，不验证注解字段的值

getBean()方法修正，增加重复bean异常抛出 （完成）

Bean创建过程中循环依赖检测

类{com.jerehao.devia.beans.build.BeanBuilder},允许创建类型相同，但qualifiee不同的bea
