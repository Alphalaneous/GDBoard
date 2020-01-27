package Main;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

class ParseLevel {

	static void parse(byte[] level, String levelID) throws IOException {

		ArrayList<GDObject> lvlObject = new ArrayList<>();
		String decompressed = decompress(level);
		ArrayList<String[]> Objects = new ArrayList<>();

		String[] values = decompressed.split(";");
		for (String value : values) {
			if ((value.split(",")[1].equalsIgnoreCase("1110")) || (value.split(",")[1].equalsIgnoreCase("211"))
					|| (value.split(",")[1].equalsIgnoreCase("914"))) {
				//System.out.println(value.split(",")[1]);
				Objects.add(value.split(","));
			}
		}
		int IDImageCount = 0;
		outer: for (int i = 0; i < Objects.size(); i++) {
			lvlObject.add(new GDObject());
			for (int j = 0; j < Objects.get(i).length; j = j + 2) {
				if (Objects.get(i)[j].equals("1")) {
					lvlObject.get(i).ID(Double.parseDouble(Objects.get(i)[j + 1]));
				}
				// if(lvlObject.get(i).getID() == 1110 || lvlObject.get(i).getID() == 211 ||
				// lvlObject.get(i).getID() == 914) {
				/*
				 * if (Objects.get(i)[j].equals("2")) {
				 * lvlObject.get(i).X(Double.parseDouble(Objects.get(i)[j + 1])); } if
				 * (Objects.get(i)[j].equals("3")) {
				 * lvlObject.get(i).Y(Double.parseDouble(Objects.get(i)[j + 1])); } if
				 * (Objects.get(i)[j].equals("4")) {
				 * lvlObject.get(i).horizontalFlip(Double.parseDouble(Objects.get(i)[j + 1])); }
				 * if (Objects.get(i)[j].equals("5")) {
				 * lvlObject.get(i).verticalFlip(Double.parseDouble(Objects.get(i)[j + 1])); }
				 * if (Objects.get(i)[j].equals("6")) {
				 * lvlObject.get(i).rotation(Double.parseDouble(Objects.get(i)[j + 1])); } if
				 * (Objects.get(i)[j].equals("7")) {
				 * lvlObject.get(i).R(Double.parseDouble(Objects.get(i)[j + 1])); } if
				 * (Objects.get(i)[j].equals("8")) {
				 * lvlObject.get(i).G(Double.parseDouble(Objects.get(i)[j + 1])); } if
				 * (Objects.get(i)[j].equals("9")) {
				 * lvlObject.get(i).B(Double.parseDouble(Objects.get(i)[j + 1])); } if
				 * (Objects.get(i)[j].equals("10")) {
				 * lvlObject.get(i).duration(Double.parseDouble(Objects.get(i)[j + 1])); } if
				 * (Objects.get(i)[j].equals("11")) {
				 * lvlObject.get(i).touchTriggered(Double.parseDouble(Objects.get(i)[j + 1])); }
				 * if (Objects.get(i)[j].equals("12")) {
				 * lvlObject.get(i).secretCoinID(Double.parseDouble(Objects.get(i)[j + 1])); }
				 * if (Objects.get(i)[j].equals("13")) {
				 * lvlObject.get(i).specialObjectChecked(Double.parseDouble(Objects.get(i)[j +
				 * 1])); } if (Objects.get(i)[j].equals("14")) {
				 * lvlObject.get(i).tintGround(Double.parseDouble(Objects.get(i)[j + 1])); } if
				 * (Objects.get(i)[j].equals("15")) {
				 * lvlObject.get(i).setColorToP1(Double.parseDouble(Objects.get(i)[j + 1])); }
				 * if (Objects.get(i)[j].equals("16")) {
				 * lvlObject.get(i).setColorToP2(Double.parseDouble(Objects.get(i)[j + 1])); }
				 * if (Objects.get(i)[j].equals("17")) {
				 * lvlObject.get(i).blending(Double.parseDouble(Objects.get(i)[j + 1])); } if
				 * (Objects.get(i)[j].equals("18")) {
				 * lvlObject.get(i).unknown18(Double.parseDouble(Objects.get(i)[j + 1])); } if
				 * (Objects.get(i)[j].equals("19")) {
				 * lvlObject.get(i).unknown19(Double.parseDouble(Objects.get(i)[j + 1])); } if
				 * (Objects.get(i)[j].equals("20")) {
				 * lvlObject.get(i).EL1(Double.parseDouble(Objects.get(i)[j + 1])); }
				 */
				if (Objects.get(i)[j].equals("21")) {
					lvlObject.get(i).color1(Double.parseDouble(Objects.get(i)[j + 1]));
				} /*
					 * if (Objects.get(i)[j].equals("22")) {
					 * lvlObject.get(i).color2(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("23")) {
					 * lvlObject.get(i).targetColorID(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * if (Objects.get(i)[j].equals("24")) {
					 * lvlObject.get(i).ZLayer(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("25")) {
					 * lvlObject.get(i).ZOrder(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("26")) {
					 * lvlObject.get(i).unknown26(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("27")) {
					 * lvlObject.get(i).unknown27(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("28")) {
					 * lvlObject.get(i).offsetX(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("29")) {
					 * lvlObject.get(i).offsetY(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("30")) {
					 * lvlObject.get(i).easing(Double.parseDouble(Objects.get(i)[j + 1])); }
					 */
				if (Objects.get(i)[j].equals("31")) {
					String formatted = Objects.get(i)[j + 1].replace("_", "/").replace("-", "+");
					// System.out.println(formatted);
					String text = new String(Base64.getDecoder().decode(formatted));
					lvlObject.get(i).objectText(text);
				}
				if (Objects.get(i)[j].equals("32")) {
					lvlObject.get(i).scaling(Double.parseDouble(Objects.get(i)[j + 1]));
				} /*
					 * if (Objects.get(i)[j].equals("33")) {
					 * lvlObject.get(i).unknown33(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("34")) {
					 * lvlObject.get(i).groupParent(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("35")) {
					 * lvlObject.get(i).opacity(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("36")) {
					 * lvlObject.get(i).unknown36(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("37")) {
					 * lvlObject.get(i).unknown37(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("38")) {
					 * lvlObject.get(i).unknown38(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("39")) {
					 * lvlObject.get(i).unknown39(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("40")) {
					 * lvlObject.get(i).unknown40(Double.parseDouble(Objects.get(i)[j + 1])); }
					 */
				if (Objects.get(i)[j].equals("41")) {
					lvlObject.get(i).color1HSVEnabled(Double.parseDouble(Objects.get(i)[j + 1]));
				} /*
					 * if (Objects.get(i)[j].equals("42")) {
					 * lvlObject.get(i).color2HSVEnabled(Double.parseDouble(Objects.get(i)[j + 1]));
					 * } if (Objects.get(i)[j].equals("43")) {
					 * lvlObject.get(i).color1HSVValues(Objects.get(i)[j + 1]); } if
					 * (Objects.get(i)[j].equals("44")) {
					 * lvlObject.get(i).color2HSVValues(Objects.get(i)[j + 1]); } if
					 * (Objects.get(i)[j].equals("45")) {
					 * lvlObject.get(i).fadeIn(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("46")) {
					 * lvlObject.get(i).hold(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("47")) {
					 * lvlObject.get(i).fadeOut(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("48")) {
					 * lvlObject.get(i).pulseMode(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("49")) {
					 * lvlObject.get(i).copiedColorHSVValues(Objects.get(i)[j + 1]); } if
					 * (Objects.get(i)[j].equals("50")) {
					 * lvlObject.get(i).copiedColorID(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * if (Objects.get(i)[j].equals("51")) {
					 * lvlObject.get(i).targetGroupID(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * if (Objects.get(i)[j].equals("52")) {
					 * lvlObject.get(i).targetType(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("53")) {
					 * lvlObject.get(i).unknown53(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("54")) {
					 * lvlObject.get(i).yellowTeleportationPortalDistance(Double.parseDouble(Objects
					 * .get(i)[j + 1])); } if (Objects.get(i)[j].equals("55")) {
					 * lvlObject.get(i).unknown55(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("56")) {
					 * lvlObject.get(i).activateGroup(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * if (Objects.get(i)[j].equals("57")) {
					 * lvlObject.get(i).groupIDs(Objects.get(i)[j + 1]); } if
					 * (Objects.get(i)[j].equals("58")) {
					 * lvlObject.get(i).lockToPlayerX(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * if (Objects.get(i)[j].equals("59")) {
					 * lvlObject.get(i).lockToPlayerY(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * if (Objects.get(i)[j].equals("60")) {
					 * lvlObject.get(i).copyOpacity(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("61")) {
					 * lvlObject.get(i).EL2(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("62")) {
					 * lvlObject.get(i).spawnTriggered(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * if (Objects.get(i)[j].equals("63")) {
					 * lvlObject.get(i).spawnDelay(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("64")) {
					 * lvlObject.get(i).dontFade(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("65")) {
					 * lvlObject.get(i).mainOnly(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("66")) {
					 * lvlObject.get(i).detailOnly(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("67")) {
					 * lvlObject.get(i).dontEnter(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("68")) {
					 * lvlObject.get(i).degrees(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("69")) {
					 * lvlObject.get(i).times360(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("70")) {
					 * lvlObject.get(i).lockObjectRotation(Double.parseDouble(Objects.get(i)[j +
					 * 1])); } if (Objects.get(i)[j].equals("71")) {
					 * lvlObject.get(i).followTargetCenterSecondary(Double.parseDouble(Objects.get(i
					 * )[j + 1])); } if (Objects.get(i)[j].equals("72")) {
					 * lvlObject.get(i).xMod(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("73")) {
					 * lvlObject.get(i).yMod(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("74")) {
					 * lvlObject.get(i).unknownFollow(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * if (Objects.get(i)[j].equals("75")) {
					 * lvlObject.get(i).strength(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("76")) {
					 * lvlObject.get(i).animationID(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("77")) {
					 * lvlObject.get(i).count(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("78")) {
					 * lvlObject.get(i).subtractCount(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * if (Objects.get(i)[j].equals("79")) {
					 * lvlObject.get(i).pickupMode(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("80")) {
					 * lvlObject.get(i).itemBlockABID(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * if (Objects.get(i)[j].equals("81")) {
					 * lvlObject.get(i).holdMode(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("82")) {
					 * lvlObject.get(i).toggleMode(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("83")) {
					 * lvlObject.get(i).unknown83(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("84")) {
					 * lvlObject.get(i).interval(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("85")) {
					 * lvlObject.get(i).easingRate(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("86")) {
					 * lvlObject.get(i).exclusive(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("87")) {
					 * lvlObject.get(i).multiTrigger(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * if (Objects.get(i)[j].equals("88")) {
					 * lvlObject.get(i).comparison(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("89")) {
					 * lvlObject.get(i).dualMode(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("90")) {
					 * lvlObject.get(i).speed(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("91")) {
					 * lvlObject.get(i).followDelay(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("92")) {
					 * lvlObject.get(i).yOffset(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("93")) {
					 * lvlObject.get(i).triggerOnExit(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * if (Objects.get(i)[j].equals("94")) {
					 * lvlObject.get(i).dynamicBlock(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * if (Objects.get(i)[j].equals("95")) {
					 * lvlObject.get(i).blockBID(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("96")) {
					 * lvlObject.get(i).disableGlow(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("97")) {
					 * lvlObject.get(i).customRotationSpeed(Double.parseDouble(Objects.get(i)[j +
					 * 1])); } if (Objects.get(i)[j].equals("98")) {
					 * lvlObject.get(i).disableRotation(Double.parseDouble(Objects.get(i)[j + 1]));
					 * } if (Objects.get(i)[j].equals("99")) {
					 * lvlObject.get(i).multiActivate(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * if (Objects.get(i)[j].equals("100")) {
					 * lvlObject.get(i).enableUseTarget(Double.parseDouble(Objects.get(i)[j + 1]));
					 * } if (Objects.get(i)[j].equals("101")) {
					 * lvlObject.get(i).targetPosCoordinates(Double.parseDouble(Objects.get(i)[j +
					 * 1])); } if (Objects.get(i)[j].equals("102")) {
					 * lvlObject.get(i).editorDisable(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * if (Objects.get(i)[j].equals("103")) {
					 * lvlObject.get(i).highDetail(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("104")) {
					 * lvlObject.get(i).unknown104(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("105")) {
					 * lvlObject.get(i).maxSpeed(Double.parseDouble(Objects.get(i)[j + 1])); } if
					 * (Objects.get(i)[j].equals("106")) {
					 * lvlObject.get(i).randomizeStart(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * if (Objects.get(i)[j].equals("107")) {
					 * lvlObject.get(i).animationSpeed(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * if (Objects.get(i)[j].equals("108")) {
					 * lvlObject.get(i).linkedGroupID(Double.parseDouble(Objects.get(i)[j + 1])); }
					 * /*System.out.println("ID: " + lvlObject.get(i).getID());
					 * System.out.println("X: " + lvlObject.get(i).getX()); System.out.println("Y: "
					 * + lvlObject.get(i).getY()); System.out.println("Flipped Horizontally: " +
					 * lvlObject.get(i).getHorizontalFlip());
					 * System.out.println("Flipped Vertically: " +
					 * lvlObject.get(i).getVerticalFlip()); System.out.println("Rotation: " +
					 * lvlObject.get(i).getRotation()); System.out.println("R: " +
					 * lvlObject.get(i).getR()); System.out.println("G: " +
					 * lvlObject.get(i).getG()); System.out.println("B: " +
					 * lvlObject.get(i).getB()); System.out.println("Duration: " +
					 * lvlObject.get(i).getDuration()); System.out.println("TouchTriggered: " +
					 * lvlObject.get(i).getTouchTriggered()); System.out.println("SecretCoinID: " +
					 * lvlObject.get(i).getSecretCoinID());
					 * System.out.println("SpecialObjectChecked: " +
					 * lvlObject.get(i).getSpecialObjectChecked());
					 * System.out.println("TintGround: " + lvlObject.get(i).getTintGround());
					 * System.out.println("SetColorToPlayerColor1: " +
					 * lvlObject.get(i).getSetColorToP1());
					 * System.out.println("SetColorToPlayerColor2: " +
					 * lvlObject.get(i).getSetColorToP2()); System.out.println("Blending: " +
					 * lvlObject.get(i).getBlending()); System.out.println("UnknownFeature18: " +
					 * lvlObject.get(i).getUnknown18()); System.out.println("UnknownFeature19: " +
					 * lvlObject.get(i).getUnknown19()); System.out.println("EL1: " +
					 * lvlObject.get(i).getEL1()); System.out.println("Color1: " +
					 * lvlObject.get(i).getColor1()); System.out.println("Color2: " +
					 * lvlObject.get(i).getColor2()); System.out.println("TargetColorID: " +
					 * lvlObject.get(i).getTargetColorID()); System.out.println("ZLayer: " +
					 * lvlObject.get(i).getZLayer()); System.out.println("ZOrder: " +
					 * lvlObject.get(i).getZOrder()); System.out.println("UnknownFeature26: " +
					 * lvlObject.get(i).getUnknown26()); System.out.println("UnknownFeature27: " +
					 * lvlObject.get(i).getUnknown27()); System.out.println("OffsetX: " +
					 * lvlObject.get(i).getOffsetX()); System.out.println("OffsetY: " +
					 * lvlObject.get(i).getOffsetY()); System.out.println("Easing: " +
					 * lvlObject.get(i).getEasing()); System.out.println("TextObjectText: " +
					 * lvlObject.get(i).getObjectText()); System.out.println("Scaling: " +
					 * lvlObject.get(i).getScaling()); System.out.println("UnknownFeature33: " +
					 * lvlObject.get(i).getUnknown33()); System.out.println("GroupParent: " +
					 * lvlObject.get(i).getGroupParent()); System.out.println("Opacity: " +
					 * lvlObject.get(i).getOpacity()); System.out.println("UnknownFeature36: " +
					 * lvlObject.get(i).getUnknown36()); System.out.println("UnknownFeature37: " +
					 * lvlObject.get(i).getUnknown37()); System.out.println("UnknownFeature38: " +
					 * lvlObject.get(i).getUnknown38()); System.out.println("UnknownFeature39: " +
					 * lvlObject.get(i).getUnknown39()); System.out.println("UnknownFeature40: " +
					 * lvlObject.get(i).getUnknown40()); System.out.println("Color1HSVEnabled: " +
					 * lvlObject.get(i).getColor1HSVEnabled());
					 * System.out.println("Color2HSVEnabled: " +
					 * lvlObject.get(i).getColor2HSVEnabled()); System.out.println("FadeIn: " +
					 * lvlObject.get(i).getFadeIn()); System.out.println("Hold: " +
					 * lvlObject.get(i).getHold()); System.out.println("FadeOut: " +
					 * lvlObject.get(i).getFadeOut()); System.out.println("PulseMode: " +
					 * lvlObject.get(i).getPulseMode()); System.out.println("CopiedColorID: " +
					 * lvlObject.get(i).getCopiedColorID()); System.out.println("TargetGroupID: " +
					 * lvlObject.get(i).getTargetGroupID()); System.out.println("TargetType: " +
					 * lvlObject.get(i).getTargetType()); System.out.println("UnknownFeature53: " +
					 * lvlObject.get(i).getUnknown53());
					 * System.out.println("YellowTeleportationPortalDistance: " +
					 * lvlObject.get(i).getYellowTeleportationPortalDistance());
					 * System.out.println("UnknownFeature55: " + lvlObject.get(i).getUnknown55());
					 * System.out.println("ActivateGroup: " + lvlObject.get(i).getActivateGroup());
					 * System.out.println("GroupIDs: " + lvlObject.get(i).getGroupIDs());
					 * System.out.println("LockToPlayerX: " + lvlObject.get(i).getLockToPlayerX());
					 * System.out.println("LockToPlayerY: " + lvlObject.get(i).getLockToPlayerY());
					 * System.out.println("CopyOpacity: " + lvlObject.get(i).getCopyOpacity());
					 * System.out.println("EL2: " + lvlObject.get(i).getEL2());
					 * System.out.println("SpawnTriggered: " +
					 * lvlObject.get(i).getSpawnTriggered()); System.out.println("SpawnDelay: " +
					 * lvlObject.get(i).getSpawnDelay()); System.out.println("DontFade: " +
					 * lvlObject.get(i).getDontFade()); System.out.println("MainOnly: " +
					 * lvlObject.get(i).getMainOnly()); System.out.println("DetailOnly: " +
					 * lvlObject.get(i).getDetailOnly()); System.out.println("DontEnter: " +
					 * lvlObject.get(i).getDontEnter()); System.out.println("Degrees: " +
					 * lvlObject.get(i).getDegrees()); System.out.println("Times360: " +
					 * lvlObject.get(i).getTimes360()); System.out.println("LockObjectRotation: " +
					 * lvlObject.get(i).getLockObjectRotation()); System.out.
					 * println("FollowGroupID/TargetPosGroupID/CenterGroupID/SecondaryGroupID: " +
					 * lvlObject.get(i).getFollowTargetCenterSecondary());
					 * System.out.println("XMod: " + lvlObject.get(i).getxMod());
					 * System.out.println("YMod: " + lvlObject.get(i).getyMod());
					 * System.out.println("UnknownFollowTriggerFeature: " +
					 * lvlObject.get(i).getUnknownFollow()); System.out.println("Strength: " +
					 * lvlObject.get(i).getStrength()); System.out.println("AnimationID: " +
					 * lvlObject.get(i).getAnimationID()); System.out.println("Count: " +
					 * lvlObject.get(i).getCount()); System.out.println("SubtractCount: " +
					 * lvlObject.get(i).getSubtractCount()); System.out.println("PickupMode: " +
					 * lvlObject.get(i).getPickupMode());
					 * System.out.println("ItemID/BlockID/BlockAID: " +
					 * lvlObject.get(i).getItemBlockABID()); System.out.println("HoldMode: " +
					 * lvlObject.get(i).getHoldMode()); System.out.println("ToggleMode: " +
					 * lvlObject.get(i).getToggleMode()); System.out.println("UnknownFeature83: " +
					 * lvlObject.get(i).getUnknown83()); System.out.println("Interval: " +
					 * lvlObject.get(i).getInterval()); System.out.println("EasingRate: " +
					 * lvlObject.get(i).getEasingRate()); System.out.println("Exclusive: " +
					 * lvlObject.get(i).getExclusive()); System.out.println("MultiTrigger: " +
					 * lvlObject.get(i).getMultiTrigger()); System.out.println("Comparison: " +
					 * lvlObject.get(i).getComparison()); System.out.println("DualMode: " +
					 * lvlObject.get(i).getDualMode()); System.out.println("Speed: " +
					 * lvlObject.get(i).getSpeed()); System.out.println("FollowDelay: " +
					 * lvlObject.get(i).getFollowDelay()); System.out.println("YOffset: " +
					 * lvlObject.get(i).getyOffset()); System.out.println("TriggerOnExit: " +
					 * lvlObject.get(i).getTriggerOnExit()); System.out.println("DynamicBlock: " +
					 * lvlObject.get(i).getDynamicBlock()); System.out.println("BlockBID: " +
					 * lvlObject.get(i).getBlockBID()); System.out.println("DisableGlow: " +
					 * lvlObject.get(i).getDisableGlow());
					 * System.out.println("CustomRotationSpeed: " +
					 * lvlObject.get(i).getCustomRotationSpeed());
					 * System.out.println("DisableRotation: " +
					 * lvlObject.get(i).getDisableRotation()); System.out.println("MultiActivate: "
					 * + lvlObject.get(i).getMultiActivate());
					 * System.out.println("EnableUseTarget: " +
					 * lvlObject.get(i).getEnableUseTarget());
					 * System.out.println("TargetPosCoordinates: " +
					 * lvlObject.get(i).getTargetPosCoordinates());
					 * System.out.println("EditorDisable: " + lvlObject.get(i).getEditorDisable());
					 * System.out.println("HighDetail: " + lvlObject.get(i).getHighDetail());
					 * System.out.println("UnknownFeature104: " + lvlObject.get(i).getUnknown104());
					 * System.out.println("MaxSpeed: " + lvlObject.get(i).getMaxSpeed());
					 * System.out.println("RandomizeStart: " +
					 * lvlObject.get(i).getRandomizeStart()); System.out.println("AnimationSpeed: "
					 * + lvlObject.get(i).getAnimationSpeed()); System.out.println("LinkedGroupID: "
					 * + lvlObject.get(i).getLinkedGroupID()); System.out.println("Color 1 Hue: " +
					 * lvlObject.get(i).getColor1Hue()); System.out.println("Color 1 Saturation: " +
					 * lvlObject.get(i).getColor1Saturation());
					 * System.out.println("Color 1 Brightness: " +
					 * lvlObject.get(i).getColor1Brightness());
					 * System.out.println("Color 1 Saturation Check: " +
					 * lvlObject.get(i).getColor1SatCheck());
					 * System.out.println("Color 1 Brightness Check: " +
					 * lvlObject.get(i).getColor1BriCheck()); System.out.println("Color 2 Hue: " +
					 * lvlObject.get(i).getColor2Hue()); System.out.println("Color 2 Saturation: " +
					 * lvlObject.get(i).getColor2Saturation());
					 * System.out.println("Color 2 Brightness: " +
					 * lvlObject.get(i).getColor2Brightness());
					 * System.out.println("Color 2 Saturation Check: " +
					 * lvlObject.get(i).getColor2SatCheck());
					 * System.out.println("Color Brightness Check: " +
					 * lvlObject.get(i).getColor2BriCheck()); System.out.println("Copy Color Hue: "
					 * + lvlObject.get(i).getCopyColorHue());
					 * System.out.println("Copy Color Saturation: " +
					 * lvlObject.get(i).getCopyColorSaturation());
					 * System.out.println("Copy Color Brightness: " +
					 * lvlObject.get(i).getCopyColorBrightness());
					 * System.out.println("Copy Color Saturation Check: " +
					 * lvlObject.get(i).getCopyColorSatCheck());
					 * System.out.println("Copy Color Brightness Check: " +
					 * lvlObject.get(i).getCopyColorBriCheck()); System.out.println("\n[Object " + i
					 * + "]\n");
					 */

				File file = new File("blockedWords.txt");
				Scanner sc = new Scanner(file);

				for (int k = 0; k < Requests.getLevelData().size(); k++) {
					if (Requests.getLevelData().get(k).getLevelID().equalsIgnoreCase(levelID)) {
						out: while (sc.hasNextLine()) {
							String[] text = lvlObject.get(i).getObjectText().toUpperCase().split(" ");
							String word = sc.nextLine();

							for (String s : text) {

								if (s.equalsIgnoreCase(word)) {
									System.out.println("Contains Vulgar");
									Requests.getLevelData().get(k).setContainsVulgar();
									break out;
								}
							}
							
						}
						if (lvlObject.get(i).getID() == 1110 || lvlObject.get(i).getID() == 211
								&& lvlObject.get(i).getScaling() <= 0.1 && lvlObject.get(i).getScaling() != 0.0
								&& lvlObject.get(i).getColor1HSVEnabled() == 1) {
							double color = lvlObject.get(i).getColor1();
							if (lvlObject.get(i).getColor1() == color) {
								IDImageCount++;
							}
						}
						if (IDImageCount >= 3000) {
							System.out.println("Contains Image Hack");

							Requests.getLevelData().get(k).setContainsImage();
							break outer;
						}
						sc.close();
					}
				}
			}

		}
		// }

	}

	private static String decompress(byte[] compressed) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
		GZIPInputStream gis = new GZIPInputStream(bis);
		BufferedReader br = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		gis.close();
		bis.close();
		return sb.toString();
	}
}
