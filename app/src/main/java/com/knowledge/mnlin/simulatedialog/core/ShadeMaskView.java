package com.knowledge.mnlin.simulatedialog.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.knowledge.mnlin.simulatedialog.animations.PageTANoEffect;
import com.knowledge.mnlin.simulatedialog.interfaces.FullPage;
import com.knowledge.mnlin.simulatedialog.interfaces.Page;
import com.knowledge.mnlin.simulatedialog.interfaces.PageAppearance;
import com.knowledge.mnlin.simulatedialog.interfaces.PageCallback;
import com.knowledge.mnlin.simulatedialog.interfaces.PageLauncherType;
import com.knowledge.mnlin.simulatedialog.interfaces.PageLifeCycle;
import com.knowledge.mnlin.simulatedialog.interfaces.PageTransAnimation;
import com.knowledge.mnlin.simulatedialog.interfaces.PartPage;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2019/10/15  10:03
 * function : simulate the mask
 *
 * @author mnlin0905@gmail.com
 */
public final class ShadeMaskView extends AppCompatImageView implements View.OnClickListener, FullPage {
    /**
     * dimen bg-drawable
     */
    private Drawable maskDrawable;

    /**
     * action dispatch
     */
    @Nullable
    private MaskOperateListener listener;

    /**
     * host object
     */
    @NotNull
    private PartPage hostPage;

    /**
     * default no effect
     */
    PageTransAnimation animationForMask = PageTANoEffect.getSingleInstance();

    {
        maskDrawable = new ColorDrawable(Color.parseColor("#00000000"));
    }

    public ShadeMaskView(@NonNull Context context) {
        this(context, null);
    }

    public ShadeMaskView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadeMaskView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // init the mask
        setBackground(maskDrawable);
        setOnClickListener(this);
        setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * @param listener dest listener
     */
    @NonNull
    public ShadeMaskView setMaskOperateListener(@Nullable MaskOperateListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.dispatchMaskOnClick(this);
        }
    }

    /**
     * @return index in {@link PageStackRecord} ; -1 if not attach to {@link PageManager}
     */
    @Override
    public int getIndexInStackRecord() {
        return hostPage.getIndexInStackRecord() - 1;
    }

    /**
     * page must relative context(if has be added to parent)
     *
     * @return {@link PageParent}
     */
    @NonNull
    @Override
    public PageParent getPageParent() {
        return PageParent.instance;
    }

    /**
     * @return page's style
     */
    @Override
    public int getPageAppearanceType() {
        return PageAppearance.PAGE_APPEARANCE_FULLSCREEN;
    }

    /**
     * called when "back" be pressed
     *
     * @return if true,manager will not close the page (regard as a dialog)
     */
    @Override
    public boolean onBackPressed() {
        return false;
    }

    /**
     * @return page's launcher type
     */
    @Override
    public int getLauncherType() {
        return PageLauncherType.LAUNCHER_DEFAULT_TYPE;
    }

    /**
     * Called back in constructor
     */
    @Override
    public void onPageCreate() {

    }

    /**
     * page's content-view has set
     */
    @Override
    public void onPageViewInject() {

    }

    /**
     * page is visible
     */
    @Override
    public void onPageAttachToParent() {

    }

    /**
     * page visible and interactive
     */
    @Override
    public void onPageActive() {

    }

    /**
     * Before the interface is invisible, it has entered the visible state again.
     * <p>
     * called after {@link PageLifeCycle#onPageActive()}
     */
    @Override
    public void onPageNewIntent() {

    }

    /**
     * At this time, page is about to be removed from the view interface
     * <p>
     * or it's a background-page (only before part-page that's what happens)
     */
    @Override
    public void onPageDeactive() {

    }

    /**
     * page be blocked
     */
    @Override
    public void onPageDetachFromParent() {

    }

    /**
     * When pages are reused
     *
     * @see Activity#onNewIntent(Intent)
     */
    @Override
    public void onPageReused() {

    }

    /**
     * page current phase
     * <p>
     * the life cycle of "ShadeMaskView" is meaningless
     */
    @Override
    public int getCurrentGradation() {
        return PageLifeCycle.PAGE_GRADATION_IDEL;
    }

    /**
     * @return real-view in page ; if it is null , the system reads the annotated values
     */
    @NonNull
    @Override
    public View providerContentView() {
        return this;
    }

    /**
     * @return the layout-params put in parent-view , if it is null , the system will take the default value (read from xml)
     */
    @NonNull
    @Override
    public ViewGroup.LayoutParams providerIntegrateParams() {
        return this.getLayoutParams();
    }

    @NotNull
    public PartPage getHostPage() {
        return hostPage;
    }

    /**
     * inject host-page: part-page
     *
     * @param hostPage host page
     * @return self
     */
    ShadeMaskView setHostPage(@NotNull PartPage hostPage) {
        this.hostPage = hostPage;
        return this;
    }

    @Override
    public void onPageRecordRightPush(Page page) {
        animationForMask.onPageRecordRightPush(page);
    }

    @Override
    public void onPageRecordLeftInsert(Page page) {
        animationForMask.onPageRecordLeftInsert(page);
    }

    @Override
    public void onPageRecordRightPop(Page page, PageCallback<Page> mustCalledWhenEndOrCancel) {
        animationForMask.onPageRecordRightPop(page, mustCalledWhenEndOrCancel);
    }

    @Override
    public void onPageRecordLeftRemove(Page page, PageCallback<Page> mustCalledWhenEndOrCancel) {
        animationForMask.onPageRecordLeftRemove(page, mustCalledWhenEndOrCancel);
    }

    /**
     * dispatch the mask's action
     */
    public interface MaskOperateListener {
        /**
         * dispatch the click action
         *
         * @param mask target
         */
        void dispatchMaskOnClick(@NonNull ShadeMaskView mask);
    }
}