# SC2002 Turn-Based Combat Game

This repository contains a command-line turn-based combat game built for the SC2002 Object Oriented Design & Programming assignment. The project models a small battle system with two playable classes, three difficulty levels, item usage, status effects, and a simple console UI that lets you play the game end to end.

The current codebase is structured around a clear split between:
- domain logic such as combatants, actions, items, cooldowns, and effects
- battle coordination in the engine layer
- console interaction in the UI layer
- plain Java tests for domain, unit, and integration coverage

## What The Game Includes

- Two player classes:
  - `Warrior` with `Shield Bash`
  - `Wizard` with `Arcane Blast`
- Three difficulty levels:
  - `Easy`
  - `Medium`
  - `Hard`
- Starting item selection:
  - `Potion`
  - `Smoke Bomb`
  - `Power Stone`
- Speed-based turn order
- Backup enemy waves for levels that require them
- Status effects such as defend, stun, smoke bomb invulnerability, and Arcane Blast attack buffs
- A playable console flow for:
  - class selection
  - choosing two starting items
  - choosing difficulty
  - playing the battle
  - replaying with the same setup, starting a new game, or exiting


## Project Structure

```text
action/       Core battle actions such as attacks, defend, and special skills
effect/       Status effects and effect management
engine/       Battle loop, level setup, and battle coordination
item/         Inventory items and item behavior
model/        Core combatant, player, enemy, and cooldown models
strategy/     Turn order and enemy action selection strategies
tests/        Plain Java test suite
testsupport/  Lightweight testing utilities and scripted UI helpers
ui/           Console UI, game setup flow, and application entry point
util/         Shared utility logic such as damage calculation
```

## Running The Game

This project does not use Maven or Gradle. It can be compiled and run directly with `javac` and `java`.

Recommended: use Java 17 or newer.

From the repository root:


### Windows (PowerShell)

```
if (Test-Path build) {
    Get-ChildItem -LiteralPath build -Recurse -Force | ForEach-Object { $_.Attributes = 'Normal' }
    (Get-Item -LiteralPath build).Attributes = 'Normal'
    Remove-Item -Recurse -Force -LiteralPath build
}
New-Item -ItemType Directory -Force -Path build | Out-Null
javac -d build (Get-ChildItem action,effect,engine,item,model,strategy,ui,util -Recurse -Filter *.java | ForEach-Object FullName)
java -cp build ui.Main
```

### macOS / Linux (bash or zsh)

```bash
rm -rf build
mkdir build
javac -d build $(find action effect engine item model strategy ui util -name "*.java")
java -cp build ui.Main
```

## Current Status

At the current stage, the repository includes:
- a working battle engine
- a working console UI
- replay and new-game flow