# myagent
用于平滑替换运行中 Java 进程的类定义。

Class definition for smooth replacement of running Java processes.

使用该 agent 可以在不重启进程的情况下修改代码，并且立即生效。

The agent can modify the code without restarting the process and take effect immediately.
## 前提条件
Java应用进程必须设置`Can-Retransform-Classes = true`,如果使用 maven 打包，可以在 pom.xml 中进行如下配置：

Java application process must set 'can retransform classes = true'. If Maven is used for packaging, you can use the pom.xml The configuration is as follows:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
            <configuration>
                <transformers>
                    <transformer
                            implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                        <manifestEntries>
                            <Can-Redefine-Classes>true</Can-Redefine-Classes>
                            <Can-Retransform-Classes>true</Can-Retransform-Classes>
                        </manifestEntries>
                    </transformer>
                </transformers>
            </configuration>
        </execution>
    </executions>
        </plugin>
    </plugins>
</build>
```
## 如何使用
maven打包后，执行 `java -jar myagent-1.0-SNAPSHOT.jar`

run `java -jar myagent-1.0-SNAPSHOT.jar`


按照提示依次输入“进程ID”、“全限定类名”、“替换用的class文件地址”、“日志文件全路径”。示例如下：

```
entry a PID from upper:1234
className:java.lang.String
classPath:/classdir/String.class
logfile:/logdir/example.log
```

也可以通过启动参数的形势传递，如：

`java -jar myagent-1.0-SNAPSHOT.jar 1234 java.lang.String /classdir/String.class /logdir/example.log`

如果正常执行结束将会看到如下信息：
```
attaching...
loadAgent...
agent loaded!
detaching...
detached!
```


详细的执行日志可以通过日志文件查看，执行过程中的异常信息也会记录在日志文件中。
