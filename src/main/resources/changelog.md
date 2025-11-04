# Progression Overhaul v0.1.4 (Beta) – Full Changelog
_Every Feature. Every Detail._

---

## 1. Fatigue System
**Mechanics**
- Tracks player fatigue from mining, combat, and crafting.
- **Increases** when:
    - Mining Blocks
    - Fighting mobs
- **Decreases** when:
    - Sleeping in beds
    - Resting near campfires

**Status Effects**
- **High Fatigue** (≥ 0.9): Blindness, Hunger, Slowness
- **Low Fatigue** (≤ 0.2): Speed, Haste

**Implementation**
- Custom `FatigueComponent` for client/server sync
- Tick-based updates every 20 ticks (1 second)
- Integrated with smithing recipes (e.g., chainmail → iron armor upgrade) (WIP)

---

## 2. Custom Crafting System
**Recipes**
- **Pebble Crafting**
    - Pebbles → Cobblestone, Granite, Diorite, Andesite and vise versa
    - Cobblestone + Stick + Plant Fiber → Stone tools
- **Plant Fiber**
    - Dry Grass + Dry Grass → Plant Fiber
    - Plant Fiber + Stick + Cobblestone→ Wooden tools
- **Smithing Templates**
    - Iron Upgrade Template: Chainmail → Iron armor (3–4 iron ingots)
    - 100% chance to spawn in a Blacksmith chest, and a chance to spawn in various structures
- **Axe-Cutting Recipes**
    - Logs + Axe → Stripped Logs -> Wood
    - Supported: Oak, Birch, Spruce, Jungle, etc.
    - Bark drops as a byproduct (random chance)
- **Clay Furnace Recipes**
    - Raw Iron/Gold → Ingots
    - Fuels: Logs, sticks, bark, and Wood type items/blocks in general

---

## 3. Clay Furnace Block
**Features**
- Custom block entity for smelting raw materials
- Fuels: Logs, sticks, bark
- **GUI**: Smelting progress bar + fuel indicator

**Recipe**

---

## 4. POI (Point of Interest) System
**Mechanics**
- Tracks campfires, villages, and structures

**Triggers**
- Fatigue reduction near campfires
- Advancements for crafting key items (WIP)

**Implementation**
- **POI Index**: Scans chunks for campfires/villages; data persists to disk
- **Triggers**:
    - `CampfireWarmthManager` – Manages message display and fatigue reduction
    - Triggers and storing POIs are handled by the rest of the World Interaction engine

---

## 5. Custom Items
- **Pebbles** – crafted from stone; used in stone tools and recipes
- **Plant Fiber** – crafted from dry grass; used for early tools and smithing templates, a random drop from breaking grass
- **Smithing Templates** – Iron upgrade template (chainmail → iron armor, 3–4 iron ingots)

---

## 6. Mixin Enhancements
- **Ore Drops**:
    - Iron/Gold ore drops raw ore instead of ingots
    - Smelted in clay furnace
- **Tool Efficiency**:
    - Axe-cutting recipes for stripped logs
    - Bark drops as a byproduct

---

## 7. Advancements (WIP)
- **Pebble Crafting** – “Craft 4 pebbles from cobblestone”
- **Smithing** – “Upgrade chainmail to iron armor”
- **Clay Furnace** – “Smelt raw iron into ingots”

---

## 8. Known Issues
- Fatigue effects may be too harsh early-game
- Clay furnace fuel efficiency needs balancing
- POI indexing may cause lag in large worlds
- POI indexing may lag or prevent world load on restart when using c2me
- clay furnace lose contents when unloaded
- Mod is still in early development be sure to report all bugs to the repository, I appreciate you all :D
- The Mod is Currently incompatible with C2ME
- The game sometimes get Stuck on the world loading screen after entering a world then leaving and trying to enter an other one

---

## 9. Future Plans
- Fatigue system: Add a forward facing interface for the player to directly interact with the system, its more of a
backbone atm
- POI triggers:
    - Village-based crafting boosts
    - Gain xp when entering new locations 
    - Rumor system for introducing dynamic hints about the world 
    - Story system as the main forward facing narration esc system  
- Clay Furnace:
    - Rework the entire Clay Furnace block, block entity, fuel types and how it works 
    - Fix all present bugs

---

## 10. Technical Details
- **Code Structure**:
    - Custom recipes (`AxeCuttingRecipe`, `ClayFurnaceRecipe`, `CountedSmithingRecipe`)
- **Data Generation**:
    - Recipes (pebble crafting, smithing templates)
    - Advancements (pebbles, iron upgrade) (WIP)

---

## 11. License
- MIT License – redistribution allowed with license notice

---

## 12. Installation
1. Download the mod JAR
2. Place in `mods/` folder
3. Launch with **Fabric Loader**

---

## 13. Support
- Report bugs or request features on GitHub "https://github.com/OhACD/ProgressionOverhaul-1.21.4"
- Join the community for updates and feedback

---

**Progression Overhaul v0.1.4** 
redefines early-game survival with deeper crafting, meaningful fatigue mechanics,
and a focus on exploration—all while preserving a familiar vanilla feel.
