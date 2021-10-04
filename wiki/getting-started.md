# Getting started

FlyLib Reloaded (hereafter referred to as FlyLib) starts injecting with the following code.

**Kotlin:**
```kotlin
class SomePlugin: JavaPlugin {
    init {
        flyLib {
            //Initial configuration of FlyLib. These will be described later.
        }
    }
}
```

**Java:**
```java
class SomePlugin extends JavaPlugin {
    public SomePlugin() {
        FlyLibKt.flyLib(this, flyLib -> {
            //Initial configuration of FlyLib. These will be described later.
        });
    }
}
```

This allows FlyLib to hook into the process when the plugin is activated and deactivated.
At the moment, this hooked process will automatically do the following

- Registering and unregistering Koin context to be used inside FlyLib
- Registering and unregistering commands

Also, since most of the menus and other functions used by FlyLib use the Koin context registered above, it is strongly recommended writing the above in the constructor or onEnable method of the plugin when using FlyLib features.

Below is a detailed list of FlyLib's features and how to use them.

**Commands:**
- [About Commands](https://github.com/TeamKun/flylib-reloaded/blob/master/wiki/commands/about.md)