package catalyss.rebound;

import catalyss.rebound.config.ReboundedConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rebound implements ModInitializer {
	public static final String MOD_ID = "rebound";
	public static final Logger LOGGER = LoggerFactory.getLogger("rebound");
	public int oldHealth;
	public boolean once=false;
	public boolean Breakingrules=false;
	public boolean HasAchievement = false;
	public static KeyBinding CustomGui;
	public static MutableText KeyBindList;
	public static Boolean IsKeybindShown = false;
	public static Boolean _IsKeybindShown = false;
	public int[] KEYCODES = {0, 1, 2, 3, 4, 5, 6, 70, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90,290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300, 301, 282, 320, 321, 322, 323, 324, 325, 326, 327, 328, 329, 334, 330, 335, 336, 332, 331, 333, 264, 263, 262, 265, 39, 92, 44, 61, 96, 91, 45, 46, 93, 59, 47, 32, 258, 342, 341, 340, 346, 345, 344, 257, 256, 259, 261, 269, 268, 260, 267, 266, 280, 284, 281,};
	public static final ReboundedConfig CONFIG = ReboundedConfig.createAndLoad();

	@Override
	public void onInitialize() {
		/*static {

        FN13 =  [302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313]
    }
     */
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello this is my first mod! feel free to judge <3");

		CustomGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.rebound.showkeybinggui", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_F6, // The keycode of the key
				"category.rebound" // The translation key of the keybinding's category.
		));

		HudRenderCallback.EVENT.register(new KeybindOverlay());
        ClientTickEvents.START_CLIENT_TICK.register((client) -> {
			if (client == null || client.player == null){ Breakingrules =false; return;}
            if( client.currentScreen != null && client.currentScreen.getTitle().getContent().toString().contains("controls.keybinds.title") && !HasAchievement){
				client.currentScreen.close();
				if(!Breakingrules)
				{
					Text Title = Text.literal("§4§l§n§o").append(Text.translatable("PopUp.rebound.Warning.title"));
					MutableText Desc = Text.literal("§4§l").append(Text.translatable("PopUp.rebound.Warning.Description"));
					DisplayPopUp(client, Title,Desc);
					Breakingrules =true;
				}else
				{
					List<Integer> Used = new ArrayList<>();
					for (int i = 0; i < client.options.allKeys.length; i++) {
						if(client.options.allKeys[i].getTranslationKey().equals("key.rebound.showkeybinggui")) continue;
						int key =KEYCODES[(int) (Math.random() * (KEYCODES.length))];
						int maxtry = 0;
						while (Used.contains(key) && maxtry<52){
							key =KEYCODES[((int) (Math.random() * (KEYCODES.length)))];
							if(maxtry>50) Used = new ArrayList<>();
							maxtry++;
						}
						Used.add(key);
						client.options.allKeys[i].setBoundKey(InputUtil.fromKeyCode(key,0));
					}
					KeyBinding.updateKeysByCode();
					UpdateBindList(client);

					Text Title = Text.literal("§f").append(Text.translatable("PopUp.rebound.Whathaveyoudone.title"));
					MutableText Desc = Text.literal("§f").append(Text.translatable("PopUp.rebound.Whathaveyoudone.Description"));
					DisplayPopUp(client, Title,Desc);
				}
			}

			if(CustomGui.isPressed())
			{
				IsKeybindShown = !_IsKeybindShown;
			}else {
				_IsKeybindShown = IsKeybindShown;
			}

			int health = (int) Math.ceil(client.player.getHealth()/2);
			if (client.player.hurtTime > 5) {
				if((client.world != null && client.world.getLevelProperties().isHardcore()) || CONFIG.PermaHarcode())
				{
					if (CONFIG.Difficulty().name().contains("EasyMode")) PlayerhardcorModeEasyMode(client, health);
					else if (CONFIG.Difficulty().name().contains("WhatKeyAgain")) PlayerhardcorModeWhatKeyAgain(client, health);
					else PlayerhardcorModeHardcoreMode(client, health);
				} else {
					if (CONFIG.Difficulty().name().contains("EasyMode")) EasyMode(client, health);
					else if (CONFIG.Difficulty().name().contains("WhatKeyAgain")) WhatKeyAgain(client, health);
					else HardcoreMode(client, health);
				}

			}else{
				once = false;
			}

			UpdateBindList(client);
			oldHealth = (int) Math.ceil(client.player.getHealth()/2);
		});
	}

	private void HardcoreMode(MinecraftClient client,int health){

		if (health == 0)
		{
			if(once)return;
			for (int i = 0; i < client.options.allKeys.length * 6; i++) {
				int keyswapmax = client.options.allKeys.length;

				int Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);
				int Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);

				while(client.options.allKeys[Swap1].getTranslationKey().equals("key.rebound.showkeybinggui")) Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);
				while(client.options.allKeys[Swap2].getTranslationKey().equals("key.rebound.showkeybinggui")) Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);

				InputUtil.Key SwapKey2 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap2]);
				InputUtil.Key SwapKey1 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap1]);

				if(SwapKey2.getTranslationKey().contains("unknown")) SwapKey2 =InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))],0);
				if(SwapKey1.getTranslationKey().contains("unknown")) SwapKey1 =InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))],0);

				client.options.allKeys[Swap1].setBoundKey(SwapKey2);
				client.options.allKeys[Swap2].setBoundKey(SwapKey1);

				KeyBinding.updateKeysByCode();
			}

			Text Title = Text.translatable("PopUp.rebound.KeyRandomized.title");

			MutableText Desc = Text.literal("§a").append(Text.translatable("PopUp.rebound.KeyRandomized.Description"));
			DisplayPopUp(client, Title,Desc);
			once = true;

		}
		else
		{
			once = false;

			for (int i = 0; i < oldHealth - health; i++) {
				int keyswapmax = client.options.allKeys.length;

				int Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);
				int Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);

				while(client.options.allKeys[Swap1].getTranslationKey().equals("key.rebound.showkeybinggui")) Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);
				while(client.options.allKeys[Swap2].getTranslationKey().equals("key.rebound.showkeybinggui")) Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);

				InputUtil.Key SwapKey2 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap2]);
				InputUtil.Key SwapKey1 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap1]);

				if(SwapKey2.getTranslationKey().contains("unknown")) SwapKey2 =InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))],0);
				if(SwapKey1.getTranslationKey().contains("unknown")) SwapKey1 =InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))],0);

				client.options.allKeys[Swap1].setBoundKey(SwapKey2);
				client.options.allKeys[Swap2].setBoundKey(SwapKey1);

				KeyBinding.updateKeysByCode();

				Text Title = Text.translatable("PopUp.rebound.KeySwapped.title");

				MutableText k2 = Text.translatable(client.options.allKeys[Swap2].getTranslationKey());
				MutableText k1 = Text.translatable(client.options.allKeys[Swap1].getTranslationKey());

				MutableText Desc = Text.literal("§a").append(k2).append("§r ").append(Text.translatable("PopUp.rebound.KeySwapped.Description")).append(" §c").append(k1);
				DisplayPopUp(client, Title,Desc);
			}
		}
	}
	private void PlayerhardcorModeHardcoreMode(MinecraftClient client,int health){

		if (health >= 5)
		{
			if(once)return;
			for (int i = 0; i < client.options.allKeys.length * 6; i++) {
				int keyswapmax = client.options.allKeys.length;

				int Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);
				int Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);

				while(client.options.allKeys[Swap1].getTranslationKey().equals("key.rebound.showkeybinggui")) Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);
				while(client.options.allKeys[Swap2].getTranslationKey().equals("key.rebound.showkeybinggui")) Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);

				InputUtil.Key SwapKey2 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap2]);
				InputUtil.Key SwapKey1 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap1]);

				if(SwapKey2.getTranslationKey().contains("unknown")) SwapKey2 =InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))],0);
				if(SwapKey1.getTranslationKey().contains("unknown")) SwapKey1 =InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))],0);

				client.options.allKeys[Swap1].setBoundKey(SwapKey2);
				client.options.allKeys[Swap2].setBoundKey(SwapKey1);

				KeyBinding.updateKeysByCode();
			}

			Text Title = Text.translatable("PopUp.rebound.KeyRandomized.title");

			MutableText Desc = Text.literal("§a").append(Text.translatable("PopUp.rebound.KeyRandomized.Description"));
			DisplayPopUp(client, Title,Desc);
			once = true;

		}
		else
		{
			once = false;

			for (int i = 0; i < oldHealth - health; i++) {
				int keyswapmax = client.options.allKeys.length;

				int Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);
				int Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);

				while(client.options.allKeys[Swap1].getTranslationKey().equals("key.rebound.showkeybinggui")) Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);
				while(client.options.allKeys[Swap2].getTranslationKey().equals("key.rebound.showkeybinggui")) Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);

				InputUtil.Key SwapKey2 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap2]);
				InputUtil.Key SwapKey1 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap1]);

				if(SwapKey2.getTranslationKey().contains("unknown")) SwapKey2 =InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))],0);
				if(SwapKey1.getTranslationKey().contains("unknown")) SwapKey1 =InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))],0);

				client.options.allKeys[Swap1].setBoundKey(SwapKey2);
				client.options.allKeys[Swap2].setBoundKey(SwapKey1);

				KeyBinding.updateKeysByCode();

				Text Title = Text.translatable("PopUp.rebound.KeySwapped.title");

				MutableText k2 = Text.translatable(client.options.allKeys[Swap2].getTranslationKey());
				MutableText k1 = Text.translatable(client.options.allKeys[Swap1].getTranslationKey());

				MutableText Desc = Text.literal("§a").append(k2).append("§r ").append(Text.translatable("PopUp.rebound.KeySwapped.Description")).append(" §c").append(k1);
				DisplayPopUp(client, Title,Desc);
			}
		}
	}
	private void EasyMode(MinecraftClient client,int health) {

		if (health == 0) {
			if(once)return;
			int keyswapmax = client.options.allKeys.length;

			int Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);
			int Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);

			while (client.options.allKeys[Swap1].getTranslationKey().equals("key.rebound.showkeybinggui"))
				Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);
			while (client.options.allKeys[Swap2].getTranslationKey().equals("key.rebound.showkeybinggui"))
				Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);

			InputUtil.Key SwapKey2 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap2]);
			InputUtil.Key SwapKey1 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap1]);

			if (SwapKey2.getTranslationKey().contains("unknown"))
				SwapKey2 = InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))], 0);
			if (SwapKey1.getTranslationKey().contains("unknown"))
				SwapKey1 = InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))], 0);

			client.options.allKeys[Swap1].setBoundKey(SwapKey2);
			client.options.allKeys[Swap2].setBoundKey(SwapKey1);

			KeyBinding.updateKeysByCode();

			Text Title = Text.translatable("PopUp.rebound.KeySwapped.title");

			MutableText k2 = Text.translatable(client.options.allKeys[Swap2].getTranslationKey());
			MutableText k1 = Text.translatable(client.options.allKeys[Swap1].getTranslationKey());

			MutableText Desc = Text.literal("§a").append(k2).append("§r ").append(Text.translatable("PopUp.rebound.KeySwapped.Description")).append(" §c").append(k1);
			DisplayPopUp(client, Title,Desc);
			once = true;
		}
	}
	private void WhatKeyAgain(MinecraftClient client,int health) {

		if (health == 0) {
			if (once) return;
			for (int i = 0; i < Math.ceil(client.options.allKeys.length*(Math.abs(CONFIG.RandomizedPercent())/100.0f)); i++) {
				int keyswapmax = client.options.allKeys.length;

				int Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);
				int Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);

				while (client.options.allKeys[Swap1].getTranslationKey().equals("key.rebound.showkeybinggui"))
					Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);
				while (client.options.allKeys[Swap2].getTranslationKey().equals("key.rebound.showkeybinggui"))
					Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);

				InputUtil.Key SwapKey2 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap2]);
				InputUtil.Key SwapKey1 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap1]);

				if (SwapKey2.getTranslationKey().contains("unknown"))
					SwapKey2 = InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))], 0);
				if (SwapKey1.getTranslationKey().contains("unknown"))
					SwapKey1 = InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))], 0);

				client.options.allKeys[Swap1].setBoundKey(SwapKey2);
				client.options.allKeys[Swap2].setBoundKey(SwapKey1);

				KeyBinding.updateKeysByCode();

				once = true;
			}
			Text Title = Text.literal("§e"+CONFIG.RandomizedPercent()+"%§r ").append(Text.translatable("PopUp.rebound.PercentKeyRandomized.title"));
			MutableText Desc = Text.literal("§a").append(Text.translatable("PopUp.rebound.KeyRandomized.Description"));
			DisplayPopUp(client, Title,Desc);
		}
		else if (health <= 5) {
			for (int i = 0; i < (oldHealth/2) - (health/2); i++) {
				int keyswapmax = client.options.allKeys.length;

				int Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);
				int Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);

				while (client.options.allKeys[Swap1].getTranslationKey().equals("key.rebound.showkeybinggui"))
					Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);
				while (client.options.allKeys[Swap2].getTranslationKey().equals("key.rebound.showkeybinggui"))
					Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);

				InputUtil.Key SwapKey2 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap2]);
				InputUtil.Key SwapKey1 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap1]);

				if (SwapKey2.getTranslationKey().contains("unknown"))
					SwapKey2 = InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))], 0);
				if (SwapKey1.getTranslationKey().contains("unknown"))
					SwapKey1 = InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))], 0);

				client.options.allKeys[Swap1].setBoundKey(SwapKey2);
				client.options.allKeys[Swap2].setBoundKey(SwapKey1);

				KeyBinding.updateKeysByCode();

				Text Title = Text.translatable("PopUp.rebound.KeySwapped.title");

				MutableText k2 = Text.translatable(client.options.allKeys[Swap2].getTranslationKey());
				MutableText k1 = Text.translatable(client.options.allKeys[Swap1].getTranslationKey());

				MutableText Desc = Text.literal("§a").append(k2).append("§r ").append(Text.translatable("PopUp.rebound.KeySwapped.Description")).append(" §c").append(k1);

				DisplayPopUp(client, Title, Desc);
			}
		}
	}
	private void PlayerhardcorModeEasyMode(MinecraftClient client,int health) {
		for (int i = 0; i < (oldHealth / 2) - (health / 2); i++) {
			int keyswapmax = client.options.allKeys.length;

			int Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);
			int Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);

			while (client.options.allKeys[Swap1].getTranslationKey().equals("key.rebound.showkeybinggui"))
				Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);
			while (client.options.allKeys[Swap2].getTranslationKey().equals("key.rebound.showkeybinggui"))
				Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);

			InputUtil.Key SwapKey2 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap2]);
			InputUtil.Key SwapKey1 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap1]);

			if (SwapKey2.getTranslationKey().contains("unknown"))
				SwapKey2 = InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))], 0);
			if (SwapKey1.getTranslationKey().contains("unknown"))
				SwapKey1 = InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))], 0);

			client.options.allKeys[Swap1].setBoundKey(SwapKey2);
			client.options.allKeys[Swap2].setBoundKey(SwapKey1);

			KeyBinding.updateKeysByCode();

			Text Title = Text.translatable("PopUp.rebound.KeySwapped.title");

			MutableText k2 = Text.translatable(client.options.allKeys[Swap2].getTranslationKey());
			MutableText k1 = Text.translatable(client.options.allKeys[Swap1].getTranslationKey());

			MutableText Desc = Text.literal("§a").append(k2).append("§r ").append(Text.translatable("PopUp.rebound.KeySwapped.Description")).append(" §c").append(k1);

			DisplayPopUp(client, Title, Desc);
		}
	}
	private void PlayerhardcorModeWhatKeyAgain(MinecraftClient client,int health) {

			if (once) return;
			for (int i = 0; i < Math.ceil(client.options.allKeys.length*(Math.abs(CONFIG.RandomizedPercent())/100.0f)); i++) {
				int keyswapmax = client.options.allKeys.length;

				int Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);
				int Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);

				while (client.options.allKeys[Swap1].getTranslationKey().equals("key.rebound.showkeybinggui"))
					Swap1 = (int) ((Math.random() * (keyswapmax)) + 0);
				while (client.options.allKeys[Swap2].getTranslationKey().equals("key.rebound.showkeybinggui"))
					Swap2 = (int) ((Math.random() * (keyswapmax)) + 0);

				InputUtil.Key SwapKey2 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap2]);
				InputUtil.Key SwapKey1 = KeyBindingHelper.getBoundKeyOf(client.options.allKeys[Swap1]);

				if (SwapKey2.getTranslationKey().contains("unknown"))
					SwapKey2 = InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))], 0);
				if (SwapKey1.getTranslationKey().contains("unknown"))
					SwapKey1 = InputUtil.fromKeyCode(KEYCODES[(int) (Math.random() * (KEYCODES.length))], 0);

				client.options.allKeys[Swap1].setBoundKey(SwapKey2);
				client.options.allKeys[Swap2].setBoundKey(SwapKey1);

				KeyBinding.updateKeysByCode();

				once = true;

				Text Title = Text.translatable("PopUp.rebound.KeySwapped.title");

				MutableText k2 = Text.translatable(client.options.allKeys[Swap2].getTranslationKey());
				MutableText k1 = Text.translatable(client.options.allKeys[Swap1].getTranslationKey());

				MutableText Desc = Text.literal("§a").append(k2).append("§r ").append(Text.translatable("PopUp.rebound.KeySwapped.Description")).append(" §c").append(k1);

				DisplayPopUp(client, Title, Desc);
			}
		}

	private void DisplayPopUp(MinecraftClient client, Text TitleText, MutableText DescText){
		if(!CONFIG.PopUpDisplay()) return;

		Text Title = Text.of(TitleText.getString());
		Text Desc = Text.of(DescText.getString());
		client.getToastManager().add(new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION, Title, Desc));
	}
	private void UpdateBindList(MinecraftClient client)
	{
		KeyBindList = (MutableText) Text.of("");
		for (int i = 0; i < client.options.allKeys.length ; i++) {
			MutableText k1 = Text.translatable(client.options.allKeys[i].getTranslationKey().toString());
			MutableText k2 = Text.translatable(client.options.allKeys[i].getBoundKeyTranslationKey().toString());
			if(!client.options.allKeys[i].getBoundKeyTranslationKey().equals(client.options.allKeys[i].getDefaultKey().getTranslationKey()))
			{
				KeyBindList.append("§a").append(k1).append("  §r→§c  ").append( k2).append( System.lineSeparator());
			}
			else
			{
				KeyBindList.append( "§r").append(k1).append("  §r→§r  ").append( k2).append( System.lineSeparator());
			}
		}
	}

}