# コマンドシステム

## コンセプト

FlyLibのコマンドの概念は、基本的にBukkitと同じです。名前や説明、権限、使用方法などがあります。 BukkitとFlyLibの違いは、FlyLibではplugin.ymlのようなリソースファイルを使用しないことです。
すべてのコマンドに必要な変数は、以下のように、クラス上で定義されています。

**Kotlin:**

```kotlin
class SomeCommand : Command("some") {
    init {
        description("This is some description of this command.")
        permission(Permission.OP)
        example("/some", "/some <number>")
        //or permission(Permission("permission_name", PermissionDefault.TRUE))
    }
}
```

**Java**

```java
class SomeCommand extends Command {
    public SomeCommand() {
        super("some");
        description("This is some description of this command.");
        permission(Permission.OP);
        example("/some", "/some <number>");
        //or permission(new Permission("permission_name", PermissionDefault.TRUE));
    }
}
```

FlyLibのコマンドは、[FlyLibを使い始める](https://github.com/TeamKun/flylib-reloaded/blob/master/wiki/ja/getting-started.md)
で説明したFlyLibのエントリーポイント(ラムダ式、FlyLibBuilder)で定義することができます。 これにより、パーミッションの設定や、プラグインの有効化・無効化時の登録・解除を自動的に行います。
具体的には、`command(new SomeCommand());`のようにして定義することができます。

**⚠️注意:** 子となるコマンドは、FlyLibビルダーで定義する必要はありません。親コマンドのchildrenメソッドを使って定義することで、自動的に子コマンドとして処理されます。

## CommandContextについて

FlyLibコマンドシステムには、CommandContextと呼ばれるものがあります。これは、コマンドの実行時にさまざまな情報と、その情報に適用できるメソッドを含むコンテナーのようなものです。    
たとえば、実行されたCommandSender、（キャストできる場合）実行されたプレーヤー、（存在する場合）実行されたワールド、座標が含まれます。
コマンドの入力、コマンドへの入力から解析された引数のリスト、および可能であれば型にキャストされた引数のリストもあります。  
メソッドには、コマンドを実行したCommandSenderに色付きのメッセージを送信するユーティリティメソッドが含まれています。

コマンドの入力に対して何かしらアクションを起こしたいときは、実行時に渡されるCommandContextを使用することを推奨しています。

## "Usage"とは？

### "Usage"の説明

FlyLibの "Usage"の概念はBukkitよりも少し複雑になっています。
FlyLibでは、コマンドがどのような引数を取り、何をするのか、どのようなパーミッションが必要なのか、どのようなタブ補完をしたいのか、といった必要な情報を事前に定義します。 一方で、入力が定義されているものにはなかったり(引数の数が違う等)
、定義されている型とは異なる型だったりすると、プレイヤーのクライアント側にエラーが表示されます。 例えば、「explode」というコマンドを考えてみましょう。 このコマンドは次のような引数をとります。

```
/explode <player> <power(integer)>
```

このコマンドは、次のような入力でのみ実行できます。

```
/explode SomePlayer 3
/explode @a[distance=..5] 10
```

以下のような定義外の入力をすると、クライアントにエラーが表示され、コマンドや"Usage"で定義された実行内容が実際に実行されることはありません。

```
/explode SomePlayer
/explode SomePlayer powerrrrrrrr
```

"Usage"を指定する方法は、コマンドで説明や権限、使用例を指定するのと同様で、 コマンドのコンストラクタで，Usageメソッドを呼び出すだけです。 このメソッドの引数はUsageBuilderを引数にとるラムダ式です。
JavaでもKotlinでも、このUsageBuilderを操作することで、 引数の定義、"Usage"の実行、"Usage"の権限の設定などを行うことができます。

**権限について:**

UsageBuilderで明示的に指定しない限り、"Usage"の権限はコマンドを継承します。  
例えば、"/config "コマンドの権限がOPで、`/config get`の権限のみをEVERYONEに設定したい場合、`permission(Permission.EVERYONE)`を`/config get`
のUsageBuilder内で明示的に指定しない限り、`/config`コマンドのOPが継承されてしまいます。

**実行内容について:**
"Usage"の実行内容は、明示的に指定されていない限りコマンドクラスのexecuteメソッドが実行されます。 これ は、複数のUsageの処理を1つにまとめて処理したい場合に非常に便利です。
後述するように、コマンドクラスのexecuteメソッドは、デフォルトでCommandContextのsendHelpメソッドを呼び出すようになっているので
"Usage"が指定されていない（引数がない）入力の場合に自動的にヘルプメッセージを呼び出すことができます。

### "Usage"を具体的に定義する方法

**Kotlin:**

```kotlin
class ExplodeCommand : Command("explode") {
    init {
        description("Explode a player.")
        permission(Permission.OP)

        usage {
            description("This is description of this usage.")
            permission(Permission.EVERYONE)
            //何も指定されていない場合は、SomeCommandの権限設定であるOPが継承されます。

            entityArgument(name = "target", enableSelector = true, enableEntities = false)
            integerArgument(name = "power", min = 1, max = 10)

            executes { context ->
                val players = context.typedArgs[0] as List<Entity>
                val power = context.typedArgs[1] as Int

                players.forEach {
                    context.world.createExplosion(...)
                }

                context.success("Exploded ${players.size} players!")
                //気をつけてください。successメソッドは緑のメッセージを送るだけなので注意が必要です。 
                //Bukkitのように、コマンドがtrueかfalseを返すシステムはなく、successメソッドを呼んでもほかの処理は変わりません。
            }
        }
    }
}
```

**Java:**

```java
class ExplodeCommand extends Command {
    public ExplodeCommand() {
        super("explode");
        description("Explode a player.");
        permission(Permission.OP);

        usage {
            description("This is description of this usage.");
            permission(Permission.EVERYONE);
            //何も指定されていない場合は、SomeCommandの権限設定であるOPが継承されます。

            entityArgument("target", true, false);
            integerArgument("power", 1, 10);

            executes(context -> {
                ArrayList<Entity> players = (ArrayList<Entity>) context.typedArgs[0];
                int power = (int) context.typedArgs[1];

                players.stream().forEach(player -> {
                    context.world.createExplosion(...)
                });

                context.success("Exploded " + players.size + " players!");
                //気をつけてください。successメソッドは緑のメッセージを送るだけなので注意が必要です。 
                //Bukkitのように、コマンドがtrueかfalseを返すシステムはなく、successメソッドを呼んでもほかの処理は変わりません。
            });
        }
    }
}
```