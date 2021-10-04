# About commands

## Concept

The concept of commands in FlyLib is basically the same as in Bukkit.
There is a name, a description of the command, permissions, usage, etc.
The difference between Bukkit and FlyLib is that FlyLib does not use resource files such as plugin.yml. The elements needed for all commands are defined on the class. As follows.

**Kotlin:**
```kotlin
class SomeCommand: Command("some") {
    init {
        description("This is some description of this command.")
        permission(Permission.OP)
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
        //or permission(new Permission("permission_name", PermissionDefault.TRUE));
    }
}
```

## What is "usage"?

The concept of Usage in FlyLib is a bit more complicated than in Bukkit, because in FlyLib you define in advance what arguments the command will take, what it will do, and what permissions it needs.
On the other hand, if the input is not defined, or is of a different type than defined, an error will be displayed on the client side.
For example, consider the command "explode", which takes the following arguments

```
/explode <player> <power(integer)>
```

This command can only be executed with input like the following.

```
/explode SomePlayer 3
/explode @a[distance=..5] 10
```

If the following is entered, an error will be displayed to the client, and any execution defined in the Command will never be actually executed.
```
/explode SomePlayer
/explode SomePlayer powerrrrrrrr
```