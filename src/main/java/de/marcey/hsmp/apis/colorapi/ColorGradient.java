package de.marcey.hsmp.apis.colorapi;

import net.md_5.bungee.api.ChatColor;

public class ColorGradient {

    private ChatColor from;
    private ChatColor to;
    private int length;
    private String message;

    private boolean bold;

    public ColorGradient(String message){
        this.message = message;
        this.length = message.length();
        this.bold = false;
    }

    public ColorGradient withFrom(ChatColor from){
        this.from = from;
        return this;
    }
    public ColorGradient withTo(ChatColor to){
        this.to = to;
        return this;
    }
    public ColorGradient withLength(int length){
        this.length = length;
        return this;
    }

    public ColorGradient withBold(boolean bold){
        this.bold = bold;
        return this;
    }

    public ColorGradient withMessage(String message){
        this.message = message;
        return this;
    }

    public String execute(){
        StringBuilder gradientBuilder = new StringBuilder();
        char[] args = this.message.toCharArray();
        for(int i = 0; i < this.length; i++){
            float ratio = (float) i / (float) (this.length-1);
            ChatColor color = blendColors(from, to, ratio);
            gradientBuilder.append(color + (this.bold ? "§l" : "")).append(args[i]);
        }
        return gradientBuilder.toString();
    }

    private ChatColor blendColors(ChatColor start, ChatColor end, float ratio) {
        int red1 = start.getColor().getRed();
        int green1 = start.getColor().getGreen();
        int blue1 = start.getColor().getBlue();
        int red2 = end.getColor().getRed();
        int green2 = end.getColor().getGreen();
        int blue2 = end.getColor().getBlue();

        int blendedRed = (int) (red1 + (ratio * (red2 - red1)));
        int blendedGreen = (int) (green1 + (ratio * (green2 - green1)));
        int blendedBlue = (int) (blue1 + (ratio * (blue2 - blue1)));

        return ChatColor.of(new java.awt.Color(blendedRed, blendedGreen, blendedBlue));
    }

    public String getMessage() {
        return message;
    }
}
