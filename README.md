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

## Assignment Context

The project is based on the SC2002 turn-based combat assignment, so the game is centered around a single-player battle against enemy waves with class-specific skills and limited-use items.

The main assignment-facing ideas reflected in this repository are:
- separation of UI from battle logic
- reusable object-oriented domain classes
- turn-based combat with status effects and cooldowns
- multiple difficulty levels with different enemy compositions
- test coverage for both isolated logic and full battle flow

## Important Repo Note

This repository currently uses one intentional gameplay rule that differs from the assignment examples:

- Special skill cooldown advances on every player turn, unconditionally.

That means cooldown still ticks:
- on a normal attack turn
- on a defend turn
- on an item turn
- on a `Power Stone` turn
- on a skipped or stunned player turn
- on a real special skill turn after the skill resets cooldown

So in this repo, `Power Stone` still gives a free special-skill effect, but the turn itself still advances cooldown like any other player turn.

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

From the repository root in PowerShell:

```powershell
if (Test-Path build) {
    Get-ChildItem -LiteralPath build -Recurse -Force | ForEach-Object { $_.Attributes = 'Normal' }
    (Get-Item -LiteralPath build).Attributes = 'Normal'
    Remove-Item -Recurse -Force -LiteralPath build
}
New-Item -ItemType Directory -Force -Path build | Out-Null
javac -d build (Get-ChildItem action,effect,engine,item,model,strategy,ui,util -Recurse -Filter *.java | ForEach-Object FullName)
java -cp build ui.Main
```

## Running The Tests

The project uses a plain Java test harness under `tests/` and `testsupport/`.

From the repository root in PowerShell:

```powershell
if (Test-Path build-tests) {
    Get-ChildItem -LiteralPath build-tests -Recurse -Force | ForEach-Object { $_.Attributes = 'Normal' }
    (Get-Item -LiteralPath build-tests).Attributes = 'Normal'
    Remove-Item -Recurse -Force -LiteralPath build-tests
}
New-Item -ItemType Directory -Force -Path build-tests | Out-Null
javac -d build-tests (Get-ChildItem action,effect,item,model,strategy,engine,ui,util,tests,testsupport -Recurse -Filter *.java | ForEach-Object FullName)
java -cp build-tests tests.TestMain
```

The current suite covers:
- cooldown behavior
- action and item behavior
- inventory and status effect management
- player and strategy wiring
- level construction
- battle engine integration flow
- console UI setup and prompt handling

## Main Entry Points

- Play the game: `ui.Main`
- Run the test suite: `tests.TestMain`

## Current Status

At the current stage, the repository includes:
- a working battle engine
- a working console UI
- replay and new-game flow
- plain Java automated tests across the main gameplay layers

What is still typically left for assignment submission work is non-code deliverables such as UML diagrams, sequence diagrams, and the final report/documentation package if those are required by your submission format.
