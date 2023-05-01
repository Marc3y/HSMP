package de.marcey.hsmp.apis.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gui {

    private String displayname;
    private int size;
    public HashMap<Integer, ItemStack> itemStacks = new HashMap<>();

    public Gui(){
    }

    public int getSize() {
        return size;
    }

    public String getDisplayname() {
        return displayname;
    }

    public Gui withDisplayname(String displayname){
        this.displayname = displayname;
        return this;
    }

    public Gui withSize(int size){
        this.size = size;
        return this;
    }

    public Gui withItemStack(ItemGui itemGui, int slot){
        this.itemStacks.put(slot, itemGui.getItemStack());
        return this;
    }
    public Gui withItemStack(ItemStack itemStack, int slot){
        this.itemStacks.put(slot, itemStack);
        return this;
    }
    public Gui withItemStack(ItemGui itemGui, List<Integer> slots){
        for(int i : slots){
            this.itemStacks.put(i, itemGui.getItemStack());
        }
        return this;
    }

    public Gui withItemStack(ItemStack itemStack, List<Integer> slots){
        for(int i : slots){
            this.itemStacks.put(i, itemStack);
        }
        return this;
    }

    public ItemGui withItem(String displayname){
        ItemGui itemGui = new ItemGui(this);
        itemGui.thatHasDisplayname(displayname);
        return itemGui;
    }

    public Inventory create(){
        Inventory inv = Bukkit.createInventory(null, this.size, this.displayname);
        for(Map.Entry<Integer, ItemStack> entry : this.itemStacks.entrySet()){
            inv.setItem(entry.getKey(), entry.getValue());
        }
        return inv;
    }


}
