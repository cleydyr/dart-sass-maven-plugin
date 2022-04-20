package com.github.cleydyr.dart.command;

import com.github.cleydyr.dart.command.exception.SassCommandException;

public interface SassCommand {
    void execute() throws SassCommandException;
}
