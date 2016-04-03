package de.superioz.library.bukkit.common.protocol;

import lombok.Getter;

/**
 * Created on 02.04.2016.
 */
@Getter
public class WrappedChatComponent {

	private String text;
	private Object handle;

	/**
	 * Constructor for text to chat base
	 *
	 * @param text The text
	 */
	public WrappedChatComponent(String text){
		this.text = text;
		this.handle = ProtocolUtil.getChatBaseComponent(text);
	}

	/**
	 * Constructor for chatbase as handle
	 *
	 * @param chatBase The chatbase
	 */
	public WrappedChatComponent(Object chatBase){
		this.handle = chatBase;
		this.text = ProtocolUtil.getText(chatBase);
	}

}
