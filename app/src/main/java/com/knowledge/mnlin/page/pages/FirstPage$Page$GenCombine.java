package com.knowledge.mnlin.page.pages;

import android.support.annotation.Nullable;

import com.knowledge.mnlin.page.gen_interfaces.PageGenCombineOperate;
import com.knowledge.mnlin.page.interfaces.PageTransAnimation;

/**
 * DO NOT EDIT THIS FILE!!! IT WAS GENERATED BY Page : PageMainEnterProcessor
 */
public class FirstPage$Page$GenCombine implements PageGenCombineOperate {
    @Nullable
    @Override
    public PageTransAnimation getPageTransAnimation() {
        return SecondPage$Page$TransAnim.autoGenerateTransAnim();
    }

    @Override
    public int getPageAppearanceType() {
        return FirstPage$Page$AppearanceType.autoGenerateAppearanceType();
    }

    @Override
    public int getPageLauncherType() {
        return FirstPage$Page$LauncherType.autoGenerateLauncherType();
    }
}