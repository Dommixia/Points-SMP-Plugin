# Points SMP Plugin

A custom Minecraft server plugin built in Java for a multiplayer SMP server. Players earn points through advancements, PvP kills, and defeating boss mobs. Includes a live leaderboard, admin controls, and two custom craftable weapons.

## What this plugin does

- Players earn points by completing advancements, killing other players, and defeating boss mobs
- Admins can add or remove points from any player via commands
- A leaderboard command displays the top 10 players by points in real time
- Two fully custom weapons with unique enchants, crafting recipes, and special effects

## Point sources

| Action | Points |
|---|---|
| Completing an advancement | ✅ Yes |
| Killing a player | ✅ Yes |
| Killing the Wither | ✅ Yes |
| Killing the Warden | ✅ Yes |
| Killing the Ender Dragon | ✅ Yes |

## Custom weapons

### Oathbreaker
A powerful golden sword with custom enchantments and a special lightning strike effect.

- **Enchants:** Unbreaking VI, Fire Aspect III
- **Special effect:** Strikes lightning on the target upon hit
- **Crafting recipe:**
- [Nether Star]
- [Nether Star]
- [Blaze Rod]

### Arctic Spear
A custom ranged/melee weapon with ice-themed effects.
- **Enchants:** Loyalty IV
- **Special effect:** Applies Coldness, Slowness upon hit on enemies
- **Crafting recipe:**
- [Heavy Core]
- [heart_of_the_sea] [trident] [heart_of_the_sea]
- [Blaze Rod]


## Commands

| Command | Description | Permission |
|---|---|---|
| `/leaderboard` | Shows top 10 players by points | All players |
|`/points` | Shows your no. of points | All players|
| `/adminpoints add <player> <amount>` | Adds points to a player | Admin only |
| `/adminpoints remove <player> <amount>` | Removes points from a player | Admin only |

## Tech stack

- Java
- Spigot / Paper API (Minecraft plugin framework)
- Maven (build system)

## Project structure
