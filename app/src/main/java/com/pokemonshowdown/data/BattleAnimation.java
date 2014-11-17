package com.pokemonshowdown.data;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pokemonshowdown.app.BattleFragment;
import com.pokemonshowdown.app.R;

import java.util.Arrays;

public class BattleAnimation {
    public final static String BTAG = BattleAnimation.class.getName();

    public static AnimatorSet processMove(String move, View view, BattleFragment battleFragment, String[] split) {
        move = MyApplication.toId(move);
        if (view == null) {
            return null;
        }
        RelativeLayout battleWrapper = (RelativeLayout) view.findViewById(R.id.animation_layout);
        RelativeLayout atkC = (RelativeLayout) view.findViewById(battleFragment.getPkmLayoutId(split[0]));
        ImageView atk = (ImageView) view.findViewById(battleFragment.getSpriteId(split[0]));
        RelativeLayout defC = (RelativeLayout) view.findViewById(battleFragment.getPkmLayoutId(split[2]));
        ImageView def = (ImageView) view.findViewById(battleFragment.getSpriteId(split[2]));
        return charge(battleFragment.getActivity(), atkC, atk, defC, def, split);
    }

    public static AnimatorSet attack(Context context, RelativeLayout atkC, ImageView atk, RelativeLayout defC, ImageView def) {
        int[] locAtk = new int[2];
        int[] locDef = new int[2];
        atk.getLocationOnScreen(locAtk);
        def.getLocationOnScreen(locDef);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator attackX = ObjectAnimator.ofFloat(atk, "x", 0f);
        attackX.setDuration(BattleFragment.ANIMATION_LONG);
        ObjectAnimator attackY = ObjectAnimator.ofFloat(atk, "y", 0f);
        attackY.setDuration(BattleFragment.ANIMATION_LONG);
        animatorSet.play(attackX);
        animatorSet.play(attackY).with(attackX);
        return animatorSet;
    }

    public static AnimatorSet charge(Context context, RelativeLayout atkC, final ImageView atk, RelativeLayout defC, ImageView def, String[] split) {
        if (split.length >= 4 && split[3].equals("[still]")) {
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator alpha = ObjectAnimator.ofFloat(atk, "alpha", 0.3f);
            alpha.setDuration(BattleFragment.ANIMATION_LONG);
            return animatorSet;
        } else {
            AnimatorSet animatorSet = flight(context, atkC, atk, defC);
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    atk.setAlpha(1f);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            return animatorSet;
        }
    }

    public static AnimatorSet dance(Context context, RelativeLayout atkC, ImageView atk) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator shakeLeft = ObjectAnimator.ofFloat(atk, "x", 0f);
        shakeLeft.setDuration(BattleFragment.ANIMATION_LONG / 4);
        ObjectAnimator shakeRight = ObjectAnimator.ofFloat(atk, "x", atkC.getWidth() - atk.getWidth());
        shakeRight.setDuration(BattleFragment.ANIMATION_LONG / 2);
        ObjectAnimator shakeMiddle = ObjectAnimator.ofFloat(atk, "x", atk.getX());
        shakeMiddle.setDuration(BattleFragment.ANIMATION_LONG / 4);
        animatorSet.play(shakeLeft);
        animatorSet.play(shakeRight).after(shakeLeft);
        animatorSet.play(shakeMiddle).after(shakeRight);
        return animatorSet;
    }

    public static AnimatorSet flight(Context context, final RelativeLayout atkC, final ImageView atk, final RelativeLayout defC) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator flightLeft = ObjectAnimator.ofFloat(atk, "x", 0f);
        flightLeft.setDuration(BattleFragment.ANIMATION_LONG / 4);
        ObjectAnimator flightTop = ObjectAnimator.ofFloat(atk, "y", 0f);
        flightTop.setDuration(BattleFragment.ANIMATION_LONG / 4);
        ObjectAnimator flightAlpha = ObjectAnimator.ofFloat(atk, "alpha", 0f);
        flightAlpha.setDuration(BattleFragment.ANIMATION_LONG / 4);
        final ImageView leftClaw = new ImageView(context);
        leftClaw.setImageResource(R.drawable.battle_leftclaw);
        final ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ObjectAnimator flightClawRight = ObjectAnimator.ofFloat(leftClaw, "x", defC.getWidth());
        flightClawRight.setDuration(BattleFragment.ANIMATION_LONG / 2);
        ObjectAnimator flightClawBottom = ObjectAnimator.ofFloat(leftClaw, "y", defC.getHeight());
        flightClawBottom.setDuration(BattleFragment.ANIMATION_LONG / 2);
        flightClawRight.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                defC.addView(leftClaw, imageParams);
                leftClaw.setX(0f);
                leftClaw.setY(0f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                defC.removeView(leftClaw);
                atk.setX(atkC.getWidth() - atk.getWidth());
                atk.setY(atkC.getHeight() - atk.getHeight());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        ObjectAnimator flightMiddleLeft = ObjectAnimator.ofFloat(atk, "x", atk.getX());
        flightMiddleLeft.setDuration(BattleFragment.ANIMATION_LONG / 4);
        ObjectAnimator flightMiddleTop = ObjectAnimator.ofFloat(atk, "y", atk.getY());
        flightMiddleTop.setDuration(BattleFragment.ANIMATION_LONG / 4);
        ObjectAnimator flightMiddleAlpha = ObjectAnimator.ofFloat(atk, "alpha", 1f);
        flightMiddleAlpha.setDuration(BattleFragment.ANIMATION_LONG / 4);
        animatorSet.play(flightLeft).with(flightTop);
        animatorSet.play(flightLeft).with(flightAlpha);
        animatorSet.play(flightLeft).before(flightClawRight);
        animatorSet.play(flightClawRight).with(flightClawBottom);
        animatorSet.play(flightMiddleLeft).after(flightClawRight);
        animatorSet.play(flightMiddleLeft).with(flightMiddleTop);
        animatorSet.play(flightMiddleLeft).with(flightMiddleAlpha);
        return animatorSet;
    }

    public static AnimatorSet shake(RelativeLayout atkC, ImageView atk) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator shakeLeft = ObjectAnimator.ofFloat(atk, "x", 0f);
        shakeLeft.setDuration(BattleFragment.ANIMATION_LONG / 6);
        ObjectAnimator shakeRightAll = ObjectAnimator.ofFloat(atk, "x", atkC.getWidth() - atk.getWidth());
        shakeRightAll.setDuration(BattleFragment.ANIMATION_LONG / 3);
        ObjectAnimator shakeLeftAll = ObjectAnimator.ofFloat(atk, "x", 0f);
        shakeLeftAll.setDuration(BattleFragment.ANIMATION_LONG / 3);
        ObjectAnimator shakeMiddle = ObjectAnimator.ofFloat(atk, "x", atk.getX());
        shakeMiddle.setDuration(BattleFragment.ANIMATION_LONG / 6);
        animatorSet.play(shakeLeft);
        animatorSet.play(shakeRightAll).after(shakeLeft);
        animatorSet.play(shakeLeftAll).after(shakeRightAll);
        animatorSet.play(shakeMiddle).after(shakeLeftAll);
        return animatorSet;
    }

    public static AnimatorSet xatk(Context context, final RelativeLayout atkC, final ImageView atk, final RelativeLayout defC) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator flightLeft = ObjectAnimator.ofFloat(atk, "x", 0f);
        flightLeft.setDuration(BattleFragment.ANIMATION_LONG / 4);
        ObjectAnimator flightTop = ObjectAnimator.ofFloat(atk, "y", 0f);
        flightTop.setDuration(BattleFragment.ANIMATION_LONG / 4);
        ObjectAnimator flightAlpha = ObjectAnimator.ofFloat(atk, "alpha", 0f);
        flightAlpha.setDuration(BattleFragment.ANIMATION_LONG / 4);
        final ImageView leftClaw = new ImageView(context);
        leftClaw.setImageResource(R.drawable.battle_leftclaw);
        final ImageView rightClaw = new ImageView(context);
        rightClaw.setImageResource(R.drawable.battle_rightclaw);
        final ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ObjectAnimator flightClawRight = ObjectAnimator.ofFloat(leftClaw, "x", defC.getWidth());
        flightClawRight.setDuration(BattleFragment.ANIMATION_LONG / 2);
        ObjectAnimator flightClawBottom = ObjectAnimator.ofFloat(leftClaw, "y", defC.getHeight());
        flightClawBottom.setDuration(BattleFragment.ANIMATION_LONG / 2);
        ObjectAnimator flightClawLeft = ObjectAnimator.ofFloat(rightClaw, "x", 0f);
        flightClawLeft.setDuration(BattleFragment.ANIMATION_LONG / 2);
        ObjectAnimator flightClawBottomL = ObjectAnimator.ofFloat(rightClaw, "y", defC.getHeight());
        flightClawBottomL.setDuration(BattleFragment.ANIMATION_LONG / 2);
        flightClawRight.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                defC.addView(leftClaw, imageParams);
                leftClaw.setX(0f);
                leftClaw.setY(0f);
                defC.addView(rightClaw, imageParams);
                rightClaw.setX(defC.getWidth() - rightClaw.getWidth());
                rightClaw.setY(0f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                defC.removeView(leftClaw);
                defC.removeView(rightClaw);
                atk.setX(atkC.getWidth() - atk.getWidth());
                atk.setY(atkC.getHeight() - atk.getHeight());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        ObjectAnimator flightMiddleLeft = ObjectAnimator.ofFloat(atk, "x", atk.getX());
        flightMiddleLeft.setDuration(BattleFragment.ANIMATION_LONG / 4);
        ObjectAnimator flightMiddleTop = ObjectAnimator.ofFloat(atk, "y", atk.getY());
        flightMiddleTop.setDuration(BattleFragment.ANIMATION_LONG / 4);
        ObjectAnimator flightMiddleAlpha = ObjectAnimator.ofFloat(atk, "alpha", 1f);
        flightMiddleAlpha.setDuration(BattleFragment.ANIMATION_LONG / 4);
        animatorSet.play(flightLeft).with(flightTop);
        animatorSet.play(flightLeft).with(flightAlpha);
        animatorSet.play(flightLeft).before(flightClawRight);
        animatorSet.play(flightClawRight).with(flightClawBottom);
        animatorSet.play(flightClawRight).with(flightClawLeft);
        animatorSet.play(flightClawRight).with(flightClawBottomL);
        animatorSet.play(flightMiddleLeft).after(flightClawRight);
        animatorSet.play(flightMiddleLeft).with(flightMiddleTop);
        animatorSet.play(flightMiddleLeft).with(flightMiddleAlpha);
        return animatorSet;
    }

    public static AnimatorSet self(Context context, final RelativeLayout atkC, final ImageView atk) {
        AnimatorSet animatorSet = new AnimatorSet();
        final ImageView flash = new ImageView(context);
        flash.setImageResource(R.drawable.battle_wisp);
        flash.setMaxHeight(80);
        flash.setMaxWidth(80);
        flash.setScaleType(ImageView.ScaleType.CENTER);
        flash.setAdjustViewBounds(true);
        float[] atkCenter = getCenter(atk);
        flash.setX(atkCenter[0] - flash.getWidth() * 0.5f);
        flash.setY(atkCenter[1] - flash.getHeight() * 0.5f);
        final ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ObjectAnimator flash1 = ObjectAnimator.ofFloat(flash, "alpha", 1f);
        flash1.setDuration(BattleFragment.ANIMATION_LONG / 3);
        ObjectAnimator flash2 = ObjectAnimator.ofFloat(flash, "alpha", 0f, 1f);
        flash2.setDuration(BattleFragment.ANIMATION_LONG / 3);
        ObjectAnimator flash3 = ObjectAnimator.ofFloat(flash, "alpha", 0f, 1f);
        flash3.setDuration(BattleFragment.ANIMATION_LONG / 3);
        flash1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                atkC.addView(flash, imageParams);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        flash3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                atkC.removeView(flash);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.play(flash1).before(flash2);
        animatorSet.play(flash2).before(flash3);
        return animatorSet;
    }

    public static AnimatorSet selfLight(Context context, final RelativeLayout atkC, final ImageView atk) {
        AnimatorSet animatorSet = new AnimatorSet();
        final ImageView flash = new ImageView(context);
        flash.setImageResource(R.drawable.battle_electroball);
        flash.setMaxHeight(atk.getHeight());
        flash.setMaxWidth(atk.getWidth());
        flash.setScaleType(ImageView.ScaleType.CENTER);
        flash.setAdjustViewBounds(true);
        flash.setX(atk.getX());
        flash.setY(atk.getY());
        final ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(flash, "scaleX", 0.2f);
        scaleX.setDuration(BattleFragment.ANIMATION_LONG);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(flash, "scaleY", 0.2f);
        scaleY.setDuration(BattleFragment.ANIMATION_LONG);
        scaleX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                atkC.addView(flash, imageParams);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                atkC.removeView(flash);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.play(scaleX).with(scaleY);
        return animatorSet;
    }

    public static AnimatorSet selfDark(Context context, final RelativeLayout atkC, final ImageView atk) {
        AnimatorSet animatorSet = new AnimatorSet();
        final ImageView flash = new ImageView(context);
        flash.setImageResource(R.drawable.battle_shadowball);
        flash.setMaxHeight(atk.getHeight());
        flash.setMaxWidth(atk.getWidth());
        flash.setScaleType(ImageView.ScaleType.CENTER);
        flash.setAdjustViewBounds(true);
        flash.setX(atk.getX());
        flash.setY(atk.getY());
        final ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(flash, "scaleX", 0.2f);
        scaleX.setDuration(BattleFragment.ANIMATION_LONG);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(flash, "scaleY", 0.2f);
        scaleY.setDuration(BattleFragment.ANIMATION_LONG);
        scaleX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                atkC.addView(flash, imageParams);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                atkC.removeView(flash);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.play(scaleX).with(scaleY);
        return animatorSet;
    }

    public static AnimatorSet spinAtk(Context context, final RelativeLayout atkC, final ImageView atk, final RelativeLayout defC) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator flightLeft = ObjectAnimator.ofFloat(atk, "x", 0f);
        flightLeft.setDuration(BattleFragment.ANIMATION_LONG / 4);
        ObjectAnimator flightTop = ObjectAnimator.ofFloat(atk, "y", 0f);
        flightTop.setDuration(BattleFragment.ANIMATION_LONG / 4);
        ObjectAnimator flightAlpha = ObjectAnimator.ofFloat(atk, "alpha", 0f);
        flightAlpha.setDuration(BattleFragment.ANIMATION_LONG / 4);
        final ImageView spin = new ImageView(context);
        spin.setImageDrawable(atk.getDrawable());
        spin.setX(0);
        spin.setY(0);
        final ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ObjectAnimator spinRight = ObjectAnimator.ofFloat(spin, "x", defC.getWidth());
        spinRight.setDuration(BattleFragment.ANIMATION_LONG / 2);
        ObjectAnimator spinBottom = ObjectAnimator.ofFloat(spin, "y", defC.getHeight());
        spinBottom.setDuration(BattleFragment.ANIMATION_LONG / 2);
        ObjectAnimator spinRotation = ObjectAnimator.ofFloat(spin, "rotation", 720);
        spinRotation.setDuration(BattleFragment.ANIMATION_LONG / 2);
        spinRight.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                defC.addView(spin, imageParams);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                defC.removeView(spin);
                atk.setX(atkC.getWidth() - atk.getWidth());
                atk.setY(atkC.getHeight() - atk.getHeight());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        ObjectAnimator flightMiddleLeft = ObjectAnimator.ofFloat(atk, "x", atk.getX());
        flightMiddleLeft.setDuration(BattleFragment.ANIMATION_LONG / 4);
        ObjectAnimator flightMiddleTop = ObjectAnimator.ofFloat(atk, "y", atk.getY());
        flightMiddleTop.setDuration(BattleFragment.ANIMATION_LONG / 4);
        ObjectAnimator flightMiddleAlpha = ObjectAnimator.ofFloat(atk, "alpha", 1f);
        flightMiddleAlpha.setDuration(BattleFragment.ANIMATION_LONG / 4);
        animatorSet.play(flightLeft).with(flightTop);
        animatorSet.play(flightLeft).with(flightAlpha);
        animatorSet.play(flightLeft).before(spinRight);
        animatorSet.play(spinRight).with(spinBottom);
        animatorSet.play(spinRight).with(spinRotation);
        animatorSet.play(flightMiddleLeft).after(spinRight);
        animatorSet.play(flightMiddleLeft).with(flightMiddleTop);
        animatorSet.play(flightMiddleLeft).with(flightMiddleAlpha);
        return animatorSet;
    }

    public static AnimatorSet trick(Context context, final RelativeLayout wrapper, final RelativeLayout atkC, final ImageView atk, final RelativeLayout defC, ImageView def) {
        AnimatorSet animatorSet = new AnimatorSet();
        final ImageView trick = new ImageView(context);
        trick.setImageResource(R.drawable.pokeball_available);
        final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        float startX = atkC.getX() + atk.getX() + atk.getWidth() * 0.5f;
        float startY = atkC.getY() + atk.getY() + atk.getHeight() * 0.5f;
        float endX = defC.getX() + def.getX() + def.getWidth() * 0.5f;
        float endY = defC.getY() + def.getY() + def.getHeight() * 0.5f;
        ObjectAnimator goX = ObjectAnimator.ofFloat(trick, "x", startX, endX);
        goX.setDuration(BattleFragment.ANIMATION_LONG / 2);
        ObjectAnimator goY = ObjectAnimator.ofFloat(trick, "y", startY, endY);
        goY.setDuration(BattleFragment.ANIMATION_LONG / 2);
        goX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                wrapper.addView(trick, layoutParams);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        ObjectAnimator backX = ObjectAnimator.ofFloat(trick, "x", startX);
        backX.setDuration(BattleFragment.ANIMATION_LONG / 2);
        ObjectAnimator backY = ObjectAnimator.ofFloat(trick, "y", startY);
        backY.setDuration(BattleFragment.ANIMATION_LONG / 2);
        backX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                wrapper.removeView(trick);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.play(goX).with(goY);
        animatorSet.play(backX).after(goX);
        animatorSet.play(backX).with(backY);
        return animatorSet;
    }

    public static float[] getCenter(View view) {
        float[] toReturn = new float[2];
        toReturn[0] = view.getX() + view.getWidth() * 0.5f;
        toReturn[1] = view.getY() + view.getHeight() * 0.5f;
        return toReturn;
    }

    public static ImageView getImageView(Context activityContext, int resource) {
        ImageView toReturn = new ImageView(activityContext);
        toReturn.setImageResource(resource);

        ViewGroup.LayoutParams layoutParams = toReturn.getLayoutParams();
        layoutParams.width = 32;
        layoutParams.height = 32;

        return toReturn;
    }

}