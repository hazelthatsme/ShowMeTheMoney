# ShowMeTheMoney
A Minecraft plugin (Bukkit, Spigot, Paper, etc.) that adds money to mob drops.

## Features
- Compatible with [Vault](https://dev.bukkit.org/projects/vault).
- If Vault is not present, uses its own economy system.
- Highly configurable: (Example config.yml found in `src/main/resources`)
  - Currency names used by internal economy system,
  - Item colors,
  - Sound effects on pickup,
  - Dropped amounts per entity.

## Compiling
ShowMeTheMoney uses a Maven .pom file to handle all the dependencies to compile the plugin.