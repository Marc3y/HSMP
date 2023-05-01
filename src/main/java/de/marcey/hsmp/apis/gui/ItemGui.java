package de.marcey.hsmp.apis.gui;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemGui {

    private Gui gui;
    private Material material;
    private boolean enchant;
    private List<String> lore;
    private String displayname;
    private int amount;
    private List<ItemFlag> itemFlagsToRemove = new ArrayList<>();
    private List<ItemFlag> itemFlagToAdd = new ArrayList<>();

    public ItemGui(Gui gui){
        this.enchant = false;
        this.amount = 1;
        this.gui = gui;
        this.lore = new ArrayList<>();
    }

    public ItemGui thatHasDisplayname(String displayname){
        this.displayname = displayname;
        return this;
    }

    public ItemGui thatHasMaterial(Material material){
        this.material = material;
        return this;
    }

    public ItemGui thatIsEnchant(boolean enchant){
        this.enchant = enchant;
        return this;
    }
    public ItemGui thatAddsLore(String lore){
        this.lore.add(lore);
        return this;
    }

    public ItemGui thatAddsLore(String... lore){
        for(String l : lore) {
            this.lore.add(l);
        }
        return this;
    }
    public ItemGui thatHasAmount(int amount){
        this.amount = amount;
        return this;
    }

    public ItemGui thatHasItemFlag(ItemFlag itemFlag){
        this.itemFlagToAdd.add(itemFlag);
        return this;
    }

    public ItemGui thatRemovesItemFlag(ItemFlag itemFlag){
        this.itemFlagsToRemove.remove(itemFlag);
        return this;
    }

    public ItemStack getItemStack(){
        ItemStack stack = new ItemStack(this.material, this.amount);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(this.displayname);
        meta.setLore(this.lore);
        if(this.enchant){
            meta.addEnchant(Enchantment.DURABILITY, 3, true);
        }
        for(ItemFlag flag : itemFlagToAdd){
            meta.addItemFlags(flag);
        }
        for(ItemFlag flag : itemFlagsToRemove){
            meta.removeItemFlags(flag);
        }
        stack.setItemMeta(meta);
        return stack;
    }


    public Gui create(int slot){
        this.gui.withItemStack(this, slot);
        return this.gui;
    }

    public Gui create(int... slots){
        List<Integer> ints = new ArrayList<>();
        for(int i : slots){
            ints.add(i);
        }
        this.gui.withItemStack(this, ints);
        return this.gui;
    }

    public Gui create(ForValue... slots){
        List<Integer> ints = new ArrayList<>();
        for(ForValue value : slots){
            for(int i = value.getFrom(); i < value.getTo(); i++){
                ints.add(i);
            }
        }
        this.gui.withItemStack(this, ints);
        return this.gui;
    }


    public Gui create(List<Integer> slots){
        this.gui.withItemStack(this, slots);
        return this.gui;
    }


    public Gui createForAllRemaining(){
        List<Integer> ints = new ArrayList<>();
        for(Map.Entry<Integer, ItemStack> i : this.gui.itemStacks.entrySet()){
            ints.add(i.getKey());
        }
        for(int i = 0; i < this.gui.getSize(); i++){
            if(!ints.contains(i)){
                this.gui.withItemStack(this, i);
            }
        }
        return this.gui;
    }

}
