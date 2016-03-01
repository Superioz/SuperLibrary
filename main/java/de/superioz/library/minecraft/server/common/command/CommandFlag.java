package de.superioz.library.minecraft.server.common.command;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 01.03.2016.
 */
@Getter
public class CommandFlag {

	public static final String SPECIFIER = "-";

	private String name;
	private boolean needArguments;
	private int maxArguments;
	private List<String> arguments;

	// default constructor
	public CommandFlag(String name, boolean needArguments, int maxArguments){
		this.needArguments = needArguments;
		this.name = name;
		this.maxArguments = maxArguments;
	}

	public CommandFlag(String name, boolean needArguments){
		this(name, needArguments, 1);
	}

	public CommandFlag(String name){
		this(name, false);
	}

	/**
	 * Sets the arguments for this flag
	 *
	 * @param arguments The arguments
	 */
	public void setArguments(String[] arguments){
		List<String> args = new ArrayList<>();

		// Check if flag consists of infinite arguments (e.g a name)
		if(maxArguments < 0){
			maxArguments = arguments.length;
		}

		// Get the flag arguments from given string array
		for(int i = 0; i < maxArguments; i++){
			String s = arguments[i];

			if(s.startsWith("-")) break;
			else args.add(s);
		}
		this.arguments = args;
	}

	/**
	 * Get the argument from this flag
	 *
	 * @param index The index
	 * @return The argument
	 */
	public String getArgument(int index){
		if(index < 1 || index > getArguments().size())
			index = 1;

		return getArguments().get(index - 1);
	}

	/**
	 * Check if the flag doesn't contain an argument
	 *
	 * @return The result as boolean
	 */
	public boolean isEmpty(){
		return getArguments().size() == 0;
	}

}
