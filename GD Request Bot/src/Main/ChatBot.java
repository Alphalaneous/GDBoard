package Main;

import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;
import com.cavariux.twitchirc.Core.TwitchBot;
import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.client.GDClientBuilder;
import com.github.alex1304.jdash.entity.GDLevel;
import com.github.alex1304.jdash.exception.MissingAccessException;
import com.github.alex1304.jdash.util.LevelSearchFilters;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatBot extends TwitchBot {

	ChatBot() {

		this.setUsername("chatbot");
		System.out.println(Settings.getOAuth());
		this.setOauth_Key("oauth:" + Settings.getOAuth());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onMessage(User user, Channel channel, String msg) {

		// --------------------
		// If the bot sends the message, don't read it
		// TODO: Account Adding

		if (!String.valueOf(user).equals("betalaneous")) {

			// --------------------
			// If message does not start with "!"
			// TODO: Clean up code, remove redundancy

			if (!msg.startsWith("!")) {

				// --------------------
				// Get request from chat without commands

				Matcher m = Pattern.compile("\\s*(\\d{6,})\\s*").matcher(msg);
				if (m.find()) {
					try {
						Requests.addRequest(m.group(1), user.toString());

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			// --------------------
			// If message does start with "!" (Is a command)

			else if (msg.startsWith("!")) {

				// --------------------
				// Replace "!" with nothing

				msg = msg.replaceAll("[!?,]", "");

				// --------------------
				// Split message into arguments, get first part of command

				String command = msg.split("\\s+")[0];
				String[] arguments = msg.split("\\s+");

				// --------------------
				// Queue Command
				// TODO: Queue pages

				try {
					if (command.equalsIgnoreCase("ID") || command.equalsIgnoreCase("name")
							|| command.equalsIgnoreCase("current") || command.equalsIgnoreCase("level")) {
						Main.sendMessage("The current level is " + Requests.levels.get(0).getName() + " ("
								+ Requests.levels.get(0).getLevelID() + ")");
					}
					if (command.equalsIgnoreCase("song")) {
						Main.sendMessage("The song is " + Requests.levels.get(0).getSongName() + " by "
								+ Requests.levels.get(0).getSongAuthor() + " (" + Requests.levels.get(0).getSongID()
								+ ")");
					}
					if (command.equalsIgnoreCase("likes")) {
						Main.sendMessage("There are " + Requests.levels.get(0).getLikes() + " likes!");
					}
					if (command.equalsIgnoreCase("downloads")) {
						Main.sendMessage("There are " + Requests.levels.get(0).getDownloads() + " downloads!");
					}
					if (command.equalsIgnoreCase("difficulty")) {
						Main.sendMessage("The difficulty is " + Requests.levels.get(0).getDifficulty().toLowerCase());
					}
					
					
					
				} catch (IndexOutOfBoundsException ignored) {

				}

				if (command.equalsIgnoreCase("remove")) {
					for (int i = 0; i < Requests.levels.size(); i++) {
						try {
						if (Requests.levels.get(i).getLevelID().equals(Requests.levels
								.get((Integer.parseInt(arguments[1])) - 1).getLevelID())
								&& user.toString().equalsIgnoreCase(Requests.levels.get(i).getRequester())) {
							LevelsWindow2.removeButton(i);
							Requests.levels.remove(i);
							SongWindow.refreshInfo();
							InfoWindow.refreshInfo();
							Main.sendMessage("@" + user.toString() + ", your level has been removed!");
						}
						}
						catch(NumberFormatException e) {
							e.printStackTrace();
						}
					}
				}

				if (command.equalsIgnoreCase("q") || command.equalsIgnoreCase("queue") || command.equalsIgnoreCase("levellist")
						|| command.equalsIgnoreCase("list") || command.equals("requests")) {
					if(user.isMod(channel) || ("#" + user.toString()).equalsIgnoreCase(channel.toString())){
					StringBuilder message = new StringBuilder();
					for (int i = 0; i < Requests.levels.size(); i++) {
						message.append(i).append(1).append(": ").append(Requests.levels.get(i).getName()).append(" (").append(Requests.levels.get(i).getLevelID()).append("), ");
					}
					sendMessage(message, channel);
					}
					else {
						System.out.println(user.toString() + " " + channel.toString());
						sendMessage("This command is for mods, use !where or !position to find your position in the queue!", channel);
					}
				}

				// --------------------
				// Get request via command (Allows for smaller numbers
				// TODO: Level Name Requesting
				if (command.equalsIgnoreCase("p") || command.equalsIgnoreCase("where") || command.equalsIgnoreCase("position")) {
					for (int i = 0; i < Requests.levels.size(); i++) {
						if (Requests.levels.get(i).getRequester().equalsIgnoreCase(user.toString())) {
							int j = i + 1;

							Main.sendMessage("@" + user.toString() + ", " + Requests.levels.get(i).getName()
									+ " is at position " + j + " in the queue!");
							break;
						}
					}
				}
				if (command.equalsIgnoreCase("r") || command.equalsIgnoreCase("request") || command.equalsIgnoreCase("req")) {
					Matcher m = Pattern.compile("(\\d+)").matcher(arguments[1]);
					if (m.matches() && arguments.length <= 2) {
						try {
							Requests.addRequest(m.group(1), user.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						StringBuilder message = new StringBuilder();
						for (int i = 1; i < arguments.length; i++) {
							if (arguments.length - 1 == i) {
								message.append(arguments[i]);
							} else {
								message.append(arguments[i]).append(" ");
							}
						}
						System.out.println(message);
						if (message.toString().contains("by")) {
							String level = message.toString().split("by ")[0].toUpperCase();
							String username = message.toString().split("by ")[1];
							System.out.println(username);
							System.out.println(level);
							AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();
							try {
								outerloop: for (int j = 0; j < 10; j++) {
									Object[] levelPage = Objects.requireNonNull(client.getLevelsByUser(Objects.requireNonNull(client.searchUser(username).block()), j)
											.block()).asList().toArray();
									for (int i = 0; i < 10; i++) {
										if (((GDLevel) levelPage[i]).getName().toUpperCase()
												.startsWith(level.substring(0, level.length() - 1))) {
											Requests.addRequest(String.valueOf(((GDLevel) levelPage[i]).getId()),
													user.toString());
											break outerloop;
										}
									}
								}

							} catch (IndexOutOfBoundsException ignored) {
							} catch (MissingAccessException | IOException e) {
								sendMessage("@" + user.toString() + " That level or user doesn't exist!", channel);
								e.printStackTrace();
							}

						} else {

							AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();
							try {
								Requests.addRequest(String
										.valueOf(Objects.requireNonNull(client.searchLevels(message.toString(), LevelSearchFilters.create(), 0)
												.block()).asList().get(0).getId()),
										user.toString());
							} catch (MissingAccessException | IOException e) {
								sendMessage("@" + user.toString() + " That level doesn't exist!", channel);
								e.printStackTrace();
							}
						}

					}
				}

				// --------------------
				// TODO: Add rest of commands

				/*
				 * if (command.equals("info")) { return; String msgtosend = "Version " +
				 * BotHandler.getVersion() +
				 * " - Made by TreeHouseFalcon. A bot with a wide variety of tools and commands to help make streaming Geometry Dash easier!"
				 * ; if (BotHandler.isOp(this.bot.getUserBot())) { msgtosend = msgtosend +
				 * "(Download it here for free: http://adf.ly/1aufUr)"; }
				 * 
				 * this.bot.sendMessage(msgtosend); break; }
				 * 
				 * if (command.equals("unblockreqsfromuser")) { if (!arguments[1].equals("")) {
				 * //BotHandler.unblockUserFromRequesting(arguments[1].toLowerCase()); }
				 * 
				 * //this.bot.sendMessage(event.getUser().getNick() + " -> " +
				 * arguments[1].toLowerCase() + " has been unblocked from requesting levels.");
				 * }
				 * 
				 * if (command.equals("blockreqsfromuser")) { //if (!arguments[1].equals("") &&
				 * BotHandler.isOp(event.getUser())) {
				 * //BotHandler.blockUserFromRequesting(arguments[1].toLowerCase()); //}
				 * 
				 * //if (BotHandler.getBlockedUsers().contains(arguments[1].toLowerCase())) {
				 * //this.bot.sendMessage(event.getUser().getNick() + " -> " +
				 * arguments[1].toLowerCase() + " has been blocked from requesting levels.");
				 * return; //} } if (command.equals("streamerstats")) { if
				 * (BotHandler.getSetting(15).equals("")) { this.bot.sendMessage("/w " +
				 * event.getUser().getNick() + " -> An error has occured. (Username not set)");
				 * return; }
				 * 
				 * HttpURLConnection con = (HttpURLConnection) (new
				 * URL("http://www.boomlings.com/database/getGJUsers20.php")).openConnection();
				 * con.setRequestMethod("POST"); con.setDoOutput(true); String urlParams =
				 * "gameVersion=21&binaryVersion=33&str=" + BotHandler.getSetting(15) +
				 * "&page=0" + "&total=0" + "&secret=" + "Wmfd2893gb7"; DataOutputStream wr =
				 * new DataOutputStream(con.getOutputStream()); wr.writeBytes(urlParams);
				 * wr.flush(); wr.close(); BufferedReader in = new BufferedReader(new
				 * InputStreamReader(con.getInputStream())); StringBuffer response = new
				 * StringBuffer();
				 * 
				 * String thisLine; while ((thisLine = in.readLine()) != null) {
				 * response.append(thisLine); }
				 * 
				 * if (response.toString().equals("-1")) { return; }
				 * 
				 * in.close(); String[] parsedData = response.toString().split("[:]"); String[]
				 * parsedScoreData = this.getUserScoreData(parsedData[21]);
				 * 
				 * for (int i = 0; i < parsedData.length; ++i) { System.out.println(i + ": " +
				 * parsedData[i]); }
				 * 
				 * this.bot.sendMessage("/w " + parsedData[1] + "'s stats | Global Rank: " +
				 * parsedScoreData[39] + " | Stars: " + parsedData[23] + " | Secret Coins: " +
				 * parsedData[5] + " | User Coins: " + parsedData[7] + " | Demons: " +
				 * parsedData[27].split("[#]")[0]); } if (command.equals("disband") &&
				 * arguments.length == 1 && BotHandler.isSuperOp(event.getUser())) {
				 * this.bot.sendMessage(event.getUser().getNick() + " -> Shutting down...");
				 * System.exit(0); }
				 * 
				 * if (BotHandler.isOp(event.getUser())) { if
				 * (arguments[1].equalsIgnoreCase("off")) { BotHandler.setAreRequestsOn(false);
				 * this.bot.sendAction("-> Requests are now off!"); } else if
				 * (arguments[1].equalsIgnoreCase("on")) { BotHandler.setAreRequestsOn(true);
				 * this.bot.sendAction("-> Requests are now on!"); } }
				 */
			}
		}
	}
}
