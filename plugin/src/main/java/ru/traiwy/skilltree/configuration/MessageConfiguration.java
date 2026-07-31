package ru.traiwy.skilltree.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class MessageConfiguration {
	 Component commandNotFound = Component.empty();
}
