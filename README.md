# Progression Overhaul (Fabric 1.21.4)

A gameplay-focused Fabric mod that reshapes early progression with new items, blocks, recipes, and mechanics. Built for Minecraft 1.21.4 using Fabric Loom and Yarn mappings, with Java 21.

## Features
- New early-game items: pebbles (stone variants), plant fiber, bark, loose dirt, sharp stone.
- New block: Clay Furnace (custom UI, fuel rules, and processing behavior).
- Custom recipes and tags for early progression and smithing upgrade templates.
- Advancements aligned to new recipes and progression (JSON under `data/poh/advancement/`).
- Loot table and drop behavior tweaks via mixins and registry hooks.
- Modular architecture: clear separation for items, blocks, recipes, screens, components, world hooks, and mixins.

## Requirements
- Java 21
- Gradle (wrapper included)
- Minecraft 1.21.4, Fabric Loader `0.16.14`

## Getting Started
- Clone the repository
- Open in your IDE (IntelliJ recommended). Ensure JDK 21 is used.
- Let Loom resolve dependencies on first sync.

### Build
- Build the mod JAR (includes remapped sources):

```bash
./gradlew build
```

The distributable JAR will be in `build/libs/`.

### Run in Dev
- Launch a dev client with the mod loaded:

```bash
./gradlew runClient
```

### Data Generation (optional)
The project includes data generators for models, loot, tags, and recipes. If you opt into datagen, prefer committing hand-authored JSON only where needed and let the generator produce the rest to avoid duplication with `src/main/generated/`.

## Project Structure
```
src/main/java/net/ohacd/poh/
  ProgressionOverhaul.java           # Main entrypoint
  ProgressionOverhaulClient.java     # Client entrypoint (screens)
  block/                             # Blocks + custom implementations
  block/entity/                      # Block entities + registration
  command/                           # Commands and registration
  component/                         # Cardinal Components + events
  datagen/                           # Data providers (tags, loot, models, recipes)
  init/                              # Centralized registries (items, blocks, recipes, etc.)
  item/                              # Items + materials + smithing templates
  mixin/                             # Mixins (crafting, ore drops, smithing, etc.)
  recipe/                            # Custom recipe classes and types
  screen/                            # Screen handlers and client screens
  util/                              # Utilities, tags, helpers
  world/                             # World hooks and logic

src/main/resources/
  assets/poh/                        # Models, textures, lang
  data/minecraft/                    # Loot tables, recipes (vanilla namespace overrides)
  data/poh/                          # Recipes, advancements, tags, loot (mod namespace)
  fabric.mod.json                    # Mod metadata and entrypoints
```

## Notable Systems
- Clay Furnace
  - Block: `block/custom/ClayFurnaceBlock.java`
  - Block Entity: `block/entity/custom/ClayFurnaceBlockEntity.java`
  - Screen + Handler: `screen/custom/ClayFurnaceScreen.java`, `screen/custom/ClayFurnaceScreenHandler.java`
  - Recipes: custom type/inputs in `recipe/` and JSON under `data/poh/recipe/`
  - Fuel restricted via `data/poh/tags/item/clay_fuel.json`

- Items and Blocks
  - Items registered in `item/ModItems.java`
  - Blocks registered in `block/ModBlocks.java`

- Screens and Block Entities
  - Screen handler keys: `screen/ModScreenHandlers.java`
  - Block entities: `block/entity/ModBlockEntities.java`

- Advancements
  - JSON under `data/poh/advancement/` (fixed from `advancment/`)

- Mixins
  - Defined in `poh.mixins.json`, sources under `mixin/`
  - Example: `SmithingScreenHandlerMixin` for custom addition counts.

## Development Notes
- Java 21 is enforced in `build.gradle` and `gradle.properties`.
- Yarn mappings and Fabric API versions are pinned for 1.21.4; see `gradle.properties`.
- Use centralized registration (`init/ModRegistries.java`) to keep side effects orderly.
- Client-only code should be kept in the client entrypoint and client packages.

## Common Tasks
- Bump Minecraft/Fabric versions: update `gradle.properties` (`minecraft_version`, `yarn_mappings`, `loader_version`, `fabric_version`, `loom_version`).
- Add a new item:
  - Define in `item/ModItems.java`
  - Add textures and `assets/poh/models/item/<id>.json`
  - Add localization to `assets/poh/lang/en_us.json`
  - Add recipes/advancements as needed under `data/poh/`

- Add a new block:
  - Register in `block/ModBlocks.java`
  - Add `BlockItem` via `registerBlockItem` helper
  - Provide models (`assets/poh/models/block` + `item`) and blockstate JSON
  - Add loot table and tags if needed

## Troubleshooting
- Advancements not loading: ensure the folder is `data/poh/advancement/` (correct spelling) and JSON validates.
- Datagen conflicts: avoid duplicating identical JSON in both `src/main/resources` and `src/main/generated`.
- Mixin target changes: check Yarn mappings for 1.21.4 if a mixin fails; update injector targets accordingly.

## License
This project is licensed under the MIT License. See `LICENSE`.
