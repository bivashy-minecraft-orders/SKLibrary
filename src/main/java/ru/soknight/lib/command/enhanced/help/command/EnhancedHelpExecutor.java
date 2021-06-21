package ru.soknight.lib.command.enhanced.help.command;

import org.bukkit.command.CommandSender;
import ru.soknight.lib.argument.CommandArguments;
import ru.soknight.lib.command.enhanced.EnhancedExecutor;
import ru.soknight.lib.command.enhanced.help.HelpMessageFactory;
import ru.soknight.lib.command.enhanced.help.line.HelpLine;
import ru.soknight.lib.command.response.CommandResponseType;
import ru.soknight.lib.configuration.Messages;

import java.util.List;

public abstract class EnhancedHelpExecutor extends EnhancedExecutor {

	private final HelpMessageFactory factory;
	private final Messages messages;
	
	private String header, footer;
	private List<HelpLine> lines;
	
	public EnhancedHelpExecutor(Messages messages) {
		super(messages);
		
		this.factory = new HelpMessageFactory(messages, this::completeMessage);
		this.messages = messages;
	}
	
	protected HelpMessageFactory factory() {
		return factory;
	}

	protected void requirePermission(String permission) {
		super.setPermission(permission);
		super.setResponseMessageByKey(CommandResponseType.NO_PERMISSIONS, "error.no-permissions");
	}
	
	protected void completeMessage() {
		this.lines = factory.getMessageContent();
	}
	
	protected void sendHelpMessage(CommandSender receiver) {
		StringBuilder builder = new StringBuilder();
		
		if(header != null)
			builder.append(header);
		
		if(lines != null && !lines.isEmpty())
			lines.stream()
					.filter(l -> l.isAvailableFor(receiver))
					.map(HelpLine::format)
					.forEach(l -> builder.append("\n").append(l));
		
		if(footer != null)
			builder.append("\n").append(footer);
		
		receiver.sendMessage(builder.toString());
	}
	
	protected void setHeader(String header) {
		this.header = header;
	}
	
	protected void setHeaderFrom(String path) {
		this.header = messages.get(path);
	}
	
	protected void setFooter(String footer) {
		this.footer = footer;
	}
	
	protected void setFooterFrom(String path) {
		this.footer = messages.get(path);
	}
	
	@Override
	protected void executeCommand(CommandSender sender, CommandArguments args) {
		sendHelpMessage(sender);
	}

}
