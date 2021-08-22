
/*
 * Copyright 2021 Yu Junyang
 * https://github.com/lowkeyfish
 *
 * This file is part of Sonar Intellij plugin.
 *
 * Sonar Intellij plugin is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Sonar Intellij plugin is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sonar Intellij plugin.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.yujunyang.intellij.plugin.sonar.common;

public class CallerStack extends Throwable {

	private static final long serialVersionUID = 1L;


	public CallerStack() {
		super("called from");
	}


	public CallerStack(final CallerStack cause) {
		super("called from", cause);
	}


	public static void initCallerStack(final Throwable throwable, final CallerStack callerStack) {
		Throwable lastCause = throwable;
		while (lastCause.getCause() != null) {
			lastCause = lastCause.getCause();
		}
		try {
			lastCause.initCause(callerStack);
		} catch (final IllegalStateException ignored) {
			// some exceptions may override getCause(), but not initCause()
			// => getCause() can be null, but cause is alreay set
		}
	}


}
