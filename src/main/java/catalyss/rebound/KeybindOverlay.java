package catalyss.rebound;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

import static catalyss.rebound.Rebound.IsKeybindShown;

public class KeybindOverlay implements HudRenderCallback {


    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {


        int x = 0;
        int y = 0;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        x = width;
        y = height;

        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        if(IsKeybindShown){
            String[] rebounded= Rebound.KeyBindList.getString().split(System.lineSeparator());
            int imax = rebounded.length;
            int i = 0;
            for (int ix =5; ix<x;) {
                for (int iy = 5; iy < y-100; ) {
                    if(i>= imax)break;
                    String str = rebounded[i];
                    if(rebounded[i].contains("key.keyboard.")) {
                        str = rebounded[i].split("key.keyboard.")[0];
                        String ss = rebounded[i].split("key.keyboard.")[1];
                        str += ss.substring(0, 1).toUpperCase() + ss.substring(1);
                    };
                    str = str.replace("key.keyboard.","");

                    drawContext.drawText(renderer, str, ix, iy, -1, false);
                    i++;
                    iy+=10;
                }
                if(i>= imax)break;
                ix+=200;
            }
        };


    }
}
