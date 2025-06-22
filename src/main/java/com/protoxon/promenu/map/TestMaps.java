package com.protoxon.promenu.map;

import com.protoxon.promenu.ProMenu;
import com.protoxon.promenu.database.Database;
import com.protoxon.promenu.menus.search.tags.SearchTag;

import java.util.List;

public class TestMaps {

    public static void addMaps() {
        Database.maps.addMapToDatabase(new Map("Would You Rather", "Command Realm", SearchTag.PUZZLE, List.of("choices", "questions", "logic"), 29, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Chunk Runner", "NeoMC", SearchTag.PARKOUR, List.of("quick", "chunk"), 4, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Makers Wars", "MineMakers Team", SearchTag.PVP, List.of("skywars", "islands"), 41, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Makers Party", "MineMakers Team", SearchTag.NONE, List.of("variety", "party", "random"), 14, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Meteor Miners", "NeoMC", SearchTag.PUZZLE, List.of("mining", "asteroids", "race"), 16, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Missile Wars", "Supersette", SearchTag.PVP, List.of("flying machine", "redstone", "piston", "explosions", "tnt", "battle"), 50, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Combat Cube", "NeoMC", SearchTag.PVP, List.of("arena", "cube", "combat"), 20, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Blastoff", "NeoMC", SearchTag.NONE, List.of("space", "rockets", "jump"), 9, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Phasmophobia", "NeoMc", SearchTag.HORROR, List.of("ghost", "scary", "haunt", "investigation"), 43, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Makers Spleef", "MineMakers Team", SearchTag.PVP, List.of("spleef", "arena", "snow"), 11, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Makers Punch", "MineMakers Team", SearchTag.PVP, List.of("punch", "knockback", "arena"), 5, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Block Hunt", "protoxon", SearchTag.PUZZLE, List.of("hide and seek", "search"), 30, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Wildfire", "CloudWolf", SearchTag.NONE, List.of("fire", "forest", "danger"), 37, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Biome Run", "Supersette", SearchTag.PARKOUR, List.of("biome", "run", "speed"), 3, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Lava Escape", "protoxon", SearchTag.PARKOUR, List.of("lava", "escape", "climb"), 28, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Crafty Monkeys", "MineMakers Team", SearchTag.PUZZLE, List.of("monkeys", "crafting", "puzzle"), 18, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Elytra Rush", "TheblueMan003", SearchTag.PARKOUR, List.of("elytra", "flight", "race"), 46, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Phantom Run", "iWacky", SearchTag.PARKOUR, List.of("phantom", "run", "night"), 24, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Pixel Party", "DutchCommanderMC", SearchTag.NONE, List.of("colors", "party", "tiles"), 7, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Death Run", "FeatureFire", SearchTag.PARKOUR, List.of("trap", "death", "runner", "obstacles"), 26, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Duel TNT", "ChainsawNinja", SearchTag.PVP, List.of("tnt", "duel", "explosion"), 33, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Guess My Drawing", "TheblueMan003", SearchTag.PUZZLE, List.of("drawing", "guess", "art"), 39, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Dimensional Rush", "TheblueMan003", SearchTag.PARKOUR, List.of("dimension", "rush", "portal"), 25, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Blocket League", "SethBling", SearchTag.NONE, List.of("soccer", "car"), 1, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Kitty Kombat", "SlimeLabs", SearchTag.PVP, List.of("cheeto, cat, kitty, gun, shoot, fight, arena, projectile, funny, meow"), 38, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("SkyKnock", "SkyGames", SearchTag.PVP, List.of("sky", "knockback", "void"), 8, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Escape the Facility", "MapleMC", SearchTag.PUZZLE, List.of("escape", "lab", "maze"), 12, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Zombie Bunker", "DeadZone Devs", SearchTag.HORROR, List.of("zombie", "waves", "defend", "bunker"), 21, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Slime Sumo", "SlimeLabs", SearchTag.PVP, List.of("slime", "sumo", "arena"), 35, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Color Clash", "DutchCommanderMC", SearchTag.PUZZLE, List.of("color", "match", "reaction"), 40, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Rocket Riders", "Command Realm", SearchTag.PVP, List.of("rockets", "battle", "flight"), 44, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("DropZone", "NeoMC", SearchTag.PARKOUR, List.of("dropper", "fall", "precision"), 42, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Haunted Depths", "iWacky", SearchTag.HORROR, List.of("ghost", "cave", "dark", "creepy"), 32, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Totem Trouble", "MineMakers Team", SearchTag.PUZZLE, List.of("totem", "swap", "puzzle"), 47, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Parkour Spiral", "Hielke", SearchTag.PARKOUR, List.of("spiral", "climb", "jump"), 22, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("One in the Chamber", "InfinityMinigames", SearchTag.PVP, List.of("bow", "instakill", "chamber"), 27, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Button Chaos", "RedstoneDreams", SearchTag.PUZZLE, List.of("buttons", "memory", "challenge"), 36, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Dragon Dash", "TheblueMan003", SearchTag.PARKOUR, List.of("dragon", "flight", "speed"), 6, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Abandoned Asylum", "HorrorBlocks", SearchTag.HORROR, List.of("asylum", "haunted", "psychological"), 31, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Beacon Battle", "SethBling", SearchTag.PVP, List.of("beacon", "defend", "team", "destroy"), 10, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Glider Gauntlet", "ElytraWorks", SearchTag.PARKOUR, List.of("elytra", "rings", "fly"), 0, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Paintball Panic", "MiniSquad", SearchTag.PVP, List.of("paintball", "color", "gun"), 34, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Time Trials", "NeoMC", SearchTag.PARKOUR, List.of("speedrun", "checkpoints", "race"), 13, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Boulder Bash", "SlimeLabs", SearchTag.PUZZLE, List.of("push", "boulder", "obstacle"), 17, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Spooky Sprint", "GhostlyGames", SearchTag.HORROR, List.of("run", "fear", "chase", "dark"), 15, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("The Floor is Lava", "Command Realm", SearchTag.PARKOUR, List.of("lava", "jump", "platform"), 19, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Minecart Mayhem", "RollerCrafters", SearchTag.NONE, List.of("minecart", "ride", "chaos"), 23, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("BoomBox Battle", "TNTGames", SearchTag.PVP, List.of("tnt", "arena", "blast"), 48, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Cursed Carnival", "HorrorCraft", SearchTag.HORROR, List.of("carnival", "clown", "cursed"), 2, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Build & Break", "BuildBattlePro", SearchTag.PUZZLE, List.of("build", "destroy", "strategy"), 49, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Sky Sprint", "SkyWorks", SearchTag.PARKOUR, List.of("air", "run", "islands"), 45, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Rage Parkour", "NeoMC", SearchTag.PARKOUR, List.of("rage", "difficult", "precision"), 9, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Trap Tactics", "RedstoneTricksters", SearchTag.PUZZLE, List.of("traps", "logic", "timing"), 43, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Island Duel", "PVPX", SearchTag.PVP, List.of("1v1", "island", "fight"), 4, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Speed Builders", "Cubed Creations", SearchTag.PUZZLE, List.of("memory", "build", "speed"), 20, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Tower Tumble", "BlockBashers", SearchTag.PVP, List.of("tower", "fall", "fight", "arena"), 6, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Maze of Madness", "WickedMazes", SearchTag.PUZZLE, List.of("maze", "insane", "twist", "confusing"), 38, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Red Light, Green Light", "Squidcraft", SearchTag.PUZZLE, List.of("stop", "go", "reaction", "timing"), 17, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Obsidian Outpost", "NeoMC", SearchTag.PVP, List.of("obsidian", "siege", "base", "attack"), 36, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Wool Wars", "WoollyDev", SearchTag.PVP, List.of("wool", "colors", "teams", "capture"), 44, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Elevator Escape", "Command Realm", SearchTag.PUZZLE, List.of("escape", "elevator", "mechanism", "redstone"), 27, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Ghost Tag", "SlimeLabs", SearchTag.HORROR, List.of("ghost", "tag", "invisible", "run"), 14, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Color Memory", "DutchCommanderMC", SearchTag.PUZZLE, List.of("memory", "tiles", "match"), 25, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Sky Snipers", "CloudWolf", SearchTag.PVP, List.of("archery", "sky", "snipe", "long range"), 8, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Hallway Horror", "HorrorCraft", SearchTag.HORROR, List.of("hallway", "jumpscare", "ghost", "flashlight"), 39, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Mob Arena X", "EnderByte", SearchTag.PVP, List.of("arena", "mobs", "waves", "boss"), 13, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Build Battle", "Hypixel", SearchTag.PUZZLE, List.of("creative", "theme", "vote", "contest"), 18, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Slime Climb", "SlimeLabs", SearchTag.PARKOUR, List.of("slime", "bounce", "vertical", "race"), 46, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Button Brawl", "RedstoneDreams", SearchTag.PVP, List.of("button", "arena", "tactical", "reaction"), 10, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Haunted Hideout", "iWacky", SearchTag.HORROR, List.of("hide", "ghost", "dark", "shadows"), 7, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Treasure Trackers", "MineMakers Team", SearchTag.PUZZLE, List.of("treasure", "hunt", "search", "clues"), 11, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Frost Fight", "ColdCraft", SearchTag.PVP, List.of("snowball", "ice", "winter", "duel"), 15, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Lava Leap", "ParkourCraft", SearchTag.PARKOUR, List.of("lava", "jump", "hot", "danger"), 22, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Totem Defense", "Protoxon", SearchTag.PVP, List.of("totem", "base", "waves", "teamwork"), 35, MapType.MINIGAMES), MapType.MINIGAMES);
        Database.maps.addMapToDatabase(new Map("Shadow Sprint", "DarkWolf", SearchTag.PARKOUR, List.of("dark", "speed", "sprint", "night"), 3, MapType.MINIGAMES), MapType.MINIGAMES);


        /*
        for (int i = 1; i <= 160; i++) {
            String name = "Test Map " + i;
            String creator = switch (i % 4) {
                case 0 -> "Protoxon";
                case 1 -> "MineMakers";
                case 2 -> "BlockDev";
                default -> "RedstoneCo";
            };
            SearchTag tag = switch (i % 3) {
                case 0 -> SearchTag.PVP;
                case 1 -> SearchTag.PUZZLE;
                default -> SearchTag.PARKOUR;
            };
            List<String> keywords = switch (tag) {
                case PVP -> List.of("combat", "fight", "duel");
                case PUZZLE -> List.of("logic", "riddle", "brain");
                case PARKOUR -> List.of("jump", "speed", "course");
                default -> List.of("misc");
            };
            MapRegistry.addMap(new Map(name, creator, tag, keywords));
        }

         */
    }
}
