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

上記のコードにより、FlyLibはプラグインの有効化・無効化の際の処理をフックすることができます。 現時点では、このフックされた処理で、以下のことが自動的に行われます。

- FlyLib内部で使用するKoinContextの登録・解除
- コマンドの登録・登録解除

また、FlyLibで使用されるメニューや拡張など、機能のほとんどは、上記で登録したKoinContextを使用しています。 そのため、FlyLibの機能を使用する際には、プラグインのコンストラクタやonEnableメソッド(
つまりはプラグインのエントリーポイント)に上記のコードを記述することを強く推奨します。

以下に、FlyLibの機能とその使い方の詳細をまとめました。

- [コマンドシステム](https://github.com/TeamKun/flylib-reloaded/blob/master/wiki/ja/commands.md)