# devia

## 2018-01-12
支持完全的JSR-330注解 (过程中)

## 2018-01-13
代理类设置（参照Latke）
构造器注入点支持 （完成）
Provider<T>的支持
Bean实例的完全创建 （50% 代理类，Provider未完成）

## 2018-01-14

（完成）
对于@Qualify元注解，@Named是JSR330一个默认的@Qualify注解
构建对于@Named实现
当@Named注解在类上是，不指定value则设置其value的值为类名首字母小写
当@Named注解在字段或参数上是，不指定value则设置其value为变量名，当字段或参数不出现@Named注解时，不验证这个注解
对于其他自定义的被@Qualify定义的注解，仅验证出现与否，不验证注解字段的值

（完成）
getBean()方法修正，增加重复bean异常抛出

Bean创建过程中循环依赖检测

（完成）
类{com.jerehao.devia.bean.build.BeanBuilder},允许创建类型相同，但qualifiee不同的bea

## 2018-01-15

scope支持
当创建bean时，bean类出现@scope原注解定义的注解时，次注解起作用
@Singleton 容器中仅存一份 （默认）
@Prototype 每次请求获取新的
@request 同一请求，创建一份
@session 同一session创建一份

每次请求创建一个线程

注解兼容
Devia注解：
@Named   ->     @Named   (在Deiva中，name为Bean的唯一标识)
@Inject     ->      @Inject
@Scope(BeanScope)   ->     @Singleton
@Qualifier 不转化，直接支持
provider<T> 不转化，直接支持

bean创建过程优化，完成到Context.get()

## 2018-01-16

拦截器支持
增加配置文件中interceptors的读取
形如：

```
<interceptors>
    <interceptor class="">
        <path></path>
    </interceptor>
</interceptors>
```

或直接使用注解配置

------

(完成)
完全注解配置

web.xml 中，指定注解类（@ApplicationConfig）

```
@ApplicationConfig
@AutoScanPackage("package1,package2")
@ResourcesMapping(value="", location="", fileFilter)
public class ApplicationConfig {
    
    @Bean
    public BeanClass beanName() {
        return new BeanClass();
    }
    
    @Bean
    public BeanClass2 beanName2() {
        return new BeanClass(beanName());
    }
}

```

## 2018-01-18

支持拦截器

-----------------

数据库相关


## 2018-01-19

只对单个查询做缓存

## 2018-01-20

当@Named与其他bean注解同时出现时，以第二注解为准

先仔细了解sql语句
设计类定义
如何根据定义创建表和组装sql语句

## 2018-01-23

保留model层来创建数据库，并辅助dao层
dao层，直接使用sql语句创建

枚举类型直接转字符串

## 2018-01-26

Model列定义和数据库匹配项检查

对于枚举类型，如果设置DataType为数字则存索引。否则存名字。