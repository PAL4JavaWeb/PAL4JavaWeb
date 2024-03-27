# 1. config.properties

|参数名称|参数说明|参数值|
|---|---|---|
|app.name|当前应用的调用关系写入数据库里的表名后缀|test_bookstore|
|call.graph.output.detail|生成调用链时的详细程度，1: 最详细，2: 中等，3: 最简单|1|
|thread.num|并发处理线程数量/数据源连接池数量|20|
|ignore.dup.callee.in.one.caller|生成向下的调用链时，在一个调用方法中出现多次的被调用方法（包含方法调用自定义数据），是否需要忽略|false|
|multi.impl.gen.in.current.file|生成向下的调用链时，若接口或父类存在多个实现类或子类，接口或父类方法调用多个实现类或子类方法的调用关系生成位置|true|
|output.root.path|生成文件的根目录，以"/"或"\\"作为分隔符，末尾是否为分隔符不影响，默认为当前目录||
|db.insert.batch.size|批量写入数据库时每次插入的数量|1000|
|check.jar.file.updated|检查jar包文件是否有更新|true|
|caller.show.raw.method.call.info|生成向下的方法完整调用链时，是否显示原始方法调用信息|true|

# 2. config_db.properties

|参数名称|参数说明|参数值|
|---|---|---|
|db.use.h2|是否使用H2数据库|false|
|db.h2.file.path|H2数据库文件路径（仅当使用H2数据库时需要指定）||
|db.driver.name|数据库配置（仅当使用非H2数据库时需要指定），驱动类名|com.mysql.cj.jdbc.Driver|
|db.url|数据库配置（仅当使用非H2数据库时需要指定），URL|jdbc:mysql://127.0.0.1:3306/test_db?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true|
|db.username|数据库配置（仅当使用非H2数据库时需要指定），用户名|root|
|db.password|数据库配置（仅当使用非H2数据库时需要指定），密码|123456|

# 3. 不区分顺序的其他配置信息

# 4. _jacg_config/allowed_class_prefix.properties

- 参数说明

将java-callgraph2生成的直接调用关系文件写入数据库时使用的配置，需要处理的类名前缀

- 参数值

```
```

# 5. _jacg_config/method_class_4callee.properties

- 参数说明

生成调用指定类/方法的所有向上的方法完整调用链时的配置文件,指定需要生成的类名

- 参数值

```
com.pluralsight.DBConnection:connect()
```

# 6. _jacg_config/method_class_4caller.properties

- 参数说明

生成指定类/方法调用的所有向下的方法完整调用链时的配置文件，指定需要生成的类名，与方法前缀，以及起始代码行号、结束代码行号

- 参数值

```
com.pluralsight.BookDAO
```

# 7. _jacg_config/ignore_class_keyword.properties

- 参数说明

生成指定类/方法调用的所有向上/向下的方法完整调用链时的配置文件，指定忽略的类名关键字

- 参数值

```
```

# 8. _jacg_config/ignore_full_method_prefix.properties

- 参数说明

生成指定类/方法调用的所有向上/向下的方法完整调用链时的配置文件，指定忽略的完整方法前缀

- 参数值

```
```

# 9. _jacg_config/ignore_method_prefix.properties

- 参数说明

生成指定类/方法调用的所有向上/向下的方法完整调用链时的配置文件，指定忽略的方法名前缀

- 参数值

```
```

# 10. 区分顺序的其他配置信息

## 10.1. _jacg_config/jar_dir.properties

- 参数说明

指定需要处理的jar包，或保存class、jar文件的目录

- 参数值

```
out/artifacts/bookstore_war2/bookstore_war.war
```

## 10.2. _jacg_find_keyword/find_keyword_4callee.properties

- 参数说明

生成向上的方法完整调用链文件后，再对结果文件根据关键字生成到起始方法的调用链时，用于指定关键字

- 参数值

```
```

## 10.3. _jacg_find_keyword/find_keyword_4caller.properties

- 参数说明

生成向下的方法完整调用链文件后，再对结果文件根据关键字生成到起始方法的调用链时，用于指定关键字

- 参数值

```
```

## 10.4. _jacg_extensions/code_parser.properties

- 参数说明

定义用于对代码进行解析的扩展类完整类名

- 参数值

```
com.adrninistrator.jacg.extensions.code_parser.jar_entry_other_file.MyBatisMySqlSqlInfoCodeParser
com.adrninistrator.jacg.extensions.code_parser.jar_entry_other_file.SpringTaskCodeParser
com.adrninistrator.jacg.extensions.code_parser.method_annotation.MyBatisAnnotationCodeParser
```

## 10.5. _jacg_extensions/extended_data_add.properties

- 参数说明

定义根据方法调用信息添加方法调用自定义数据扩展类完整类名

- 参数值

```
com.adrninistrator.jacg.extensions.extended_data_add.MybatisMySqlSqlInfoAdd
```

## 10.6. _jacg_extensions/extended_data_supplement.properties

- 参数说明

定义对方法调用自定义数据进行补充的扩展类完整类名

- 参数值

```
```

## 10.7. _jacg_extensions/method_annotation_handler.properties

- 参数说明

定义处理方法上的注解生成用于显示信息的扩展类完整类名

- 参数值

```
com.adrninistrator.jacg.annotation.formatter.SpringMvcRequestMappingFormatter
com.adrninistrator.jacg.annotation.formatter.SpringTransactionalFormatter
com.adrninistrator.jacg.annotation.formatter.DefaultAnnotationFormatter
```

## 10.8. _jacg_extensions/method_call_add.properties

- 参数说明

定义人工添加方法调用关系的扩展类完整类名

- 参数值

```
```

## 10.9. _jacg_extensions/find_keyword_filter.properties

- 参数说明

定义用于对调用链文件查找关键字时使用的过滤器扩展类完整类名

- 参数值

```
```

