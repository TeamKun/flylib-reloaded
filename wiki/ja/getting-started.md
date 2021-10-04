# FlyLibを使い始める

FlyLib Reloaded（以下、FlyLib）は、以下のコードで注入(Paperの内部にFlyLibを登録すること)を開始します。

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

また、FlyLibで使用されるメニューや拡張などの機能のほとんどは、上記で登録したKoinコンテキストを使用しています。 そのため、FlyLibの機能を使用する際には、プラグインのコンストラクタやonEnableメソッド(
つまりはプラグインのエントリーポイント)に上記のコードを記述することを強く推奨します。

以下に、FlyLibの機能とその使い方の詳細をまとめました。

- [コマンドシステム](https://github.com/TeamKun/flylib-reloaded/blob/master/wiki/ja/commands.md)