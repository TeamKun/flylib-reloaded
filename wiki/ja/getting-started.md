# FlyLibを使い始める

## 依存関係を追加する

FlyLib Reloaded(以下、FlyLib)は、Maven Centralに公開されているため、普通のプロジェクト構成であれば、以下を依存関係に追加するだけですぐに使い始めることが出来ます。

```
Group: dev.kotx
Artifact: flylib-reloaded
```

![](https://img.shields.io/maven-central/v/dev.kotx/flylib-reloaded?color=blueviolet&label=version&style=flat-square)

Gradle(Gradle Kotlin DSLを含む)であれば、

```groovy
implementation("dev.kotx:flylib-reloaded:[version]")
```

を追加することで、 Mavenであれば、

```xml

<dependency>
    <group>dev.kotx</group>
    <name>flylib-reloaded</name>
    <version>[version]</version>
</dependency>
```

を追加することでFlyLibを依存関係に含めることが出来ます。

しかし、依存関係を追加するだけでは、実行できないはずです。 生成したプラグインのjarファイルは、FlyLibを依存関係として持っていますが、対象となるFlyLibはJarファイルに含まれておらず、読み込まれていないからです。  
簡単な解決策として、shadowJar(mavenだとshade)というGradleプラグインを用いて、依存関係すべてを生成したjarファイルに含めることが出来ます。

```groovy
plugins {
    id 'com.github.johnrengelman.shadow' version '6.0.0'
    //Kotlin Gradle DSLの場合は以下を使用してください。
    //id("com.github.johnrengelman.shadow") version "6.0.0"
}
```

GradleのshadowJarタスクを使用して`./build/lib/`に生成されたjarファイルはすべての依存関係を含んでいるので、サーバーのpluginsフォルダに入れるだけで、問題なく読み込むことが出来ます。

しかし、別のバージョンのFlyLibを使用する複数のプラグインがあった場合はどうでしょうか。 サーバーは同じ名前のクラスを読み込もうとしますが、別のバージョンのFlyLibが存在するので、どちらか片方は読み込みに失敗してしまいます。
その場合の解決策として、relocationが挙げられます。日本語では「再配置」の意味を持ちますが、プラグイン毎に別のパッケージとしてすべての依存関係を「再配置」することにより、
そのプラグインのみが参照する形でFlyLibを使用することが出来、複数プラグインを導入するサーバーなどでは役に立ちます。

##### build.gradle

```groovy
import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation

task relocateShadow(type: ConfigureShadowRelocation) {
    target = tasks.shadowJar
    prefix = project.group
}

tasks.shadowJar.dependsOn tasks.relocateShadow
```

##### build.gradle.kts

```kotlin
import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation

val relocateShadow by tasks.registering(ConfigureShadowRelocation::class) {
    target = tasks.shadowJar.get()
    prefix = project.group.toString()
}

tasks.shadowJar {
    dependsOn(relocateShadow)
}
```

## FlyLibをプラグインに導入する

FlyLibは、以下のコードで注入(Paperの内部にFlyLibを登録すること)を開始します。

**Kotlin:**

```kotlin
class SomePlugin : JavaPlugin {
    init {
        flyLib {
            //FlyLibの初期設定です。これらは後述します。
        }
    }
}
```

**Java:**

```java
class SomePlugin extends JavaPlugin {
    public SomePlugin() {
        FlyLibKt.flyLib(this, flyLib -> {
            //FlyLibの初期設定です。これらは後述します。 
        });
    }
}
```

上記のコードにより、FlyLibはプラグインの有効化・無効化の際の処理をフックし、コマンドの追加などができるようになります。

以下に、FlyLibの機能とその使い方の詳細をまとめました。

- [コマンドシステム](https://github.com/TeamKun/flylib-reloaded/blob/master/wiki/ja/commands.md)
- [コンフィグシステム](https://github.com/TeamKun/flylib-reloaded/blob/master/wiki/ja/config.md)
- [メニューシステム](https://github.com/TeamKun/flylib-reloaded/blob/master/wiki/ja/menu.md)