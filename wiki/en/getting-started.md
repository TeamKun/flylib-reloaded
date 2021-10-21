# Getting started

## Add a dependency

FlyLib Reloaded (following, FlyLib) is published in Maven Central, so if you have a normal project configuration, you can start using it immediately by adding the following to the dependency.
```
Group: dev.kotx
Artifact: flylib-reloaded
```
![](https://img.shields.io/maven-central/v/dev.kotx/flylib-reloaded?color=blueviolet&label=version&style=flat-square)

For Gradle (including Gradle Kotlin DSL), you can add the following:
```groovy
implementation("dev.kotx:flylib-reloaded:[version]")
```
With Maven, FlyLib can be included in the dependency by adding the following.
```xml
<dependency>
    <group>dev.kotx</group>
    <name>flylib-reloaded</name>
    <version>[version]</version>
</dependency>
```

## Introduce FlyLib into a plugin

FlyLib starts injection (registering FlyLib inside Paper) with the following code.

**Kotlin:**

```kotlin
class SomePlugin : JavaPlugin {
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

This allows FlyLib to hook into the process when the plugin is activated and deactivated. At the moment, this hooked
process will automatically do the following

- Registering and unregistering Koin context to be used inside FlyLib
- Registering and unregistering commands

Also, since most of the menus and other functions used by FlyLib use the Koin context registered above, it is strongly
recommended writing the above in the constructor or onEnable method of the plugin when using FlyLib features.

Below is a detailed list of FlyLib's features and how to use them.

- [Commands](https://github.com/TeamKun/flylib-reloaded/blob/master/wiki/en/commands.md)