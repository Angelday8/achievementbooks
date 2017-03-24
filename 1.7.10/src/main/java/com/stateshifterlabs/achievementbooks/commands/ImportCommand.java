package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Loader;
import com.stateshifterlabs.achievementbooks.data.compatibility.SA.SA;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class ImportCommand extends CommandBase {

	private Loader loader;
	private NetworkAgent networkAgent;
	private String dataDir;


	public ImportCommand(Loader loader, NetworkAgent networkAgent, String dataDir) {

		this.loader = loader;
		this.networkAgent = networkAgent;
		this.dataDir = dataDir;
	}

	@Override
	public String getCommandName() {
		return "import";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "import";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {

		if(!sender.getEntityWorld().isRemote) {
			SA importer = new SA(dataDir);
			Book newBook = importer.createElementList(importer.parseFormattings());

			importer.saveBook(newBook);
			loader.init();

			importer.parseSaveData(newBook, networkAgent);

			Item item = GameRegistry.findItem(MODID, newBook.itemName());
			sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName()).inventory
					.addItemStackToInventory(new ItemStack(item, 1));

			sender.addChatMessage(new ChatComponentText("Finished importing the achievement book."));
			sender.addChatMessage(new ChatComponentText(
					"New book file created in config/achievementbooks/imported_achievement_book.json"));
			sender.addChatMessage(new ChatComponentText("It's not going to be perfect, but gets the most of the job done."));
		} else {
			sender.addChatMessage(new ChatComponentText("Importing currently only works in Single Player mode."));
			sender.addChatMessage(new ChatComponentText("Change to SP and create your book there"));
		}

	}
}
