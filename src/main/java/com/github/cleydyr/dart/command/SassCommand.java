package com.github.cleydyr.dart.command;

import com.github.cleydyr.dart.command.exception.SassCommandException;

public interface SassCommand {
	String execute() throws SassCommandException;
}
