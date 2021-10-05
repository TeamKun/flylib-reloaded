# Command System

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

This command can be defined in the FlyLib builder, described
in [Getting started](https://github.com/TeamKun/flylib-reloaded/blob/master/wiki/en/getting-started.md), to
automatically set permissions and register and unregister plugins when they are enabled or disabled.
Like `command(new SomeCommand());`

FlyLib commands have the concept of children. This is called a subcommand in other frameworks, and it eliminates the
need for complex argument handling like in Bukkit.  
Unless you explicitly specify permissions on the child side, child commands inherit the permissions of the parent.

**⚠️Warnings:** The child commands do not need to be defined in the FlyLib builder. By defining them using the children
method on the parent command, they are automatically handled as children.

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
Usage", set permissions, etc.

**About permission:**

Unless explicitly specified in the UsageBuilder, the permissions of "Usage" will be inherited by the command.   
For example, if the permission of the "/config" command is OP, and you want to set only `/config get` to EVERYONE, the
OP of the `/config` command will be inherited unless you explicitly specify `permission(Permission.EVERYONE)` in the
UsageBuilder of `/config get`.

**About execution:**

The execution of Usage will execute the execute method of the command class, unless explicitly specified otherwise. This
is very useful when you want to combine the processing of multiple Usages into one and process them separately.  
As we will see later, the command class execute will call the sendHelp method of its CommandContext by default, so it
can automatically call a help message when no Usage is specified (no arguments).

### How to define "Usage" in concrete terms

**Kotlin:**

```kotlin
class ExplodeCommand : Command("explode") {
    init {
        description("Explode a player.")
        permission(Permission.OP)

        usage {
            description("This is description of this usage.")
            permission(Permission.EVERYONE)
            //If none is specified, the authority setting of SomeCommand, OP, will be inherited.

            entityArgument(name = "target", enableSelector = true, enableEntities = false)
            integerArgument(name = "power", min = 1, max = 10)

            executes { context ->
                val players = context.typedArgs[0] as List<Entity>
                val power = context.typedArgs[1] as Int

                players.forEach {
                    context.world.createExplosion(...)
                }

                context.success("Exploded ${players.size} players!")
                //Be careful, the success method just sends a green message. 
                //There is no system like Bukkit where the command returns true or false.
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
            //If none is specified, the authority setting of SomeCommand, OP, will be inherited.

            entityArgument("target", true, false);
            integerArgument("power", 1, 10);

            executes(context -> {
                ArrayList<Entity> players = (ArrayList<Entity>) context.typedArgs[0];
                int power = (int) context.typedArgs[1];

                players.stream().forEach(player -> {
                    context.world.createExplosion(...)
                });

                context.success("Exploded " + players.size + " players!");
                //Be careful, the success method just sends a green message. 
                //There is no system like Bukkit where the command returns true or false.
            });
        }
    }
}
```