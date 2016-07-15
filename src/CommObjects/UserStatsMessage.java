/*
 * The MIT License
 *
 * Copyright 2016 Aleks.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package CommObjects;

import Objects.Raptor;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author Aleks
 */
public class UserStatsMessage extends Message {

    private final Map<Raptor.RaptorSubsystem, Integer> subsystems = new EnumMap<>(Raptor.RaptorSubsystem.class);

    public UserStatsMessage(Map<Raptor.RaptorSubsystem, Integer> subsystems) {
        this.subsystems.putAll(subsystems);
    }

    public Map<Raptor.RaptorSubsystem, Integer> getSubsystems() {
        return Collections.unmodifiableMap(subsystems);
    }

}
