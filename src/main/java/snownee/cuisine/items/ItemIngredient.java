package snownee.cuisine.items;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.cuisine.Cuisine;
import snownee.cuisine.CuisineRegistry;
import snownee.cuisine.api.Effect;
import snownee.cuisine.api.Form;
import snownee.cuisine.api.Ingredient;
import snownee.cuisine.api.Material;
import snownee.cuisine.client.model.IngredientMeshDefinition;
import snownee.cuisine.internal.CuisinePersistenceCenter;
import snownee.cuisine.proxy.ClientProxy;
import snownee.cuisine.util.I18nUtil;
import snownee.kiwi.client.AdvancedFontRenderer;
import snownee.kiwi.item.IModItem;
import snownee.kiwi.util.Util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ItemIngredient extends ItemFood implements IModItem
{
    public ItemIngredient()
    {
        super(1, false);
    }

    @Override
    public String getName()
    {
        return "ingredient";
    }

    @Override
    public void register(String modid)
    {
        setRegistryName(modid, getName());
        setTranslationKey(modid + "." + getName());
    }

    @Override
    public Item cast()
    {
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void mapModel()
    {
        ModelLoader.setCustomMeshDefinition(this, IngredientMeshDefinition.INSTANCE);
        ModelBakery.registerItemVariants(this, ClientProxy.EMPTY, new ResourceLocation(Cuisine.MODID, "cmaterial/cubed"), new ResourceLocation(Cuisine.MODID, "cmaterial/diced"), new ResourceLocation(Cuisine.MODID, "cmaterial/minced"), new ResourceLocation(Cuisine.MODID, "cmaterial/paste"), new ResourceLocation(Cuisine.MODID, "cmaterial/shredded"), new ResourceLocation(Cuisine.MODID, "cmaterial/sliced"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        if (stack.getTagCompound() == null)
        {
            return false;
        }
        Ingredient ingredient = CuisinePersistenceCenter.deserializeIngredient(stack.getTagCompound());
        if (ingredient == null)
        {
            return false;
        }
        return ingredient.getMaterial().hasGlowingOverlay(ingredient);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
    {
        NBTTagCompound data = stack.getTagCompound();
        if (data != null)
        {
            Ingredient ingredient = CuisinePersistenceCenter.deserializeIngredient(data);
            if (ingredient != null)
            {
                for (Effect effect : ingredient.getEffects())
                {
                    if (effect.showInTooltips())
                    {
                        tooltip.add(Util.color(effect.getColorForDisplay()) + I18n.format(effect.getName()));
                    }
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public FontRenderer getFontRenderer(ItemStack stack)
    {
        return AdvancedFontRenderer.INSTANCE;
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public String getItemStackDisplayName(ItemStack stack)
    {
        NBTTagCompound data = stack.getTagCompound();
        if (data == null)
        {
            return I18nUtil.translate("material.unknown");
        }
        else
        {
            Ingredient ingredient = CuisinePersistenceCenter.deserializeIngredient(data);
            return ingredient == null ? I18nUtil.translate("material.unknown") : ingredient.getTranslation();
        }
    }

    @Override
    public final void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        // No-op to avoid bad things from happening
    }

    public static ItemStack make(Material material, Form form)
    {
        return ItemIngredient.make(material, form, 1);
    }

    public static ItemStack make(Material material, Form form, float size)
    {
        return ItemIngredient.make(material, form, size, 1, form.getStandardActions());
    }

    public static ItemStack make(Material material, Form form, float size, int amount, int[] actions)
    {
        if (material.isValidForm(form))
        {
            ItemStack stack = new ItemStack(CuisineRegistry.INGREDIENT, amount);
            Ingredient ingredient = new Ingredient(material, form, size);
            NBTTagCompound data = CuisinePersistenceCenter.serialize(ingredient);
            data.setIntArray(KEY_ACTIONS, actions);
            stack.setTagCompound(data);
            return stack;
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    public static ItemStack make(Ingredient ingredient)
    {
        ItemStack itemStack = new ItemStack(CuisineRegistry.INGREDIENT);
        itemStack.setTagCompound(CuisinePersistenceCenter.serialize(ingredient));
        return itemStack;
    }

    public static int[] getActions(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null || !tag.hasKey(KEY_ACTIONS, Constants.NBT.TAG_INT_ARRAY))
        {
            if (stack.getItem() == CuisineRegistry.INGREDIENT)
            {
                Cuisine.logger.warn("Found invalid ItemIngredient: {}", stack);
            }
            return new int[2];
        }
        return tag.getIntArray(KEY_ACTIONS);
    }

    public static List<ItemStack> getAllValidFormsWithException(Material material, EnumSet<Form> exceptions)
    {
        EnumSet<Form> forms = EnumSet.complementOf(exceptions);
        forms.retainAll(material.getValidForms());
        return forms.stream().map(form -> make(material, form)).collect(Collectors.toList());
    }

    public static final String KEY_ACTIONS = "actions";
}
