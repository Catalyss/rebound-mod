package catalyss.rebound.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;

@Modmenu(modId = "rebound")
@Config(name = "reboundedconfig",wrapperName = "ReboundedConfig",saveOnModification = true,defaultHook = false)
public class ConfigModel {
    public Choices Difficulty = Choices.EasyMode;
    public boolean PermaHarcode = false;
    public boolean PopUpDisplay = true;
    public float RandomizedPercent = 5f;

    public enum Choices {
        EasyMode, WhatKeyAgain, YouWantedThis;
    }
}