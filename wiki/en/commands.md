# About commands

## Concept

The concept of commands in FlyLib is basically the same as in Bukkit. There is a name, a description of the command,
permissions, usage, etc. The difference between Bukkit and FlyLib is that FlyLib does not use resource files such as
plugin.yml. The variables needed for all commands are defined on the class. As follows.

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

## What is "usage"?

### Description of "usage"

The concept of "Usage" in FlyLib is a bit more complicated than in Bukkit, because in FlyLib you define in advance what
arguments the command will take, what it will do, what permissions it needs, what kind of tab completion you want the
arguments to do, and other necessary information. On the other hand, if the input is not defined, or is of a different
type than what is defined, an error will be displayed on the client side. For example, consider the command "explode",
which takes the following arguments

```
/explode <player> <power(integer)>
```

This command can only be executed with input like the following.

```
/explode SomePlayer 3
/explode @a[distance=..5] 10
```

If the following is entered, an error will be displayed to the client, and any execution defined in the Command will
never be actually executed.

```
/explode SomePlayer
/explode SomePlayer powerrrrrrrr
```


The way to specify "Usage" is similar to specifying the description, permission, or example of a command. In the
constructor of the command, call the usage method. The argument of this method is a lambda expression that takes
UsageBuilder as an argument. In both Java and Kotlin, you can manipulate this UsageBuilder to add arguments, execute "
usage", set permissions, etc.

**About permission:**

Unless explicitly specified in the UsageBuilder, the permissions of "Usage" will be inherited by the command.   
For example, if the permission of the "/config" command is OP, and you want to set only `/config get` to EVERYONE, the
OP of the `/config` command will be inherited unless you explicitly specify `permission(Permission.EVERYONE)` in the
UsageBuilder of `/config get`.

**About execution:**

The execution of Usage will execute the execute method of the command class, unless explicitly specified otherwise. This
is very useful when you want to combine the processing of multiple Usages into one and process them separately. As we
will see later, the command class execute will call the sendHelp method of its CommandContext by default, so it can
automatically call a help message when no Usage is specified (no arguments).

### How to define "Usage" in concrete terms
