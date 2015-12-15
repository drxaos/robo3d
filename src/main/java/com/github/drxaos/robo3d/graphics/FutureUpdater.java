/*
 * Copyright (C) 2014 Loris Durand, Guillaume Lesniak, Cristian Sanna,
 *                    Lucie Wiemert
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.drxaos.robo3d.graphics;

import java.util.ArrayList;
import java.util.List;

public class FutureUpdater {

    private List<FutureCallback> mFutureCallbacks;

    public FutureUpdater() {
        mFutureCallbacks = new ArrayList<FutureCallback>();
    }

    public void addFutureTimeCallback(FutureCallback cb) {
        synchronized (this) {
            mFutureCallbacks.add(cb);
        }
    }

    public void update(float tpf) {
        synchronized (this) {
            List<FutureCallback> callbacks = new ArrayList<FutureCallback>(mFutureCallbacks);
            for (FutureCallback cb : callbacks) {
                if (cb.decreaseTime(tpf)) {
                    mFutureCallbacks.remove(cb);
                }
            }
        }
    }
}
