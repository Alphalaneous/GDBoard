package Main;

import java.util.ArrayList;
import java.util.Arrays;

public class GDObject {

    private double id;
    private double x;
    private double y;
    private double horizontalFlip;
    private double verticalFlip;
    private double rotation;
    private double r;
    private double g;
    private double b;
    private double duration;
    private double touchTriggered;
    private double secretCoinID;
    private double specialObjectChecked;
    private double tintGround;
    private double setColorToP1;
    private double setColorToP2;
    private double blending;
    private double unknown18;
    private double unknown19;
    private double EL1;
    private double color1;
    private double color2;
    private double targetColorID;
    private double ZLayer;
    private double ZOrder;
    private double unknown26;
    private double unknown27;
    private double offsetX;
    private double offsetY;
    private double easing;
    private String objectText = "";
    private double scaling;
    private double unknown33;
    private double groupParent;
    private double opacity;
    private double unknown36;
    private double unknown37;
    private double unknown38;
    private double unknown39;
    private double unknown40;
    private double color1HSVEnabled;
    private double color2HSVEnabled;
    private double fadeIn;
    private double hold;
    private double fadeOut;
    private double pulseMode;
    private double copiedColorID;
    private double targetGroupID;
    private double targetType;
    private double unknown53;
    private double yellowTeleportationPortalDistance;
    private double unknown55;
    private double activateGroup;
    private String groupIDs;
    private double lockToPlayerX;
    private double lockToPlayerY;
    private double copyOpacity;
    private double EL2;
    private double spawnTriggered;
    private double spawnDelay;
    private double dontFade;
    private double mainOnly;
    private double detailOnly;
    private double dontEnter;
    private double degrees;
    private double times360;
    private double lockObjectRotation;
    private double followTargetCenterSecondary;
    private double xMod;
    private double yMod;
    private double unknownFollow;
    private double strength;
    private double animationID;
    private double count;
    private double subtractCount;
    private double pickupMode;
    private double itemBlockABID;
    private double holdMode;
    private double toggleMode;
    private double unknown83;
    private double interval;
    private double easingRate;
    private double exclusive;
    private double multiTrigger;
    private double comparison;
    private double dualMode;
    private double speed;
    private double followDelay;
    private double yOffset;
    private double triggerOnExit;
    private double dynamicBlock;
    private double blockBID;
    private double disableGlow;
    private double customRotationSpeed;
    private double disableRotation;
    private double multiActivate;
    private double enableUseTarget;
    private double targetPosCoordinates;
    private double editorDisable;
    private double highDetail;
    private double unknown104;
    private double maxSpeed;
    private double randomizeStart;
    private double animationSpeed;
    private double linkedGroupID;
    private double color1Hue;
    private double color1Saturation;
    private double color1Brightness;
    private double color1SatCheck;
    private double color1BriCheck;
    private double color2Hue;
    private double color2Saturation;
    private double color2Brightness;
    private double color2SatCheck;
    private double color2BriCheck;
    private double copyColorHue;
    private double copyColorSaturation;
    private double copyColorBrightness;
    private double copyColorSatCheck;
    private double copyColorBriCheck;

    void ID(double value) {
        id = value;
    }

    void X(double value) {
        x = value;
    }

    void Y(double value) {
        y = value;
    }

    void horizontalFlip(double value) {
        horizontalFlip = value;
    }

    void verticalFlip(double value) {
        verticalFlip = value;
    }

    void rotation(double value) {
        rotation = value;
    }

    void R(double value) {
        r = value;
    }

    void G(double value) {
        g = value;
    }

    void B(double value) {
        b = value;
    }

    void duration(double value) {
        duration = value;
    }

    void touchTriggered(double value) {
        touchTriggered = value;
    }

    void secretCoinID(double value) {
        secretCoinID = value;
    }

    void specialObjectChecked(double value) {
        specialObjectChecked = value;
    }

    void tintGround(double value) {
        tintGround = value;
    }

    void setColorToP1(double value) {
        setColorToP1 = value;
    }

    void setColorToP2(double value) {
        setColorToP2 = value;
    }

    void blending(double value) {
        blending = value;
    }

    void unknown18(double value) {
        unknown18 = value;
    }

    void unknown19(double value) {
        unknown19 = value;
    }

    void EL1(double value) {
        EL1 = value;
    }

    void color1(double value) {
        color1 = value;
    }

    void color2(double value) {
        color2 = value;
    }

    void targetColorID(double value) {
        targetColorID = value;
    }

    void ZLayer(double value) {
        ZLayer = value;
    }

    void ZOrder(double value) {
        ZOrder = value;
    }

    void unknown26(double value) {
        unknown26 = value;
    }

    void unknown27(double value) {
        unknown27 = value;
    }

    void offsetX(double value) {
        offsetX = value;
    }

    void offsetY(double value) {
        offsetY = value;
    }

    void easing(double value) {
        easing = value;
    }

    void objectText(String value) {
        objectText = value;
    }

    void scaling(double value) {
        scaling = value;
    }

    void unknown33(double value) {
        unknown33 = value;
    }

    void groupParent(double value) {
        groupParent = value;
    }

    void opacity(double value) {
        opacity = value;
    }

    void unknown36(double value) {
        unknown36 = value;
    }

    void unknown37(double value) {
        unknown37 = value;
    }

    void unknown38(double value) {
        unknown38 = value;
    }

    void unknown39(double value) {
        unknown39 = value;
    }

    void unknown40(double value) {
        unknown40 = value;
    }

    void color1HSVEnabled(double value) {
        color1HSVEnabled = value;
    }

    void color2HSVEnabled(double value) {
        color2HSVEnabled = value;
    }

    void color1HSVValues(String value) {
        ArrayList<String> HSVInfo = new ArrayList<>(Arrays.asList(value.split("a")));
        color1Hue = Double.parseDouble((String) HSVInfo.get(0));
        color1Saturation = Double.parseDouble((String) HSVInfo.get(1));
        color1Brightness = Double.parseDouble((String) HSVInfo.get(2));
        color1SatCheck = Double.parseDouble((String) HSVInfo.get(3));
        color1BriCheck = Double.parseDouble((String) HSVInfo.get(4));
    }

    void color2HSVValues(String value) {
        ArrayList<String> HSVInfo = new ArrayList<>(Arrays.asList(value.split("a")));
        color2Hue = Double.parseDouble((String) HSVInfo.get(0));
        color2Saturation = Double.parseDouble((String) HSVInfo.get(1));
        color2Brightness = Double.parseDouble((String) HSVInfo.get(2));
        color2SatCheck = Double.parseDouble((String) HSVInfo.get(3));
        color2BriCheck = Double.parseDouble((String) HSVInfo.get(4));
    }

    void fadeIn(double value) {
        fadeIn = value;
    }

    void hold(double value) {
        hold = value;
    }

    void fadeOut(double value) {
        fadeOut = value;
    }

    void pulseMode(double value) {
        pulseMode = value;
    }

    void copiedColorHSVValues(String value) {
        ArrayList<String> HSVInfo = new ArrayList<>(Arrays.asList(value.split("a")));
        copyColorHue = Double.parseDouble((String) HSVInfo.get(0));
        copyColorSaturation = Double.parseDouble((String) HSVInfo.get(1));
        copyColorBrightness = Double.parseDouble((String) HSVInfo.get(2));
        copyColorSatCheck = Double.parseDouble((String) HSVInfo.get(3));
        copyColorBriCheck = Double.parseDouble((String) HSVInfo.get(4));
    }

    void copiedColorID(double value) {
        copiedColorID = value;
    }

    void targetGroupID(double value) {
        targetGroupID = value;
    }

    void targetType(double value) {
        targetType = value;
    }

    void unknown53(double value) {
        unknown53 = value;
    }

    void yellowTeleportationPortalDistance(double value) {
        yellowTeleportationPortalDistance = value;
    }

    void unknown55(double value) {
        unknown55 = value;
    }

    void activateGroup(double value) {
        activateGroup = value;
    }

    void groupIDs(String value) {
        groupIDs = value;
    }

    void lockToPlayerX(double value) {
        lockToPlayerX = value;
    }

    void lockToPlayerY(double value) {
        lockToPlayerY = value;
    }

    void copyOpacity(double value) {
        copyOpacity = value;
    }

    void EL2(double value) {
        EL2 = value;
    }

    void spawnTriggered(double value) {
        spawnTriggered = value;
    }

    void spawnDelay(double value) {
        spawnDelay = value;
    }

    void dontFade(double value) {
        dontFade = value;
    }

    void mainOnly(double value) {
        mainOnly = value;
    }

    void detailOnly(double value) {
        detailOnly = value;
    }

    void dontEnter(double value) {
        dontEnter = value;
    }

    void degrees(double value) {
        degrees = value;
    }

    void times360(double value) {
        times360 = value;
    }

    void lockObjectRotation(double value) {
        lockObjectRotation = value;
    }

    void followTargetCenterSecondary(double value) {
        followTargetCenterSecondary = value;
    }

    void xMod(double value) {
        xMod = value;
    }

    void yMod(double value) {
        yMod = value;
    }

    void unknownFollow(double value) {
        unknownFollow = value;
    }

    void strength(double value) {
        strength = value;
    }

    void animationID(double value) {
        animationID = value;
    }

    void count(double value) {
        count = value;
    }

    void subtractCount(double value) {
        subtractCount = value;
    }

    void pickupMode(double value) {
        pickupMode = value;
    }

    void itemBlockABID(double value) {
        itemBlockABID = value;
    }

    void holdMode(double value) {
        holdMode = value;
    }

    void toggleMode(double value) {
        toggleMode = value;
    }

    void unknown83(double value) {
        unknown83 = value;
    }

    void interval(double value) {
        interval = value;
    }

    void easingRate(double value) {
        easingRate = value;
    }

    void exclusive(double value) {
        exclusive = value;
    }

    void multiTrigger(double value) {
        multiTrigger = value;
    }

    void comparison(double value) {
        comparison = value;
    }

    void dualMode(double value) {
        dualMode = value;
    }

    void speed(double value) {
        speed = value;
    }

    void followDelay(double value) {
        followDelay = value;
    }

    void yOffset(double value) {
        yOffset = value;
    }

    void triggerOnExit(double value) {
        triggerOnExit = value;
    }

    void dynamicBlock(double value) {
        dynamicBlock = value;
    }

    void blockBID(double value) {
        blockBID = value;
    }

    void disableGlow(double value) {
        disableGlow = value;
    }

    void customRotationSpeed(double value) {
        customRotationSpeed = value;
    }

    void disableRotation(double value) {
        disableRotation = value;
    }

    void multiActivate(double value) {
        multiActivate = value;
    }

    void enableUseTarget(double value) {
        enableUseTarget = value;
    }

    void targetPosCoordinates(double value) {
        targetPosCoordinates = value;
    }

    void editorDisable(double value) {
        editorDisable = value;
    }

    void highDetail(double value) {
        highDetail = value;
    }

    void unknown104(double value) {
        unknown104 = value;
    }

    void maxSpeed(double value) {
        maxSpeed = value;
    }

    void randomizeStart(double value) {
        randomizeStart = value;
    }

    void animationSpeed(double value) {
        animationSpeed = value;
    }

    void linkedGroupID(double value) {
        linkedGroupID = value;
    }

    double getID() {
        return id;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

    double getHorizontalFlip() {
        return horizontalFlip;
    }

    double getVerticalFlip() {
        return verticalFlip;
    }

    double getRotation() {
        return rotation;
    }

    double getR() {
        return r;
    }

    double getG() {
        return g;
    }

    double getB() {
        return b;
    }

    double getDuration() {
        return duration;
    }

    double getTouchTriggered() {
        return touchTriggered;
    }

    double getSecretCoinID() {
        return secretCoinID;
    }

    double getSpecialObjectChecked() {
        return specialObjectChecked;
    }

    double getTintGround() {
        return tintGround;
    }

    double getSetColorToP1() {
        return setColorToP1;
    }

    double getSetColorToP2() {
        return setColorToP2;
    }

    double getBlending() {
        return blending;
    }

    double getUnknown18() {
        return unknown18;
    }

    double getUnknown19() {
        return unknown19;
    }

    double getEL1() {
        return EL1;
    }

    double getColor1() {
        return color1;
    }

    double getColor2() {
        return color2;
    }

    double getTargetColorID() {
        return targetColorID;
    }

    double getZLayer() {
        return ZLayer;
    }

    double getZOrder() {
        return ZOrder;
    }

    double getUnknown26() {
        return unknown26;
    }

    double getUnknown27() {
        return unknown27;
    }

    double getOffsetX() {
        return offsetX;
    }

    double getOffsetY() {
        return offsetY;
    }

    double getEasing() {
        return easing;
    }

    String getObjectText() {
        return objectText;
    }

    double getScaling() {
        return scaling;
    }

    double getUnknown33() {
        return unknown33;
    }

    double getGroupParent() {
        return groupParent;
    }

    double getOpacity() {
        return opacity;
    }

    double getUnknown36() {
        return unknown36;
    }

    double getUnknown37() {
        return unknown37;
    }

    double getUnknown38() {
        return unknown38;
    }

    double getUnknown39() {
        return unknown39;
    }

    double getUnknown40() {
        return unknown40;
    }

    double getColor1HSVEnabled() {
        return color1HSVEnabled;
    }

    double getColor2HSVEnabled() {
        return color2HSVEnabled;
    }

    double getFadeIn() {
        return fadeIn;
    }

    double getHold() {
        return hold;
    }

    double getFadeOut() {
        return fadeOut;
    }

    double getPulseMode() {
        return pulseMode;
    }

    double getCopiedColorID() {
        return copiedColorID;
    }

    double getTargetGroupID() {
        return targetGroupID;
    }

    double getTargetType() {
        return targetType;
    }

    double getUnknown53() {
        return unknown53;
    }

    double getYellowTeleportationPortalDistance() {
        return yellowTeleportationPortalDistance;
    }

    double getUnknown55() {
        return unknown55;
    }

    double getActivateGroup() {
        return activateGroup;
    }

    String getGroupIDs() {
        return groupIDs;
    }

    double getLockToPlayerX() {
        return lockToPlayerX;
    }

    double getLockToPlayerY() {
        return lockToPlayerY;
    }

    double getCopyOpacity() {
        return copyOpacity;
    }

    double getEL2() {
        return EL2;
    }

    double getSpawnTriggered() {
        return spawnTriggered;
    }

    double getSpawnDelay() {
        return spawnDelay;
    }

    double getDontFade() {
        return dontFade;
    }

    double getMainOnly() {
        return mainOnly;
    }

    double getDetailOnly() {
        return detailOnly;
    }

    double getDontEnter() {
        return dontEnter;
    }

    double getDegrees() {
        return degrees;
    }

    double getTimes360() {
        return times360;
    }

    double getLockObjectRotation() {
        return lockObjectRotation;
    }

    double getFollowTargetCenterSecondary() {
        return followTargetCenterSecondary;
    }

    double getxMod() {
        return xMod;
    }

    double getyMod() {
        return yMod;
    }

    double getUnknownFollow() {
        return unknownFollow;
    }

    double getStrength() {
        return strength;
    }

    double getAnimationID() {
        return animationID;
    }

    double getCount() {
        return count;
    }

    double getSubtractCount() {
        return subtractCount;
    }

    double getPickupMode() {
        return pickupMode;
    }

    double getItemBlockABID() {
        return itemBlockABID;
    }

    double getHoldMode() {
        return holdMode;
    }

    double getToggleMode() {
        return toggleMode;
    }

    double getUnknown83() {
        return unknown83;
    }

    double getInterval() {
        return interval;
    }

    double getEasingRate() {
        return easingRate;
    }

    double getExclusive() {
        return exclusive;
    }

    double getMultiTrigger() {
        return multiTrigger;
    }

    double getComparison() {
        return comparison;
    }

    double getDualMode() {
        return dualMode;
    }

    double getSpeed() {
        return speed;
    }

    double getFollowDelay() {
        return followDelay;
    }

    double getyOffset() {
        return yOffset;
    }

    double getTriggerOnExit() {
        return triggerOnExit;
    }

    double getDynamicBlock() {
        return dynamicBlock;
    }

    double getBlockBID() {
        return blockBID;
    }

    double getDisableGlow() {
        return disableGlow;
    }

    double getCustomRotationSpeed() {
        return customRotationSpeed;
    }

    double getDisableRotation() {
        return disableRotation;
    }

    double getMultiActivate() {
        return multiActivate;
    }

    double getEnableUseTarget() {
        return enableUseTarget;
    }

    double getTargetPosCoordinates() {
        return targetPosCoordinates;
    }

    double getEditorDisable() {
        return editorDisable;
    }

    double getHighDetail() {
        return highDetail;
    }

    double getUnknown104() {
        return unknown104;
    }

    double getMaxSpeed() {
        return maxSpeed;
    }

    double getRandomizeStart() {
        return randomizeStart;
    }

    double getAnimationSpeed() {
        return animationSpeed;
    }

    double getLinkedGroupID() {
        return linkedGroupID;
    }
    double getColor1Hue() {
        return color1Hue;
    }

    double getColor1Saturation() {
        return color1Saturation;
    }

    double getColor1Brightness() {
        return color1Brightness;
    }

    double getColor1SatCheck() {
        return color1SatCheck;
    }

    double getColor1BriCheck() {
        return color1BriCheck;
    }

    double getColor2Hue() {
        return color2Hue;
    }

    double getColor2Saturation() {
        return color2Saturation;
    }

    double getColor2Brightness() {
        return color2Brightness;
    }

    double getColor2SatCheck() {
        return color2SatCheck;
    }

    double getColor2BriCheck() {
        return color2BriCheck;
    }

    double getCopyColorHue() {
        return copyColorHue;
    }

    double getCopyColorSaturation() {
        return copyColorSaturation;
    }

    double getCopyColorBrightness() {
        return copyColorBrightness;
    }

    double getCopyColorSatCheck() {
        return copyColorSatCheck;
    }

    double getCopyColorBriCheck() {
        return copyColorBriCheck;
    }

}