package snownee.cuisine;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Cuisine.MODID, name = Cuisine.MODID, category = "")
@Mod.EventBusSubscriber(modid = Cuisine.MODID)
public final class CuisineConfig
{

    private CuisineConfig()
    {
        throw new UnsupportedOperationException("No instance for you");
    }

    @SubscribeEvent
    public static void onConfigReload(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(Cuisine.MODID))
        {
            ConfigManager.sync(Cuisine.MODID, Config.Type.INSTANCE);
        }
    }

    @Config.Comment("General settings of Cuisine.")
    @Config.LangKey("cuisine.config.general")
    @Config.Name("General")
    public static final General GENERAL = new General();

    public static final class General
    {
        General()
        {
            // No-op. Package-level access.
        }

        @Config.Comment("Automatically unlock all recipes for all newly spawned players.")
        @Config.LangKey("cuisine.config.general.auto_recipe_unlocking")
        @Config.Name("AutoRecipeUnlocking")
        public boolean autoRecipeUnlocking = true;

        @Config.Comment("Give player the manual of Cuisine Mod when they firstly spawn in world.")
        @Config.LangKey("cuisine.config.general.spawn_book")
        @Config.Name("SpawnBook")
        @Config.RequiresMcRestart
        public boolean spawnBook = true;

        @Config.LangKey("cuisine.config.general.fruit_growing_speed")
        @Config.Name("FruitGrowingSpeed")
        @Config.RangeInt(min = 0, max = 100)
        public int fruitGrowingSpeed = 15;

        @Config.LangKey("cuisine.config.general.fruit_drops")
        @Config.Name("FruitDrops")
        public boolean fruitDrops = true;

        @Config.Name("PassableLeaves")
        public boolean passableLeaves = false;

        @Config.Comment("If true, bamboo can be used as a blowpipe which can shoot seeds.")
        @Config.LangKey("cuisine.config.general.bamboo_blowpipe")
        @Config.Name("BambooBlowpipe")
        @Config.RequiresMcRestart
        public boolean bambooBlowpipe = false;

        @Config.Comment("Allow axes to chop things on chopping board. Provided for mod pack creators.")
        @Config.LangKey("cuisine.config.progression.axe_chopping")
        @Config.Name("AxeChopping")
        public boolean axeChopping = true;

        @Config.Name("AxeChoppingPlanksOutput")
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 1, max = 64)
        public int axeChoppingPlanksOutput = 6;

        @Config.Name("AxeChoppingStickOutput")
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 1, max = 64)
        public int axeChoppingStickOutput = 4;

        @Config.Comment("Length of one mill working cycle, measured in ticks. Ideally, there are 20 ticks in 1 second.")
        @Config.LangKey("cuisine.config.general.mill_work_cycle")
        @Config.Name("MillWorkCycle")
        @Config.RangeInt(min = 5)
        public int millWorkPeriod = 160;

        @Config.Comment("If true, natural sunlight will be considered as a valid heat source for earthen basin.")
        @Config.LangKey("cuisine.config.general.basin_heating_in_daylight")
        @Config.Name("BasinHeatingInDaylight")
        @Config.RequiresMcRestart
        public boolean basinHeatingInDaylight = true;

        @Config.Name("BasinConvertingConcrete")
        @Config.RequiresMcRestart
        public boolean basinConvertingConcrete = true;

        @Config.Name("EnableSqueezer")
        public boolean enableSqueezer = true;

        @Config.Name("SqueezerUsesFE")
        @Config.RequiresMcRestart
        public int squeezerUsesFE = 0;

        @Config.Name("DrinkroUsesFE")
        @Config.RequiresMcRestart
        public int drinkroUsesFE = 0;

        @Config.Name("BasicSeedsWeight")
        @Config.RequiresMcRestart
        public int basicSeedsWeight = 5;

        @Config.Name("BetterHarvest")
        @Config.RequiresMcRestart
        public boolean betterHarvest = true;

        @Config.Name("BetterHarvestBlacklist")
        @Config.RequiresMcRestart
        public String[] betterHarvestBlacklist = {};

        @Config.Name("AttachWaterBottleCapability")
        @Config.Comment("one = 250mB")
        @Config.RequiresMcRestart
        public boolean attachWaterBottleCapability = true;

        @Config.Name("WinePotionDurationModifier")
        public float winePotionDurationModifier = 1F;

        @Config.Name("JuiceSqueezingAmount")
        @Config.Comment("Juice squeezing amount in mB")
        public int juiceSqueezingAmount = 250;

        @Config.Name("RareCrops")
        public boolean rareCrops = true;
    }

    @Config.Comment("Config options of Cuisine Hardcore Module.")
    @Config.LangKey("cuisine.config.hardcore")
    @Config.Name("Hardcore")
    public static final Hardcore HARDCORE = new Hardcore();

    public static final class Hardcore
    {
        Hardcore()
        {
            // No-op, package-level access.
        }

        @Config.Comment("If true, hardcore module will be turned on.")
        @Config.LangKey("cuisine.config.hardcore.enable")
        @Config.Name("Enable")
        @Config.RequiresMcRestart
        public boolean enable = true;

        @Config.Comment("If true, bread will require mill to make.")
        @Config.LangKey("cuisine.config.hardcore.bread")
        @Config.Name("HardcoreBread")
        @Config.RequiresMcRestart
        public boolean harderBreadProduction = true;

        @Config.Comment("If true, cookie will require furnace to make.")
        @Config.LangKey("cuisine.config.hardcore.cookie")
        @Config.Name("HardcoreCookie")
        @Config.RequiresMcRestart
        public boolean harderCookieProduction = true;

        @Config.Comment("If true, sugar will require mill to make.")
        @Config.LangKey("cuisine.config.hardcore.sugar")
        @Config.Name("HardcoreSugar")
        @Config.RequiresMcRestart
        public boolean harderSugarProduction = true;

        @Config.Comment("If true, certain foods will give you fewer heal amount and saturation.")
        @Config.LangKey("cuisine.config.hardcore.food_level_lose")
        @Config.Name("HardcoreFoodLevel")
        @Config.RequiresMcRestart
        public boolean lowerFoodLevel = true;

        @Config.Comment(
            "If HardcoreFoodLevel is enabled, this will determine how much heal amount and saturation you can still get."
        )
        @Config.LangKey("cuisine.config.hardcore.food_level_retain_ratio")
        @Config.Name("FoodLevelRetainRatio")
        public double foodLevelRetainRatio = 0.5;

        @Config.Name("DisableGenericFood")
        @Config.RequiresMcRestart
        public boolean disableGenericFood = false;

        @Config.Comment("If HardcoreFoodLevel is enabled, food item that is listed here will NOT be affected.")
        @Config.LangKey("cuisine.config.hardcore.food_level_lose_blacklist")
        @Config.Name("FoodLevelDowngradeBlacklist")
        public String[] lowerFoodLevelBlacklist = new String[0];

        @Config.Comment("If true, player will lose culinary skill points when they died.")
        @Config.LangKey("cuisine.config.general.skill_lose_on_death")
        @Config.Name("HardcoreCulinarySkill")
        public boolean loseSkillPointsOnDeath = false;

        @Config.Comment(
            { "If Culinary Skill Downgrade on Death is enabled, this will determines how many point are kept.", "For example, 0.5 means 50% are kept." }
        )
        @Config.LangKey("cuisine.config.general.skill_retain_ratio")
        @Config.Name("CulinarySkillRetainRatio")
        @Config.RangeDouble(min = 0, max = 1)
        public double skillPointsRetainRatio = 1.0;

        @Config.Name("BadSkillPunishment")
        public boolean badSkillPunishment = true;

    }

    @Config.Comment("Configurable variables related to world generation")
    @Config.LangKey("cuisine.config.world_gen")
    @Config.Name("WorldGen")
    public static final WorldGen WORLD_GEN = new WorldGen();

    public static final class WorldGen
    {
        @Config.Comment(
            "Generation rate of fruit trees; larger value means higher generation rate; set to 0 to disable."
        )
        @Config.LangKey("cuisine.config.general.fruit_trees_gen_rate")
        @Config.Name("FruitTreesGenWeight")
        @Config.RangeInt(min = 0, max = 100)
        @Config.RequiresMcRestart
        public int fruitTreesGenRate = 50;

        @Config.Name("FruitTreesGenDimensions")
        @Config.RequiresMcRestart
        public int[] fruitTreesGenDimensions = new int[] { 0 };

        @Config.Comment("Generation rate of bamboo; larger value means higher generation rate; set to 0 to disable.")
        @Config.LangKey("cuisine.config.general.bamboos_gen_rate")
        @Config.Name("BamboosGenWeight")
        @Config.RangeInt(min = 0, max = 100)
        @Config.RequiresMcRestart
        public int bamboosGenRate = 6;

        @Config.Name("BamboosGenDimensions")
        @Config.RequiresMcRestart
        public int[] bamboosGenDimensions = new int[] { 0 };

        @Config.Name("LegacyBamboosGen")
        public boolean legacyBambooGen = false;

        @Config.Comment(
            "Generation rate of wild crops; larger value means higher generation rate; set to 0 to disable."
        )
        @Config.LangKey("cuisine.config.general.crops_gen_rate")
        @Config.Name("CropsGenWeight")
        @Config.RangeInt(min = 0, max = 100)
        @Config.RequiresMcRestart
        public int cropsGenRate = 40;

        @Config.Name("CropsGenDimensions")
        @Config.RequiresMcRestart
        public int[] cropsGenDimensions = new int[] { 0 };
    }

    @Config.Name("Client")
    public static final Client CLIENT = new Client();

    public static final class Client
    {
        @Config.Comment("List of axes that will show in JEI recipes. Does not affect chopping board behavior.")
        @Config.LangKey("cuisine.config.progression.axe_list")
        @Config.Name("AxeList")
        @Config.RequiresMcRestart
        public String[] axeList = new String[] { "minecraft:wooden_axe", "minecraft:stone_axe", "minecraft:iron_axe", "minecraft:golden_axe", "minecraft:diamond_axe" };

        @Config.LangKey("cuisine.config.general.always_render_drinkro")
        @Config.Comment("Rendering contents in all directions. WIP feature.")
        @Config.Name("AlwaysRenderDrinkro")
        public boolean alwaysRenderDrinkro = false;

        @Config.Name("ShowHoloGui")
        public boolean showHoloGui = true;

        @Config.Name("ShowTooltipIcons")
        public boolean showTooltipIcons = true;
    }

    @Config.Name("Compat")
    public static final Compat COMPAT = new Compat();

    public static final class Compat
    {
        @Config.Name("NormalNutrientModifier")
        public float normalNutrientModifier = 0.5f;

        @Config.Name("SupernaturalNutrientModifier")
        public float supernaturalNutrientModifier = 0.2f;
    }
}
